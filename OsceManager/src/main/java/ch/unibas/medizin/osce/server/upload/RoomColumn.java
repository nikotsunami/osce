package ch.unibas.medizin.osce.server.upload;

import java.util.List;

public class RoomColumn {

	private List<RoomDetail> roomDetailList;

	public List<RoomDetail> getRoomDetailList() {
		return roomDetailList;
	}

	public void setRoomDetailList(List<RoomDetail> roomDetailList) {
		this.roomDetailList = roomDetailList;
	}
}
