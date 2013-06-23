package ch.unibas.medizin.osce.server.upload;

import java.util.Date;

import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;

public class SPSlot {

	private Date timeStart;
	
	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
		
	}

	private Date timeEnd;
	
	
	
	private Osce osce;

	public Osce getOsce() {
		return osce;
	}

	public void setOsce(Osce osce) {
		this.osce = osce;
	}
	
	
}
