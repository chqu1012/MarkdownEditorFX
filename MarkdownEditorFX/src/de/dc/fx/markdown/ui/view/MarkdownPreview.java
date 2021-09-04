package de.dc.fx.markdown.ui.view;

import com.google.inject.Inject;

import de.dc.fx.markdown.ui.service.IFlexmarkService;
import de.dc.workbench.fx.ui.EmfEditorTextChangedView;
import de.dc.workbench.fx.ui.EmfView;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MarkdownPreview extends EmfEditorTextChangedView {

	@Inject IFlexmarkService flexmarkService;
	
	private WebEngine engine;
	
	public MarkdownPreview() {
		eventBroker.register(this);
		
		var webView = new WebView();
		engine = webView.getEngine();
		setContent(webView);
	}
	
	@Override
	protected void onEditorTextChanged(String content) {
		String html = flexmarkService.parse(content);
		engine.loadContent(html);		
	}

	@Override
	protected void onEditorChanged(EmfView editor) {
		if (editor instanceof MonacoTextEditor textEditor) {
			String content = textEditor.getInput().getText();
			String html = flexmarkService.parse(content);
			engine.loadContent(html);				
		}
	}
}
