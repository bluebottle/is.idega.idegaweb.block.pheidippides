package is.idega.idegaweb.pheidippides.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.presentation.IWContext;
import com.idega.servlet.filter.BaseFilter;

public class RegistrationFilter extends BaseFilter {

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		
		IWContext iwc = new IWContext(request, response, request.getSession().getServletContext());
		
		String uri = request.getRequestURI().replaceAll("/registration/", "");
		String event = uri.substring(0, uri.indexOf("/"));
		String lang = uri.length() > event.length() + 1 ? uri.substring(uri.indexOf("/") + 1) : "is";
		
		String url = iwc.getApplicationSettings().getProperty("registration.url." + event);
		url += "?iw_language=" + (lang.equals("is") ? "is_IS" : "en");
				
		response.sendRedirect(url);
	}

}
