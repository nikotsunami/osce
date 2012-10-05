/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.util.RotationRefreshEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RotationRefreshHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class SequenceOsceSubViewImpl extends Composite implements SequenceOsceSubView,RotationRefreshHandler {

	private static SequenceOsceSubViewUiBinder uiBinder = GWT
			.create(SequenceOsceSubViewUiBinder.class);

	interface SequenceOsceSubViewUiBinder extends UiBinder<Widget, SequenceOsceSubViewImpl> {
	}

	private Delegate delegate;
	
	private SequenceOsceSubViewImpl sequenceOsceSubViewImpl;

	protected Set<String> paths = new HashSet<String>();
	
	private OsceProxy proxy;
	
	public OsceSequenceProxy osceSequenceProxy;
	public OsceDayProxy osceDayProxy;
	

	public OsceProxy getProxy() {
		return proxy;
	}

	public void setProxy(OsceProxy proxy) {
		this.proxy = proxy;
	}

	private Presenter presenter;
	
	
	@UiField
	public Label nameOfSequence;
	
	// Module 5 bug Report Change
	
	/*@UiField
	public TextBox chaneNameOfSequence;
	@UiField(provided = true)
	public FocusableValueListBox<OsceSequences> chaneNameOfSequence = new FocusableValueListBox<OsceSequences>(new EnumRenderer<OsceSequences>());	
	// E Module 5 bug Report Change

	@UiField
	public Button ok;
	*/
	@UiField
	public IconButton edit;
	
	@UiField
	public IconButton spliteSequence;
	
	//Module 5 Bug Report Solution
	/*@UiField
	public TextBox sequenceRotation;*/
	@UiField
	Label sequenceRotation;
	//E Module 5 Bug Report Solution
	
	
	@UiHandler("spliteSequence")
	public void saveOsceDataClicked(ClickEvent event){
		delegate.saveOsceDataSplit(sequenceOsceSubViewImpl);
	}
	
	@UiHandler("edit")
	public void changeNameClicked(ClickEvent event)
        {
	// Module 5 bug Report Change
		//chaneNameOfSequence.setValue(nameOfSequence.getText());
		Log.info("Click Edit Button.");		
		//chaneNameOfSequence.setAcceptableValues(Arrays.asList(OsceSequences.values()));
		//chaneNameOfSequence.setValue(OsceSequences.getSequenceByString(nameOfSequence.getText().charAt(0)));
		delegate.editOsceSequence(event,sequenceOsceSubViewImpl);
		
		
		//ok.setVisible(true);
		//chaneNameOfSequence.setVisible(true);
		// E Module 5 bug Report Change
		//	delegate.saveOsceData(proxy);
		
	}
		// Module 5 bug Report Change
/*	@UiHandler("ok")
	public void okClicked(ClickEvent event)
	{
		
		// Module 5 bug Report Change
		Log.info("Ok Clicked");
		if((((chaneNameOfSequence.getListBox().getItemText(chaneNameOfSequence.getListBox().getSelectedIndex())).trim()).compareToIgnoreCase(""))==0)
		{
			MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
			dialog.showConfirmationDialog("Please Select atleast one Sequence");
			return;
		}
		else
		{
		// E Module 5 bug Report Change
		delegate.saveSequenceLabel(sequenceOsceSubViewImpl);
		ok.setVisible(false);
		chaneNameOfSequence.setVisible(false);
		}
	//	nameOfSequence.setText(chaneNameOfSequence.getText());
		
	}*/
	
	// E Module 5 bug Report Change	
	
	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	
	// Highlight onViolation
	public Map<String, Widget> osceSequenceMap;
			// E Highlight onViolation
	public SequenceOsceSubViewImpl() {
		this(null);
	}

	
	public SequenceOsceSubViewImpl(OsceSequenceProxy sequence) {
		initWidget(uiBinder.createAndBindUi(this));
		sequenceOsceSubViewImpl=this;
		init();
		OsceConstants constants = GWT.create(OsceConstants.class);
//		spliteSequence.setText(constants.splitSequence());
		spliteSequence.setTitle(constants.splitSequence());
		// Module 5 bug Report Change
		/*chaneNameOfSequence.setVisible(false);
		ok.setVisible(false);
		ok.setText("ok");*/
		// E Module 5 bug Report Change
		
		if (sequence == null) {
			// Highlight onViolation
			osceSequenceMap=new HashMap<String, Widget>();
			osceSequenceMap.put("label", nameOfSequence);
			osceSequenceMap.put("numberRotation", nameOfSequence);
			osceSequenceMap.put("osceDay", nameOfSequence);
			// E Highlight onViolation
		} else {
			osceSequenceProxy=sequence;
		}
	}

	
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// TODO implement this!
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	//Module 5 Bug Report Solution
	@Override
	public Label getSequenceRotationLable() {
		return this.sequenceRotation;
	}
	//E Module 5 Bug Report Solution
	
	@Override
	public void onRotationChanged(RotationRefreshEvent event) 
	{
		Log.info("Rotation Change");
		
		/*Log.info("Current Sequence Rotaions: " + event.getPreviousSequenceRotation());
		Log.info("Next Sequence Rotaions: " + event.getNextSequenceRotation());
		
		Log.info("~Osce Sequence Proxy: " + osceSequenceProxy.getId());
		Log.info("~Current Sequence Id: " + event.getCurrentSequenceId());
		Log.info("=Next Sequence Id: " + event.getNextSequenceId());		
		Log.info("=Osce Sequence Proxy: " + osceSequenceProxy.getId());*/
		
		if(event.getCurrentSequenceId()!=null && event.getNextSequenceId()!=null)
		{
			if(osceSequenceProxy.getId().equals(Long.valueOf(event.getCurrentSequenceId())))
			{
				Log.info("Current Sequence Found");
				Log.info("Current Sequence Id: "+ event.getCurrentSequenceId());
				this.sequenceRotation.setText(event.getPreviousSequenceRotation());		
				this.sequenceRotation.setTitle(event.getPreviousSequenceRotation());
			}
			else if(osceSequenceProxy.getId().equals(Long.valueOf(event.getNextSequenceId())))
			{
				Log.info("Next Sequence Found");
				Log.info("Next Sequence Id: "+ event.getNextSequenceId());
				this.sequenceRotation.setText(event.getNextSequenceRotation());
				this.sequenceRotation.setTitle(event.getNextSequenceRotation());
			}	
		}
		
		
	}
	
	
}
