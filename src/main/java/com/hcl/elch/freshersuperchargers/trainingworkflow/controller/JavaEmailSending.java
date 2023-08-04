package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;
import org.camunda.bpm.engine.RuntimeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Assessment;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.AssessmentRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;

@Controller
public class JavaEmailSending implements JavaDelegate {


//	final Logger log = Syste.getLogger(JavaEmailSending.class.getName());
	Logger log = LogManager.getLogger(JavaEmailSending.class);

	@Autowired
	private AssessmentRepo repo;

	@Autowired
	RuntimeService rs;
	
	@Autowired
	private EmailSenderService 	senderService;
	
	
	private Random random = new Random();
	
	public JavaEmailSending(EmailSenderService senderService) {
		this.senderService = senderService;
	}


	@Override
	public void execute(DelegateExecution execution) throws CamundaException {
		try {
			log.info("This is Email Sending Task about status");
			
			String Email = (String) execution.getVariable("Email");
			String Task = (String) execution.getVariable("task");
			String test=(String) execution.getVariable("test");
			String poc=(String) execution.getVariable("poc");
			String githubUrl="githuburl";
			
			execution.setVariable(githubUrl,"https://github.com/ahmadtausif38/TrainingWorkFlow.git");
			String userId=(String) execution.getVariable("userId");
			
			execution.setVariable("BranchName", userId+Task);
			log.debug(" Branch Names : "+userId+Task);
			
			String username=(String) execution.getVariable("username");
						
			long moduleId = (long) execution.getVariable("moduleId");
			List<Assessment>assessmentList=new ArrayList<>();//2
            assessmentList=repo.findByModuleId(moduleId);//3
			String decision= (String) execution.getVariable("Decision");
			
//			Random random=new Random();
			
			if(test.equals("1") && poc.equals("0")) {
				Assessment assessment=assessmentList.get(random.nextInt(assessmentList.size()));
				String assessmentLink="null";
                if(assessment!=null) {
                    assessmentLink = assessment.getAssessmentLink();
                }
                log.info("Assesment Link :- "+assessmentLink);
              
				String s = senderService.mailSendingForTask(username,Email, Task.toUpperCase(), assessmentLink);
				
			}
			if(test.equals("0") && poc.equals("1")) {
				log.info("mailing POC details");
				 String branch=(String) execution.getVariable("BranchName");
       		  		String githuburl=(String) execution.getVariable(githubUrl);
				String s=senderService.mailSendingForPoc(username,Email, Task.toUpperCase(),branch,githuburl);
				
			}
			if(test.equals("1") && poc.equals("1")) {
				if(decision==null) {
					Assessment assessment=assessmentList.get(random.nextInt(assessmentList.size()));
					String assessmentLink = assessment.getAssessmentLink();
					String s =senderService.mailSendingForTask(username,Email, Task.toUpperCase(), assessmentLink);
					log.info("Assesment Link :- "+assessmentLink);
				}
				else {
					if(decision.equals("Yes")) {
						log.info("mailing POC details");
						String branch=(String) execution.getVariable("BranchName");
	       		  		String githuburl=(String) execution.getVariable(githubUrl);
						String s=senderService.mailSendingForPoc(username,Email, Task.toUpperCase(),branch,githuburl);
						
					}
				}
				
			}
			
			
		} catch (Exception e) {
			log.error(e.toString());
			throw new BpmnError("Exception Occured", e);
		}
	}

}
