package is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.test;

import java.net.URL;

import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.ContestantRequest;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.ContestantServiceLocator;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.IContestantService;
import is.idega.idegaweb.pheidippides.webservice.hlaupastyrkur.client.Login;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityList;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceLocator;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType;

public class Test {
	public static void main(String[] args) {
        String userID = args[0];
        String passwd = args[1];

		Test test = new Test();
		test.testContestantService(userID, passwd);
		//test.testExternalCharities();

	}

    @SuppressWarnings("unused")
	private void testExternalCharities() {
        try {
            CharityWebServiceLocator locator = new CharityWebServiceLocator();
            CharityWebServiceSoap_PortType port = locator.getCharityWebServiceSoap(new URL("https://www.hlaupastyrkur.is/charitywebservice.asmx"));
            CharityList[] charities = port.getCharitiesLocalized("is");

            for (CharityList charityList : charities) {
                System.out.println("id = " + charityList.getID());
                System.out.println("name = " + charityList.getName());
                System.out.println("ssn = " + charityList.getSSN());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@SuppressWarnings("unused")
    private void testContestantService(String userID, String password) {
	       try {
	            ContestantServiceLocator locator = new ContestantServiceLocator();
	            IContestantService port = locator
	                    .getBasicHttpBinding_IContestantService(new URL(
	                            "http://www.hlaupastyrkur.is/services/contestantservice.svc"));

	            //ContestantRequest request = new ContestantRequest("21_km", new Login(password, userID), "", "65", "", "", "Palli Test", "pallitest", "", "pallitest", "0610703899", Boolean.TRUE);
                ContestantRequest request = new ContestantRequest(new Login(password, userID), "65", "21_km", "Palli Test", "pallitest", "pallitest", "0610703899", Boolean.TRUE);
	            port.registerContestant(request);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	}
}