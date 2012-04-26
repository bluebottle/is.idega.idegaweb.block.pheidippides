package is.idega.idegaweb.pheidippides.webservice.client;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestClient client = new TestClient();
		client.doStuff();
	}

	private void doStuff() {
		try {
			//String endpoint = "http://vala.reykjavik.is/WSLeikskoli/WSLeikskoliSoapHttpPort";

			CharityServiceServiceLocator locator = new CharityServiceServiceLocator();
			CharityService port = locator.getCharityService();
			
			Charity info[] = port.getCharities();//getCharityInformation("0610703899");

			if (info != null) {
				for (Charity charity : info) {
					System.out.println(charity.getId() + ", " + charity.getName() + ", " + charity.getDescription());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
