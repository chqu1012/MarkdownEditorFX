package de.dc.fx.markdown.ui.editor;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;

public class MarkdownEditor extends MonacoTextEditor{

	@Inject IEventBroker eventBroker;
	
	public MarkdownEditor() {
		paneMonaco.setCurrentLanguage("markdown");
		paneMonaco.setCurrentTheme("vs-dark");
		
		eventBroker.register(this);
	}
	
	@Subscribe
	public void subscribeRevealToLine(EventContext<Integer> context) {
		if (context.match("/reveal/active/monaco/editor/to/line")) {
			var input = context.getInput();
			if(input!=null) {
				var linenumber = input+1;
				paneMonaco.executeJS("editorView.revealLine("+linenumber+");");
			}
		}
	}
}
