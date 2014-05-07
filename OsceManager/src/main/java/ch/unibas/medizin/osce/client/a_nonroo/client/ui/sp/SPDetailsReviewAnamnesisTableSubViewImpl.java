package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SPDetailsReviewAnamnesisTableSubViewImpl extends Composite implements SPDetailsReviewAnamnesisTableSubView {

	private static SPDetailsReviewAnamnesisTableSubViewImplUiBinder uiBinder = GWT
			.create(SPDetailsReviewAnamnesisTableSubViewImplUiBinder.class);

	interface SPDetailsReviewAnamnesisTableSubViewImplUiBinder extends
			UiBinder<Widget, SPDetailsReviewAnamnesisTableSubViewImpl> {
	}
	
	@UiField (provided = true)
	FlexTable table;
	
	private String NO_ICON = "<span class=\"ui-icon ui-icon-circle-close\"></span>";
	private String YES_ICON="<span class=\"ui-icon ui-icon-circle-check\"></span>";
	
	private Delegate delegate;
	private OsceConstants constants = GWT.create(OsceConstants.class);

	private Label quesiontLabel = new Label("Question");
	
	private Label answerLabel = new Label("Answer");
	
	private boolean isChangedData;
	
	public SPDetailsReviewAnamnesisTableSubViewImpl() {
		table = new FlexTable();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initTable();
	}
	
	private void initTable() {

		table.setWidget(0, 0,quesiontLabel);
		table.setWidget(0, 1,answerLabel);
		
		table.getColumnFormatter().setWidth(0, "50%");
		
		table.getWidget(0, 0).addStyleName("flexTableHeader");
		table.getWidget(0, 1).addStyleName("flexTableHeader");
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean setValue(List<AnamnesisChecksValueProxy> anamnesisCheckValueList,List<SpAnamnesisChecksValueProxy> spAnamnesisCheckValueList) {
		
		isChangedData=false;
		
		for(int count=0;count < anamnesisCheckValueList.size() ; count++){
			
			AnamnesisChecksValueProxy anamnesisChecksValueProxy = anamnesisCheckValueList.get(count);
			
			Log.info("Anamnesis check value proxy is : " + anamnesisChecksValueProxy.getId());
			
			SpAnamnesisChecksValueProxy spAnamnesisChecksValueProxy = spAnamnesisCheckValueList.get(count);
			
			Log.info("SP Anamnesis check value proxy is : " + spAnamnesisChecksValueProxy.getId());
			
			String anmnesischeckOldValue =anamnesisChecksValueProxy.getAnamnesisChecksValue();
			
			String anmnesischeckNewValue=spAnamnesisChecksValueProxy.getAnamnesisChecksValue();
			
			AnamnesisCheckProxy anamnesisCheckProxy = anamnesisChecksValueProxy.getAnamnesischeck();
			
			VerticalPanel mainPanel = new VerticalPanel();
			mainPanel.setWidth("100%");
			
			//If both old and new value is null
			if(anmnesischeckOldValue==null && anmnesischeckNewValue==null){
			
				//if question is of yes-no type question.
				if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_YES_NO.ordinal()){

					HTML html=getUserSelectedYesOrNoValue(anamnesisChecksValueProxy,spAnamnesisChecksValueProxy);
					mainPanel.add(html);
				}else{
					HTML html =new HTML();
					mainPanel.add(html);
				}
			}else if(anmnesischeckOldValue==null && anmnesischeckNewValue!=null){
				
					SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
				
					if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_OPEN.ordinal()){
							//Label oldValue = new Label(anmnesischeckOldValue);
							//oldValue.addStyleName("oldData");
							Label newValue = new Label(anmnesischeckNewValue);
							newValue.addStyleName("newAnswered");
							//mainPanel.add(oldValue);
							mainPanel.add(newValue);
					}else if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_MULT_S.ordinal() ||
							
							anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_MULT_M.ordinal()){
					
							String [] allQuestionOptions = anamnesisCheckProxy.getValue().split("\\|");
					
							for (int option = 0; option < allQuestionOptions.length; option++) {
								safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(YES_ICON).appendHtmlConstant(allQuestionOptions[option]);
								HTML html = new HTML(safeHtml.toSafeHtml());
								html.addStyleName("newAnswered");
								mainPanel.add(html);
							}
						}
					isChangedData=true;
			}else if(anmnesischeckOldValue!=null && anmnesischeckNewValue==null){
				
						SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
						
						if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_OPEN.ordinal()){
							Label oldValue = new Label(anmnesischeckOldValue);
							oldValue.addStyleName("oldData");
							mainPanel.add(oldValue);
						}else if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_MULT_S.ordinal() ||
								anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_MULT_M.ordinal()){
				
							String [] allQuestionOptions = anamnesisCheckProxy.getValue().split("\\|");
				
							for (int option = 0; option < allQuestionOptions.length; option++) {
								safeHtml.appendHtmlConstant(YES_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant(allQuestionOptions[option]);
								HTML html = new HTML(safeHtml.toSafeHtml());
								html.addStyleName("oldAnswered");
								mainPanel.add(html);
							}
						}
						isChangedData=true;
			}else if(anmnesischeckOldValue!=null && anmnesischeckNewValue!=null){
				
					SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
					
					if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_OPEN.ordinal()){
						
						Label oldValue = new Label(anmnesischeckOldValue);
						
						Label newValue = new Label(anmnesischeckNewValue);
						
						if(!anmnesischeckOldValue.equals(anmnesischeckNewValue)){
							oldValue.addStyleName("oldData");
							newValue.addStyleName("newAnswered");
							
							mainPanel.add(oldValue);
							
							mainPanel.add(newValue);
							isChangedData=true;
						}else{
							mainPanel.add(oldValue);
						}
						
						
					}else if(anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_MULT_S.ordinal() ||
							anamnesisCheckProxy.getType().ordinal()==AnamnesisCheckTypes.QUESTION_MULT_M.ordinal()){
				
						String [] allQuestionOptions = anamnesisCheckProxy.getValue().split("\\|");
				
						String[] allOldSelectedValues= anmnesischeckOldValue.split("\\-");
						
						String[] allNewSelectedValues= anmnesischeckNewValue.split("\\-");
						
						for (int option = 0; option < allQuestionOptions.length; option++) {
					
							String oldOption=allOldSelectedValues[option];
							String newOption =allNewSelectedValues[option];
							//if old and new option is same
							if(oldOption.equals(newOption)){
								safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant(allQuestionOptions[option]);
								HTML html = new HTML(safeHtml.toSafeHtml());
								mainPanel.add(html);
							}else if(oldOption.equals("0") && newOption.equals("1")){
								safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(YES_ICON).appendHtmlConstant(allQuestionOptions[option]);
								HTML html = new HTML(safeHtml.toSafeHtml());
								html.addStyleName("newAnswered");
								mainPanel.add(html);
								isChangedData=true;
							}else if(oldOption.equals("1") && newOption.equals("0")){
								safeHtml.appendHtmlConstant(YES_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant(allQuestionOptions[option]);
								HTML html = new HTML(safeHtml.toSafeHtml());
								html.addStyleName("oldAnswered");
								mainPanel.add(html);
								isChangedData=true;
							}
							
						}
					}
				}
		
			Label questionLabel =new Label(anamnesisCheckProxy.getText());
			if((count%2)==0){
				table.getRowFormatter().addStyleName(count, "evenRowBgColor");
			}else{
				table.getRowFormatter().addStyleName(count, "oddRowBgColor");
			}
			table.setWidget(count+1, 0,questionLabel);
			table.setWidget(count+1, 1,mainPanel);
		}
	
		return isChangedData;
	}
	
	public HTML getUserSelectedYesOrNoValue(AnamnesisChecksValueProxy anamnesisChecksValueProxy,SpAnamnesisChecksValueProxy spAnamnesisChecksValueProxy){
	
		HTML html=new HTML();
		SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
	
		
		if(anamnesisChecksValueProxy.getTruth()==null && spAnamnesisChecksValueProxy.getTruth()==null){
			//safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant("NO");
			html= new HTML();
			//html.setStyleName("oldAnswered");
			return html;
			
		}else if(anamnesisChecksValueProxy.getTruth()==null && spAnamnesisChecksValueProxy.getTruth()!=null){
			safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(YES_ICON).appendHtmlConstant("YES");
			html= new HTML(safeHtml.toSafeHtml());
			html.setStyleName("newAnswered");
			isChangedData=true;
			return html;
			
		}else if(anamnesisChecksValueProxy.getTruth()!=null && spAnamnesisChecksValueProxy.getTruth()==null){
			safeHtml.appendHtmlConstant(YES_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant("NO");
			html= new HTML(safeHtml.toSafeHtml());
			html.setStyleName("oldAnswered");
			isChangedData=true;
			return html;
			
		}else{
			
			
				 if(!anamnesisChecksValueProxy.getTruth() && !spAnamnesisChecksValueProxy.getTruth()){
					 safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant("NO");
					html = new HTML(safeHtml.toSafeHtml());
					//html.setStyleName("oldAnswered");
					return html;
				}else if(!anamnesisChecksValueProxy.getTruth() && spAnamnesisChecksValueProxy.getTruth()){
					safeHtml.appendHtmlConstant(NO_ICON).appendHtmlConstant(YES_ICON).appendHtmlConstant("YES");
					html = new HTML(safeHtml.toSafeHtml());
					html.setStyleName("newAnswered");
					isChangedData=true;
					return html;
				}else if(anamnesisChecksValueProxy.getTruth() && !spAnamnesisChecksValueProxy.getTruth()){
					safeHtml.appendHtmlConstant(YES_ICON).appendHtmlConstant(NO_ICON).appendHtmlConstant("NO");
					html = new HTML(safeHtml.toSafeHtml());
					html.setStyleName("oldAnswered");
					isChangedData=true;
					return html;
				}else if(anamnesisChecksValueProxy.getTruth() && spAnamnesisChecksValueProxy.getTruth()){
					safeHtml.appendHtmlConstant(YES_ICON).appendHtmlConstant(YES_ICON).appendHtmlConstant("YES");
					html = new HTML(safeHtml.toSafeHtml());
					//html.setStyleName("oldAnswered");
					return html;
				}
		}
		return html;
	}
	
}

