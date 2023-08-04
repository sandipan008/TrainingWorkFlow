package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserTaskException extends Exception {
	
	final Logger log = LogManager.getLogger(UserTaskException.class.getName());
	
	public UserTaskException(String s,Exception e)
	{
		super(s);
		log.error(e.toString());
		log.error(s);
	}

}
