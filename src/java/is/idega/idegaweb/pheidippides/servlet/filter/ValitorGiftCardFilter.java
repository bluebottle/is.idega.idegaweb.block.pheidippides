package is.idega.idegaweb.pheidippides.servlet.filter;
import is.idega.idegaweb.pheidippides.business.GiftCardAnswerHolder;
import is.idega.idegaweb.pheidippides.business.GiftCardSession;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.presentation.IWContext;
import com.idega.servlet.filter.BaseFilter;


public class ValitorGiftCardFilter extends BaseFilter {

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		
		IWContext iwc = new IWContext(request, response, request.getSession().getServletContext());
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(iwc.getServletContext());
		PheidippidesService service = (PheidippidesService) springContext.getBean("pheidippidesService");
		GiftCardSession session = (GiftCardSession) springContext.getBean("giftCardSession");
		
		GiftCardAnswerHolder holder = service.storeGiftCard(session.getCards(), session.getCreatorUUID(), session.getEmail(), session.getLocale(), true);
		
		response.sendRedirect(holder.getValitorURL());
	}

}