package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class BreadCrumbNavigation extends Composite {
	private final HorizontalPanel panel;
//	ValueListBox<T>
	
	public class BreadCrumb implements IsWidget {
		
//		public BreadCrumb

		@Override
		public Widget asWidget() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public BreadCrumbNavigation() {
		panel = new HorizontalPanel();
		initWidget(panel);
	}
}
