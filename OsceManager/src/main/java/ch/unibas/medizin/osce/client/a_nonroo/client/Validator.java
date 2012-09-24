package ch.unibas.medizin.osce.client.a_nonroo.client;


public class Validator {
	
	public static boolean isNotNull(String... strings){
		
		for(String string : strings){
			if(string == null || string.equals(""))
				return false;
		}
		return true;
	}
	
	public static boolean isNotNull(Object... objects){
		
		for(Object object : objects){
			if(object == null)
				return false;
		}
		return true;
	}
}