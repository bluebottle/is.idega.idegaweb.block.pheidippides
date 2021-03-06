package is.idega.idegaweb.pheidippides.output;

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
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.pheidippides.business.GiftCardHolder;
import is.idega.idegaweb.pheidippides.business.GiftCardPrintingContext;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.GiftCard;

public class GiftCardWriter extends DownloadWriter implements MediaWritable {

	private static final String PARAMETER_GIFT_CARD_CODE = "prm_gift_card_code";

	@Autowired
	private PheidippidesDao dao;

	private MemoryFileBuffer buffer = null;
	private Locale locale;

	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			this.locale = iwc.getCurrentLocale();

			GiftCard giftCard = getDao().getGiftCard(iwc.getParameter(PARAMETER_GIFT_CARD_CODE));

			GiftCardHolder holder = new GiftCardHolder();
			holder.setAmount(giftCard.getAmount());
			holder.setAmountText(giftCard.getAmountText());
			holder.setGreetingText(giftCard.getGreeting());
			holder.setCode(giftCard.getCode());
			holder.setCreated(new IWTimestamp(giftCard.getHeader().getValidFrom()).getDateString("d. MMMM yyyy", locale));
			holder.setTemplateNumber(giftCard.getTemplateNumber());

			this.buffer = getDocumentBuffer(new GiftCardPrintingContext(iwc, holder, locale));
			setAsDownload(iwc, giftCard.getCode() + ".pdf", this.buffer.length());
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

	@Override
	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		}
		return super.getMimeType();
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
			mis.close();
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
			return IBOLookup.getServiceInstance(getIWApplicationContext(), PrintingService.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private IWApplicationContext getIWApplicationContext() {
		return IWMainApplication.getDefaultIWApplicationContext();
	}
}