package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.AssessmentRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;

@Service
public class AssessmentService {
	
	@Autowired
	private AssessmentRepo assessmentRepo;

	@Autowired
	private ModuleRepo moduleRepo;
	
	public List<String> findAssesmentById(int id) {
		return assessmentRepo.findAssesment(id);
		
	}
	
	public List<Map<String,Object>> getAllAssesmentLink() {
		
		List<Map<String,Object>> ans= new ArrayList<>();
		List<Modules>module=moduleRepo.findAll();
		for(int i=0;i<module.size();i++) {
			Map<String,Object>mp=new HashMap<>();
			List<String> assesment = assessmentRepo.findAssesment(module.get(i).getModuleId());
			mp.put("Module_Id", module.get(i).getModuleId());
			mp.put("links", assesment);
			ans.add(mp);
		}
		
		return ans;
	}

}
