package is.idega.idegaweb.pheidippides.business.importer;

import is.idega.idegaweb.pheidippides.business.GrubService;
import is.idega.idegaweb.pheidippides.data.School;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.user.data.Group;
import com.idega.util.expression.ELUtil;
import com.idega.util.text.TextSoap;

@Scope("session")
@Service("schoolImportFileHandler")
public class SchoolImportFileHandler implements ImportFileHandler {

	@Autowired
	private GrubService service;
	
	ImportFile importFile = null;
	List failedRecords;
	
	public boolean handleRecords() throws RemoteException {
		if (service == null) {
			ELUtil.getInstance().autowire(this);
		}
		failedRecords = new ArrayList();
		
		int counter = 0; 
		String record;
		while (!(record = (String) this.importFile.getNextRecord()).equals("")) {
			ArrayList values = this.importFile.getValuesFromRecordString(record);
			String name = TextSoap.capitalize((String) values.get(0), " ");
			String personalID = (String) values.get(1);
			counter++;
			
			School school = service.getSchool(personalID);
			if (school == null){
				service.createSchool(name, personalID);
			}
			
			if (counter % 50 == 0) {
				System.out.println("[SchoolImportFileHandler]: "+counter+" records imported");
			}
		}
		System.out.println("[SchoolImportFileHandler]: "+counter+" records imported");
		
		return true;
	}
	
	public void setImportFile(ImportFile file) throws RemoteException {
		this.importFile = file;
	}
	
	public void setRootGroup(Group rootGroup) throws RemoteException {
	}
	
	public List getFailedRecords() throws RemoteException {
		return failedRecords;
	}
	
	public List getSuccessRecords() throws RemoteException {
		return new ArrayList();
	}
}