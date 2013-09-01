package uk.co.kicraft.wanted.filters;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ProductionFilter implements Filter {

	@Override
	public boolean isLoggable(LogRecord record) {		
		String levelName = record.getLevel().getName();		
		if(levelName.matches("[SEVERE|WARNING|INFO]")) {
			return true;
		} else {
			return false;
		}
	}	
	
}
