/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.TagEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.GeneralException;
import util.exception.TagExisitException;

/**
 *
 * @author yingshi
 */
@Local
public interface TagControllerLocal {

    TagEntity createNewTag(TagEntity newTag) throws TagExisitException,GeneralException;

    public List<MealKitEntity> retrieveMealKitsByTags(List<TagEntity> tags);

    public void linkTagAndMealKit(Long tagId, Long mealKitId);

    
}
