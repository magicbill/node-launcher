package org.tp23.eclipse.node.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.tp23.eclipse.launching.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class NodePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public NodePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Node.js preferences");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(
			new StringFieldEditor(PreferenceConstants.NODE_PATH, "Default node path", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.NPM_PATH, "Npm path", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
	
}