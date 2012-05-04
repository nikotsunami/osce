package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientFilterViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView.Presenter;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsViewImpl extends Composite implements  RoomMaterialsView{

	private static RoomMaterialsViewImplUiBinder uiBinder = GWT
			.create(RoomMaterialsViewImplUiBinder.class);

	interface RoomMaterialsViewImplUiBinder extends UiBinder<Widget, RoomMaterialsViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	@UiField
	public SimplePanel detailsPanel;
	@UiField
	HasClickHandlers showSubview;
	
	private Presenter presenter;
	
	public RoomMaterialsViewImpl() {
		// TODO Auto-generated constructor stub
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}
	
	public void init()
	{
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
	}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler ("showSubview")
	public void showSubviewClicked(ClickEvent event) {
		delegate.showSubviewClicked();
	}
}
