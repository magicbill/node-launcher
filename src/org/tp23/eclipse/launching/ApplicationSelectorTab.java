package org.tp23.eclipse.launching;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.tp23.eclipse.node.preferences.NodePreferencePage;
import org.tp23.eclipse.node.preferences.PreferenceConstants;

/**
 * The SWT GUI to edit the parameters for the launch.
 * 
 * @author teknopaul
 */
public class ApplicationSelectorTab extends AbstractLaunchConfigurationTab implements ILaunchConfigurationTab {

	// UI Texts
	private static final String PROJECT = "Project";
	private static final String NODE_BINARY = "Node Binary";
	private static final String CHANGE_BINARY = "Change";
	private static final String CHANGE_PROJECT = "Change";
	private static final String LAUNCHABLE_SCRIPT = "Launchable Script";

	private ILaunchConfiguration fLaunchConfig;

	protected Text fMainText;
	protected Text fArgsText; // TODO
	protected Text fProjText;
	private Button fProjButton;
	protected Text fAppText;
	private Button fAppButton;
	protected Text fPortText;
	private WorkingDirectoryBlock workingDirBlock;
	
	private WidgetListener fListener = new WidgetListener();

	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
		((GridLayout) comp.getLayout()).verticalSpacing = 0;
		createApplicationEditor(comp);
		createVerticalSpacer(comp, 1);
		createProjectEditor(comp);
		createVerticalSpacer(comp, 1);
		createMainTypeEditor(comp);
		createVerticalSpacer(comp, 1);
		createPortEditor(comp);
		createVerticalSpacer(comp, 1);
		createPwdEditor(comp);
		setControl(comp);
	}

	@Override
	public String getName() {
		return "Application";
	}


	private void setCurrentLaunchConfiguration(ILaunchConfiguration config) {
		fLaunchConfig = config;
	}

	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		setCurrentLaunchConfiguration(config);
		updateMainTypeFromConfig(config);
		updateProjectNameFromConfig(config);
		updateApplicationFromConfig(config);
		updatePortFromConfig(config);
		workingDirBlock.initializeFrom(config);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(ApplicationLauncherConstants.ATTR_APP_BINARY, fAppText.getText().trim());
		config.setAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, fProjText.getText().trim());
        config.setAttribute(ApplicationLauncherConstants.ATTR_MAIN_TYPE_NAME, fMainText.getText().trim());
        config.setAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, fPortText.getText().trim());
        workingDirBlock.performApply(config);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		String nodePath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.NODE_PATH);
		config.setAttribute(ApplicationLauncherConstants.ATTR_APP_BINARY, nodePath);
		config.setAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, "");
        config.setAttribute(ApplicationLauncherConstants.ATTR_MAIN_TYPE_NAME, "lib/index,js");
        config.setAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, "8888");
        workingDirBlock.setDefaults(config);
	}

	/**
	 * Creates the widgets for specifying a main type.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	protected void createProjectEditor(Composite parent) {
		Font font = parent.getFont();
		
		Group group = new Group(parent, SWT.NONE);
		group.setText(PROJECT);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setFont(font);
		fProjText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fProjText.setLayoutData(gd);
		fProjText.setFont(font);
		fProjText.addModifyListener(fListener);
		fProjButton = createPushButton(group, CHANGE_PROJECT, null);
		fProjButton.addSelectionListener(fListener);
	}

	protected void createApplicationEditor(Composite parent) {
		Font font = parent.getFont();
		
		Group group = new Group(parent, SWT.NONE);
		group.setText(NODE_BINARY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setFont(font);
		fAppText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fAppText.setLayoutData(gd);
		fAppText.setFont(font);
		fAppText.addModifyListener(fListener);
		fAppButton = createPushButton(group, CHANGE_BINARY, null);
		fAppButton.addSelectionListener(fListener);
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


	protected void createPwdEditor(Composite parent) {
		workingDirBlock = new WorkingDirectoryBlock(ApplicationLauncherConstants.ATTR_WORKING_DIRECTORY) {
			@Override
			protected void updateLaunchConfigurationDialog() {
				super.updateLaunchConfigurationDialog();
				ApplicationSelectorTab.this.updateLaunchConfigurationDialog();
			}

			@Override
			protected void setDirty(boolean dirty) {
				super.setDirty(dirty);
				ApplicationSelectorTab.this.setDirty(dirty);
			}

			@Override
			protected IProject getProject(ILaunchConfiguration config) throws CoreException {
				String projectName = config.getAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, "");
				return getWorkspaceRoot().getProject(projectName);
			}
			
		};
		workingDirBlock.createControl(parent);
	}

	private void createMainTypeEditor(Composite parent) {
		Font font = parent.getFont();
		Group mainGroup = SWTFactory.createGroup(parent, LAUNCHABLE_SCRIPT, 2, 1, GridData.FILL_HORIZONTAL);
		Composite comp = SWTFactory.createComposite(mainGroup, font, 2, 2, GridData.FILL_BOTH, 0, 0);
		fMainText = SWTFactory.createSingleText(comp, 1);
		fMainText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
		createMainTypeExtensions(comp);
	}

	protected void updateMainTypeFromConfig(ILaunchConfiguration config) {
		String mainTypeName = "";
		try {
			mainTypeName = config.getAttribute(ApplicationLauncherConstants.ATTR_MAIN_TYPE_NAME, "");
		} catch (CoreException ce) {
			JDIDebugUIPlugin.log(ce);
		}
		fMainText.setText(mainTypeName);
	}
	protected void updateApplicationFromConfig(ILaunchConfiguration config) {
		String appBinName = "";
		try {
			appBinName = config.getAttribute(ApplicationLauncherConstants.ATTR_APP_BINARY, "");
		} catch (CoreException ce) {
			JDIDebugUIPlugin.log(ce);
		}
		fAppText.setText(appBinName);
	}
	protected void updateProjectNameFromConfig(ILaunchConfiguration config) {
		String projectName = "";
		try {
			projectName = config.getAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, "");
		} catch (CoreException ce) {
			JDIDebugUIPlugin.log(ce);
		}
		fProjText.setText(projectName);
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
	protected void updatePwdFromConfig(ILaunchConfiguration config) {
		String pwd = "";
		try {
			pwd = config.getAttribute(ApplicationLauncherConstants.ATTR_WORKING_DIRECTORY, "");
		} catch (CoreException ce) {
			JDIDebugUIPlugin.log(ce);
		}
		
	}

	private IProject chooseProject() {
		ILabelProvider labelProvider = new DefaultLabelProvider();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setTitle("Select Project");
		dialog.setMessage("Select Project");
		try {
			dialog.setElements(getWorkspaceRoot().getProjects());
		} catch (Exception jme) {
			JDIDebugUIPlugin.log(jme);
		}
		IProject javaProject = getProject();
		if (javaProject != null) {
			dialog.setInitialSelections(new Object[] { javaProject });
		}
		if (dialog.open() == Window.OK) {
			return (IProject) dialog.getFirstResult();
		}
		return null;
	}

	private String chooseApp(){
		Shell shell = JDIDebugUIPlugin.getActiveWorkbenchShell();
		shell.open();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFileName(fAppText.getText());
		String returned = dialog.open();
		return returned;
	}

	protected IProject getProject() {
		String projectName = fProjText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getWorkspaceRoot().getProject(projectName);
	}

	protected IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * Show a dialog that lets the user select a project. This in turn provides
	 * context for the main type, allowing the user to key a main type name, or
	 * constraining the search for main types to the specified project.
	 */
	protected void handleProjectButtonSelected() {
		IProject project = chooseProject();
		if (project == null) {
		    return;
		}
		String projectName = project.getName();
		fProjText.setText(projectName);
	}

	/**
	 * Show a dialog that lets the user select a project. This in turn provides
	 * context for the main type, allowing the user to key a main type name, or
	 * constraining the search for main types to the specified project.
	 */
	protected void handleAppButtonSelected() {
		String binary = chooseApp();
		if (binary == null) {
		    return;
		}
		fAppText.setText(binary);
	}

	protected void createMainTypeExtensions(Composite parent) {
		//do nothing by default
	}

	/**
	 * A listener which handles widget change events for the controls in this
	 * tab.
	 */
	private class WidgetListener implements ModifyListener, SelectionListener {

		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}

		public void widgetDefaultSelected(SelectionEvent e) {/* do nothing */
		}

		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			if (source == fProjButton) {
				handleProjectButtonSelected();
			} else if (source == fAppButton) {
				handleAppButtonSelected();
			} else {
				updateLaunchConfigurationDialog();
			}
		}
	}

}
