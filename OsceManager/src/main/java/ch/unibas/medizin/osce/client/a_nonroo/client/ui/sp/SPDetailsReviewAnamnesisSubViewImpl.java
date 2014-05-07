package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;


import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author manishp
 *
 */
public class SPDetailsReviewAnamnesisSubViewImpl extends Composite implements  SPDetailsReviewAnamnesisSubView {

	private static SPDetailsReviewAnamnesisViewImplUiBinder uiBinder = GWT
			.create(SPDetailsReviewAnamnesisViewImplUiBinder.class);

	interface SPDetailsReviewAnamnesisViewImplUiBinder extends UiBinder<Widget, SPDetailsReviewAnamnesisSubViewImpl> {
		
	}

	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	public ImageResource icon1 = uiIcons.triangle1West(); 
	public ImageResource icon2=  uiIcons.triangle1East();
	Unit u=Unit.PX;
	
	
	@UiField
	public HorizontalPanel horizontalanamnesisPanel;

	@UiField(provided=true)
	public ScrolledTabLayoutPanel anamnesisTabs=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private SPDetailsReviewAnamnesisSubViewImpl spDetailsReviewAnamnesisSubViewImpl;

	public SPDetailsReviewAnamnesisSubViewImpl()
	{
		
		spDetailsReviewAnamnesisSubViewImpl=this;
		initWidget(uiBinder.createAndBindUi(this));
		
		horizontalanamnesisPanel.addStyleName("horizontalPanelStyle");
		horizontalanamnesisPanel.add(anamnesisTabs);
		
	}

	

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	public SPDetailsReviewAnamnesisTableSubView addAnamnesisCheckTitle(AnamnesisCheckTitleProxy title) {
		Log.info("initializing anamnesis check title table and putting it on tab");
		String titleText = "";
		
		if (title.getText() != null) {
			titleText = title.getText();
		}
		
		SPDetailsReviewAnamnesisTableSubView tableSubView = new SPDetailsReviewAnamnesisTableSubViewImpl();
		
		anamnesisTabs.add((Widget) tableSubView, titleText);
		
		//anamnesisTabs.getTabWidget(0).getParent().addStyleName("chnagedTabStyle");
		
		/*if (anamnesisTabs.getWidgetCount() == 1) {
			anamnesisTabs.addSelectionHandler(new SelectionHandler<Integer>() {
				@Override
				public void onSelection(SelectionEvent<Integer> event) {
					//delegate.performAnamnesisSearch();
					//delegate.storeDisplaySettings();
				}
			});
		}*/
		return tableSubView;
	}



	public ScrolledTabLayoutPanel getAnamnesisTabs() {
		return anamnesisTabs;
	}
	
}
