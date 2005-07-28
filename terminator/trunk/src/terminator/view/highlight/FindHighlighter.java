package terminator.view.highlight;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import terminator.model.*;
import terminator.view.*;

/**
 * Highlights the results of user-initiated finds.
 */
public class FindHighlighter implements Highlighter {
	/** The highlighter pen style. */
	private final Style style = new Style(Color.black, Color.yellow, null, null, false);

	private Pattern pattern;
	
	public String getName() {
		return "Find Highlighter";
	}
	
	public int setRegularExpression(JTextBuffer view, String regularExpression) {
		view.removeHighlightsFrom(this, 0);
		if (regularExpression.length() > 0) {
			this.pattern = Pattern.compile(regularExpression);
			return addHighlights(view, 0);
		} else {
			forgetRegularExpression(view);
			return 0;
		}
	}
	
	public void forgetRegularExpression(JTextBuffer view) {
		view.removeHighlightsFrom(this, 0);
		this.pattern = null;
	}
	
	/** Request to add highlights to all lines of the view from the index given onwards. */
	public int addHighlights(JTextBuffer view, int firstLineIndex) {
		TextBuffer model = view.getModel();
		int count = 0;
		for (int i = firstLineIndex; i < model.getLineCount(); i++) {
			String line = model.getLine(i);
			count += addHighlightsOnLine(view, i, line);
		}
		return count;
	}
	
	private int addHighlightsOnLine(JTextBuffer view, int lineIndex, String text) {
		if (pattern == null) {
			return 0;
		}
		int count = 0;
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			Location start = new Location(lineIndex, matcher.start());
			Location end = new Location(lineIndex, matcher.end());
			Highlight highlight = new Highlight(this, start, end, style);
			view.addHighlight(highlight);
			++count;
		}
		return count;
	}

	/** Request to do something when the user clicks on a Highlight generated by this Highlighter. */
	public void highlightClicked(JTextBuffer view, Highlight highlight, String text, MouseEvent event) {
		return;
	}
}
