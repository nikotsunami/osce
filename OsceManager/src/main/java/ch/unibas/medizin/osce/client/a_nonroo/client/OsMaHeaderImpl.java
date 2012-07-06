package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.Arrays;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;
import ch.unibas.medizin.osce.client.style.resources.UiStyles;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.Locale;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
	IconButton messages;
	
	@UiField
	Label selectLanguageLabel;
	
	@UiField (provided = true)
	ValueListBox<Locale> selectLanguageBox = new ValueListBox<Locale>(new AbstractRenderer<Locale>() {
		// Note: this is not an EnumRenderer bc. translations of language names would be futile.
		@Override
		public String render(Locale locale) {
			return locale.getLanguageName();
		}
	});
	
	@UiHandler("messages")
	public void showMessages(ClickEvent event) {
		delegate.showMessages(messages);
	}

	public OsMaHeaderImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		Log.info("getHostPageBaseURL(): " + GWT.getHostPageBaseURL());
		Log.info("getModuleBaseURL(): " + GWT.getModuleBaseURL());
		Log.info("getModuleName(): " + GWT.getModuleName());
		Log.info("getPermutationStrongName(): " + GWT.getPermutationStrongName());
		
		// message button...
		messages.setText(constants.logout());
		messages.setHTML(new SafeHtmlBuilder().appendHtmlConstant(constants.messages() + " (0)").toSafeHtml());
		
		selectLanguageLabel.setText(constants.selectLanguage());
		
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
		uiStyles.uiCss().ensureInjected();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

}
