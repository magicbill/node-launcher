package org.tp23.eclipse.node.assist;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;
import org.eclipse.wst.jsdt.core.compiler.libraries.SystemLibraryLocation;
import org.tp23.eclipse.launching.ApplicationLauncherConstants;

/**
 * Initializes the Node.js JavaScript libraries sothat context sensitive help (Ctrl + space)
 * can popup the values i nteh default node namespace.
 * 
 * N.B. this code is baseed on the FireFox support in EclipseJS.
 * 
 * @author teknopaul
 *
 */
public class NodeLibInitializer extends JsGlobalScopeContainerInitializer implements IJsGlobalScopeContainerInitializer {

	protected static final String CONTAINER_ID = "org.tp23.eclipse.node.assist.NodeLibrary";
	protected static final String ContainerDescription = "Node.js Support Library";
	
	// it seems these are passed to FindSupport to locate the file data from the Bundle directory
	// /libraries  N.B. this is not the same as the calsspath location /libraries
	protected static final char[][] LIBRARY_FILE_NAMES = {
		"Buffer.js".toCharArray(),
		"Console.js".toCharArray(),
		"EventEmitter.js".toCharArray(),
		"globals.js".toCharArray()
		
	};
	
	protected static final String PLUGIN_ID = "";

	static class NodeLibLocation extends SystemLibraryLocation {
		
		NodeLibLocation() {
			super();
		}


		public char[][] getLibraryFileNames() {
			return NodeLibInitializer.LIBRARY_FILE_NAMES;
		}

		protected String getPluginId() {
			return ApplicationLauncherConstants.LAUNCH_NODE_PLUGIN_ID;
		}
		
		private static LibraryLocation fInstance;
		
		public static LibraryLocation getInstance(){
			if(fInstance== null){
				fInstance = new NodeLibLocation();
			}
			return fInstance;
		}
	}

	public LibraryLocation getLibraryLocation() {
		return NodeLibLocation.getInstance();
	}


	public String getDescription(IPath containerPath, IJavaScriptProject project) {
		return NodeLibInitializer.ContainerDescription;
	}

	public String getDescription() {
		return NodeLibInitializer.ContainerDescription;
	}

	
	public IPath getPath() {
		return new Path(NodeLibInitializer.CONTAINER_ID);
	}

	public int getKind() {
		return IJsGlobalScopeContainer.K_SYSTEM;
	}


	public boolean canUpdateJsGlobalScopeContainer(IPath containerPath, IJavaScriptProject project) {
		return true;
	}

	public String[] containerSuperTypes() {
		return new String[]{"GlobalScope"};
	}


}
