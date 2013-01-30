/**
 * 
 */
package ch.unibas.medizin.osce.client.util.email;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rahul
 *
 */
@RemoteServiceRelativePath("email")
public interface EmailService extends RemoteService{

	public Boolean sendMail(final String[] toAddress, final String fromAddress, final String subject, final String message);
}
