/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.ManagerEntity;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.ManagerNotFoundException;
import util.helperClass.SecurityHelper;
/**
 *
 * @author yingshi
 */
@Stateless
public class StaffController implements StaffControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    @Override
    public ManagerEntity staffLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            ManagerEntity manager = retrieveManagerByUsername(username);
            
            if (SecurityHelper.verifyPassword(password, manager.getPassword())){
                return manager;
            }else{
                throw new InvalidLoginCredentialException("Wrong password!");
            }
        } catch (ManagerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!"+ex.getMessage());
        }
    }
    
    @Override
    public ManagerEntity retrieveManagerByUsername(String username) throws ManagerNotFoundException {
        Query query = em.createQuery("SELECT m FROM ManagerEntity m WHERE m.userName = :username");
        query.setParameter("username", username);
        try{
            ManagerEntity manager = (ManagerEntity) query.getSingleResult();
            return manager;
        }catch(NoResultException ex){
            
        }
        return null;
    }


    
}
