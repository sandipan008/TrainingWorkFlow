package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CamundaException extends Exception implements Serializable{
	
	private static final long serialVersionUID = 5723154452989537444L;
	private transient Logger log = LogManager.getLogger(CamundaException.class.getName());
	
	public CamundaException(String s,Exception e)
	{
		super(s);
		log.error(e);
		log.error(s);
	}

}
