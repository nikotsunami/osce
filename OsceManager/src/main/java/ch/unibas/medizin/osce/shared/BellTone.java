package ch.unibas.medizin.osce.shared;

public enum BellTone {

	TONE_2(2, 2), TONE_4(4, 0), TONE_8(8, 1);

	int tone;
	int qwtTone;

	BellTone(int tone, int qwtTone) {
		this.tone = tone;
		this.qwtTone = qwtTone;
	}

	public int getTone() {
		return tone;
	}

	public int getQwtTone() {
		return qwtTone;
	}

}
