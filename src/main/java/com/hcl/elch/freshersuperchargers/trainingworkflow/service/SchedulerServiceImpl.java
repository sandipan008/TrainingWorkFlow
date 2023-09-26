package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.controller.TaskController;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;

@Service
public class SchedulerServiceImpl{
	
	@Autowired
	private TaskRepo taskRepo;
	
	
	@Autowired
	private TaskController taskController;
	
	protected final static Logger log = LogManager.getLogger(SchedulerServiceImpl.class.getName());

	
	//scheduler for fetch data daily 12am & 12pm

	@Scheduled(cron = "*/60 * * * * *")
	public void fetchTask() {
	 try {
		List<Task> t=taskRepo.findByStatus("InProgress");
		for(Task task1:t) { 
			log.debug("Schedular Data : {}",task1);
			task1.setStatus("Processing");
			taskRepo.save(task1);
			taskController.getDetails(task1);
		}	
	 }
	 catch(Exception e)
	 {
		 log.error("Exception occured, Unable to perform Scheduler Task ");
	 }
  }
	
}
