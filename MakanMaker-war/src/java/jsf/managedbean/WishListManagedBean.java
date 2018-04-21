/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.WishListControllerLocal;
import entity.CustomerEntity;
import entity.MealKitEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author yingshi
 */
@Named(value = "wishListManagedBean")
@ViewScoped
public class WishListManagedBean implements Serializable {

    @EJB(name = "WishListControllerLocal")
    private WishListControllerLocal wishListControllerLocal;

    private List<MealKitEntity> wishListMealKits;
    private Long customerId;

    public WishListManagedBean() {
        wishListMealKits = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        CustomerEntity currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        System.err.println("***" + currCustomer);

        try {
            customerId = currCustomer.getCustomerId();
            wishListMealKits = wishListControllerLocal.getWishListByCustomerId(getCustomerId(),false);
        } catch (NullPointerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Login", null));
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

                FacesContext.getCurrentInstance().getExternalContext().redirect("/MakanMaker-war/index.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(WishListManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public void deleteFromWishList(ActionEvent event) {
        Long toDelete = ((MealKitEntity) event.getComponent().getAttributes().get("toDelete")).getMealKitId();
        wishListControllerLocal.deleteFromWishList(customerId, toDelete);
        wishListMealKits = wishListControllerLocal.getWishListByCustomerId(getCustomerId(),false);
    }

    public void addToShoppingCart(ActionEvent event) {
        Long toAdd = ((MealKitEntity) event.getComponent().getAttributes().get("toAdd")).getMealKitId();
        wishListControllerLocal.addWishListItemToShoppingCart(customerId, toAdd);
        wishListMealKits = wishListControllerLocal.getWishListByCustomerId(getCustomerId(),false);
    }

    /**
     * @return the wishListMealKits
     */
    public List<MealKitEntity> getWishListMealKits() {
        return wishListMealKits;
    }

    /**
     * @param wishListMealKits the wishListMealKits to set
     */
    public void setWishListMealKits(List<MealKitEntity> wishListMealKits) {
        this.wishListMealKits = wishListMealKits;
    }

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

}
