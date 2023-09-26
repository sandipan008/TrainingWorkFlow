package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Feedback;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.POC_Feedback;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.AssessmentService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.CamundaService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.CategoryService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.FeedbackService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.ModuleService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.TaskServiceImpl;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.WorkflowService;

@RestController
@RequestMapping("/workflow/")
public class MasterController {
	
//	private final RestTemplate restTemplate = new RestTemplate();
//
//	private final String camundaApiUrl = "http://localhost:9002/engine-rest/user";


	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private AssessmentService assessmentService; 
	
	@Autowired
	private ModuleService moduleService; 
	
	@Autowired
	private TaskServiceImpl taskService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private CamundaService camundaService;
	
//	@Autowired
//	private ModuleRepo moduleRepo;
//	@GetMapping("/check")
//	public Modules test() {
//		System.out.println("Modules :- "+moduleRepo.getByModuleId(11));
//		return moduleRepo.getByModuleId(11);
//	}
	@GetMapping("/check")
	public String test() {
		return "working...";
	}
	
// *********************************Assessment End Points *********************************************
	@GetMapping("/allAssesments")
	public ResponseEntity<?> getAllAssesmentLink(){
		List<Map<String,Object>> ans=assessmentService.getAllAssesmentLink();
		if(ans.isEmpty()) {
			return ResponseEntity.ok("Record Not Found..!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(ans);
	}
	
	@GetMapping("/assesments/{id}")
	public ResponseEntity<?> getAssesmentLinks(@PathVariable int id){
		List<String> ans=assessmentService.findAssesmentById(id);
		if (ans.isEmpty()) {
	        return ResponseEntity.ok("Record Not Found..!");
	    }
		return ResponseEntity.status(HttpStatus.OK).body(ans);
	}
	
// *********************************Category & Workflow End Points *********************************************
	
	@GetMapping("/categories")
	public ResponseEntity<?> getAllcategory(){
		List<Category> res=categoryService.getAllCatetory();
		if(res.isEmpty()) {
			return ResponseEntity.ok("Record Not Found..!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(res);		
	}
	
	@GetMapping("/category-module")
	public ResponseEntity<?> getModuleByCat(){
		List<Map<String,Object>>ans= workflowService.getModuleByCat();	
		if(ans.isEmpty()) {
			return ResponseEntity.ok("Record Not Found..!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(ans);	
		
	}
	
	// *********************************Modules End Points *********************************************

	@GetMapping("/modules")
	public ResponseEntity<?> getModules(){
		List<Modules> modules= moduleService.getModules();
		if(modules.isEmpty()) {
			return ResponseEntity.ok("Record Not Found..!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(modules);	
		
	}
	
	@PostMapping("/addModules")
	public ResponseEntity<?> addModule(@RequestBody Modules m){
		String modules= moduleService.addModule(m);
		if(modules==null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modules);
		}
		return ResponseEntity.status(HttpStatus.OK).body(modules);	
		
	}
	
	@PutMapping("/module-update/{id}")
	public ResponseEntity<?> updateModule(@PathVariable long id, @RequestBody Modules m){
		String modules= moduleService.update(m,id);
		if(modules==null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modules);
		}
		return ResponseEntity.status(HttpStatus.OK).body(modules);	
		
	}
	
	// *********************************Task End Points *********************************************
	
	@GetMapping("/tasks")
	public ResponseEntity<?> getUsersTasks(){
		List<Task> tasks= taskService.getAllTasks();
		if(tasks.isEmpty()) {
			return ResponseEntity.ok("Record Not Found..!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
		
		
	}
	
	//  *********************************FeedBack End Points *********************************************
	
	@GetMapping("/taskFeedback")
	public ResponseEntity<?> getTasksFeedback(){
		List<Feedback> feedback= feedbackService.getTaskFeedback();
		if(feedback.isEmpty()) {
			return ResponseEntity.ok("Record Not Found in Table..!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(feedback);
	}

	@GetMapping("/pocFeedback")
	public ResponseEntity<?> getPocFeedback(){
		List<POC_Feedback> feedback= feedbackService.getPocFeedback();
		if(feedback.isEmpty()) {
			return ResponseEntity.ok("Record Not Found in Table!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(feedback);		
	}
	
  //  *********************************Approver/SME End Points *********************************************
	
//	@GetMapping("/approver")
//	public ResponseEntity<List<POC_Feedback>> getAllApprover(){
//		
//	}
	
	@GetMapping("/camunda-users")
	public ResponseEntity<String> getAllCamundaUsers() {
		String response = camundaService.getAllCamundaUsers();
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

	}

	@GetMapping("/camunda-groups")
	public ResponseEntity<String> getAllCamundaGroups() {
		String response = camundaService.getAllCamundaGroups();
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

	}

}
