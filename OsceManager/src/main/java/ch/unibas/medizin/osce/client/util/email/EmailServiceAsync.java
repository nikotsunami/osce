/**
 * 
 */
package ch.unibas.medizin.osce.client.util.email;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rahul
 *
 */
public interface EmailServiceAsync{

	void sendMail(final String[] toAddress, final String fromAddress, final String subject, final String message, AsyncCallback<Boolean> callback);
}
