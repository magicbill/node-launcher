package org.tp23.eclipse.npm;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.tp23.eclipse.launching.Util;

public class CreateModulesProjectAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		Util.createNodeModulesProject();
		/*
		MessageDialog.openInformation(
				shell,
				"NPM",
				"Modeuls project created.");*/
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
