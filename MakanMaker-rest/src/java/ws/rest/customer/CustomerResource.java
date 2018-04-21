/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.customer;

import ejb.session.stateless.CustomerControllerLocal;
import entity.CustomerEntity;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.datamodel.customer.CustomerResponse;
import rest.datamodel.customer.MsgResponse;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

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
            System.err.println("****CustomerResource.retrieveCustomer(): Customer Retrieval ends");
            customer.setPassword(null);
            System.err.println("****CustomerResource.retrieveCustomer(): Customer Retrieval of customer id: " + customer.getCustomerId() + " ends");
            System.err.println("****CustomerResource.retrieveCustomer(): Customer details: " + customer.getUserName() + ", " + customer.getPassword() + ", " + customer.getEmail() + ".");
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
    

    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@QueryParam("username") String username, @QueryParam("password") String password, @QueryParam("fullName") String fullName, @QueryParam("email") String email, @QueryParam("gender") int gender, @QueryParam("dateOfBirth") String dateOfBirth, @QueryParam("mobile") String mobile) {
        System.err.println("********** CustomerResource.updateCustomer(): Method Begins");
        if((fullName != null) && (email != null) && (gender != -1) && (dateOfBirth != null) && (mobile != null) && (username != null) && (password != null))
        {
            try
            {
                System.err.println("********** CustomerResource.updateCustomer(): Begins");
                CustomerEntity customer = customerController.customerLogin(username, password, true);
                System.err.println("********** CustomerResource.updateCustomer(): Customer " + customer.getUserName() + " login remotely via web service");
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                
                customer.setFullName(fullName);
                customer.setEmail(email);
                customer.setGender(gender);
                System.err.println("********** CustomerResource.updateCustomer(): Date of Birth is: " + dateOfBirth);
                customer.setDateOfBirth(formatter.parse(dateOfBirth));
                customer.setMobile(mobile);
                
                CustomerEntity updatedCustomer=customerController.updateCustomer(customer,true);
                System.err.println("********** CustomerResource.updateCustomer(): Customer " + updatedCustomer.getUserName() + " has been saved successfully");
                
                return Response.status(Response.Status.OK).entity(new MsgResponse("Customer updated successfully", true)).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                System.err.println("********** CustomerResource.updateCustomer(): " + ex.getMessage());

                MsgResponse msgRsp = new MsgResponse(ex.getMessage(), false);
            
                return Response.status(Response.Status.UNAUTHORIZED).entity(msgRsp).build();
            }
            catch(CustomerNotFoundException ex)
            {
                System.err.println("********** CustomerResource.updateCustomer(): " + ex.getMessage());

                MsgResponse msgRsp = new MsgResponse(ex.getMessage(), false);
                
                return Response.status(Response.Status.BAD_REQUEST).entity(msgRsp).build();
            }
            catch(Exception ex)
            {
                System.err.println("********** CustomerResource.updateCustomer(): " + ex.getMessage());

                MsgResponse msgRsp = new MsgResponse(ex.getMessage(), false);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msgRsp).build();
            }
        }
        else
        {
            
            MsgResponse msgRsp = new MsgResponse("Invalid update customer request", false);
            
            return Response.status(Response.Status.BAD_REQUEST).entity(msgRsp).build();
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
