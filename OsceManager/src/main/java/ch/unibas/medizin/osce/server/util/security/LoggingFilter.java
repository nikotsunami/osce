package ch.unibas.medizin.osce.server.util.security;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class LoggingFilter implements Filter{

	public static final String USER_NAME = "userName";
	private static final String PAGEURL = "pageurl";
	private static Logger log = Logger.getLogger(LoggingFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
            if (request instanceof HttpServletRequest) {
            	HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            	HttpSession session = httpServletRequest.getSession();
            	Object userNameObj = session.getAttribute(USER_NAME);
            	if(userNameObj != null && userNameObj instanceof String) {
            		MDC.put(USER_NAME, (String)userNameObj);
            		String pageURL = httpServletRequest.getHeader(PAGEURL);
                	if(StringUtils.isNotBlank(pageURL)) {
                		log.debug(pageURL);	
                	}
            	}
        	}
            chain.doFilter(request, response);
        } finally {
            MDC.remove(USER_NAME);
        }
	}

	@Override
	public void destroy() {
	}

}
