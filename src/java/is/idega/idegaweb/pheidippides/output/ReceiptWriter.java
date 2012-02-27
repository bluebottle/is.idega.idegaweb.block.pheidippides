package is.idega.idegaweb.pheidippides.output;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.business.ReceiptPrintingContext;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Registration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.expression.ELUtil;

public class ReceiptWriter extends DownloadWriter implements MediaWritable {

	private static final String PARAMETER_REGISTRATION_PK = "prm_registration_pk";
	
	@Autowired
	private PheidippidesDao dao;
	
	private MemoryFileBuffer buffer = null;
	private Locale locale;
	private IWResourceBundle iwrb;

	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			this.locale = iwc.getCurrentLocale();
			this.iwrb = iwc.getIWMainApplication().getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);

			Registration registration = getDao().getRegistration(Long.parseLong(iwc.getParameter(PARAMETER_REGISTRATION_PK)));
			
			this.buffer = getDocumentBuffer(new ReceiptPrintingContext(iwc, registration, locale));
			setAsDownload(iwc, iwrb.getLocalizedString("receipt_filename", "receipt") + ".pdf", this.buffer.length());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}

	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		}
		return super.getMimeType();
	}

	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else {
			System.err.println("buffer is null");
		}
	}

	private MemoryFileBuffer getDocumentBuffer(PrintingContext pcx) {
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			buffer.setMimeType("application/pdf");
			OutputStream mos = new MemoryOutputStream(buffer);

			pcx.setDocumentStream(mos);

			getPrintingService().printDocument(pcx);
			
			return buffer;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private PrintingService getPrintingService() {
		try {
			return (PrintingService) IBOLookup.getServiceInstance(getIWApplicationContext(), PrintingService.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
	
	private IWApplicationContext getIWApplicationContext() {
		return IWMainApplication.getDefaultIWApplicationContext();
	}
}