
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
import util.enumeration.TagCategoryEnum;

/**
 *
 * @author yingshi
 */
@Entity
public class TagEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private TagCategoryEnum tagCategory;
    
    @ManyToMany(mappedBy = "tags")
    private List<MealKitEntity> mealKits;

    public TagEntity() {
    }

    public TagEntity(String name, TagCategoryEnum tagCategory) {
        this.name = name;
        this.tagCategory = tagCategory;
    }   
    
    
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tagId != null ? tagId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tagId fields are not set
        if (!(object instanceof TagEntity)) {
            return false;
        }
        TagEntity other = (TagEntity) object;
        if ((this.tagId == null && other.tagId != null) || (this.tagId != null && !this.tagId.equals(other.tagId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TagEntity[ id=" + tagId + " ]";
    }
    
    /**
     * @return the tagCategory
     */
    public TagCategoryEnum getTagCategory() {
        return tagCategory;
    }

    /**
     * @param tagCategory the tagCategory to set
     */
    public void setTagCategory(TagCategoryEnum tagCategory) {
        this.tagCategory = tagCategory;
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
     * @return the mealKits
     */
    public List<MealKitEntity> getMealKits() {
        return mealKits;
    }

    /**
     * @param mealKits the mealKits to set
     */
    public void setMealKits(List<MealKitEntity> mealKits) {
        this.mealKits = mealKits;
    }

}
