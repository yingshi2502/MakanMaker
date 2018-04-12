/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.customer;

import ejb.session.stateless.AddressControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.datamodel.customer.AddressResponse;
import rest.datamodel.customer.AddressListResponse;
import rest.datamodel.customer.CustomerResponse;

/**
 * REST Web Service
 *
 * @author yingshi
 */
@Path("customer")
public class CustomerResource {

    CustomerControllerLocal customerController = lookupCustomerControllerLocal();

    
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
    }
    
    @Path("{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomer(@PathParam("customerId") Long customerId)
    {
        CustomerResponse rsp;
        try
        {
            
            CustomerEntity customer = customerController.retrieveCustomerById(customerId,true);
            customer.setPassword(null);
            rsp = new CustomerResponse("Retrieve Success", true, customer);
//            AddressEntity address = addressController.retrieveAddressById(addressId, true);
//            
//            if (address != null) rsp = new AddressResponse("Success Retreive", true, address);
//            else rsp = new AddressResponse("No Such Address", false, null);
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new CustomerResponse(ex.getMessage(), false, null);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }

    private CustomerControllerLocal lookupCustomerControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerControllerLocal) c.lookup("java:global/MakanMaker/MakanMaker-ejb/CustomerController!ejb.session.stateless.CustomerControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
    
}
