package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;

@Component
public class TaskAssignmentListener implements TaskListener {
	
		@Autowired 
		RuntimeService  rs;
		
		@Autowired 
		EmailSenderService senderService;
		
		@Value("${camunda.url}") 
		String url;

	  public static String Assignee=null;
     
	  protected final static Logger log = LogManager.getLogger(TaskAssignmentListener.class.getName());
	  

	  public void notify(DelegateTask delegateTask) {

		 
		  log.info("This is the SME/Approver task");
		  log.debug("Variables : "+delegateTask.getVariable("groupId"));
		  String taskId = delegateTask.getId();
		  String assignee = delegateTask.getAssignee();
		  
		  url=url+taskId;
		  
		  String task=(String) delegateTask.getVariable("task");
		  
		  String taskName=delegateTask.getName();
		  
		  String username=(String) delegateTask.getVariable("username");

		  List<User> userList = delegateTask.getProcessEngineServices().getIdentityService().createUserQuery().memberOfGroup((String) delegateTask.getVariable("groupId")).list();
		  log.info("Group users "+userList);  
		  Assignee=assignee;
	     
	    /*if (assignee != null) {

	      IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
		  User use = identityService.createUserQuery().userId(assignee).singleResult();
		  String email=use.getEmail();*/
		  
		 // String recipient="";
		 
//          for(User user : userList)
//          {
//        	  if (user != null) 
//        	  {
//        		 // recipient+=user.getEmail()+",";
//        		 // recipient+=user.getEmail();
//        		  System.out.println("Assignees :"+user.getFirstName());
//        	  }
//          }
		  String recipient[]= new String[userList.size()];
		  for(int i=0;i<userList.size();i++)
          {
			 User user=userList.get(i);
        	 String email =user.getEmail();
        	 recipient[i]=email;
        	 log.info("Assignees :"+user.getFirstName());
        	 log.info("Assignees Mail :"+email);
        	  
          }
		  log.info("Recipient :"+Arrays.toString(recipient));
		  log.info("Approval url :- "+url);
	        /*if (recipient != null && !recipient.isEmpty()) {

	          Email email = new SimpleEmail();
	          email.setCharset("utf-8");
	          email.setHostName(HOST);
	          email.setAuthentication(USER, PWD);

	          try {
	            email.setFrom("noreply@camunda.org");
	            email.setSubject("Task assigned: " + delegateTask.getName());
	            email.setMsg("Please complete: http://localhost:8080/camunda/app/tasklist/default/#/task=" + taskId);
	            email.addTo(recipient);
	            //email.addRecipients(Message.RecipientType.CC,
                      InternetAddress.parse(recipient));

	            email.send();
	            LOGGER.info(
	                "Task Assignment Email successfully sent to group '"  + "' with address '" + recipient + "'.");
	          } 
	          catch (Exception e) {
	            LOGGER.log(Level.WARNING, "Could not send email to assignees in group", e);
	          }

	        } 
	        else {
	          LOGGER.warning("Not sending email to group "  + "', users has no email address.");
	        }

	      } 
	      else {
	        LOGGER.warning("Not sending email to users " + "', users are not enrolled with identity service.");
	      }

	    }*/ 
          
         // System.out.println("Recipient :"+recipient+" assignee :- "+assignee +" task :- "+delegateTask.getName());
          
          //if (recipient != null && !recipient.isEmpty()) {
          if (recipient.length>0) {
          try {
        	  if(taskName.contains("POC")) {
        		  String branch=(String) delegateTask.getVariable("BranchName");
        		  String githuburl=(String) delegateTask.getVariable("githuburl");
        		  String s= senderService.mailSendingForPocApprover("team" ,recipient,task, url, username,branch,githuburl);  
        	  }
        	  else {
    
        		  String s= senderService.mailSendingToApprover("team" ,recipient,task, url, username);
        	  }
           
        	//  System.out.println(s);
        	//if(s.length()>=1) {
//        		 LOGGER.info(
//        	                "Task Assignment Email successfully sent to group '"  + "' with address '" + Arrays.toString(recipient) + "'.");
          }
        	//}
            
          catch (Exception e) {
            log.error( "Could not send email to assignees in group"+ e);
          }

        } 
        else {
          log.error("Not sending email to group "  + "', users has no email address.");
        }

          
	}
 }
