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
 * Launches the application, takes parameters form the config and calls eclipse specific
 * exec functions so that when executed the in-built console works.
 * 
 * @author teknopaul
 *
 */
public class NodeApplicationLaunchConfigurationDelegate extends AbstractApplicationLaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, 
            String mode, 
            ILaunch launch, 
            IProgressMonitor monitor) throws CoreException {
		
		// TODO  these from super class
		//boolean existsProblems(IProject proj)
		//boolean saveBeforeLaunch(ILaunchConfiguration configuration, String JavaDoc mode, IProgressMonitor monitor)
		
		// TODO int port = SocketUtil.findFreePort();
		
		// command array
		List<String>cmd = new ArrayList<String>();
		
		// get the node binary , already absolute path
		String binary = configuration.getAttribute(ApplicationLauncherConstants.ATTR_APP_BINARY, "");
		cmd.add(binary);
		
		// if in debug mode add the debug port to the node command line
		if ("debug".equals(mode)) {
			String port = configuration.getAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, "");
			if (port.length() > 0) {
				cmd.add("--debug=" + Integer.parseInt(port));
			}
		}
		
		// get real paths from eclispe virtual paths
		 IProject proj = getProject(configuration);

		// get the absolute path of the JavaScript file to run
		String main = getAbsoluteMainPath(configuration, proj);
		cmd.add(main);
		
		// get the absolute path of the pwd to use
		File pwd = getWorkingDirectory(configuration, proj);
		
		// Launch node.js
		newProcess(launch ,
				exec(cmd.toArray(new String[cmd.size()]) , pwd) ,
				configuration.getName() , 
				new HashMap());// TODO environment
		
		
		
		// TODO launch google chrome debugger		
		if ("debug".equals(mode)) {
			
			try {
				// these seem to be the chrome launch types
				// id="org.chromium.debug.ui.LaunchType$StandaloneV8"
				// id="org.chromium.debug.ui.LaunchType$Chromium"
				// implemented by 
				// org.chromium.debug.ui.launcher.StandaloneV8LaunchType
				// org.chromium.debug.ui.launcher.ChromiumLaunchType
				
		
				ILaunchConfigurationType configType = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurationType("org.chromium.debug.ui.LaunchType$Chromium");
				ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(configType);
				// If there is only one it must be correct
				if (configs.length == 0) {
					JDIDebugUIPlugin.log(new Status(IStatus.WARNING, ApplicationLauncherConstants.LAUNCH_NODE_PLUGIN_ID, 
							"No V8 Remote Debug Launch Configurations"));
					return;
				}
				if (configs.length == 1) {
					JDIDebugUIPlugin.log(new Status(IStatus.INFO, ApplicationLauncherConstants.LAUNCH_NODE_PLUGIN_ID, 
							"Launching " + configs[0].getName()));
					DebugUITools.launch(configs[0], mode);	
					return;
				}
				// if there is more than one take the one that starts with "default"
				JDIDebugUIPlugin.log(new Status(IStatus.WARNING, ApplicationLauncherConstants.LAUNCH_NODE_PLUGIN_ID, 
				"More than one v8 Launch Config found loking for one prefixed \"default\""));

				for (int i = 0; i < configs.length; i++) {
					if (configs[i].getName().startsWith("default")) {
						JDIDebugUIPlugin.log(new Status(IStatus.INFO, ApplicationLauncherConstants.LAUNCH_NODE_PLUGIN_ID, 
						"Launching " + configs[i].getName()));
						DebugUITools.launch(configs[i], mode);
						return;
					}
				}
			}
			catch(Exception ex) {
				JDIDebugUIPlugin.log(ex);
			}

		}
	}



	


}
