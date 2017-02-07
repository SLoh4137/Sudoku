/**
 * 
 * @author Stephan Loh
 * Last updated as of 2/6/17
 * Lock object used in CheckValid tasks 
 */

package model;

public class ValidLock {
	private boolean valid;
	
	public ValidLock() {
		this.valid = true;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void update(boolean value) {
		valid = valid && value;
	}

}
