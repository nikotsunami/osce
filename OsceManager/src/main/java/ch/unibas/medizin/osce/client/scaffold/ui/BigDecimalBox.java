package ch.unibas.medizin.osce.client.scaffold.ui;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ValueBox;

/**
 * A ValueBox that uses {@link BigDecimalParser} and {@link BigDecimalRenderer}.
 */
public class BigDecimalBox extends ValueBox<BigDecimal> {

	public BigDecimalBox() {
		super(Document.get().createTextInputElement(), BigDecimalRenderer.instance(), BigDecimalParser.instance());
	}
}