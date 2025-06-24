package com.bbva.cmek;

import com.bbva.cmek.lib.rme1.CMEKRME1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bill payment
 *
 */
public class CMEKTME101COTransaction extends AbstractCMEKTME101COTransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(CMEKTME101COTransaction.class);

	/**
	 * The execute method...
	 */
	@Override
	public void execute() {
		CMEKRME1 cmekRME1 = this.getServiceLibrary(CMEKRME1.class);
		// TODO - Implementation of business logic
	}

}
