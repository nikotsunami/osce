package ch.unibas.medizin.osce.server.util;

public class ServerUtils {
	
	public static String IntToLetter(int Int) {
	    if (Int<26){
	      return Character.toString((char)(Int+65));
	    } else {
	      if (Int%26==0) {
	        return IntToLetter((Int/25)-1)+IntToLetter(Int%26);
	      } else {
	        return IntToLetter((Int/26)-1) +IntToLetter(Int%26);
	      }
	    }
	  }

}
