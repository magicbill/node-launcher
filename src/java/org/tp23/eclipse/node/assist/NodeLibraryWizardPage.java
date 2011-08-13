package org.tp23.eclipse.node.assist;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPage;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension2;
import org.eclipse.wst.jsdt.ui.wizards.NewElementWizardPage;

/**
 * 
 * @author teknopaul
 *
 */
public class NodeLibraryWizardPage extends NewElementWizardPage implements IJsGlobalScopeContainerPage, IJsGlobalScopeContainerPageExtension, IJsGlobalScopeContainerPageExtension2 {

	private static final String CONTAINER_ID = "org.tp23.eclipse.node.assist.NodeLibrary";

	public NodeLibraryWizardPage() {
		super("NodeLib");
		setTitle(NodeMessages.NodeLibraryWizardPage_title);
		setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
	}

	public boolean finish() {
		return true;
	}

	public IIncludePathEntry getSelection() {
		System.out.println("Unimplemented method:BaseLibraryWizardPage.getSelection");
		return null;
	}

	public void setSelection(IIncludePathEntry containerEntry) {
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		DialogField field = new DialogField();

		field.setLabelText(NodeMessages.NodeLibraryWizardPage_FirefoxLibraryAdded);
		LayoutUtil.doDefaultLayout(composite, new DialogField[]{field}, false, SWT.DEFAULT, SWT.DEFAULT);
		Dialog.applyDialogFont(composite);
		setControl(composite);
		setDescription(NodeMessages.NodeLibraryWizardPage_BrowserSupport);
	}

	public void initialize(IJavaScriptProject project, IIncludePathEntry[] currentEntries) {
		// nothing to initialize
	}

	public IIncludePathEntry[] getNewContainers() {
		IIncludePathEntry library = JavaScriptCore.newContainerEntry(new Path(CONTAINER_ID));
		return new IIncludePathEntry[]{library};
	}


}
