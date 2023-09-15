package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.UserTaskException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ProjectWorkflowRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.UserRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.WorkflowRepo;

@RestController
public class TaskController implements JavaDelegate {

	public Task glob;

	public static long id;

	@Autowired
	private UserRepo ur;

	@Autowired
	private ModuleRepo mr;

	@Autowired
	private TaskRepo tr;
	
	@Autowired
	private WorkflowRepo workflowRepo;
	
	@Autowired
	private ProjectWorkflowRepo projectWorkflowRepo;

	@Autowired
	RuntimeService rs;

	final Logger log = LogManager.getLogger(TaskController.class.getName());
	
	String errorStatus="Error";

	@Transactional
	public void getDetails(Task task) throws CamundaException {
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("glo", task);
			log.info("Start Event Connected");
			id = task.getId();
			glob = task;
			RestTemplate rt = new RestTemplate();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Content_Type", "application/json");
			HttpEntity<Task> httpen = new HttpEntity<>(task, httpHeaders);
			rt.postForEntity("http://localhost:9002/engine-rest/process-definition/key/Decision/start", httpen, String.class);
		} catch (Exception e) {
			Task t1 = tr.getById(glob.getId());
			t1.setStatus(this.errorStatus);
			tr.save(t1);
			throw new CamundaException("Camunda Exception, Unable to start the process", e);
		}
	}

	User camundaTask(Task t) throws UserTaskException {
		try {
			User u = ur.findBysapId(t.getUserId());
			log.info("User EMAIL :- {} Username :- {}" , u.getEmail() , u.getName());
			return u;
		} catch (Exception e) {
			throw new UserTaskException("Exception Occured, Unable to fetch Email Id", e);
		}
	}

	public Modules category(Task t) throws UserTaskException {
		try {
			Modules m = mr.getBymoduleName(t.getTask().toUpperCase());
			if (m == null) {
				Task t1 = tr.getById(glob.getId());
				t1.setStatus(this.errorStatus);
				tr.save(t1);
			} else {
				return m;
			}
		} catch (Exception e) {
			throw new UserTaskException("Exception Occured, Module Name is Incorrect or Null", e);
		}
		return null;
	}

	@Override
	public void execute(DelegateExecution execution) throws CamundaException {
		try {
			log.info("In Service task to send EmailId");
			execution.setVariable("ErrorId", Long.toString(glob.getId()));
			User s = camundaTask(glob);
			execution.setVariable("Email", s.getEmail());
			execution.setVariable("username", s.getName());
			execution.setVariable("ProjectAssignation", s.getProjAssignedStatus());
			log.debug(s.getProjAssignedStatus());

			Modules m = category(glob);
			String taskName=glob.getTask().toUpperCase();
			execution.setVariable("mainid", glob);
			execution.setVariable("test", m.getExam());
			execution.setVariable("groupId", m.getGroupId());
			execution.setVariable("moduleId", m.getModuleId());
			execution.setVariable("poc", m.getPOC());
			execution.setVariable("task", taskName);
			execution.setVariable("TaskId", Long.toString(glob.getTaskId()));
			execution.setVariable("userId", Long.toString(glob.getUserId()));
			execution.setVariable("status", glob.getStatus());
			LocalDate localDate = glob.getDuedate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String date = localDate.format(formatter);
			execution.setVariable("duedate", date);
			log.debug(glob.getStatus());
			log.debug(taskName+" "+s.getProjAssignedStatus());
			if(taskName.equals("NEW_HIRE") ) {
				log.info("In project workflow table");
				Category category=s.getCategory();
				long categoryId=category.getUserId();
				List<workflow> workflows=workflowRepo.findBycategory(categoryId);
				for(workflow i:workflows) {
					ProjectWorkflow newWorkflow = ProjectWorkflow.builder().Name(i.getName()).user(s).taskId(i.getTaskId())
							.build();
					projectWorkflowRepo.save(newWorkflow);
				}
				log.debug(workflows);
				
			}
		} catch (Exception e) {
			log.error(e);
			Task t1 = tr.getById(glob.getId());
			t1.setStatus(this.errorStatus);
			tr.save(t1);
			throw new BpmnError("Error,  Some Values are missing or wrong in task details", e);
		}
	}

}
