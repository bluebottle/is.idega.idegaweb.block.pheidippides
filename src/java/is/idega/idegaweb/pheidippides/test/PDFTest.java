package is.idega.idegaweb.pheidippides.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ujac.print.DocumentHandlerException;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.FileResourceLoader;

public class PDFTest {

	public static void main(String[] args) {
		try {
			// defining the document properties, this map is used for dynamical content evaluation.
			Map<String, String> documentProperties = new HashMap<String, String>();
			documentProperties.put("name", "Þorbergur Ingi Jónsson");
			documentProperties.put("finishTime", "4:13:25");
			documentProperties.put("placement", "1");
			documentProperties.put("genderPlacement", "1");
			documentProperties.put("groupPlacement", "1");
			documentProperties.put("ageGroup", "30 - 39 ára");
			documentProperties.put("gender", "karlar");
						
			// instantiating the document printer
			FileInputStream templateStream = new FileInputStream("/Users/palli/Development/illuminati/git/com.idega.block.custom/is.idega.idegaweb.block.pheidippides/resources/is_IS.locale/print/lv/2017/certificate.xml");
			DocumentPrinter documentPrinter = new DocumentPrinter(templateStream, documentProperties);
			// in case you'd like to use a XML parser different from the default implementation
			// you can specify it here (apache xerces in this case).
			documentPrinter.setXmlReaderClass("org.apache.xerces.parsers.SAXParser");
			// defining the ResourceLoader: This is necessary if you like to 
			// dynamically load resources like images during template processing.
			documentPrinter.setResourceLoader(new FileResourceLoader("./src/java/is/idega/idegaweb/pheidippides/test/"));
			// generating the document output
			FileOutputStream pdfStream = new FileOutputStream("/Users/palli/output.pdf");
			documentPrinter.printDocument(pdfStream);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (DocumentHandlerException dhe) {
			dhe.printStackTrace();
		}
	}
}