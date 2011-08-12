package org.tp23.eclipse.launching;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.internal.jobs.JobStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.internal.core.variables.WorkspaceResolver;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.actions.ILaunchable;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.launching.SocketUtil;

/**
 * Launches nodeunit and passes eh script as a praramter
 * 
 * @author teknopaul
 *
 */
public class NodeunitApplicationLaunchConfigurationDelegate extends AbstractApplicationLaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, 
            String mode, 
            ILaunch launch, 
            IProgressMonitor monitor) throws CoreException {
		
		// TODO  these from super class
		//boolean existsProblems(IProject proj)
		//boolean saveBeforeLaunch(ILaunchConfiguration configuration, String JavaDoc mode, IProgressMonitor monitor)
		
		// command array
		List<String>cmd = new ArrayList<String>();
		
		// get the nodeunit binary , already absolute path
		String binary = configuration.getAttribute(ApplicationLauncherConstants.ATTR_APP_BINARY, "");
		cmd.add(binary);
		cmd.add("--reporter");
		cmd.add("default");
		
		// get real paths from eclispe virtual paths
		 IProject proj = getProject(configuration);

		// get the absolute path of the JavaScript file to run
		File main = new File(getAbsoluteMainPath(configuration, proj));
		cmd.add(main.getName());
		
		// It seems current versions of nodeunit ONLY work if pwd is the dir of the test, which is crap
		File pwd = main.getParentFile();
		
		// this would be better
		//File pwd = getWorkingDirectory(configuration, proj);
		
		// Launch bin/nodeunit
		newProcess(launch ,
				exec(cmd.toArray(new String[cmd.size()]) , pwd) ,
				"nodeunit - " + configuration.getName() , 
				new HashMap());// TODO environment
	
	}



	


}
