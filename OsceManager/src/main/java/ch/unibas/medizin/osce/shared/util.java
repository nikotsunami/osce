package ch.unibas.medizin.osce.shared;

import com.allen_sauer.gwt.log.client.Log;

public class util {
	
	public static Long getLong(String str){

		Long longValue = new Long("0");

		try{

		longValue = Long.parseLong(str);


		}catch(NumberFormatException e){

		}

		return longValue;

		}

		 
	public static Integer getInteger(String str){

		Integer intValue = new Integer("0");

		try{

		intValue = Integer.parseInt(str);


		}catch(NumberFormatException e){

		}

		return intValue;

		}
		 
		public static String getEmptyIfNull(Object str)

		{

		if (str == null)

		return "";

		else

		return str.toString();

		}
		public static Integer checkInteger(Integer integer){
			Integer newInteger = new Integer(0);
			try{
				if(integer==null){
					return newInteger;
				}
			}catch(NumberFormatException e){
				Log.info("Number Formater Exception at checkInteger");
				e.printStackTrace();
			}
				return integer;
			
		}
		public static Long checkLongr(Long longValue){
			Long newLong = new Long(0);
			try{
				if(longValue==null){
					return newLong;
				}
			}catch(NumberFormatException e){
				Log.info("Number Formater Exception at checkInteger");
				e.printStackTrace();
			}
				return longValue;
			
		}
		public static Short checkShort(Short shortValue){
			Short newShort =0;
			try{
				if(shortValue==null){
					return newShort;
				}
			}catch(NumberFormatException e){
				Log.info("Number Formater Exception at checkInteger");
				e.printStackTrace();
			}
				return shortValue;
				
			
		}


}
