package is.idega.idegaweb.pheidippides.servlet;

import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.presentation.IWContext;

public class ValitorSuccess extends HttpServlet {

	private static final long serialVersionUID = -5479791076359839694L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(200);
		resp.setContentType("text/plain");

		IWContext iwc = new IWContext(req, resp, req.getSession().getServletContext());
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(iwc.getServletContext());
		PheidippidesService service = (PheidippidesService) springContext.getBean("pheidippidesService");
		
		String uniqueID = iwc.getParameter("uniqueID");
		RegistrationHeader header = service.getRegistrationHeader(uniqueID);
		
		if (header.getStatus().equals(RegistrationHeaderStatus.WaitingForPayment)) {
			String securityString = iwc.getParameter("RafraenUndirskriftSvar");
			String cardType = iwc.getParameter("Kortategund");
			String cardNumber = iwc.getParameter("KortnumerSidustu");
			String paymentDate = iwc.getParameter("Dagsetning");
			String authorizationNumber = iwc.getParameter("Heimildarnumer");
			String transactionNumber = iwc.getParameter("Faerslunumer");
			String referenceNumber = iwc.getParameter("Tilvisunarnumer");
			String comment = iwc.getParameter("Athugasemd");
			String saleID = iwc.getParameter("VefverslunSalaID");
			
			service.markRegistrationAsPaid(header, false, securityString, cardType, cardNumber, paymentDate, authorizationNumber, transactionNumber, referenceNumber, comment, saleID);
		}
		
		PrintWriter out = resp.getWriter();
		out.println("The Valitor success response has been processed...");
		out.close();
	}

}