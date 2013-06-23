package ch.unibas.medizin.osce.server.upload;

import java.util.List;

public class SPTimeSlotRow {

	private List<TimeSlotDetail> spSlotsPerSeq;

	public List<TimeSlotDetail> getSpSlotsPerSeq() {
		return spSlotsPerSeq;
	}

	public void setSpSlotsPerSeq(List<TimeSlotDetail> spSlotsPerSeq) {
		this.spSlotsPerSeq = spSlotsPerSeq;
	}
}
