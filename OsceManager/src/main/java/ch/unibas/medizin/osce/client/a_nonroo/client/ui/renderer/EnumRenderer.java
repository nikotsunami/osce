package ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;

import ch.unibas.medizin.osce.client.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.shared.Comparison;

public class EnumRenderer<T extends Enum<?>> extends AbstractRenderer<T>{
	protected final OsceConstantsWithLookup constants = GWT.create(OsceConstantsWithLookup.class);
	private final Type rendererType;
	
	public static enum Type {
		DEFAULT, NUMERIC, LANGSKILL, ANAMNESIS, NATIONALITY, SCAR
	}
	
	public EnumRenderer() {
		this(Type.DEFAULT);
	}
	
	public EnumRenderer(Type rendererType) {
		this.rendererType = rendererType;
	}
	
	protected String getIdentifier(T object) {
		if (!(object instanceof Comparison ) || rendererType == Type.DEFAULT) {
			return object.name();
		} else {
			return rendererType.name() + "_" + object.name();
		}
	}

	@Override
	public String render(T object) {
		if (object == null) return "";
		return constants.getString(getIdentifier(object));
	}
	
}
