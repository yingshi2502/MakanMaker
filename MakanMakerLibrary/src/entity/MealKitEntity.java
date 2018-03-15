/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author Ismahfaris
 */
@Entity
public class MealKitEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealKitId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double price;
//    
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private CategoryEnum category;
    
    private List<String> ingredients;
    @Column(nullable = false)
    private boolean isAvailable;
    
    @OneToMany(mappedBy = "mealKit")
    private List<ReviewEntity> reviews;
    
    @ManyToMany
    private List<OrderEntity> orders;
    
    @ManyToMany
    private List<TagEntity> tags;
    
    public MealKitEntity(){
        
    }

    public MealKitEntity(String name, Double price, boolean isAvailable) {
        this.name = name;
        this.price = price;
       // this.category = category;
        this.isAvailable = isAvailable;
    }
    
    
    public MealKitEntity(String name, double price, List<String> ingredients, boolean isAvailable, List<ReviewEntity> reviews){
        this();
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
       // this.category = category;
        this.isAvailable = isAvailable;
        this.reviews = reviews;
        
    }
    
    public Long getMealKitId() {
        return mealKitId;
    }

    public void setMealKitId(Long mealKitId) {
        this.mealKitId = mealKitId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getMealKitId() != null ? getMealKitId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MealKitEntity)) {
            return false;
        }
        MealKitEntity other = (MealKitEntity) object;
        if ((this.getMealKitId() == null && other.getMealKitId() != null) || (this.getMealKitId() != null && !this.mealKitId.equals(other.mealKitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MealKitEntity[ id=" + getMealKitId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the ingredients
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

//    /**
//     * @return the category
//     */
//    public CategoryEnum getCategory() {
//        return category;
//    }
//
//    /**
//     * @param category the category to set
//     */
//    public void setCategory(CategoryEnum category) {
//        this.category = category;
//    }

    /**
     * @return the isAvailable
     */
    public boolean isIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
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
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    /**
     * @return the tags
     */
    public List<TagEntity> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }
    
}
