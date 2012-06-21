/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class AnamnesisCheckDetailsViewImpl extends Composite implements AnamnesisCheckDetailsView {

	private static AnamnesisCheckDetailsViewImplUiBinder uiBinder = GWT
			.create(AnamnesisCheckDetailsViewImplUiBinder.class);

	interface AnamnesisCheckDetailsViewImplUiBinder extends
	UiBinder<Widget, AnamnesisCheckDetailsViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField
	IconButton edit;

	@UiField
	IconButton delete;
	
	@UiField
	TabPanel anamnesisPanel;

	private Delegate delegate;

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
	public AnamnesisCheckDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		anamnesisPanel.selectTab(0);
		anamnesisPanel.getTabBar().setTabText(0, constants.anamnesisValues());
		TabPanelHelper.moveTabBarToBottom(anamnesisPanel);
		
		edit.setText(constants.edit());
		delete.setText(constants.delete());
		
		labelType.setInnerText(constants.type() + ":");
		labelText.setInnerText(constants.question() + ":");

	}
	
	@UiField
	SpanElement labelType;
	@UiField
	SpanElement labelValue;
	@UiField
	SpanElement labelText;
	
	@UiField
	SpanElement header;
	@UiField
	SpanElement type;
	@UiField
	VerticalPanel valuePanel;
	
	@UiField
	SpanElement text;
	
	@UiField
	SpanElement labelTitle;
	@UiField
	SpanElement title;
	@UiField
	SpanElement labelPrevious;
	@UiField
	SpanElement previous;

//	@UiField
//	SpanElement version;

//	@UiField
//	SpanElement createDate;
//
//	@UiField
//	SpanElement anamnesischecksvalues;
//
//	@UiField
//	SpanElement scars;

	AnamnesisCheckProxy proxy;

//	@UiField
//	SpanElement displayRenderer;

	private Presenter presenter;

	public void setValue(AnamnesisCheckProxy proxy, String previousAnamnesisCheckText) {
		this.proxy = proxy;
		String headerText = "[";
		headerText += proxy.getId() == null ? "" : String.valueOf(proxy.getId());
		headerText += "] ";
		headerText += proxy.getText() == null ? "" : String.valueOf(proxy.getText());
		header.setInnerText(headerText);
		
		type.setInnerText(proxy.getType() == null ? "" : new EnumRenderer<AnamnesisCheckTypes>().render(proxy.getType()));
		text.setInnerText(proxy.getText() == null ? "" : String.valueOf(proxy.getText()));

		if (proxy.getType() == AnamnesisCheckTypes.QUESTION_MULT_M || proxy.getType() == AnamnesisCheckTypes.QUESTION_MULT_S) {
			labelValue.setInnerText(constants.possibleAnswers() + ":");
			if (proxy.getValue() != null) {
				String substrs[] = proxy.getValue().split("\\|");
				for (int i = 0; i < substrs.length; i++) {
					valuePanel.add(new Label(substrs[i]));
				}
			}
		} else {
			labelValue.setInnerText("");
		}
		
//		if(proxy.getType() == AnamnesisCheckTypes.QUESTION_TITLE){
//			GWT.log("in AnamnesisCheckDetailsViewImpl setValue type is QUESTION_TITLE");
//			labelTitle.setInnerText("");
//			title.setInnerText("");
//		}else{
			if(proxy.getAnamnesisCheckTitle()!=null){
				String titleTextString = proxy.getAnamnesisCheckTitle().getText();
				labelTitle.setInnerText(constants.insideTitle());
			    title.setInnerText(titleTextString == null ? "" : String.valueOf(titleTextString));
			}else{
				labelTitle.setInnerText(constants.insideTitle());
				title.setInnerText(constants.noTitle());
			}
//		}
		
		
	    if(proxy.getSort_order() != null && proxy.getSort_order() > 1){
//	    	if(proxy.getType() == AnamnesisCheckTypes.QUESTION_TITLE){
//	    	labelPrevious.setInnerText(constants.previousTitle());
//	    	}else{
	    	labelPrevious.setInnerText(constants.previousQuestion());
//	    	}
		    previous.setInnerText(previousAnamnesisCheckText == null ? "" : String.valueOf(previousAnamnesisCheckText));
	    }else{
	    	labelPrevious.setInnerText("");
	    	previous.setInnerText("");
	    }
		
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter AnamnesisCheckActivity) {
		this.presenter =  AnamnesisCheckActivity;
	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public AnamnesisCheckProxy getValue() {
		return proxy;
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}
}
