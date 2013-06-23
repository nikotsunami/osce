package ch.unibas.medizin.osce.server.upload;

import java.util.List;

import ch.unibas.medizin.osce.domain.OsceDay;

public class OsceDayRow {

	List<OsceDay> osceDayList;

	public List<OsceDay> getOsceDayList() {
		return osceDayList;
	}

	public void setOsceDayList(List<OsceDay> osceDayList) {
		this.osceDayList = osceDayList;
	}
}
