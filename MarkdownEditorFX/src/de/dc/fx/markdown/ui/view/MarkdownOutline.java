package de.dc.fx.markdown.ui.view;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.dc.fx.markdown.ui.editor.MarkdownEditor;
import de.dc.fx.markdown.ui.service.IFlexmarkService;
import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.EventTopic;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.ui.EmfFilteredTreeView;
import de.dc.workbench.fx.ui.markdown.MarkdownDocument;
import de.dc.workbench.fx.ui.markdown.MarkdownFactory;
import de.dc.workbench.fx.ui.markdown.MarkdownNode;
import de.dc.workbench.fx.ui.markdown.MarkdownPackage;
import de.dc.workbench.fx.ui.markdown.provider.MarkdownItemProviderAdapterFactory;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

public class MarkdownOutline extends EmfFilteredTreeView<MarkdownNode>{

	@Inject IFlexmarkService flexmarkService;
	@Inject IEventBroker eventBroker;
	
	public MarkdownOutline() {
		super("Outline");
		
		eventBroker.register(this);
	}

	@Override
	protected void onTreeViewSelectedItemChanged(ObservableValue<? extends TreeItem<MarkdownNode>> observable,
			TreeItem<MarkdownNode> oldValue, TreeItem<MarkdownNode> newValue) {
		super.onTreeViewSelectedItemChanged(observable, oldValue, newValue);
		if (newValue!=null) {
			MarkdownNode value = newValue.getValue();
			eventBroker.post("/reveal/active/monaco/editor/to/line", value.getLineNumber());
		}
	}
	
	@Subscribe
	public void subscribeCurrentSelectedEditor(EventContext<Tab> context) {
		if (context.match(EventTopic.CURRENT_SELECTED_EDITOR) && context.getInput() instanceof MarkdownEditor editor) {
			String text = editor.getInput().getText();
			MarkdownDocument doc = flexmarkService.parseAst(text);
			setInput(doc);
			expandAll();
		}
	}
	
	@Override
	protected AdapterFactory getModelItemProviderAdapterFactory() {
		return new MarkdownItemProviderAdapterFactory();
	}

	@Override
	protected EObject createRootModel() {
		return MarkdownFactory.eINSTANCE.createMarkdownDocument();
	}

	@Override
	protected EPackage getEPackage() {
		return MarkdownPackage.eINSTANCE;
	}

}
