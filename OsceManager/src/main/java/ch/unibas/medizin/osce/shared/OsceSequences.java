package ch.unibas.medizin.osce.shared;


public enum OsceSequences {

    A, B, C, D, E;
    
    public static OsceSequences getConstByIndex(int index) {
    	for (OsceSequences s : OsceSequences.values()) {
            if (s.ordinal() == index) {
                return s;
            }
        }
        return null;
    }
}
