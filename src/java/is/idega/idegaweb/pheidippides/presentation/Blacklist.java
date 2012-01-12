package is.idega.idegaweb.pheidippides.presentation;

import is.idega.idegaweb.pheidippides.GrubConstants;
import is.idega.idegaweb.pheidippides.bean.GrubBean;
import is.idega.idegaweb.pheidippides.dao.GrubDao;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class Blacklist extends IWBaseComponent {

	@Autowired
	private GrubDao dao;
	
	private IWBundle iwb;
	
	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		iwb = getBundle(context, getBundleIdentifier());
		
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/grub.css"));

		GrubBean bean = getBeanInstance("grubBean");
		bean.setBlacklist(getDao().getBlacklist());

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("blacklist/view.xhtml"));
		add(facelet);
	}

	private String getBundleIdentifier() {
		return GrubConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private GrubDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}

}