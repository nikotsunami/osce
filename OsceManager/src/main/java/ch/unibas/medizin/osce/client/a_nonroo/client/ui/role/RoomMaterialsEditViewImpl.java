package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.PriceType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsEditViewImpl extends Composite implements
		RoomMaterialsEditView, Editor<MaterialListProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	// private static EditView instance;
	private OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField
	SpanElement title;

	@UiField
	SpanElement labelName;

	@UiField
	SpanElement labelType;
	@UiField
	SpanElement labelPrice;
	@UiField
	SpanElement labelPriceType;

	@UiField
	TextBox name;

	@UiField(provided = true)
	FocusableValueListBox<MaterialType> type = new FocusableValueListBox<MaterialType>(
			new EnumRenderer<MaterialType>());

	@UiField
	IntegerBox price;

	@UiField(provided = true)
	FocusableValueListBox<PriceType> priceType = new FocusableValueListBox<PriceType>(
			new EnumRenderer<PriceType>());

	@UiField
	IconButton cancel;
	@UiField
	IconButton save;
	@UiField
	DivElement errors;

	@UiField
	TabPanel materialListPanel;

	private Delegate delegate;
	private Presenter presenter;

	
	// Violation Changes Highlight
			Map<String, Widget> editViewMap;
		// E Violation Changes Highlight
			
	public RoomMaterialsEditViewImpl() {

		initWidget(BINDER.createAndBindUi(this));

		priceType.setAcceptableValues(java.util.Arrays.asList(PriceType
				.values()));

		type.setAcceptableValues(java.util.Arrays.asList(MaterialType.values()));

		TabPanelHelper.moveTabBarToBottom(materialListPanel);

		// DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy");
		// birthday.setFormat(new DateBox.DefaultFormat(fmt));

		cancel.setText(constants.cancel());
		save.setText(constants.save());

		setTabTexts();
		setLabelTexts();

		name.setFocus(true);
		name.selectAll();
		
		// Violation Changes Highlight
					editViewMap=new HashMap<String, Widget>();
					editViewMap.put("name",name);
					editViewMap.put("type",type );
					editViewMap.put("price", price);
					editViewMap.put("priceType", priceType);
					
				// E Violation Changes Highlight

	}

	private List<Integer> getIntegerList(int minValue, int maxValue) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = minValue; i <= maxValue; i++) {
			values.add(new Integer(i));
		}
		return values;
	}

	private void createFocusHandlers() {
		type.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				// type.sele
			}
		});
		name.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				name.selectAll();
			}
		});
		price.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				price.selectAll();
			}
		});

	}

	private void createKeyHandlers() {
		name.addKeyUpHandler(new KeyUpHandler() {
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
					type.setFocus(true);
			}
		});

	}

	private void setTabTexts() {
		materialListPanel.getTabBar().setTabText(0, constants.roomMaterials());
		materialListPanel.selectTab(0);
		name.setFocus(true);
		name.selectAll();

	}

	private void setLabelTexts() {
		labelType.setInnerText(constants.type() + ":");
		labelName.setInnerText(constants.roomMaterialName() + ":");
		labelPrice.setInnerText(constants.roomMaterialPrice() + ":");
		labelPriceType.setInnerText(constants.roomMaterialPriceType() + ":");
	}

	@Override
	public RequestFactoryEditorDriver<MaterialListProxy, RoomMaterialsEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<MaterialListProxy, RoomMaterialsEditViewImpl> driver = GWT
				.create(Driver.class);
		driver.initialize(this);
		return driver;
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

	interface Binder extends UiBinder<Widget, RoomMaterialsEditViewImpl> {
	}

	interface Driver
			extends
			RequestFactoryEditorDriver<MaterialListProxy, RoomMaterialsEditViewImpl> {
	}

	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			title.setInnerText(constants.editMaterial());
		} else {
			title.setInnerText(constants.roomMaterials());
		}
	}

	@Override
	public void setPresenter(Presenter roomMaterialEditActivity) {
		this.presenter = roomMaterialEditActivity;
	}

	public void setRoomDetails(MaterialListProxy materialListProxy) {
		this.name.setValue(materialListProxy.getName());
		// this.type.setValue(materialListProxy.getType().name().toString());
		this.price.setValue(materialListProxy.getPrice());
		// this.price.setValue(materialListProxy.getPrice().toString());
		// this.price.setValue(materialListProxy.getPrice().toString());

	}

	@Override
	public String getName() {

		return name.getValue();
	}

	@Override
	public MaterialType getType() {

		return type.getValue();
	}

	@Override
	public Integer getPrice() {

		return price.getValue();
	}

	@Override
	public PriceType getPriceType() {

		return priceType.getValue();
	}

	// Violation Changes Highlight

		@Override
		public Map getEditViewMap() {
			// TODO Auto-generated method stub
			return this.editViewMap;
		}
		
		// E Violation Changes Highlight
}
