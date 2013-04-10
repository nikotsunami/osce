package ch.unibas.medizin.osce.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Simple Wrapper to call R script out of java and parse the following console output.
 * 
 * @author Daniel Kohler (daniel.kohler@unibas.ch)
 *
 */
public class WrapR {
	
	private static Logger log = Logger.getLogger(WrapR.class);
	
	/* program with which R scripts are run (use absolute path if script-path is not contained in PATH variable) */
	//private String rInterpreter = "C:/Program Files/R/R-2.15.2/bin/x64/Rscript";
	private String rInterpreter = "Rscript";
	
	/* script to be called */
	private String rScript;
	
	/* pattern according to which R generates output (there is always a prefix like "[1]" */
	private String regExpResponse = "\\[\\d+\\]";
	
	/* internal parameters (cannot be changed from the outside) - contains, for example, the */
	private List<String> params;
	
	/* ProcessBuilder stuff from java - in charge for setting up and calling an external script */
	private ProcessBuilder pb;
	private Process p;
	
	/* list of responses from R script (one line per entry) */
	private List<String> response;
	
	/**
	 * Constructor - sets the wrapper to a specific R script that is invoked with execute()
	 * @param script R script that should be invoked
	 */
	public WrapR(String script) {
		params = new ArrayList<String>();
		rScript = script;
	}
	
	/**
	 * Execute the specified R script (sets up and calls the external concatenated command
	 * @return success (true) or failure (false)
	 */
	public boolean execute() {
		try {
			List<String> paramsAll = new ArrayList<String>();
			// add interpreter and script at the beginning
			paramsAll.add(rInterpreter);
			paramsAll.add(rScript);
			
			// add user parameters
			paramsAll.addAll(params);
			
			for (String temp : paramsAll)
				log.info("TEMP : " + temp);
			
			pb = new ProcessBuilder(paramsAll);
			p = pb.start();
			
			return true;
		} catch (Exception e) {
			log.error("ERROR IN R : " , e);
			return false;
		}
	}
	
	/**
	 * Concatenate parameters to single string (separated by space)
	 * @return concatenated parameters
	 */
	public String paramsAsString() {
		StringBuilder sb = new StringBuilder();

		for (String s : params) {
			sb.append(s).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * Add a string value that is passed to the R script
	 * @param param a string value to be added to parameter list
	 */
	public void addParam(String param) {
		params.add(param);
	}
	
	/**
	 * Add an integer value that is passed to the R script
	 * @param param an integer value to be added to parameter list
	 */
	public void addParam(int param) {
		params.add(new Integer(param).toString());
	}
	
	/**
	 * Add an double value that is passed to the R script
	 * @param param a double value to be added to parameter list
	 */
	public void addParam(double param) {
		params.add(new Double(param).toString());
	}
	
	/**
	 * Add a boolean value that is passed to the R script
	 * @param param a boolean value to be added to parameter list
	 */
	public void addParam(boolean param) {
		params.add(param == true ? "true" : "false");
	}
	
	/**
	 * Parse written console output according to regular expression pattern
	 * @return lines that the R script has output
	 * @throws IOException
	 */
	public List<String> getResponse() {
		if(response == null) {
			try {
				response = new ArrayList<String>();
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while((line = br.readLine()) != null) {
					String[] pOut = line.split(regExpResponse);
					if(pOut.length >= 2) {
						// trim and remove prefix and suffix "
						String content = pOut[1].trim();
						content = content.replaceAll("\"$", "").replaceAll("^\"", "");
						response.add(content);
					}
				}
				return response;
			} catch(IOException e) {
				System.err.println(e.getMessage());
				return null;
			}
		} else {
			return response;
		}
	}
	
	/**
	 * Get response as map in the format "key = value" (assumes that the output
	 * of the R script is in the appropriate format.
	 * @return map storing output in "key = value"
	 */
	public Map<String, String> getReponseAsMap() {
		if(response == null)
			getResponse();
		
		Map<String, String> responseMap = new HashMap<String, String>();

		for(String r : response) {
			String[] tmp = r.split("=");
			if(tmp.length >= 2)
				responseMap.put(tmp[0].trim(), tmp[1].trim());
		}
		
		return responseMap;
	}
	
	/**
	 * Print environment information for current process (e.g. PATH, JAVA_HOME, etc.)
	 */
	public void printEnv() {
		Map<String, String> env = pb.environment();
		for(String s : env.keySet()) {
			log.info(s + " = " + env.get(s));
		}
	}
}
