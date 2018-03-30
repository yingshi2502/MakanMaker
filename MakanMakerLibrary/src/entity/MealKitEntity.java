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
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Integer nutrition;
   
    @Column(nullable = false)
    private String time;
    
    @Column(nullable = false)
    private String imagePath;
            
    private List<String> ingredients;
    
    private List<String> recipe;
    
    @Column(nullable = false)
    private boolean isAvailable;
    
    @OneToMany(mappedBy = "mealKit")
    private List<ReviewEntity> reviews;
    
    @OneToMany(mappedBy = "mealKit")
    private List<OrderEntity> orders;
    
    @ManyToMany
    private List<TagEntity> tags;
    
    public MealKitEntity(){
        this.price = 0.00;
    }

    public MealKitEntity(String name, Double price, boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }
    
    public MealKitEntity(String name, double price, List<String> ingredients, boolean isAvailable, Integer nutrition, String description, String time, String imagePath){
        this();
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.isAvailable = isAvailable;
        this.description = description;
        this.nutrition = nutrition;
        this.time = time;
        this.imagePath = imagePath;
    }
    
    public MealKitEntity(String name, double price, List<String> ingredients, boolean isAvailable, Integer nutrition, String description, String time, String imagePath, List<ReviewEntity> reviews){
        this();
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.isAvailable = isAvailable;
        this.reviews = reviews;
        this.description = description;
        this.nutrition = nutrition;
        this.time = time;
        this.imagePath = imagePath;
    }
    
     /**
     * @return the recipe
     */
    public List<String> getRecipe() {
        return recipe;
    }

    /**
     * @param recipe the recipe to set
     */
    public void setRecipe(List<String> recipe) {
        this.recipe = recipe;
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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the nutrition
     */
    public Integer getNutrition() {
        return nutrition;
    }

    /**
     * @param nutrition the nutrition to set
     */
    public void setNutrition(Integer nutrition) {
        this.nutrition = nutrition;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
}
