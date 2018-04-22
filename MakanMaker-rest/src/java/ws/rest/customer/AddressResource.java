/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.customer;

import ejb.session.stateless.AddressControllerLocal;
import entity.AddressEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import rest.datamodel.customer.AddressListResponse;
import rest.datamodel.customer.AddressRequest;
import rest.datamodel.customer.AddressResponse;
import util.exception.GeneralException;
import rest.datamodel.customer.MsgResponse;


/**
 * REST Web Service
 *
 * @author yingshi
 */
@Path("address")
public class AddressResource {

    AddressControllerLocal addressController = lookupAddressControllerLocal();

    @Context
    private UriInfo context;

    
    /**
     * Creates a new instance of AddressResource
     */
    public AddressResource() {
    }

    @Path("retrieveById/{addressId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAddress(@PathParam("addressId") Long addressId)
    {
        AddressResponse rsp;
        try
        {
            AddressEntity address = addressController.retrieveAddressById(addressId, true);
            if (address != null) rsp = new AddressResponse("Success Retrieve", true, address);
            else rsp = new AddressResponse("No Such Address", false, null);
            return Response.status(Response.Status.OK).entity(rsp).build();
        
        }
        catch(Exception ex)
        {
            rsp = new AddressResponse(ex.getMessage(), false, null);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }
    
    @Path("retrieveAddressByCustomerId")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAddressByCustomerId(@QueryParam("customerId") String customerId)
    {
        AddressListResponse rsp;
        try
        {
            Long cid = Long.parseLong(customerId);
            List<AddressEntity> addresses = addressController.retrieveAddressByCustomerId(cid,true);
            rsp = new AddressListResponse("Succeed Retrieve", true, addresses);
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new AddressListResponse(ex.getMessage(), false, null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }
    
    @Path("create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAddress(JAXBElement<AddressRequest> jaxbAddressRequest){
        System.err.println("*********AddressResource.createAddress()" + jaxbAddressRequest.getValue());
        AddressResponse rsp;
        
        if((jaxbAddressRequest != null) && (jaxbAddressRequest.getValue() != null))
        {
            try
            {
                AddressRequest addressReq = jaxbAddressRequest.getValue();               
                
                AddressEntity newAdd = addressController.createNewAddress(addressReq.getAddress(), addressReq.getCustomerId(),true);
                System.err.println("*********AddressResource.createAddress(): addressId is: " + newAdd.getAddressId());

                rsp = new AddressResponse("Create Successful", true, newAdd);
                return Response.status(Response.Status.OK).entity(rsp).build();
            }
            
            catch(GeneralException ex)
            {
                rsp = new AddressResponse(ex.getMessage(), false, null);
                
                return Response.status(Response.Status.OK).entity(rsp).build();
            }
            catch(Exception ex)
            {
                 rsp = new AddressResponse(ex.getMessage(), false, null);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
            }
        }
        else
        {
            rsp = new AddressResponse("Invalid create new product request", false, null);
            
            return Response.status(Response.Status.BAD_REQUEST).entity(rsp).build();
        }
    }
    
    @Path("update")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAddress(@QueryParam("customerId") String customerId, @QueryParam("addressId") String addressId, @QueryParam("streetAddress") String streetAddress, @QueryParam("floorUnit") String floorUnit, @QueryParam("postalCode") String postalCode, @QueryParam("fullName") String fullName, @QueryParam("phoneNumber") String phoneNumber, @QueryParam("isDefaultShipping") boolean isDefaultShipping, @QueryParam("isDefaultBilling") boolean isDefaultBilling){
        
        MsgResponse rsp;
        
        if((customerId != null) && (addressId != null) && (streetAddress != null) && (floorUnit != null) && (postalCode != null) && (fullName != null) && (phoneNumber != null))
        {
            try
            {
                AddressEntity addressToUpdate = new AddressEntity();
                List<AddressEntity> addresses = addressController.retrieveAddressByCustomerId(Long.parseLong(customerId),true);
                for(AddressEntity address: addresses) {
                    if(address.getAddressId() == Long.parseLong(addressId)) {
                        address.setFloorUnit(floorUnit);
                        address.setFullName(fullName);
                        address.setPhoneNumber(phoneNumber);
                        address.setPostalCode(postalCode);
                        address.setStreetAddress(streetAddress);
                        address.setIsDefaultBilling(isDefaultBilling);
                        address.setIsDefaultShipping(isDefaultShipping);
                        addressToUpdate = address;
                        break;
                    }
                }
                
                addressController.updateAddress(addressToUpdate);
                
                rsp = new MsgResponse("Update Successful", true);
                return Response.status(Response.Status.OK).entity(rsp).build();
            }
            
            catch(Exception ex)
            {
                 rsp = new MsgResponse(ex.getMessage(), false);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
            }
        }
        else
        {
            rsp = new MsgResponse("Invalid update product request", false);
            
            return Response.status(Response.Status.BAD_REQUEST).entity(rsp).build();
        }
    }
    
    
    
    
    @Path("delete")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAddress(@QueryParam("addressId") String addressId)
    {
        MsgResponse rsp;
        try
        {
            Long aid = Long.parseLong(addressId);
            
            addressController.deleteAddress(aid);
            
            rsp = new MsgResponse("Delete Successfully", true);
            
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new MsgResponse(ex.getMessage(), false);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }
    

    private AddressControllerLocal lookupAddressControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AddressControllerLocal) c.lookup("java:global/MakanMaker/MakanMaker-ejb/AddressController!ejb.session.stateless.AddressControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
