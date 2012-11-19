package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StatisticalEvaluationDetailPostViewImpl extends Composite implements StatisticalEvaluationDetailPostView{

	private static StatisticalEvaluationDetailPostViewImplUiBinder uiBinder = GWT.create(StatisticalEvaluationDetailPostViewImplUiBinder.class);

	interface StatisticalEvaluationDetailPostViewImplUiBinder extends UiBinder<Widget, StatisticalEvaluationDetailPostViewImpl> {
	}

	private Delegate delegate;
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private OscePostProxy oscePostProxy;
	
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}
	
	@UiField
	Label postNameLbl;
	
	@UiField
	HorizontalPanel postNameHP;
	
	public HorizontalPanel getPostNameHP() {
		return postNameHP;
	}

	public Label getPostNameLbl() {
		return postNameLbl;
	}

	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}

	OsceConstants constants = GWT.create(OsceConstants.class);
	
	public StatisticalEvaluationDetailPostViewImpl() 
	{
		Log.info("Call StatisticalEvaluationDetailPostViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		postNameLbl.addStyleName("verticalText");
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public Widget asWidget() {
		return this;
	}

}
