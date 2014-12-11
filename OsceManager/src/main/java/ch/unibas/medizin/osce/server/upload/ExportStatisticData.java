package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.math.stat.StatUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.ItemAnalysis;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.PostAnalysis;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.server.CalculateCronbachValue;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.RoleTopicFactor;

public class ExportStatisticData extends HttpServlet{

	
	private static Logger Log = Logger.getLogger(UploadServlet.class);
	private static Double Double;
	
	
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException 
	  {
		  String osceId=request.getParameter("osceId");
		  String exportType=request.getParameter("export");
		  
		  if(exportType.equals("old"))
		  {
			  List<String> fileNameList = new ArrayList<String>();
			  String fileName=createCSV(new Long(osceId),request, getServletConfig().getServletContext(), true,fileNameList);
			  
			  ByteArrayOutputStream os = new ByteArrayOutputStream();
			  createZipFile("OsceStatisticData.zip", fileNameList,os);
			  try{
					
					
					
				  resp.setContentType("application/x-download");
					
				  resp.setHeader("Content-Disposition", "attachment; filename=" + "OsceStatisticData.zip");
					
					   
	
					Log.info("path :" + fileName);
					//String file=OsMaFilePathConstant.ROLE_IMAGE_FILEPATH+path;
					Log.info(" file :" + fileName);
					
					OutputStream out = resp.getOutputStream();
					
					byte[] in = os.toByteArray();
					if (in.length > 0){
					    out.write(in);
					}
					
					File htmlFile=new File(fileName);
					htmlFile.delete();
					out.flush();
				
				}
				catch (Exception e) {
					Log.error(e.getMessage(),e);
				}finally {
					os.close();
				}
		  }
		  else if(exportType.equals("new"))
		  {
			  List<String> fileNameList = new ArrayList<String>();
			  String fileName=createNewCSV(new Long(osceId),request, getServletConfig().getServletContext(), true,fileNameList);
			  String configFileName = createConfigurationFile(new Long(osceId), request, getServletConfig().getServletContext());
			  fileNameList.add(configFileName);
			  ByteArrayOutputStream os = new ByteArrayOutputStream();
			  createZipFile("NewOsceStatisticData.zip", fileNameList,os);
			  try{
					
					
					
				  resp.setContentType("application/x-download");
					
				  resp.setHeader("Content-Disposition", "attachment; filename=" + "NewOsceStatisticData.zip");
					
					   
	
					Log.info("path :" + fileName);
					//String file=OsMaFilePathConstant.ROLE_IMAGE_FILEPATH+path;
					Log.info(" file :" + fileName);
					
					OutputStream out = resp.getOutputStream();
					
					byte[] in = os.toByteArray();
					if (in.length > 0){
					    out.write(in);
					}
					
					File htmlFile=new File(fileName);
					htmlFile.delete();
					out.flush();
				
				}
				catch (Exception e) {
					Log.error(e.getMessage(),e);
				}finally {
					os.close();
				}
		  }
	  }
	  
	  
	private String createConfigurationFile(Long osceId, HttpServletRequest request, ServletContext servletContext) {
		String configFileName = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML + "configure.R");
		try
		{
			Osce osce=Osce.findOsce(osceId);
			
			 List<OsceDay> osceDays=osce.getOsce_days();
				
			 CalculateCronbachValue calculateCronbachValue = new CalculateCronbachValue();		 
			 
			 FileWriter writer = new FileWriter(configFileName);
			 
			 List<String> postFileNameList = new ArrayList<String>();
			 List<String> postShortNameList = new ArrayList<String>();
			 List<String> postLongNameList = new ArrayList<String>();
			 List<String> postPassMarkList = new ArrayList<String>();
			 List<String> postIdList = new ArrayList<String>();
			 List<String> postWeightList = new ArrayList<String>();
			 List<String> postRatioList = new ArrayList<String>();
			 
				
			 for(int i=0;i<osceDays.size();i++)
			 {
				 OsceDay osceDay=osceDays.get(i);
				 List<OsceSequence> osceSequences=osceDay.getOsceSequences();
					
				for(int a=0;a<osceSequences.size();a++)
				{
					OsceSequence osceSeq=osceSequences.get(a);
					
					List<OscePost> oscePostList = osceSeq.getOscePosts();
					String fileName = "";
					int noOfOscePost = 0;
					for (OscePost oscePost : oscePostList)
					{
						noOfOscePost = noOfOscePost + 1;
						String postFileName;
						
						postIdList.add(oscePost.getId().toString());
						if (oscePost.getStandardizedRole() != null)
						{
							postFileName = oscePost.getStandardizedRole().getShortName() + "_" + osceSeq.getLabel() +".csv";
							postFileName = postFileName.replaceAll("\\\\", "");
							postFileName = postFileName.replaceAll("\\/", "");
							postFileName = postFileName.replaceAll(" ", "");
							
							fileName = "Day"+ (i+1) + "_" + ("P"+noOfOscePost) + "_" + oscePost.getStandardizedRole().getShortName() + "_" +osceSeq.getLabel();
							fileName = fileName.replaceAll("\\\\", "");
							fileName = fileName.replaceAll("\\/", "");
							fileName = fileName.replaceAll(" ", "");
							
							postShortNameList.add(oscePost.getStandardizedRole().getShortName());
							postLongNameList.add(oscePost.getStandardizedRole().getLongName());
							if (RoleTopicFactor.WEIGHT.equals(oscePost.getStandardizedRole().getTopicFactor())) {
								postWeightList.add(Boolean.TRUE.toString());
								postRatioList.add(Boolean.FALSE.toString());
							}
							else if (RoleTopicFactor.RATIO.equals(oscePost.getStandardizedRole().getTopicFactor())) {
								postWeightList.add(Boolean.FALSE.toString());
								postRatioList.add(Boolean.TRUE.toString());
							}
							else {
								postWeightList.add(Boolean.FALSE.toString());
								postRatioList.add(Boolean.FALSE.toString());
							}
						}
						else
						{
							postFileName = "post" + oscePost.getId() + "_" + osceSeq.getLabel() +".csv";
							fileName = "Day"+ (i+1) + "_" + ("P"+noOfOscePost) + "_" + "post" + oscePost.getId() + "_" + osceSeq.getLabel();
							postShortNameList.add("");
							postLongNameList.add("");
							postWeightList.add(Boolean.FALSE.toString());
							postRatioList.add(Boolean.FALSE.toString());
						}
								
						postFileNameList.add(fileName);
						
						PostAnalysis postAnalysis = PostAnalysis.findPostLevelData(osce, oscePost);
						
						if (postAnalysis != null)
						{
							postPassMarkList.add(postAnalysis.getBoundary().toString());
						}
						else
						{
							List<String> valueList = createOscePostCSV(request, servletContext, oscePost.getId(), postFileName, new ArrayList<String>(), new ArrayList<Integer>(), null);

							if(valueList != null && valueList.size() == 2)
							{
								fileName = valueList.get(0); // return filename									
							}
							
							Map<String, String> postResultMap = calculateCronbachValue.calculateOscePostResult(fileName, oscePost.getId());
							
							String postPassingStr = postResultMap.get("passMark") == null ? "0" : postResultMap.get("passMark");
							postPassMarkList.add(postPassingStr);
						}
					}
						
				}
			 }
			 
			 writer.write("posts <- NULL");
			 writer.write("\n");			 
			 writer.write("datafolder <- \"NewOsceStatisticData\"");
			 writer.write("\n\n");
			 
			 writer.write("posts$filenames <- c(\"" + StringUtils.join(postFileNameList, "\",\"") + "\")");
			 writer.write("\n");
			 writer.write("posts$id <- c(\"" + StringUtils.join(postIdList, "\",\"") + "\")");
			 writer.write("\n");
			 writer.write("posts$short.names <- c(\"" + StringUtils.join(postShortNameList, "\",\"") + "\")");
			 writer.write("\n");
			 writer.write("posts$long.names <- c(\"" + StringUtils.join(postLongNameList, "\",\"") + "\")");
			 writer.write("\n");
			 writer.write("posts$passmark <- c(\"" + StringUtils.join(postPassMarkList, "\",\"") + "\")");
			 writer.write("\n");
			 writer.write("posts$part <- c(\"" + StringUtils.join(postRatioList, "\",\"") + "\")");
			 writer.write("\n");
			 writer.write("posts$weight <- c(\"" + StringUtils.join(postWeightList, "\",\"") + "\")");
			 writer.write("\n");
			
			 writer.write("posts <- as.data.frame(posts)");
			 writer.write("\n");
			 writer.write("posts <- posts[order(posts$filenames),]");
			 writer.write("\n");
			 writer.write("posts$short.names <- gsub(\"_\",\"\",posts$short.names)");
			 writer.write("\n");
			 
			 writer.flush();
			 writer.close();
			 
			 return configFileName;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		 
		return configFileName;		 
	}


	/*public String createCSV(Long osceId,HttpServletRequest request)
	  {
		  String path=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML);
	        String fileName=path+System.currentTimeMillis()+".csv";
		  Osce osce=Osce.findOsce(osceId);
			
			List<OsceDay> osceDays=osce.getOsce_days();
			
			for(int i=0;i<osceDays.size();i++)
			{
				OsceDay osceDay=osceDays.get(i);
				List<OsceSequence> osceSequences=osceDay.getOsceSequences();
				
				for(int a=0;a<osceSequences.size();a++)
				{
					OsceSequence osceSeq=osceSequences.get(a);
					
				}
			}
			
			try
			{
				
			    FileWriter writer = new FileWriter(fileName);
			    
			    //column header[start
			    writer.append("examiners");
			    writer.append('|');
			    writer.append("students");
			    writer.append('|');
			    
			    //retrieve distinc Item
			    List<ChecklistQuestion> questions=Answer.retrieveDistinctItems(osceId);
			    
			    for(int i=0;i<questions.size();i++)
			    {
			    	 writer.append("item "+questions.get(i).getId());
			    	 writer.append('|');
			    }
			    writer.append("impression ");
			    writer.append('\n');
		 
			    //column header end]
		 
			    //generate whatever data you want[Start
			    
			    //retrieve data
			    Long lastCandidateId = 0l;
			    
			    List<Answer> csvData=Answer.retrieveExportCsvData(osceId);
			    
			    System.out.println("CSVDATA SIZE : " + csvData.size());
			    for(int i=0;i<csvData.size();i++)
			    {
			    	Answer answer=csvData.get(i);
			    	
			    	if (!lastCandidateId.equals(answer.getStudent().getId()))
			    	{
			    		if (!lastCandidateId.equals(0))
			    		{
			    			//impression item
					    	String impressionItemString=request.getParameter("p"+answer.getOscePostRoom().getOscePost().getId().toString());
					    	if(impressionItemString==null)
					    	 writer.append('0');//impression
					    	else
					    	{
					    		if(impressionItemString.equals("0"))
					    			writer.append('0');
					    		else
					    		{
					    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString), osceId);
					    			if(impressionItem !=null)
					    				writer.append(impressionItem.getChecklistOption().getValue());
					    			else
					    				writer.append('0');
					    		}
					    	}
			    		}
			    		
			    		writer.append('\n');
			    		
			    		writer.append(answer.getDoctor().getPreName() + " "+ answer.getDoctor().getName());
			    		writer.append('|');
			    		writer.append(answer.getStudent().getPreName() + " "+ answer.getStudent().getName());
			    		writer.append('|');
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
		    		writer.append('|');
			    	
			    	for(int j=i;j<csvData.size();j++)
			    	{
			    		
			    		writer.append(csvData.get(j).getChecklistOption().getValue());
			    		writer.append('|');
			    		
			    		if((j!=csvData.size()-1 && csvData.get(j).getStudent().getId()!=csvData.get(j+1).getStudent().getId() ))
			    		{
			    			i=j;
			    			 writer.append('0');//impression
			    			 writer.append('\n');
			    			break;
			    		}
			    	}
			    	
			    	for(int j=0;j<questions.size();j++)
			    	{
			    		Answer item=Answer.findAnswer(answer.getStudent().getId(), questions.get(j).getId(), osceId);
			    		if(item==null)//missing
			    		{
			    			writer.append('0');
			    			writer.append('|');
			    		}
			    		else
			    		{
			    			writer.append(item.getChecklistOption().getValue());
				    		writer.append('|');
			    		}
			    	}
			    	
			    	
			    	//impression item
			    	String impressionItemString=request.getParameter("p"+answer.getOscePostRoom().getOscePost().getId().toString());
			    	if(impressionItemString==null)
			    	 writer.append('0');//impression
			    	else
			    	{
			    		if(impressionItemString.equals("0"))
			    			writer.append('0');
			    		else
			    		{
			    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString), osceId);
			    			if(impressionItem !=null)
			    				writer.append(impressionItem.getChecklistOption().getValue());
			    			else
			    				writer.append('0');
			    		}
			    	}
	    			 
			    	
			    }
			    
			  //generate whatever data you want end]
		 
			    writer.flush();
			    writer.close();
			}
			catch(IOException e)
			{
			     e.printStackTrace();
			} 
			
		  return fileName;
	  }*/
	  
	  /*
	   * flag is used to decide for which this file will generated
	   * If flag is true then it generated for Export and
	   * If flag is false then it generated for Rscript
	  */
	  public static String createNewCSV(Long osceId,HttpServletRequest request, ServletContext servletContext, Boolean flag,final List<String> fileNameList)
	  {
		  String fileName = "";
		  String zipFileName = "";
		  char alphaSeq = 'A';
		  
		  Long impressionQueId = null;
		  List<Long> impressionQuestion=new ArrayList<Long>();
		  
		  try
		  {
			 // String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);
			  //System.out.println("PATH : " + path);
			  
			  zipFileName = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML + "NewOsceStatisticData.zip");
			  
			  Osce osce=Osce.findOsce(osceId);
				
				List<OsceDay> osceDays=osce.getOsce_days();
				
				CalculateCronbachValue calculateCronbachValue = new CalculateCronbachValue();
				
				for(int i=0;i<osceDays.size();i++)
				{
					OsceDay osceDay=osceDays.get(i);
					List<OsceSequence> osceSequences=osceDay.getOsceSequences();
					
					for(int a=0;a<osceSequences.size();a++)
					{
						OsceSequence osceSeq=osceSequences.get(a);
						
						List<OscePost> oscePostList = osceSeq.getOscePosts();
						int postIndex = 0;
						for (OscePost oscePost : oscePostList)
						{
							postIndex += 1;
							ArrayList<String> fileNames=new ArrayList<String>();
							fileNames.add("data");
							fileNames.add("examiner");
							fileNames.add("student");
							fileNames.add("post");
							fileNames.add("items");
							for(int k=0;k<5;k++)
							{
								if (oscePost.getStandardizedRole() != null)
									fileName = "Day"+ (i+1) + "_" + ("P"+postIndex) + "_" + oscePost.getStandardizedRole().getShortName() + "_" +osceSeq.getLabel()+"_"+ fileNames.get(k) +".csv";
								else
									fileName = "Day"+ (i+1) + "_" + ("P"+postIndex) + "_" + "post" + oscePost.getId() + "_" + osceSeq.getLabel()+"_"+fileNames.get(k) +".csv";
							
								fileName = fileName.replaceAll("\\\\", "");
								fileName = fileName.replaceAll("\\/", "");
								fileName = fileName.replaceAll(" ", "");
								
								fileName = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML + fileName);
								fileNameList.add(fileName);
								
								
								if(k==0)//data file
								{
									//System.out.println("FILE PATH : " + fileName);
									
									FileWriter writer = new FileWriter(fileName);
									writer.append("examiners");
									writer.append('|');
									writer.append("students");
									writer.append('|');
									
									//List<Long> postMissingQueList = ItemAnalysis.findDeactivatedItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId());
									List<Long> postMissingItemQueList = ItemAnalysis.findDeactivatedChecklistItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId()); 
									String missingQue = "";
									
									//List<ChecklistTopic> checklistTopicList = new ArrayList<ChecklistTopic>();
									List<ChecklistItem> checklistItemTopicList = new ArrayList<ChecklistItem>();
									
									if (oscePost.getStandardizedRole() != null && oscePost.getStandardizedRole().getCheckList() != null) {
										checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklist(oscePost.getStandardizedRole().getCheckList().getId());
									}

									/*if (oscePost.getStandardizedRole() != null)
										checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();*/
									
									alphaSeq = 'A';
									
									impressionQueId = null;
									impressionQuestion.clear();
									
									
									//for (ChecklistTopic checklistTopic : checklistTopicList)
									for (ChecklistItem checklistItemTopic : checklistItemTopicList)
									{
										//List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
										List<ChecklistItem> itemQuestionList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistItemTopic.getId());
										
										int count = 1;
										//for (ChecklistQuestion question : questionList)
										for (ChecklistItem question : itemQuestionList)
										{
											if ((impressionQueId == null || impressionQueId == 0)  && question.getIsRegressionItem() != null && question.getIsRegressionItem())
											{
												impressionQueId = question.getId();
											}
											
											if(question.getIsRegressionItem() != null && question.getIsRegressionItem())
											{
												impressionQuestion.add(question.getId());
											}
											
											if (postMissingItemQueList.contains(question.getId()) == false)
											{
												if (flag)
													writer.append(question.getId().toString());
												else
													writer.append("Q" + String.valueOf(question.getId()));
												
												writer.append('|');
											}
											
											if (flag == true && postMissingItemQueList.contains(question.getId()))
											{
												if (missingQue == null || missingQue.isEmpty())
													missingQue = String.valueOf(alphaSeq) + count;
												else
													missingQue = missingQue + "," + String.valueOf(alphaSeq) + count;
											}
											
											count += 1;
											//writer.append(question.getId().toString());
										}
										alphaSeq++;
									}
									
									String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
									if (impressionItemString != null)
										impressionQueId=Long.parseLong(impressionItemString);
									
									for(int l=0;l<impressionQuestion.size();l++)
									{
										writer.append("impression l");
										writer.append('|');
									}
									//writer.append('\n');
								    
									if (flag)
									{
										writer.append("Bonus Point");
									}
									
								    //List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(osceDay.getId(), oscePost.getId());
								    List<Answer> answerList = Answer.retrieveExportCsvDataByItemAndOscePost(osceDay.getId(), oscePost.getId());
								    Long lastCandidateId = null;
								    Answer answer = null;
								    
								    for (int j=0; j<answerList.size(); j++)
								    {
								    	answer = answerList.get(j);
								    	

							    		if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
								    	{
								    		if (lastCandidateId != null)
								    		{
								    			for(int l=0;l<impressionQuestion.size();l++)
								    			{
								    				//Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQuestion.get(l), osceDay.getId());
								    				Answer impressionItem1=Answer.findItemAnswer(lastCandidateId, impressionQuestion.get(l), osceDay.getId());
								    				if(impressionItem1.getChecklistOption()!=null)
								    					writer.append(impressionItem1.getChecklistOption().getValue());
								    				else
								    					writer.append("0");
								    				
								    				writer.append('|');
								    			}
									    		/*else
									    		{
									    			writer.append('0');//impression
									    		}*/
								    			
								    			if (flag)
								    			{	
								    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(j-1).getDoctor().getId());
								    				writer.append(addPoint.toString());
								    				//writer.append('|');
								    			}
								    		}
								    		
								    		writer.append('\n');
								    		
							    			writer.append(answer.getDoctor().getId().toString());
								    		writer.append('|');
								    		writer.append(answer.getStudent().getId().toString());
								    		writer.append('|');
								    		
								    		/*else	
								    		{
								    			writer.append(answer.getDoctor().getPreName() + " " + answer.getDoctor().getName());
								    			writer.append('|');
									    		writer.append(answer.getStudent().getPreName() + " " + answer.getStudent().getName());
									    		writer.append('|');
								    		}	*/	    		
								    		
								    		lastCandidateId = answer.getStudent().getId();
								    	}
							    		
							    		if (postMissingItemQueList.contains(answer.getChecklistItem().getId()) == false)
								    	{
							    			if(answer.getChecklistOption()!=null)
									    		writer.append(answer.getChecklistOption().getValue());
									    	else
									    		writer.append("0");
							    			
							    			writer.append('|');
								    	}	
							    		
								    	//writer.append(answer.getChecklistQuestion().getId().toString());
								    }
								    
								    if (answerList.size() > 0)
								    {
								    	
								    	
								    	for(int l=0;l<impressionQuestion.size();l++)
						    			{
						    				//Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQuestion.get(l), osceDay.getId());
						    				Answer impressionItem1=Answer.findItemAnswer(lastCandidateId, impressionQuestion.get(l), osceDay.getId());
						    				if(impressionItem1.getChecklistOption() !=null)
						    					writer.append(impressionItem1.getChecklistOption().getValue());
						    				else
						    					writer.append("0");
						    				
						    				writer.append('|');
						    			}
							    		/*else
							    		{
							    			writer.append('0');//impression
							    		}*/
								    	
								    	if (flag)
						    			{	
								    		Long examinerId = answerList.get(answerList.size()-1).getDoctor().getId();
								    		Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), examinerId);
						    				writer.append(addPoint.toString());
						    				
						    			}
								    }
							    	
								    writer.flush();
								    writer.close();
								    
								    if (answerList.size() == 0)
								    {
								    	FileWriter writer2 = new FileWriter(fileName);
								    	writer2.write("examiners|students|impression|pass/fall");
								    	writer2.flush();
								    	writer2.close();						    
								    }
								    
								    if (flag)
								    	calculateCronbachValue.calculatePassFail(fileName, missingQue);
								}
								
								if(k==1)//examiner file-- id, gender , name, prename
								{
									FileWriter writer = new FileWriter(fileName);
									writer.append("id");
									writer.append('|');
									writer.append("gender");
									writer.append('|');
									writer.append("name");
									writer.append('|');
									writer.append("prename");
									//writer.append('|');
									writer.append("\n");
									
									//data
									//List<Doctor> examiner=Answer.retrieveDistinctExaminer(oscePost.getId());
									List<Doctor> examiner = Answer.retrieveDistinctExaminerByItem(oscePost.getId());
									
									for(Doctor d:examiner)
									{
										writer.append(d.getId().toString());
										writer.append('|');
										writer.append(d.getGender().toString());
										writer.append('|');
										writer.append(d.getName());
										writer.append('|');
										writer.append(d.getPreName());
										//writer.append('|');
										writer.append("\n");
									}
									
									writer.flush();
								    writer.close();
								}
								
								if(k==2)// Students file (id, gender, name, prename, student_id)
								{
									FileWriter writer = new FileWriter(fileName);
									writer.append("id");
									writer.append('|');
									writer.append("gender");
									writer.append('|');
									writer.append("name");
									writer.append('|');
									writer.append("prename");
									writer.append('|');
									writer.append("student_id");
									//writer.append('|');
									writer.append("\n");
									//data
									//List<Student> examiner=Answer.retrieveDistinctStudent(oscePost.getId());
									List<Student> student = Answer.retrieveDistinctStudentByItem(oscePost.getId());
									
									
									for(Student d:student)
									{
										writer.append(d.getId().toString());
										writer.append('|');
										if(d.getGender() != null)
										writer.append(d.getGender().toString());
										writer.append('|');
										writer.append(d.getName());
										writer.append('|');
										writer.append(d.getPreName());
										writer.append('|');
										writer.append(d.getStudentId());
								
										writer.append("\n");
									}
									
									
									writer.flush();
								    writer.close();
								}
								
								if(k==3)// Postfile (id, short_name, long_name)
								{
									FileWriter writer = new FileWriter(fileName);
									writer.append("id");
									writer.append('|');
									writer.append("short_name");
									writer.append('|');
									writer.append("long_name");
									//writer.append('|');
									writer.append("\n");
									
									writer.append(oscePost.getId().toString());
									writer.append('|');
									if(oscePost.getStandardizedRole() !=null)
									writer.append(oscePost.getStandardizedRole().getShortName());
									writer.append('|');
									if(oscePost.getStandardizedRole() !=null)
									writer.append(oscePost.getStandardizedRole().getLongName());
									//writer.append('|');
									writer.append("\n");
									
									writer.flush();
								    writer.close();
									
								}
								Map<Long, Double> topicWiseRatioMap = new HashMap<Long, Double>();
								if(k==4)// Item file (id, item_text, points (example 0|1|2.5|3), is_eval_item, weight)
								{
									if (oscePost.getStandardizedRole() != null && RoleTopicFactor.RATIO.equals(oscePost.getStandardizedRole().getTopicFactor()) && oscePost.getStandardizedRole().getCheckList() != null) {
										Double totalPoints = 0.0; 
										CheckList checkList = oscePost.getStandardizedRole().getCheckList();
										List<ChecklistItem> checklistTopicItemList = ChecklistItem.findChecklistTopicByChecklist(checkList.getId());
										Map<Long, Double> topicWiseMaxPoint = new HashMap<Long, Double>();
										
										for (ChecklistItem checklistTopicItem : checklistTopicItemList) {
											Double totalTopicPoints = 0.0;
											List<ChecklistItem> checklistQuestionItemList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistTopicItem.getId());
											
											for (ChecklistItem checklistQuestionItem : checklistQuestionItemList) {
												int maxOptionVal = ChecklistOption.findMaxOptionValueByQuestionId(checklistQuestionItem.getId());
												totalTopicPoints += maxOptionVal;
											}
											
											totalPoints += totalTopicPoints;
											topicWiseMaxPoint.put(checklistTopicItem.getId(), totalTopicPoints);
											System.out.println("total topic points" + totalTopicPoints);
											System.out.println("total  points" + totalPoints);
											
										}
										
										for (Entry<Long, java.lang.Double> entry : topicWiseMaxPoint.entrySet()) {
											Double topicsRatio = entry.getValue()/totalPoints;

											topicWiseRatioMap.put(entry.getKey(), topicsRatio);
											System.out.println("topic ratio map :" + topicWiseRatioMap);

										}
									
									}				
									
									FileWriter writer = new FileWriter(fileName);
									writer.append("id");
									writer.append('|');
									writer.append("topic");
									writer.append('|');
									writer.append("item_text");
									writer.append('|');
									writer.append("points");
									writer.append('|');
									writer.append("is_eval_item");
									writer.append('|');
									writer.append("weight");
									//writer.append('|');
									writer.append("\n");
									
									//List<Long> postMissingQueList = ItemAnalysis.findDeactivatedItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId());
									List<Long> postMissingQueList = ItemAnalysis.findDeactivatedChecklistItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId());
									
									//List<ChecklistQuestion> items=Answer.retrieveDistinctQuestion(oscePost.getId());
									List<ChecklistItem> items=Answer.retrieveDistinctQuestionItem(oscePost.getId());
									//for(ChecklistQuestion d:items)
									for(ChecklistItem d:items)
									{
										if (d.getId().equals(337l)) {
											System.out.println("TEST" + d.getParentItem().getWeight() ==null? "no weight" : d.getParentItem().getWeight());
										}
										
										if (postMissingQueList.contains(d.getId()) == false)
										{
											writer.append(d.getId().toString());
											writer.append('|');
											writer.append(d.getParentItem().getId().toString());
											writer.append('|');
											writer.append(d.getName());
											writer.append('|');
											
											List<ChecklistOption> options=d.getCheckListOptions();
											String points="";
											double pointList[] = new double[options.size()];
											
											for(int m=0;m<options.size();m++)
											{
												
												pointList[m]=new Double(options.get(m).getValue());
												if(m==0)
												{
													points=options.get(m).getValue();
												}
												else
												{
													points=points+"/"+options.get(m).getValue();
												}
											}
											
											double maxPoint=StatUtils.max(pointList);
											double average=StatUtils.mean(pointList);
											
											double weight = 0.0;
											/*if (NumberUtils.isNumber(String.valueOf(maxPoint)) && NumberUtils.isNumber(String.valueOf(average)))
											{
												weight=Answer.roundTwoDecimals(Answer.percentage(average, maxPoint));
											}*/
											
											if (oscePost.getStandardizedRole() != null) {
												if (RoleTopicFactor.RATIO.equals(oscePost.getStandardizedRole().getTopicFactor())) {
													
													if(topicWiseRatioMap.containsKey(d.getParentItem().getId())){
														Double  ratio= topicWiseRatioMap.get(d.getParentItem().getId());
														if(d.getParentItem().getWeight() != null) {
															weight =(d.getParentItem().getWeight() / 100) * ratio;	
														}
														
												 }
													
												}
												else if (RoleTopicFactor.WEIGHT.equals(oscePost.getStandardizedRole().getTopicFactor())) {
													if (d.getParentItem() != null && d.getParentItem().getWeight() != null) {
														weight = d.getParentItem().getWeight();
													}
												}
											}
											if (d.getId().equals(202l)) {
												System.out.println(weight);
											}
											
											writer.append(points);
											writer.append('|');
											writer.append(d.getIsRegressionItem().toString());
											writer.append('|');
											writer.append(""+weight);
											//writer.append('|');
											writer.append("\n");
										}										
									}
									
									writer.flush();
								    writer.close();
								}
								
							}
						}
					}
				}
				
//				createZipFile(zipFileName, fileNameList, path);
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return zipFileName;
	  }
	  
	  
	  /*
	   * flag is used to decide for which this file will generated
	   * If flag is true then it generated for Export and
	   * If flag is false then it generated for Rscript
	  */
	  public static String createCSV(Long osceId,HttpServletRequest request, ServletContext servletContext, Boolean flag,final List<String> fileNameList)
	  {
		  String fileName = "";
		  String zipFileName = "";
		  char alphaSeq = 'A';
		  
		  Long impressionQueId = null;
		  
		  try
		  {
			 // String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);
			  //System.out.println("PATH : " + path);
			  
			  zipFileName = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML + "OsceStatisticData.zip");
			  
			  Osce osce=Osce.findOsce(osceId);
				
				List<OsceDay> osceDays=osce.getOsce_days();
				
				CalculateCronbachValue calculateCronbachValue = new CalculateCronbachValue();
				
				for(int i=0;i<osceDays.size();i++)
				{
					OsceDay osceDay=osceDays.get(i);
					List<OsceSequence> osceSequences=osceDay.getOsceSequences();
					
					for(int a=0;a<osceSequences.size();a++)
					{
						OsceSequence osceSeq=osceSequences.get(a);
						
						List<OscePost> oscePostList = osceSeq.getOscePosts();
						
						for (OscePost oscePost : oscePostList)
						{
							if (oscePost.getStandardizedRole() != null)
								fileName = "Day"+ (i+1) + "_" + oscePost.getStandardizedRole().getShortName() + "_" + osceSeq.getLabel() +".csv";
							else
								fileName = "Day"+ (i+1) + "_" + "post" + oscePost.getId() + "_" + osceSeq.getLabel() +".csv";
							
							fileName = fileName.replaceAll("\\\\", "");
							fileName = fileName.replaceAll("\\/", "");
							fileName = fileName.replaceAll(" ", "");
							
							fileName = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML + fileName);
							fileNameList.add(fileName);
							
							//System.out.println("FILE PATH : " + fileName);
							
							FileWriter writer = new FileWriter(fileName);
							writer.append("name of examiner");
							writer.append('|');
							writer.append("id of examiner");
							writer.append('|');
							writer.append("name of student");
							writer.append('|');
							writer.append("id of student");
							writer.append('|');
							
							
							//List<Long> postMissingQueList = ItemAnalysis.findDeactivatedItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId());
							List<Long> postMissingQueList = ItemAnalysis.findDeactivatedChecklistItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId());
							String missingQue = "";
							
							/*List<ChecklistTopic> checklistTopicList = new ArrayList<ChecklistTopic>();

							if (oscePost.getStandardizedRole() != null)
								checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();*/
							
							List<ChecklistItem> checklistItemTopicList = new ArrayList<ChecklistItem>();
							
							if (oscePost.getStandardizedRole() != null && oscePost.getStandardizedRole().getCheckList() != null) {
								checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklist(oscePost.getStandardizedRole().getCheckList().getId());
							}
							
							alphaSeq = 'A';
							
							impressionQueId = PostAnalysis.findImpressionQuestionByOscePostAndOsce(oscePost.getId(), osceId);
							
							List<Long> questionIdList = new ArrayList<Long>();
														
							//for (ChecklistTopic checklistTopic : checklistTopicList)
							for (ChecklistItem checklistItemTopic : checklistItemTopicList)
							{
								//List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
								List<ChecklistItem> itemQuestionList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistItemTopic.getId());
								
								
								int count = 1;
								//for (ChecklistQuestion question : questionList)
								for (ChecklistItem questionItem : itemQuestionList)
								{
									if ((impressionQueId == null || impressionQueId == 0)  && questionItem.getIsRegressionItem() != null && questionItem.getIsRegressionItem())
									{
										impressionQueId = questionItem.getId();
									}
									
									questionIdList.add(questionItem.getId());
									
									if (flag)
										writer.append(String.valueOf(alphaSeq) + count);
									else
										writer.append("Q" + String.valueOf(questionItem.getId()));
									
									if (flag == true && postMissingQueList.contains(questionItem.getId()))
									{
										if (missingQue == null || missingQue.isEmpty())
											missingQue = String.valueOf(alphaSeq) + count;
										else
											missingQue = missingQue + "," + String.valueOf(alphaSeq) + count;
									}
									
									count += 1;
									//writer.append(question.getId().toString());
									writer.append('|');
								}
								alphaSeq++;
							}
							
							
							//find impression item
							//Answer.findImpressionItems(oscePost);
							
							String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
							if (impressionItemString != null)
								impressionQueId=Long.parseLong(impressionItemString);
							
							if (flag)
							{
								writer.append("AddPoint");
								writer.append('|');
							}
							writer.append("impression ");

							writer.append('\n');
							
							List<Student> studentList = Answer.findDistinctStudentByOscePost(oscePost.getId());
							
							for (Student student : studentList) {
								
								//List<Answer> answerList = Answer.retrieveExportCsvDataByOscePostAndStudent(oscePost.getId(), student.getId());
								List<Answer> answerList = Answer.retrieveExportCsvDataByItemOscePostAndStudent(oscePost.getId(), student.getId());
								
								if (answerList.size() > 0 && answerList.size() <= questionIdList.size())
								{
									Answer answer = answerList.get(0);		
									writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
						    		writer.append('|');
						    		writer.append(answer.getDoctor().getId().toString() );
						    		writer.append('|');
						    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
						    		writer.append('|');
						    		writer.append(answer.getStudent().getId().toString() );
						    		writer.append('|');
						    		
						    		Map<Long, ChecklistOption> answerMap = new HashMap<Long, ChecklistOption>();
						    		
						    		for (Answer ans : answerList) {
						    			/*if (ans.getChecklistQuestion() != null)
						    				answerMap.put(ans.getChecklistQuestion().getId(), ans.getChecklistOption());*/
						    			
						    			if (ans.getChecklistItem() != null)
						    				answerMap.put(ans.getChecklistItem().getId(), ans.getChecklistOption());
									}
						    		
						    		for (Long queId : questionIdList)
						    		{
						    			if (answerMap.containsKey(queId))
						    				writer.append(answerMap.get(queId) == null ? "0" : answerMap.get(queId).getValue());
						    			else
						    				writer.append("0");
						    			
						    			writer.append('|');
						    		}
						    		
						    		if (flag)
					    			{	
					    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size() - 1).getDoctor().getId());
					    				writer.append(addPoint.toString());
					    				writer.append('|');
					    			}
					    			
					    			if (impressionQueId != null && impressionQueId != 0)
					    			{
					    				//Answer impressionItem1=Answer.findAnswer(student.getId(), impressionQueId, osceDay.getId());
					    				Answer impressionItem1=Answer.findItemAnswer(student.getId(), impressionQueId, osceDay.getId());
					    				//writer.append(impressionItem1.getChecklistOption().getValue());
					    				if (impressionItem1 != null)
					    					writer.append(impressionItem1.getChecklistOption().getValue());
					    				else
					    					writer.append('0');
					    			}
						    		else
						    		{
						    			writer.append('0');//impression
						    		}	    			
					    			
					    			writer.append('\n');
								}
								
							}
						    
						    /*List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(osceDay.getId(), oscePost.getId());
						    Long lastCandidateId = null;
						    Answer answer = null;
						    
						    for (int j=0; j<answerList.size(); j++)
						    {
						    	answer = answerList.get(j);
						    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
						    	{
						    		if (lastCandidateId != null)
						    		{
						    			if (flag)
						    			{	
						    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(j-1).getDoctor().getId());
						    				writer.append(addPoint.toString());
						    				writer.append('|');
						    			}
						    			
						    			if (impressionQueId != null && impressionQueId != 0)
						    			{
						    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, osceDay.getId());
						    				//writer.append(impressionItem1.getChecklistOption().getValue());
						    				if (impressionItem1 != null)
						    					writer.append(impressionItem1.getChecklistOption().getValue());
						    				else
						    					writer.append('0');
						    			}
							    		else
							    		{
							    			writer.append('0');//impression
							    		}
						    		}
						    		
						    		writer.append('\n');
						    		
					    			
					    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
						    		writer.append('|');
						    		writer.append("\"" + answer.getDoctor().getId() );
						    		writer.append('|');
						    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
						    		writer.append('|');
						    		writer.append("\"" + answer.getStudent().getId());
						    		writer.append('|');
						    		
						    		else	
						    		{
						    			writer.append(answer.getDoctor().getPreName() + " " + answer.getDoctor().getName());
						    			writer.append('|');
writer.append(answer.getDoctor().getId() );
						    			writer.append('|');
							    		writer.append(answer.getStudent().getPreName() + " " + answer.getStudent().getName());
							    		writer.append('|');
writer.append(answer.getStudent().getId() );
						    			writer.append('|');
						    		}		    		
						    		
						    		lastCandidateId = answer.getStudent().getId();
						    	}
						    	
						    	writer.append(answer.getChecklistOption().getValue());
						    	//writer.append(answer.getChecklistQuestion().getId().toString());
					    		writer.append('|');
						    }
						    
						    if (answerList.size() > 0)
						    {
						    	if (flag)
				    			{	
				    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
				    				writer.append(addPoint.toString());
				    				writer.append('|');
				    			}
						    	
						    	if (impressionQueId != null)
				    			{
				    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, osceDay.getId());
				    				//writer.append(impressionItem1.getChecklistOption().getValue());
				    				if (impressionItem1 != null)
				    					writer.append(impressionItem1.getChecklistOption().getValue());
				    				else
				    					writer.append('0');
				    			}
					    		else
					    		{
					    			writer.append('0');//impression
					    		}
						    }*/
					    	
						    writer.flush();
						    writer.close();
						    
						    if (studentList.size() == 0)
						    {
						    	FileWriter writer2 = new FileWriter(fileName);
						    	writer2.write("name of examiner | id of examiner | name of student | id of student | impression | pass/fall");
						    	writer2.flush();
						    	writer2.close();						    
						    }
						    
						    if (flag)
						    	calculateCronbachValue.calculatePassFail(fileName, missingQue);
						}
					}
				}
				
//				createZipFile(zipFileName, fileNameList, path);
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return zipFileName;
	  }
	  
	 public static void createZipFile(String zipFilePath, List<String> fileNameList,OutputStream os)
	 {
				       
		    try {
		      ZipOutputStream zipOut = new ZipOutputStream(os);
		      
		      for (int i = 0 ; i < fileNameList.size() ; i ++) {
		        
					byte[] buf = new byte[1024];
					int len;
					File file = new File(fileNameList.get(i));
					FileInputStream in=null;
					if(file.exists())
					{
					 in = new FileInputStream(file);
					zipOut.putNextEntry(new ZipEntry(file.getName()));
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					
					in.close();
					
					}
					
		            zipOut.closeEntry();	            
		            
		            if(file.exists())
		            {
		            	file.delete();
		            }
		      }
		  
		      zipOut.close();
		      
		      Log.info("Done...");
		      
		    } catch (FileNotFoundException e) {
		    	Log.error(e.getMessage(),e);
		    } catch (IOException e) {
		    	Log.error(e.getMessage(),e);
		    }
	 }

	 public static String createExaminerCSV(HttpServletRequest request, ServletContext servletContext, Long oscePostId, Long examinerId, String fileName, Integer addPoint, Long impressionQueId)
	  {
		  
		  //Long impressionQueId = null;
		   
		  try
		  {
  				String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);

				fileName = path + fileName;
				
				//System.out.println("FILE PATH : " + fileName);
				
				FileWriter writer = new FileWriter(fileName);
				writer.append("examiners");
				writer.append('|');
				writer.append("students");
				writer.append('|');
				
				OscePost oscePost = OscePost.findOscePost(oscePostId);

				
				/*List<ChecklistTopic> checklistTopicList = new ArrayList<ChecklistTopic>();
				
				if (oscePost.getStandardizedRole() != null)
					checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();*/
				
				List<ChecklistItem> checklistItemTopicList = new ArrayList<ChecklistItem>();
				
				if (oscePost.getStandardizedRole() != null && oscePost.getStandardizedRole().getCheckList() != null) {
					checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklist(oscePost.getStandardizedRole().getCheckList().getId());
				}
				
				//impressionQueId = null;
				
				//String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
				
				/*if (impressionItemString != null)
					impressionQueId=Long.parseLong(impressionItemString);*/
				
				List<Long> questionIdList = new ArrayList<Long>();
				
				//for (ChecklistTopic checklistTopic : checklistTopicList)
				for (ChecklistItem checklistItemTopic : checklistItemTopicList)
				{
					//List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
					List<ChecklistItem> itemQuestionList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistItemTopic.getId());
					
					int count = 1;
					//for (ChecklistQuestion question : questionList)
					for (ChecklistItem questionItem : itemQuestionList)
					{
						if ((impressionQueId == null || impressionQueId.equals(0))  && questionItem.getIsRegressionItem() != null && questionItem.getIsRegressionItem())
						{
							impressionQueId = questionItem.getId();
						}
						
						questionIdList.add(questionItem.getId());
						
						writer.append("Q" + String.valueOf(questionItem.getId()));					
						writer.append('|');
					}
				}
				writer.append("AddPoint");
				writer.append('|');
				writer.append("impression ");
				writer.append('\n');
				
				List<Student> studentList = Answer.findDistinctStudentByExaminerAndOscePost(oscePostId, examinerId);
				
				for (Student student : studentList) {
					
					//List<Answer> answerList = Answer.retrieveExportCsvDataByOscePostAndStudent(oscePost.getId(), student.getId());
					List<Answer> answerList = Answer.retrieveExportCsvDataByItemOscePostAndStudent(oscePost.getId(), student.getId());
					
					if (answerList.size() > 0 && answerList.size() <= questionIdList.size())
					{
						Answer answer = answerList.get(0);		
						writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		Map<Long, ChecklistOption> answerMap = new HashMap<Long, ChecklistOption>();
			    		
			    		for (Answer ans : answerList) {
			    			if (ans.getChecklistItem() != null)
			    				answerMap.put(ans.getChecklistItem().getId(), ans.getChecklistOption());
						}
			    		
			    		for (Long queId : questionIdList)
			    		{
			    			if (answerMap.containsKey(queId))
			    				writer.append(answerMap.get(queId) == null ? "0" : answerMap.get(queId).getValue());
			    			else
			    				writer.append("0");
			    			
			    			writer.append('|');
			    		}
			    		
			    		if (impressionQueId != null)
		    			{
			    			writer.append(addPoint.toString());
		    				writer.append('|');
		    				
		    				//Answer impressionItem1=Answer.findAnswer(student.getId(), impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
		    				Answer impressionItem1=Answer.findItemAnswer(student.getId(), impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
		    				
		    				if (impressionItem1 != null)
		    					writer.append(impressionItem1.getChecklistOption().getValue());
		    				else
		    					writer.append('0');
		    			}
			    		else
			    		{
			    			writer.append(addPoint.toString());
		    				writer.append('|');
			    			writer.append('0');//impression
			    		}
			    		
			    		writer.append('\n');
					}
				}
				
			    
			    /*List<Answer> answerList = Answer.retrieveExportCsvDataByOscePostAndExaminer(oscePostId, examinerId);
			    Long lastCandidateId = null;
			    Answer answer = null;
			    for (int j=0; j<answerList.size(); j++)
			    {
			    	answer = answerList.get(j);
			    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
			    	{
			    		if (lastCandidateId != null)
			    		{
			    			if (impressionQueId != null && impressionQueId != 0)
			    			{
			    				writer.append(addPoint.toString());
			    				writer.append('|');
			    				
			    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
			    				//writer.append(impressionItem1.getChecklistOption().getValue());
			    				if (impressionItem1 != null)
			    					writer.append(impressionItem1.getChecklistOption().getValue());
			    				else
			    					writer.append('0');
			    			}
				    		else
				    		{
				    			writer.append(addPoint.toString());
			    				writer.append('|');
				    			writer.append('0');//impression
				    		}
			    		}
			    		
			    		writer.append('\n');
		    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    			    		
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
			    	writer.append('|');
			    }
			    
			    if (answerList.size() > 0)
			    {
			    	if (impressionQueId != null)
	    			{
			    		writer.append(addPoint.toString());
	    				writer.append('|');
	    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
	    				if (impressionItem1 != null)
	    					writer.append(impressionItem1.getChecklistOption().getValue());
	    				else
	    					writer.append('0');
	    			}
		    		else
		    		{
		    			writer.append(addPoint.toString());
	    				writer.append('|');
		    			writer.append('0');//impression
		    		}
			    }*/
		    	
			    writer.flush();
			    writer.close();
							
			    return fileName;			    
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return fileName;
	  }
	 
	 public static List<String> createOscePostCSV(HttpServletRequest request, ServletContext servletContext, Long oscePostId, String fileName, List<String> examinerId, List<Integer> addPoints, Long impressionQueId)
	  {
		  
		 // Long impressionQueId = null;
		 List<String> valueList = new ArrayList<String>();
		 
		  
		  try
		  {
  				String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);
  				fileName = path + fileName;
				
				//System.out.println("FILE PATH : " + fileName);
				
				FileWriter writer = new FileWriter(fileName);
				writer.append("examiners");
				writer.append('|');
				writer.append("students");
				writer.append('|');
				
				OscePost oscePost = OscePost.findOscePost(oscePostId);
				
				/*List<ChecklistTopic> checklistTopicList = new ArrayList<ChecklistTopic>();
				
				if (oscePost.getStandardizedRole() != null)
					checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();*/
				
				List<ChecklistItem> checklistItemTopicList = new ArrayList<ChecklistItem>();
				
				if (oscePost.getStandardizedRole() != null && oscePost.getStandardizedRole().getCheckList() != null) {
					checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklist(oscePost.getStandardizedRole().getCheckList().getId());
				}
				
				//impressionQueId = null;
				
				//String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
				/*if (impressionQueId != null && impressionQueId > 0l)
					impressionQueId=Long.parseLong(impressionItemString);*/
				
				List<Long> questionIdList = new ArrayList<Long>();
				
				//for (ChecklistTopic checklistTopic : checklistTopicList)
				for (ChecklistItem checklistItemTopic : checklistItemTopicList)
				{
					//List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
					List<ChecklistItem> itemQuestionList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistItemTopic.getId());
					
					int count = 1;
					//for (ChecklistQuestion question : questionList)
					for (ChecklistItem questionItem : itemQuestionList)
					{
						if ((impressionQueId == null || impressionQueId == 0)  && questionItem.getIsRegressionItem() != null && questionItem.getIsRegressionItem())
						{
							impressionQueId = questionItem.getId();
						}
						
						writer.append("Q" + String.valueOf(questionItem.getId()));
						
						writer.append('|');
						
						questionIdList.add(questionItem.getId());
					}
					
				}
				writer.append("AddPoint");
				writer.append('|');
				writer.append("impression ");
				writer.append('\n');
				
				List<Student> studentList = Answer.findDistinctStudentByOscePost(oscePost.getId());
				
				for (Student student : studentList) {
					
					//List<Answer> answerList = Answer.retrieveExportCsvDataByOscePostAndStudent(oscePost.getId(), student.getId());
					List<Answer> answerList = Answer.retrieveExportCsvDataByItemOscePostAndStudent(oscePost.getId(), student.getId());
					
					if (answerList.size() > 0 && answerList.size() <= questionIdList.size())
					{
						Answer answer = answerList.get(0);		
						writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		Map<Long, ChecklistOption> answerMap = new HashMap<Long, ChecklistOption>();
			    		
			    		for (Answer ans : answerList) {
			    			/*if (ans.getChecklistQuestion() != null)
			    				answerMap.put(ans.getChecklistQuestion().getId(), ans.getChecklistOption());*/
			    			
			    			if (ans.getChecklistItem() != null)
			    				answerMap.put(ans.getChecklistItem().getId(), ans.getChecklistOption());
						}
			    		
			    		for (Long queId : questionIdList)
			    		{
			    			if (answerMap.containsKey(queId))
			    				writer.append(answerMap.get(queId) == null ? "0" : answerMap.get(queId).getValue());
			    			else
			    				writer.append("0");
			    			
			    			writer.append('|');
			    		}
			    		
			    		Integer addPoint = 0;
		    			String key="p"+oscePostId+"e"+answerList.get(answerList.size()-1).getDoctor().getId();
		    			if (examinerId.contains(key))
		    			{
		    				int index = examinerId.indexOf(key);
		    				addPoint = addPoints.get(index);
		    			}
		    			else
		    			{
		    				addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
		    			}
		    			
		    			if (impressionQueId != null && impressionQueId != 0)
		    			{
		    				writer.append(addPoint.toString());
		    				writer.append('|');
		    				
		    				//Answer impressionItem1=Answer.findAnswer(student.getId(), impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
		    				Answer impressionItem1=Answer.findItemAnswer(student.getId(), impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
		    				//writer.append(impressionItem1.getChecklistOption().getValue());
		    				if (impressionItem1 != null)
		    					writer.append(impressionItem1.getChecklistOption().getValue());
		    				else
		    					writer.append('0');
		    			}
			    		else
			    		{
			    			writer.append(addPoint.toString());
		    				writer.append('|');
			    			writer.append('0');//impression
			    		}	    			
		    			
		    			writer.append('\n');
					}
					
				}
				
			    /*List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(oscePost.getOsceSequence().getOsceDay().getOsce().getId(), oscePost.getId());
			    Long lastCandidateId = null;
			    Answer answer = null;
			    for (int j=0; j<answerList.size(); j++)
			    {
			    	answer = answerList.get(j);
			    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
			    	{
			    		if (lastCandidateId != null)
			    		{
			    			Integer addPoint = 0;
			    			String key="p"+oscePostId+"e"+answerList.get(j-1).getDoctor().getId();
			    			if (examinerId.contains(key))
			    			{
			    				int index = examinerId.indexOf(key);
			    				addPoint = addPoints.get(index);
			    			}
			    			else
			    			{
			    				addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(j-1).getDoctor().getId());
			    			}
			    			
			    			if (impressionQueId != null && impressionQueId != 0)
			    			{
			    				writer.append(addPoint.toString());
			    				writer.append('|');
			    				
			    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
			    				if (impressionItem1 != null)
			    					writer.append(impressionItem1.getChecklistOption().getValue());
			    				else
			    					writer.append('0');
			    			}
				    		else
				    		{
				    			writer.append(addPoint.toString());
			    				writer.append('|');
			    				
				    			writer.append('0');//impression
				    		}
			    			
			    		}
			    		
			    		writer.append('\n');
			    		
		    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
			    	writer.append('|');
			    }
			    
			    if (answerList.size() > 0)
			    {
			    	answer = answerList.get(answerList.size() - 1);
			    	Integer addPoint = 0;
	    			String key="p"+oscePostId+"e"+answer.getDoctor().getId();
	    			if (examinerId.contains(key))
	    			{
	    				int index = examinerId.indexOf(key);
	    				addPoint = addPoints.get(index);
	    			}
	    			else
	    			{
	    				addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
	    			}
			    	
			    	if (impressionQueId != null)
	    			{
			    		writer.append(addPoint.toString());
	    				writer.append('|');
	    				
	    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
	    				//writer.append(impressionItem1.getChecklistOption().getValue());
	    				if (impressionItem1 != null)
	    					writer.append(impressionItem1.getChecklistOption().getValue());
	    				else
	    					writer.append('0');
	    			}
		    		else
		    		{
		    			writer.append(addPoint.toString());
	    				writer.append('|');
	    				
		    			writer.append('0');//impression
		    		}
			    	
			    }*/
		    	
			    writer.flush();
			    writer.close();
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  valueList.add(fileName);
		  valueList.add(impressionQueId == null ? null : String.valueOf(impressionQueId));
		  return valueList;
	  }

	 public static String createOscePostGraphCSV(HttpServletRequest request, ServletContext servletContext, String fileName, OscePost oscePost)
	  {
		  
		  char alphaSeq = 'A';
		  Long impressionQueId = null;
		  
		  try
		  {
			  	String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML + fileName);
			  	
			  	Log.info("PATH : " + path);
				
				CalculateCronbachValue calculateCronbachValue = new CalculateCronbachValue();
	
				fileName = path;
				
				Log.info("fileName : " + fileName);
				
				FileWriter writer = new FileWriter(fileName);
				writer.append("examiners");
				writer.append('|');
				writer.append("students");
				writer.append('|');
				
				//List<Long> postMissingQueList = ItemAnalysis.findDeactivatedItemByOscePostAndOsceSeq(oscePost.getId(), oscePost.getOsceSequence().getId());
				List<Long> postMissingQueList = ItemAnalysis.findDeactivatedChecklistItemByOscePostAndOsceSeq(oscePost.getId(), oscePost.getOsceSequence().getId());
				String missingQue = "";
				
				/*List<ChecklistTopic> checklistTopicList = new ArrayList<ChecklistTopic>();
				
				if (oscePost.getStandardizedRole() != null)
					checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();*/
				alphaSeq = 'A';
				
				impressionQueId = PostAnalysis.findImpressionQuestionByOscePostAndOsce(oscePost.getId(), oscePost.getOsceSequence().getOsceDay().getOsce().getId());
				
				List<ChecklistItem> checklistItemTopicList = new ArrayList<ChecklistItem>();
				
				if (oscePost.getStandardizedRole() != null && oscePost.getStandardizedRole().getCheckList() != null) {
					checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklist(oscePost.getStandardizedRole().getCheckList().getId());
				}
				/*String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
				if (impressionItemString != null)
					impressionQueId=Long.parseLong(impressionItemString);*/
				
				List<Long> questionIdList = new ArrayList<Long>();
				
				//for (ChecklistTopic checklistTopic : checklistTopicList)
				for (ChecklistItem checklistTopicItem : checklistItemTopicList)
				{
					//List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
					List<ChecklistItem> itemQuestionList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistTopicItem.getId());
					
					
					int count = 1;
					//for (ChecklistQuestion question : questionList)
					for (ChecklistItem questionItem : itemQuestionList)
					{
						if ((impressionQueId == null || impressionQueId == 0)  && questionItem.getIsRegressionItem() != null && questionItem.getIsRegressionItem())
						{
							impressionQueId = questionItem.getId();
						}
						
						questionIdList.add(questionItem.getId());
						
						writer.append("Q" + String.valueOf(questionItem.getId()));
						
						if (postMissingQueList.contains(questionItem.getId()))
						{
							if (missingQue == null || missingQue.isEmpty())
								missingQue = "Q" + String.valueOf(questionItem.getId());
							else
								missingQue = missingQue + "," + "Q" + String.valueOf(questionItem.getId());
						}
						
						count += 1;
						//writer.append(question.getId().toString());
						writer.append('|');
					}
					alphaSeq++;
				}
				writer.append("AddPoint");
				writer.append('|');
				writer.append("impression ");
			    writer.append('\n');
			    
				List<Student> studentList = Answer.findDistinctStudentByOscePost(oscePost.getId());
				
				for (Student student : studentList) {
					
					//List<Answer> answerList = Answer.retrieveExportCsvDataByOscePostAndStudent(oscePost.getId(), student.getId());
					List<Answer> answerList = Answer.retrieveExportCsvDataByItemOscePostAndStudent(oscePost.getId(), student.getId());
					
					if (answerList.size() > 0 && answerList.size() <= questionIdList.size())
					{
						Answer answer = answerList.get(0);		
						writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		Map<Long, ChecklistOption> answerMap = new HashMap<Long, ChecklistOption>();
			    		
			    		for (Answer ans : answerList) {
			    			/*if (ans.getChecklistQuestion() != null)
			    				answerMap.put(ans.getChecklistQuestion().getId(), ans.getChecklistOption());*/
			    			if (ans.getChecklistItem() != null)
			    				answerMap.put(ans.getChecklistItem().getId(), ans.getChecklistOption());
						}
			    		
			    		for (Long queId : questionIdList)
			    		{
			    			if (answerMap.containsKey(queId))
			    				writer.append(answerMap.get(queId) == null ? "0" : answerMap.get(queId).getValue());
			    			else
			    				writer.append("0");
			    			
			    			writer.append('|');
			    		}
			    		
			    		Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
	    				writer.append(addPoint.toString());
	    				writer.append('|');		    			
		    			
		    			if (impressionQueId != null && impressionQueId != 0)
		    			{
		    				//Answer impressionItem1=Answer.findAnswer(student.getId(), impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
		    				Answer impressionItem1=Answer.findItemAnswer(student.getId(), impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
		    				if (impressionItem1 != null)
		    					writer.append(impressionItem1.getChecklistOption().getValue());
		    				else
		    					writer.append('0');
		    			}
			    		else
			    		{
			    			writer.append('0');//impression
			    		}
		    			
		    			writer.append('\n');
					}
					
				}
				
			   /* List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(oscePost.getOsceSequence().getOsceDay().getId(), oscePost.getId());
			    Long lastCandidateId = null;
			    Answer answer = null;
			    for (int j=0; j<answerList.size(); j++)
			    {
			    	answer = answerList.get(j);
			    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
			    	{
			    		if (lastCandidateId != null)
			    		{
		    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(j-1).getDoctor().getId());
		    				writer.append(addPoint.toString());
		    				writer.append('|');
			    			
			    			
			    			if (impressionQueId != null && impressionQueId != 0)
			    			{
			    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
			    				if (impressionItem1 != null)
			    					writer.append(impressionItem1.getChecklistOption().getValue());
			    				else
			    					writer.append('0');
			    			}
				    		else
				    		{
				    			writer.append('0');//impression
				    		}
			    		}
			    		
			    		writer.append('\n');
			    		
		    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
			    	//writer.append(answer.getChecklistQuestion().getId().toString());
		    		writer.append('|');
			    }
			    
			    if (answerList.size() > 0)
			    {
		    		Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
    				writer.append(addPoint.toString());
    				writer.append('|');
	    			
			    	
			    	if (impressionQueId != null)
	    			{
	    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
	    				if (impressionItem1 != null)
	    					writer.append(impressionItem1.getChecklistOption().getValue());
	    				else
	    					writer.append('0');
	    			}
		    		else
		    		{
		    			writer.append('0');//impression
		    		}
			    }*/
		    	
			    writer.flush();
			    writer.close();
			    
			    List<Long> valueList = new ArrayList<Long>();
			    valueList = ChecklistOption.findCheckListOptionValueByQuestionItem(impressionQueId);
			    
			    Collections.sort(valueList);
			    
			    if (valueList.size() > 0)
			    	calculateCronbachValue.createGraph(fileName, missingQue, valueList.get(0), valueList.get(valueList.size()-1));
			    else
			    	calculateCronbachValue.createGraph(fileName, missingQue, 0l, 0l);
			   	
			   	fileName = fileName.replace(".csv", ".png");

		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return fileName;
	  }
}
