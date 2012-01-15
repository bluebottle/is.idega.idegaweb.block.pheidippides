package is.idega.idegaweb.pheidippides.servlet.filter;
import is.idega.idegaweb.pheidippides.business.PheidippidesRegistrationSession;
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


public class ValitorFilter extends BaseFilter {

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
		PheidippidesRegistrationSession session = (PheidippidesRegistrationSession) springContext.getBean("pheidippidesRegistrationSession");
		
		service.storeRegistration(session.getParticipantHolders());
		
		String uuid = service.createValitorSecurityString("12345011999018536035056191428820http://www.mbl.ishttp://www.visir.isISK");
		
		StringBuilder builder = new StringBuilder("https://testvefverslun.valitor.is/default.aspx");
		builder.append("?VefverslunID=1").append("&Lang=is").append("&Gjaldmidill=ISK").append("&Adeinsheimild=0").append("&Vara_1_Lysing=Palli");
		builder.append("&Vara_1_Fjoldi=1").append("&Vara_1_Verd=1999").append("&Vara_1_Afslattur=0").append("&KaupandaUpplysingar=0").append("&Tilvisunarnumer=8536035056191428820");
		builder.append("&SlodTokstAdGjaldfaera=http://www.mbl.is").append("&SlodTokstAdGjaldfaeraTexti=Eureka").append("&SlodTokstAdGjaldfaeraServerSide=http://www.visir.is");
		builder.append("&SlodNotandiHaettirVid=http://www.bleikt.is").append("&RafraenUndirskrift=").append(uuid);
		
		response.sendRedirect(builder.toString());
	}

}