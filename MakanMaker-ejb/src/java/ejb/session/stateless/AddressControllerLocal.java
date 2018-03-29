/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmptyListException;
import util.exception.GeneralException;

/**
 *
 * @author yingshi
 */
@Local
public interface AddressControllerLocal {

    public AddressEntity createNewAddress(AddressEntity address, Long customerId) throws GeneralException;

    public void updateAddress(AddressEntity address);

    public void deleteAddress(Long addressId) throws GeneralException;

    public List<AddressEntity> retrieveAddressByCustomerId(Long customerId) throws EmptyListException;

    public AddressEntity getDefaultAddressById(Long customerId) throws EmptyListException;
    
}
