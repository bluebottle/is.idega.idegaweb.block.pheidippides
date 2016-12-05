package is.idega.idegaweb.pheidippides.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;

import is.idega.idegaweb.pheidippides.business.Currency;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.business.RegistrationHeaderStatus;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;

public class ValitorSuccess extends HttpServlet {

	private static final long serialVersionUID = -5479791076359839694L;

	private static final String VALITOR_SECURITY_NUMBER_EUR = "VALITOR_SECURITY_NUMBER_EUR";
	private static final String VALITOR_SECURITY_NUMBER = "VALITOR_SECURITY_NUMBER";
    private static final String VALITOR_TOUR_SECURITY_NUMBER = "VALITOR_TOUR_SECURITY_NUMBER";

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
		PheidippidesDao dao = (PheidippidesDao) springContext.getBean("pheidippidesDao");

		String uniqueID = iwc.getParameter("uniqueID");
		RegistrationHeader header = service.getRegistrationHeader(uniqueID);

		if (header.getStatus().equals(RegistrationHeaderStatus.WaitingForPayment)) {
			String securityString = iwc.getParameter("RafraenUndirskriftSvar");
			String referenceNumber = iwc.getParameter("Tilvisunarnumer");
			String securityNumber = IWMainApplication
					.getDefaultIWApplicationContext().getApplicationSettings()
					.getProperty(VALITOR_SECURITY_NUMBER, "12345");

			if (header.getCurrency().equals(Currency.EUR)) {
				securityNumber = IWMainApplication
						.getDefaultIWApplicationContext().getApplicationSettings()
						.getProperty(VALITOR_SECURITY_NUMBER_EUR, "12345");
			}

			List<Registration> registrations = dao.getRegistrations(header);
			for (Registration registration : registrations) {
	            if (registration.getRace().getEvent().getReportSign().equals("TOR") || registration.getRace().getEvent().getReportSign().equals("NLH")) {
	                securityNumber = IWMainApplication
	                        .getDefaultIWApplicationContext().getApplicationSettings()
	                        .getProperty(VALITOR_TOUR_SECURITY_NUMBER, "12345");
	            }

	            break;
            }

			if (validateSecurityString(service, securityNumber, referenceNumber, securityString)) {
				String cardType = iwc.getParameter("Kortategund");
				String cardNumber = iwc.getParameter("KortnumerSidustu");
				String paymentDate = iwc.getParameter("Dagsetning");
				String authorizationNumber = iwc.getParameter("Heimildarnumer");
				String transactionNumber = iwc.getParameter("Faerslunumer");
				String comment = iwc.getParameter("Athugasemd");
				String saleID = iwc.getParameter("VefverslunSalaID");

				service.markRegistrationAsPaid(header, false, false, securityString, cardType, cardNumber, paymentDate, authorizationNumber, transactionNumber, referenceNumber, comment, saleID);
			}
		}

		PrintWriter out = resp.getWriter();
		out.println("The Valitor success response has been processed...");
		out.close();
	}

	private boolean validateSecurityString(PheidippidesService service, String securityNumber, String referenceNumber, String securityString) {
		String created = service.createValitorSecurityString(securityNumber + referenceNumber);
		if (created != null && created.equals(securityString)) {
			return true;
		}

		return false;
	}
}