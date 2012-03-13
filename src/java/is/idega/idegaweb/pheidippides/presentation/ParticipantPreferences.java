package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.CreateException;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.file.data.ICFile;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.UploadFile;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ParticipantPreferences extends IWBaseComponent implements IWPageEventListener {

	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DATE_OF_BIRTH = "prm_date_of_birth";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_POSTAL_CODE = "prm_postal_code";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_COUNTRY_PK = "prm_country";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_PHONE = "prm_phone";
	private static final String PARAMETER_MOBILE = "prm_mobile";

	@Autowired
	private PheidippidesService service;
	
	@Autowired
	private JQuery jQuery;
	
	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		if (iwc.isLoggedOn()) {
			iwb = getBundle(context, getBundleIdentifier());
			
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getJQuery().getBundleURISToValidation());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, iwb.getVirtualPathWithFileNameString("javascript/participantPreferences.js"));

			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));
	
			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setEventHandler(IWMainApplication.getEncryptedClassName(ParticipantPreferences.class));
			bean.setLocale(iwc.getCurrentLocale());
			bean.setParticipant(getService().getParticipant(iwc.getCurrentUser()));
			bean.setProperties(getService().getCountries());
			bean.setProperty(new AdvancedProperty(iwc.getApplicationSettings().getProperty("default.ic_country", "104"), iwc.getApplicationSettings().getProperty("default.ic_country", "104")));
	
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			facelet.setFaceletURI(iwb.getFaceletURI("participantPreferences/view.xhtml"));
			add(facelet);
		}
	}

	private PheidippidesService getService() {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return service;
	}

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		String fullName = iwc.getParameter(PARAMETER_NAME);
		@SuppressWarnings("deprecation")
		Date dateOfBirth = iwc.isParameterSet(PARAMETER_DATE_OF_BIRTH) ? new IWTimestamp(IWDatePickerHandler.getParsedDate(iwc.getParameter(PARAMETER_DATE_OF_BIRTH))).getSQLDate() : null;
		String address = iwc.getParameter(PARAMETER_ADDRESS);
		String postalCode = iwc.getParameter(PARAMETER_POSTAL_CODE);
		String city = iwc.getParameter(PARAMETER_CITY);
		Integer countryPK = iwc.isParameterSet(PARAMETER_COUNTRY_PK) ? Integer.parseInt(iwc.getParameter(PARAMETER_COUNTRY_PK)) : null;
		String gender = iwc.getParameter(PARAMETER_GENDER);
		String email = iwc.getParameter(PARAMETER_EMAIL);
		String phone = iwc.getParameter(PARAMETER_PHONE);
		String mobile = iwc.getParameter(PARAMETER_MOBILE);
		
		ICFile file = null;
		UploadFile uploadFile = iwc.getUploadedFile();
		if (uploadFile != null && uploadFile.getName() != null && uploadFile.getName().length() > 0) {
			try {
				FileInputStream input = new FileInputStream(uploadFile.getRealPath());

				file = ((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).create();
				file.setName(uploadFile.getName());
				file.setMimeType(uploadFile.getMimeType());
				file.setFileValue(input);
				file.setFileSize((int) uploadFile.getSize());
				file.store();

				int fileID = ((Integer) file.getPrimaryKey()).intValue();
				uploadFile.setId(fileID);
				try {
					FileUtil.delete(uploadFile);
				}
				catch (Exception ex) {
					System.err.println("MediaBusiness: deleting the temporary file at " + uploadFile.getRealPath() + " failed.");
				}
			}
			catch (RemoteException e) {
				e.printStackTrace(System.err);
				uploadFile.setId(-1);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				uploadFile.setId(-1);
			}
			catch (CreateException e) {
				e.printStackTrace();
				uploadFile.setId(-1);
			}
		}
		
		getService().updateUser(iwc.getCurrentUser().getUniqueId(), fullName, dateOfBirth, address, postalCode, city, countryPK, gender, email, phone, mobile, file);
		
		return true;
	}
}