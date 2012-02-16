package ch.unibas.medizin.osce.client.style.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class ProxySuggestOracle<T> extends SuggestOracle {
	public interface GenericSuggestion<T> extends Suggestion {
		public T getProxy();
	}
	
	public class ProxySuggestion<T> implements GenericSuggestion<T>, IsSerializable {
		private T suggestion;
		private String displayString;
	    private String replacementString;

		public ProxySuggestion(T proxy, String displayString, String replacementString) {
			this.suggestion = proxy;
			this.displayString = displayString;
			this.replacementString = replacementString;
		}
	    
		@Override
		public String getDisplayString() {
			return displayString;
		}

		@Override
		public String getReplacementString() {
			return replacementString;
		}

		@Override
		public T getProxy() {
			return suggestion;
		}
	}
	
	/**
	   * A class reresenting the bounds of a word within a string.
	   *
	   * The bounds are represented by a {@code startIndex} (inclusive) and
	   * an {@code endIndex} (exclusive).
	   */
	  private static class WordBounds implements Comparable<WordBounds> {

	    final int startIndex;
	    final int endIndex;

	    public WordBounds(int startIndex, int length) {
	      this.startIndex = startIndex;
	      this.endIndex = startIndex + length;
	    }

	    public int compareTo(WordBounds that) {
	      int comparison = this.startIndex - that.startIndex;
	      if (comparison == 0) {
	        comparison = that.endIndex - this.endIndex;
	      }
	      return comparison;
	    }
	  }

	  private static final char WHITESPACE_CHAR = ' ';
	  private static final String WHITESPACE_STRING = " ";

	  /** Regular expression used to collapse all whitespace in a query string. */
	  private static final String NORMALIZE_TO_SINGLE_WHITE_SPACE = "\\s+";

	  /** Associates individual words with candidates. */
	  private HashMap<String, Set<String>> toCandidates = new HashMap<String, Set<String>>();

	  /** Associates candidates with their formatted suggestions. */
	  private HashMap<String, T> toRealSuggestions = new HashMap<String, T>();
	  
	  private List<String> words = new ArrayList<String>();

	  /** The whitespace masks used to prevent matching and replacing of the given substrings. */
	  private char[] whitespaceChars;

	  private Response defaultResponse;
		
//		private List<ProxySuggestion<T>> suggestions;
	  private Renderer<T> renderer;
	
	public ProxySuggestOracle(Renderer<T> renderer) {
		this(renderer, " ");
	}
	
	/**
	   * Constructor for <code>MultiWordSuggestOracle</code> which takes in a set of
	   * whitespace chars that filter its input.
	   * <p>
	   * Example: If <code>".,"</code> is passed in as whitespace, then the string
	   * "foo.bar" would match the queries "foo", "bar", "foo.bar", "foo...bar", and
	   * "foo, bar". If the empty string is used, then all characters are used in
	   * matching. For example, the query "bar" would match "bar", but not "foo
	   * bar".
	   * </p>
	   *
	   * @param whitespaceChars the characters to treat as word separators
	   */
	  public ProxySuggestOracle(Renderer<T> renderer, String whitespaceChars) {
	    this.whitespaceChars = new char[whitespaceChars.length()];
	    for (int i = 0; i < whitespaceChars.length(); i++) {
	      this.whitespaceChars[i] = whitespaceChars.charAt(i);
	    }
		this.renderer = renderer;
//		this.suggestions = new ArrayList<ProxySuggestion<T>>();
	  }
	
	public void add(T proxy) {
//		suggestions.add(new ProxySuggestion<T>(proxy, renderer, renderer));
		String candidate = normalizeSuggestionString(renderer.render(proxy));
	    // candidates --> real suggestions.
	    toRealSuggestions.put(candidate, proxy);

	    // word fragments --> candidates.
	    String[] words = candidate.split(WHITESPACE_STRING);
	    for (int i = 0; i < words.length; i++) {
	      String word = words[i];
	      this.words.add(word);
	      Set<String> l = toCandidates.get(word);
	      if (l == null) {
	        l = new HashSet<String>();
	        toCandidates.put(word, l);
	      }
	      l.add(candidate);
	    }
	    Collections.sort(this.words);
	}
	
	/**
	   * Adds all suggestions specified.
	   * @param collection the collection
	   */
	  public final void addAll(Collection<T> collection) {
	    for (T suggestion : collection) {
	      add(suggestion);
	    }
	  }

	  /**
	   * Removes all of the suggestions from the oracle.
	   */
	  public void clear() {
	    words.clear();
	    toCandidates.clear();
	    toRealSuggestions.clear();
	  }

	  @Override
	  public boolean isDisplayStringHTML() {
	    return true;
	  }

	  @Override
	  public void requestDefaultSuggestions(Request request, Callback callback) {
	    if (defaultResponse != null) {
	      callback.onSuggestionsReady(request, defaultResponse);
	    } else {
	      super.requestDefaultSuggestions(request, callback);
	    }
	  }

	  @Override
	  public void requestSuggestions(Request request, Callback callback) {
	    String query = normalizeSearchString(request.getQuery());
	    int limit = request.getLimit();

	    // Get candidates from search words.
	    List<String> candidates = createCandidatesFromSearch(query);

	    // Respect limit for number of choices.
	    int numberTruncated = Math.max(0, candidates.size() - limit);
	    for (int i = candidates.size() - 1; i > limit; i--) {
	      candidates.remove(i);
	    }

	    // Convert candidates to suggestions.
	    List<ProxySuggestion<T>> suggestions =
	        convertToFormattedSuggestions(query, candidates);

	    Response response = new Response(suggestions);
	    response.setMoreSuggestionsCount(numberTruncated);

	    callback.onSuggestionsReady(request, response);
	  }

	  /**
	   * Sets the default suggestion collection.
	   *
	   * @param suggestionList the default list of suggestions
	   */
	  public void setDefaultSuggestions(Collection<ProxySuggestion<T>> suggestionList) {
	    this.defaultResponse = new Response(suggestionList);
	  }

	  /**
	   * A convenience method to set default suggestions using a proxy...
	   *
	   * Note to use this method each default suggestion must be a proxy.
	   *
	   * @param suggestionList the default list of suggestions
	   */
	  public final void setDefaultSuggestionsFromText(Collection<T> suggestionList) {
	    Collection<ProxySuggestion<T>> accum = new ArrayList<ProxySuggestion<T>>();
	    for (T candidate : suggestionList) {
	      accum.add(createSuggestion(candidate, renderer.render(candidate), renderer.render(candidate)));
	    }
	    setDefaultSuggestions(accum);
	  }

	  /**
	   * Creates the suggestion based on the given replacement and display strings.
	   *
	   * @param replacementString the string to enter into the SuggestBox's text box
	   *          if the suggestion is chosen
	   * @param displayString the display string
	   *
	   * @return the suggestion created
	   */
	  protected ProxySuggestion<T> createSuggestion(T proxy, String replacementString, String displayString) {
	    return new ProxySuggestion<T>(proxy, replacementString, displayString);
	  }

	  /**
	   * Returns real suggestions with the given query in <code>strong</code> html
	   * font.
	   *
	   * @param query query string
	   * @param candidates candidates
	   * @return real suggestions
	   */
	  private List<ProxySuggestion<T>> convertToFormattedSuggestions(String query,
	      List<String> candidates) {
	    List<ProxySuggestion<T>> suggestions = new ArrayList<ProxySuggestion<T>>();

	    for (int i = 0; i < candidates.size(); i++) {
	      String candidate = candidates.get(i);
	      int cursor = 0;
	      int index = 0;
	      // Use real suggestion for assembly.
	      T formattedSuggestion = toRealSuggestions.get(candidate);

	      // Create strong search string.
	      SafeHtmlBuilder accum = new SafeHtmlBuilder();

	      String[] searchWords = query.split(WHITESPACE_STRING);
	      while (true) {
	        WordBounds wordBounds = findNextWord(candidate, searchWords, index);
	        if (wordBounds == null) {
	          break;
	        }
	        if (wordBounds.startIndex == 0 ||
	            WHITESPACE_CHAR == candidate.charAt(wordBounds.startIndex - 1)) {
	          String part1 = renderer.render(formattedSuggestion).substring(cursor, wordBounds.startIndex);
	          String part2 = renderer.render(formattedSuggestion).substring(wordBounds.startIndex,
	              wordBounds.endIndex);
	          cursor = wordBounds.endIndex;
	          accum.appendEscaped(part1);
	          accum.appendHtmlConstant("<strong>");
	          accum.appendEscaped(part2);
	          accum.appendHtmlConstant("</strong>");
	        }
	        index = wordBounds.endIndex;
	      }

	      // Check to make sure the search was found in the string.
	      if (cursor == 0) {
	        continue;
	      }

	      accum.appendEscaped(renderer.render(formattedSuggestion).substring(cursor));
	      ProxySuggestion<T> suggestion = createSuggestion(formattedSuggestion, renderer.render(formattedSuggestion), 
	    		  accum.toSafeHtml().asString());
	      suggestions.add(suggestion);
	    }
	    return suggestions;
	  }

	  /**
	   * Find the sorted list of candidates that are matches for the given query.
	   */
	  private List<String> createCandidatesFromSearch(String query) {
	    ArrayList<String> candidates = new ArrayList<String>();

	    if (query.length() == 0) {
	      return candidates;
	    }

	    // Find all words to search for.
	    String[] searchWords = query.split(WHITESPACE_STRING);
	    HashSet<String> candidateSet = null;
	    for (int i = 0; i < searchWords.length; i++) {
	      String word = searchWords[i];

	      // Eliminate bogus word choices.
	      if (word.length() == 0 || word.matches(WHITESPACE_STRING)) {
	        continue;
	      }

	      // Find the set of candidates that are associated with all the
	      // searchWords.
	      HashSet<String> thisWordChoices = createCandidatesFromWord(word);
	      if (candidateSet == null) {
	        candidateSet = thisWordChoices;
	      } else {
	        candidateSet.retainAll(thisWordChoices);

	        if (candidateSet.size() < 2) {
	          // If there is only one candidate, on average it is cheaper to
	          // check if that candidate contains our search string than to
	          // continue intersecting suggestion sets.
	          break;
	        }
	      }
	    }
	    if (candidateSet != null) {
	      candidates.addAll(candidateSet);
	      Collections.sort(candidates);
	    }
	    return candidates;
	  }

	  /**
	   * Creates a set of potential candidates that match the given query.
	   *
	   * @param query query string
	   * @return possible candidates
	   */
	  private HashSet<String> createCandidatesFromWord(String query) {
	    HashSet<String> candidateSet = new HashSet<String>();
	   
	    List<String> words = new ArrayList<String>();
	    for (String word : this.words) {
	    	if (word.contains(query)) {
	    		words.add(word);
	    	}
	    }
	    
//	    List<String> words = tree.getSuggestions(query, Integer.MAX_VALUE);
	    if (words != null) {
	      // Find all candidates that contain the given word the search is a
	      // subset of.
	      for (int i = 0; i < words.size(); i++) {
	        Collection<String> belongsTo = toCandidates.get(words.get(i));
	        if (belongsTo != null) {
	          candidateSet.addAll(belongsTo);
	        }
	      }
	    }
	    return candidateSet;
	  }

	  /**
	   * Returns a {@link WordBounds} representing the first word in {@code
	   * searchWords} that is found in candidate starting at {@code indexToStartAt}
	   * or {@code null} if no words could be found.
	   */
	  private WordBounds findNextWord(String candidate, String[] searchWords, int indexToStartAt) {
	    WordBounds firstWord = null;
	    for (String word : searchWords) {
	      int index = candidate.indexOf(word, indexToStartAt);
	      if (index != -1) {
	        WordBounds newWord = new WordBounds(index, word.length());
	        if (firstWord == null || newWord.compareTo(firstWord) < 0) {
	          firstWord = newWord;
	        }
	      }
	    }
	    return firstWord;
	  }
	
//	public void addAll(Collection<T> allSuggestions) {
//		for (T proxy : allSuggestions) {
//			add(proxy);
//		}
//	}
	
//	@Override
//	public void requestSuggestions(Request request, Callback callback) {
//		// TODO Improve Search function
//		String query = request.getQuery().toLowerCase();
//		ArrayList<ProxySuggestion<T>> result = new ArrayList<ProxySuggestion<T>>();
//		Iterator<ProxySuggestion<T>> iter = suggestions.iterator();
//		while(iter.hasNext()) {
//			ProxySuggestion<T> currentProxy = iter.next(); 
//			if (currentProxy.getDisplayString().toLowerCase().contains(query)) {
//				result.add(currentProxy);
//			}
//		}
//		callback.onSuggestionsReady(request, new Response(result));
//	}
	
	  /**
	   * Normalize the search key by making it lower case, removing multiple spaces,
	   * apply whitespace masks, and make it lower case.
	   */
	  private String normalizeSearchString(String search) {
	    // Use the same whitespace masks and case normalization for the search
	    // string as was used with the candidate values.
	    search = normalizeSuggestionString(search);

	    // Remove all excess whitespace from the search string.
	    search = search.replaceAll(NORMALIZE_TO_SINGLE_WHITE_SPACE,
	        WHITESPACE_STRING);

	    return search.trim();
	  }
	
	/**
	   * Takes the formatted suggestion, makes it lower case and blanks out any
	   * existing whitespace for searching.
	   */
	  private String normalizeSuggestionString(String formattedSuggestion) {
	    // Formatted suggestions should already have normalized whitespace. So we
	    // can skip that step.

	    // Lower case suggestion.
	    formattedSuggestion = formattedSuggestion.toLowerCase();

	    // Apply whitespace.
	    if (whitespaceChars != null) {
	      for (int i = 0; i < whitespaceChars.length; i++) {
	        char ignore = whitespaceChars[i];
	        formattedSuggestion = formattedSuggestion.replace(ignore, WHITESPACE_CHAR);
	      }
	    }
	    return formattedSuggestion;
	  }
}
