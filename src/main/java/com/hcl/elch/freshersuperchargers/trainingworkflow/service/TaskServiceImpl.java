package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.controller.TaskController;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.DroolsEngineException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.NotFoundException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ProjectWorkflowRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.WorkflowRepo;


@Service
public class TaskServiceImpl {

	@Autowired
	private KieContainer kieContainer;

	@Autowired
	private TaskRepo taskRepo;

	@Autowired
	private ModuleRepo moduleRepo;

	@Autowired
	private WorkflowRepo workflowRepo;

	public long id;

	@Autowired
	private ProjectWorkflowRepo projectWorkflowRepo;

	String status = "Completed";

	String errorStatus = "Error";

	protected final static Logger log = LogManager.getLogger(TaskServiceImpl.class.getName());

	public List<Task> getAllTasks(){
		return taskRepo.findAll();
	}
	public Task getStatus(Task task, Category category) throws DroolsEngineException {

		try {
			id = TaskController.id;
			String moduleName = "null";
			String last = "null";

//			if(!projectAssignation) {
//                long l = category.getUserId();
//                List<workflow> m = workflowRepo.findBycategory(l);
//                log.debug(m.toString());
//                try {
//                    for (int i = 0; i < m.size(); i++) {
//                        if ((m.get(i).getTaskId() == task.getTaskId())&& (i <= m.size())) {
//                            log.debug("M Value " + m.get(i).getName() + " Task Value " + task.getTask());
//                            moduleName = m.get(i + 1).getName();
//                        }
//                    }
//                } catch (IndexOutOfBoundsException e) {
//                    last = "last";
//                    log.error("Getting IndexOutOfBound exception");
//                }
//                log.debug(category.getCategory() + " category " + category.getUserId());
//            } else {
                long l = task.getUserId();
                List<ProjectWorkflow> p = projectWorkflowRepo.findBySapId(l);

 

                try {
                	System.out.println("fetching new task");
                    for (int i = 0; i < p.size(); i++) {
                        if (p.get(i).getTaskId() == task.getTaskId() && i <= p.size()) {
                            log.debug("M Value " + p.get(i).getName() + " Task Value " + task.getTask());
                            moduleName = p.get(i + 1).getName();
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    last = "last";
                }
//            }

			if (!last.equals("last")) {
				KieSession kieSession = kieContainer.newKieSession();
				Task t1 = new Task();

				log.info("Modules from database is :- {}", moduleRepo.getBymoduleName(task.getTask()));
				Modules m1 = moduleRepo.getBymoduleName(moduleName);
				kieSession.setGlobal("m", m1);

				kieSession.setGlobal("t1", t1);
				kieSession.setGlobal("A", moduleName);
				kieSession.setGlobal("log",log);
				kieSession.insert(task);
				kieSession.insert(category);
				kieSession.fireAllRules();
				kieSession.dispose();
				log.debug("New value :- {}", t1);
				return t1;
			} else {
				task.setStatus(this.status);
				return task;
			}
		} catch (Exception e) {
			log.error("Caught the Drools Exception");
			Task t1 = taskRepo.getById(id);
			t1.setStatus(this.errorStatus);
			taskRepo.save(t1);
			throw new DroolsEngineException("Unable to perform the Drools Task,Because of drl file", e);
		}
	}

	// to save new record details
	public void save(Task st) {
		try {
			log.debug(st.getStatus());
			if (!st.getStatus().equals(this.status)) {
				taskRepo.save(st);
			}
		} catch (Exception e) {
			log.error("Exception Occured. Unable to add new task details. ");
			Task t1 = taskRepo.getById(id);
			t1.setStatus(this.errorStatus);
			taskRepo.save(t1);
		}
	}

	// to update status of current task
	public void setComplete(Task task) {
		try {
			List<Task> t = taskRepo.getByuserId(task.getUserId());
			long n = 0;
			for (Task temp : t) {
				if (temp.getTaskId() == task.getTaskId()) {
					n = temp.getId();
				}
			}
			Task t1 = taskRepo.getById(n);
			log.debug(task.getStatus());
			t1.setStatus(task.getStatus());
			log.info("Approver Name {}", task.getApprover());
			t1.setApprover(task.getApprover());
			taskRepo.save(t1);
		} catch (Exception e) {
			log.error("Exception occured, Unable to update the status of current task. ");
			Task t1 = taskRepo.getById(id);
			t1.setStatus(this.errorStatus);
			taskRepo.save(t1);
		}
	}

	public List<Task> getByUserId(long id) throws NotFoundException {
		try {
			return taskRepo.getByuserId(id);
		} catch (Exception e) {
			Task t1 = taskRepo.getById(id);
			t1.setStatus(this.errorStatus);
			taskRepo.save(t1);
			throw new NotFoundException("NotFoundException occured. Can't find userId.");
		}
	}

}