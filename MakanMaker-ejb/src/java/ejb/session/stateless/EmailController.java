/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import util.email.EmailManager;

/**
 *
 * @author yingshi
 */
@Stateless
public class EmailController implements EmailControllerLocal {

    private EmailManager emailManager = new EmailManager("yingshi", "Llcnnzcxa52");

    @Asynchronous
    @Override
    public Future<Boolean> emailPlaceOrder(OrderEntity order, String fromEmailAddress, String toEmailAddress) throws InterruptedException {
        System.err.println("***Going to send emailWonNotification Async");
        Boolean result = emailManager.emailPlaceOrder(order, fromEmailAddress, toEmailAddress);
        return new AsyncResult<>(result);
    }

    @Asynchronous
    @Override
    public Future<Boolean> emailAprFool(String fromEmailAddress, String toEmailAddress, String name, String title, String content, String luokuan) throws InterruptedException {
        System.err.println("***Going to send emailOutOfBidNotification Async");
        Boolean result = emailManager.AprFool(fromEmailAddress, toEmailAddress, name, title, content, luokuan);
        return new AsyncResult<>(result);
    }

}

