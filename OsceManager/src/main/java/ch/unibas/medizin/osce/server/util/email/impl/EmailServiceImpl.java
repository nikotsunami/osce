/**
 * 
 */
package ch.unibas.medizin.osce.server.util.email.impl;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import ch.unibas.medizin.osce.client.util.email.EmailService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author rahul
 *
 */
@SuppressWarnings("serial")
public class EmailServiceImpl extends RemoteServiceServlet implements EmailService {

	private static Logger log = Logger.getLogger(EmailServiceImpl.class); 
	
	private JavaMailSender sender;
	
	public EmailServiceImpl() {

//		ClassPathResource res = new ClassPathResource("META-INF/spring/applicationContext.xml");
//		XmlBeanFactory factory = new XmlBeanFactory(res);
//
//		this.sender = factory.getBean(JavaMailSender.class);
//
//		System.out.println("Sender == = =" + sender);
	}
	
	public Boolean sendMail(final String[] toAddress, final String fromAddress, final String subject, final String message){
		
		MimeMessagePreparator preparator = null;
    	try{
    		log.info("=======================Sending Mail Start=======================");
			
			preparator = new MimeMessagePreparator() {
				
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

					helper.setTo(toAddress);
					helper.setFrom(fromAddress);
					helper.setBcc(fromAddress);
					helper.setSubject(subject);
					helper.setText(message,true);
					
				}
			};
			
			
//			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
//			
//			senderImpl.setHost("smtp.spec-india.com");
//			senderImpl.setUsername("sankit");
//			senderImpl.setPassword("password1");
//			log.info("Password not set=======================");
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
			
//			log.info("Java Mail Properties set=======================");
			
//			senderImpl.send(simpleMessage);
			
			sender.send(preparator);
			log.info("=======================Mail Sent successfully=======================");
			log.info("=======================Sending Mail End=======================");
			
			return true;
			
    	}catch (Exception e) {
    		log.error(e.getMessage());
    		e.printStackTrace();
    		return false;
		}finally{
			preparator = null;
		}
    }

	public void setSender(JavaMailSender sender) {
		log.info("setSender");
		this.sender = sender;
	}
	
}
