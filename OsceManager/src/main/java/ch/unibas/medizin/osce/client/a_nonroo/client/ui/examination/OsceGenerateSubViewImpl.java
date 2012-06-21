package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OsceGenerateSubViewImpl extends Composite implements OsceGenerateSubView{
	
	private Delegate delegate;
	
	private static OsceGenerateSubViewImplUiBinder uiBinder = GWT
			.create(OsceGenerateSubViewImplUiBinder.class);

	interface OsceGenerateSubViewImplUiBinder extends UiBinder<Widget, OsceGenerateSubViewImpl> {
	}
	
	@UiField
	VerticalPanel accordianVP;
	
	
	public VerticalPanel getAccordianVP() {
		return accordianVP;
	}

	public void setAccordianVP(VerticalPanel accordianVP) {
		this.accordianVP = accordianVP;
	}

	AccordianPanelViewImpl accordian;
	
	@Override
	public void setDelegate(Delegate delegate) {
		// TODO Auto-generated method stub
		
	}
	
	public OsceGenerateSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		/*accordian=new AccordianPanelViewImpl(true);
		accordian.add(new HeaderViewImpl(), new ContentViewImpl());//parcor one
		accordian.add(new HeaderViewImpl(), new ContentViewImpl());//parcor two
		accordianVP.insert(accordian, 0);*/
	}
}
