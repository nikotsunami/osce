package ch.unibas.medizin.osce.shared;

public enum OsceStatus {

	OSCE_NEW,OSCE_BLUEPRINT,OSCE_GENRATED,OSCE_FIXED,OSCE_CLOSED;

	private String OsceScreenStatus;
	public String getOsceStatus(OsceStatus status){
		
		switch(status){
		case  OSCE_NEW : OsceScreenStatus="Osce_New_Status";
		break;
		case OSCE_BLUEPRINT : OsceScreenStatus="Osce_BluePrint_Status";
		break;
		case OSCE_GENRATED : OsceScreenStatus="Osce_Genrated_Status";
		break;
		case OSCE_FIXED	: OsceScreenStatus="Osce_Fixed_Status";
		break;
		case OSCE_CLOSED : OsceScreenStatus="Osce_Closed_Status";
		break;
		default : OsceScreenStatus="Osce_New_Status";
		}
		return OsceScreenStatus;
	}

}
