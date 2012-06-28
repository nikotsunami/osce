/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
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
public class AnamnesisCheckTitleDetailsViewImpl extends Composite implements AnamnesisCheckTitleDetailsView {

	private static AnamnesisCheckTitleDetailsViewImplUiBinder uiBinder = GWT
			.create(AnamnesisCheckTitleDetailsViewImplUiBinder.class);

	interface AnamnesisCheckTitleDetailsViewImplUiBinder extends
	UiBinder<Widget, AnamnesisCheckTitleDetailsViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField
	IconButton edit;

	@UiField
	IconButton delete;
	
	@UiField
	TabPanel anamnesisTitlePanel;

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
	public AnamnesisCheckTitleDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		anamnesisTitlePanel.selectTab(0);
		anamnesisTitlePanel.getTabBar().setTabText(0, constants.anamnesisValues());
		TabPanelHelper.moveTabBarToBottom(anamnesisTitlePanel);
		
		edit.setText(constants.edit());
		delete.setText(constants.delete());
		
<<<<<<< HEAD
		labelText.setInnerText(constants.anamnesisCheckTitle() + ":");

	}
	
	@UiField
	SpanElement labelText;
	@UiField
	SpanElement text;
	@UiField
	SpanElement labelPrevious;
	@UiField
	SpanElement previous;

	@UiField
	SpanElement header;
	AnamnesisCheckTitleProxy proxy;
	


=======
//		labelType.setInnerText(constants.type() + ":");
		labelText.setInnerText(constants.question() + ":");

	}
	
//	@UiField
//	SpanElement labelType;
//	@UiField
//	SpanElement labelValue;
	@UiField
	SpanElement labelText;
	
//	@UiField
//	SpanElement header;
//	@UiField
//	SpanElement type;
//	@UiField
//	VerticalPanel valuePanel;
	
	@UiField
	SpanElement text;
	
//	@UiField
//	SpanElement labelTitle;
//	@UiField
//	SpanElement title;
//	@UiField
//	SpanElement labelPrevious;
//	@UiField
//	SpanElement previous;

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

	AnamnesisCheckTitleProxy proxy;

//	@UiField
//	SpanElement displayRenderer;
>>>>>>> paul/master

	private Presenter presenter;

	public void setValue(AnamnesisCheckTitleProxy proxy,String title) {
		this.proxy = proxy;
<<<<<<< HEAD
		String headerText = "[";
		headerText += proxy.getId() == null ? "" : String.valueOf(proxy.getId());
		headerText += "] ";
		headerText += proxy.getText() == null ? "" : String.valueOf(proxy.getText());
		header.setInnerText(headerText);
		text.setInnerText(proxy.getText() == null ? "" : String.valueOf(proxy.getText()));		
		
		if(proxy.getSort_order() != null && proxy.getSort_order() > 1){
	    	labelPrevious.setInnerText(constants.previousTitle());
		    previous.setInnerText(title == null ? "" : String.valueOf(title));
	    }else{
	    	labelPrevious.setInnerText("");
	    	previous.setInnerText("");
	    }
		
=======
//		String headerText = "[";
//		headerText += proxy.getId() == null ? "" : String.valueOf(proxy.getId());
//		headerText += "] ";
//		headerText += proxy.getText() == null ? "" : String.valueOf(proxy.getText());
//		header.setInnerText(headerText);
//		
//		//type.setInnerText(proxy.getType() == null ? "" : new EnumRenderer<AnamnesisCheckTypes>().render(proxy.getType()));
		text.setInnerText(proxy.getText() == null ? "" : String.valueOf(proxy.getText()));
//
//		//if (proxy.getType() == AnamnesisCheckTypes.QUESTION_MULT_M || proxy.getType() == AnamnesisCheckTypes.QUESTION_MULT_S) {
//			labelValue.setInnerText(constants.possibleAnswers() + ":");
//			if (proxy.getValue() != null) {
//				String substrs[] = proxy.getValue().split("\\|");
//				for (int i = 0; i < substrs.length; i++) {
//					valuePanel.add(new Label(substrs[i]));
//				}
//			}
//	//	} else {
//			labelValue.setInnerText("");
//	//	}
		
//		//if(proxy.getType() == AnamnesisCheckTypes.QUESTION_TITLE){
//			GWT.log("in AnamnesisCheckDetailsViewImpl setValue type is QUESTION_TITLE");
//			labelTitle.setInnerText("");
//			title.setInnerText("");
//		//}else{
//			if(proxy.getTitle()!=null){
//				String titleTextString = proxy.getTitle().getText();
//				labelTitle.setInnerText(constants.insideTitle());
//			    title.setInnerText(titleTextString == null ? "" : String.valueOf(titleTextString));
//			}else{
//				labelTitle.setInnerText(constants.insideTitle());
//				title.setInnerText(constants.noTitle());
//			}
//		//}
//		
//		
//	    if(proxy.getSort_order()!=null&&proxy.getSort_order()!=1){
//	    	//if(proxy.getType() == AnamnesisCheckTypes.QUESTION_TITLE){
//	    	labelPrevious.setInnerText(constants.previousTitle());
//	    	//}else{
//	    		labelPrevious.setInnerText(constants.previousQuestion());
//	    	//}
//		    previous.setInnerText(previousAnamnesisCheckText == null ? "" : String.valueOf(previousAnamnesisCheckText));
//	    //}else{
//	    	labelPrevious.setInnerText("");
//	    	previous.setInnerText("");
//	    }
//		
//		
>>>>>>> paul/master
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter AnamnesisCheckTitleDetailsActivity) {
		this.presenter =  AnamnesisCheckTitleDetailsActivity;
	}
<<<<<<< HEAD

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public AnamnesisCheckTitleProxy getValue() {
		return proxy;
	}
=======
//
//	public Widget asWidget() {
//		return this;
//	}
//
//	public boolean confirm(String msg) {
//		return Window.confirm(msg);
//	}
//
//	public AnamnesisCheckTitleProxy getValue() {
//		return proxy;
//	}
>>>>>>> paul/master

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}
}
