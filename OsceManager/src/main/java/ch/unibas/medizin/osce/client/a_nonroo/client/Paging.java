package ch.unibas.medizin.osce.client.a_nonroo.client;

import com.allen_sauer.gwt.log.client.Log;


public class Paging {
	
	public static int getLastPageStart(int pageSize, int total){
		
		int start = 0;
		try {
			if(total > pageSize ){
				
//				if(total % pageSize == 0)
					start = total - pageSize;
//				else
//					start = (total / pageSize) + pageSize - 1;
				
				
			}
			
			Log.info("start === = "+start);
			Log.info("total === = "+total);
			Log.info("pSize === = "+pageSize);
			
			return start;
			
		} catch (Exception e) {	
			Log.error("ERROR :"+e.getMessage());
			return -1;
		}
		
	}
}