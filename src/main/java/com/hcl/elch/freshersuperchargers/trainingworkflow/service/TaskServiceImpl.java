package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.controller.TaskController;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.DroolsEngineException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.CategoryRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ProjectWorkflowRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.WorkflowRepo;


@Service
public class TaskServiceImpl {
	
	 @Autowired
	 private KieContainer kieContainer;
	 
	 @Autowired
	 private TaskRepo tr;
	 
	 @Autowired
	 private CategoryRepo cr;
	 
	 @Autowired
	 private ModuleRepo mr;
	 
	 public Task glob;
	 
	 @Autowired
	 private WorkflowRepo wr;
	 
	 @Autowired
	 private ProjectWorkflowRepo pr;
	 
	 String status="Completed";
	 
	 protected final static Logger LOGGER = Logger.getLogger(SchedulerServiceImpl.class.getName());
	 

	public Task getStatus(Task task,Category category,Boolean ProjectAssignation) throws DroolsEngineException{
		
	try {
		String s="null";
		String last="null";
		//if(ProjectAssignation.equalsIgnoreCase("No")) {
		if(ProjectAssignation==false) {
		long l=category.getUserId();
		List<workflow> m=wr.findBycategory(l);
		LOGGER.info(m.toString());
		try {
		for(int i=0;i<m.size();i++)
		{
			if(m.get(i).getTaskId()==task.getTaskId() && i<=m.size()) 
			{
				LOGGER.info("M Value "+m.get(i).getName()+ " Task Value "+task.getTask());
				s=m.get(i+1).getName();
			}
		}}
		catch(IndexOutOfBoundsException e) {
			last="last";
		}
		LOGGER.info(category.getCategory()+" category "+category.getUserId());
		}
		else {
			LOGGER.info("Inside else ");
			long l=task.getUserId();
			//System.out.println(l);
			List<ProjectWorkflow> p=pr.findByUser_sapId(l);
			//System.out.println(p.toString());
			LOGGER.info("value of  p :- "+p);
			
			try {
				for(int i=0;i<p.size();i++)
				{
					if(p.get(i).getTaskId()==task.getTaskId() && i<=p.size()) 
					{
						LOGGER.info("M Value "+p.get(i).getName()+ " Task Value "+task.getTask());
						s=p.get(i+1).getName();
						LOGGER.info("S value"+s);
					}
				}}
				catch(IndexOutOfBoundsException e) {
					last="last";
			}	
		}
		if(last!="last") {
		KieSession kieSession =kieContainer.newKieSession();
		Task t1=new Task();
		
		LOGGER.info("Modules form database is :- "+mr.getBymoduleName(task.getTask()).toString());
		Modules m1=mr.getBymoduleName(s);
		LOGGER.info("next module "+ m1.getDuration()+" "+m1.getModuleName());
		kieSession.setGlobal("m",m1);
		
		kieSession.setGlobal("t1",t1);
		kieSession.setGlobal("A",s);
		kieSession.insert(task);
		kieSession.insert(category);
		kieSession.fireAllRules();
		kieSession.dispose();
		LOGGER.info("New value :-"+t1.toString()); 
		return t1;
		}
		else {
			task.setStatus(status);
			return task;
		}}
	catch(Exception e)
	{
		LOGGER.info("Caught the Drools Exception");
		Task t1=tr.getById(TaskController.id);
		t1.setStatus("Error");
		tr.save(t1);
		throw new DroolsEngineException("Unable to perform the Drools Task,Because of drl file", e);	
	}
	}

	
	//to save new record details 
	public void save(Task st) 
	{
	  try 
	  {
		  LOGGER.info(st.getStatus());
		  if(st.getStatus()!="Completed")
		  {
			  tr.save(st);
		  }
		  else {
			  //tr.save(st);
		  }
	  }
	  catch(Exception e)
	  {
		  LOGGER.info("Unable to add new task details "+e);
		  Task t1=tr.getById(TaskController.id);
		  t1.setStatus("Error");
		  tr.save(t1);
	  }
	}

	
	
	//to update status of current task 
	public void setComplete(Task task) {
	try {
		List<Task> t=tr.getByuserId(task.getUserId());
		long n = 0;
		for(Task tr: t)
		{
			if(tr.getTaskId()==task.getTaskId()) {
			  n=tr.getId();
			}
		}
		Task t1=tr.getById(n);
		LOGGER.info(task.getStatus());
		t1.setStatus(task.getStatus());
		LOGGER.info("Approver Name "+task.getApprover());
		t1.setApprover(task.getApprover());
		tr.save(t1);
	  }catch(Exception e)
		{
		  LOGGER.info("Exception occured, Unable to update the status of current task "+e);
		  Task t1=tr.getById(TaskController.id);
		  t1.setStatus("Error");
		  tr.save(t1);
		}
	}

	
	public List<Task> getByUserId(long id) throws Exception{
	  try {
		return tr.getByuserId(id);
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  Task t1=tr.getById(TaskController.id);
			t1.setStatus("Error");
			tr.save(t1);
		  throw new Exception(e);
	  }
	}
	
}
