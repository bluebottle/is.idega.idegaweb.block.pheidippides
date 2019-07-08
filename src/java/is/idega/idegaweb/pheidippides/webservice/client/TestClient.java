package is.idega.idegaweb.pheidippides.webservice.client;

import java.net.URL;

import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityList;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceLocator;
import is.idega.idegaweb.pheidippides.webservice.org.hlaupastyrkur.CharityWebServiceSoap_PortType;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestClient client = new TestClient();
		client.doStuff2();
	}

	private void testStuff() {
	    int x = 1;
	    x += x++ * 2;
	    System.out.println("x = " + x);
	}

	private void doStuff2() {
	    try {
	        CharityWebServiceLocator locator = new CharityWebServiceLocator();
	        CharityWebServiceSoap_PortType port = locator.getCharityWebServiceSoap();
	        CharityList[] charities = port.getCharitiesLocalized("is");
	        for (int i = 0; i < charities.length; i++) {
                System.out.println(i + ", id = " + charities[i].getID() + ", name = " + charities[i].getName() + ", ssn = " + charities[i].getSSN());
            }

           charities = port.getCharitiesLocalized("en");
            for (int i = 0; i < charities.length; i++) {
                System.out.println(i + ", id = " + charities[i].getID() + ", name = " + charities[i].getName() + ", ssn = " + charities[i].getSSN());
            }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void doStuff() {
		try {
			CharityServiceServiceLocator locator = new CharityServiceServiceLocator();
			//CharityService port = locator.getCharityService(new URL("http://pheidippidestest.sidan.is/services/CharityService"));
			CharityService port = locator.getCharityService(new URL("http://skraning.marathon.is/services/CharityService"));

			//Session session = port.authenticateUser("isb", "ch4r1tys3rv1c3");//getCharities();//getCharityInformation("0610703899");
			CharityInformation charity = port.getCharityInformation("2211785649");

			/*Charity info[] = port.getCharities();

			if (info != null) {
				for (Charity charity : info) {
					System.out.println(charity.getId() + ", " + charity.getName() + ", " + charity.getDescription());
				}
			}*/

			//System.out.println("session = " + session.getSessionID());
			if (charity == null) {
				System.out.println("no charity");
			} else {
				System.out.println("address = " + charity.getAddress());
				System.out.println("charity id = " + charity.getCharityID());
				System.out.println("charity name = " + charity.getCharityName());
				System.out.println("city = " + charity.getCity());
				System.out.println("country = " + charity.getCountry());
				System.out.println("distance = " + charity.getDistance());
				System.out.println("email = " + charity.getEmail());
				System.out.println("gender = " + charity.getGender());
				System.out.println("mobile = " + charity.getMobile());
				System.out.println("name = " + charity.getName());
				System.out.println("nationality = " + charity.getNationality());
				System.out.println("personal id = " + charity.getPersonalID());
				System.out.println("phone = " + charity.getPhone());
				System.out.println("postal code = " + charity.getPostalCode());
			}

			//Charity charity = port.getCharity("0610703899");
			//if (charity != null) {
			//	System.out.println("Got something");
			//}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
