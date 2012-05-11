/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;


import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubViewImpl;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nikotsunami
 *
 */
public class StandardizedPatientViewImpl extends Composite implements  StandardizedPatientView {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, StandardizedPatientViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	@UiField (provided = true)
	public QuickSearchBox searchBox;
	@UiField
	public IconButton filterButton;
	@UiField
	public IconButton refreshButton;
	@UiField
	public IconButton newButton;
	@UiField
	public IconButton exportButton;
	@UiField
	public SimplePanel detailsPanel;
	@UiField(provided = true)
	public SimplePager pager;
	@UiField(provided = true)
	public CellTable<StandardizedPatientProxy> table;
	
	@UiField
	public StandartizedPatientAdvancedSearchSubViewImpl standartizedPatientAdvancedSearchSubViewImpl;
	
	public StandartizedPatientAdvancedSearchSubViewImpl getStandartizedPatientAdvancedSearchSubViewImpl() {
		return standartizedPatientAdvancedSearchSubViewImpl;
	}

	//By SPEC[Start
	//protected Set<String> paths = new HashSet<String>();
	protected ArrayList<String> paths = new ArrayList<String>();
	//By SPEC]End
	private StandardizedPatientFilterViewImpl filterPanel;
	private Presenter presenter;
	
	@UiHandler("exportButton")
	public void exportButtonClicked(ClickEvent event) {
		// TODO export action
	}
	
	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newClicked();
	}
	
	public String getQuery() {
		return searchBox.getValue();
	}
	
//	@UiHandler ("filterButton")
//	public void filterButtonClicked(ClickEvent event) {
//		showFilterPanel((Widget) event.getSource());
//	}
	
	@UiHandler("refreshButton")
	public void refreshButtonClicked(ClickEvent event) {
		delegate.performSearch(searchBox.getValue(), getSearchFilters());
	}
	
	@UiHandler("filterButton")
	public void filterButtonHover(MouseOverEvent event) {
		showFilterPanel((Widget) event.getSource());
	}
	
	private void showFilterPanel(Widget eventSource) {
		int x = eventSource.getAbsoluteLeft();
		int y = eventSource.getAbsoluteTop();
		filterPanel.setPopupPosition(x, y);
		filterPanel.show();
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
	public StandardizedPatientViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<StandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		filterPanel = new StandardizedPatientFilterViewImpl();
		filterPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (filterPanel.selectionChanged()) {
					filterPanel.clearSelectionChanged();
					delegate.performSearch(searchBox.getValue(), getSearchFilters());
				}
			}
			
		});
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue(), getSearchFilters());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addPatient());
		exportButton.setText(constants.export());
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
//    	paths.add("id");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//                public String render(java.lang.Long obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getId());
//            }
//        }, "Id");
//        paths.add("version");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//                public String render(java.lang.Integer obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getVersion());
//            }
//        }, "Version");
//        paths.add("gender");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getGender().toString());
//            }
//        }, "Gender");
		paths.add("name");
		table.addColumn(new TextColumn<StandardizedPatientProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
		paths.add("preName");
		table.addColumn(new TextColumn<StandardizedPatientProxy>() {
			
			{ this.setSortable(true); }	//By SPEC

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getPreName());
			}
		}, constants.preName());
		
/*        paths.add("street");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getStreet());
//            }
//        }, "Street");
//        paths.add("city");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getCity());
//            }
//        }, "City");
//        paths.add("postalCode");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//                public String render(java.lang.Integer obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getPostalCode());
//            }
//        }, "Postal Code");
//        paths.add("telephone");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getTelephone());
//            }
//        }, "Telephone");
//        paths.add("mobile");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getMobile());
//            }
//        }, "Mobile");
//        paths.add("birthday");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getBirthday());
//            }
//        }, "Birthday");
*/
		paths.add("email");
		table.addColumn(new TextColumn<StandardizedPatientProxy>() {

			{ this.setSortable(true); }	//By SPEC
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getEmail());
			}
		}, constants.email());
//        paths.add("nationality");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<ch.unibas.medizin.osce.client.managed.request.NationalityProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance();
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getNationality());
//            }
//        }, "Nationality");
//        paths.add("profession");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<ch.unibas.medizin.osce.client.managed.request.ProfessionProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance();
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getProfession());
//            }
//        }, "Profession");
//        paths.add("langskills");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer.instance());
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getLangskills());
//            }
//        }, "Langskills");
//        paths.add("bankAccount");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<ch.unibas.medizin.osce.client.managed.request.BankaccountProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer.instance();
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getBankAccount());
//            }
//        }, "Bank Account");
//        paths.add("descriptions");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<DescriptionProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer.instance();
//
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getDescriptions());
//            }
//        }, "Descriptions");
//        paths.add("anamnesisForm");
//        table.addColumn(new TextColumn<StandardizedPatientProxy>() {
//
//            Renderer<AnamnesisFormProxy> renderer = ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer.instance();
//            @Override
//            public String getValue(StandardizedPatientProxy object) {
//                return renderer.render(object.getAnamnesisForm());
//            }
//        }, "Anamnesis Form");
	}
	
	public List<String> getSearchFilters() {
		return filterPanel.getFilters();
	}
	
	public SearchCriteria getCriteria() {
		return new SearchCriteria();
//		return filterPanel.getCriteria();
	}

	@Override
	public CellTable<StandardizedPatientProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void updateSearch() {
		String q = searchBox.getValue();
		delegate.performSearch(q, getSearchFilters());
	}

	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IconButton getExportButton() {
		return exportButton;
	}
	
	@Override public QuickSearchBox getSearchBox() {
		return searchBox;
	}

}
