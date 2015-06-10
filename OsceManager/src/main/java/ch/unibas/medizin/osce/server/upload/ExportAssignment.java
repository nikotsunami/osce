package ch.unibas.medizin.osce.server.upload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.Room;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.RoleTypes;

public class ExportAssignment  extends HttpServlet {
	
	private static Logger Log = Logger.getLogger(UploadServlet.class);
	
	private ServletConfig servletConfig;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Log.info("ExportAssignment doGet :osceId :" + request.getParameter("osceId"));
		
		String osceId=request.getParameter("osceId");
		String type=request.getParameter("type");
		String fileName=createHtml(new Long(osceId), new Integer(type));
		
		try{
			
				servletConfig=getServletConfig();
				
				 response.setContentType("application/x-download");
				 if(new Integer(type) ==0)
				    response.setHeader("Content-Disposition", "attachment; filename=" + "assignment_student_"+osceId+".html");
				 else if(new Integer(type) ==1)
					 response.setHeader("Content-Disposition", "attachment; filename=" + "assignment_sp_"+osceId+".html");
				 else
					 response.setHeader("Content-Disposition", "attachment; filename=" + "assignment_sp_plans_"+osceId+".xlsx");


				Log.info("path :" + fileName);
				//String file=OsMaFilePathConstant.ROLE_IMAGE_FILEPATH+path;
				Log.info(" file :" + fileName);
				
				OutputStream out = response.getOutputStream();
				FileInputStream in = new FileInputStream(fileName);
				
				
				
				byte[] buffer = new byte[4096];
				int length;
				while ((length = in.read(buffer)) > 0){
				    out.write(buffer, 0, length);
				}
				in.close();
				
				File htmlFile=new File(fileName);
				htmlFile.delete();
				out.flush();
			
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		
		 
		
	}
	/*If type =2 export SP Plans*/
	public  String createHtml(Long osceId,int type)
	{
		if(type==2)
		{
			String fileName=createExcel(osceId, type);
			return fileName;
		}
		else
		{
		Osce osce=Osce.findOsce(osceId);
		
		int postLength = osce.getPostLength();
		
		List<OsceDay> osceDays=osce.getOsce_days();
		
		Document doc=createDocument();
		Element root = doc.createElement("osceDays");
		
		//append root to document
		doc.appendChild(root);
		
		int dayOffsetRotation=0;
		int startRotation=0;
		int rotationOffSet=0;
		for(int c=0;c<osceDays.size();c++)
		{
			OsceDay osceDay=osceDays.get(c);
			Element osceDayElement=createEmptyChildNode("osceDay",doc,root);
			
			
			
			String osceDayIDNodeValue="Day "+new SimpleDateFormat("dd.MM.yyyy").format(osceDay.getOsceDate());
			
			createChildNode("osceDayID", osceDayIDNodeValue, doc, osceDayElement);
			
			List<OsceSequence> osceSequences=osceDay.getOsceSequences();
			
			
			
			for(int a=0;a<osceSequences.size();a++)
			{
				Element parcoursElement=createEmptyChildNode("parcours",doc,osceDayElement);
				
				OsceSequence osceSeq=osceSequences.get(a);
				List<Course> courses=osceSeq.getCourses();
				
				if(a!=0)
				{
					startRotation=startRotation+osceSequences.get(a-1).getNumberRotation();
					
				}
				//dayOffsetRotation=dayOffsetRotation+osceSeq.getNumberRotation()+startRotation;
				startRotation=rotationOffSet;
				rotationOffSet = rotationOffSet +  osceSeq.getNumberRotation();
				
				for(int i=0;i<courses.size();i++)
				{
					Course course=courses.get(i);
					Element parcourElement=createEmptyChildNode("parcour",doc,parcoursElement);
					//createChildNode("parcourCss", "accordion-title-selected"+course.getColor(), doc, parcourElement);
					createChildNode("parcourColor", course.getColor(), doc, parcourElement);
					
					Element postsElement=createEmptyChildNode("posts",doc,parcourElement);
					
					//List<OscePost> oscePosts=osceSeq.getOscePosts();
					List<OscePost> oscePosts = OscePostRoom.findOscePostByCourseId(course.getId());
					
					Map<Long, Long> postWiseRoomMap = OscePostRoom.findRoomByOscePostAndCourse(oscePosts, course.getId());
					
					for(OscePost oscePost:oscePosts)
					{
						Element postElement=createEmptyChildNode("post",doc,postsElement);
						createChildNode("postName", "Post " +oscePost.getSequenceNumber(), doc, postElement);
						
						OscePostRoom postRoom=OscePostRoom.findPostRoom(oscePost.getId(), course.getId());
						if(postRoom !=null && postRoom.getRoom() != null )
							createChildNode("postRoom", postRoom.getRoom().getRoomNumber(), doc, postElement);
						else
							createChildNode("postRoom", "-", doc, postElement);
						
						if(oscePost.getOscePostBlueprint().getPostType() != PostType.BREAK)
							createChildNode("standardizedRole", oscePost.getStandardizedRole().getLongName(), doc, postElement);
						else
							createChildNode("standardizedRole", "-", doc, postElement);						
					}
					
					//student break
					boolean islogicalStudentBreakAssignmentsExist=false;
					if(type==0)
					{
						List<Assignment> logicalStudentBreakAssignments=Assignment.retrieveLogicalStudentInBreak(osceDay.getId(), course.getId());
						if(logicalStudentBreakAssignments.size()==0)
							islogicalStudentBreakAssignmentsExist=false;
						else
							islogicalStudentBreakAssignmentsExist=true;
						if(islogicalStudentBreakAssignmentsExist)
						{
					Element postElement=createEmptyChildNode("post",doc,postsElement);
					createChildNode("postName", "Student Break", doc, postElement);
					}
					}
					
					
					//find examiners, rotation and course wise from Assignment table			
					Element rotationsElement=createEmptyChildNode("rotations",doc,parcourElement);
					
					
					
					
					for(int j=startRotation;j<(rotationOffSet);j++)
					{
						
						Element rotationElement=createEmptyChildNode("rotation",doc,rotationsElement);
						
						createChildNode("type", new Integer(type).toString(), doc, rotationElement);
						if(type==0) //for student only show rotattion
						{
							
							createChildNode("rotationId", "rotation "+(j+1), doc, rotationElement);
						}
						//retrieve examiners
						Element examinersElement=createEmptyChildNode("examiners",doc,rotationElement);
						for(OscePost oscePost:oscePosts)
						{
							Element examinerElement=createEmptyChildNode("examiner",doc,examinersElement);
							Assignment assignment=Assignment.findExaminersRoationAndCourseWise(osceDay.getId(), j, course.getId(), oscePost.getId());
							
							Doctor examiner=null;
							if(assignment != null)
							 examiner=assignment.getExaminer();
							
							String examinerName="-";
							if(examiner !=null)
								examinerName=examiner.getPreName() +" "+ examiner.getName();
							else
								examinerName="NA";
							
							if(type==0)  //for student only show rotattion
							createChildNode("examinerName", examinerName, doc, examinerElement);
						}
						
						//Student break
						
						if(type==0)
						{
							if(islogicalStudentBreakAssignmentsExist)
							{
							Element examinerElement=createEmptyChildNode("examiner",doc,examinersElement);
							createChildNode("examinerName", "NA", doc, examinerElement);
							}
							
						}
						List<Date> timeStarts=null;
						if(type==0)
							timeStarts=Assignment.findDistinctTimeStartRotationWise(osceDay.getId(), j,type);
						else
						{
							//timeStarts=Assignment.findDistinctTimeStartRotationWise(osceDay.getId(), j,0);
							timeStarts=Assignment.findDistinctSPTimeStartRotationWise(osceDay.getId(), j);
						}
						
						List<Date> timeEnds=null;
						if(type==1)
						{
							//timeEnds=Assignment.findDistinctTimeStartRotationWise(osceDay.getId(), j,type);
							timeEnds=Assignment.findDistinctSPTimeEndCourseAndRotationWise(course.getId(), osceDay.getId() , j);
						}
						
						//retrieve startEndtimes and student for particular course and rotation
						List<Assignment> assignments=Assignment.findAssignmentRotationAndCourseWise(osceDay.getId(), j, course.getId(),type);
						Element startEndTimesElement=createEmptyChildNode("startEndTimes",doc,rotationElement);
						List<PatientInRole> patientInRoleList=new ArrayList<PatientInRole>();
						
						for(int d=0;d<timeStarts.size();d++)
						{
							
							ArrayList<Integer> numOfRow=new ArrayList<Integer>();
							Date timeStart=timeStarts.get(d);
							Date timeEnd=null;
							if(type==1)
								timeEnd=timeEnds.get(d);
							
							Element startEndTimeElement=createEmptyChildNode("startEndTime",doc,startEndTimesElement);
							//createChildNode("parcourColor", course.getColor(), doc, startEndTimeElement);
							String timeStartValue=String.format("%tR", timeStart);
									//NumberFormat.getFormat("00").format(timeStart.getHours()) +":" + NumberFormat.getFormat("00").format(timeStart.getMinutes());
							
							Element studentsElement=createEmptyChildNode("students",doc,startEndTimeElement);
							
							Date endTime=null;
							
							//one more loop because of student break (k=num of post +1)
							int looplength=0;
							
							if(type==0 && islogicalStudentBreakAssignmentsExist)
							{
								looplength=oscePosts.size()+1;
							}
							else
							{
								looplength=oscePosts.size();
							}
							for(int k=0;k<looplength;k++)
							{
								//sp break change
								OscePost oscePost=null;
								if((type==0 && oscePosts.size() != k) || type ==1)
									oscePost=oscePosts.get(k);
								
								boolean found=false;
								boolean spEqlToNxtSlot=true;
								
								String spBreakName="";
								
								Assignment prevSPAssignment=null;
								
								for(int b=0;b<assignments.size();b++)
								{
									Assignment assignment=assignments.get(b);
									
									//student break
									OscePost assignmentPost=null;
									if((oscePosts.size()  != k  && type==0 && assignment.getOscePostRoom() != null) || type ==1)
									  assignmentPost=assignment.getOscePostRoom().getOscePost();
									
											//for student
											//if(type ==0 && assignment.getOscePostRoom() !=null && assignmentPost != null && oscePost !=null && oscePost.getId() == assignmentPost.getId() && assignment.getTimeStart().equals(timeStart)  )
											if(type ==0 && assignment.getOscePostRoom() !=null && assignmentPost != null && oscePost !=null &&  ( assignment.getOscePostRoom().getRoom() == null ? (oscePost.getId().equals(assignmentPost.getId())) : (assignment.getOscePostRoom().getRoom().getId().equals(postWiseRoomMap.get(oscePost.getId()))) ) && assignment.getTimeStart().equals(timeStart)  )
											{
												Element studentElement=createEmptyChildNode("student",doc,studentsElement);
												Student student=assignment.getStudent();
												String studentName="-";
												if(student != null)
													studentName=student.getPreName() +" "+ student.getName();
												else
													studentName="s"+assignment.getSequenceNumber();
												
												endTime=assignment.getTimeEnd();
												
												createChildNode("studentName", studentName, doc, studentElement);
											
												found=true;
												break;
											}
											
											//for SP
											 //if(type ==1 && timeEnd != null && assignmentPost != null && oscePost !=null &&  oscePost.getId() == assignmentPost.getId() && (assignment.getTimeEnd().equals(timeEnd) || assignment.getTimeEnd().after(timeEnd)) &&(assignment.getTimeStart().equals(timeStart) || assignment.getTimeStart().before(timeStart)))
											if(type ==1 && timeEnd != null && assignmentPost != null && oscePost !=null && oscePost.getId().equals(assignmentPost.getId()) && (assignment.getTimeEnd().equals(timeEnd) || assignment.getTimeEnd().after(timeEnd)) &&(assignment.getTimeStart().equals(timeStart) || assignment.getTimeStart().before(timeStart)))
											{
												
												 /*prevSPAssignment=assignment;
												 
												 if(prevSPAssignment!=null && assignment.getPatientInRole().equals(prevSPAssignment.getPatientInRole()))
												 {
													 	d++;
														timeEnd=timeEnds.get(d);
														found=true;
														break;
												 }*/
												 
												 //logic for merge sp
												 if( d!=timeStarts.size()-1)//not last
												 {
													 
													 
													 int numOfRowToMerge=0;
													 for(int temp=d+1;temp<timeStarts.size();temp++)
													 {
														 Date timeStartTemp=timeStarts.get(temp);
														 Assignment nxtSPassignment=Assignment.findNxtSPSlot(osceDay.getId(), osceSeq.getId(), course.getId(), assignment.getOscePostRoom().getOscePost().getId(), timeStartTemp);
														 
														 
														 if(nxtSPassignment==null)
														 {
															 spEqlToNxtSlot=false;
															 break;
														 }
														 if(nxtSPassignment != null && spEqlToNxtSlot && nxtSPassignment.getPatientInRole()==null)// sp not assigned
														 {
															 if(nxtSPassignment.getSequenceNumber()!= null && !nxtSPassignment.getSequenceNumber().equals(assignment.getSequenceNumber()))
															 {
																 spEqlToNxtSlot=false;
																 break;
															 }
														 }
														 if(nxtSPassignment != null && spEqlToNxtSlot && nxtSPassignment.getPatientInRole() != null && !nxtSPassignment.getPatientInRole().equals(assignment.getPatientInRole()))
														 {
															 spEqlToNxtSlot=false;
															 break;
														 }
														 
														 if(spEqlToNxtSlot)
														 {
															 numOfRowToMerge++;
															
														 }
													 }
													 
													 numOfRow.add(numOfRowToMerge);
													 
												 }
												
												 
												Element studentElement=createEmptyChildNode("student",doc,studentsElement);
												PatientInRole patientInRole=assignment.getPatientInRole();
												String studentName="-";
												if(patientInRole != null)
													studentName=patientInRole.getPatientInSemester().getStandardizedPatient().getPreName() + " " + patientInRole.getPatientInSemester().getStandardizedPatient().getName();
												else
													studentName="SP"+assignment.getSequenceNumber();
												
												endTime=assignment.getTimeEnd();
												
												if (assignmentPost != null && assignmentPost.getOscePostBlueprint() != null && PostType.DUALSP.equals(assignmentPost.getOscePostBlueprint().getPostType()))
												{
													if ((b+1) < assignments.size())
													{
														Assignment ass = assignments.get((b+1));
														PatientInRole patientInRole1=ass.getPatientInRole();
														
														if(patientInRole1 != null)
														{
															StandardizedPatient standardizedPatient = patientInRole1.getPatientInSemester().getStandardizedPatient();
															if (standardizedPatient != null)
															{
																studentName = studentName + ", " + standardizedPatient.getPreName() + " " + standardizedPatient.getName();
															}
														}
														else
															studentName = studentName + ", " + "SP" + assignment.getSequenceNumber();
														
														b = b + 1;
													}
												}
												
												createChildNode("studentName", studentName, doc, studentElement);
												
												found=true;
												//break;
											}
											
											//for student break
												if(assignment.getOscePostRoom() == null && oscePost== null && assignmentPost == null && (assignment.getTimeStart().equals(timeStart)) && type ==0)
												{
													
													Element studentElement=createEmptyChildNode("student",doc,studentsElement);
													Student student=assignment.getStudent();
													String studentName="-";
													if(student != null)
														studentName=student.getPreName() +" "+ student.getName();
													else
														studentName="s"+assignment.getSequenceNumber();
													
													endTime=assignment.getTimeEnd();
													
													createChildNode("studentName", studentName, doc, studentElement);
												
													found=true;
													break;
													
												}
											
										/*	else
											{
												Element studentElement=createEmptyChildNode("student",doc,studentsElement);
												createChildNode("studentName", "-", doc, studentElement);
											}*/
										/*	else if(k==oscePosts.size()-1)
											{
												Element studentElement=createEmptyChildNode("student",doc,studentsElement);
												createChildNode("studentName", "-", doc, studentElement);
												movetoNext=true;
											//	break;
											}
										*/
								/*		if(movetoNext)
										{
											break;
										}
									*/
								}
								
								// for sp break
							/*	if(oscePost==null && type==1)
								{
									Element spBreakElement=createEmptyChildNode("student",doc,studentsElement);
									createChildNode("studentName", spBreakName, doc, spBreakElement);
								}
								*/
								if(!found && type==0)
								{	
									Element studentElement=createEmptyChildNode("student",doc,studentsElement);
									createChildNode("studentName", "NA", doc, studentElement);
								}
								else if(!found && type==1)
								{	
									Element studentElement=createEmptyChildNode("student",doc,studentsElement);
									createChildNode("studentName", "NA", doc, studentElement);
								}
							}
							
							//merge
							if(type==1)
							{
								if(d!=timeStarts.size()-1)
								{
									if(numOfRow.size()>0)
									{
									//d=d+Collections.min(numOfRow);
									timeEnd=timeEnds.get(d);
									numOfRow.clear();
									}
									/*else
										d++;*/
									
								}
								
								
							}
							
							//if(type==0)
							if(type==0)
							{
								if (endTime == null)
									endTime = dateAddMin(timeStart, postLength);
								
								timeStartValue=timeStartValue +"-"+ String.format("%tR", endTime);
									
								createChildNode("startEndTimeValue", timeStartValue, doc, startEndTimeElement);
							}
							else if(endTime !=null)
							{
								if (timeEnd == null)
									timeEnd = dateAddMin(timeStart, postLength);
								
								timeStartValue=timeStartValue +"-"+ String.format("%tR", timeEnd);
								createChildNode("startEndTimeValue", timeStartValue, doc, startEndTimeElement);
							}
						}
					}
					
					//after all rotations and when parcour is last than insert SP Break
					if(i==courses.size()-1 && type==1)
					{
						List<Assignment> spBreakAssignments=Assignment.retrieveAssignmentOfLogicalBreakPost(osceDay.getId(), osceSeq.getId());
						
						
				//		Element spBreakRotationElement=createEmptyChildNode("spBreakRotation",doc,parcourElement);
						if(spBreakAssignments.size() > 0)
						{
						//post count to set column span of sp break
						createChildNode("postCount", String.valueOf(osceSeq.getOscePosts().size() + 1), doc, parcourElement);
						//Changed for OMS-158
						Room reserveSPRoom = osceSeq.getOsceDay().getReserveSPRoom();
						if(reserveSPRoom!=null){
							createChildNode("spBreak", "Reserve (" + reserveSPRoom.getRoomNumber()+ ")", doc, parcourElement);
						}else{
							createChildNode("spBreak", "Reserve (Not assigned)", doc, parcourElement);
						}
						
						Element spBreakRotationsElement=createEmptyChildNode("spBreakrotations",doc,parcourElement);
						
							for(int j=startRotation;j<(rotationOffSet);j++)
							{
								Element rotationElement=createEmptyChildNode("rotation",doc,spBreakRotationsElement);
								
								createChildNode("rotationPostCount", String.valueOf(osceSeq.getOscePosts().size()+1), doc, rotationElement);
								createChildNode("type", new Integer(type).toString(), doc, rotationElement);
								if(type==0)
								{
									
									createChildNode("rotationId", "rotation "+(j+1), doc, rotationElement);
								}
							//	List<Assignment> spBreakAssignments=Assignment.retrieveAssignmentOfLogicalBreakPost(osceDay.getId(), osceSeq.getId());
								
								//rotation
								//List<Date> timeStarts=Assignment.findDistinctTimeStartRotationWise(osceDay.getId(), j,0);
								List<Date> timeStarts=Assignment.findDistinctSPTimeStartRotationWise(osceDay.getId(), j);
								List<Date> timeEnds=null;
								
								//timeEnds=Assignment.findDistinctTimeStartRotationWise(osceDay.getId(), j,1);
								timeEnds=Assignment.findDistinctSPTimeEndCourseAndRotationWise(course.getId(), osceDay.getId() , j);
							
								
								//time start and sp in break
								Element startEndTimesElement=createEmptyChildNode("startEndTimes",doc,rotationElement);
								for(int l=0;l<timeStarts.size();l++)
								{
									Date timeStart=timeStarts.get(l);
									Date timeEnd=null;
									
										timeEnd=timeEnds.get(l);
									Element startEndTimeElement=createEmptyChildNode("startEndTime",doc,startEndTimesElement);
									
									//Assignment assignment=spBreakAssignments.get(l);
									
									//Date timeStart=assignment.getTimeStart();
									//Date timeEnd=assignment.getTimeEnd();
									
									String commaSeperatedSpBreak="";
									boolean spEqlToNxtSlot=true;
									ArrayList<Integer> rowNum=new ArrayList<Integer>();
											
									for(int k=0;k<spBreakAssignments.size();k++)
									{
										Assignment assignment=spBreakAssignments.get(k);
										
										 
										
										
										
										if((assignment.getTimeEnd().equals(timeEnd) || assignment.getTimeEnd().after(timeEnd)) &&(assignment.getTimeStart().equals(timeStart) || assignment.getTimeStart().before(timeStart)))
										//if(assignment.getTimeStart().equals(timeStart))
										{
											
											//logic for merge sp
											 if( l!=timeStarts.size()-1)//not last
											 {
												
												 
												 int numOfRowToMerge=0;
												 for(int temp=l+1;temp<timeStarts.size();temp++)
												 {
													 Date timeStartTemp=timeStarts.get(temp);
													 List<PatientInRole> nxtSPassignments=Assignment.findNxtSPLogicalBreak(osceDay.getId(), osceSeq.getId(),  timeStartTemp);
													 
													 
													 if(nxtSPassignments==null || nxtSPassignments.size()==0)
													 {
														 spEqlToNxtSlot=false;
														 break;
													 }
													 if(assignment.getPatientInRole()==null)
													 {
														 spEqlToNxtSlot=false;
														 break;
													 }
													 if(nxtSPassignments != null && spEqlToNxtSlot && !nxtSPassignments.contains(assignment.getPatientInRole()))
													 {
														 spEqlToNxtSlot=false;
														 break;
													 }
													 
													 if(spEqlToNxtSlot)
													 {
														 numOfRowToMerge++;
														
													 }
												 }
												 
												 rowNum.add(numOfRowToMerge);
												 
											 }
											
											if(commaSeperatedSpBreak.equals(""))
											{
												if(assignment.getPatientInRole() !=null )
													commaSeperatedSpBreak=assignment.getPatientInRole().getPatientInSemester().getStandardizedPatient().getPreName() +" "+ assignment.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName();
												else
													commaSeperatedSpBreak="SP"+assignment.getSequenceNumber();
											}
											else
											{
												if(assignment.getPatientInRole() !=null )
													commaSeperatedSpBreak=commaSeperatedSpBreak+", "+assignment.getPatientInRole().getPatientInSemester().getStandardizedPatient().getPreName() +" "+ assignment.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName();
												else
													commaSeperatedSpBreak=commaSeperatedSpBreak+", "+"SP"+assignment.getSequenceNumber();
											}
										}
									/*	if(!(k!=spBreakAssignments.size()-1 && assignment.getTimeStart().equals(spBreakAssignments.get(k).getTimeStart())))
										{
											l=k;
											break;
										}
										*/
									}
									
									//merge
									if(type==1)
									{
										if(l!=timeStarts.size()-1)
										{
											if(rowNum.size()>0)
											{
											//l=l+Collections.min(rowNum);
											timeEnd=timeEnds.get(l);
											rowNum.clear();
											}
											/*else
												d++;*/
											
										}
										
										
									}
									createChildNode("startEndTimeValue", String.format("%tR", timeStart) +"-" + String.format("%tR", timeEnd), doc, startEndTimeElement);
									createChildNode("spBreakPostCount", String.valueOf(osceSeq.getOscePosts().size()), doc, startEndTimeElement);
									
									createChildNode("commaSeperatedSpBreak", commaSeperatedSpBreak, doc, startEndTimeElement);
								}
							}
						}
					}
				}
				
				
				
				//insert SP Break
			//	if(spBreakAssignments.size() > 0)
				
			}
			
			
		}
		
		return saveXML(doc);
		}
	}
	/*create xls for Sp Plans*/
	private String createExcel(Long osceId, int type) {
		
		
		 String path=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML);
         String outputFileName =path +System.currentTimeMillis()+".xlsx";
		 
         try
         {
        	 //create xls file
	         Workbook wb = new XSSFWorkbook();
	         Sheet sheet=wb.createSheet("Einsatzplan Simulationspatienten");
	         sheet.setFitToPage(true);
	         // create header inside sheet
	         //the header row: centered text in 48pt font
	        Row headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(80);
	        Cell titleCell = headerRow.createCell(0);
	        titleCell.setCellValue("Einsatzplan Simulationspatienten");
	        
	        CellStyle titleStyle;
	        Font titleFont = wb.createFont();
	        titleFont.setFontHeightInPoints((short)18);
	        titleFont.setColor(IndexedColors.BLACK.getIndex());
	        titleStyle = wb.createCellStyle();
	        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
	        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	        titleStyle.setFont(titleFont);
	        titleCell.setCellStyle(titleStyle);
	        
	        
	       //  titleCell.setCellStyle(styles.get("title"));
	       // sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$N$1"));
	        
	        //osce
	        Osce osce=Osce.findOsce(osceId);
	        List<OsceDay> osceDays=osce.getOsce_days();
	        
	        List<List<Object>> completeExcel=new ArrayList<List<Object>>();
	        
	        for(OsceDay osceDay:osceDays)
	        {
	        	//osce day
	        	
	        	List<Object> excelWholeDayDetail=new ArrayList<Object>();
	        	completeExcel.add(excelWholeDayDetail);
	        	String osceDayIDNodeValue=new SimpleDateFormat("dd.MM.yyyy").format(osceDay.getOsceDate());
	        	
	        	String osceLable = /*new EnumRenderer<StudyYears>().render(osce.getStudyYear().name()) +*/  
						/*+ new EnumRenderer<Semesters>().render(osce.getSemester().getSemester())*/ osceDay.getOsce().getName()+"  " + osceDayIDNodeValue;
	        	
	        	OsceDayDetail osceDayDetail=new OsceDayDetail();
	        	osceDayDetail.setHeaderLabel(osceLable);
	        	excelWholeDayDetail.add(osceDayDetail);
	        	
		       /* Row osceHeader = sheet.createRow(row++);
		        Cell osceHeaderCell=osceHeader.createCell(0);
		        osceHeaderCell.setCellValue(osceLable);*/
		        
		       List<OsceSequence> osceSeqs=osceDay.getOsceSequences();
		       List<OsceSequenceDetail> osceSeqDetailList=new ArrayList<OsceSequenceDetail>();
		       
		       SequenceRow sequenceRow=new SequenceRow();
		       sequenceRow.setOsceSequenceDetails(osceSeqDetailList);
		       
		       excelWholeDayDetail.add(sequenceRow);
		       //time slot list
		       List<TimeSlotDetail> timeSlotDetails=new ArrayList<TimeSlotDetail>();
		       
		       SPTimeSlotRow spTimeSlotRow=new SPTimeSlotRow();
		       spTimeSlotRow.setSpSlotsPerSeq(timeSlotDetails);
		       excelWholeDayDetail.add(spTimeSlotRow);
		       
		       //column wise remaining detals
		       ExcelDetail excelDetail=new ExcelDetail();
		       List<Object> excelDetails=new ArrayList<Object>();
		       excelDetail.setExcelDetail(excelDetails);
		       excelWholeDayDetail.add(excelDetail);
		        for(int i=0;i<osceSeqs.size();i++)
		        {
		        	OsceSequence osceSeq=osceSeqs.get(i);
		        	
		        	OsceSequenceDetail osceSeqDetail=new OsceSequenceDetail();
		        	
		        	List<OscePost> oscePosts=osceSeq.getOscePosts();
		        	List<OscePost> oscePostWithoutBreak = new ArrayList<OscePost>();
		        	
		        	int numOfBreakPost=0;
		        	for(int postIndex=0;postIndex<oscePosts.size();postIndex++)
		        	{
		        		if(oscePosts.get(postIndex).getOscePostBlueprint().getPostType()==PostType.BREAK)
		        		{
		        			numOfBreakPost++;
		        		}
		        		else
		        		{
		        			oscePostWithoutBreak.add(oscePosts.get(postIndex));
		        		}
		        	}
		        	
		        	//find distinct time sp slots per sequence
		        	TimeSlotDetail timeSlotDetail=new TimeSlotDetail();
		        	//timeSlotDetail.setTimeStarts( Assignment.findDistinctSPSlotPerSeq(osceSeq));
		        	
		        	List<Date> timeStartList=Assignment.findDistinctSPSlotPerSeq(osceSeq);
		        	
		        	timeSlotDetails.add(timeSlotDetail);
		        	List<SPSlot> spSlotList=new ArrayList<SPSlot>();
		        	timeSlotDetail.setSpSlotList(spSlotList);
		        	//find spDetail per sp Slot. dont include break post but include logical break post.order by post
		        	
		        	List<SPColumn> spColumnList=new ArrayList<SPColumn>();
		        	for(int j=0;j<timeStartList.size();j++)
		        	{
		        		Date timeStart=timeStartList.get(j);
		        		List<Assignment> assignments=Assignment.findAssignmentBySplot(osceDay, timeStart);
		        		List<SPDetail> spDetailsList=new ArrayList<SPDetail>();
		        		
		        		List<PostDetail> postDetailList=new ArrayList<PostDetail>();
		        		
		        		SPSlot spSlot=new SPSlot();
		        		spSlot.setTimeStart(timeStart);
		        		spSlot.setOsce(osce);
		        		
		        		
		        		
		        		spSlotList.add(spSlot);
		        		List<RoomDetail> roomDetailList=new ArrayList<RoomDetail>();
		        		
		        		
		        		if(j==0)//include postdetail 
	        			{
		        			
		        			for(int postIndex=0;postIndex<oscePosts.size();postIndex++)
		        			{
		        				for(int courseIndex=0;courseIndex<osceSeq.getCourses().size();courseIndex++)
		        				{
		        					if(oscePosts.get(postIndex).getOscePostBlueprint().getPostType() != PostType.BREAK)
		        					{
			        					PostDetail postDetail=new PostDetail();
				        				//OscePostRoom oscePostRoom=a.getOscePostRoom();
				        				//postDetail.setPatientInRole(a.getPatientInRole());
				        				//logical sp break
				        				//if(oscePostRoom==null)
				        				//{
				        				//	postDetail.setRowSpan(1);
				        				//	postDetail.setOscePostRoom(null);
				        				//}
				        				//else
				        				//{
			        						postDetail.setRowSpan(osceSeq.getCourses().size());
			        						postDetail.setOscePost(oscePosts.get(postIndex));
				        				//	postDetail.setOscePostRoom(oscePostRoom);
				        				//}
				        				postDetailList.add(postDetail);
		        					}
		        				}
		        			}
	        				
	        				
	        			}
		        		
		        		for(Assignment a:assignments)
		        		{
		        			boolean dualSp = false;
		        			if(j==0)
		        			{
		        				PostDetail postDetail=new PostDetail();
		        				OscePostRoom oscePostRoom=a.getOscePostRoom();
		        				postDetail.setPatientInRole(a.getPatientInRole());
		        				//logical sp break
		        				if(oscePostRoom==null)
		        				{
		        					postDetail.setRowSpan(1);
		        					postDetail.setOscePostRoom(null);
		        					postDetail.setReserve(true);
		        					//Added for OMS-158
		        					Room reserveSPRoom = a.getOsceDay().getReserveSPRoom();
		        					if(reserveSPRoom!=null){
		        						postDetail.setReserveRoomNumber(reserveSPRoom.getRoomNumber());	
		        					}else{
		        						postDetail.setReserveRoomNumber("Not assigned");
		        					}
		        					
		        					postDetailList.add(postDetail);
		        				}

		        			}
		        			
		        			spSlot.setTimeEnd(a.getTimeEnd());
		        			
		        			//sp detail
		        			
		        			OscePostRoom oscePostRoom=a.getOscePostRoom();
		        			SPDetail spDetail = null;
		        			
		        			if (oscePostRoom != null && PostType.DUALSP.equals(oscePostRoom.getOscePost().getOscePostBlueprint().getPostType()))
		        				spDetail = checkSPDetailWithOscePostRoomId(spDetailsList, oscePostRoom.getId(), a.getSequenceNumber());
		        			
		        			if (spDetail == null)
		        			{
		        				spDetail=new SPDetail();
			        			spDetail.setOscePostRoom(oscePostRoom);
			        			spDetail.setPatientInRole(a.getPatientInRole());
			        			spDetail.setSequenceNumber(a.getSequenceNumber());
			        			spDetail.setNumOfBreakPost(numOfBreakPost);
			        			spDetail.setOscePosts(oscePostWithoutBreak);
			        			spDetail.setCourses(osceSeq.getCourses());
			        			spDetailsList.add(spDetail);
		        			}
		        			else
		        			{
		        				dualSp = true;
		        				spDetail.setDualPatientInRole(a.getPatientInRole());
		        			}
		        			
		        			if(j==timeStartList.size()-1 && i==osceSeqs.size()-1 && dualSp == false)//append room details at end of last sequence
		        			{
		        				RoomDetail roomDetail=new RoomDetail();
		        				if(a.getOscePostRoom()==null)//logical break
		        				{
		        					roomDetail.setRoomNumber("");
		        				}
		        				else if(a.getPatientInRole()==null)// material post type
		        				{
		        					roomDetail.setRoomNumber("");
		        				}
		        				else
		        				{
		        					roomDetail.setRoom(a.getOscePostRoom().getRoom());
		        				}
		        				roomDetail.setOscePosts(oscePostWithoutBreak);
		        				roomDetail.setNumOfBreakPost(numOfBreakPost);
		        				roomDetail.setOscePostRoom(a.getOscePostRoom());
		        				roomDetail.setCourses(osceSeq.getCourses());
		        				roomDetailList.add(roomDetail);
		        			}
		        			
		        		}
		        		
		        		spSlot.setRotation(Assignment.findRotationNumberFromSPSlot(osceDay, timeStart, spSlot.getTimeEnd()));
		        		
		        		/*merge sp column*/
		        		/*for(int k=0;k<spDetailsList.size();k++)
		        		{
		        			SPDetail spDetail=spDetailsList.get(k);
		        			if(k!=spDetailsList.size()-1)//not last
		        			{
		        				SPDetail spDetailNext=spDetailsList.get(k+1);
		        				
		        				if(spDetail.equals(spDetailNext))
		        				{
		        					spDetailsList.remove(k);
		        					k=0;
		        					
		        				}
		        			}
		        		}*/
		        		
		        		if(j==0)//first column of seq-post column
		        		{
		        			PostColumn postColumn=new PostColumn();
		        			postColumn.setPostDetailList(postDetailList);
		        			excelDetails.add(postColumn);
		        		}
		        		SPColumn spColumn=new SPColumn();
		        		spColumn.setSpDetailList(spDetailsList);
		        		excelDetails.add(spColumn);
		        		spColumnList.add(spColumn);
		        		if(j==timeStartList.size()-1 && i==osceSeqs.size()-1)//append room details at end of last sequence-last column
		        		{
		        			RoomColumn roomColumn=new RoomColumn();
		        			roomColumn.setRoomDetailList(roomDetailList);
		        			excelDetails.add(roomColumn);
		        		}
		        		
		        	}
		        	
		        	//merge SP Column
		        	
		        	for(int index=0;index<spColumnList.size();index++)
		        	{
		        			
		        			List<SPDetail> spDetailList=spColumnList.get(index).getSpDetailList();
		        			
		        			
		        				
		        		List<SPDetail> nextspDetailList=null;
		        		if((index+1) <= spColumnList.size()-1)
		        		{
		        			nextspDetailList=spColumnList.get(index+1).getSpDetailList();
		        			
		        			int spSlotRotation=spSlotList.get(index).getRotation();
		        			int nextspSlotRotation=spSlotList.get(index+1).getRotation();
		        			if(spSlotRotation==nextspSlotRotation && nextspDetailList != null )
			        		{
		        				boolean flag=true;
		        				for(int spIndex=0;spIndex<spDetailList.size();spIndex++)
		        				{
		        					SPDetail spDetail=spDetailList.get(spIndex);
		        					SPDetail nextSlotSP=null;
		        					if (spIndex < nextspDetailList.size())
		        					{
		        						nextSlotSP=nextspDetailList.get(spIndex);
		        					}
		        					
		        					if(!spDetail.equals(nextSlotSP))
		        					{
		        						flag=false;
		        					}
		        				}
		        				if(flag)
		        				{
			        				spSlotList.get(index).setTimeEnd(spSlotList.get(index+1).getTimeEnd());
				        			spSlotList.remove(index+1);
				        			excelDetails.remove(spColumnList.get(index+1));
				        			spColumnList.remove(index+1);
				        			index=-1;
		        				}
		        				
			        			
			        		}
		        		}
		        		
		        		
		        	}
		        	
		        	
		        	osceSeqDetail.setOsceSeq(osceSeq);
		        	osceSeqDetail.setColSpan(timeSlotDetail.getSpSlotList().size()*2);
		        	osceSeqDetailList.add(osceSeqDetail);
		        }
		        
	        	
	        }
	        
	        //write data to excel;
	        int row=1;
	        
	        for(int i=0;i<completeExcel.size();i++)//equals to number of oscedays
	        {
	        	List<Object> excelDetailPerDay=completeExcel.get(i);
	        	int col=0;
	        	for(int j=0;j<excelDetailPerDay.size();j++)//all seq/day
	        	{
	        		Object obj=excelDetailPerDay.get(j);
	        		
	        		if(obj instanceof OsceDayDetail)
	        		{
	        			OsceDayDetail osceDayDetail=(OsceDayDetail)obj;
	        			row++;
	        			Row osceDayExcelRow=sheet.createRow(row++);
	        			Cell cell =osceDayExcelRow.createCell(0);
	        			cell.setCellValue(osceDayDetail.getHeaderLabel());
	        			
	        			CellStyle osceDayStyle;
	        	        Font osceDayFont = wb.createFont();
	        	        osceDayFont.setFontHeightInPoints((short)14);
	        	        osceDayFont.setColor(IndexedColors.BLACK.getIndex());
	        	        osceDayStyle = wb.createCellStyle();
	        	        osceDayStyle.setAlignment(CellStyle.ALIGN_CENTER);
	        	        osceDayStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	        	        osceDayStyle.setFont(osceDayFont);
	        	        cell.setCellStyle(osceDayStyle);
	        			
	        		}
	        		
	        		if(obj instanceof SequenceRow)
	        		{
	        			SequenceRow seqRow=(SequenceRow)obj;
	        			
	        			List<OsceSequenceDetail> osceSequenceDetailList=seqRow.getOsceSequenceDetails();
	        			
	        			//sequence Row
	        			Row excelRow=sheet.createRow(row++);
	        			int seqCol=0;
	        			for(int k=0;k<osceSequenceDetailList.size();k++)
	        			{
	        				OsceSequenceDetail osceSequenceDetail=osceSequenceDetailList.get(k);
	        				
	        				
	        				//seqCol=seqCol+2;
	        				Cell cell=excelRow.createCell(seqCol);
	        				String value="Version " +osceSequenceDetail.getOsceSeq().getLabel();
	        				cell.setCellValue(value);
	        				sheet.addMergedRegion(new CellRangeAddress(row-1,row-1,seqCol,seqCol+osceSequenceDetail.getColSpan()));
	        				seqCol=seqCol+osceSequenceDetail.getColSpan()+1;
	        				
	        				CellStyle seqStyle;
		        	        Font seqFont = wb.createFont();
		        	        seqFont.setFontHeightInPoints((short)12);
		        	        seqFont.setColor(IndexedColors.BLACK.getIndex());
		        	        seqStyle = wb.createCellStyle();
		        	        seqFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		        	        
		        	        //seqStyle.setAlignment(CellStyle.ALIGN_CENTER);
		        	        seqStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		        	        seqStyle.setFont(seqFont);
		        	        seqStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());		      
		        	        seqStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		        	        
		        	        cell.setCellStyle(seqStyle);
		        	        
		        			
	        			}
	        		}
	        		
	        		else if(obj instanceof SPTimeSlotRow)
	        		{
	        			SPTimeSlotRow spTimeSlotRow=(SPTimeSlotRow)obj;
	        			List<TimeSlotDetail> timeSlotDetailList=spTimeSlotRow.getSpSlotsPerSeq();
	        			
	        			int timeSlotCol=0;
	        			Row timeSlotRow=sheet.createRow(row++);
	        			
	        			for(int k=0;k<timeSlotDetailList.size();k++)
	        			{
	        				TimeSlotDetail timeSlotDetail=timeSlotDetailList.get(k);
	        				
	        				List<SPSlot> spSlots=timeSlotDetail.getSpSlotList();
	        				
	        				//post
	        				Cell postcell=timeSlotRow.createCell(timeSlotCol);
	        				timeSlotCol=timeSlotCol+1;
	        				postcell.setCellValue("Post");
	        				
	        				//timeslot
	        				for(int l=0;l<spSlots.size();l++)
	        				{
	        					SPSlot spSlot=spSlots.get(l);
	        					Cell timeSlotCell=timeSlotRow.createCell(timeSlotCol);
	        					sheet.setColumnWidth(timeSlotCol+1, 5*256);
	        					
	        					 SimpleDateFormat ft = 
	        						      new SimpleDateFormat ("HH:mm");
	        					
	        					timeSlotCell.setCellValue(ft.format(spSlot.getTimeStart()) +"-"+ft.format(spSlot.getTimeEnd()));
	        					
	        					CellStyle seqStyle;
			        	        Font seqFont = wb.createFont();
			        	        seqFont.setFontHeightInPoints((short)12);
			        	        seqFont.setColor(IndexedColors.BLACK.getIndex());
			        	        seqFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			        	        seqStyle = wb.createCellStyle();
			        	        //seqStyle.setAlignment(CellStyle.ALIGN_CENTER);
			        	        seqStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			        	        seqStyle.setFont(seqFont);
			        	        seqStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());	
			        	        seqStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			        	        timeSlotCell.setCellStyle(seqStyle);
			        	        
			        	        if(l != spSlots.size()-1)
			        	        {
			        	        	long nextTime=spSlots.get(l+1).getTimeStart().getTime();
			        	        	long currentSlotTime=spSlot.getTimeEnd().getTime();
			        	        	
			        	        	long middlebreak=spSlot.getOsce().getMiddleBreak()*60*1000;
			        	        	long slotDiff=nextTime-currentSlotTime;
			        	        	if((slotDiff) > middlebreak)
			        	        	{
			        	        		Cell cell=timeSlotRow.createCell(timeSlotCol+1);
			        	        		
			        	        		slotDiff=slotDiff/1000;
			        	        		slotDiff=slotDiff/60;
			        	        		cell.setCellValue(slotDiff+"mt");
			        	        	}
			        	        	
			        	        }
			        	        
			        	        timeSlotCol=timeSlotCol+2;

	        				}
	        				
	        				if(k==timeSlotDetailList.size()-1)
	        				{
	        					Cell roomcell=timeSlotRow.createCell(timeSlotCol);
		        				timeSlotCol=timeSlotCol+1;
		        				roomcell.setCellValue("Room");
	        				}
	        				
	        			}
	        		}
	        		else if(obj instanceof ExcelDetail)
	        		{
	        			ExcelDetail excelDetail=(ExcelDetail)obj;
	        			
	        			List<Row> excelRow=new ArrayList<Row>();
	        			int startrow=0;
    					int endRow=0;
    					int rowSpan=0;
	        			for(int k=0;k<excelDetail.getExcelDetail().size();k++)
	        			{
	        				Object column=excelDetail.getExcelDetail().get(k);
	        				
        					if(k==0)
        					{
        						startrow=row;
        					}

	        				if(column instanceof PostColumn)
	        				{
	        					PostColumn postColumn=(PostColumn)column;
	        					
	        					List<PostDetail> postDetailList=postColumn.getPostDetailList();
	        					
	        					if(k==0)
	        					for(int l=0;l<postDetailList.size();l++)
	        					{
	        						PostDetail postDetail=postDetailList.get(l);
	        						OscePostRoom oscePostRoom=postDetail.getOscePostRoom();
	        						rowSpan=postDetail.getRowSpan();
	        						
	        						//for(int m=0;m<rowSpan;m++)
	        						 excelRow.add(sheet.createRow(row++));	        							        							        						
	        					}
	        					
	        					endRow=excelRow.size();
	        					//int index=0;
	        					int index1=0;
	        					int colorIndex=2;
	        					
	        					
	        					
	        					int postIndex=0;
	        					boolean flag=true;
	        					for(int index=0;index<postDetailList.size();index++)
	        					{
	        						if (index1 < postDetailList.size())
	        						{
	        							PostDetail postDetail=postDetailList.get(index1);
		        						rowSpan=postDetail.getRowSpan();
		        						OscePostRoom oscePostRoom=postDetail.getOscePostRoom();
		        						PatientInRole patientInRole=postDetail.getPatientInRole();
		        						Row postRow=excelRow.get(index1);
	        							Cell postCell=postRow.createCell(col);
	        							sheet.autoSizeColumn(col, true);
	        							postCell.setCellStyle(spPostStyle(wb, "color_"+colorIndex));
		        						if(postDetail.isReserve())//logical break
		        						{
		        							//Changed for OMS-158
		        							String reserveSPRoom = postDetail.getReserveRoomNumber();
		        							postCell.setCellValue("Reserve ("+ reserveSPRoom +")");
		        							
		        							//postIndex++;
		        							//colorIndex++;
		        						}
		        						else if(postDetail.getOscePost().getStandardizedRole().getRoleType()==RoleTypes.Material) //material
		        						{
		        							
		        							
		        							if (postDetail.getOscePost().getOsceSequence().getCourses().size() <= 1)
		        							{
		        								postCell.getCellStyle().setWrapText(true);
		        								postCell.setCellValue("Notfall\nPhantom");		        											        							
		        							}
		        							else
		        							{
		        								postCell.setCellValue("Notfall");
		        								Cell cell=excelRow.get(index1+1).createCell(col);
			        							cell.setCellValue("Phantom");
			        							sheet.autoSizeColumn(col, true);
			        							cell.setCellStyle(spPostStyle(wb, "color_"+colorIndex));
			        							int temp=1;
			        							for(int postRowIndex=2;postRowIndex<rowSpan;postRowIndex++)
			        							{
			        								Cell cell1=excelRow.get(index1+1+temp).createCell(col);
			            							sheet.autoSizeColumn(col, true);
			            							cell1.setCellStyle(spPostStyle(wb, "color_"+colorIndex));
			            							temp++;
			        							}
		        							}
		        							
		        							postIndex++;
		        							colorIndex++;
		        						}
		        						else
		        						{
		        							OscePost oscePost=postDetail.getOscePost();
		        							StandardizedRole role=oscePost.getStandardizedRole();
		        							String roleLongName=role.getLongName();
		        							
		        							Cell cell;
		        							if (oscePost.getOsceSequence().getCourses().size() > 1)
		        								cell=excelRow.get(index1+1).createCell(col);
		        							else
		        								cell = postCell;
		        							
		        							String postTypeLbl=role.getRoleType().toString();
		        							
		        							sheet.autoSizeColumn(col, true);
		        							cell.setCellStyle(spPostStyle(wb, "color_"+colorIndex));
		        							
		        							if (oscePost.getOsceSequence().getCourses().size() <= 1)
		        							{
		        								if(oscePost.getStandardizedRole().getRoleType()==RoleTypes.Material)
			        							{
		        									postCell.getCellStyle().setWrapText(true);
			        								postCell.setCellValue("Notfall\nPhantom");			        								
			        							}
			        							else
			        							{
			        								postCell.getCellStyle().setWrapText(true);
			        								String advanceSearchCriteria=AdvancedSearchCriteria.findAdvancedSearchCriteriasByStandardizedRoleIDValue(role);
			        								if (advanceSearchCriteria.isEmpty())
			        									postCell.setCellValue(roleLongName + "\n" + postTypeLbl);
			        								else
			        									postCell.setCellValue(roleLongName + "\n" + postTypeLbl + "\n" + advanceSearchCriteria);
			        							}
		        							}
		        							else
		        							{
		        								if(oscePost.getStandardizedRole().getRoleType()==RoleTypes.Material)
			        							{
			        								postCell.setCellValue("Notfall");
			        								cell.setCellValue("Phantom");
			        							}
			        							else
			        							{
			        								postCell.setCellValue(roleLongName);
			        								
			        								String advanceSearchCriteria=AdvancedSearchCriteria.findAdvancedSearchCriteriasByStandardizedRoleIDValue(role);
			        								
			        								cell.setCellValue(postTypeLbl+" "+advanceSearchCriteria);
			        							}
		        							}
		        							
		        							
		        							int temp=1;
		        							for(int postRowIndex=2;postRowIndex<rowSpan;postRowIndex++)
		        							{
		        								Cell cell1=excelRow.get(index1+1+temp).createCell(col);
		            							sheet.autoSizeColumn(col, true);
		            							cell1.setCellStyle(spPostStyle(wb, "color_"+colorIndex));
		            							temp++;
		        							}
		        							postIndex++;
		        							colorIndex++;
			        						
		        						}
		        						//index++;
		        						//if(rowSpan!=1)
		        						//{
		        						index1=index1+rowSpan;
		        						if(rowSpan!=1)
		        						index=index1;
		        						/*}
		        						else
		        						{
		        							//index1=index;
		        							index1=index1+rowSpan;
		        						}*/
		        						
		        						/*else
		        						{
		        							index1++;
		        						}*/
		        						
		        						if((index) <= postDetailList.size()-1)
		        						{
		        							if(postDetailList.get(index).isReserve())
		        							{
		        								
		        								postIndex=0;
		        								if(flag)
		        								{
		        									index--;
		        									flag=false;
		        								}
		        								//index1--;
		        								//index--;
		        								//index1--;
		        							}
		        						}
	        						}	
	        						
	        					}
	        					
	        					
	        					col++;
	        				
	        				}
	        				
	        				
	        				if(column instanceof SPColumn)
	        				{
	        					SPColumn spColumn=(SPColumn) column;
	        					List<SPDetail> spDetailList=spColumn.getSpDetailList();
	        						        					
	        					List<OscePost> oscePosts=null;
	        					if(spDetailList.size()>0)
	        					{
	        						oscePosts=spDetailList.get(0).getOscePosts();
	        					}	        					
	        					
	        					if(oscePosts!=null)
	        					for(int m=0;m<oscePosts.size();m++)
	        					{
	        						OscePost oscePost=oscePosts.get(m);
	        						int spIndex=0;
	        						
		        					for(int n=0;n<spDetailList.size();n++)
		        					{
		        						SPDetail spDetail=spDetailList.get(n);
		        						
		        						PatientInRole patientInRole=spDetail.getPatientInRole();
		        						OscePostRoom oscePostRoom=spDetail.getOscePostRoom();
		        						
		        						if(patientInRole!=null && oscePostRoom != null && oscePostRoom.getOscePost().getId().equals(oscePost.getId()))
		        						{	
		        							int index = oscePosts.indexOf(oscePostRoom.getOscePost())*spDetail.getCourses().size()+spIndex;
		        							Cell spCell=excelRow.get(index).createCell(col);
		        							StandardizedPatient sp=patientInRole.getPatientInSemester().getStandardizedPatient();
		        							
		        							String name = "";
		        							if (PostType.DUALSP.equals(oscePost.getOscePostBlueprint().getPostType()))
		        							{
		        								name = sp.getPreName() + " " + sp.getName().charAt(0) + ".";
		        								PatientInRole dualSp = spDetail.getDualPatientInRole();
		        								if (dualSp != null)
		        								{
		        									StandardizedPatient sp1 = dualSp.getPatientInSemester().getStandardizedPatient();
		        									name = name + ", " + sp1.getPreName() + " " + sp1.getName().charAt(0) + ".";
		        								}
		        							}
		        							else
		        							{
		        								name=sp.getPreName()+" "+sp.getName().charAt(0)+".";
		        							}
		        							
		        							spCell.setCellValue(name);
		        							sheet.autoSizeColumn(col, true);
		        							
		        							spCell.setCellStyle(spParcourStyle(wb, oscePostRoom.getCourse().getColor()));
		        							spIndex++;
			        						
		        						}
		        						else if(patientInRole==null && oscePostRoom != null && oscePostRoom.getOscePost().getId().equals(oscePost.getId()))
		        						{
		        							int index = oscePosts.indexOf(oscePostRoom.getOscePost())*spDetail.getCourses().size()+spIndex;
		        							Cell spCell=excelRow.get(index).createCell(col);
		        							//StandardizedPatient sp=patientInRole.getPatientInSemester().getStandardizedPatient();
		        							//String name=sp.getPreName()+" "+sp.getName();
		        							if (PostType.DUALSP.equals(oscePost.getOscePostBlueprint().getPostType()))
		        							{
		        								spCell.setCellValue("NA, NA");
		        							}
		        							else
		        							{
		        								spCell.setCellValue("NA");
		        							}
		        							
		        							sheet.autoSizeColumn(col, true);
		        							
		        							spCell.setCellStyle(spParcourStyle(wb, oscePostRoom.getCourse().getColor()));
		        							spIndex++;
		        						}
		        								        						
		        						/*else if(oscePostRoom==null && patientInRole!=null)
		        						{
		        							Cell spCell=excelRow.get(n).createCell(col);
		        							StandardizedPatient sp=patientInRole.getPatientInSemester().getStandardizedPatient();
		        							String name=sp.getPreName()+" "+sp.getName();
		        							spCell.setCellValue(name);
		        						}
		        						else
		        						{
		        							Cell spCell=excelRow.get(n).createCell(col);
		        							spCell.setCellValue("");
		        						}*/
		        						
		        					}
		        			/*		spIndex=-1;
		        					 if(oscePost.getStandardizedRole().getRoleType()==RoleTypes.Material)
		        					 {
		        						 if(spDetailList.size()>0)
		        						 {
		        							 for(int temp=0;temp<spDetailList.get(0).getCourses().size();i++)
		        							 {
		        								 Course course=spDetailList.get(0).getCourses().get(temp);
		        								 Cell spCell=excelRow.get(oscePosts.indexOf(oscePost)*spDetailList.get(0).getCourses().size()+spIndex).createCell(col);
				        							//StandardizedPatient sp=patientInRole.getPatientInSemester().getStandardizedPatient();
				        							//String name=sp.getPreName()+" "+sp.getName();
				        							spCell.setCellValue("NA");
				        							sheet.autoSizeColumn(col, true);
				        							
				        							spCell.setCellStyle(spParcourStyle(wb, course.getColor()));
				        							spIndex++;
					        					
		        							 }
		        						 }
		        					 }*/
		        					
	        					}
	        					
	        					//logical break SP
	        					int rowIndex=0;
	        					for(int n=0;n<spDetailList.size();n++)
	        					{
	        						SPDetail spDetail=spDetailList.get(n);
	        						
	        						PatientInRole patientInRole=spDetail.getPatientInRole();
	        						OscePostRoom oscePostRoom=spDetail.getOscePostRoom();
	        						
	        						if(oscePostRoom==null && patientInRole!=null)
	        						{
	        							Cell spCell=null;
	        							//spCell=excelRow.get((oscePosts.size()-spDetail.getNumOfBreakPost())*spDetail.getCourses().size()+rowIndex).createCell(col);	
	        							spCell=excelRow.get((oscePosts.size())*spDetail.getCourses().size()+rowIndex).createCell(col);
	        							StandardizedPatient sp=patientInRole.getPatientInSemester().getStandardizedPatient();
	        							String name=sp.getPreName()+" "+sp.getName().charAt(0) +".";
	        							spCell.setCellValue(name);
	        							rowIndex++;
	        							sheet.autoSizeColumn(col, true);
	        						}
	        						
	        					}
	        					
	        					col=col+2;
	        				}
	        				
	        				if(column instanceof RoomColumn)
	        				{
	        					RoomColumn roomColumn=(RoomColumn) column;
	        					List<RoomDetail> roomDetailList=roomColumn.getRoomDetailList();
	        					List<OscePost> oscePosts=null;
	        					if(roomDetailList.size()>0)
	        					{
	        						oscePosts=roomDetailList.get(0).getOscePosts();
	        					}
	        					
	        					if(oscePosts!=null)
	        					for(int m=0;m<oscePosts.size();m++)
	        					{
	        						OscePost oscePost=oscePosts.get(m);
	        						int roomIndex=0;
		        					for(int n=0;n<roomDetailList.size();n++)
		        					{
		        						RoomDetail roomDetail=roomDetailList.get(n);
		        						
		        						Room room=roomDetail.getRoom();
		        						OscePostRoom oscePostRoom=roomDetail.getOscePostRoom();
		        						
		        						if(room !=null && oscePostRoom != null && oscePostRoom.getOscePost().equals(oscePost))
		        						{
		        							int index = oscePosts.indexOf(oscePostRoom.getOscePost())*roomDetail.getCourses().size()+roomIndex;
		        							Cell roomCell=excelRow.get(index).createCell(col);
		        							roomCell.setCellValue(room.getRoomNumber());
		        							roomCell.setCellStyle(spParcourStyle(wb, roomDetail.getOscePostRoom().getCourse().getColor()));
		        							sheet.autoSizeColumn(col, true);
		        							roomIndex++;
		        						}
		        						
		        						
		        					}
	        					}
	        				}
	        			}
	        		}
	        	}

	        }
	        //print data for all osceday
	        
	       
	        for(int i=0;i<completeExcel.size();i++)//equals to number of oscedays
	        {
	        	List<Object> excelDetailPerDay=completeExcel.get(i);
	        	
	        	for(Object o:excelDetailPerDay)
	        	{
	        		if(o instanceof SequenceRow)
	        		{
	        			SequenceRow seqRow=(SequenceRow)o;
	        			
	        			List<OsceSequenceDetail> osceSequenceDetailList=seqRow.getOsceSequenceDetails();
	        			for(OsceSequenceDetail osceSequenceDetail:osceSequenceDetailList)
	        			{
	        				Log.info("Version" +osceSequenceDetail.getOsceSeq().getId() +"column Span " + osceSequenceDetail.getColSpan());
	        			}
	        		}
	        	}
	        	
	        }
	        
	         
	         FileOutputStream out = new FileOutputStream(outputFileName);
	         wb.write(out);
	         out.close();
	         return outputFileName;
         }
         catch(Exception e)
         {
        	 e.printStackTrace();
        	 return outputFileName;
         }
		
	}

	private SPDetail checkSPDetailWithOscePostRoomId(List<SPDetail> spDetailsList, Long oscePostRoomId, Integer seqNumber) {
		for (SPDetail spDetail : spDetailsList)
		{
			if (spDetail != null && spDetail.getOscePostRoom() != null 
					&& spDetail.getOscePostRoom().getId().equals(oscePostRoomId) 
					&& seqNumber.equals(spDetail.getSequenceNumber()) )
			{
				return spDetail;
			}
		}
		return null;
	}
	
	public String saveXML(Document doc)
	{
		try{
			TransformerFactory factory = TransformerFactory.newInstance();
	        Transformer transformer = factory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(doc);
	        transformer.transform(source, result);
	        String xmlString = sw.toString();

	       // File file = new File("osMaEntry/gwt/unibas/"+System.currentTimeMillis()+".xml");
	        String path=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML);
	        String fileName=path+System.currentTimeMillis()+".xml";
	        
	        
	        File file = new File(fileName);
	        file.createNewFile();
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
	        bw.write(xmlString);
	        bw.flush();
	        bw.close();
	        
	        
	        String htmlFileName=convertXmlToHtml(fileName);
	        
	        return htmlFileName;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	public String convertXmlToHtml(String fileName)
	{
		 try
	        {
			 
			 	
			 	
	            TransformerFactory tFactory = TransformerFactory.newInstance();
	            String xslPath=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentXslPath);
	            Source xslDoc = new StreamSource(xslPath);
	            Source xmlDoc = new StreamSource(fileName);
	            
	            String path=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML);
	            String outputFileName =path +System.currentTimeMillis()+".html";
	            OutputStream htmlFile = new FileOutputStream(outputFileName);

	            Transformer transformer = tFactory.newTransformer(xslDoc);
	            transformer.transform(xmlDoc, new StreamResult(htmlFile));
	            htmlFile.close();
	            File xmlFile=new File(fileName);
	            xmlFile.delete();
	           return outputFileName;
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	public  Element createChildNode(String nodeName,String nodeValue,Document doc,Element parent)
	{
		 Element element = doc.createElement(nodeName);//create  node
		 parent.appendChild(element);	//append to its parent
	        Text text2 = doc.createTextNode(nodeValue);	//create Text node/ value
	        element.appendChild(text2); 
	        return element;
	}
	public Element createEmptyChildNode(String nodeName,Document doc,Element parent)
	{
		 Element element = doc.createElement(nodeName);//create  node
		 parent.appendChild(element);	//append to its parent
	      //  Text text2 = doc.createTextNode(nodeValue);	//create Text node/ value
	    //    element.appendChild(text2); 
	        return element;
	}
	
	public  Document createDocument()
	{
		try{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        return doc;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
	}
	/**
	 * setting parcour style
	 * @param wb
	 * @param color
	 * @return
	 */
	//changed code as per OMS-154
	public CellStyle spParcourStyle(Workbook wb,String color)
	{
		CellStyle seqStyle;
        
		Font seqFont = wb.createFont();
        seqFont.setFontHeightInPoints((short)12);
        seqStyle = wb.createCellStyle();
        seqFont.setBoldweight((short)2);
        
        //seqStyle.setAlignment(CellStyle.ALIGN_CENTER);
        seqStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        seqStyle.setFont(seqFont);
        seqStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());		        	        
        seqStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        if(color.equalsIgnoreCase("color_1"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
        	 seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        
        if(color.equalsIgnoreCase("color_2"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_3"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.PINK.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_4"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_5"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_6"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_7"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_8"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_9"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_10"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_11"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.PLUM.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_12"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_13"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_14"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_15"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.INDIGO.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_16"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        
        
        return seqStyle;
        
        /*
         * #Color Names
color_1=Brown
color_2=Yellow.
color_3=Pink
color_4=Aqua
color_5=Grey
color_6=Red
color_7=Beige
color_8=Blue
color_9=Green
color_10=Orange
color_11=Purpur
color_12=Maroon
color_13=Olive
color_14=Violet
color_15=Indigo
color_16=Black
         * 
         * */
        
	}
	
	/**
	 * setting sp post style
	 * @param wb
	 * @param color
	 * @return
	 */
	//changed code as per OMS-154
	public CellStyle spPostStyle(Workbook wb,String color)
	{
		CellStyle seqStyle;
        
		Font seqFont = wb.createFont();
        seqFont.setFontHeightInPoints((short)12);
        seqStyle = wb.createCellStyle();
        seqFont.setBoldweight((short)2);
        
        //seqStyle.setAlignment(CellStyle.ALIGN_CENTER);
        seqStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        seqStyle.setFont(seqFont);
        seqStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());		        	        
        seqStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        if(color.equalsIgnoreCase("color_1"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        
        if(color.equalsIgnoreCase("color_2"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_3"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_4"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_5"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_6"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_7"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.PLUM.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_8"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.TAN.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_9"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_10"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if(color.equalsIgnoreCase("color_11"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.ORCHID.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_12"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_13"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_14"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_15"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.INDIGO.getIndex());
        	seqFont.setColor(IndexedColors.WHITE.getIndex());
        }
        if(color.equalsIgnoreCase("color_16"))
        {
        	seqStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        	seqFont.setColor(IndexedColors.BLACK.getIndex());
        }
        return seqStyle;
        
	}
    
	private Date dateAddMin(Date date, int minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}	
}
