/**
 * 
 */
package com.cluster9.logDispatcherRestService;

import com.clustercld.logsmanager.entities.LogParagraph;

/**
 * @author claude
 * throws exception without boilerplate code in the lambda
 */

@FunctionalInterface
public interface MapExceptionThrower {
	
	
	public void accept(LogParagraph log) throws Exception;
	
	
	// this call implies the Map is able to go through that method to pass the log to the lambda
	// how a lambda calls another lambda????
	static void rethrowException(MapExceptionThrower function) throws Exception {
		
		// l -> function.accept(l);

	}

}
