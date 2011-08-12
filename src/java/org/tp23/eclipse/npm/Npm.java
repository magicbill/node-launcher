package org.tp23.eclipse.npm;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.tp23.eclipse.launching.Activator;
import org.tp23.eclipse.launching.Util;
import org.tp23.eclipse.node.preferences.PreferenceConstants;

public class Npm {

	public static final void install(final String name) throws CoreException {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		WorkspaceJob job = new WorkspaceJob("Install " + name) {
            public IStatus runInWorkspace(IProgressMonitor m) {
				String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				String pwd = workspace.getRoot().getRawLocation().toPortableString();
				if (workspace.getRoot().getProject("node_modules").exists()) {
					try {
						Process p = DebugPlugin.exec(new String[]{npmPath, "install", name}, new File(pwd));
						while( ! m.isCanceled( )) {
							try {
								Thread.sleep(500L);
								int ok = p.exitValue();
								Util.getNodeModulesProject().refreshLocal(1, m);
								break;
							} catch (Exception e) {
								// normal process not finished
							}
						}
						
					} catch (Exception e) {
						Util.errorMessage("Problem installing " + name);
					}
					return Status.CANCEL_STATUS;
				}
				else {
					Util.errorMessage("Create local modules project first");
				}
				return Status.OK_STATUS;
            }
		};
        job.setUser(true);
        job.schedule();
	}

	public static final boolean search(String name) throws CoreException {
		
		/*  TODO need a view for the output
		String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String pwd = workspace.getRoot().getRawLocation().toPortableString();
		
		Process p = DebugPlugin.exec(new String[]{npmPath, "search", name}, new File(pwd));
		*/
		return true;
	}

	public static final boolean publish(final IProject project) throws CoreException {
		WorkspaceJob job = new WorkspaceJob("Publish " + project.getName()) {
            public IStatus runInWorkspace(IProgressMonitor m) {
				String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				String pwd = workspace.getRoot().getRawLocation().toPortableString();
				
				String toPublish = project.getRawLocation().toPortableString();
				if (toPublish.length() > 1) {
					File check = new File(toPublish);
					if (check.isDirectory()) {
						check = new File(check, "package.json");
						if (check.exists() && check.isFile() && check.canRead()) {
							try {
								Process p = DebugPlugin.exec(new String[]{npmPath, "publish", toPublish}, new File(pwd));
								while( ! m.isCanceled() ) {
									try {
										Thread.sleep(500L);
										int ok = p.exitValue();
										if (ok == 0) {
											return Status.OK_STATUS;
										}
										return Status.CANCEL_STATUS;
									} catch (Exception e) {
										// normal process not finished
									}
								}
							} catch (CoreException e) {
								Util.errorMessage("Publishing failed");
							}
						}
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
