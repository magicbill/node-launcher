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

/**
 * Ubligy hack but it does not seem possible to hookin to ecliaps' mechanism
 * for adding console buttons
 * @author teknopaul
 *
 */
public class RemoveConsolesAction implements IWorkbenchWindowActionDelegate {
	
	/**
	 * Constructor for Action1.
	 */
	public RemoveConsolesAction() {
	}


	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		Util.removeNpmConsoles();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void init(IWorkbenchWindow wWindow) {
	}


	@Override
	public void dispose() {
	}

}
