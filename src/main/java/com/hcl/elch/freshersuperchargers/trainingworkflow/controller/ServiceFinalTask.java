package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Feedback;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.POC_Feedback;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.CategoryRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.FeedbackRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.POC_FeedBackRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.UserRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.TaskServiceImpl;



@Component
public class ServiceFinalTask implements JavaDelegate{
	
	
	@Autowired
	 private TaskServiceImpl ts;
	
	@Autowired
	 private TaskRepo tr;
	 
	@Autowired 
	private CategoryRepo cr;
	
	@Autowired 
	private UserRepo ur;
	
	@Autowired
	private FeedbackRepo feedbackRepo;
	
	@Autowired
	private POC_FeedBackRepo pocFeedbackRepo;
 
	public Task newTask;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	final Logger LOGGER = Logger.getLogger(ServiceFinalTask.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws CamundaException{
	 try {
		LOGGER.info("/////////////////////Final Service task///////////////////");
		Task task=new Task();
		Feedback feedback=new Feedback();
		POC_Feedback pocFeedback=new POC_Feedback();
		String date= (String) execution.getVariable("duedate");
		String userid= (String) execution.getVariable("userId");
		String taskid= (String) execution.getVariable("TaskId");
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate  d1 = LocalDate.parse(date, df);
		//System.out.println("Approver By"+execution.getVariable("approver")); 
		String username= (String) execution.getVariable("username");
		String email=(String) execution.getVariable("Email");
		String test=(String) execution.getVariable("test");
		String poc=(String) execution.getVariable("poc");
		
		task.setTask((String) execution.getVariable("task"));
		task.setStatus((String) execution.getVariable("status"));
		task.setDuedate(d1);
		task.setUserId(Long.parseLong(userid));
		task.setTaskId(Long.parseLong(taskid));
		String Approver= TaskAssignmentListener.Assignee;
		task.setApprover((String) execution.getVariable("approver"));
		if(test.equals("1") && poc.equals("0")){
			feedback.setApprover((String) execution.getVariable("approver"));
			feedback.setTask((String) execution.getVariable("task"));
			feedback.setUserId(Long.parseLong(userid));
			feedback.setScore((String) execution.getVariable("score"));
			feedback.setRemarks((String) execution.getVariable("remarks"));
			feedbackRepo.save(feedback);
		}
		else if(test.equals("0") && poc.equals("1")){
			pocFeedback.setApprover((String) execution.getVariable("approver"));
			pocFeedback.setTask((String) execution.getVariable("task"));
			pocFeedback.setUserId(Long.parseLong(userid));
			pocFeedback.setRemarks((String) execution.getVariable("remarks"));
			pocFeedbackRepo.save(pocFeedback);
		}
		else if(test.equals("1") && poc.equals("1")){
			feedback.setApprover((String) execution.getVariable("approver"));
			feedback.setTask((String) execution.getVariable("task"));
			feedback.setUserId(Long.parseLong(userid));
			feedback.setScore((String) execution.getVariable("score"));
			feedback.setRemarks((String) execution.getVariable("remarks"));
			pocFeedback.setApprover((String) execution.getVariable("pocApprover"));
			pocFeedback.setTask((String) execution.getVariable("task"));
			pocFeedback.setUserId(Long.parseLong(userid));
			pocFeedback.setRemarks((String) execution.getVariable("pocRemarks"));
			feedbackRepo.save(feedback);
			pocFeedbackRepo.save(pocFeedback);
		}
//		System.out.println(d1);
//		System.out.println(task.getTask());
		String PA=(String) execution.getVariable("ProjectAssignation");
		
		nextTask(task,PA);
		
		LOGGER.info("Current task obj from ServiceFinalTask :- "+task.toString());
		LOGGER.info("Next task obj from ServiceFinalTask :- "+newTask.toString());
		
		//boolean hasNext=true;
		if(newTask.getTaskId()==task.getTaskId()) {
			//hasNext=false;
			String s=emailSenderService.mailSendingForTaskCompletion(username, email);
		}else {
			String s=emailSenderService.mailSendingForNextTask(username, email, newTask.getTask(), newTask.getDuedate());
		}
	  }
	  catch(Exception e)
	 {
		  LOGGER.info("An Exception from Final Service Task Due to ");
		  e.printStackTrace();
		  throw new BpmnError("Camunda Exception",e);
	 }
	 
	}
	
	
    public void nextTask(Task task,String ProjectAssignation) {
    try
    {
//    	System.out.println(task.getUserId());
    	User u=ur.findBysapId(task.getUserId());
//    	System.out.println(u.toString()+" "+u.getCategory().getCategory());
    	Category c=cr.findById(u.getCategory().getUserId()).get();
//    	System.out.println(c.getCategory()+" "+c.getUserId());
        //Task st=ts.getStatus(task,c);
    	Task st=ts.getStatus(task,c,ProjectAssignation);
    	newTask=st;
        ts.setComplete(task);
//        System.out.println(st.getTask());
        ts.save(st);
    }
    catch(Exception e)
    {
    	LOGGER.info("Unable to move to next Task"+e);
    }
}}
