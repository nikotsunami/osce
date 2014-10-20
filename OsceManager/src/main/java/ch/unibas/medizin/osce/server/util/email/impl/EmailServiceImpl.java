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
	
	private String fromAddress;

	private String subject;
	
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
					
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"UTF-8");

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
/**
 * This method is used to send email. This is added when I need to send email to standardized patients when their entry is created in osce to notify them that 
 * now they can use sp-portal by using sent link.
 * @param toAddress
 * @param message
 * @return
 */
	public Boolean sendMail(final String[] toAddress, final String message) {
		
		MimeMessagePreparator preparator = null;
    	try{
    		log.info("=======================Sending Mail Start=======================");
			
			preparator = new MimeMessagePreparator() {
				
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
					
					helper.setTo(toAddress);
					helper.setCc(sendCopy);
					helper.setFrom(fromAddress);
					helper.setSubject(subject);
					helper.setText(message,true);
					
				}
			};
			
			sender.send(preparator);
			log.info("=======================Mail Sent successfully=======================");
			log.info("=======================Sending Mail End=======================");
			
			return true;
			
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
    		return false;
		}finally{
			preparator = null;
		}
    }
	
public Boolean sendMail(final String[] toAddress, final String subject,final String message) {

		MimeMessagePreparator preparator = null;
		try {
			log.info("=======================Sending Mail Start=======================");

			preparator = new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {

					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
					helper.setTo(toAddress);
					helper.setFrom(fromAddress);
					helper.setSubject(subject);
					helper.setText(message, true);

				}
			};

			sender.send(preparator);
			log.info("=======================Mail Sent successfully=======================");
			log.info("=======================Sending Mail End=======================");

			return true;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			preparator = null;
		}
	}

	public void setSender(JavaMailSender sender) {
		log.info("setSender");
		this.sender = sender;
	}
	
	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
