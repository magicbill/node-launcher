package org.tp23.eclipse.nodeunit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;
import org.tp23.eclipse.launching.ApplicationLauncherConstants;

public class NodeunitRunner {

	public static Process run(final Process p) {
		
		try {
			NodeunitResultsView view = getView();
			if (view != null) {
				view.clearResults();
				InputStream err;
				final CloningInputStream out = new CloningInputStream(p.getInputStream());
				Process cloner = new Process() {
					@Override
					public int waitFor() throws InterruptedException {
						return p.waitFor();
					}
					@Override
					public OutputStream getOutputStream() {
						return p.getOutputStream();
					}
					@Override
					public InputStream getInputStream() {
						return out;
					}
					@Override
					public InputStream getErrorStream() {
						return p.getErrorStream();
					}
					@Override
					public int exitValue() {
						return p.exitValue();
					}	
					@Override
					public void destroy() {
						p.destroy();
					}
				};
				readStream(view, out.cloneStream());
				return cloner;
			}
		} catch (CoreException e) {
			e.printStackTrace(); //TODO need an error console eclispeJS does not have one
		}
		return p;
	}
	
	private static NodeunitResultsView getView() throws CoreException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		//IViewDescriptor viewDesc = workbench.getViewRegistry().find(ApplicationLauncherConstants.NODEUNIT_VIEW_ID);
		
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IViewPart nodeUnitview = pages[j].findView(ApplicationLauncherConstants.NODEUNIT_VIEW_ID);
				if (nodeUnitview != null) {
					return (NodeunitResultsView)nodeUnitview;
				}
			}
		}
		// not the view we are looking for return (NodeunitResultsView)viewDesc.createView();
		return null;
	}

	private static void readStream(final NodeunitResultsView view, final InputStream out) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(out));
				
				String line = null;
				
				try {
					while( (line = br.readLine())  != null) {
						//System.err.println(line);
						TestResult result = parseLine(line);
						if (result != null) {
							view.addTestResult(result);
						}
					}
				} catch (IOException e) {
					view.addTestResult(new TestResult(false, "Executing test gave IO error"));
				}
			}
		});
		t.setName("Nodeunit stream monitor");
		t.start();
	}

	private static TestResult parseLine(String line) {
		if ( line.indexOf("✔") >= 0 ) {
			return new TestResult(true, line);
		}
		if ( line.indexOf("✖") >= 0 ) {
			return new TestResult(false, line);
		}
		return null;
	}
	
	private static class CloningInputStream extends InputStream {

		private boolean finished = false;
		private int pos = 0;
		private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		private InputStream source;
		private InputStream clone;
		
		CloningInputStream(InputStream source) {
			this.source = source;
			clone = new InputStream() {
				@Override
				public int read() throws IOException {
					byte data;
					while(! finished) {
						try {
							data = buffer.toByteArray()[pos];
							pos++;
							return (int)data;
						} catch (IndexOutOfBoundsException e) {
							try {
								synchronized(buffer) {
									buffer.wait(500L);
								}
							} catch (InterruptedException e1) {
							}
						}
					}
					return -1;
				}
			};
		}
		public InputStream cloneStream() {
			return clone;
		}
		@Override
		public int read() throws IOException {
			int read = this.source.read();
			if (read >= 0) {
				buffer.write(read);
				synchronized(buffer) {
					buffer.notifyAll();
				}
			}
			else {
				finished = true;
			}
			return read;
		}
		
	}
	
}
