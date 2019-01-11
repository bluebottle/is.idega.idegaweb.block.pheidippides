package is.idega.idegaweb.pheidippides.servlet.filter;
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

import is.idega.idegaweb.pheidippides.business.PheidippidesRegistrationSession;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationAnswerHolder;


public class ValitorFilter extends BaseFilter {

	@Override
    public void destroy() {
	}

	@Override
    public void init(FilterConfig config) throws ServletException {
	}

	@Override
    public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;

		IWContext iwc = new IWContext(request, response, request.getSession().getServletContext());
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(iwc.getServletContext());
		PheidippidesService service = (PheidippidesService) springContext.getBean("pheidippidesService");
		PheidippidesRegistrationSession session = (PheidippidesRegistrationSession) springContext.getBean("pheidippidesRegistrationSession");

		session.addParticipantHolder(session.getCurrentParticipant());

		RegistrationAnswerHolder holder = null;
		if (session.getValitorShopId() != null) {
            holder = service.storeRegistration(session.getParticipantHolders(), true, session.getRegistrantUUID(), !session.isRegistrationWithPersonalId(), iwc.getCurrentLocale(), null, false, session.getCurrency(), session.getGiftCards(), session.getDiscountCode(), session.getValitorShopId(), session.getValitorSecurityNumber(), session.getValitorReturnURLText(), session.getValitorReturnURL());
		} else {
		    holder = service.storeRegistration(session.getParticipantHolders(), true, session.getRegistrantUUID(), !session.isRegistrationWithPersonalId(), iwc.getCurrentLocale(), null, false, session.getCurrency(), session.getGiftCards(), session.getDiscountCode());
		}

		session.empty();

		response.sendRedirect(holder.getValitorURL());
	}
}