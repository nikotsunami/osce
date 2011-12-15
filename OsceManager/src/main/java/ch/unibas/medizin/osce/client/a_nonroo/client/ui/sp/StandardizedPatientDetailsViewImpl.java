/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.interfaces.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.interfaces.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LabelBase;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Node;

/**
 * @author niko2
 *
 */
public class StandardizedPatientDetailsViewImpl extends Composite implements  StandardizedPatientDetailsView{

	private static StandardizedPatientDetailsViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientDetailsViewImplUiBinder.class);

	interface StandardizedPatientDetailsViewImplUiBinder extends UiBinder<Widget, StandardizedPatientDetailsViewImpl> {
		
	}

	private Presenter presenter;
	StandardizedPatientProxy proxy;

	// Panels
	@UiField
	TabPanel patientPanel;
	@UiField
	TabPanel scarAnamnesisPanel;

	// SubViews
	@UiField 
	StandardizedPatientScarSubViewImpl standardizedPatientScarSubViewImpl;
	@UiField
	StandardizedPatientAnamnesisSubViewImpl standardizedPatientAnamneisSubViewImpl;
	
	// CellTable (for Langskills)
	@UiField (provided = true)
	public CellTable<LangSkillProxy>langTable;
	@UiField (provided = true)
	public SimplePager pager;
	private List<AbstractEditableCell<?, ?>> editableCells;
	protected Set<String> paths = new HashSet<String>();

	// Buttons
	@UiField
	IconButton edit;
	@UiField
	IconButton delete;
	@UiField
	IconButton maps;
	@UiField
	IconButton langSkillAddButton;
	
	// Labels (Fieldnames)
	@UiField
	SpanElement labelStreet;
	@UiField
	SpanElement labelCity;
	@UiField
	SpanElement labelPLZ;
	@UiField
	SpanElement labelTelephone;
	@UiField
	SpanElement labelMobile;
	@UiField
	SpanElement labelEmail;
	@UiField
	SpanElement labelBankName;
	@UiField
	SpanElement labelBankIBAN;
	@UiField
	SpanElement labelBankBIC;
	@UiField
	SpanElement labelBirthdate;
	@UiField
	SpanElement labelGender;
	@UiField
	SpanElement labelHeight;
	@UiField
	SpanElement labelWeight;
	@UiField
	SpanElement labelNationality;
	@UiField
	SpanElement labelProfession;

	// Fields
	@UiField
	SpanElement gender;
	@UiField
	SpanElement street;
	@UiField
	SpanElement city;
	@UiField
	SpanElement postalCode;
	@UiField
	SpanElement telephone;
	@UiField
	SpanElement mobile;
	@UiField
	SpanElement birthday;
	@UiField
	SpanElement height;
	@UiField
	SpanElement weight;
	@UiField
	Anchor email;
	@UiField
	SpanElement nationality;
	@UiField
	SpanElement profession;
//	@UiField
//	SpanElement langskills;
	@UiField
	SpanElement description;
	@UiField
	SpanElement displayRenderer;
	@UiField
	SpanElement bankIBAN;
	@UiField
	SpanElement bankName;
	@UiField
	SpanElement bankBIC;

	private Delegate delegate;
	
	@UiField(provided = true)
    ValueListBox<LangSkillProxy> languageBox = new ValueListBox<LangSkillProxy>(new AbstractRenderer<LangSkillProxy>() {

        public String render(LangSkillProxy obj) {
            return obj == null ? "" : String.valueOf(obj.getSpokenlanguage());
        }
    });
	
	@UiField(provided = true)
    ValueListBox<LangSkillProxy> langSkillBox = new ValueListBox<LangSkillProxy>(new AbstractRenderer<LangSkillProxy>() {

        public String render(LangSkillProxy obj) {
            return obj == null ? "" : String.valueOf(obj.getSkill());
        }
    });

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
	public StandardizedPatientDetailsViewImpl() {
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		langTable = new CellTable<LangSkillProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
		initTable();
		
		patientPanel.selectTab(0);
		scarAnamnesisPanel.selectTab(0);
		
		Node tabTable = patientPanel.getElement().getFirstChild();
		Node contentPanel = tabTable.getLastChild();
		tabTable.removeChild(contentPanel);
		tabTable.insertFirst(contentPanel);
		
		edit.setText(Messages.EDIT);
		delete.setText(Messages.DELETE);
		maps.setText(Messages.GOOGLE_MAPS);
		langSkillAddButton.setText(Messages.ADD_LANGSKILL);
		
		reorderTabs(patientPanel);
		reorderTabs(scarAnamnesisPanel);
		
		setTabTexts();
		setLabelTexts();
	}
	
	private void setTabTexts() {
		patientPanel.getTabBar().setTabText(0, Messages.CONTACT_INFO);
		patientPanel.getTabBar().setTabText(1, Messages.DETAILS);
		patientPanel.getTabBar().setTabText(2, Messages.LANGUAGE_SKILLS);
		patientPanel.getTabBar().setTabText(3, Messages.BANK_ACCOUNT);
		patientPanel.getTabBar().setTabText(4, Messages.DESCRIPTION);
		
		scarAnamnesisPanel.getTabBar().setTabText(0, Messages.TRAITS);
		scarAnamnesisPanel.getTabBar().setTabText(1, Messages.ANAMNESIS_VALUES);
	}
	
	/**
	 * Hack to reorder z-index layering of tabs in tabbar so that the leftmost tab is on top.
	 * @param panel
	 */
	private void reorderTabs(TabPanel panel) {
		Element tabBar = panel.getTabBar().getElement();
		int numChildren = panel.getTabBar().getTabCount();
		for (int i=0; i < numChildren; i++) {
			Element tab = (Element)tabBar.getChild(0).getChild(0).getChild(i+1).getChild(0);
			tab.setAttribute("style", tab.getAttribute("style") + "; z-index: " + (numChildren - i) + ";");
		}
	}
	
	private void setLabelTexts() {
		labelCity.setInnerText(Messages.CITY + ":");
		labelPLZ.setInnerText(Messages.PLZ + ":");
		labelEmail.setInnerText(Messages.EMAIL + ":");
		labelMobile.setInnerText(Messages.MOBILE + ":");
		labelStreet.setInnerText(Messages.STREET + ":");
		labelTelephone.setInnerText(Messages.TELEPHONE + ":");
		
		labelBankName.setInnerText(Messages.BANK_NAME + ":");
		labelBankIBAN.setInnerText(Messages.BANK_IBAN + ":");
		labelBankBIC.setInnerText(Messages.BANK_BIC + ":");
		
		labelBirthdate.setInnerText(Messages.BIRTHDAY + ":");
		labelGender.setInnerText(Messages.GENDER + ":");
		labelHeight.setInnerText(Messages.HEIGHT + ":");
		labelWeight.setInnerText(Messages.WEIGHT + ":");
		labelNationality.setInnerText(Messages.NATIONALITY + ":");
		labelProfession.setInnerText(Messages.PROFESSION + ":");
	}
	
	private void initTable() {
		langTable.addColumn(new TextColumn<LangSkillProxy>() {
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LangSkillProxy object) {
				return renderer.render(object.getSpokenlanguage().getLanguageName().toString());
			}
			
		}, Messages.LANGUAGES);

		langTable.addColumn(new TextColumn<LangSkillProxy>() {
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(LangSkillProxy object) {
				return renderer.render(object.getSkill().toString());
			}
		}, Messages.LANGUAGE_SKILLS);
		
		addColumn(new ActionCell<LangSkillProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<LangSkillProxy>() {
					public void execute(LangSkillProxy langSkill) {
						if(Window.confirm(Messages.REALLY_DELETE))
							delegate.deleteLangSkillClicked(langSkill);
					}
				}), "", new GetValue<LangSkillProxy>() {
					public LangSkillProxy getValue(LangSkillProxy skill) {
						return skill;
					}
		}, null);
		langTable.addColumnStyleName(2, "iconCol");
	}
	
	/**
	 * Add a column with a header.
	 *
	 * @param <C> the cell type
	 * @param cell the cell used to render the column
	 * @param headerText the header string
	 * @param getter the value getter for the cell
	 */
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<LangSkillProxy, C> fieldUpdater) {
		Column<LangSkillProxy, C> column = new Column<LangSkillProxy, C>(cell) {
			@Override
			public C getValue(LangSkillProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		langTable.addColumn(column, headerText);
	}
	
	private static interface GetValue<C> {
		C getValue(LangSkillProxy contact);
	}

	public void setValue(StandardizedPatientProxy proxy) {
		this.proxy = proxy;
		//version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
		gender.setInnerText(proxy.getGender() == null ? "" : String.valueOf(proxy.getGender()));
		street.setInnerText(proxy.getStreet() == null ? "" : String.valueOf(proxy.getStreet()));
		city.setInnerText(proxy.getCity() == null ? "" : String.valueOf(proxy.getCity()));
		postalCode.setInnerText(proxy.getPostalCode() == null ? "" : String.valueOf(proxy.getPostalCode()));
		telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
		mobile.setInnerText(proxy.getMobile() == null ? "" : String.valueOf(proxy.getMobile()));
		birthday.setInnerText(proxy.getBirthday() == null ? "" : DateTimeFormat.getFormat(Messages.DATE_TIME_FORMAT).format(proxy.getBirthday()));
		height.setInnerText(proxy.getHeight() == null ? "" : String.valueOf(proxy.getHeight()));
		weight.setInnerText(proxy.getWeight() == null ? "" : String.valueOf(proxy.getWeight()));
		
		email.setHref("mailto:" + (proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		email.setText((proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		
		nationality.setInnerText(proxy.getNationality() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance().render(proxy.getNationality()));
		profession.setInnerText(proxy.getProfession() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().render(proxy.getProfession()));
//		langskills.setInnerText(proxy.getLangskills() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer.instance()).render(proxy.getLangskills()));
		
//		Set<LangSkillProxy> langSkillSet = proxy.getLangskills();
		
		description.setInnerText(proxy.getDescriptions() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer.instance().render(proxy.getDescriptions()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer.instance().render(proxy));
		
		BankaccountProxy bank = proxy.getBankAccount();
		bankName.setInnerText(bank == null ? "" : String.valueOf(bank.getBankName()));
		bankIBAN.setInnerText(bank == null ? "" : String.valueOf(bank.getIBAN()));
		bankBIC.setInnerText(bank == null ? "" : String.valueOf(bank.getBIC()));
	}
	
	private String createGoogleMapsLink(StandardizedPatientProxy proxy) {
		return "http://maps.google.com/maps?q=" + proxy.getStreet() + ",+" + proxy.getPostalCode() + "+" + proxy.getCity();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter =  StandardizedPatientActivity;
	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public StandardizedPatientProxy getValue() {
		return proxy;
	}
	
	@UiHandler("maps")
	public void onMapsClicked(ClickEvent e) {
		Window.open(createGoogleMapsLink(this.proxy), "_blank", "");
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}
	
	@UiHandler("langSkillAddButton")
	public void langSkillAddButtonClicked(ClickEvent e) {
		delegate.addLangSkillClicked();
	}

	@Override
	public ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubViewImpl getStandardizedPatientAnamnesisSubViewImpl() {
		return standardizedPatientAnamneisSubViewImpl;
	}

	@Override
	public ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubViewImpl getStandardizedPatientScarSubViewImpl() {
		return standardizedPatientScarSubViewImpl;
	}

	@Override
	public CellTable<LangSkillProxy> getLangSkillTable() {
		return langTable;
	}

	@Override
	public ValueListBox<LangSkillProxy> getLanguageBox() {
		return languageBox;
	}

	@Override
	public ValueListBox<LangSkillProxy> getLangSkillBox() {
		return langSkillBox;
	}
}
