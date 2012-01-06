/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.interfaces.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.interfaces.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.interfaces.QuestionTypeImages;

import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class AnamnesisCheckViewImpl extends Composite implements AnamnesisCheckView {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, AnamnesisCheckViewImpl> {
	}

	private Delegate delegate;

	@UiField
	SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	TextBox searchBox;

	@UiField
	Button newButton;
	
	@UiField
	SimplePanel detailsPanel;
	
	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided=true)
	CellTable<AnamnesisCheckProxy> table;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newClicked();
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
	public AnamnesisCheckViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AnamnesisCheckProxy>(15, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, 30, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), 400);
		newButton.setText(Messages.ADD_ANAMNESIS_VALUE);
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		searchBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent arg0) {
				searchBox.setValue("");
			}
		});
		searchBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent arg0) {
				if(searchBox.getValue().isEmpty()) {
					searchBox.setValue(Messages.SEARCHFIELD);
				}
			}
		});
		searchBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				String q = searchBox.getValue();
				delegate.performSearch(q);
			}
		});
		
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		paths.add("type");
		
		table.addColumn(new Column<AnamnesisCheckProxy, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(AnamnesisCheckProxy proxy) {
				QuestionTypeImages resources = GWT.create(QuestionTypeImages.class);
				String html = "";
				switch (proxy.getType()) {
				case Title:
					html = "<img src=\"" + resources.title().getURL() + "\" title=\"" + Messages.ANAMNESIS_QUESTION_TITLE + "\" />";
					break;
				case QuestionMultM:
					html = "<img src=\"" + resources.questionMultM().getURL() + "\" title=\"" + Messages.ANAMNESIS_QUESTION_MULTM + "\" />";
					break;
				case QuestionMultS:
					html = "<img src=\"" + resources.questionMultS().getURL() + "\" title=\"" + Messages.ANAMNESIS_QUESTION_MULTS + "\" />";
					break;
				case QuestionYesNo:
					html = "<img src=\"" + resources.questionYesNo().getURL() + "\" title=\"" + Messages.ANAMNESIS_QUESTION_YESNO + "\" />";
					break;
				case QuestionOpen:
				default:
					html = "<img src=\"" + resources.questionOpen().getURL() + "\" title=\"" + Messages.ANAMNESIS_QUESTION_OPEN + "\" />";
				}
				return (new SafeHtmlBuilder().appendHtmlConstant(html).toSafeHtml());
			}
		}, Messages.TYPE);
		
		paths.add("text");
		table.addColumn(new TextColumn<AnamnesisCheckProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AnamnesisCheckProxy object) {
				return renderer.render(object.getText());
			}
		}, Messages.TEXT);
		paths.add("value");
		
		table.addColumn(new Column<AnamnesisCheckProxy, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(AnamnesisCheckProxy proxy) {
				// TODO Auto-generated method stub
				String html = "";
				String[] values;
				
				if (proxy != null) {
					values = proxy.getValue().split("\\|");
					if (values.length > 1) {
						html = "<ul>";
						for (int i=0; i < values.length; i++) {
							html += "<li>" + values[i] + "</li>";
						}
						html += "</ul>";
					} else {
						html = values[0];
					}
				}
				
				return (new SafeHtmlBuilder().appendHtmlConstant(html)).toSafeHtml();
			}
			
		}, Messages.VALUE);
		
//		table.addColumn(new TextColumn<AnamnesisCheckProxy>() {
//
//			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//				public String render(java.lang.String obj) {
//					return obj == null ? "" : String.valueOf(obj);
//				}
//			};
//
//			@Override
//			public String getValue(AnamnesisCheckProxy object) {
//				return renderer.render(object.getValue());
//			}
//		}, "Value");
		
		table.addColumnStyleName(0, "iconCol");
//		paths.add("createDate");
//		table.addColumn(new TextColumn<AnamnesisCheckProxy>() {
//
//			Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
//
//			@Override
//			public String getValue(AnamnesisCheckProxy object) {
//				return renderer.render(object.getCreateDate());
//			}
//		}, "Create Date");
//		paths.add("anamnesischecksvalues");
//		table.addColumn(new TextColumn<AnamnesisCheckProxy>() {
//
//			Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueProxyRenderer.instance());
//
//			@Override
//			public String getValue(AnamnesisCheckProxy object) {
//				return renderer.render(object.getAnamnesischecksvalues());
//			}
//		}, "Anamnesischecksvalues");
//		paths.add("scars");
//		table.addColumn(new TextColumn<AnamnesisCheckProxy>() {
//
//			Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.ScarProxyRenderer.instance());
//
//			@Override
//			public String getValue(AnamnesisCheckProxy object) {
//				return renderer.render(object.getScars());
//			}
//		}, "Scars");
	}

	@Override
	public CellTable<AnamnesisCheckProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
