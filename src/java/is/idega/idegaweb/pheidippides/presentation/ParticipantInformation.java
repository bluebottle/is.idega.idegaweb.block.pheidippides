package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.PheidippidesConstants;
import is.idega.idegaweb.pheidippides.bean.PheidippidesBean;
import is.idega.idegaweb.pheidippides.business.PheidippidesService;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class ParticipantInformation extends IWBaseComponent {

	@Autowired
	private PheidippidesService service;

	private IWBundle iwb;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		if (iwc.isLoggedOn()) {
			User user = iwc.getCurrentUser();
			iwb = getBundle(context, getBundleIdentifier());

			PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/pheidippides.css"));

			PheidippidesBean bean = getBeanInstance("pheidippidesBean");
			bean.setParticipant(getService().getParticipant(user));
			
			FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
			facelet.setFaceletURI(iwb.getFaceletURI("participantInformation/view.xhtml"));
			add(facelet);
		}
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
}