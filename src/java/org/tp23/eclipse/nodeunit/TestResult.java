package org.tp23.eclipse.nodeunit;

/**
 * A final bean.  Bringing the dot notation back into Java.
 * 
 * @author teknopaul
 */
public class TestResult {

	public final boolean passed;
	public final String text;

	public TestResult(boolean passed, String text) {
		this.passed = passed;
		this.text = text;
	}
	
	
}
