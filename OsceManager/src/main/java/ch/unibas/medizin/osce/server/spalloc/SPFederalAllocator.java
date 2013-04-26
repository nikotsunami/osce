package ch.unibas.medizin.osce.server.spalloc;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.PatientInRole;

public class SPFederalAllocator {

	private final OsceDay osceDay;
		
	private List<SPModel> patientInRoleModelList = new ArrayList<SPModel>();
	
	private List<SPModel> patientInRoleModelBreakList = new ArrayList<SPModel>();
	
	private List<SPModel> patientInRoleModelSlotList = new ArrayList<SPModel>();

	public SPFederalAllocator(OsceDay osceDay) {		
		
		this.osceDay = osceDay;
		
	}
	
	public void allocateSp()
	{
		List<OscePost> oscePostList = OscePost.findOscePostByOsceDay(osceDay.getId());
				
		//iterate for all post of that osce day
		for (OscePost oscePost : oscePostList)
		{
			//System.out.println("OSCE POST NAME : " + oscePost.getStandardizedRole().getShortName());
			patientInRoleModelList.clear();
			
			//load all sp for that oscepost and osce day
			List<PatientInRole> patientInRoleList = PatientInRole.findPatientInRoleByOsceDayAndOscePostOrderById(osceDay.getId(), oscePost.getId());
			
			for (PatientInRole pir : patientInRoleList)
			{
				//System.out.println("PIR ID : " + pir.getId() + " SP NAME : " + pir.getPatientInSemester().getStandardizedPatient().getName() + " : " + pir.getPatientInSemester().getStandardizedPatient().getPreName());
				patientInRoleModelList.add(new SPModel(pir, 0));
			}
			
			//load assignment of particular oscepost for all rotation
			List<Assignment> postAssList = Assignment.findAssignmentByOscePostAndOsceDay(osceDay.getId(), oscePost.getId());
			//System.out.println("postAssList SIZE : " + postAssList.size());
			
			patientInRoleModelBreakList.clear();
			patientInRoleModelSlotList.clear();
			
			
			for (Assignment ass : postAssList)
			{
				//iterate loop and compare emergency value and swap the pir with break pir
				SPModel removePir = null;
				SPModel newPir = null;
							
				for (SPModel spBreakModel : patientInRoleModelBreakList)
				{
					int breakPirValue = spBreakModel.getValue();
					
					for (SPModel spModel : patientInRoleModelSlotList)
					{
						int pirValue = spModel.getValue();
						
						if(breakPirValue < pirValue)
						{
							removePir = spBreakModel;
							newPir = spModel;
							
							break;
						}
					}
				}				
							
				if(newPir != null && removePir != null)
				{
					List<SPModel> tempModel = new ArrayList<SPModel>();
					
					for (SPModel spModel : patientInRoleModelList)
					{
						if(spModel.getPatientInRole().getId() == newPir.getPatientInRole().getId())
						{
							tempModel.add(removePir);
						}
						else if (spModel.getPatientInRole().getId() == removePir.getPatientInRole().getId())
						{
							tempModel.add(newPir);
						}
						else
						{
							tempModel.add(spModel);
						}
					}
					
					patientInRoleModelList.clear();
					patientInRoleModelList = tempModel;
				}
				
				//load assignment for particular rotation for rotation wise
				List<Assignment> assList = Assignment.findAssignmentByOscePostAndOsceDayAndTimeStartAndTimeEnd(osceDay.getId(), oscePost.getId(), ass.getTimeStart(), ass.getTimeEnd(), ass.getSequenceNumber());
				//System.out.println("assList SIZE : " + assList.size());
				
				int i = 0;
				
				patientInRoleModelBreakList.clear();
				patientInRoleModelSlotList.clear();
				
				for (i=0; i<assList.size(); i++)
				{
					if (i < patientInRoleModelList.size())
					{
						Assignment assignment = assList.get(i);
						PatientInRole pir = patientInRoleModelList.get(i).getPatientInRole();
						
						if (assignment == null)
							continue;
							
						assignment.setPatientInRole(pir);
						assignment.persist();
						
						int value = patientInRoleModelList.get(i).getValue();
						
						if(assignment.getOscePostRoom() == null)
						{							
							patientInRoleModelBreakList.add(new SPModel(pir, value));
						}
						else
						{
							value += 1;
							patientInRoleModelSlotList.add(new SPModel(pir, value));
						}
						
						patientInRoleModelList.get(i).setValue(value);
					}
				}
			}
					
		}
	}
	
	public class SPModel{
		
		private PatientInRole patientInRole;
		
		private Integer value;
		
		public SPModel(PatientInRole pir, Integer val) {
			this.patientInRole = pir;
			this.value = val;
		}

		public PatientInRole getPatientInRole() {
			return patientInRole;
		}

		public void setPatientInRole(PatientInRole patientInRole) {
			this.patientInRole = patientInRole;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}		
	}
}
