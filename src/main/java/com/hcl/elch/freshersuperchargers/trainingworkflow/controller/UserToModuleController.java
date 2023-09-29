package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ProjectWorkflowRepo;

@RestController
@RequestMapping("/workflow")
public class UserToModuleController {

	@Autowired
	private ProjectWorkflowRepo projectWorkflowRepo;

	@Autowired
	private ModuleRepo moduleRepo;

	@GetMapping("/modules/{userSapId}")
	public HashMap<Long, String> getModulesBysapId(@PathVariable("userSapId") long userSapId) {
		List<ProjectWorkflow> names=projectWorkflowRepo.findprojectWorkflowBySapId(userSapId);
		HashMap<Long, String> response=new HashMap<>();
		for(ProjectWorkflow temp:names) {
			String name=temp.getName();
			long taskId=temp.getTaskId();
			response.put(taskId,name);
		}
		return response;
	}

	@PostMapping("/modules/{userSapId}/update/moduleName")
	public ResponseEntity<Object> addNameToPosition(@PathVariable("userSapId") Long userSapId,
			@RequestParam("position") int position, @RequestParam("name") String name) {

		List<ProjectWorkflow> workflows = projectWorkflowRepo.findBySapId(userSapId);
//		User user=workflows.get(0).getUser();
		Long sapid = workflows.get(0).getSapId();

		Modules module = moduleRepo.getBymoduleName(name);
		if (module == null) {
			return ResponseEntity.badRequest().body("Name is not present in Modules table");
		}

		if (position < 1 || position > (workflows.size() + 1)) {
			return ResponseEntity.badRequest().body("Invalid position");
		}

		ProjectWorkflow newWorkflow = ProjectWorkflow.builder().Name(name).sapId(sapid).taskId(position).build();
		if (position == workflows.size() + 1) {
			workflows.add(newWorkflow);
		} else if (position <= workflows.size()) {
			for (int i = position - 1; i < workflows.size(); i++) {
				ProjectWorkflow workflow = workflows.get(i);
				workflow.setTaskId(workflow.getTaskId() + 1);
			}
			workflows.add(position - 1, newWorkflow);
		}

		projectWorkflowRepo.saveAll(workflows);

		return ResponseEntity.ok("Name added successfully at position " + position);
	}

}
