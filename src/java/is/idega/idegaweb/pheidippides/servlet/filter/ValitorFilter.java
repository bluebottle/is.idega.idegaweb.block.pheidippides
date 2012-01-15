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


public class ValitorFilter extends BaseFilter {

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		
		IWContext iwc = new IWContext(request, response, request.getSession().getServletContext());
		
		response.sendRedirect("http://www.mbl.is");
	}

}