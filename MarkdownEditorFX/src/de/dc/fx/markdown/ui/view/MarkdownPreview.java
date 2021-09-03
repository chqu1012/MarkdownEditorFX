package de.dc.fx.markdown.ui.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.admonition.AdmonitionExtension;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.aside.AsideExtension;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.macros.MacrosExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
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
	private static MutableDataSet options = new MutableDataSet();
	private Parser parser;
	private HtmlRenderer renderer;
	
	static {
		List<Extension> extensions = new ArrayList<>();
		extensions.add(TablesExtension.create());
		extensions.add(StrikethroughExtension.create());
		extensions.add(MacrosExtension.create());
		extensions.add(AdmonitionExtension.create());
		extensions.add(AttributesExtension.create());
		extensions.add(AnchorLinkExtension.create());
		extensions.add(AbbreviationExtension.create());
		extensions.add(AsideExtension.create());
		extensions.add(AttributesExtension.create());
		extensions.add(AutolinkExtension.create());
		extensions.add(DefinitionExtension.create());
		extensions.add(EmojiExtension.create());
		extensions.add(EnumeratedReferenceExtension.create());
		extensions.add(FootnoteExtension.create());
		extensions.add(GfmIssuesExtension.create());
		extensions.add(GfmUsersExtension.create());
		extensions.add(GitLabExtension.create());
		extensions.add(InsExtension.create());
		extensions.add(TocExtension.create());
		extensions.add(TypographicExtension.create());
		extensions.add(WikiLinkExtension.create());
		options.set(Parser.EXTENSIONS, extensions);
	}
	
	public MarkdownPreview() {
		eventBroker.register(this);
		
		parser = Parser.builder(options).build();
		renderer = HtmlRenderer.builder(options).build();
		
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
