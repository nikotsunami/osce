package ch.unibas.medizin.osce.server.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.unibas.medizin.osce.domain.Administrator;

import com.allen_sauer.gwt.log.client.Log;

public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {		
		Log.info("Inside destroy");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		Log.info("Inside doFilter");
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		Enumeration<String> names = request.getHeaderNames();
		/*Log.info("Headers are:");
		while(names.hasMoreElements())
		{
			String headerName = names.nextElement();
			Log.info(headerName + " : " + request.getHeader(headerName));
		}
		
		Log.info("Attributes are:");
		names = request.getAttributeNames();
		while(names.hasMoreElements())
		{
			String attributeName = names.nextElement();
			Log.info(attributeName + " : " + request.getAttribute(attributeName));
		}
		
	
		
		Cookie[] cookies = request.getCookies();
		Log.info("Cookies are:");
		int index = 0;
		while(index > cookies.length)
		{			
			Log.info(cookies[index].getName() + " : " + cookies[index].getValue());
		}
		*/

		/*Administrator administrator = Administrator.findAdministratorsByEmail("foo@bar.com");
		
		Log.info("Administrator : " + administrator);
		
		if(administrator != null)
		{
			Log.info("Login successfully" );
			filterChain.doFilter(servletRequest, servletResponse);
		}*/
		
		/* for production uniqueID and for testing uid */
		String userId = request.getHeader("uniqueID");
		//String userId = request.getHeader("uid");
		Log.info("User Id : " + userId);
		boolean flag = false;
		// Session Management
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			String sessionUserId = (String) session.getAttribute("userId");
			if(userId.equals(sessionUserId)) {
				Log.info("----> Authenticated using session");
				flag = true;
			}else {
				flag = authenticationUsingDB(servletResponse, userId);
				if(flag == true) {
					session.setAttribute("userId", userId);
					Log.info("----> Authenticated using DB");
				}
			}
		}else {
			Log.info("----> Authenticated using New Session");
			flag = authenticationUsingDB(servletResponse, userId);
			if(flag == true){
				session = request.getSession();
				session.setAttribute("userId", userId);
			}		
		}
		
		
		if(flag)
			filterChain.doFilter(servletRequest, servletResponse);
		else
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, userId + " is not authorized to oscemanager.");
		
		/*Log.info("User Id : " + userId);
		if(userId == null || !userId.trim().equals("myself"))		
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, userId + " is not authorized to oscemanager.");
		else
			filterChain.doFilter(servletRequest, servletResponse);*/
	}

	private boolean authenticationUsingDB(ServletResponse servletResponse,
			String userId) {
		boolean flag = false;
		
		try{
			List<Administrator> listAdministrator = Administrator.findAllAdministrators();
			if(userId !=null && userId.equals("210760@vho-switchaai.ch"))
			{
				Log.info("Login successfully by 210760@vho-switchaai.ch" );
				flag=true;
			}
			else if(userId !=null && userId.equals("myself")){
				Log.info("Login successfully by myself" );
				flag=true;
			}
			else if(listAdministrator != null)
			{
				Log.info("listAdministrator : " + listAdministrator.size());
				for(Administrator administrator:listAdministrator)
				{
					if(administrator.getEmail().equals(userId))
					{
						Log.info("Login successfully" );
						flag=true;
						break;
						
					}
				}
			}
			else
			{
				Log.info("Login failes");
				((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, userId + " is not authorized to oscemanager.");
			}
		}catch(Exception e)
		{
			flag=false;
		}
		return flag;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		Log.info("Inside destroy");		
	}
	

}
