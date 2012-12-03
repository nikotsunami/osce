package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gwt.core.client.GWT;

import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

public class ExportStatisticData extends HttpServlet{

	
	private static Logger Log = Logger.getLogger(UploadServlet.class);
	
	
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException 
	  {
		  String osceId=request.getParameter("osceId");
		  String fileName=createCSV(new Long(osceId),request);
		  
		  try{
				
				
				
			  resp.setContentType("application/x-download");
				
			  resp.setHeader("Content-Disposition", "attachment; filename=" + "exportStatisticData"+osceId+".csv");
				
				   

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
				e.printStackTrace();
			}
	  }
	  
	  
	  public String createCSV(Long osceId,HttpServletRequest request)
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
			    List<Answer> csvData=Answer.retrieveExportCsvData(osceId);
			    for(int i=0;i<csvData.size();i++)
			    {
			    	Answer answer=csvData.get(i);
			    	writer.append(answer.getDoctor().getPreName() + " "+ answer.getDoctor().getName());
			    	writer.append('|');
			    	writer.append(answer.getStudent().getPreName() + " "+ answer.getStudent().getName());
			    	writer.append('|');
			    	
			    	/*for(int j=i;j<csvData.size();j++)
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
			    	}*/
			    	
			    	for(int j=0;j<questions.size();j++)
			    	{
			    		Answer item=Answer.findAnswer(answer.getStudent().getId(), questions.get(j).getId());
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
			    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString));
			    			if(impressionItem !=null)
			    				writer.append(impressionItem.getChecklistOption().getValue());
			    			else
			    				writer.append('0');
			    		}
			    	}
	    			 writer.append('\n');
			    	
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
	  }
}
