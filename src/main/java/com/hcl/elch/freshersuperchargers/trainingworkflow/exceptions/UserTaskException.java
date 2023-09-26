package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserTaskException extends Exception implements Serializable{
	
	private static final long serialVersionUID = -1891576400220163727L;
	private transient Logger log = LogManager.getLogger(UserTaskException.class.getName());
	
	public UserTaskException(String s,Exception e)
	{
		super(s);
		log.error(e);
		log.error(s);
	}

}
