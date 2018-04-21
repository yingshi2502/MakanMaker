/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.mealkit;

import ejb.session.stateless.MealKitControllerLocal;
import entity.MealKitEntity;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import rest.datamodel.customer.MsgResponse;
import rest.datamodel.mealkit.MealKitListResponse;
import rest.datamodel.mealkit.MealKitRequest;
import rest.datamodel.mealkit.MealKitResponse;

/**
 * REST Web Service
 *
 * @author Ismahfaris
 */
@Path("mealkit")
public class mealKitResource {

    MealKitControllerLocal mealKitController = lookupMealKitControllerLocal();

    @Context
    private UriInfo context;


    public mealKitResource() {
    }

    @Path("retrieveMealKitById")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMealKitById(@QueryParam("mealKitId") Long mealKitId){
        MealKitResponse rsp;
        try{
            System.err.println("mealKitResource.retrieveMealKitById: ");  
            MealKitEntity mk = mealKitController.retrieveMealKitById(mealKitId,true);
               
            
            if (mk != null) rsp = new MealKitResponse("Success Retreive", true, mk);
            else rsp = new MealKitResponse("No Such MealKit", false, null);
            return Response.status(Response.Status.OK).entity(rsp).build();
        
        }
        catch(Exception ex)
        {
            rsp = new MealKitResponse(ex.getMessage(), false, null);
            System.err.println("mealKitResource.retrieveMealKitById: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
        
    }

    @Path("retrieveAllMealKits")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMealKits()
    {
        System.err.println("********** retrieveAllMealKits");
        
        MealKitListResponse rsp;
        try
        {
            
            List<MealKitEntity> mealKits = mealKitController.retrieveAvailableMealKits(true);
            rsp = new MealKitListResponse("Succeed Retrieve", true, mealKits);
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new MealKitListResponse(ex.getMessage(), false, null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }
    
    @Path("retrieveAvailableMealKits")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAvailableMealKits()
    {
        MealKitListResponse rsp;
        try
        {
            
            List<MealKitEntity> mealKits = mealKitController.retrieveAvailableMealKits(true);
            rsp = new MealKitListResponse("Succeed Retrieve", true, mealKits);
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new MealKitListResponse(ex.getMessage(), false, null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }    
    
    
    @Path("searchMealKits")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMealKits(@QueryParam("keywords") String keywords)
    {
        MealKitListResponse rsp;
        try
        {
            
            List<MealKitEntity> mealKits = mealKitController.searchMealKits(keywords, true);
            rsp = new MealKitListResponse("Succeed Retrieve", true, mealKits);
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new MealKitListResponse(ex.getMessage(), false, null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }      
    

   /* @Path("delete")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMealKit(@QueryParam("mealKitEntity") MealKitEntity mealKitEntity)
    {
        MsgResponse rsp;
        try
        {     
            mealKitController.deleteMealKit(mealKitEntity);
            
            rsp = new MsgResponse("Delete Successfully", true);
            
            return Response.status(Response.Status.OK).entity(rsp).build();
        }
        catch(Exception ex)
        {
            rsp = new MsgResponse(ex.getMessage(), false);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }
    }
*/

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMealKit(JAXBElement<MealKitRequest> jaxbMealKitRequest){
        
        MsgResponse rsp;
        
        if((jaxbMealKitRequest != null) && (jaxbMealKitRequest.getValue() != null))
        {
            try
            {
                MealKitRequest mealKitReq = jaxbMealKitRequest.getValue();               
                
                mealKitController.updateMealKit(mealKitReq.getMealKit());
                
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
    
    
    private MealKitControllerLocal lookupMealKitControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MealKitControllerLocal) c.lookup("java:global/MakanMaker/MakanMaker-ejb/MealKitController!ejb.session.stateless.MealKitControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
