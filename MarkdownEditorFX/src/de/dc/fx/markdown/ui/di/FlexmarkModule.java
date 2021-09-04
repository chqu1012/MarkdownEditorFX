package de.dc.fx.markdown.ui.di;

import com.google.inject.AbstractModule;

import de.dc.fx.markdown.ui.service.FlexmarkService;
import de.dc.fx.markdown.ui.service.IFlexmarkService;
import de.dc.workbench.fx.ui.markdown.di.MarkdownModule;
import de.dc.workbench.fx.ui.markdown.service.IMarkdownService;
import de.dc.workbench.fx.ui.markdown.service.MarkdownService;

public class FlexmarkModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(IMarkdownService.class).to(MarkdownService.class).asEagerSingleton();
		bind(IFlexmarkService.class).to(FlexmarkService.class).asEagerSingleton();
		
		install(new MarkdownModule());
	}

}
