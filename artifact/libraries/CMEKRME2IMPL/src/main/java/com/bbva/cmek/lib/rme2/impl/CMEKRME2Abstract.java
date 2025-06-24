package com.bbva.cmek.lib.rme2.impl;

import com.bbva.cmek.lib.rme2.CMEKRME2;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.elara.utility.api.connector.APIConnector;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class CMEKRME2Abstract extends AbstractLibrary implements CMEKRME2 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected APIConnector internalApiConnector;

	protected APIConnector externalApiConnector;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param internalApiConnector the this.internalApiConnector to set
	*/
	public void setInternalApiConnector(APIConnector internalApiConnector) {
		this.internalApiConnector = internalApiConnector;
	}

	/**
	* @param externalApiConnector the this.externalApiConnector to set
	*/
	public void setExternalApiConnector(APIConnector externalApiConnector) {
		this.externalApiConnector = externalApiConnector;
	}

}