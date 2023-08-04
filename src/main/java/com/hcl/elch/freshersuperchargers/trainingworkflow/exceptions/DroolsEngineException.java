package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DroolsEngineException extends Exception 
{
	final Logger log = LogManager.getLogger(DroolsEngineException.class.getName());
	public  DroolsEngineException(String str,Exception e) {	
		super(str);
		log.error(e.toString());
		log.error(str);
	}
}
