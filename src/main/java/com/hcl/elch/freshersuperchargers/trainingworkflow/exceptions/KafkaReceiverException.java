package com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KafkaReceiverException extends Exception
{
	final Logger log = LogManager.getLogger(KafkaReceiverException.class.getName());
	public KafkaReceiverException(String str, Exception e) {	
		super(str);
		log.error(e.toString());
	}  

}
