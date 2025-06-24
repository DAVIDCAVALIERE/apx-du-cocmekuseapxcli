package com.bbva.cmek.lib.rme1.impl;

import com.bbva.cmek.lib.rme1.CMEKRME1;
import com.bbva.cmek.lib.rme2.CMEKRME2;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.elara.utility.jdbc.JdbcUtils;
import com.datiobd.daas.DaasMongoConnector;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class CMEKRME1Abstract extends AbstractLibrary implements CMEKRME1 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected JdbcUtils jdbcUtils;

	protected CMEKRME2 cmekRME2;

	protected DaasMongoConnector daasMongoConnector = new DaasMongoConnector();


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param jdbcUtils the this.jdbcUtils to set
	*/
	public void setJdbcUtils(JdbcUtils jdbcUtils) {
		this.jdbcUtils = jdbcUtils;
	}

	/**
	* @param cmekRME2 the this.cmekRME2 to set
	*/
	public void setCmekRME2(CMEKRME2 cmekRME2) {
		this.cmekRME2 = cmekRME2;
	}

}