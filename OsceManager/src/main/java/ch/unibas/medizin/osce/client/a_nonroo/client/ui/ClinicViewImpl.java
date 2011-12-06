/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.interfaces.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.interfaces.MySimplePagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nikotsunami
 *
 */
public class ClinicViewImpl extends Composite implements  ClinicView {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, ClinicViewImpl> {
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

	@UiField (provided = true)
	CellTable<ClinicProxy> table;

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
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), 400);
		newButton.setText(Messages.ADD_CLINIC);
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
		}, "Name");
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
		}, "City");
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
		return detailsPanel;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
