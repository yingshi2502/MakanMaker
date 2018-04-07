/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.customer;

import ejb.session.stateless.CustomerControllerLocal;
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
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import entity.CustomerEntity;
import rest.datamodel.customer.LoginResponse;
import util.exception.InvalidLoginCredentialException;
/**
 * REST Web Service
 *
 * @author yingshi
 */
@Path("login")
public class LoginResource {

    CustomerControllerLocal customerController = lookupCustomerControllerLocal();

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }
    
    
    
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("username") String username, @QueryParam("password") String password){
        LoginResponse rsp;
        try{
            
            CustomerEntity customer = customerController.customerLogin(username, password, true);
            System.err.println("****Login"+ customer.getMobile());
            customer.getAddresses().clear();
            customer.getOrderHistory().clear();
            customer.getTransactions().clear();
            customer.setShoppingCart(null);
            customer.setPassword(null);
            rsp = new LoginResponse(true, "Login Success", customer);
        }catch(InvalidLoginCredentialException ex){
            rsp = new LoginResponse(false, ex.getMessage(), null);
        }
        return Response.status(Response.Status.OK).entity(rsp).build();
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
