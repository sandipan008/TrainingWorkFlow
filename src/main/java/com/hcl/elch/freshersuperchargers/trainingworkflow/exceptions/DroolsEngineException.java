package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DroolsEngineException extends Exception implements Serializable
{
	private static final long serialVersionUID = -3213968997938330109L;
	private transient Logger log = LogManager.getLogger(DroolsEngineException.class.getName());
	public  DroolsEngineException(String str,Exception e) {	
		super(str);
		log.error(e);
		log.error(str);
	}
}
