package org.tp23.eclipse.launching;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.ui.actions.ILaunchable;

/**
 * 
 * @author teknopaul
 *
 */
public abstract class AbstractApplicationLaunchConfigurationDelegate extends LaunchConfigurationDelegate implements ILaunchable {

	protected String getAbsoluteMainPath(ILaunchConfiguration configuration, IProject proj) throws CoreException {
		String main = configuration.getAttribute(ApplicationLauncherConstants.ATTR_MAIN_TYPE_NAME, "");
		IFile  mainFile = proj.getFile(main);
		main = mainFile.getRawLocation().toPortableString();
		return main;
	}
	
	protected IProject getProject(ILaunchConfiguration configuration) throws CoreException {
		String projectName = configuration.getAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, "");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject proj = root.getProject(projectName);
		return proj;
	}
	
	protected File getWorkingDirectory(ILaunchConfiguration configuration, IProject proj) throws CoreException {
		File pwd = new File("./");
		String workspaceLoc = configuration.getAttribute(ApplicationLauncherConstants.ATTR_WORKING_DIRECTORY, ""); // returns ${workspace_loc:bla/blah}
		String resolved = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(workspaceLoc);
		if ( "".equals(resolved) ) {
			pwd = new File(proj.getRawLocation().toPortableString() );
		}
		else {
			pwd = new File(resolved);
		}
		return pwd;
	}
	
	protected Process exec(String[] cmdLine, File workingDirectory) throws CoreException {
	    return DebugPlugin.exec(cmdLine, workingDirectory);
	}

	/**
	 * @since 3.0
	 * @see DebugPlugin#exec(String[], File, String[])
	 */
	protected Process exec(String[] cmdLine, File workingDirectory, String[] envp) throws CoreException {
	    return DebugPlugin.exec(cmdLine, workingDirectory, envp);
	}


	protected IProcess newProcess(ILaunch launch, Process p, String label, Map attributes) throws CoreException {
	    IProcess process= DebugPlugin.newProcess(launch, p, label, attributes);
	    if (process == null) {
	        p.destroy();
	        abort("Failed", null, 23);
	    }
	    return process;
	}
	
	protected void abort(String message, Throwable exception, int code) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, ApplicationLauncherConstants.NODE_LAUNCH_CONFIG_TYPE_ID, code, message, exception));
	}
}
