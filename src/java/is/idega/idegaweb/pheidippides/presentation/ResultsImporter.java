package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.ResultsImportBean;
import is.idega.idegaweb.pheidippides.business.ResultsImportStatus;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;
import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.data.Event;
import is.idega.idegaweb.pheidippides.data.ParticipantResult;
import is.idega.idegaweb.pheidippides.data.Race;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class ResultsImporter extends IWBaseComponent {
	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_SELECT_FILE = 1;
	private static final int ACTION_DONE = 2;
	private static final int ACTION_ERROR = 3;

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
						iwb.getVirtualPathWithFileNameString("javascript/lvResultsImporter.js"));

		PresentationUtil.addStyleSheetToHeader(iwc,
				iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

		ResultsImportBean bean = getBeanInstance("resultsImportBean");
		bean.setLocale(iwc.getCurrentLocale());
		bean.setProperty(new AdvancedProperty(String.valueOf(IWTimestamp
				.RightNow().getYear()), String.valueOf(IWTimestamp.RightNow()
				.getYear())));

		Event event = getDao().getEventByReportSign("LV");
		bean.setEvent(event);
		List<Race> races = getDao().getRaces(event, 2015);
		/**
		 * @TODO make the year selectable
		 */
		bean.setRace(races.get(0));
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
		facelet.setFaceletURI(iwb
				.getFaceletURI("lvResultsImporter/import.xhtml"));
		add(facelet);
	}

	private void showDone(IWContext iwc) {
		UploadFile uploadFile = iwc.getUploadedFile();
		if (uploadFile != null && uploadFile.getName() != null
				&& uploadFile.getName().length() > 0) {
			try {
				ResultsImportBean bean = getBeanInstance("resultsImportBean");

				FileInputStream input = new FileInputStream(
						uploadFile.getRealPath());
				Map<ResultsImportStatus, List<ParticipantResult>> toImport = getService()
						.importResultsExcelFile(input, bean.getEvent(),
								IWTimestamp.RightNow().getYear());

				boolean hasError = false;
				List<ParticipantResult> errors = null;
				List<ParticipantResult> participantResults = null;
				if (toImport != null && !toImport.isEmpty()) {
					errors = toImport
							.get(ResultsImportStatus.MISSING_REQUIRED_FIELD);
					if (errors != null && !errors.isEmpty()) {
						bean.setMissingRequiredFields(errors);
						hasError = true;
					}

					if (!hasError) {
						participantResults = toImport
								.get(ResultsImportStatus.OK);
						if (participantResults != null
								&& !participantResults.isEmpty()) {

							int counter = getService().storeRaceResults(
									participantResults);
							
							System.out.println("Stored " + counter + " results");
						} else {
							bean.setUnableToImportFile(true);
							hasError = true;
						}
					}
				} else {
					bean.setUnableToImportFile(true);
					hasError = true;
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
		facelet.setFaceletURI(iwb.getFaceletURI("lvResultsImporter/done.xhtml"));
		add(facelet);
	}

	private void showError(IWContext iwc) {
		FaceletComponent facelet = (FaceletComponent) iwc.getApplication()
				.createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("lvResultsImporter/error.xhtml"));
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