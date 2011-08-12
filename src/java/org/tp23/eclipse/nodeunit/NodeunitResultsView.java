package org.tp23.eclipse.nodeunit;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.progress.FinishedJobs;
import org.eclipse.ui.internal.progress.ProgressMessages;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.part.ViewPart;

public class NodeunitResultsView extends PageBookView implements IPage {// extends ViewPart {
	
	public static final String ID = "org.tp23.eclipse.nodeunit.view";

	private TableViewer viewer;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if(parent instanceof TestResult[]) {
				return (TestResult[])parent;
			}
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if (obj instanceof TestResult) {
				TestResult result = (TestResult)obj;
				return result.text;
			}
			else {
				return obj.toString();
			}
			
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			
			if (obj instanceof TestResult) {
				TestResult result = (TestResult)obj;
				if (result.passed) {
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
				}
				else {
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
				}
			}
			else {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
			}
					
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		// Provide the input to the ContentProvider
		viewer.setInput(new TestResult[]{new TestResult(true, "not executed")});
		initPulldownMenu();
		//addMenu(viewer.getControl());
	}

	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void clearResults() {
		// what a palavour, we need to clear results in the UI thread and hang this one until it completes
		IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(
				  new Runnable() {
				    public void run(){
				    	viewer.setInput(new TestResult[]{});
				    }
				  });
	}
	
	public void setTestResults(final TestResult[] results) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.asyncExec(
				  new Runnable() {
				    public void run(){
				    	viewer.setInput(results);
				    }
				  });
		
	}

	public void addTestResult(final TestResult result) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.asyncExec(
				  new Runnable() {
				    public void run(){
				    	viewer.add(result);
				    }
				  });
	}
	
	// TODO not working
	private void addMenu(final Control parent) { 
		parent.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
			    Menu popupMenu = new Menu(parent);
			    MenuItem clearItem = new MenuItem(popupMenu, SWT.CASCADE);
			    clearItem.setText("Clear");
			    clearItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event arg0) {
						clearResults();
					}
				}); 
			
			    Menu newMenu = new Menu(popupMenu);
			    clearItem.setMenu(newMenu);
			}
	    });
    }


	@Override
	protected IPage createDefaultPage(PageBook pageBook) {
		createControl(pageBook);
		return this;
	}


	@Override
	protected PageRec doCreatePage(IWorkbenchPart arg0) {
		return null;
	}


	@Override
	protected void doDestroyPage(IWorkbenchPart arg0, PageRec arg1) {
		
	}


	@Override
	protected IWorkbenchPart getBootstrapPart() {
		return null;
	}


	@Override
	protected boolean isImportant(IWorkbenchPart arg0) {
		return false;
	}


	@Override
	public void createControl(Composite parent) {
		this.createPartControl(parent);
	}


	@Override
	public Control getControl() {
		return viewer.getControl();
	}


	@Override
	public void setActionBars(IActionBars actionBars) {
	}
	
	Action clearAllAction;


	private void initPulldownMenu() {
		createClearAllAction();
        IMenuManager menuMgr = getViewSite().getActionBars()
                .getMenuManager();
        menuMgr.add(clearAllAction);
    }

    /**
     * Create the clear all action for the receiver.
     */
    private void createClearAllAction() {
        clearAllAction = new Action("Clear results") {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            public void run() {
                NodeunitResultsView.this.clearResults();
            }
        };
        clearAllAction
                .setToolTipText(ProgressMessages.NewProgressView_RemoveAllJobsToolTip);
        ImageDescriptor id = WorkbenchImages
                .getWorkbenchImageDescriptor("/elcl16/progress_remall.gif"); //$NON-NLS-1$
        if (id != null) {
            clearAllAction.setImageDescriptor(id);
        }
        id = WorkbenchImages
                .getWorkbenchImageDescriptor("/dlcl16/progress_remall.gif"); //$NON-NLS-1$
        if (id != null) {
            clearAllAction.setDisabledImageDescriptor(id);
        }
    }

}