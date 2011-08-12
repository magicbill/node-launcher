package org.tp23.eclipse.npm;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.tp23.eclipse.launching.Activator;
import org.tp23.eclipse.launching.Util;
import org.tp23.eclipse.node.preferences.PreferenceConstants;

public class InstallDomAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow wWindow;
	
	/**
	 * Constructor for Action1.
	 */
	public InstallDomAction() {
		super();
	}


	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		try {
			Npm.install("dom-js");
		} catch (CoreException e) {
			String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
			Util.errorMessage(
					"Is npm installed and configured? \n\ncurrent npm path from preferences" + 
					npmPath);
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void init(IWorkbenchWindow wWindow) {
		this.wWindow = wWindow;
	}

}
