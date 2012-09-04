package ch.unibas.medizin.osce.shared;

public enum TimeBell {
	TRUE(1), FALSE(0), NONE(-1);

	int value;

	TimeBell(int value) {
		this.value = value;
	}
}
