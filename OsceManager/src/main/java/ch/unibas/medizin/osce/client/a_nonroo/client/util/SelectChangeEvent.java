package ch.unibas.medizin.osce.client.a_nonroo.client.util;



import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;

public class SelectChangeEvent extends GwtEvent<SelectChangeHandler> {
	private static final Type TYPE = new Type<SelectChangeHandler>();

	private String selectedText;
	private SemesterProxy semesterProxy;

	/*public SelectChangeEvent(String selectedText) {
		this.selectedText = selectedText;
	}*/
	
	public SelectChangeEvent(SemesterProxy semesterProxy) 
	{
		Log.info("~Call Select Change Event for Proxy: " + semesterProxy.getCalYear());
		this.semesterProxy = semesterProxy;
	}

	public static Type getType() {
		return TYPE;
	}

	/** @returns The item added to the model */
	public String getSelectedText() {
		return selectedText;
	}
	
	public SemesterProxy getSemesterProxy() {
		return semesterProxy;
	}

	@Override
	protected void dispatch(SelectChangeHandler handler) {
		handler.onSelectionChange(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type getAssociatedType() {
		return TYPE;
	}
}