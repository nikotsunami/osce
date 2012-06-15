package ch.unibas.medizin.osce.shared;

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


}
