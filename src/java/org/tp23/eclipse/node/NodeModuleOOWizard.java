/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.tp23.eclipse.node;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.dialogs.DialogUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * Wizard to create a new Object Oriented Module.
 * 
 * @author teknopaul
 *
 */
public class NodeModuleOOWizard extends BasicNewResourceWizard {
	
	WizardNewFileCreationPage mainPage;

	public NodeModuleOOWizard() {
	}
	
	public void addPages() {
		super.addPages();
		mainPage = new WizardNewFileCreationPage("newNodeModuleOOPage", getSelection());//$NON-NLS-1$
		mainPage.setTitle("New oject oriented modeul");
		mainPage.setDescription("Create a node module that can have multiple instances creted");
		addPage(mainPage);
	}

	 
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("Node Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
	}
	
	protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/newfile_wiz.png");//$NON-NLS-1$
		setDefaultPageImageDescriptor(desc);
	}

	
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
        if (file == null) {
        	return false;
        }
        try {
			file.setContents(NodeModuleOOWizard.class.getResourceAsStream("/org/tp23/eclipse/node/node-module-oo.js") , 0, null);
		} catch (CoreException e) {
			return false;
		}
		
		selectAndReveal(file);
		
		// Open editor on new file.
		IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
		try {
			if (dw != null) {
				IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (PartInitException e) {
			DialogUtil.openError(dw.getShell(), ResourceMessages.FileResource_errorMessage, e.getMessage(), e);
		}
		
        return true;
	}



}
