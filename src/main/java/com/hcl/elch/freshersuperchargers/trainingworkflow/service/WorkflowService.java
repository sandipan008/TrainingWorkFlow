package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.CategoryRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.WorkflowRepo;

@Service
public class WorkflowService {
	
	@Autowired
	private WorkflowRepo wr;
	
	@Autowired
	private CategoryRepo cr;
	
	
//	public List<List<workflow>> getModuleByCat(){
//		List<List<workflow>> ans = new ArrayList<>();
//		List<Category> c = cr.findAll();
//		for (int i = 0; i < c.size(); i++) {
//			ans.add(wr.findBycategory(c.get(i).getUserId()));
//		}
//		return ans;
//	}
	public List<Map<String,Object>> getModuleByCat(){
		List<Map<String,Object>> ans = new ArrayList<>();
		List<Category> c = cr.findAll();
		for (int i = 0; i < c.size(); i++) {
			Map<String,Object>mp=new HashMap<>();
			List<workflow> workflow=wr.findBycategory(c.get(i).getUserId());
			mp.put("category_id", c.get(i).getUserId());
			mp.put("modules", workflow);
			ans.add(mp);
		}
		return ans;
	}
//	public Map<Map<String,Long>, List<workflow>> getModuleByCatId() {
//    Map<Map<String,Long>, List<workflow>> ans = new HashMap<>();
//    List<Category> c = cr.findAll();
//    for (Category category : c) {
//        Long categoryId = category.getUserId();
//        Map<String, Long> categoryMap = new HashMap<>();
//        categoryMap.put("categoryId", categoryId);
//        List<workflow> workflows = wr.findBycategory(categoryId);
//        ans.put(categoryMap, workflows);
//    }
//    return ans;
//}

}
