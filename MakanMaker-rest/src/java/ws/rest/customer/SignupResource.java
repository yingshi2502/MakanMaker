/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.customer;

import ejb.session.stateless.CustomerControllerLocal;
import entity.CustomerEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import rest.datamodel.customer.LoginResponse;
import rest.datamodel.customer.SignUpRequest;
import rest.datamodel.customer.SignUpResponse;
import util.exception.CustomerExistException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author yingshi
 */
@Path("signup")
public class SignupResource {

    CustomerControllerLocal customerController = lookupCustomerControllerLocal();

    @Context
    private UriInfo context;

    
    
    public SignupResource() {
    }

//    @Path("object")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response signupObject(JAXBElement<SignUpRequest> jaxbSignUpReq) {
//        SignUpResponse rsp;
//        if ((jaxbSignUpReq != null) && (jaxbSignUpReq.getValue() != null)) {
//            try {
//                SignUpRequest signUpReq = jaxbSignUpReq.getValue();
//                CustomerEntity customer = customerController.createNewCustomer(signUpReq.getCustomer(),true);
//                rsp = new SignUpResponse("Sign Up Success", true, customer);
    //  customer.setPassword(null);
//                return Response.status(Response.Status.OK).entity(rsp).build();
//            } catch (CustomerExistException | GeneralException ex) {
//                rsp = new SignUpResponse(ex.getMessage(), false, null);
//                return Response.status(Response.Status.OK).entity(rsp).build();
//            } catch (Exception ex) {
//                rsp = new SignUpResponse(ex.getMessage(), false, null);
//
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
//            }
//        } else {
//            rsp = new SignUpResponse("Invalid Request Message", false, null);
//            return Response.status(Response.Status.BAD_REQUEST).entity(rsp).build();
//        }
//    }

   // @Path("plain")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(@QueryParam("username") String username, @QueryParam("password") String password, @QueryParam("fullName") String fullName, @QueryParam("mobile") String mobile, @QueryParam("email") String email, @QueryParam("dob") String dob, @QueryParam("gener") String gender) {
        SignUpResponse rsp;

        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = sf.parse(dob);
            int g = gender.equals("1")?1:0;
            CustomerEntity customer = new CustomerEntity(username, fullName, mobile, email, password, dateOfBirth, g);
            customer = customerController.createNewCustomer(customer,true);
            customer.setPassword(null);
            System.err.println("****Signup"+customer.getCustomerId());
            rsp = new SignUpResponse("Sign Up Success", true, customer);
            return Response.status(Response.Status.OK).entity(rsp).build();
        } catch (CustomerExistException | GeneralException ex) {
                        System.err.println("****Signup GE");

            rsp = new SignUpResponse(ex.getMessage(), false, null);
            return Response.status(Response.Status.OK).entity(rsp).build();
        } catch (Exception ex) {
                        System.err.println("****Signup asdfasf" );

            rsp = new SignUpResponse(ex.getMessage(), false, null);
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
