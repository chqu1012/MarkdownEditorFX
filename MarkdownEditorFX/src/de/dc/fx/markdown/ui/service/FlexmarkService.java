package de.dc.fx.markdown.ui.service;

import java.util.ArrayList;

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
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.collection.iteration.ReversiblePeekingIterable;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.misc.Extension;

import de.dc.workbench.fx.ui.markdown.MarkdownDocument;
import de.dc.workbench.fx.ui.markdown.MarkdownFactory;
import de.dc.workbench.fx.ui.markdown.MarkdownNode;
import de.dc.workbench.fx.ui.markdown.service.IMarkdownService;

public class FlexmarkService implements IFlexmarkService {

	@Inject IMarkdownService markdownService;
	
	private MutableDataSet options = new MutableDataSet();
	private Parser parser;
	private HtmlRenderer renderer;

	public FlexmarkService() {
		var extensions = new ArrayList<Extension>();
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
		
		parser = Parser.builder(options).build();
		renderer = HtmlRenderer.builder(options).build();
	}
	
	@Override
	public String parse(String content) {
		var document = parser.parse(content);
		return renderer.render(document);
	}
	
	@Override
	public MarkdownDocument parse(Document document) {
		MarkdownDocument mdDocument = MarkdownFactory.eINSTANCE.createMarkdownDocument();
		var children = document.getChildren();
		for (Node child : children) {
			iterate(mdDocument, child);
		}
		return mdDocument;
	}
	
	private Node iterate(MarkdownNode parent, Node child) {
		ReversiblePeekingIterable<Node> children = child.getChildren();
		for (Node node : children) {
			MarkdownNode mdNode = markdownService.createNode(node.getNodeName());
			if (mdNode==null) {
				continue;
			}
			try {
				mdNode.setLineNumber(node.getLineNumber());
			} catch (Exception e) {
			}
			mdNode.setNodeType(node.getNodeName());
			mdNode.setValue(node.getChars().toStringOrNull());
			parent.getChildren().add(mdNode);
			iterate(mdNode, node);
		}
		return child;
	}

	@Override
	public MarkdownDocument parseAst(String content) {
		var document = parser.parse(content);
		return parse(document);
	}
}
