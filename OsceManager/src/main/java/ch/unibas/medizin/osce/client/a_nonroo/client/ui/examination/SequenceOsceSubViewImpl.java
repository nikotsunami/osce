/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.scaffold.ui.ShortBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class SequenceOsceSubViewImpl extends Composite implements SequenceOsceSubView {

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
	
	@UiField
	public TextBox chaneNameOfSequence;

	@UiField
	public Button ok;
	
	@UiField
	public IconButton edit;
	
	@UiField
	public Button spliteSequence;
	
	@UiField
	public TextBox sequenceRotation;
	
	
	@UiHandler("spliteSequence")
	public void saveOsceDataClicked(ClickEvent event){
		delegate.saveOsceDataSplit(sequenceOsceSubViewImpl);
	}
	
	@UiHandler("edit")
	public void changeNameClicked(ClickEvent event){
	//	delegate.saveOsceData(proxy);
		ok.setVisible(true);
		chaneNameOfSequence.setVisible(true);
		chaneNameOfSequence.setText(nameOfSequence.getText());
	}
	
	@UiHandler("ok")
	public void okClicked(ClickEvent event){
		delegate.saveSequenceLabel(sequenceOsceSubViewImpl);
		ok.setVisible(false);
		chaneNameOfSequence.setVisible(false);
	//	nameOfSequence.setText(chaneNameOfSequence.getText());
		
	}
	
	
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
		initWidget(uiBinder.createAndBindUi(this));
		sequenceOsceSubViewImpl=this;
		init();
		spliteSequence.setText("Splite");
		chaneNameOfSequence.setVisible(false);
		ok.setVisible(false);
		ok.setText("ok");
		
		// Highlight onViolation
		osceSequenceMap=new HashMap<String, Widget>();
		osceSequenceMap.put("label", nameOfSequence);
		osceSequenceMap.put("numberRotation", nameOfSequence);
		osceSequenceMap.put("osceDay", nameOfSequence);
		
		
				// E Highlight onViolation
	}

	
	public SequenceOsceSubViewImpl(OsceSequenceProxy sequence) {
		initWidget(uiBinder.createAndBindUi(this));
		sequenceOsceSubViewImpl=this;
		init();
		spliteSequence.setText("Splite");
		chaneNameOfSequence.setVisible(false);
		ok.setVisible(false);
		ok.setText("ok");
		osceSequenceProxy=sequence;
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
	
	
	
	
}
