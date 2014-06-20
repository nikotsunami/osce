package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceParcourViewImpl extends Composite implements ManualOsceParcourView {

	private static ManualOsceParcourViewImplUiBinder uiBinder = GWT.create(ManualOsceParcourViewImplUiBinder.class);

	interface ManualOsceParcourViewImplUiBinder extends UiBinder<Widget, ManualOsceParcourViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField
	VerticalPanel mainPanel;
	
	AccordianPanelView accordianPanelView;
	
	ContentViewImpl contentView;
	
	HeaderView headerView;
	
	FocusableValueListBox<PostType> postTypeListBox;
	
	private CourseProxy courseProxy;
	
	private OsceSequenceProxy osceSequenceProxy;
	
	IconButton deleteButton = new IconButton();
	
	private VerticalPanel addOscePostVerticalPanel;
	
	public ManualOsceParcourViewImpl(String color, Boolean deleteBtnFlg) {
		initWidget(uiBinder.createAndBindUi(this));
		
		deleteButton.setIcon("trash");
		
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
				dialogBox.showYesNoDialog(constants.manualOsceDeleteParcour());
				
				dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						dialogBox.hide();
						delegate.deleteParcour(osceSequenceProxy, courseProxy);
					}
				});
			}
		});
		
		init(color, deleteBtnFlg);
	}

	private void init(String color, Boolean deleteBtnFlg) {
		accordianPanelView = new AccordianPanelViewImpl(true);
		
		contentView=new ContentViewImpl();
		contentView.addStyleName("accordion-title-selected" + color);
		contentView.getContentPanel().getElement().getStyle().setWidth(100, Unit.PCT);
		contentView.getScrollPanel().getElement().getStyle().setWidth(100, Unit.PCT);
		
		if (addOscePostVerticalPanel == null)
		{
			initCreatePostView();
		}
		
		contentView.getPostHP().insert(addOscePostVerticalPanel, contentView.getPostHP().getWidgetCount());
		
		headerView = new HeaderViewImpl();		
		headerView.getHeaderPanel().setHeight("230px");
        headerView.getColorPicker().setIcon("colorPickerIcon");
		headerView.getHeaderSimplePanel().setHeight("230px");
		
		if (deleteBtnFlg)
		{
			headerView.getDeleteBtnPanel().add(deleteButton);
		}		
	
		headerView.setContentView(contentView);
		
		accordianPanelView.add(headerView.asWidget(), contentView);
		
		headerView.addParcourStyle(color);
		
		mainPanel.add(accordianPanelView);
	}
	
	public VerticalPanel initCreatePostView(){
		if (addOscePostVerticalPanel == null)
			addOscePostVerticalPanel = new VerticalPanel();
		
		addOscePostVerticalPanel.setWidth("140px");
		addOscePostVerticalPanel.setHeight("110px");
		addOscePostVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		addOscePostVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		addOscePostVerticalPanel.addStyleName("tabStyle");
		addOscePostVerticalPanel.addStyleName("patientTopContainer");		
		addOscePostVerticalPanel.addStyleName("setOscePostViewHeaderPanelColor");
				
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("138px");
		horizontalPanel.addStyleName("setOsceCreatePostViewHeaderPanelColor");
		horizontalPanel.add(new Label(constants.postType()));
		
		postTypeListBox = new FocusableValueListBox<PostType>(new EnumRenderer<PostType>());
		postTypeListBox.setWidth("120px");
		postTypeListBox.setAcceptableValues(Arrays.asList(PostType.NORMAL, PostType.BREAK));
		
		postTypeListBox.addValueChangeHandler(new ValueChangeHandler<PostType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PostType> event) {
				if (event.getValue() != null)
				{
					delegate.addOscePost(courseProxy, event.getValue(), postTypeListBox);
				}
			}
		});
		
		addOscePostVerticalPanel.add(horizontalPanel);
		addOscePostVerticalPanel.add(postTypeListBox);
		
		addOscePostVerticalPanel.getElement().getStyle().setMarginBottom(90, Unit.PCT);
		
		return addOscePostVerticalPanel;
	}
	

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public ContentViewImpl getContentView() {
		return contentView;
	}
	
	public HeaderView getHeaderView() {
		return headerView;
	}
	
	public AccordianPanelView getAccordianPanelView() {
		return accordianPanelView;
	}
	
	public CourseProxy getCourseProxy() {
		return courseProxy;
	}
	
	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
		headerView.setProxy(courseProxy);
	}

	@Override
	public void addDeleteButton() {
		headerView.getDeleteBtnPanel().add(deleteButton);
	}
	
	public void removeDeleteButton(){
		headerView.getDeleteBtnPanel().clear();
	}
	
	public OsceSequenceProxy getOsceSequenceProxy() {
		return osceSequenceProxy;
	}
	
	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy) {
		this.osceSequenceProxy = osceSequenceProxy;
	}
	
	public VerticalPanel getAddOscePostVerticalPanel() {
		return addOscePostVerticalPanel;
	}
	
	public FocusableValueListBox<PostType> getPostTypeListBox() {
		return postTypeListBox;
	}
	
	public IconButton getDeleteButton() {
		return deleteButton;
	}
}
