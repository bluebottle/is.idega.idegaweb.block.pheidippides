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

import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

public class PDFTest {

	public static void main(String[] args) {
		try {
			// defining the document properties, this map is used for dynamical content evaluation.
			Map<String, String> documentProperties = new HashMap<String, String>();
			documentProperties.put("amount", "4.000 kr.");
			documentProperties.put("amountText", "fjögur þúsund krónur");
			documentProperties.put("key", "QAXOEVXSPHEUQI2E");
			documentProperties.put("created", new IWTimestamp(9, 1, 2013).getDateString("d. MMMM yyyy", LocaleUtil.getIcelandicLocale()));
			
			// instantiating the document printer
			FileInputStream templateStream = new FileInputStream("/Users/laddi/Development/idega/git-repo-v4/com.idega.block.custom/is.idega.idegaweb.block.pheidippides/src/java/is/idega/idegaweb/pheidippides/test/template.xml");
			DocumentPrinter documentPrinter = new DocumentPrinter(templateStream, documentProperties);
			// in case you'd like to use a XML parser different from the default implementation
			// you can specify it here (apache xerces in this case).
			documentPrinter.setXmlReaderClass("org.apache.xerces.parsers.SAXParser");
			// defining the ResourceLoader: This is necessary if you like to 
			// dynamically load resources like images during template processing.
			documentPrinter.setResourceLoader(new FileResourceLoader("./src/java/is/idega/idegaweb/pheidippides/test/"));
			// generating the document output
			FileOutputStream pdfStream = new FileOutputStream("/Users/laddi/Development/idega/git-repo-v4/com.idega.block.custom/is.idega.idegaweb.block.pheidippides/src/java/is/idega/idegaweb/pheidippides/test/output.pdf");
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