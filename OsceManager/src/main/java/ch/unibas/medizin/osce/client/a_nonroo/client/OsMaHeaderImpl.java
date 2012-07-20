package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.Arrays;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.style.resources.UiStyles;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.Locale;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class OsMaHeaderImpl extends Composite implements OsMaHeader {

	private static OsMaHeaderUiBinder uiBinder = GWT.create(OsMaHeaderUiBinder.class);
	private OsceConstants constants = GWT.create(OsceConstants.class);

	interface OsMaHeaderUiBinder extends UiBinder<Widget, OsMaHeaderImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	UiStyles uiStyles;
	
	@UiField
	IconButton logoutButton;
	
	@UiField
	Label selectLanguageLabel;
	
	@UiField
	Label recordView;
	
	@UiField (provided = true)
	ValueListBox<String> selectRecordBox= new ValueListBox<String>(new AbstractRenderer<String>() {
		@Override
		public String render(String object) {
			return object;
		}
	});
	
	@UiField (provided = true)
	ValueListBox<Locale> selectLanguageBox = new ValueListBox<Locale>(new AbstractRenderer<Locale>() {
		// Note: this is not an EnumRenderer bc. translations of language names would be futile.
		@Override
		public String render(Locale locale) {
			return locale.getLanguageName();
		}
	});

	public OsMaHeaderImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		Log.info("getHostPageBaseURL(): " + GWT.getHostPageBaseURL());
		Log.info("getModuleBaseURL(): " + GWT.getModuleBaseURL());
		Log.info("getModuleName(): " + GWT.getModuleName());
		Log.info("getPermutationStrongName(): " + GWT.getPermutationStrongName());
		logoutButton.setText(constants.logout());
		selectLanguageLabel.setText(constants.selectLanguage());
		recordView.setText(constants.tableSize());
		
		Locale[] locales = Locale.values();
		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
		
		for (Locale locale: locales) {
			if (currentLocale.equals(locale.toString())) {
				selectLanguageBox.setValue(locale);
			}
		}
		
		selectLanguageBox.setAcceptableValues(Arrays.asList(locales));
		selectLanguageBox.addValueChangeHandler(new ValueChangeHandler<Locale>() {

			@Override
			public void onValueChange(ValueChangeEvent<Locale> event) {
				delegate.changeLocale(selectLanguageBox.getValue());
			}
		});
		
		fillRecordBoxValue();
		
		checkCookies();
				
		selectRecordBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				delegate.changeRecordValue(selectRecordBox.getValue());			
			}
		});
				
		uiStyles.uiCss().ensureInjected();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public void checkCookies()
	{
		String temp = Cookies.getCookie("user");
		
		if (temp == null)
		{
			Log.info("Value is null");
		}	
		else
		{
			OsMaConstant.TABLE_PAGE_SIZE = Integer.parseInt(temp);
			selectRecordBox.setValue(temp);
		}
	}

	public void fillRecordBoxValue()
	{
		selectRecordBox.setAcceptableValues(Arrays.asList("5","10","20","30","50","100","ALL"));
		
		/*selectRecordBox.setValue("5");
		selectRecordBox.setValue("10");
		selectRecordBox.setValue("20");
		selectRecordBox.setValue("30");
		selectRecordBox.setValue("50");
		selectRecordBox.setValue("100");
		selectRecordBox.setValue("ALL");
		selectRecordBox.setValue("");*/
	}
}
