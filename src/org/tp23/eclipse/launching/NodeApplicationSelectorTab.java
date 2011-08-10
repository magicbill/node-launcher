package org.tp23.eclipse.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class NodeApplicationSelectorTab extends ApplicationSelectorTab {

	private WidgetListener fListener = new WidgetListener();
	
	protected Text fPortText;

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite comp = (Composite)super.getControl();
		createVerticalSpacer(comp, 1);
		createPortEditor(comp);
		
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		updatePortFromConfig(config);
		super.initializeFrom(config);
	}
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, fPortText.getText().trim());
		super.performApply(config);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, "8888");
        super.setDefaults(config);
	}
	

	protected void createPortEditor(Composite parent) {
		Font font = parent.getFont();
		
		Group group = new Group(parent, SWT.NONE);
		group.setText("Debug Port");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setFont(font);
		fPortText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fPortText.setLayoutData(gd);
		fPortText.setFont(font);
		fPortText.addModifyListener(fListener);
	}
	protected void updatePortFromConfig(ILaunchConfiguration config) {
		String debugPort = "";
		try {
			debugPort = config.getAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, "");
		} catch (CoreException ce) {
			JDIDebugUIPlugin.log(ce);
		}
		fPortText.setText(debugPort);
	}
	
	private class WidgetListener implements ModifyListener, SelectionListener {

		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}

		public void widgetDefaultSelected(SelectionEvent e) {/* do nothing */
		}

		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			updateLaunchConfigurationDialog();
		}
	}
}
