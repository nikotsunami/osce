package ch.unibas.medizin.osce.shared;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.Semester;

public  class MapOsceRole {


	Osce osce;
	
	Semester semester;
	
	String standandarizeRoleId;	
	

	
	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public Osce getOsce() {
		return osce;
	}

	public void setOsce(Osce osce) {
		this.osce = osce;
	}

	public String getStandandarizeRoleId() {
		return standandarizeRoleId;
	}

	public void setStandandarizeRoleId(String standandarizeRoleId) {
		this.standandarizeRoleId = standandarizeRoleId;
	}
	
	
}
