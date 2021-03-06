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
		
		public static String getLabelString(String text,int length,String afterTrimSymbol)
		{
			//Log.info("Text String Size: " + text.length());
			if(text.length()>length)
			{
			//	Log.info("Text Length is greater than 8 Before Text is: " + text);
				text=text.substring(0, length).concat(afterTrimSymbol);	
			//	Log.info("Text Length is greater than 8 So Text is: " + text);
			}			
			return text;
			
		}
		
		/*
		 * text : The text to be printed.
		 * length : Len of the text to be shown after that  ... will be concatenated. 
		 */
		
		 public static String getFormatedString(String text,int length)
		{
			//Log.info("Text String Size: " + text.length());
			if(text.length()>length)
			{
			//	Log.info("Text Length is greater than 8 Before Text is: " + text);
				text=text.substring(0, length).concat("...");	
			//	Log.info("Text Length is greater than 8 So Text is: " + text);
			}			
			return text;
			
		}

}
