package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RotationRefreshEvent extends GwtEvent<RotationRefreshHandler> {
	
	private static final Type TYPE = new Type<RotationRefreshEvent>();
	
	private String previousSequenceRotation;
	private String nextSequenceRotation;
	private String currentSequenceId;
	private String nextSequenceId;
	
	private String currentDayId;
	private String previousDayId;
	
	
	public String getPreviousDayId() {
		return previousDayId;
	}

	public void setPreviousDayId(String previousDayId) {
		this.previousDayId = previousDayId;
	}

	public String getCurrentDayId() {
		return currentDayId;
	}

	public void setCurrentDayId(String currentDayId) {
		this.currentDayId = currentDayId;
	}

	public String getCurrentSequenceId() {
		return currentSequenceId;
	}

	public void setCurrentSequenceId(String currentSequenceId) {
		this.currentSequenceId = currentSequenceId;
	}

	public String getNextSequenceId() {
		return nextSequenceId;
	}

	public void setNextSequenceId(String nextSequenceId) {
		this.nextSequenceId = nextSequenceId;
	}


	
	public String getPreviousSequenceRotation() {
		return previousSequenceRotation;
	}

	public void setPreviousSequenceRotation(String previousSequenceRotation) {
		this.previousSequenceRotation = previousSequenceRotation;
	}

	public String getNextSequenceRotation() {
		return nextSequenceRotation;
	}

	public void setNextSequenceRotation(String nextSequenceRotation) {
		this.nextSequenceRotation = nextSequenceRotation;
	}

	
	
	public RotationRefreshEvent(String previousSequenceRotation, String nextSequenceRotation, String currnetSequenceId, String nextSequenceId)
	{
		this.previousSequenceRotation=previousSequenceRotation;
		this.nextSequenceRotation=nextSequenceRotation;		
		this.currentSequenceId=currnetSequenceId;
		this.nextSequenceId=nextSequenceId;
	}
	
	public RotationRefreshEvent(String currentDayId, String previousDayId)
	{
		this.currentDayId=currentDayId;
		this.previousDayId=previousDayId;
	}
	
	
	public static Type getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RotationRefreshHandler handler) {
		handler.onRotationChanged(this);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RotationRefreshHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static HandlerRegistration register(EventBus eventBus, RotationRefreshHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}
}
