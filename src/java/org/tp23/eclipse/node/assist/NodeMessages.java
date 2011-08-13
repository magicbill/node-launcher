package org.tp23.eclipse.node.assist;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author teknopaul
 *
 */
public class NodeMessages extends NLS {
	
	private static final String BUNDLE_NAME= "org.tp23.eclipse.node.assist.NodeMessages";//$NON-NLS-1$

	private NodeMessages() {
		// Do not instantiate
	}
	
	public static String NodeLibraryWizardPage_title;
	public static String NodeLibraryWizardPage_FirefoxLibraryAdded;
	public static String NodeLibraryWizardPage_BrowserSupport;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, NodeMessages.class);
	}

	
}
