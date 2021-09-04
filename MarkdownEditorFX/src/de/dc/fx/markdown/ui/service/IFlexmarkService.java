package de.dc.fx.markdown.ui.service;

import com.vladsch.flexmark.util.ast.Document;

import de.dc.workbench.fx.ui.markdown.MarkdownDocument;

public interface IFlexmarkService {

	String parse(String content);
	
	MarkdownDocument parse(Document document);
	MarkdownDocument parseAst(String content);
}
