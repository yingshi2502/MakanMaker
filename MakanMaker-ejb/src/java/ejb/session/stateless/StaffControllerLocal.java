/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ManagerEntity;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.ManagerNotFoundException;

/**
 *
 * @author yingshi
 */
@Local
public interface StaffControllerLocal {

    public ManagerEntity retrieveManagerByUsername(String username) throws ManagerNotFoundException;

    public ManagerEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
