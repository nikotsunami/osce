package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;

public class ExportStatisticData extends HttpServlet{

	
	private static Logger Log = Logger.getLogger(UploadServlet.class);
	
	
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException 
	  {
		  String osceId=request.getParameter("osceId");
		  String fileName=createCSV(new Long(osceId),request);
		  
		  try{
				
				
				
			  resp.setContentType("application/x-download");
				
			  resp.setHeader("Content-Disposition", "attachment; filename=" + "OsceStatisticData.zip");
				
				   

				Log.info("path :" + fileName);
				//String file=OsMaFilePathConstant.ROLE_IMAGE_FILEPATH+path;
				Log.info(" file :" + fileName);
				
				OutputStream out = resp.getOutputStream();
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
				Log.error(e.getMessage(),e);
			}
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
	  
	  public String createCSV(Long osceId,HttpServletRequest request)
	  {
		  String fileName = "";
		  String zipFileName = "";
		  char alphaSeq = 'A';
		  List<String> fileNameList = new ArrayList<String>();
		  Long impressionQueId = null;
		  
		  try
		  {
			  String path=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML);
			  System.out.println("PATH : " + path);
			  //String fileName=path+System.currentTimeMillis()+".csv";
			  zipFileName = path + "OsceStatisticData.zip";
			  
			  Osce osce=Osce.findOsce(osceId);
				
				List<OsceDay> osceDays=osce.getOsce_days();
				
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
							fileName = "Day"+ (i+1) + "_" + oscePost.getStandardizedRole().getShortName() + ".csv";
							fileName = path + fileName;
							fileNameList.add(fileName);
							
							//System.out.println("FILE PATH : " + fileName);
							
							FileWriter writer = new FileWriter(fileName);
							writer.append("examiners");
							writer.append('|');
							writer.append("students");
							writer.append('|');
							 
							List<ChecklistTopic> checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();
							alphaSeq = 'A';
							impressionQueId=null;
							for (ChecklistTopic checklistTopic : checklistTopicList)
							{
								List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
								
								int count = 1;
								for (ChecklistQuestion question : questionList)
								{
									if (impressionQueId == null && question.getIsOveralQuestion() != null && question.getIsOveralQuestion())
									{
										impressionQueId = question.getId();
									}
									writer.append(String.valueOf(alphaSeq) + count++);
									writer.append('|');
								}
								alphaSeq++;
							}
							writer.append("impression ");
						    //writer.append('\n');
						    
						    List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(osceDay.getId(), oscePost.getId());
						    Long lastCandidateId = null;
						    Answer answer = null;
						    for (int j=0; j<answerList.size(); j++)
						    {
						    	answer = answerList.get(j);
						    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
						    	{
						    		if (lastCandidateId != null)
						    		{
						    			String impressionItemString=request.getParameter("p"+answer.getOscePostRoom().getOscePost().getId().toString());
								    	if(impressionItemString==null)
								    	{
								    		if (impressionQueId != null)
							    			{
							    				Answer impressionItem1=Answer.findAnswer(answer.getStudent().getId(), impressionQueId, osceId);
							    				writer.append(impressionItem1.getChecklistOption().getValue());
							    			}
								    		else
								    		{
								    			writer.append('0');//impression
								    		}
								    	}
								    	else
								    	{
								    		if(impressionItemString.equals("0"))
								    			writer.append('0');
								    		else
								    		{
								    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString), osceId);
								    			if(impressionItem !=null)
								    				writer.append(impressionItem.getChecklistOption().getValue());
								    			else if (impressionQueId != null)
								    			{
								    				Answer impressionItem1=Answer.findAnswer(answer.getStudent().getId(), impressionQueId, osceId);
								    				writer.append(impressionItem1.getChecklistOption().getValue());
								    			}
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
						    }
						    
						    if (answerList.size() > 0)
						    {
						    	answer = answerList.get(answerList.size() - 1);
							    
							    String impressionItemString=request.getParameter("p"+answer.getOscePostRoom().getOscePost().getId().toString());
						    	if(impressionItemString==null)
						    	{	
						    		if (impressionQueId != null)
					    			{
					    				Answer impressionItem1=Answer.findAnswer(answer.getStudent().getId(), impressionQueId, osceId);
					    				writer.append(impressionItem1.getChecklistOption().getValue());
					    			}
						    		else
						    		{
						    			writer.append('0');//impression
						    		}
						    	}
						    	else
						    	{
						    		if(impressionItemString.equals("0"))
						    			writer.append('0');
						    		else
						    		{
						    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString), osceId);
						    			if(impressionItem !=null)
						    				writer.append(impressionItem.getChecklistOption().getValue());
						    			else if (impressionQueId != null)
						    			{
						    				Answer impressionItem1=Answer.findAnswer(answer.getStudent().getId(), impressionQueId, osceId);
						    				writer.append(impressionItem1.getChecklistOption().getValue());
						    			}
						    			else
						    				writer.append('0');
						    		}
						    	}
						    }
					    	
						    writer.flush();
						    writer.close();
						}
					}
				}
				
				createZipFile(zipFileName, fileNameList, path);
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return zipFileName;
	  }
	  
	 public static void createZipFile(String zipFilePath, List<String> fileNameList, String path)
	 {
				       
		    try {
		      ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
		      
		      for (int i = 0 ; i < fileNameList.size() ; i ++) {
		        
					byte[] buf = new byte[1024];
					int len;
					File file = new File(fileNameList.get(i));
					FileInputStream in = new FileInputStream(file);
					zipOut.putNextEntry(new ZipEntry(file.getName()));
					
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					
		            zipOut.closeEntry();
		      }
		  
		      zipOut.close();
		      
		      Log.info("Done...");
		      
		    } catch (FileNotFoundException e) {
		    	Log.error(e.getMessage(),e);
		    } catch (IOException e) {
		    	Log.error(e.getMessage(),e);
		    }
	 }
}
