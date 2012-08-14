package ch.unibas.medizin.osce.shared;

public enum ColorPicker {
	blue,white,green,yellow,red;
	
	public static ColorPicker getConstByIndex(int index) {
    	for (ColorPicker s : ColorPicker.values()) {
            if (s.ordinal() == index) {
                return s;
            }
        }
        return null;
    }
}
