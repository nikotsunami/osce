package ch.unibas.medizin.osce.server.upload;

import ch.unibas.medizin.osce.domain.OsceSequence;

public class OsceSequenceDetail {

	private OsceSequence osceSeq;
	
	private int ColSpan;

	public OsceSequence getOsceSeq() {
		return osceSeq;
	}

	public void setOsceSeq(OsceSequence osceSeq) {
		this.osceSeq = osceSeq;
	}

	public int getColSpan() {
		return ColSpan;
	}

	public void setColSpan(int colSpan) {
		ColSpan = colSpan;
	}
}
