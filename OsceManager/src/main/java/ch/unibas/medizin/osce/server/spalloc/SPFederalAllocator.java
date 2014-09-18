package ch.unibas.medizin.osce.server.spalloc;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.shared.PostType;

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
		Boolean flag = Assignment.clearSPAssignmentByOsceDay(osceDay.getId());
		
		if (flag == true)
		{
			boolean isAscending = false;
			for (OsceSequence osceSequence : osceDay.getOsceSequences())
			{
				isAscending = !isAscending;
				
				List<OscePost> oscePostList = OscePost.findOscePostByOsceSequenceId(osceSequence.getId());
				
				//iterate for all post of that osce day
				for (OscePost oscePost : oscePostList)
				{
					/*if (oscePost.getStandardizedRole() != null)
						System.out.println("OSCE POST NAME : " + oscePost.getStandardizedRole().getShortName());*/
				
					patientInRoleModelList.clear();
					
					//load all sp for that oscepost and osce day
					List<PatientInRole> patientInRoleList = PatientInRole.findPatientInRoleByOsceSequenceAndOscePostOrderById(osceSequence.getId(), oscePost.getId(), isAscending);
					
					if (PostType.DUALSP.equals(oscePost.getOscePostBlueprint().getPostType()))
					{
						List<PatientInRole> dualSpPatient = new ArrayList<PatientInRole>();
						List<PatientInRole> dualSpSupportive = new ArrayList<PatientInRole>();
						for (PatientInRole pir : patientInRoleList)
						{
							if (Boolean.TRUE.equals(pir.getIsSupportive()))
								dualSpSupportive.add(pir);
							else
								dualSpPatient.add(pir);
						}
						
						for (int i=0; i<dualSpPatient.size(); i++)
						{
							PatientInRole pir = dualSpPatient.get(i);
							PatientInRole dualPir = null;
							if (i < dualSpSupportive.size())
								dualPir = dualSpSupportive.get(i);
							
							String pname = pir.getPatientInSemester().getStandardizedPatient().getName() + " " + pir.getPatientInSemester().getStandardizedPatient().getPreName();
							String sname = dualPir.getPatientInSemester().getStandardizedPatient().getName() + " " + dualPir.getPatientInSemester().getStandardizedPatient().getPreName();
							patientInRoleModelList.add(new SPModel(pir, dualPir, 0));
							
						}
					}
					else
					{
						for (PatientInRole pir : patientInRoleList)
						{
							//System.out.println("PIR ID : " + pir.getId() + " SP NAME : " + pir.getPatientInSemester().getStandardizedPatient().getName() + " : " + pir.getPatientInSemester().getStandardizedPatient().getPreName());
							patientInRoleModelList.add(new SPModel(pir, null, 0));
						}
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
						List<Assignment> assList = new ArrayList<Assignment>();
						//load assignment for particular rotation for rotation wise
						if (PostType.DUALSP.equals(oscePost.getOscePostBlueprint().getPostType()))
							assList = Assignment.findAssignmentByOscePostAndOsceDayAndTimeStartAndTimeEnd(osceDay.getId(), oscePost.getId(), ass.getTimeStart(), ass.getTimeEnd(), ass.getSequenceNumber(), (patientInRoleModelList.size() * 2));
						else
							assList = Assignment.findAssignmentByOscePostAndOsceDayAndTimeStartAndTimeEnd(osceDay.getId(), oscePost.getId(), ass.getTimeStart(), ass.getTimeEnd(), ass.getSequenceNumber(), patientInRoleModelList.size());
						//System.out.println("assList SIZE : " + assList.size());
						
						int i = 0;
						
						patientInRoleModelBreakList.clear();
						patientInRoleModelSlotList.clear();
						
						int index = 0;
						
						for (i=0; i<assList.size(); i++)
						{
							if (index < patientInRoleModelList.size())
							{
								Assignment assignment = assList.get(i);
								PatientInRole pir = patientInRoleModelList.get(index).getPatientInRole();
								PatientInRole dualPir = patientInRoleModelList.get(index).getDualPatientInRole();
								
								if (assignment == null)
									continue;
								
								//System.out.println("PIR ID : " + pir.getId() + " SP NAME : " + pir.getPatientInSemester().getStandardizedPatient().getName() + " : " + pir.getPatientInSemester().getStandardizedPatient().getPreName());
								
								int value = patientInRoleModelList.get(index).getValue();
								
								if(assignment.getOscePostRoom() == null)
								{	
									PatientInRole breakPir = PatientInRole.findPatientInRoleByPatientInSemesterAndOscePostNull(pir.getPatientInSemester().getId());
									
									if (breakPir != null)
									{
										assignment.setPatientInRole(breakPir);
										assignment.persist();
									}
								
									if (PostType.DUALSP.equals(oscePost.getOscePostBlueprint().getPostType()) && dualPir != null)
									{
										i = i + 1;
										if (i < assList.size())
										{
											PatientInRole dualBreakPir = PatientInRole.findPatientInRoleByPatientInSemesterAndOscePostNull(dualPir.getPatientInSemester().getId());
											Assignment dualSpAss = assList.get(i);
											if (assignment.getTimeStart().equals(dualSpAss.getTimeStart()) && assignment.getTimeEnd().equals(dualSpAss.getTimeEnd()) && assignment.getSequenceNumber().equals(dualSpAss.getSequenceNumber()))
											{
												dualSpAss.setPatientInRole(dualBreakPir);
												dualSpAss.persist();
											}											
										}
									}
									
									patientInRoleModelBreakList.add(new SPModel(pir, dualPir, value));
								}
								else
								{
									assignment.setPatientInRole(pir);
									assignment.persist();
									
									if (PostType.DUALSP.equals(oscePost.getOscePostBlueprint().getPostType()) && dualPir != null)
									{
										i = i + 1;
										if (i < assList.size())
										{
											Assignment dualSpAss = assList.get(i);
											if (assignment.getTimeStart().equals(dualSpAss.getTimeStart()) && assignment.getTimeEnd().equals(dualSpAss.getTimeEnd()) && assignment.getSequenceNumber().equals(dualSpAss.getSequenceNumber()))
											{
												dualSpAss.setPatientInRole(dualPir);
												dualSpAss.persist();
											}											
										}
									}
									value += 1;
									patientInRoleModelSlotList.add(new SPModel(pir, dualPir, value));
								}
								
								patientInRoleModelList.get(index).setValue(value);
								
								index = index + 1;
							}
						}
					}
							
				}
			
			}
		}
	}
	
	public class SPModel{
		
		private PatientInRole patientInRole;
		
		private PatientInRole dualPatientInRole;
		
		private Integer value;
		
		public SPModel(PatientInRole pir, PatientInRole dualPir, Integer val) {
			this.patientInRole = pir;
			this.value = val;
			this.dualPatientInRole = dualPir;
		}
		
		public PatientInRole getDualPatientInRole() {
			return dualPatientInRole;
		}
		
		public void setDualPatientInRole(PatientInRole dualPatientInRole) {
			this.dualPatientInRole = dualPatientInRole;
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
