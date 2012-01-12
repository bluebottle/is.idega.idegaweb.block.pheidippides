package is.idega.idegaweb.pheidippides.output;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.business.GrubService;
import is.idega.idegaweb.pheidippides.business.GrubSession;
import is.idega.idegaweb.pheidippides.data.MealRegistration;
import is.idega.idegaweb.pheidippides.data.Product;
import is.idega.idegaweb.pheidippides.data.School;
import is.idega.idegaweb.pheidippides.data.Student;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.StringHandler;
import com.idega.util.expression.ELUtil;

public class RegistrationsWriter extends DownloadWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private Locale locale;
	private IWResourceBundle iwrb;

	private static final String PARAMETER_SCHOOL_YEAR = "prm_school_year";
	private static final String PARAMETER_SCHOOL_CLASS = "prm_school_class";
	private static final String PARAMETER_PRODUCT = "prm_product";
	private static final String PARAMETER_DATE = "prm_date";

	@Autowired
	private GrubService service;
	
	@Autowired
	private GrubSession session;
	
	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		this.locale = iwc.getApplicationSettings().getApplicationLocale();
		this.iwrb = iwc.getIWMainApplication().getBundle(GrubConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);
		
		Date date = new IWTimestamp().getDate();
		if (iwc.isParameterSet(PARAMETER_DATE)) {
			date = new IWTimestamp(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_DATE))).getDate();
		}

		Integer year = iwc.isParameterSet(PARAMETER_SCHOOL_YEAR) ? Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_YEAR)) : null;
		String schoolClass = iwc.isParameterSet(PARAMETER_SCHOOL_CLASS) ? iwc.getParameter(PARAMETER_SCHOOL_CLASS) : null;
		Product product = iwc.isParameterSet(PARAMETER_PRODUCT) ? getService().getProduct(Long.parseLong(iwc.getParameter(PARAMETER_PRODUCT))) : null;
		
		Set<School> schools = new TreeSet<School>();
		if (getSession().getSchool() != null) {
			schools.add(getSession().getSchool());
		}
		else {
			Collection<School> allSchools = getService().getAllSchools();
			for (School school : allSchools) {
				schools.add(school);
			}
		}

		try {
			this.buffer = writeXLS(iwc, schools, date, year, schoolClass, product);
			setAsDownload(iwc, "registrations.xls", this.buffer.length());
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
		}
		else {
			System.err.println("buffer is null");
		}
	}
	
	public MemoryFileBuffer writeXLS(IWContext iwc, Set<School> schools, Date date, Integer year, String schoolClass, Product product) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		boolean manySchools = schools.size() > 1;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = manySchools ? wb.createSheet() : wb.createSheet(StringHandler.shortenToLength(schools.iterator().next().getName(), 30));

		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);

		int cellRow = 0;
		HSSFRow row = sheet.createRow(cellRow++);

		short iCell = 0;
		if (manySchools) {
			HSSFCell cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("school", "School"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("provider_code", "Provider code"));
			cell.setCellStyle(style);
		}
		HSSFCell cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("personal_id", "Personal ID"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("grade", "Grade"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("school_class", "Class"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("student_number", "Student number"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("allergies", "Allergies"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("comments", "Comments"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("start_date", "Start date"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("end_date", "End date"));
		cell.setCellStyle(style);

		boolean isAdmin = iwc.hasRole(GrubConstants.ROLE_KEY_ADMIN);
		boolean isSchoolAdmin = iwc.hasRole(GrubConstants.ROLE_KEY_SCHOOL_ADMIN);
		if (isSchoolAdmin || isAdmin) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("payer.personal_id", "Payer personal ID"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("payer.name", "Payer name"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("payment_method", "Payment method"));
			cell.setCellStyle(style);
		}
		if (isAdmin) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("card.card_number", "Card number"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("card.card_validity", "Card validity"));
			cell.setCellStyle(style);
		}
		
		for (School school : schools) {
			Map<Student, List<MealRegistration>> registrations = getService().getMealRegistrationsMap(school, date, year, schoolClass, product);
	
			if (registrations != null && !registrations.isEmpty()) {
				Set<Student> students = registrations.keySet();
				if (!students.isEmpty()) {
					for (Student student : students) {
						row = sheet.createRow(cellRow++);
						iCell = 0;
						
						if (manySchools) {
							row.createCell(iCell++).setCellValue(school.getName());
							row.createCell(iCell++).setCellValue(school.getPaymentCode());
						}

						row.createCell(iCell++).setCellValue(student.getName());
						row.createCell(iCell++).setCellValue(PersonalIDFormatter.format(student.getPersonalID(), this.locale));
						row.createCell(iCell++).setCellValue(student.getYear());
						row.createCell(iCell++).setCellValue(student.getSchoolClass());
						row.createCell(iCell++).setCellValue(student.getStudentNumber());
						row.createCell(iCell++).setCellValue(student.getAllergies());
						row.createCell(iCell++).setCellValue(student.getComments());
						
						List<MealRegistration> mealRegistrations = registrations.get(student);
						MealRegistration registration = mealRegistrations.iterator().next();
		
						row.createCell(iCell++).setCellValue(new IWTimestamp(registration.getStartDate()).getLocaleDate(locale, IWTimestamp.SHORT));
						row.createCell(iCell++).setCellValue(registration.getEndDate() != null ? new IWTimestamp(registration.getEndDate()).getLocaleDate(locale, IWTimestamp.SHORT) : "");
		
						if (isSchoolAdmin || isAdmin) {
							row.createCell(iCell++).setCellValue(PersonalIDFormatter.format(registration.getPayersPersonalID(), locale));
							row.createCell(iCell++).setCellValue(registration.getPayersName()/* != null ? registration.getPayersName() : getService().getName(registration.getPayersPersonalID())*/);
							row.createCell(iCell++).setCellValue(registration.getCardNumber() != null && registration.getCardNumber().length() > 0 ? iwrb.getLocalizedString("payment_method.card", "Credit card") : iwrb.getLocalizedString("payment_method.giro", "Giro"));
						}
						if (isAdmin && registration.getCardNumber() != null && registration.getCardNumber().length() > 0) {
							row.createCell(iCell++).setCellValue(registration.getCardNumber());
							row.createCell(iCell++).setCellValue(registration.getValidMonth() + "/" + registration.getValidYear());
						}
					}
				}
			}
		}
		
		wb.write(mos);

		buffer.setMimeType(MimeTypeUtil.MIME_TYPE_EXCEL_2);
		return buffer;
	}

	private GrubService getService() {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return service;
	}

	private GrubSession getSession() {
		if (session == null) {
			ELUtil.getInstance().autowire(this);
		}
		return session;
	}
}