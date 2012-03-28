/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.resources.AnamnesisQuestionTypeImages;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
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

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private Delegate delegate;

	@UiField
	SplitLayoutPanel splitLayoutPanel;
	
	@UiField (provided = true)
	QuickSearchBox searchBox;

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
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), 400);
		newButton.setText(constants.addAnamnesisValue());
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		paths.add("type");
		
		table.addColumn(new Column<AnamnesisCheckProxy, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(AnamnesisCheckProxy proxy) {
				OsceConstantsWithLookup constantsWithLookup = GWT.create(OsceConstantsWithLookup.class);
				AnamnesisQuestionTypeImages resources = GWT.create(AnamnesisQuestionTypeImages.class);
				String html = "";
				switch (proxy.getType()) {
				case QUESTION_TITLE:
					html = "<img src=\"" + resources.title().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_TITLE() + "\" />";
					break;
				case QUESTION_MULT_M:
					html = "<img src=\"" + resources.questionMultM().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_MULT_M() + "\" />";
					break;
				case QUESTION_MULT_S:
					html = "<img src=\"" + resources.questionMultS().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_MULT_S() + "\" />";
					break;
				case QUESTION_YES_NO:
					html = "<img src=\"" + resources.questionYesNo().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_YES_NO()+ "\" />";
					break;
				case QUESTION_OPEN:
				default:
					html = "<img src=\"" + resources.questionOpen().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_OPEN() + "\" />";
				}
				return (new SafeHtmlBuilder().appendHtmlConstant(html).toSafeHtml());
			}
		}, constants.type());
		
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
		}, constants.text());
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
			
		}, constants.value());
		
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
