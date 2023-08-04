package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CamundaException extends Exception {
	
	final Logger log = LogManager.getLogger(CamundaException.class.getName());
	
	public CamundaException(String s,Exception e)
	{
		super(s);
		log.error(e.toString());
		log.error(s);
	}

}
