package ch.unibas.medizin.osce.server.util.security;
import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GWTCacheControlFilter implements Filter {

	private static final long ONE_DAY = 86400000L; 
	   
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		if (requestURI.contains(".nocache.")) {
			Date now = new Date();
			if (response instanceof HttpServletResponse) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setDateHeader("Date", now.getTime()); // one day old
				httpResponse.setDateHeader("Expires", now.getTime() - ONE_DAY);
				httpResponse.setHeader("Pragma", "no-cache");
				httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
