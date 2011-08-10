package org.tp23.eclipse.launching;

/**
 * Constants including the keys used to store data in the Launch configurations.
 * 
 * @author teknopaul
 */
public class ApplicationLauncherConstants {

	/**
	 * Matches MANIFEST.mf Bundle-SymbolicName
	 */
	public static final String LAUNCH_NODE_PLUGIN_ID = "org.tp23.eclipse.launcher.node";
	/**
	 * Matches plugin.xml launchConfigurationType id
	 */
	public static final String NODE_LAUNCH_CONFIG_TYPE_ID = "org.tp23.eclipse.launching.nodeApplication";
	/**
	 * Matches plugin.xml launchConfigurationType name
	 */
	public static final String NODE_LAUNCH_CONFIG_TYPE_NAME = "Node Application";

	// Attributes for type
	/**
	 * The executable to run ie. /usr/local/bin/node
	 */
	public static final String ATTR_APP_BINARY = "Executor";
	/**
	 * The JS file to run
	 */
	public static final String ATTR_MAIN_TYPE_NAME = "MainTypeName";
	/**
	 * TODO A string of arguments to add to the path
	 */
	public static final String ATTR_APP_ARGS = "ApplicationArguments";
	/**
	 * The project name as a string
	 */
	public static final String ATTR_PROJECT_NAME = "ProjectName";
	/**
	 * The working directory in eclisep variable form e.g ${workspace_loc:server}
	 */
	public static final String ATTR_WORKING_DIRECTORY = "PWD";
	/**
	 * The debug port to use when starting node in debug mode
	 */
	public static final String ATTR_NODE_DEBUG_PORT = "NodeDebugPort";
	

}
