package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotFoundException extends Exception implements Serializable{
	
	private static final long serialVersionUID = -683180844099653512L;
	private transient Logger log = LogManager.getLogger(NotFoundException.class.getName());

	public NotFoundException(String s)
	{
		log.error(s);
	}
}
