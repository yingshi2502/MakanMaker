
package ws.rest.customer;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author yingshi
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.rest.customer.AddressResource.class);
        resources.add(ws.rest.customer.CustomerResource.class);
        resources.add(ws.rest.customer.LoginResource.class);
        resources.add(ws.rest.customer.SignupResource.class);
        resources.add(ws.rest.review.ReviewResource.class);
    }

}
