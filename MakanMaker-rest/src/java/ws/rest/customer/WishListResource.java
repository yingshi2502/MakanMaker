/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.customer;

import ejb.session.stateless.WishListControllerLocal;
import entity.MealKitEntity;
import java.text.ParseException;
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
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.datamodel.mealkit.WishListResponse;

/**
 * REST Web Service
 *
 * @author yingshi
 */
@Path("wishList")
public class WishListResource {

    WishListControllerLocal wishListController = lookupWishListControllerLocal();

    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of WishListResource
     */
    public WishListResource() {
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWishListByCustomerId(@QueryParam("customerId")String customerId){
        WishListResponse rsp;
        try{
            Long id = Long.parseLong(customerId);
        
            List<MealKitEntity> mks = wishListController.getWishListByCustomerId(id, true);
        
            rsp = new WishListResponse("Retrieve success", true, mks);
            
        }catch(Exception ex){
            rsp = new WishListResponse(ex.getMessage(), false, null);
        }
        return Response.status(Response.Status.OK).entity(rsp).build();
    }

    private WishListControllerLocal lookupWishListControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (WishListControllerLocal) c.lookup("java:global/MakanMaker/MakanMaker-ejb/WishListController!ejb.session.stateless.WishListControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
   
    
}
