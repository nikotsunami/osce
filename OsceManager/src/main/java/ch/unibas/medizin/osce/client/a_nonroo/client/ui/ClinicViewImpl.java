/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nikotsunami
 *
 */
public class ClinicViewImpl extends Composite implements  ClinicView, RecordChangeHandler {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, ClinicViewImpl> {
	}

	private Delegate delegate;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

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
	CellTable<ClinicProxy> table;

	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=Integer.parseInt(constants.widthSize()),decreaseSize=0;
	Timer timer;
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
	public ClinicViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<ClinicProxy>(15, tableResources);
		
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
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addClinic());
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

//        paths.add("id");
//        table.addColumn(new TextColumn<ClinicProxy>() {
//
//            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//                public String render(java.lang.Long obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(ClinicProxy object) {
//                return renderer.render(object.getId());
//            }
//        }, "Id");
//        paths.add("version");
//        table.addColumn(new TextColumn<ClinicProxy>() {
//
//            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//                public String render(java.lang.Integer obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(ClinicProxy object) {
//                return renderer.render(object.getVersion());
//            }
//        }, "Version");
		paths.add("name");
		table.addColumn(new TextColumn<ClinicProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ClinicProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
//        paths.add("street");
//        table.addColumn(new TextColumn<ClinicProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(ClinicProxy object) {
//                return renderer.render(object.getStreet());
//            }
//        }, "Street");
		paths.add("postalCode");
		paths.add("city");
		table.addColumn(new TextColumn<ClinicProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ClinicProxy object) {
				return renderer.render(object.getPostalCode() + " " + object.getCity());
			}
		}, constants.city());
//	    paths.add("postalCode");
//	    table.addColumn(new TextColumn<ClinicProxy>() {
//	
//	        Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//	
//	            public String render(java.lang.Integer obj) {
//	                return obj == null ? "" : String.valueOf(obj);
//	            }
//	        };
//	
//	        @Override
//	        public String getValue(ClinicProxy object) {
//	            return renderer.render(object.getPostalCode());
//	        }
//	    }, "Postal Code");
//	    paths.add("doctors");
//	    table.addColumn(new TextColumn<ClinicProxy>() {
//	
//	        Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer.instance());
//	
//	        @Override
//	        public String getValue(ClinicProxy object) {
//	            return renderer.render(object.getDoctors());
//	        }
//	    }, "Doctors");
		
		for (int i = 0; i< table.getColumnCount(); i++){
			table.getColumn(i).setSortable(true);
		}
	}

	@Override
	public CellTable<ClinicProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public SimplePanel getDetailsPanel() {
		scrollPanel.setVisible(true);
		return detailsPanel;
	}

	public void setDetailPanel(boolean isDetailPlace) {

		splitLayoutPanel.setWidgetSize(westPanel, Integer.parseInt(constants.widthSize()) - Integer.parseInt(constants.widthMin()) );
		splitLayoutPanel.animate(Integer.parseInt(constants.animationTime()));	
//		widthSize = 1200;
//		decreaseSize = 0;
//		splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		/*if (isDetailPlace) {

			timer = new Timer() {
				@Override
				public void run() {
					if (decreaseSize <= 705) {
						splitLayoutPanel.setWidgetSize(westPanel, 1225
								- decreaseSize);
						decreaseSize += 5;
					} else {
						timer.cancel();
					}
				}
			};
			timer.schedule(1);
			timer.scheduleRepeating(1);

		} else {
			widthSize = Integer.parseInt(constants.widthSize());
			decreaseSize = 0;
			splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		}*/
	}
	

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	// by spec
		@Override
		public void onRecordChange(RecordChangeEvent event) {
			int pagesize = 0;

			if (event.getRecordValue() == "ALL") {
				pagesize = table.getRowCount();
				OsMaConstant.TABLE_PAGE_SIZE = pagesize;
			} else {
				pagesize = Integer.parseInt(event.getRecordValue());
			}

			table.setPageSize(pagesize);
		}
		// by spec
}
