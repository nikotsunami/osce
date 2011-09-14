/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nikotsunami
 *
 */
public class AnamnesisFormViewImpl extends Composite implements  AnamnesisFormView {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, AnamnesisFormViewImpl> {
	}

	private Delegate delegate;

	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField
	Button newButton;
	
	@UiField
	SimplePanel detailsPanel;

	@UiField
	CellTable<AnamnesisFormProxy> table;

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
	public AnamnesisFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		paths.add("id");
		table.addColumn(new TextColumn<AnamnesisFormProxy>() {

			Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

				public String render(java.lang.Long obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AnamnesisFormProxy object) {
				return renderer.render(object.getId());
			}
		}, "Id");
		paths.add("version");
		table.addColumn(new TextColumn<AnamnesisFormProxy>() {

			Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

				public String render(java.lang.Integer obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AnamnesisFormProxy object) {
				return renderer.render(object.getVersion());
			}
		}, "Version");
		paths.add("createDate");
		table.addColumn(new TextColumn<AnamnesisFormProxy>() {

			Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));

			@Override
			public String getValue(AnamnesisFormProxy object) {
				return renderer.render(object.getCreateDate());
			}
		}, "Create Date");
		paths.add("anamnesischecksvalues");
		table.addColumn(new TextColumn<AnamnesisFormProxy>() {

			Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueProxyRenderer.instance());

			@Override
			public String getValue(AnamnesisFormProxy object) {
				return renderer.render(object.getAnamnesischecksvalues());
			}
		}, "Anamnesischecksvalues");
		paths.add("scars");
		table.addColumn(new TextColumn<AnamnesisFormProxy>() {

			Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.ScarProxyRenderer.instance());

			@Override
			public String getValue(AnamnesisFormProxy object) {
				return renderer.render(object.getScars());
			}
		}, "Scars");
	}

	@Override
	public CellTable<AnamnesisFormProxy> getTable() {
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
