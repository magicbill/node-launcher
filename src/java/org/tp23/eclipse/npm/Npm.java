package org.tp23.eclipse.npm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.tp23.eclipse.launching.Activator;
import org.tp23.eclipse.launching.Util;
import org.tp23.eclipse.node.preferences.PreferenceConstants;

/**
 * Static methods to call npm as if from the command line
 * 
 * TODO better integration and Views , currently writes to a console.
 * 
 * @author teknopaul
 *
 */
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

	public static final boolean search(final String name) throws CoreException {

		final String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final String pwd = workspace.getRoot().getRawLocation().toPortableString();
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				Process p;
				try {
					p = DebugPlugin.exec(new String[]{npmPath, "search", name}, new File(pwd));

					MessageConsole console = Util.getConsole("npm search");
	
					MessageConsoleStream stream = console.newMessageStream();
					InputStream in = p.getInputStream();
					int read = 0;
					byte[] b = new byte[1024];
					
					try {
						while ((read = in.read(b)) >= 0) {
							stream.write(b, 0, read);
						}
					} catch (IOException e) {
					}
				} catch (CoreException e1) {
				}
			}
		});
		t.setName("npm search");
		t.start();
		
		return true;
	}

	public static final boolean ls() throws CoreException {

		String npmPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NPM_PATH);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String pwd = workspace.getRoot().getRawLocation().toPortableString();
		
		Process p = DebugPlugin.exec(new String[]{npmPath, "ls"}, new File(pwd));
		
		MessageConsole console = Util.getConsole("npm ls");

		MessageConsoleStream stream = console.newMessageStream();
		InputStream in = p.getInputStream();
		int read = 0;
		byte[] b = new byte[1024];
		
		try {
			while ((read = in.read(b)) >= 0) {
				stream.write(b, 0, read);
			}
		} catch (IOException e) {
			return false;
		}
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
