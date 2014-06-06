package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.TrainingBlockProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BlockViewImpl extends Composite implements BlockView {

	private static BlockViewImplUiBinder uiBinder = GWT.create(BlockViewImplUiBinder.class);
	
	interface BlockViewImplUiBinder extends UiBinder<Widget, BlockViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
	
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
		
	private Delegate delegate;
	
	private boolean isSplitted;
	
	@UiField
	IconButton splitBlocksButton;
	
	@UiField
	IconButton joinBlockButton;
	
	@UiField
	VerticalPanel buttonPanel;

	@UiField
	VerticalPanel labelPanel;
	
	@UiField
	Label blockLabel;
	
	private Date blockStartDate;
	
	private TrainingBlockProxy trainingBlockProxy;
	
	public BlockViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler(value={"splitBlocksButton","joinBlockButton"})
	public void mouseDownEventOfAllButtons(MouseDownEvent event)
	{
		event.preventDefault();
		event.stopPropagation();
	}
	
	@UiHandler("splitBlocksButton")
	public void proposeTrainingDayButtonClicked(ClickEvent event){
		Log.info("split Block Button Clicked");
		delegate.splitBlockClicked(blockStartDate,trainingBlockProxy);
	}
	
	@UiHandler("joinBlockButton")
	public void proposeOSceDayButtonClicked(ClickEvent event){
		Log.info("join block Button Clicked");
		delegate.joinBlockButtonClicked(blockStartDate,trainingBlockProxy);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	
	@Override
	public Date getBlockStartDate() {
		return blockStartDate;
	}

	@Override
	public void setBlockStartDate(Date blockStartDate) {
		this.blockStartDate = blockStartDate;
	}

	@Override
	public void setTrainingBlockProxy(TrainingBlockProxy blockProxy) {
		this.trainingBlockProxy=blockProxy;
		
	}

	public TrainingBlockProxy getTrainingBlockProxy() {
		return trainingBlockProxy;
	}

	@Override
	public void setIsSplitted(boolean isSplitted) {
		this.isSplitted=isSplitted;
		
	}
	
	@Override
	public boolean getIsSplitted() {
		return isSplitted;
	}

	@Override
	public void clearView() {
		buttonPanel.clear();
	}

	@Override
	public Label getBlockLabel() {
		return blockLabel;
	}

	public void setBlockLabel(Label blockLabel) {
		this.blockLabel = blockLabel;
	}

	@Override
	public IconButton getSplitBlockButton() {
		return splitBlocksButton;
		
	}

	@Override
	public IconButton getJoinBlockButton() {
		return joinBlockButton;
	}

	

}
