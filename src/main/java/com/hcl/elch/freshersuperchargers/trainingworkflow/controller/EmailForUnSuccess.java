package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;

@Component
public class EmailForUnSuccess implements JavaDelegate {

	@Autowired
	private TaskRepo tr;

	@Autowired
	RuntimeService rs;

        public long Id;

	@Autowired
	private EmailSenderService senderService;

	final Logger log = LogManager.getLogger(EmailForUnSuccess.class.getName());


	@Override
	public void execute(DelegateExecution execution) throws CamundaException {
		try {
			log.info("///////////This is Email Sending Task about status////////////////");
			Id=TaskController.id;
			String Email = (String) execution.getVariable("Email");
			String Task = (String) execution.getVariable("task");

			String username = (String) execution.getVariable("username");

			Task s1 = (com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task) execution.getVariable("mainid");
			LocalDate lt = LocalDate.now();

			log.info("Current date : {}" , lt);
			LocalDate datePlus1 = lt.plusDays(2);
			log.info("Due date : {}",datePlus1);
			s1.setDuedate(datePlus1);
			s1.setStatus("InProgress");
			tr.save(s1);
			senderService.mailSendingFailureTask(username, Email, Task.toUpperCase());
			log.debug(s1.getStatus());
		} catch (Exception e) {
			log.error(e.toString());
			Task t1=tr.getById(Id);
  		    	t1.setStatus("Error");
  		    	tr.save(t1);
			throw new BpmnError("Exception Occured", e);
		}
	}
}
