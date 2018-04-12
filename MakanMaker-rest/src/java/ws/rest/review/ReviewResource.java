/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest.review;

import ejb.session.stateless.ReviewControllerLocal;
import entity.ReviewEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.datamodel.customer.MsgResponse;
import rest.datamodel.customer.RetrieveReviewsResponse;
import util.exception.EmptyListException;

/**
 * REST Web Service
 *
 * @author yingshi
 */
@Path("review")
public class ReviewResource {

    ReviewControllerLocal reviewController = lookupReviewControllerLocal();

    @Context
    private UriInfo context;

    
    
    /**
     * Creates a new instance of ReviewResource
     */
    public ReviewResource() {
    }

    /**
     * Retrieves representation of an instance of ws.rest.review.ReviewResource
     * @return an instance of java.lang.String
     */
    @Path("create/{mealKitId}/{orderId}")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(@PathParam("mealKitId") Long mealKitId, @PathParam("orderId") Long orderId, @QueryParam("reviewer") String reviewer, @QueryParam("rating") String rating, @QueryParam("content") String content) {
        MsgResponse rsp;

        try {
            Integer r = Integer.parseInt(rating);
            
            reviewController.createNewReiview(reviewer, orderId, mealKitId, content, r);
            rsp = new MsgResponse("Add Review Success", true);
            return Response.status(Response.Status.OK).entity(rsp).build();
            
        }catch (Exception ex) {
             System.err.println("****add review asdfasf" );

            rsp = new MsgResponse(ex.getMessage(), false);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rsp).build();
        }

    }
    
    @Path("retrieve")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReviews(@QueryParam("mealKitId") String mkId){
        RetrieveReviewsResponse rsp;
        List<ReviewEntity> reviews = new ArrayList<>();
        try {
            reviews = reviewController.retrieveReviewByMealKitId(Long.parseLong(mkId),true);
            rsp = new RetrieveReviewsResponse(reviews, "Retrieve Succeed", true);
            return Response.status(Response.Status.OK).entity(rsp).build();
        }catch(NumberFormatException ex){
            rsp = new RetrieveReviewsResponse(null, "Request format wrong", false);
            return Response.status(Response.Status.BAD_REQUEST).entity(rsp).build();
        } 
        catch (EmptyListException ex) {
            return Response.status(Response.Status.OK).entity(new RetrieveReviewsResponse(reviews, "No Reviews Added yet", true)).build();
        }
    }

    private ReviewControllerLocal lookupReviewControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ReviewControllerLocal) c.lookup("java:global/MakanMaker/MakanMaker-ejb/ReviewController!ejb.session.stateless.ReviewControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
