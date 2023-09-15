package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KafkaReceiverException extends Exception implements Serializable
{
	private static final long serialVersionUID = -7775903890171923094L;
	private transient Logger log = LogManager.getLogger(KafkaReceiverException.class.getName());
	public KafkaReceiverException(String str, Exception e) {	
		super(str);
		log.error(e);
	}  

}
