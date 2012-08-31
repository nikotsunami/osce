package ch.unibas.medizin.osce.shared;


public enum OsceSequences {

	// Module 5 bug Report Change
	
	OSCE_SEQUENCES_A, OSCE_SEQUENCES_B, OSCE_SEQUENCES_C, OSCE_SEQUENCES_D, OSCE_SEQUENCES_E;
	
	static OsceSequences osceSequences;
	
	public static OsceSequences getSequenceByString(char sequenceString)
	{
		switch(sequenceString)
		{
			case  'A' :osceSequences = OSCE_SEQUENCES_A;
			break;
			case  'B' :osceSequences = OSCE_SEQUENCES_B;
			break;
			case  'C' :osceSequences = OSCE_SEQUENCES_C;
			break;
			case  'D' :osceSequences = OSCE_SEQUENCES_D;
			break;
			case  'E' :osceSequences = OSCE_SEQUENCES_E;
			break;		
			default : osceSequences = OSCE_SEQUENCES_A;
		}
		return osceSequences;
	}
	
	static String osceSequenceValue;
	public static String getOsceSequenceValue(OsceSequences osceSequences)
	{
		
		if(osceSequences.equals(OSCE_SEQUENCES_A))
		{
			osceSequenceValue = "A";	
		}
		else if(osceSequences.equals(OSCE_SEQUENCES_B))
		{
			osceSequenceValue = "B";	
		}
		else if(osceSequences.equals(OSCE_SEQUENCES_C))
		{
			osceSequenceValue = "C";	
		}
		else if(osceSequences.equals(OSCE_SEQUENCES_D))
		{
			osceSequenceValue = "D";	
		}
		else if(osceSequences.equals(OSCE_SEQUENCES_E))
		{
			osceSequenceValue = "E";	
		}
		else
		{
			osceSequenceValue = "A";
		}
	
		return osceSequenceValue;
	}
	
	// E Module 5 bug Report Change
    
    public static OsceSequences getConstByIndex(int index) {
    	for (OsceSequences s : OsceSequences.values()) {
            if (s.ordinal() == index) {
                return s;
            }
        }
        return null;
    }
    
     
}
