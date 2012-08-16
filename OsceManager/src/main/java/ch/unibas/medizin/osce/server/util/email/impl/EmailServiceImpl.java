/**
 * 
 */
package ch.unibas.medizin.osce.server.util.email.impl;

import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;



import ch.unibas.medizin.osce.client.util.email.EmailService;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author rahul
 *
 */
@SuppressWarnings("serial")
public class EmailServiceImpl extends RemoteServiceServlet implements EmailService {

	private JavaMailSender sender;
	
	public Boolean sendMail(final String[] toAddress, final String fromAddress, final String subject, final String message){
		
		MimeMessagePreparator preparator = null;
    	try{
    		Log.info("=======================Sending Mail Start=======================");
			
			preparator = new MimeMessagePreparator() {
				
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

					helper.setTo(toAddress);
					helper.setFrom(fromAddress);
					helper.setSubject(subject);
					helper.setText(message,true);
					
				}
			};
			
			
//			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
//			
//			senderImpl.setHost("smtp.spec-india.com");
//			senderImpl.setUsername("sankit");
//			senderImpl.setPassword("password1");
//			Log.info("Password not set=======================");
//			
//			Properties javaMailProperties = new Properties();
//			
//			javaMailProperties.put("mail.transport.protocol","smtp");
//			javaMailProperties.put("mail.smtp.auth",true);
//			javaMailProperties.put("mail.smtp.port",587);
//			javaMailProperties.put("mail.smtp.starttls.enable",true);
//			javaMailProperties.put("mail.debug",true);
//			
//			senderImpl.setJavaMailProperties(javaMailProperties);
			
//			Log.info("Java Mail Properties set=======================");
			
//			senderImpl.send(simpleMessage);
			
			sender.send(preparator);
			Log.info("=======================Mail Sent successfully=======================");
			Log.info("=======================Sending Mail End=======================");
			
			return true;
			
    	}catch (Exception e) {
    		Log.error(e.getMessage());
    		e.printStackTrace();
    		return false;
		}finally{
			preparator = null;
		}
    }

	public void setSender(JavaMailSender sender) {
		Log.info("sender = "+sender);
		this.sender = sender;
	}
	
}
