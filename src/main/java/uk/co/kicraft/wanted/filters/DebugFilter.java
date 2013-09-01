/**
 * 
 */
/**
 * @author Mike
 *
 */
package uk.co.kicraft.wanted.filters;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class DebugFilter implements Filter {

	@Override
	public boolean isLoggable(LogRecord record) {
		return true;
	}
	
}