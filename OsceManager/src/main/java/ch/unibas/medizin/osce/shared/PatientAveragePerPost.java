package ch.unibas.medizin.osce.shared;

/**
 * Used For standardized Patient Average Per Post
 * 
 * 
 */
public enum PatientAveragePerPost {
	a1(1), a2(2), a3(3), a4(4), a5(5), a6(6), a7(7), a8(8);

	private int typeId;

	private PatientAveragePerPost(int typeId) {
		this.typeId = typeId;
	}

	public int getTypeId() {
		return typeId;
	}
}
