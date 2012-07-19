package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class RoleFilterViewTooltipImpl extends PopupPanel  implements RoleView{
	
	private static RoleTopicFilterPopupUiBinder uiBinder = GWT.create(RoleTopicFilterPopupUiBinder.class);
	
	interface RoleTopicFilterPopupUiBinder extends
	UiBinder<Widget, RoleFilterViewTooltipImpl> {
	}
	
	
	private class CheckBoxItem {
		public CheckBox checkbox;
		public String name;
		
		public CheckBoxItem(CheckBox box, String n) {
			checkbox = box;
			name = n;
		}
	}
	
	private ArrayList<CheckBoxItem> fields = new ArrayList<CheckBoxItem>();
	private int maxApplicableFilters;
	private int minApplicableFilters = 1;
	private boolean selectionChanged = false;
	private boolean addBoxesShown = true;
	private Delegate delegate;
	private Presenter presenter;
	private List<String> tableFilters; // new ArrayList<String>();
	private List<String> whereFilters;// = new ArrayList<String>();
	
	private MultiWordSuggestOracle keywordoracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle autheroracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle revieweroracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle specificationoracle = new MultiWordSuggestOracle();
	
	
	
	
	@UiField
	FocusPanel filterPanelRoot;
	
	@UiField
	CheckBox TopicName;
	
	@UiField
	CheckBox Keyword;
	
	@UiField
	CheckBox ItemName;
	
	@UiField
	CheckBox RoleName;
	
	@UiField
	CheckBox CheckListItem;
	
	@UiField
	CheckBox ItemValue;
	
	@UiField
	Label Complexity;
	
	@UiField
	TextBox ComplexityText;
	
	 @UiField(provided = true)
	    ValueListBox<Comparison> ComplexityListBox = new ValueListBox<Comparison>(new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC));
	

	
	@UiField
	Label Author;
	
	
	@UiField
	Label Reviewer;
	
	


	@UiField
	Label Specification;
	
	
	
	

	
	@UiField
	Label StudyYear;
	
	
	
	@UiField(provided = true)
    ValueListBox<StudyYears> StudyYearListBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
	
	
	@UiField
	Label Keywordlbl;
	
	
	@UiField(provided = true)
	SuggestBox KeywordSugestionBox =  new SuggestBox(keywordoracle);
	
	@UiField(provided = true)
	SuggestBox autherSugestionBox =  new SuggestBox(autheroracle);
	
	@UiField(provided = true)
	SuggestBox reviewerSugestionBox =  new SuggestBox(revieweroracle);
	
	@UiField(provided = true)
	SuggestBox SpecificationSugestionBox =  new SuggestBox(specificationoracle);
	
	
	
	   
	
//	@UiField
//	ListBox KeywordListBox;
	
	@UiField
	IconButton resetButton;
	
	@UiHandler("resetButton")
	void onClick(ClickEvent e) {
		Iterator<CheckBoxItem> iter = fields.iterator();
		while(iter.hasNext()) {
			iter.next().checkbox.setValue(false);
						
		}
		
		StudyYearListBox.setValue(null);
		ComplexityListBox.setValue(null);
		
		/* by spec code for listbox instead of suggesionbox
		
		StudyYearListBox.setValue(StudyYears.values()[0]);
		
		 AuthorListBox.setValue(null);
		 ReviewerListBox.setValue(null);
		 SpecificationListBox.setValue(null);
		 KeywordListBox.setValue(null);
		


		@UiField
		ListBox StudyYearListBox;
		

		@UiField(provided = true)
	    ValueListBox<DoctorProxy> AuthorListBox = new ValueListBox<DoctorProxy>(new DoctorProxyRenderer());	
		@UiField
		ListBox AuthorListBox;
		
		@UiField(provided = true)
	  ListBox ComplexityListBox =new ListBox();
			@UiField
		ListBox ComplexityListBox;
		@UiField(provided = true)
		ValueListBox<DoctorProxy> ReviewerListBox = new ValueListBox<DoctorProxy>(new DoctorProxyRenderer());
	    

		@UiField
		ListBox ReviewerListBox;
		@UiField(provided = true)
		ValueListBox<SpecialisationProxy> SpecificationListBox = new ValueListBox<SpecialisationProxy>(new SpecialisationProxyRenderer());
		
		@UiField
		ListBox SpecificationListBox;
		 
		@UiField(provided = true)
	    ValueListBox<StudyYears> KeywordListBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
		
		@UiField(provided = true)	
		ValueListBox<KeywordProxy> KeywordListBox = new ValueListBox<KeywordProxy>(new KeywordProxyRenderer());
		
			
		@UiField(provided = true)
		TextBox keywordTextBox = new TextBox();

		by spec code for listbox instead of suggesionbox */
		
	}
	
	
	
private class CheckBoxChangeHandler implements ValueChangeHandler<Boolean> {
		

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {			
			selectionChanged = true;
			int uncheckedBoxes = 0;
			for (CheckBoxItem item : fields) {
				if (item.checkbox.getValue() == false) {
					uncheckedBoxes++;
				}
				item.checkbox.setEnabled(true);
			}
			
			if (uncheckedBoxes >= fields.size() - minApplicableFilters) {
				for (CheckBoxItem item : fields) {
					if (item.checkbox.getValue())
						item.checkbox.setEnabled(false);
				}
			} else if (fields.size() - uncheckedBoxes >= maxApplicableFilters) {
				for (CheckBoxItem item : fields) {
					if (!item.checkbox.getValue())
						item.checkbox.setEnabled(false);
				}
			}
			
			String msg = "Searching for: ";
			Iterator<String> i = getFilters().iterator();
			while(i.hasNext())
				msg = msg + i.next() + ", ";
			Log.info(msg);
		}
	}
	
	
	public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		for(CheckBoxItem checkBoxItem : fields) {
			if (checkBoxItem.checkbox.getValue()) {
				filters.add(checkBoxItem.name);
			}
		}
		
		
		getTableName();
		return filters;
	}
	
	public List<String> getTableFilters() {
		
		return tableFilters;
	}
	
public List<String> getWhereFilters() {
		
		return whereFilters;
	}
	
	
	public  void getTableName() {
	//	List<String> filters = new ArrayList<String>();
		tableFilters=null;
		whereFilters=null;
		tableFilters= new ArrayList<String>();
		whereFilters= new ArrayList<String>();
		
		
		
		if(Keyword.isChecked())	
		{
				tableFilters.add(" join sr.keywords k ");
				//whereFilters.add(" k.id = srk.keywords ");
		}
		if(ItemName.isChecked())
		{
			tableFilters.add(", role_table_item rti ");
			
			whereFilters.add(" rtiv.standardizedRole = sr.id and rti.id = rtiv.roleTableItem");
		}
		if(ItemValue.isChecked())
		{
			tableFilters.add(",role_table_item_value rtiv");
			whereFilters.add(" rtiv.standardizedRole = sr.id ");
		}
		if(RoleName.isChecked())
		{
		
		}
		
				
		if(autherSugestionBox.getValue()!="")
		{
			
			tableFilters.add(" join sr.roleParticipants rp_autor ");
			
			whereFilters.add("(rp_autor.type = 0  and rp_autor.doctor.name like'%"+autherSugestionBox.getValue()+"%') ");
		}
		if(reviewerSugestionBox.getValue()!="")
		{
		
			tableFilters.add(" join sr.roleParticipants rp_rev ");
			
			whereFilters.add("(rp_rev.type = 1  and rp_rev.doctor.name like '%"+reviewerSugestionBox.getValue()+"%') ");
		}
		if(SpecificationSugestionBox.getValue()!="")
		{
			tableFilters.add(" join rt.specialisation sp");
			whereFilters.add(" sp.name like'%"+SpecificationSugestionBox.getValue() + "%'");
		}
		if(KeywordSugestionBox.getValue()!="")
		{
			
			if(Keyword.isChecked())
			{
				
			}
			else
			tableFilters.add(" join sr.keywords k");
			whereFilters.add("k.name like'%"+KeywordSugestionBox.getValue()+"%'");
				
				
		}
		
		if(StudyYearListBox.getValue()!=null)
		{
			whereFilters.add("rt.studyYear="+ (StudyYearListBox.getValue()).ordinal());
		}
		if(ComplexityListBox.getValue()!=null){
			whereFilters.add("rt.slotsUntilChange "+ComplexityListBox.getValue().getStringValue() +" "+Integer.parseInt(ComplexityText.getValue()));
		}
		
		
		
		
		/* by spec code for advance search query 
		  if(AuthorListBox.getValue()!=null)
		{
			if(RoleName.isChecked() || (ReviewerListBox.getValue()!=null && AuthorListBox.getValue()!=null))
				tableFilters.add(",RoleParticipant rp_autor,Doctor d_autor");
			else
				tableFilters.add(",RoleParticipant rp_autor,Doctor d_autor,StandardizedRole sr");
			tableFilters.add(" join sr.roleParticipants rp_autor ");
			
			whereFilters.add("(rp_autor.type = 0  and rp_autor.doctor.name='"+AuthorListBox.getValue().getName()+"') ");
		}
		if(ReviewerListBox.getValue()!=null)
		{
			if(RoleName.isChecked() && AuthorListBox.getValue()!=null)
				tableFilters.add(",RoleParticipant rp_rev,Doctor d_rev");
			else
				tableFilters.add(",RoleParticipant rp_rev,Doctor d_rev,StandardizedRole sr");
			whereFilters.add("(sr.id =  rp_rev.standardizedRole and rp_rev.type = 0) and (rp_rev.doctor = d_rev.id) and d_rev.name='"+ReviewerListBox.getValue().getName()+"' ");
			tableFilters.add(" join sr.roleParticipants rp_rev ");
			
			whereFilters.add("(rp_rev.type = 1  and rp_rev.doctor.name='"+ReviewerListBox.getValue().getName()+"') ");
		}
		if(SpecificationListBox.getValue()!=null)
		{
			tableFilters.add(" join rt.specialisation sp");
			whereFilters.add(" sp.name ='"+SpecificationListBox.getValue().getName() + "'");
		}
		if(KeywordListBox.getValue()!=null)
		{
//			if(RoleName.isChecked() || ReviewerListBox.getValue()!=null || AuthorListBox.getValue()!=null || Keyword.isChecked()){
//			}
//			else
		//		tableFilters.add(",StandardizedRole sr,sr.keywords k");
			if(Keyword.isChecked())
			{
				
			}
			else
			tableFilters.add("join sr.keywords k");
			//whereFilters.add("k.id = sr.keywords and sr.id = k.standardizedRoles  and k.name='"+KeywordListBox.getValue().getName()+"'");
			whereFilters.add("k.name='"+KeywordListBox.getValue().getName()+"'");
				
				
		}  code for advance search query by spec*/
		
				
	
		
		
	}
	
	
	
	
	

	
	
	public RoleFilterViewTooltipImpl(){
		super(true);
		  
		
	
		
		add(uiBinder.createAndBindUi(this));
	
		StudyYearListBox.setAcceptableValues(Arrays.asList(StudyYears.values()));
		
	
		ComplexityListBox.setAcceptableValues(Arrays.asList(Comparison.values()));
		
		filterPanelRoot.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				int mouseX = event.getClientX();
				int mouseY = event.getClientY();
				
				if (mouseX < getAbsoluteLeft() || mouseX > getAbsoluteLeft() + getOffsetWidth() 
						|| mouseY < getAbsoluteTop() || mouseY > getAbsoluteTop() + getOffsetHeight()) {

					// TODO: handle it from view
					//view.updateSearch();
					
					hide();
				}
				
			}
		});
		
		
		
		OsceConstants constants = GWT.create(OsceConstants.class);
		resetButton.setText(constants.resetFilters());
		
		
		//TopicName  Keyword ItemName ItemName RoleName CheckListItem ItemValue
		
		initCheckBox(TopicName, "name", constants.topic());
		initCheckBox(Keyword, "keyword", constants. keyword());
		initCheckBox(ItemName, "itemName", constants.itemName());
		initCheckBox(RoleName, "roles", constants.roleName());
		initCheckBox(CheckListItem, "checkListItem", constants.checkListItem());
		initCheckBox(ItemValue, "itemValue", constants.itemValue());
		
		maxApplicableFilters = fields.size();
		Iterator<CheckBoxItem> fieldIter = fields.iterator();
		while (fieldIter.hasNext()) {
			CheckBox box = fieldIter.next().checkbox;
			box.addValueChangeHandler(new CheckBoxChangeHandler());
		}
		
		
		KeywordSugestionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// TODO Auto-generated method stub				

				KeywordSugestionBox.setValue(event.getSelectedItem().getReplacementString());
			}
		});
		
		KeywordSugestionBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void onChange(Widget sender) {
				// TODO Auto-generated method stub
			
				KeywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());	
			}
		});

		autherSugestionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// TODO Auto-generated method stub				
			//	System.out.println("Suggested text : " + event.getSelectedItem().getReplacementString());
				
				autherSugestionBox.setValue(event.getSelectedItem().getReplacementString());
			}
		});
		
		autherSugestionBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void onChange(Widget sender) {
				// TODO Auto-generated method stub
			//	System.out.println("change listner keyword : " + ((SuggestBox)sender).getTextBox().getValue());
				autherSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());	
			}
		});
		
		
		reviewerSugestionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// TODO Auto-generated method stub				
			//	System.out.println("Suggested text : " + event.getSelectedItem().getReplacementString());
				
				reviewerSugestionBox.setValue(event.getSelectedItem().getReplacementString());
			}
		});
		
		reviewerSugestionBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void onChange(Widget sender) {
				// TODO Auto-generated method stub
			//	System.out.println("change listner keyword : " + ((SuggestBox)sender).getTextBox().getValue());
				reviewerSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());	
			}
		});
		
		
		SpecificationSugestionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// TODO Auto-generated method stub				
				//System.out.println("Suggested text : " + event.getSelectedItem().getReplacementString());
				
				SpecificationSugestionBox.setValue(event.getSelectedItem().getReplacementString());
			}
		});
		
		SpecificationSugestionBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void onChange(Widget sender) {
				// TODO Auto-generated method stub
			//	System.out.println("change listner keyword : " + ((SuggestBox)sender).getTextBox().getValue());
				SpecificationSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());	
			}
		});




			
			
		 
	}
	

    
	
	public void clearSelectionChanged() {
		selectionChanged = false;
	}
	
	public boolean selectionChanged() {
		return selectionChanged;
	}
	
	
	private void initCheckBox(CheckBox uiField, String name, String text) {
		uiField.setText(text);
		fields.add(new CheckBoxItem(uiField, name));
	}
	
	public void setMaxApplicableFilters(int n) {
		maxApplicableFilters = n;
	}
	
	public void setMinApplicableFilters(int n) {
		if (minApplicableFilters > fields.size())
			minApplicableFilters = fields.size();
		minApplicableFilters = n;
	}
	
	public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
		return super.addCloseHandler(handler);
	}
	
	

	
@Override
	public String[] getPaths() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public CellTable<RoleTopicProxy> getTable() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void updateSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getSearchFilters() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public SimplePanel getDetailsPanel() {
		return null;
	}
	
	public ValueListBox<SpecialisationProxy> gets() {
		//return SpecificationListBox;
		return null;
	}
	
	
	/*	by spec code for list box selection value
	@Override
	public void setAuthorListBoxValues(List<DoctorProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
	
		
	//	AuthorListBox.setAcceptableValues(values);
	}
	

 	@Override
	public void setReviewerListBoxValues(List<DoctorProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
		
	
	//	ReviewerListBox.setAcceptableValues(values);
		
	}
	
	
	@Override
	public void setKeywordListBoxValues(List<KeywordProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
		
	
	//	KeywordListBox.setAcceptableValues(values);
		
	}
	
	
	@Override
	public void setSpecialisationBoxValues(List<SpecialisationProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
		
	
	
	//	SpecificationListBox.setAcceptableValues(values);
		
	}
	
	
	by spec code for list box selection value*/
	
	private void setAddBoxesShown(boolean show) {
		if (addBoxesShown  == show) {
			return;
		}
		
	//	SpecificationListBox.setVisible(show);
	//	scarAddButton.setVisible(show);
		addBoxesShown = show;
		
	}
	private void showAddBoxes() {
		setAddBoxesShown(true);
	}
	
	private void hideAddBoxes() {
		setAddBoxesShown(false);
	}

	public ValueListBox<SpecialisationProxy> getSpecialisationBox() {
	//	return SpecificationListBox;
		return null;
	}

	@Override
	public ListBox getListBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueListBox<SpecialisationProxy> getSpecialisationBoxValues() {
		// TODO Auto-generated method stub
		return null;
	}


	




	@Override
	public RoleFilterViewTooltipImpl getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKeywordAutocompleteValue(List<KeywordProxy> values) {
		// TODO Auto-generated method stub
		
		int index=0;
		while(index<values.size())
		{		
			keywordoracle.add(values.get(index).getName());
			index++;
		}
		KeywordSugestionBox = new SuggestBox(keywordoracle, new TextBox());
		
		
		
	}

	@Override
	public void setAuthorAutocompleteValue(List<DoctorProxy> values) {
		// TODO Auto-generated method stub
		int index=0;
		while(index<values.size())
		{		
			autheroracle.add(values.get(index).getName());
			index++;
		}
		autherSugestionBox = new SuggestBox(autheroracle, new TextBox());
		
	}

	@Override
	public void setReviewerAutocompleteValue(List<DoctorProxy> values) {
		// TODO Auto-generated method stub
		
		int index=0;
		while(index<values.size())
		{		
			revieweroracle.add(values.get(index).getName());
			index++;
		}
		reviewerSugestionBox = new SuggestBox(revieweroracle, new TextBox());
		
		
	}

	@Override
	public void setSpecialisationAutocompleteValue(
			List<SpecialisationProxy> values) {
		// TODO Auto-generated method stub
		
		int index=0;
		while(index<values.size())
		{		
			specificationoracle.add(values.get(index).getName());
			index++;
		}
		SpecificationSugestionBox = new SuggestBox(specificationoracle, new TextBox());
		
		
	}

	@Override
	public void setSpecialisationBoxValues(List<SpecialisationProxy> values) {
		// TODO Auto-generated method stub
		
	}
	// Highlight onViolation

	@Override
	public Map getRoleTopicMap() 
	{
		Log.info("Call getRoleTopicMap from RoleFilterViewTooltipImpl");
		return null;
	}
	// E Highlight onViolation
	

	
	//spec end
	
	
}
