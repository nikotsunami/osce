package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;

import com.google.gwt.user.cellview.client.CellTable;

public interface RoleKeywordSubView {
	
	interface Delegate {

		void addKeywordClicked();
		public void performKeywordSearch();
		public void deleteKeywordClicked(KeywordProxy keywordProxy);		
	}
	public void setDelegate(Delegate delegate);
	public void setKeywordAutocompleteValue(List<KeywordProxy> values);
	CellTable<KeywordProxy> getKeywordTable();

	// Highlight onViolation
	public Map getKeywordMap();
	// E Highlight onViolation
}
