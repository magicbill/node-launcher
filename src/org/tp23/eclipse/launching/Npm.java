package org.tp23.eclipse.launching;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.tp23.eclipse.node.preferences.PreferenceConstants;

public class Npm {

	public static final boolean install(String name) throws CoreException {
		
		String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NODE_PATH);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String pwd = workspace.getRoot().getRawLocation().toPortableString();
		if (workspace.getRoot().getProject("node_modules").exists()) {
			DebugPlugin.exec(new String[]{npmPath, "install", name}, new File(pwd));
			return true;
		}
		return false;
		
	}

	public static final boolean ls(String name) throws CoreException {
		
		String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NODE_PATH);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String pwd = workspace.getRoot().getRawLocation().toPortableString();
		
		DebugPlugin.exec(new String[]{npmPath, "ls", name}, new File(pwd));
		return true;
	}

	public static final boolean publish(IFolder folder) throws CoreException {
		
		String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NODE_PATH);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String pwd = workspace.getRoot().getRawLocation().toPortableString();
		
		String toPublish = folder.getRawLocation().toPortableString();
		File check = new File(toPublish);
		if (check.isDirectory()) {
			check = new File(check, "package.json");
			if(check.exists() && check.isFile() && check.canRead()) {
				DebugPlugin.exec(new String[]{npmPath, "publish", toPublish}, new File(pwd));
				return true;
			}
		}
		return false;
		
	}
}
