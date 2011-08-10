package org.tp23.eclipse.node.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.tp23.eclipse.launching.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.NODE_PATH, "/usr/local/bin/node");
		store.setDefault(PreferenceConstants.NPM_PATH, "/usr/bin/npm");
		
	}

}
