package ch.unibas.medizin.osce.server;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gwt.requestfactory.server.RequestFactoryServlet;



public class CalculateCronbachValue {
	
	/* R script that contains necessary routines to calculate statistics */
	private static final String POST_METRIC_FILE =  new File(CalculateCronbachValue.class.getResource("ComputeCronbach.R").getPath()).getAbsolutePath();
	
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
	
	public Map<String, String> countValue(String fileName)
	{
		File file = new File(fileName);
		
		if(file.exists())
		{	
			System.out.println("POST_METRIC_FILE PATH : " + POST_METRIC_FILE);
			
			csvFile = file.getAbsoluteFile();
			rangeLow = 1;
			rangeHigh = 10;
			autoCorrect = true;
			
			execute();
			
			List<String> response = r.getResponse();
			
			if(response.size() > 0)
			{
				cronbachValueMap.put("overall", response.get(0));
				
				for (int i=1; i<response.size()-1; i+=2)
				{
					cronbachValueMap.put(response.get(i).substring(1), response.get(i+1));
				}
				
				csvFile.delete();
				return cronbachValueMap;
			}
			
			
		}
		
		return cronbachValueMap;
	}
	
	public void execute() {
		try{
			r = new WrapR(POST_METRIC_FILE);
			r.addParam("-f");
			r.addParam(csvFile.getAbsolutePath());
			r.addParam("-r");
			r.addParam(rangeLow + ".." + rangeHigh);
			r.execute();
		}
		catch(Exception e)
		{
			log.error("Error in executing R Script", e);
		}
		
	}

}
