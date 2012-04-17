/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
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
public class DoctorViewImpl extends Composite implements  DoctorView {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, DoctorViewImpl> {
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
	
	@UiField (provided = true)
	CellTable<DoctorProxy> table;

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
	public DoctorViewImpl() {
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		table = new CellTable<DoctorProxy>(15, tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, 30, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addDoctor());
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

//    	paths.add("id");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//                public String render(java.lang.Long obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getId());
//            }
//        }, "Id");
//        paths.add("version");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//                public String render(java.lang.Integer obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getVersion());
//            }
//        }, "Version");
//        paths.add("gender");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getGender().toString());
//            }
//        }, "Gender");
//        paths.add("title");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getTitle());
//            }
//        }, "Title");
		paths.add("name");
		table.addColumn(new TextColumn<DoctorProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(DoctorProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
		paths.add("preName");
		table.addColumn(new TextColumn<DoctorProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(DoctorProxy object) {
				return renderer.render(object.getPreName());
			}
		}, constants.preName());
//        paths.add("email");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getEmail());
//            }
//        }, "Email");
//        paths.add("telephone");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getTelephone());
//            }
//        }, "Telephone");
//        paths.add("clinic");
//        table.addColumn(new TextColumn<DoctorProxy>() {
//
//            Renderer<ch.unibas.medizin.osce.client.managed.request.ClinicProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer.instance();
//
//            @Override
//            public String getValue(DoctorProxy object) {
//                return renderer.render(object.getClinic());
//            }
//        }, "Clinic");
		paths.add("office");
		table.addColumn(new TextColumn<DoctorProxy>() {

			Renderer<ch.unibas.medizin.osce.client.managed.request.OfficeProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.OfficeProxyRenderer.instance();

			@Override
			public String getValue(DoctorProxy object) {
				return renderer.render(object.getOffice());
			}
		}, constants.officeDetails());
	}

	@Override
	public CellTable<DoctorProxy> getTable() {
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
