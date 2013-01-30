package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class SuggestOracle<T> {
	private Response<T>	emptyResponse	= new Response<T>(new ArrayList<T>());
	protected AbstractSuggestBox<T, EventHandlingValueHolderItem<T>>	suggestBox;

	public interface Callback<T> {
		void onSuggestionsReady(Request request, Response<T> response);
	}

	public static class Request implements IsSerializable {
		private int			limit	= 20;
		private String	query;
		public Request() {
		}

		public Request(String query) {
			setQuery(query);
		}

		public Request(String query, int limit) {
			setQuery(query);
			setLimit(limit);
		}

		public int getLimit() {
			return limit;
		}

		public String getQuery() {
			return query;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public void setQuery(String query) {
			this.query = query;
		}
	}

	public static class Response<T> implements IsSerializable {
		private Collection<T>	suggestions;

		private boolean				moreSuggestions			= false;

		private int						numMoreSuggestions	= 0;

		public Response() {
		}

		public Response(Collection<T> suggestions) {
			setSuggestions(suggestions);
		}

		public int getMoreSuggestionsCount() {
			return this.numMoreSuggestions;
		}

		public Collection<T> getSuggestions() {
			return this.suggestions;
		}

		public boolean hasMoreSuggestions() {
			return this.moreSuggestions;
		}

		public void setMoreSuggestions(boolean moreSuggestions) {
			this.moreSuggestions = moreSuggestions;
		}

		public void setMoreSuggestionsCount(int count) {
			this.numMoreSuggestions = count;
			this.moreSuggestions = (count > 0);
		}

		public void setSuggestions(Collection<T> suggestions) {
			this.suggestions = suggestions;
		}
	}

	public SuggestOracle() {
		
		
	}

	public boolean isDisplayStringHTML() {
		return false;
	}

	public void requestDefaultSuggestions(Request request, Callback<T> callback) {
		assert (request.query == null);
		callback.onSuggestionsReady(request, emptyResponse);
	}

	public abstract void requestSuggestions(Request request, Callback<T> callback);

	public AbstractSuggestBox<T, EventHandlingValueHolderItem<T>> getSuggestBox() {
		return suggestBox;
	}

	public void setSuggestBox(AbstractSuggestBox<T, EventHandlingValueHolderItem<T>> suggestBox) {
		this.suggestBox = suggestBox;
	}
}
