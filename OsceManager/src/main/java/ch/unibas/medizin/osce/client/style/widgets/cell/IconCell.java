package ch.unibas.medizin.osce.client.style.widgets.cell;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Class used to render Icons into Cells based on numeric values. When calling the constructor,
 * an array of possible icons (either strings representing the jQueryUI icon class names or 
 * image resources). This is perfect for e.g. representing ENUMS as Icons.
 * @author mwagner
 *
 */
public class IconCell extends AbstractCell<Integer> {
	private String[] _descriptions;
	private String[] _iconDescriptors;
	private ImageResource[] _icons;
	private static Templates templates = GWT.create(Templates.class);
	
	public interface Templates extends SafeHtmlTemplates {
		// TODO ... implement for imageResource
		@SafeHtmlTemplates.Template("<span class=\"ui-icon ui-icon-{0}\"></span>")
		SafeHtml jqueryIcon(String iconDescriptor);
		
		@SafeHtmlTemplates.Template("<span class=\"ui-icon ui-icon-{0}\" title=\"{1}\"></span>")
		SafeHtml jqueryIcon(String iconDescriptor, String description);
	}
	
	public IconCell(String[] iconDescriptors) {
		this(iconDescriptors, null);
	}
	
	public IconCell(String[] iconDescriptors, String[] descriptions) {
		_iconDescriptors = iconDescriptors;
		_descriptions = descriptions;
	}
	
	public IconCell(ImageResource[] icons) {
		// TODO: implement for image resource
		_icons = icons;
	}
	
	@Override
	public void render(Cell.Context context, Integer value, SafeHtmlBuilder sb) {
		if (value == null) {
			// FIXME: really? Maeby jsut display sum diffrunt icon?
			return;
		}
		
		if (value < 0) {
			Log.warn("Value to represent smaller than zero!");
			value = 0;
		}
		
		SafeHtml renderedCell;
		if (_iconDescriptors == null) {
			renderedCell = renderImageResources(value);
		} else {
			renderedCell = renderJqueryIcons(value);
		}
		
		sb.append(renderedCell);
	}
	
	private SafeHtml renderJqueryIcons(Integer value) {
		if (value >= _iconDescriptors.length) {
			Log.warn("Value to represent larger than available icon numbers");
			value = _iconDescriptors.length - 1;
		}
		
		if (value >= _descriptions.length) {
			return templates.jqueryIcon(_iconDescriptors[value]);
		}
		return templates.jqueryIcon(_iconDescriptors[value], _descriptions[value]);
	}

	private SafeHtml renderImageResources(Integer value) {
		return null;
	}
	
}
