package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.DroolsEngineException;
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
	private ModuleRepo mr;

	@Autowired
	private WorkflowRepo wr;

	@Autowired
	private ProjectWorkflowRepo pr;

	String status = "Completed";

	protected final static Logger log = LogManager.getLogger(TaskServiceImpl.class.getName());

	public Task getStatus(Task task, Category category, String ProjectAssignation) throws DroolsEngineException {

		try {
			String s = "null";
			String last = "null";
			if (ProjectAssignation.equalsIgnoreCase("No")) {
				long l = category.getUserId();
				List<workflow> m = wr.findBycategory(l);
				log.debug(m.toString());
				try {
					for (int i = 0; i < m.size(); i++) {
						if (m.get(i).getTaskId() == task.getTaskId() && i <= m.size()) {
							log.debug("M Value " + m.get(i).getName() + " Task Value " + task.getTask());
							s = m.get(i + 1).getName();
						}
					}
				} catch (IndexOutOfBoundsException e) {
					last = "last";
				}
				log.debug(category.getCategory() + " category " + category.getUserId());
			} else {
				long l = task.getUserId();
				List<ProjectWorkflow> p = pr.findByuser_sapId(l);

				try {
					for (int i = 0; i < p.size(); i++) {
						if (p.get(i).getTaskId() == task.getTaskId() && i <= p.size()) {
							log.debug("M Value " + p.get(i).getName() + " Task Value " + task.getTask());
							s = p.get(i + 1).getName();
						}
					}
				} catch (IndexOutOfBoundsException e) {
					last = "last";
				}
			}
			if (last.equals("last")) {
				KieSession kieSession = kieContainer.newKieSession();
				Task t1 = new Task();

				log.info("Modules form database is :- " + mr.getBymoduleName(task.getTask()).toString());
				Modules m1 = mr.getBymoduleName(s);
				kieSession.setGlobal("m", m1);

				kieSession.setGlobal("t1", t1);
				kieSession.setGlobal("A", s);
				kieSession.insert(task);
				kieSession.insert(category);
				kieSession.fireAllRules();
				kieSession.dispose();
				log.debug("New value :-" + t1.toString());
				return t1;
			} else {
				task.setStatus(status);
				return task;
			}
		} catch (Exception e) {
			log.error("Caught the Drools Exception");
			throw new DroolsEngineException("Unable to perform the Drools Task,Because of drl file", e);
		}
	}

	// to save new record details
	public void save(Task st) {
		try {
			log.debug(st.getStatus());
			if (!st.getStatus().equals("Completed")) {
				tr.save(st);
			} else {
				
			}
		} catch (Exception e) {
			log.error("Unable to add new task details " + e);
		}
	}

	// to update status of current task
	public void setComplete(Task task) {
		try {
			List<Task> t = tr.getByuserId(task.getUserId());
			long n = 0;
			for (Task tr : t) {
				if (tr.getTaskId() == task.getTaskId()) {
					n = tr.getId();
				}
			}
			Task t1 = tr.getById(n);
			log.debug(task.getStatus());
			t1.setStatus(task.getStatus());
			log.info("Approver Name " + task.getApprover());
			t1.setApprover(task.getApprover());
			tr.save(t1);
		} catch (Exception e) {
			log.error("Exception occured, Unable to update the status of current task " + e);
		}
	}

	public List<Task> getByUserId(long id) throws Exception {
		try {
			return tr.getByuserId(id);
		} catch (Exception e) {
			log.error(e.toString());
			throw new Exception(e);
		}
	}

}
