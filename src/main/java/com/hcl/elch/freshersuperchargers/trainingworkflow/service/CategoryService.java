package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.NotFoundException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.CategoryRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepo cr;

	@Autowired
	private TaskRepo tr;
	
	public List<Category> getAllCatetory(){
		return cr.findAll();
	}
	
	protected final static Logger log = LogManager.getLogger(CategoryService.class.getName());

	public Category get(long user, long id) throws NotFoundException {

		try {
			Optional<Category>optional=cr.findById(user);
			if(optional.isEmpty()) {
				log.info("No Category value found");
			}
			
			Category c = optional.get();
			return c;
		} catch (Exception e) {
			Optional<Task>optional=tr.findById(id);
			if(optional.isEmpty()) {
				log.info("No Task value found");
			}
			Task t = optional.get();
			t.setStatus("Error");
			tr.save(t);
			throw new NotFoundException("Exception Occured unable to fetch values of id, Id is null or Incorrect ");

		}

	}

}
