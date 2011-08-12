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
import org.tp23.eclipse.node.preferences.PreferenceConstants;

public class SearchAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow wWindow;
	
	/**
	 * Constructor for Action1.
	 */
	public SearchAction() {
		super();
	}


	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		IInputValidator validator = new IInputValidator() {
			public String isValid(String newText) {
				if (newText != null && newText.length() > 1)
					return null;
				else
					return "Entera  search string";
			}
		};
		InputDialog dialog = new InputDialog(wWindow.getShell(), "npm search", "Enter search query", "", validator);
		if (dialog.open() == Window.OK) {
			try {
				Npm.search(dialog.getValue());
			} catch (CoreException e) {
				String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
				MessageDialog.openError(
						wWindow.getShell(),
						"Error",
						"Is npm installed and configured? \n\ncurrent npm path from preferences" + 
						npmPath);
			}
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
