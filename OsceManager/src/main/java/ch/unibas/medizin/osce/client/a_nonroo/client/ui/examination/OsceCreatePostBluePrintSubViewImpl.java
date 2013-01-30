
/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class OsceCreatePostBluePrintSubViewImpl extends Composite implements OsceCreatePostBluePrintSubView {

	private static OsceCreatePostBluePrintSubViewUiBinder uiBinder = GWT
			.create(OsceCreatePostBluePrintSubViewUiBinder.class);

	interface OsceCreatePostBluePrintSubViewUiBinder extends UiBinder<Widget, OsceCreatePostBluePrintSubViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	private static final OsceConstants constants = GWT.create(OsceConstants.class);

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
	
	public OsceCreatePostBluePrintSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}
	public OsceCreatePostBluePrintSubViewImpl(List<SpecialisationProxy> specialisationList,List<RoleTopicProxy> roleTopicList) 
	{
		this();		
		postTypeListBox.setAcceptableValues(Arrays.asList(PostType.values()));
		roleTopicListBox.setAcceptableValues(roleTopicList);
		specializationListBox.setAcceptableValues(specialisationList);
		
	}
	public OsceCreatePostBluePrintSubViewImpl(List<SpecialisationProxy> specialisationList) 
	{
		this();		
		postTypeListBox.setAcceptableValues(Arrays.asList(PostType.values()));		
		specializationListBox.setAcceptableValues(specialisationList);
		
	}


	@UiField
	 Label postTypeLbl;
	
	@UiField(provided=true)
	 ValueListBox<SpecialisationProxy> specializationListBox=new ValueListBox<SpecialisationProxy>(new AbstractRenderer<SpecialisationProxy>() {

		@Override
		public String render(SpecialisationProxy object) {
			// TODO Auto-generated method stub
			if(object==null)
				return "";
			else
				return object.getName();
		}

		@Override
		public void render(SpecialisationProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	
	
	});
	
	@UiField(provided=true)
	 ValueListBox<RoleTopicProxy> roleTopicListBox=new ValueListBox<RoleTopicProxy>(new Renderer<RoleTopicProxy>() {

		@Override
		public String render(RoleTopicProxy object) {
			if(object==null)
				return "";
			else
				return object.getName();
		}

		@Override
		public void render(RoleTopicProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	

	@UiField(provided=true)
	FocusableValueListBox<PostType> postTypeListBox=new FocusableValueListBox<PostType>(new EnumRenderer<PostType>());
	
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		postTypeLbl.setText(constants.postType());
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
	
	
	public ValueListBox getRoleTopicListBox()
	{
		return this.roleTopicListBox;
	}
	
	public ValueListBox getSpecializationListBox()
	{
		return this.specializationListBox;
	}
	
	public FocusableValueListBox getPostTypeListBox()
	{
		return this.postTypeListBox;
	}
	
	public void setRoleTopicListBox(RoleTopicProxy roleTopicProxy)
	{
		this.roleTopicListBox.setValue(roleTopicProxy);
	}
	public  void setSpecializationListBox(SpecialisationProxy specialisationProxy)
	{
		this.specializationListBox.setValue(specialisationProxy);
	}
	public  void setPostTypeListBox(PostType postType)
	{
		this.postTypeListBox.setValue(postType);
	}
	
	
	
}
