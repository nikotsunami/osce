

package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.ui.DoctorSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillSetEditor;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.Gender;
//import ch.unibas.medizin.osce.client.shared.Gender;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.google.gwt.user.datepicker.client.DateBox;

public class StandardizedPatientEditViewImpl extends Composite implements StandardizedPatientEditView, Editor<StandardizedPatientProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static StandardizedPatientEditView instance;
	
	@UiField
	TabPanel patientPanel;
//	@UiField
//	Element editTitle;
//	@UiField
//	Element createTitle;
	@UiField
	SpanElement title;

	@UiField(provided = true)
	FocusableValueListBox<Gender> gender = new FocusableValueListBox<Gender>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Gender>() {

		public String render(ch.unibas.medizin.osce.shared.Gender obj) {
			return obj == null ? "" : String.valueOf(obj);
		}
	});

	@UiField
	TextBox name;
	@UiField
	TextBox preName;
	@UiField
	TextBox street;
	@UiField
	TextBox city;
	@UiField
	IntegerBox postalCode;
	@UiField
	TextBox telephone;
	@UiField
	TextBox telephone2;
	@UiField
	TextBox mobile;
	@UiField
	DateBox birthday;
	@UiField
	IntegerBox height;
	@UiField
	IntegerBox weight;
	@UiField
	TextBox email;

	@UiField(provided = true)
	FocusableValueListBox<NationalityProxy> nationality = new FocusableValueListBox<NationalityProxy>(ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.NationalityProxy>());
	@UiField(provided = true)
	FocusableValueListBox<ProfessionProxy> profession = new FocusableValueListBox<ProfessionProxy>(ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.ProfessionProxy>());

	@UiField
	SimplePanel descriptionPanel;
	
	@UiField
	SimplePanel bankEditPanel;

	// Labels (Fieldnames)
	@UiField
	SpanElement labelName;
	@UiField
	SpanElement labelPreName;
	@UiField
	SpanElement labelStreet;
	@UiField
	SpanElement labelPLZCity;
	@UiField
	SpanElement labelTelephone;
	@UiField
	SpanElement labelTelephone2;
	@UiField
	SpanElement labelMobile;
	@UiField
	SpanElement labelEmail;
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

	@UiField
	IconButton cancel;
	@UiField
	IconButton save;
	@UiField
	DivElement errors;

	private Delegate delegate;
	private Presenter presenter;


	public StandardizedPatientEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		gender.setAcceptableValues(Arrays.asList(Gender.values()));
		
		TabPanelHelper.moveTabBarToBottom(patientPanel);
		
		cancel.setText(Messages.CANCEL);
		save.setText(Messages.SAVE);
		
		patientPanel.getTabBar().setTabText(0, Messages.CONTACT_INFO);
		patientPanel.getTabBar().setTabText(1, Messages.DETAILS);
		patientPanel.getTabBar().setTabText(2, Messages.BANK_ACCOUNT);
		patientPanel.getTabBar().setTabText(3, Messages.DESCRIPTION);
		
		patientPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				switch(event.getSelectedItem().intValue()) {
				case 0:
					preName.setFocus(true);
					break;
				case 1:
					birthday.setFocus(true);
					break;
				case 2:
//					bankName.setFocus(true);
					break;
				case 3:
//					description.setFocus(true);
					break;
				default:
				}
			}
			
		});

		setTabTexts();
		setLabelTexts();
		
		createKeyHandlers();
		createFocusHandlers();
		
		patientPanel.selectTab(0);
		preName.setFocus(true);
		preName.selectAll();
	}
	
	private void createFocusHandlers() {
		preName.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				preName.selectAll();
			}
		});
		name.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				name.selectAll();
			}
		});
		street.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				street.selectAll();
			}
		});
		postalCode.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				postalCode.selectAll();
			}
		});
		city.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				city.selectAll();
			}
		});
		telephone.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				telephone.selectAll();
			}
		});
		telephone2.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				telephone2.selectAll();
			}
		});
		mobile.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				mobile.selectAll();
			}
		});
		email.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				email.selectAll();
			}
		});
		telephone2.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				telephone2.selectAll();
			}
		});
		
		// what about birthday?
		
		height.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				height.selectAll();
			}
		});
		weight.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				weight.selectAll();
			}
		});
	}
	
	private void createKeyHandlers() {		
		preName.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					name.setFocus(true);
					if (event.isControlKeyDown()) {
						delegate.saveClicked();
					}
				} 
			}
		});
		name.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					street.setFocus(true);
			}
		});
		street.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					postalCode.setFocus(true);
			}
		});
		postalCode.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					city.setFocus(true);
			}
		});
		city.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					telephone.setFocus(true);
			}
		});
		telephone.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					mobile.setFocus(true);
			}
		});
		mobile.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					email.setFocus(true);
			}	
		});
		email.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					telephone2.setFocus(true);
			}
		});
		telephone2.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB)
					patientPanel.selectTab(1);
			}
		});
		birthday.addHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					gender.setFocus(true);
				}
			}
		}, KeyUpEvent.getType());
	}

	private void setTabTexts() {
		patientPanel.getTabBar().setTabText(0, Messages.CONTACT_INFO);
		patientPanel.getTabBar().setTabText(1, Messages.DETAILS);
		patientPanel.getTabBar().setTabText(2, Messages.BANK_ACCOUNT);
		patientPanel.getTabBar().setTabText(3, Messages.DESCRIPTION);
	}
	
	private void setLabelTexts() {
		labelName.setInnerText(Messages.NAME + ":");
		labelPreName.setInnerText(Messages.PRENAME + ":");
		labelPLZCity.setInnerText(Messages.PLZCITY + ":");
		labelEmail.setInnerText(Messages.EMAIL + ":");
		labelMobile.setInnerText(Messages.MOBILE + ":");
		labelStreet.setInnerText(Messages.STREET + ":");
		labelTelephone.setInnerText(Messages.TELEPHONE + ":");
		labelTelephone2.setInnerText(Messages.TELEPHONE + " 2:");
		
		labelBirthdate.setInnerText(Messages.BIRTHDAY + ":");
		labelGender.setInnerText(Messages.GENDER + ":");
		labelHeight.setInnerText(Messages.HEIGHT + ":");
		labelWeight.setInnerText(Messages.WEIGHT + ":");
		labelNationality.setInnerText(Messages.NATIONALITY + ":");
		labelProfession.setInnerText(Messages.PROFESSION + ":");
	}

	@Override
	public RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(Messages.SP_CREATE);
		} else {
			title.setInnerText(Messages.SP_EDIT);
		}
	}

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public void setEnabled(boolean enabled) {
		save.setEnabled(enabled);
	}

	public void showErrors(List<EditorError> errors) {
		SafeHtmlBuilder b = new SafeHtmlBuilder();
		for (EditorError error : errors) {
			b.appendEscaped(error.getPath()).appendEscaped(": ");
			b.appendEscaped(error.getMessage()).appendHtmlConstant("<br>");
		}
		this.errors.setInnerHTML(b.toSafeHtml().asString());
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		delegate.cancelClicked();
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, StandardizedPatientEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> {
	}

	@Override
	public void setEditTitle(boolean edit) {

		if (edit) {
			title.setInnerText(Messages.SP_EDIT);
		} else {
			title.setInnerText(Messages.SP_CREATE);
		}

	}

	@Override
	public void setPresenter(Presenter standardizedPatientEditActivity) {
		this.presenter=standardizedPatientEditActivity;
	}


	@Override
	public SimplePanel getDescriptionPanel() {
		return descriptionPanel;
	}
	
	@Override
	public SimplePanel getBankEditPanel() {
		return bankEditPanel;
	}


	@Override
	public void setNationalityPickerValues(Collection<NationalityProxy> values) {
		nationality.setAcceptableValues(values);		
	}


	@Override
	public void setProfessionPickerValues(Collection<ProfessionProxy> values) {
		profession.setAcceptableValues(values);		
	}


}
