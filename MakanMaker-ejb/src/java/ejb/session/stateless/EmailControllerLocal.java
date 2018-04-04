/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;

/**
 *
 * @author yingshi
 */
@Local
public interface EmailControllerLocal {

    public Future<Boolean> emailPlaceOrder(OrderEntity order, String fromEmailAddress, String toEmailAddress) throws InterruptedException;

    public Future<Boolean> emailAprFool(String fromEmailAddress, String toEmailAddress, String name, String title, String content, String luokuan) throws InterruptedException;
    
}
