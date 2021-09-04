package de.dc.fx.markdown.ui;

import com.google.inject.Inject;

import de.dc.fx.markdown.ui.di.FlexmarkModule;
import de.dc.workbench.fx.core.workspace.di.WorkspaceModule;
import de.dc.workbench.fx.ui.EmfApplication;
import de.dc.workbench.fx.ui.EmfWorkbench;
import de.dc.workbench.fx.ui.monaco.di.MonacoModule;
import de.dc.workbench.fx.ui.monaco.service.IMonacoLanguageService;

public class MarkdownEditorApp extends EmfApplication{

	@Inject IMonacoLanguageService monacoService;
	
	@Override
	protected boolean isStandaloneApp() {
		return true;
	}
	
	@Override
	protected void addModules() {
		addModule(new FlexmarkModule());
		addModule(new WorkspaceModule());
		addModule(new MonacoModule());
	}
	
	@Override
	protected void initStandaloneWorkbench(EmfWorkbench workbench) {
		super.initStandaloneWorkbench(workbench);
		monacoService.loadAll();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
