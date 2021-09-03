package de.dc.fx.markdown.ui.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.misc.Extension;

import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.EventTopic;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.core.model.EmfOutlineContext;
import de.dc.workbench.fx.ui.EmfView;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;
import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MarkdownPreview extends EmfView {

	@Inject IEventBroker eventBroker;
	private WebEngine engine;
	
	public MarkdownPreview() {
		eventBroker.register(this);
		
		var webView = new WebView();
		engine = webView.getEngine();
		setContent(webView);
	}
	
	@Subscribe
	public void subscribeCurrentSelectedEditor(EventContext<Tab> context) {
		if (context.match(EventTopic.CURRENT_SELECTED_EDITOR) && context.getInput() instanceof MonacoTextEditor editor) {
			String content = editor.getInput().getText();
			String html = parseMarkdown(content);
			engine.loadContent(html);
		}
	}

	private String parseMarkdown(String content) {
		var options = new MutableDataSet();
		List<Extension> extensions = new ArrayList<>();
		extensions.add(TablesExtension.create());
		options.set(Parser.EXTENSIONS, extensions);
		var parser = Parser.builder(options).build();
		var renderer = HtmlRenderer.builder(options).build();
		var document = parser.parse(content);
		return renderer.render(document);
	}
	
	@Subscribe
	public void subscribeMonacoEditorTextChanged(EventContext<EmfOutlineContext> context) throws IOException {
		if (context.match("/monaco/editor/text/changed")) {
			String content = context.getInput().getText();
			String html = parseMarkdown(content);
			engine.loadContent(html);
		}
	}
}
