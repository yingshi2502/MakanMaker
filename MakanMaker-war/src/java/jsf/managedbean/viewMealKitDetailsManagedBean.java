/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealKitControllerLocal;
import ejb.session.stateless.ReviewControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import ejb.session.stateless.WishListControllerLocal;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.ReviewEntity;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.EmptyListException;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "viewMealKitDetailsManagedBean")
@ViewScoped
public class viewMealKitDetailsManagedBean implements Serializable {

    @EJB
    private WishListControllerLocal wishListControllerLocal;

    @EJB
    private ReviewControllerLocal reviewController;

    @EJB
    private ShoppingCartControllerLocal shoppingCartController;
    
    private int rating;
    private Integer quantityOfServing;
    
    @EJB
    private MealKitControllerLocal mealKitControllerLocal;

    private List<ReviewEntity> reviews;
    
    private Long mealKitId;
    private MealKitEntity mealKitEntityToView;
    
    private boolean addedToWishlist;
    private boolean addedToCart;
    
    private CustomerEntity currCustomer;

    /**
     * Creates a new instance of viewMealKitDetailsManagedBean
     */
    public viewMealKitDetailsManagedBean() {
        reviews = new ArrayList<>();
        quantityOfServing = 0;
        addedToCart = false;
        currCustomer = new CustomerEntity();
        addedToWishlist = false; //need to change here; add a judge function in WishListController
    }

    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String value = request.getParameter("id");
        setMealKitId(Long.valueOf(value));
        
        currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        if (currCustomer != null){
            addedToCart = shoppingCartController.checkItemExistence(currCustomer.getCustomerId(), mealKitId);
            addedToWishlist = wishListControllerLocal.checkItemExistence(currCustomer.getCustomerId(), mealKitId);
        }
                
        try {
            setMealKitEntityToView(mealKitControllerLocal.retrieveMealKitById(getMealKitId(),false));
            System.err.println("*****retrieved" + mealKitEntityToView.getName());

            reviews = reviewController.retrieveReviewByMealKitId(mealKitId,false);
            System.err.println("****review"+ reviews.size()+ reviews.get(0).getReview());
            rating = calculateRating();
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            setMealKitEntityToView(new MealKitEntity());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

//    public void back(ActionEvent event) throws IOException {
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllMealKits.xhtml");
//    }
//
//    public void updateMealKit(ActionEvent event) throws IOException {
//        System.err.println("********* mealKitEntityToView.getMealKitId(): " + mealKitEntityToView.getMealKitId());
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mealKitIdToUpdate", mealKitEntityToView.getMealKitId());
//        FacesContext.getCurrentInstance().getExternalContext().redirect("updateMealKit.xhtml");
//    }
//
//    public void deleteMealKit(ActionEvent event) throws IOException {
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mealKitIdToDelete", mealKitEntityToView.getMealKitId());
//        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteMealKit.xhtml");
//    }
    
    public void addToShoppingCart(ActionEvent event){
        if (currCustomer == null){
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "You haven't logged in.", null));
        }else{
            if (quantityOfServing == 0){
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a quantity.", null));
            }else{
                shoppingCartController.addItem(mealKitId, quantityOfServing, currCustomer.getShoppingCart().getShoppingCartId());
                FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to shopping Cart!", null));

            }
        }
        
    }
    
    public void addToWishlist(ActionEvent event){
       if (currCustomer == null){
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "You haven't logged in ", null));
        }else{
            wishListControllerLocal.addToWishList(currCustomer.getCustomerId(), mealKitId);
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to Wish list!", null));

       } 
    }
    
    private int calculateRating(){
        int sum = 0;
        if (reviews.isEmpty()) return -1;
        for (ReviewEntity r: reviews){
            sum += r.getRating();
        }
        return Math.round(sum / reviews.size());
    }
    
    public String getDate(Date date){
        SimpleDateFormat ft1= new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(date);
    }

    /**
     * @return the mealKitEntityToView
     */
    public MealKitEntity getMealKitEntityToView() {
        return mealKitEntityToView;
    }

    /**
     * @param mealKitEntityToView the mealKitEntityToView to set
     */
    public void setMealKitEntityToView(MealKitEntity mealKitEntityToView) {
        this.mealKitEntityToView = mealKitEntityToView;
    }

    /**
     * @return the mealKitId
     */
    public Long getMealKitId() {
        return mealKitId;
    }

    /**
     * @param mealKitId the mealKitId to set
     */
    public void setMealKitId(Long mealKitId) {
        this.mealKitId = mealKitId;
    }

    /**
     * @return the reviews
     */
    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    /**
     * @param reviews the reviews to set
     */
    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the quantityOfServing
     */
    public Integer getQuantityOfServing() {
        return quantityOfServing;
    }

    /**
     * @param quantityOfServing the quantityOfServing to set
     */
    public void setQuantityOfServing(Integer quantityOfServing) {
        this.quantityOfServing = quantityOfServing;
    }

    /**
     * @return the addedToWishlist
     */
    public boolean isAddedToWishlist() {
        return addedToWishlist;
    }

    /**
     * @param addedToWishlist the addedToWishlist to set
     */
    public void setAddedToWishlist(boolean addedToWishlist) {
        this.addedToWishlist = addedToWishlist;
    }

    /**
     * @return the addedToCart
     */
    public boolean isAddedToCart() {
        return addedToCart;
    }

    /**
     * @param addedToCart the addedToCart to set
     */
    public void setAddedToCart(boolean addedToCart) {
        this.addedToCart = addedToCart;
    }

}
