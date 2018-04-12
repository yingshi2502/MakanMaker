
package util.email;

import entity.OrderEntity;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author yingshi
 */
public class EmailManager {

    private final String emailServerName = "mailauth.comp.nus.edu.sg";
    private final String mailer = "JavaMailer";
    private String smtpAuthUser;
    private String smtpAuthPassword;

    public EmailManager() {
    }

    public EmailManager(String smtpAuthUser, String smtpAuthPassword) {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }
    public Boolean emailSummary(double price, String fromEmailAddress, String toEmailAddress){
         String emailBody = "";
        emailBody += "Dear Customer " + ",\n";
        emailBody += "  The summary of your order is below:" + "\n\n";
        emailBody += "Total Price: " +price + "\n";
        emailBody += "http://localhost:8080/MakanMaker-war/userProfile/myOrders.xhtml \n";

        emailBody += "Sincerely,\n";
        emailBody += "Makan Maker\n";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Orders Placed" + price);
                
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);
                System.out.println("**Email sent in Email Manager");

                return true;
            } else {
                return false;
            }
        } catch (MessagingException e) {
            e.printStackTrace();

            return false;
        }
    }
    public Boolean emailPlaceOrder(OrderEntity order, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "";
        emailBody += "Dear Customer " + order.getCustomer().getFullName() + ",\n";
        emailBody += "  The summary of your order is below:" + "\n\n";


        emailBody += "Sincerely,\n";
        emailBody += "Makan Maker\n";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Order Placed, order number" + order.getOrderId());
                
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);
                System.out.println("**Email sent in Email Manager");

                return true;
            } else {
                return false;
            }
        } catch (MessagingException e) {
            e.printStackTrace();

            return false;
        }
        
        
    }
        //refund order, create new Letter;

    
    public Boolean sendContactUs(String fromEmailAddress, String toEmailAddress, String title, String content, String questionType, String customerUsername) {
        String emailBody = "";
        emailBody += "From " +fromEmailAddress + ",\n\n";
        emailBody += content+"\n";
        emailBody += "Customer Username "+ customerUsername;
        

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject(title);
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);
                System.out.println("**Email sent in Email Manager");

                return true;
            } else {
                return false;
            }
        } catch (MessagingException e) {
            e.printStackTrace();

            return false;
        }
    }
}
