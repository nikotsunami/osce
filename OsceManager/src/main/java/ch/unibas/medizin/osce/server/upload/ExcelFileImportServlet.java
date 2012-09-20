package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import ch.unibas.medizin.osce.domain.Appliance;
import ch.unibas.medizin.osce.domain.ClassificationTopic;
import ch.unibas.medizin.osce.domain.MainClassification;
import ch.unibas.medizin.osce.domain.Skill;
import ch.unibas.medizin.osce.domain.SkillHasAppliance;
import ch.unibas.medizin.osce.domain.SkillLevel;
import ch.unibas.medizin.osce.domain.Topic;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.shared.i18n.LearningObjective;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Servlet implementation class XlsFileImportServlet
 */
public class ExcelFileImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   

//	 private static String appUploadDirectory=OsMaFilePathConstant.EXCEL_FILEPATH;
	public String fetchRealPath(HttpServletRequest request) {

		String fileSeparator = System.getProperty("file.separator");
		return request.getSession().getServletContext().getRealPath(fileSeparator) + OsMaFilePathConstant.DOWNLOAD_DIR_PATH + fileSeparator;

	}
			
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		
		LearningObjective constants = GWTI18N.create(LearningObjective.class, new Locale("de").toString());
		
		String path="";
		
		File appUploadedFile = null;
		
		  if(!ServletFileUpload.isMultipartContent(request)){
	            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
	                    "Unsupported content");
	        }
		  
//		  String  cntxtpath=request.getSession().getServletContext().getRealPath(".");
//		  String uploadDir=cntxtpath+appUploadDirectory;
		  
		  String uploadDir=fetchRealPath(request);
		 
	        // Create a factory for disk-based file items
	        FileItemFactory factory = new DiskFileItemFactory();

	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	       
	        ProgressListener progressListener = new ProgressListener(){
	               private long megaBytes = -1;
	               public void update(long pBytesRead, long pContentLength, int pItems) {
	                   long mBytes = pBytesRead / 1000000;
	                   if (megaBytes == mBytes) {
	                       return;
	                   }
	                   megaBytes = mBytes;
	                   Log.info("We are currently reading item " + pItems);
	                   if (pContentLength == -1) {
	                       Log.info("So far, " + pBytesRead + " bytes have been read.");
	                   } else {
	                       Log.info("So far, " + pBytesRead + " of " + pContentLength
	                                          + " bytes have been read.");
	                   }
	               }
	            };
	        upload.setProgressListener(progressListener); 
	        String fileName = "";
	        String temp="";

	        try {
	            @SuppressWarnings("unchecked")
	            List<FileItem> items = upload.parseRequest(request);
	            
	            for (FileItem item : items)
	            {
	                if (item.isFormField())
	                {
	                }
	                else
	                {
	                	temp=FilenameUtils. getName(item.getName());
	                }
	            }
	           
	            fileName = temp;
	            
	            for (FileItem item : items) {
	                // process only file upload - discard other form item types
	                if (item.isFormField())
	                {
	                    continue;
	                }
	                else
	                {
	                	path = uploadDir + fileName;
	                	appUploadedFile = new File(uploadDir, fileName);
	                	appUploadedFile.createNewFile();
	                	item.write(appUploadedFile);
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("An error occurred while creating the file : " + e.getMessage());
	        }
	        
	        FileInputStream fileStream = new FileInputStream(path);       
	        POIFSFileSystem inputFile = new POIFSFileSystem(fileStream);
	        HSSFWorkbook workbook = new HSSFWorkbook(inputFile);
	        HSSFSheet sheet = workbook.getSheetAt(0);
	        HSSFRow row;
	        
	        Boolean flag = false;
	        
	        String col1, col2, col3, col4, col5, col6, col7, col8, col9;
	        
	        MainClassification mainClassification = null;
	        ClassificationTopic classificationTopic = null;
	        Topic topic = null;
	        SkillLevel skillLevel = null;
	        Skill skill = null;
	        Long skillId = null;
	       	        
	        for (int i=0; i<sheet.getLastRowNum(); i++)
	        {
	          	row = sheet.getRow(i);        	
	          	
        		if ((row.getCell(0).toString()).equals("Code"))
        		{
        			flag = true;
	        		continue;
	        	}
	        			
	        	if (flag == true)
	        	{	        		
	        		col1 = row.getCell(0).toString();
	        		col2 = row.getCell(1).toString();
	        		col3 = row.getCell(2).toString();
	        		col4 = row.getCell(3).toString();
	        		col5 = row.getCell(4).toString();
	        		col6 = row.getCell(5).toString();
	        		col7 = row.getCell(6).toString();
	        		col8 = row.getCell(7).toString();
	        		col9 = row.getCell(8).toString();
	        		
	        		if (!col1.equals(""))
	        		{
	        			String topicShortcut = "";
	        			String mainStr = col1.substring(0,1);
	        			
	        			List<MainClassification> mainList = MainClassification.findMainClassificationByShortCut(mainStr);
	        			
	        			if (mainList.size() == 0)
	        			{
	        				mainClassification = new MainClassification();
	        				mainClassification.setShortcut(mainStr);
	        				mainClassification.setDescription(constants.getString(mainStr));
	        				mainClassification.persist();	        				
	        			}
	        			else 
	        			{
	        				mainClassification = mainList.get(0);
	        			}
	        			
	        			String othrStr =  "" ; 
	        			if (col1.length() < 4)
	        				othrStr = col1.substring(2, col1.length());
	        			else
	        				othrStr = col1.substring(2, 4);
	        			
	        			if (othrStr.matches("[A-Za-z]+"))
	        			{	
	        				topicShortcut = col1.substring(5, col1.length());	
	        			}
	        			else
	        			{
	        				othrStr = "";
	        				
	        				topicShortcut = col1.substring(2, col1.length());        				
	        			}
	        			
	        			
	        			List<ClassificationTopic> topicList = ClassificationTopic.findClassificationTopicByShortCutAndMainClassi(othrStr, mainClassification);
        				if (topicList.size() == 0)
        				{
        					classificationTopic = new ClassificationTopic();
        					classificationTopic.setShortcut(othrStr);
        					
        					if (othrStr.equals(""))
        					{
        						if (mainStr.equals("P"))
        							classificationTopic.setDescription(constants.getString("CLASSIFICATION_TOPIC_P"));
        						else if (mainStr.equals("S"))
        							classificationTopic.setDescription(constants.getString("CLASSIFICATION_TOPIC_S"));
        					}        					       						
        					else
        						classificationTopic.setDescription(constants.getString(othrStr));
        					
        					classificationTopic.setMainClassification(mainClassification);
        					classificationTopic.persist();
        				}
        				else
        				{
        					classificationTopic = topicList.get(0);
        				}
        				
        				//insert col2 data into skill table;
        			
        				List<Skill> skillList = Skill.findSkillByTopicAndShortcut(classificationTopic.getId(), topicShortcut);
        				
        				if (skillList.size() == 0)
        				{
        					skill = new Skill();        				
            				skill.setShortcut(topicShortcut);
            				skill.setDescription(col2);
            				skill.persist();
        				}
        				else
        				{
        					skill = skillList.get(0);
        					skill.setDescription(col2);
        					skill.persist();
        				}
        				
        				
        				skillId = skill.getId();
	        			
	        		}
	        		
	        		if (!col3.equals(""))
	        		{
	        			List<Topic> topicList = Topic.findTopicByTopicDescAndClassificationTopic(col3, classificationTopic.getId());
	        			
	        			if (topicList.size() == 0)
	        			{
	        				topic = new Topic();
	        				topic.setTopicDesc(col3);
	        				topic.setClassificationTopic(classificationTopic);
	        				topic.persist();
	        			}
	        			else
	        			{
	        				topic = topicList.get(0);	        				
	        			}
	        		}
	        		
	        		if (!col4.equals(""))
	        		{
	        			List<SkillLevel> levelList = SkillLevel.getSkillLevelByLevelNumber(Integer.parseInt(col4));
	        			
	        			if (levelList.size() == 0)
	        			{
	        				skillLevel = new SkillLevel();
	        				skillLevel.setLevelNumber(Integer.parseInt(col4));
	        				skillLevel.persist();
	        			}
	        			else
	        			{
	        				skillLevel = levelList.get(0);
	        			}
	        			
	        			skill = Skill.findSkill(skillId);
	        			skill.setTopic(topic);
	        			skill.setSkillLevel(skillLevel);
	        			skill.persist();
	        			
	        			/*List<Skill> skillList = Skill.findSkillByTopicAndSkillLevel(topic, skillLevel);
	        			if (skillList.size() == 0)
	        			{
	        				skill = new Skill();
	        				skill.setTopic(topic);
	        				skill.setSkillLevel(skillLevel);
	        				skill.persist();
	        			}
	        			else
	        			{
	        				skill = skillList.get(0);
	        			}*/
	        		}
	        		else if (col4.equals(""))
	        		{
	        			skill = Skill.findSkill(skillId);
	        			skill.setTopic(topic);
	        			skill.persist();
	        			/*List<Skill> skillList = Skill.findSkillByTopic(topic);
	        			if (skillList.size() == 0)
	        			{
	        				skill = new Skill();
	        				skill.setTopic(topic);
	        				skill.persist();
	        			}
	        			else
	        			{
	        				skill = skillList.get(0);
	        			}*/
	        		}
	        		
	        		if (!col5.equals(""))
	        		{
	        			insertAppliance(col5, skill);
	        		}
	        		
	        		if (!col6.equals(""))
	        		{
	        			insertAppliance(col6, skill);
	        		}
	        		
	        		if (!col7.equals(""))
	        		{
	        			insertAppliance(col7, skill);
	        		}
	        		
	        		if (!col8.equals(""))
	        		{
	        			insertAppliance(col8, skill);
	        		}
	        		
	        		if (!col9.equals(""))
	        		{
	        			insertAppliance(col9, skill);
	        		}
	        	}
	        	Log.info(i + " RECORD INSERTED SUCCESSFULLY IN ALL TABLES.");
	        }
	        
	         
	        try
	        {        	
	        	System.out.println("~~Deleted : " + appUploadedFile.delete());
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void insertAppliance(String value, Skill skill)
	{
		SkillHasAppliance skillHasAppliance = null;
		Appliance appliance = null;
		
		List<Appliance> applist = Appliance.getAppliacneByShortcut(value);
		if (applist.size() == 0)
		{
			appliance = new Appliance();
			appliance.setShortcut(value);
			appliance.persist();
		}
		else
		{
			appliance = applist.get(0);
		}
		
		if (skill != null)
		{
			List<SkillHasAppliance> skillAppList = SkillHasAppliance.findSkillHasApplianceBySkillAndAppliance(skill, appliance);
		
			if (skillAppList.size() == 0)
			{
				skillHasAppliance = new SkillHasAppliance();
				skillHasAppliance.setSkill(skill);
				skillHasAppliance.setAppliance(appliance);
				skillHasAppliance.persist();
			}
		}
	}
}