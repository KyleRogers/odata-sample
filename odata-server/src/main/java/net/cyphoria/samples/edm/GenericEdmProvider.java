package net.cyphoria.samples.edm;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
@Component
public class GenericEdmProvider extends CsdlAbstractEdmProvider {

    public static final String NAMESPACE = "net.cyphoria";

    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER_FQN = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    private static final String ES_ORDERS_NAME = "Order";
    private static final String ES_ORDER_LINE_ITEM_NAME = "OrderLineItem";

    private static final FullQualifiedName ET_ORDERS = new FullQualifiedName(NAMESPACE, ES_ORDERS_NAME);
    private static final FullQualifiedName ET_ORDER_LINE_ITEM = new FullQualifiedName(NAMESPACE, ES_ORDER_LINE_ITEM_NAME);

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
        return new CsdlEntityType()
                .setName(ET_ORDERS.getName())
                .setKey(singletonList(
                        new CsdlPropertyRef().setName("Id")))
                .setProperties(
                        asList(
                                new CsdlProperty().setName("Id").setType(EdmPrimitiveTypeKind.Int16.getFullQualifiedName()),
                                new CsdlProperty().setName("Model").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                                new CsdlProperty().setName("ModelYear").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())
                                        .setMaxLength(4),
                                new CsdlProperty().setName("Price").setType(EdmPrimitiveTypeKind.Decimal.getFullQualifiedName())
                                        .setScale(2),
                                new CsdlProperty().setName("Currency").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())
                                        .setMaxLength(3)
                        )
                ).setNavigationProperties(singletonList(
                        new CsdlNavigationProperty().setName("Manufacturer").setType(ET_ORDER_LINE_ITEM)
                )
        );
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
        return  new CsdlEntitySet()
                .setName(ES_ORDERS_NAME)
                .setType(ET_ORDERS)
                .setNavigationPropertyBindings(
                        singletonList(
                                new CsdlNavigationPropertyBinding().setPath("OrderLineItem").setTarget(
                                        CONTAINER_FQN.getFullQualifiedNameAsString() + "/" + ES_ORDER_LINE_ITEM_NAME)));

    }



    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {
        List<CsdlSchema> schemas = new ArrayList<>();
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);
        // EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<>();
        schema.setEntityTypes(entityTypes);

        // ComplexTypes
        List<CsdlComplexType> complexTypes = new ArrayList<>();
        schema.setComplexTypes(complexTypes);

        // EntityContainer
        schema.setEntityContainer(getEntityContainer());
        schemas.add(schema);

        return schemas;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataException {
        CsdlEntityContainer container = new CsdlEntityContainer();
        container.setName(CONTAINER_FQN.getName());

        // EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER_FQN, ES_ORDERS_NAME));
        container.setEntitySets(entitySets);

        return container;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
        if (entityContainerName == null || CONTAINER_FQN.equals(entityContainerName)) {
            return new CsdlEntityContainerInfo().setContainerName(CONTAINER_FQN);
        }
        return null;
    }
}
