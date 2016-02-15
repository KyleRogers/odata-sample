package net.cyphoria.samples.controller;

import net.cyphoria.samples.edm.GenericCollectionProcessor;
import net.cyphoria.samples.edm.GenericEdmProvider;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.edmx.EdmxReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
@RestController
@RequestMapping("odata")
public class ODataController {

    /**
     * The ctx.
     */
    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private GenericEdmProvider edmProvider;

    @RequestMapping(value = "/**")
    public void process(final HttpServletRequest req, final HttpServletResponse resp) {
        final OData odata = OData.newInstance();
        final ServiceMetadata edm = odata.createServiceMetadata(edmProvider, new ArrayList<EdmxReference>());


        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req) {
            @Override
            public String getServletPath() {
                return "/odata";
            }
        };
        ODataHttpHandler handler = odata.createHandler(edm);
        handler.register(new GenericCollectionProcessor());

        // let the handler do the work
        handler.process(wrappedRequest, resp);
    }

}
