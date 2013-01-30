/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nikotsunami
 *
 */
public class StandardizedPatientViewImpl extends Composite implements  StandardizedPatientView, RecordChangeHandler, MenuClickHandler {

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
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	ScrollPanel mainScrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	@UiField
	public SimplePanel detailsPanel;
	@UiField(provided = true)
	public SimplePager pager;
	
	
	Map<String, String> columnName=new HashMap<String, String>();
	List<String> columnNameorder = new ArrayList<String>();

	
	/*custom celltable start code*/
	

	/*@UiField(provided = true)
	public CellTable<StandardizedPatientProxy> table;
	*/
	@UiField(provided = true)
	public AdvanceCellTable<StandardizedPatientProxy> table;
	
	
	
	/*custom celltable end code*/
	
	@UiField
	public StandartizedPatientAdvancedSearchSubViewImpl standartizedPatientAdvancedSearchSubViewImpl;
	
	public StandartizedPatientAdvancedSearchSubViewImpl getStandartizedPatientAdvancedSearchSubViewImpl() {
		return standartizedPatientAdvancedSearchSubViewImpl;
	}

	//By SPEC[Start
	//protected Set<String> paths = new HashSet<String>();
	/*custom celltable start code*/
	
	/*protected ArrayList<String> paths = new ArrayList<String>();*/
	public List<String> paths =new ArrayList<String>();
	/*custom celltable end code*/
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
		
/*		custom celltable start code*/
		

		/*CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<StandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		*/
		/*CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<StandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);*/
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<StandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		/*custom celltable end code*/
		
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

/*		custom celltable start code*/
		
	/*public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	*/
	public List<String> getPaths() {
		

		return paths;
	}
	/*custom celltable end code*/
	public void init() {

		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		
//		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
		
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
		
		
		//paths.add("name");
		//paths.add(" ");
		columnName.put(constants.name(), "name");
		columnNameorder.add(constants.name());
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
		
		
	//	paths.add("preName");
	//	paths.add(" ");
		columnName.put(constants.preName(), "preName");
		columnNameorder.add(constants.preName());
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
		
		//paths.add("email");
		//paths.add(" ");
		columnName.put(constants.email(), "email");
		columnNameorder.add(constants.email());
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
		}, constants.email(),false,true);
		
		/*custom celltable start code*/
		
		
		//paths.add("street");
		//paths.add(" ");
		columnName.put(constants.street(), "street");
		columnNameorder.add(constants.street());
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
		}, constants.street(),false);
		
		
		//paths.add("city");
		//paths.add(" ");
		columnName.put(constants.city(), "city");
		columnNameorder.add(constants.city());
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
		}, constants.city(),false);
		
		
		
		//paths.add("telephone");
		//paths.add(" ");
		columnName.put(constants.telephone(), "telephone");
		columnNameorder.add(constants.telephone());
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
		}, constants.telephone(),false);
		
		
		//paths.add("height");
		//paths.add(" ");
		columnName.put(constants.height(), "height");
		columnNameorder.add(constants.height());
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
		}, constants.height(),false);
		
		
		//paths.add("weight");
		//paths.add(" ");
		columnName.put(constants.weight(), "weight");
		columnNameorder.add(constants.weight());
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
		}, constants.weight(),false);
/*		custom celltable end code*/

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
//		if (scrollPanel == null || detailsPanel == null) {
//			scrollPanel = new ScrollPanel();
//			detailsPanel = new SimplePanel();
//			scrollPanel.add(detailsPanel);
//			splitLayoutPanel.addEast(scrollPanel, 400);
//			splitLayoutPanel.animate(20);
//		}		
		scrollPanel.setVisible(true);
		return detailsPanel;
	}

	
	public void setDetailPanel(boolean isDetailPlace) {

		if (isDetailPlace) {
			
//			int width = splitLayoutPanel.getWidget(0).getOffsetWidth();
//			int minWidth = width/2;
//			
//			Log.info("(splitLayoutPanel.getOffsetWidth()/2) == ="+(splitLayoutPanel.getOffsetWidth()/2));
//			Log.info("minWidth == ="+minWidth);
//			Log.info("width == ="+width);
//			if((width - (splitLayoutPanel.getOffsetWidth()/2)) > 100)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),width - minWidth);
			
			ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
			
			delegate.doAnimation(true);
			splitLayoutPanel.animate(OsMaConstant.ANIMATION_TIME,
					new AnimationCallback() {
						
						@Override
						public void onLayout(Layer layer, double progress) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationComplete() {
							
						}
					});
			delegate.doAnimation(false);
		} else {
			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),
					OsMaConstant.WIDTH_SIZE);
//			splitLayoutPanel
//			.animate(OsMaConstant.ANIMATION_TIME);
		}
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
			widthSize = 1225;
			decreaseSize = 0;
			splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		}*/
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

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
			OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		int right = Window.getClientWidth();
//		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		Log.info("Width For Split === = =="+splitLayoutPanel.getElement().getParentElement().getOffsetWidth());
//		splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),right - left);
		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,false);
		
//		splitLayoutPanel.getWidget(0).getElement().setAttribute("style", "position:relative;width: 100%");
//		splitLayoutPanel.getElement().getFirstChildElement().getNextSiblingElement().setAttribute("style", "position:relative;width: 100%");
//		splitLayoutPanel.getElement().getFirstChildElement().getNextSiblingElement().getFirstChildElement().setAttribute("style", "position:relative;width: 100%");
		
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;		
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		int openSplitSize = Window.getClientWidth() - (Window.getClientWidth()/6);
//		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= openSplitSize){
//			Log.info("Window.getClientWidth() == "+Window.getClientWidth());
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), Window.getClientWidth()- (Window.getClientWidth()/50));
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), Window.getClientWidth() - (Window.getClientWidth()/6));
//		}
	}

	@Override
	public Map getSortMap() {
		// TODO Auto-generated method stub
		return columnName;
	}
	
	@Override
	public List<String> getColumnSortSet()
	{
		return columnNameorder;
	}
	
	
}
