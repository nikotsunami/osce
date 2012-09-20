package ch.unibas.medizin.osce.shared;

public enum ColorPicker {
	color_1, color_2, color_3, color_4, color_5, color_6, color_7, color_8, color_9, color_10, color_11, color_12, color_13, color_14, color_15, color_16;
		
	public static ColorPicker getConstByIndex(int index) {
    	for (ColorPicker s : ColorPicker.values()) {
            if (s.ordinal() == index) {
                return s;
            }
        }
        return null;
    }
}
