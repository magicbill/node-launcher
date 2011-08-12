package org.tp23.eclipse.launching;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IProjectNatureDescriptor;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class Util {

	public static Shell getShell() {
		return PlatformUI.getWorkbench().getDisplay().getShells()[0];
	}
	
	public static void showNodeunitView() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.asyncExec(
				  new Runnable() {
				    public void run(){
				    	try {
							workbench.getActiveWorkbenchWindow().getActivePage()
								.showView(ApplicationLauncherConstants.NODEUNIT_VIEW_ID);
						} catch (PartInitException e) {
							
						}
				    }
				  }
		);
	}

	public static void errorMessage(final String message) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(
				  new Runnable() {
				    public void run(){
				    	MessageDialog.openError(
				    			display.getActiveShell(),
								"Error",
								message);
				    }
				  });

	}
	/**
	 * @return  node_modules project or null
	 */
	public static IProject getNodeModulesProject() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject("node_modules");
		if(project.exists()) {
			return project;
		}
		return null;
	}
	
	public static boolean createNodeModulesProject() {
		
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		WorkspaceJob job = new WorkspaceJob("Create node_modules") {
            public IStatus runInWorkspace(IProgressMonitor m) {
        		IProject newProject = workspace.getRoot().getProject("node_modules");
        		if ( ! newProject.exists()) {
        			IProjectDescription description = workspace.newProjectDescription("node_modules");
        			description.setComment("Auto generated project");
        			//IPath location = workspace.getRoot().getRawLocation().append("node_modules");
        			//description.setLocation(location);
        			
        			IProjectNatureDescriptor jsNature = workspace.getNatureDescriptor("org.eclipse.wst.jsdt.core.jsNature");
        			if (jsNature != null) {
        				description.setNatureIds(new String[]{"org.eclipse.wst.jsdt.core.jsNature"});
        			}
        			try {
        				newProject.create(description, null);
        				newProject.open(null);
        				newProject.refreshLocal(IResource.DEPTH_INFINITE, m);
        			} catch (CoreException e) {
        				return Status.CANCEL_STATUS;
        			}
        		}
                return Status.OK_STATUS;
            }
        };
        job.setUser(true);
        job.schedule();
		return true;
	}
	
	
}
