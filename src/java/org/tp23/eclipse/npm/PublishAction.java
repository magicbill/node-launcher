package org.tp23.eclipse.npm;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.tp23.eclipse.launching.Util;

public class PublishAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public PublishAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		org.eclipse.ui.internal.ObjectPluginAction impl = (org.eclipse.ui.internal.ObjectPluginAction)action; 

		StructuredSelection ss = (StructuredSelection)impl.getSelection();
		IProject p = (IProject)ss.getFirstElement();
		
		try {
			Npm.publish(p);
		} catch (CoreException e) {
			Util.errorMessage("Publish failed");
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		selection.isEmpty();
	}

}
