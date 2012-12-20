package is.idega.idegaweb.pheidippides.servlet;

import is.idega.idegaweb.pheidippides.business.PheidippidesService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.presentation.IWContext;

public class ValitorGiftCardCancel extends HttpServlet {

	private static final long serialVersionUID = -5479791076359839694L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(200);
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		IWContext iwc = new IWContext(req, resp, req.getSession().getServletContext());
		if (iwc.isParameterSet("uniqueID")) {
			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(iwc.getServletContext());
			PheidippidesService service = (PheidippidesService) springContext.getBean("pheidippidesService");
	
			service.markRegistrationAsPaymentCancelled(iwc.getParameter("uniqueID"));
			out.println("The Valitor cancellation response has been processed...");
		}
		else {
			out.println("The Valitor cancellation standard response...");
		}
		
		out.close();
	}
}