package net.cyphoria.samples.edm;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
public class GenericCollectionProcessor implements EntityCollectionProcessor {
    private OData odata;
    private ServiceMetadata edm;

    @Override
    public void readEntityCollection(ODataRequest request,
                                     ODataResponse response,
                                     UriInfo uriInfo,
                                     ContentType requestedContentType) throws ODataApplicationException, ODataLibraryException {
        // First we have to figure out which entity set to use
        final EdmEntitySet edmEntitySet = getEdmEntitySet(uriInfo.asUriInfoResource());

        // Second we fetch the data for this specific entity set from the mock database and transform it into an EntitySet
        // object which is understood by our serialization
        EntityCollection entitySet = new EntityCollection(); //dataProvider.readAll(edmEntitySet); // TODO
        entitySet.getEntities().add(new Entity()
                .addProperty(new Property(null, "Price", ValueType.PRIMITIVE, new BigDecimal("234.3"))));

        // Next we create a serializer based on the requested format. This could also be a custom format but we do not
        // support them in this example
        ODataSerializer serializer = odata.createSerializer(requestedContentType);

        // Now the content is serialized using the serializer.
        final ExpandOption expand = uriInfo.getExpandOption();
        final SelectOption select = uriInfo.getSelectOption();
        InputStream serializedContent = serializer.entityCollection(edm, edmEntitySet.getEntityType(), entitySet,
                EntityCollectionSerializerOptions.with()
                        .contextURL(isODataMetadataNone(requestedContentType) ? null :
                                getContextUrl(edmEntitySet, false, expand, select, null))
                        .count(uriInfo.getCountOption())
                        .expand(expand).select(select)
                        .build()).getContent();

        // Finally we set the response data, headers and status code
        response.setContent(serializedContent);
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toContentTypeString());
    }

    private EdmEntitySet getEdmEntitySet(final UriInfoResource uriInfo) throws ODataApplicationException {
        final List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
    /*
     * To get the entity set we have to interpret all URI segments
     */
        if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Invalid resource type for first segment.",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }

    /*
     * Here we should interpret the whole URI but in this example we do not support navigation so we throw an exception
     */

        final UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths.get(0);
        return uriResource.getEntitySet();
    }

    private ContextURL getContextUrl(final EdmEntitySet entitySet, final boolean isSingleEntity,
                                     final ExpandOption expand, final SelectOption select, final String navOrPropertyPath)
            throws SerializerException {

        return ContextURL.with().entitySet(entitySet)
                .selectList(odata.createUriHelper().buildContextURLSelectList(entitySet.getEntityType(), expand, select))
                .suffix(isSingleEntity ? ContextURL.Suffix.ENTITY : null)
                .navOrPropertyPath(navOrPropertyPath)
                .build();
    }

    public static boolean isODataMetadataNone(final ContentType contentType) {
        return contentType.isCompatible(ContentType.APPLICATION_JSON)
                && ContentType.VALUE_ODATA_METADATA_NONE.equals(contentType.getParameter(ContentType.PARAMETER_ODATA_METADATA));
    }


    @Override
    public void init(OData odata, ServiceMetadata edm) {
        this.odata = odata;
        this.edm = edm;
    }
}
