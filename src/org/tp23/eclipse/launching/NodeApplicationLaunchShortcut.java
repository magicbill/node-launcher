package org.tp23.eclipse.launching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class NodeApplicationLaunchShortcut implements ILaunchShortcut, org.eclipse.debug.ui.actions.ILaunchable, ILaunchShortcut2 {


	public ILaunchConfiguration createConfiguration(IFile type) {
		ILaunchConfiguration config = null;
		try {
			ILaunchConfigurationType configType = getConfigurationType();
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, DebugPlugin.getDefault().getLaunchManager()
					.generateUniqueLaunchConfigurationNameFrom(type.getName()));
			String path = type.getFullPath().toPortableString();
			String projectName = type.getProject().getName();
			path = path.substring(("/"+projectName+"/").length());
			wc.setAttribute(ApplicationLauncherConstants.ATTR_MAIN_TYPE_NAME, path);
			wc.setAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, projectName);
			wc.setAttribute(ApplicationLauncherConstants.ATTR_APP_BINARY, "/usr/local/bin/node");
			wc.setAttribute(ApplicationLauncherConstants.ATTR_NODE_DEBUG_PORT, "8888");

			// CONTEXTLAUNCHING
			wc.setMappedResources(new IResource[] { 
					type 
			});
			config = wc.doSave();
		} catch (CoreException ce) {
			JDIDebugUIPlugin.log(ce);
		}
		return config;
	}

	public ILaunchConfigurationType getConfigurationType() {
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
		return lm.getLaunchConfigurationType(ApplicationLauncherConstants.NODE_LAUNCH_CONFIG_TYPE_ID);
	}


	public String getTypeSelectionTitle() {
		return "Node";
	}


	public String getEditorEmptyMessage() {
		return "Editor empty";
	}

	public String getSelectionEmptyMessage() {
		return "Selection empty";
	}
	
	@Override
	public ILaunchConfiguration[] getLaunchConfigurations(ISelection selection) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection sSelect = (StructuredSelection)selection;
			Object file = sSelect.getFirstElement();
			if (file instanceof IFile) {
				return new ILaunchConfiguration[] {
						findLaunchConfiguration((IFile)file, getConfigurationType())		
				};
			}
		}
		return new ILaunchConfiguration[0];
	}
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection sSelect = (StructuredSelection)selection;
			Object file = sSelect.getFirstElement();
			if (file instanceof IFile) {
				ILaunchConfiguration config = findLaunchConfiguration((IFile)file, getConfigurationType());
				if (config != null) {
					try {
						ILaunch launch = DebugUITools.buildAndLaunch(config, mode, new IProgressMonitor() {
							@Override
							public void worked(int arg0) {
							}
							@Override
							public void subTask(String arg0) {
							}
							@Override
							public void setTaskName(String arg0) {
							}
							@Override
							public void setCanceled(boolean arg0) {
							}
							@Override
							public boolean isCanceled() {
								return false;
							}
							@Override
							public void internalWorked(double arg0) {
							}
							@Override
							public void done() {
							}
							@Override
							public void beginTask(String arg0, int arg1) {
							}
						});
					} catch (CoreException e) {
						JDIDebugUIPlugin.log(e);
					} 
				}
			}
		}
	}	

	@Override
	public ILaunchConfiguration[] getLaunchConfigurations(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		IFile fileElement = (IFile)input.getAdapter(IFile.class);
		return new ILaunchConfiguration[] {
			findLaunchConfiguration(fileElement, getConfigurationType())
		};
	}

	@Override
	public IResource getLaunchableResource(ISelection selection) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection sSelect = (StructuredSelection)selection;
			Object file = sSelect.getFirstElement();
			if (file instanceof IFile) {
				return (IResource)file;
			}
		}
		return null;
	}

	@Override
	public IResource getLaunchableResource(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		return (IResource)input.getAdapter(IResource.class);
	}
	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile fileElement = (IFile)input.getAdapter(IFile.class);
		ILaunchConfiguration config = findLaunchConfiguration(fileElement, getConfigurationType());
		if (config != null) {
			DebugUITools.launch(config, mode);
		}
	}
	
	/**
	 * Locate a configuration to relaunch for the given type. If one cannot be
	 * found, create one.
	 * 
	 * @return a re-usable config or <code>null</code> if none
	 */
	public ILaunchConfiguration findLaunchConfiguration(IFile type, ILaunchConfigurationType configType) {
	     List candidateConfigs = Collections.EMPTY_LIST;
	     String mainFromType = type.getProjectRelativePath().toPortableString();
	     try {
	         ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(configType);
	         candidateConfigs = new ArrayList(configs.length);
	         for (int i = 0; i < configs.length; i++) {
	             ILaunchConfiguration config = configs[i];
	             if (config.getAttribute(ApplicationLauncherConstants.ATTR_MAIN_TYPE_NAME, "").equals(mainFromType)) { //$NON-NLS-1$
	            	 if (config.getAttribute(ApplicationLauncherConstants.ATTR_PROJECT_NAME, "").equals(type.getProject().getName())) { //$NON-NLS-1$
	            		 candidateConfigs.add(config);
	                 }
	             }
	          }
	      } catch (CoreException e) {
	         JDIDebugUIPlugin.log(e);
	      }
	        
	        // If there are no existing configs associated with the IType, create one.
			// If there is exactly one config associated with the IType, return it.
			// Otherwise, if there is more than one config associated with the IType, prompt the
			// user to choose one.
	      int candidateCount = candidateConfigs.size();
	        if (candidateCount < 1) {
	            return createConfiguration(type);
	        } else if (candidateCount == 1) {
	            return (ILaunchConfiguration) candidateConfigs.get(0);
	        } else {
	            // Prompt the user to choose a config. A null result means the user
				// canceled the dialog, in which case this method returns null,
				// since canceling the dialog should also cancel launching anything.
				ILaunchConfiguration config = chooseConfiguration(candidateConfigs);
	            if (config != null) {
	                return config;
	            }
	        }
	        
	        return null;
	    }
	
	/**
	 * Show a selection dialog that allows the user to choose one of the specified
	 * launch configurations. Return the chosen config, or <code>null</code> if the
	 * user canceled the dialog.
	 */
	public ILaunchConfiguration chooseConfiguration(List configList) {
	    IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
	    ElementListSelectionDialog dialog= new ElementListSelectionDialog(JDIDebugUIPlugin.getActiveWorkbenchShell(), labelProvider);
	    dialog.setElements(configList.toArray());
	    dialog.setTitle(getTypeSelectionTitle());
	    dialog.setMessage("Select one launcher");
	    dialog.setMultipleSelection(false);
	    int result = dialog.open();
	    labelProvider.dispose();
	    if (result == Window.OK) {
	        return (ILaunchConfiguration) dialog.getFirstResult();
	    }
	    return null;
	}




}
