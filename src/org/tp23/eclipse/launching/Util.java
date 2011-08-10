package org.tp23.eclipse.launching;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IProjectNatureDescriptor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class Util {

	
	public static boolean createProject() {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject newProject = workspace.getRoot().getProject("node_modules");
		if ( ! newProject.exists()) {
			IProjectDescription description = workspace.newProjectDescription("node_modules");
			description.setComment("Auto generated project");
			IPath location = workspace.getRoot().getRawLocation().append("node_modules");
			description.setLocation(location);
			
			IProjectNatureDescriptor jsNature = workspace.getNatureDescriptor("org.eclipse.wst.jsdt.core.jsNature");
			if (jsNature != null) {
				description.setNatureIds(new String[]{"org.eclipse.wst.jsdt.core.jsNature"});
			}
			try {
				newProject.create(description, null);
			} catch (CoreException e) {
				return false;
			}
		}
		return true;
	}
	
	
}
