package is.idega.idegaweb.pheidippides.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Base32;

import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryOutputStream;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.pheidippides.business.GiftCardHolder;
import is.idega.idegaweb.pheidippides.business.GiftCardPrintingContext;
import is.idega.idegaweb.pheidippides.data.GiftCard;

public class GiftCardUtil {

    private AtomicLong sequence;
    private Random random;
    private Random seed;

    public GiftCardUtil() {
    	seed = new SecureRandom();
        sequence = new AtomicLong(System.currentTimeMillis() - seed.nextLong());
        random = new SecureRandom();
    }

    public String generateCode() {
        byte[] id = new byte[10];
        longTo5ByteArray(sequence.incrementAndGet(), id);
        byte[] rnd = new byte[5];
        random.nextBytes(rnd);
        System.arraycopy(rnd, 0, id, 5, 5);
        return new Base32().encodeAsString(id);
    }

    private void longTo5ByteArray(long l, byte[] b) {
        b[0] = (byte) (l >>> 32);
        b[1] = (byte) (l >>> 24);
        b[2] = (byte) (l >>> 16);
        b[3] = (byte) (l >>> 8);
        b[4] = (byte) (l >>> 0);
    }

    public static void main(String args[]) {
    	GiftCardUtil util = new GiftCardUtil();
    	System.out.println(util.generateCode());
    }

    public File createPDFFile(IWApplicationContext iwac, GiftCard card, Locale locale) {
    	NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		formatter.setParseIntegerOnly(true);

		GiftCardHolder holder = new GiftCardHolder();
		holder.setAmount(card.getAmount());
		holder.setAmountText(card.getAmountText());
		holder.setCode(card.getCode());
		holder.setCreated(new IWTimestamp(card.getHeader().getValidFrom()).getDateString("d. MMMM yyyy", locale));
		holder.setTemplateNumber(card.getTemplateNumber());

		try {
			final File temp = File.createTempFile("GiftCertificate", card.getCode() + ".pdf");

			FileOutputStream pdfStream = new FileOutputStream(temp);

			pdfStream.write(getDocumentBuffer(new GiftCardPrintingContext(iwac, holder, locale)).buffer());
			pdfStream.close();

			return temp;
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}

		return null;
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