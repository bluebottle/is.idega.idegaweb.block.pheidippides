package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.FiffoImportBean;
import is.idega.idegaweb.pheidippides.business.FiffoImportStatus;
import is.idega.idegaweb.pheidippides.business.ParticipantHolder;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Distance;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.Participant;
import is.idega.idegaweb.pheidippides.data.Race;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.JQueryPlugin;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.io.UploadFile;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ImportFiffoFile extends IWBaseComponent {
	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_SELECT_FILE = 1;
	private static final int ACTION_DONE = 3;
	private static final int ACTION_ERROR = 4;

	@Autowired
	private PheidippidesService service;

	@Autowired
	private JQuery jQuery;

	private IWBundle iwb;
	
	@Autowired
	private PheidippidesDao dao;


	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery()
				.getBundleURIToJQueryLib());
		PresentationUtil.addJavaScriptSourcesLinesToHeader(
				iwc,
				getJQuery().getBundleURISToValidation(
						iwc.getCurrentLocale().getLanguage()));
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery()
				.getBundleURIToJQueryPlugin(JQueryPlugin.MASKED_INPUT));

		PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
				CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
				CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc,
				"/dwr/interface/PheidippidesService.js");

		PresentationUtil
		.addJavaScriptSourceLineToHeader(
				iwc,
				iwb.getVirtualPathWithFileNameString("javascript/fiffoImporter.js"));

		PresentationUtil.addStyleSheetToHeader(iwc,
				iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		FiffoImportBean bean = getBeanInstance("fiffoImportBean");
		bean.setLocale(iwc.getCurrentLocale());
		bean.setProperty(new AdvancedProperty(String.valueOf(IWTimestamp
				.RightNow().getYear()), String.valueOf(IWTimestamp.RightNow()
				.getYear())));

		
		Event event = getDao().getEventByReportSign("RM");
		bean.setEvent(event);
		bean.setRaces(getService().getRaces(bean.getEvent().getId(), IWTimestamp.RightNow().getYear()));
		bean.setLocale(iwc.getCurrentLocale());

		switch (parseAction(iwc)) {
		case ACTION_SELECT_FILE:
			showImportForm(iwc);
			break;

		case ACTION_ERROR:
			showError(iwc);
			break;

		case ACTION_DONE:
			showDone(iwc);
			break;

		}
	}

	private void showImportForm(IWContext iwc) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("fiffoImporter/import.xhtml"));
		add(facelet);
	}

	private void showDone(IWContext iwc) {
		UploadFile uploadFile = iwc.getUploadedFile();
		if (uploadFile != null && uploadFile.getName() != null
				&& uploadFile.getName().length() > 0) {
			try {
				FiffoImportBean bean = getBeanInstance("fiffoImportBean");

				FileInputStream input = new FileInputStream(
						uploadFile.getRealPath());
				Map<FiffoImportStatus, List<Participant>> toImport = getService()
						.importFiffoUpdateExcelFile(input, bean.getEvent(), IWTimestamp.RightNow().getYear());

				boolean hasError = false;
				List<Participant> errors = null;
				List<Participant> participantList = null;
				if (toImport != null && !toImport.isEmpty()) {
					errors = toImport.get(FiffoImportStatus.MISSING_REQUIRED_FIELD);
					if (errors != null && !errors.isEmpty()) {
						bean.setMissingRequiredFields(errors);
						hasError = true;
					}

					errors = toImport
							.get(FiffoImportStatus.ERROR_IN_PERSONAL_ID);
					if (errors != null && !errors.isEmpty()) {
						bean.setInvalidPersonalID(errors);
						hasError = true;
					}

					if (!hasError) {
						System.out.println("no errors");
						participantList = toImport.get(FiffoImportStatus.OK);
						if (participantList != null && !participantList.isEmpty()) {							
							List<ParticipantHolder> holders = new ArrayList<ParticipantHolder>();
							Event event = dao.getEvent(1L);

							for (Participant participant : participantList) {
								ParticipantHolder holder = new ParticipantHolder();
								holder.setParticipant(participant);
								
								String distanceString = participant.getDistanceString();
								if (!distanceString.endsWith("km")) {
									distanceString += "km";
								}
								Distance distance = dao.getDistance(distanceString);
								Race race = dao.getRace(event, distance, IWTimestamp.RightNow().getYear(), false);
								
								holder.setRace(race);
								
								holders.add(holder);
							}

							if (!holders.isEmpty()) {
								getService().storeFiffoUpdateImportRegistration(holders, iwc.getCurrentUser().getUniqueId(), iwc.getCurrentLocale());
							}
							
							System.out.println("Got " + participantList.size() + " entries to import");
							participantList = toImport.get(FiffoImportStatus.CHANGED_DISTANCE);
							if (participantList != null) {
								System.out.println("Got " + participantList.size() + " entries to move");								
							} else {
								System.out.println("Got 0 entries to move");
							}
							participantList = toImport.get(FiffoImportStatus.ALREADY_REGISTERED);
							if (participantList != null) {
								System.out.println("Got " + participantList.size() + " already registered");								
							} else {
								System.out.println("Got 0 entries already registered");
							}

						} else {
							bean.setUnableToImportFile(true);
							hasError = true;		
							
							System.out.println("got errors 1");
						}
					}					
				} else {
					bean.setUnableToImportFile(true);
					hasError = true;

					System.out.println("got errors 2");
				}

				try {
					FileUtil.delete(uploadFile);
				} catch (Exception ex) {
					System.err
							.println("MediaBusiness: deleting the temporary file at "
									+ uploadFile.getRealPath() + " failed.");
				}

				if (hasError) {
					showError(iwc);
					return;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				uploadFile.setId(-1);
			}
		}
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("fiffoImporter/done.xhtml"));
		add(facelet);
	}

	private void showError(IWContext iwc) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("fiffoImporter/error.xhtml"));
		add(facelet);
	}

	private String getBundleIdentifier() {
		return PheidippidesConstants.IW_BUNDLE_IDENTIFIER;
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

	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}

	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer
				.parseInt(iwc.getParameter(PARAMETER_ACTION))
				: ACTION_SELECT_FILE;

		return action;
	}
}