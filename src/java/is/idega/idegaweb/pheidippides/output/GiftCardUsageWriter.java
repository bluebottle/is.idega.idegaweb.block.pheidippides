package is.idega.idegaweb.pheidippides.output;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.GiftCard;
import is.idega.idegaweb.pheidippides.data.GiftCardUsage;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;
import is.idega.idegaweb.pheidippides.data.Registration;
import is.idega.idegaweb.pheidippides.data.RegistrationHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.core.file.util.MimeTypeUtil;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.expression.ELUtil;

public class GiftCardUsageWriter extends DownloadWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private Locale locale;
	private IWResourceBundle iwrb;

	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private PheidippidesDao dao;
	
	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		this.locale = iwc.getCurrentLocale();
		this.iwrb = iwc.getIWMainApplication().getBundle(PheidippidesConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);

		List<GiftCardUsage> giftCardUsage = getDao().getGiftCardUsage();

		try {
			this.buffer = writeXLS(iwc, giftCardUsage);
			setAsDownload(iwc, "usage.xls", this.buffer.length());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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

	public MemoryFileBuffer writeXLS(IWContext iwc, List<GiftCardUsage> giftCardUsage) throws Exception {
		
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();

		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);

		int cellRow = 0;
		HSSFRow row = sheet.createRow(cellRow++);

		int iCell = 0;
		HSSFCell cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("code", "Code"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("amount", "Amount"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("date", "Date"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("status", "Status"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("race", "Race"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("year", "Year"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("personal_id", "Personal ID"));
		cell.setCellStyle(style);
		
		for (GiftCardUsage usage : giftCardUsage) {
			GiftCard card = usage.getCard();

			Participant participant = getService().getParticipantByUUID(card.getHeader().getBuyer());
			if (participant == null) {
				continue;
			}

			Race race = null;
			RegistrationHeader header = usage.getHeader();
			List<Registration> registrations = getDao().getRegistrations(header);
			if (registrations != null && !registrations.isEmpty()) {
				race = registrations.get(0).getRace();
			}
			
			row = sheet.createRow(cellRow++);
			iCell = 0;

			row.createCell(iCell++).setCellValue(card.getCode());
			row.createCell(iCell++).setCellValue(usage.getAmount());
			row.createCell(iCell++).setCellValue(new IWTimestamp(usage.getCreatedDate()).getDateString("d.M.yyyy"));
			row.createCell(iCell++).setCellValue(usage.getStatus().toString());
			row.createCell(iCell++).setCellValue(race != null ? race.getEvent().getName() : "");
			row.createCell(iCell++).setCellValue(race != null ? String.valueOf(race.getYear()) : "");
			row.createCell(iCell++).setCellValue(participant.getFullName());
			row.createCell(iCell++).setCellValue(participant.getPersonalId());
		}
		
		wb.write(mos);

		buffer.setMimeType(MimeTypeUtil.MIME_TYPE_EXCEL_2);
		return buffer;
	}

	private PheidippidesService getService() {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return service;
	}

	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}
}