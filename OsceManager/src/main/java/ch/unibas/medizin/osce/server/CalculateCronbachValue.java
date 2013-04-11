package ch.unibas.medizin.osce.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.ItemAnalysis;



public class CalculateCronbachValue {
	
	/* R script that contains necessary routines to calculate statistics */
	private static final String POST_METRIC_FILE =  new File(CalculateCronbachValue.class.getResource("ComputeCronbach.R").getPath()).getAbsolutePath();
	private static final String EXAMINER_RESULT_FILE =  new File(CalculateCronbachValue.class.getResource("ComputeResult.R").getPath()).getAbsolutePath();
	private static final String POST_RESULT_FILE =  new File(CalculateCronbachValue.class.getResource("ComputeStationResult.R").getPath()).getAbsolutePath();
	private static final String RESULT_FILE =  new File(CalculateCronbachValue.class.getResource("ComputeResultCsv.R").getPath()).getAbsolutePath();
	private static final String POST_GRAPH_FILE =  new File(CalculateCronbachValue.class.getResource("ComputeGraph.R").getPath()).getAbsolutePath();
	//private static final String POST_METRIC_FILE =  "/usr/local/OsceManager/ComputeCronbach.R";
	
	/* wrapper object that allows to invoke R scripts */
	private WrapR r;
	
	/* csv file that contains the collected information for each student */
	private File csvFile;
	
	/* lower limit of the impression range */
	private int rangeLow;
	
	/* upper limit of the impression range */
	private int rangeHigh;
	
	/* flag that indicates whether corrections on data should be performed automatically or not */
	private boolean autoCorrect;
	
	private static Logger log = Logger.getLogger(CalculateCronbachValue.class);
	
	private Map<String, String> cronbachValueMap = new HashMap<String, String>();
	
	private Map<String, String> meanMap = new HashMap<String, String>();
	
	private Map<String, String> sdMap = new HashMap<String, String>();
	
	private Map<String, String> examinerResultMap = new HashMap<String, String>();
	
	private String missingItem;
	
	public void countValue(String fileName, Set<Long> missingItemId)
	{
		File file = new File(fileName);
		
		if(file.exists())
		{	
			log.info("POST_METRIC_FILE PATH : " + POST_METRIC_FILE);
			
			missingItem = missingItemId.isEmpty() ? "0" : ("Q" + StringUtils.join(missingItemId,",Q"));
			
			log.info("Missing Item Str : " + missingItem);
			
			csvFile = file.getAbsoluteFile();
			rangeLow = 0;
			rangeHigh = 10;
			autoCorrect = true;
			
			execute(POST_METRIC_FILE);
			
			List<String> response = r.getResponse();
			
			log.info("RESPONSE SIZE : " + response.size());
			
			if(response.size() > 0)
			{
				cronbachValueMap.put("overall", response.get(0));
				meanMap.put("overall", response.get(1));
				sdMap.put("overall", response.get(2));
				
				for (int i=3; i<response.size(); i+=4)
				{
					cronbachValueMap.put(response.get(i).substring(1), response.get(i+1));
					meanMap.put(response.get(i).substring(1), response.get(i+2));
					sdMap.put(response.get(i).substring(1), response.get(i+3));
				}				
			}
			
			csvFile.delete();
		}
	}
	
	public void execute(String rScript) {
		try{
			r = new WrapR(rScript);
			r.addParam("-f");
			r.addParam(csvFile.getAbsolutePath());
			r.addParam("-m");
			r.addParam(missingItem);
			r.addParam("-r");
			r.addParam(rangeLow + ".." + rangeHigh);
			r.execute();
		}
		catch(Exception e)
		{
			log.error("Error in executing R Script", e);
		}
		
	}
	
	public void executeExaminerResult(String rScript, String passMarkStr) {
		try{
			r = new WrapR(rScript);
			r.addParam("-f");
			r.addParam(csvFile.getAbsolutePath());
			r.addParam("-m");
			r.addParam(missingItem);
			r.addParam("-p");
			r.addParam(passMarkStr);
			r.addParam("-r");
			r.addParam(rangeLow + ".." + rangeHigh);
			r.execute();
		}
		catch(Exception e)
		{
			log.error("Error in executing R Script", e);
		}
		
	}

	public Map<String, String> calculateResult(String fileName, Long oscePostId, String postPassingStr)
	{
		File file = new File(fileName);
		
		if(file.exists())
		{	
			log.info("POST_METRIC_FILE PATH : " + EXAMINER_RESULT_FILE);
			
			String missingResponse = ItemAnalysis.findDeactivatedItemByOscePost(oscePostId);
			
			missingItem = (missingResponse == null || missingResponse.isEmpty()) ? "0" : missingResponse;
			
			log.info("Missing Item Str : " + missingItem);
			
			csvFile = file.getAbsoluteFile();
			rangeLow = 0;
			rangeHigh = 10;
			autoCorrect = true;
			
			executeExaminerResult(EXAMINER_RESULT_FILE, postPassingStr);
			
			List<String> response = r.getResponse();
			
			log.info("RESPONSE SIZE : " + response.size());
			
			if(response.size() > 0)
			{
				examinerResultMap.put("passCount", response.get(0));
				examinerResultMap.put("failCount", response.get(1));
				examinerResultMap.put("passMark", response.get(2));
				examinerResultMap.put("min", response.get(3));
				examinerResultMap.put("max", response.get(4));
				examinerResultMap.put("mean", response.get(5));
				examinerResultMap.put("sd", response.get(6));
			}
			
			csvFile.delete();
			
		}
		
		return examinerResultMap;
	}
	
	public Map<String, String> calculateOscePostResult(String fileName, Long oscePostId)
	{
		File file = new File(fileName);
		
		if(file.exists())
		{	
			log.info("POST_METRIC_FILE PATH : " + POST_RESULT_FILE);
			
			String missingResponse = ItemAnalysis.findDeactivatedItemByOscePost(oscePostId);
			
			missingItem = (missingResponse == null || missingResponse.isEmpty()) ? "0" : missingResponse;
			
			log.info("Missing Item Str : " + missingItem);
			
			csvFile = file.getAbsoluteFile();
			rangeLow = 0;
			rangeHigh = 10;
			autoCorrect = true;
			
			execute(POST_RESULT_FILE);
			
			List<String> response = r.getResponse();
			
			log.info("RESPONSE SIZE : " + response.size());
			
			if(response.size() > 0)
			{
				examinerResultMap.put("passCount", response.get(0));
				examinerResultMap.put("failCount", response.get(1));
				examinerResultMap.put("passMark", response.get(2));
				examinerResultMap.put("min", response.get(3));
				examinerResultMap.put("max", response.get(4));
				examinerResultMap.put("mean", response.get(5));
				examinerResultMap.put("sd", response.get(6));
			}
			
			csvFile.delete();
			
		}
		
		return examinerResultMap;
	}
	
	public void calculatePassFail(String fileName, String missingStr)
	{
		File file = new File(fileName);
		
		if(file.exists())
		{	
			log.info("POST_METRIC_FILE PATH : " + RESULT_FILE);
			
			String missingResponse = "0";				
			
			if (missingStr != null && (!missingStr.isEmpty()))
				missingResponse = missingStr;
			
			missingItem = missingResponse;
			
			log.info("Missing Item Str : " + missingItem);
			
			csvFile = file.getAbsoluteFile();
			rangeLow = 0;
			rangeHigh = 10;
			autoCorrect = true;
			
			execute(RESULT_FILE);
			
			List<String> response = r.getResponse();
			
			if (response.size() > 0)
			{
				log.info("Response : " + response.get(0));
			}
		}
	}
	
	public void createGraph(String fileName, String missingStr, Long min, Long max)
	{
		File file = new File(fileName);
		
		if(file.exists())
		{	
			log.info("POST_METRIC_FILE PATH : " + POST_GRAPH_FILE);
			
			String missingResponse = "0";				
			
			if (missingStr != null && (!missingStr.isEmpty()))
				missingResponse = missingStr;
			
			missingItem = missingResponse;
			
			log.info("Missing Item Str : " + missingItem);
			
			csvFile = file.getAbsoluteFile();
			rangeLow = min.intValue();
			rangeHigh = max.intValue();
			autoCorrect = true;
			
			execute(POST_GRAPH_FILE);
			
			log.info("~~~Executed Successfully");
			
			List<String> response = r.getResponse();
			
			if (response.size() > 0)
			{
				log.info("Response : " + response.get(0));
			}
			//csvFile.delete();
		}
	}
	
	public Map<String, String> getCronbachValueMap() {
		return cronbachValueMap;
	}

	public void setCronbachValueMap(Map<String, String> cronbachValueMap) {
		this.cronbachValueMap = cronbachValueMap;
	}

	public Map<String, String> getMeanMap() {
		return meanMap;
	}

	public void setMeanMap(Map<String, String> meanMap) {
		this.meanMap = meanMap;
	}

	public Map<String, String> getSdMap() {
		return sdMap;
	}

	public void setSdMap(Map<String, String> sdMap) {
		this.sdMap = sdMap;
	}
	
	

}
