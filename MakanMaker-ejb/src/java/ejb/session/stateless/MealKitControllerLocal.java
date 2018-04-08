/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.GeneralException;
import util.exception.MealKitExistException;
import util.exception.MealKitNotFoundException;
import util.exception.UndeletableException;

/**
 *
 * @author Summer
 */
@Local
public interface MealKitControllerLocal {

    public MealKitEntity createNewMealKit(MealKitEntity mealKit) throws MealKitExistException, GeneralException;

    public MealKitEntity updateMealKit(MealKitEntity mealKit) throws MealKitExistException, GeneralException;

    public MealKitEntity retrieveMealKitById(Long mealKitId) throws MealKitNotFoundException;

    public void deleteMealKit(MealKitEntity mealKit) throws UndeletableException;

    public List<MealKitEntity> retrieveAllMealKits();

    public List<MealKitEntity> searchMealKits(String keywords);

    public List<MealKitEntity> retrieveAvailableMealKits();
    
}
