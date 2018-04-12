/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.OrderEntity;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.email.EmailManager;

/**
 *
 * @author yingshi
 */
@Stateless
public class EmailController implements EmailControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

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
    public Future<Boolean> emailSummary(double totalAmount, String fromEmailAddress, String toEmailAddress) throws InterruptedException {
        System.err.println("***Going to send emailWonNotification Async");
        Boolean result = emailManager.emailSummary(totalAmount, fromEmailAddress, toEmailAddress);
        return new AsyncResult<>(result);
    }

    /**
     *
     * @param fromEmailAddress
     * @param subject
     * @param content
     * @param typeOfQuestion
     * @param isMMAcct
     * @param username
     * @return
     * @throws InterruptedException
     */
    @Asynchronous
    @Override
    public Future<Boolean> emailSendContactUs(String fromEmailAddress, String subject, String content, String typeOfQuestion, boolean isMMAcct) throws InterruptedException {
        System.err.println("***Going to send emailWonNotification Async");
        String username = "Anonymous";
        if (isMMAcct) {
            Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :email");
            query.setParameter("email", fromEmailAddress);
            CustomerEntity customer = (CustomerEntity) query.getSingleResult();
            if (customer != null) {
                username = customer.getUserName();
            }
        }

        Boolean result = emailManager.sendContactUs(fromEmailAddress, "huangyingshi@gmail.com", subject, content, typeOfQuestion, username);

        return new AsyncResult<>(result);
    }

}
