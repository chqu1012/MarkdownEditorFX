package de.dc.fx.markdown.ui.editor;

import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;

public class MarkdownEditor extends MonacoTextEditor{

	public MarkdownEditor() {
		paneMonaco.setCurrentLanguage("markdown");
		paneMonaco.setCurrentTheme("vs-dark");
	}
}
