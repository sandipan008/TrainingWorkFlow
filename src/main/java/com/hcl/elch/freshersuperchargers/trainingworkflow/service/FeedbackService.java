package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Feedback;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.POC_Feedback;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.FeedbackRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.POC_FeedBackRepo;

@Service
public class FeedbackService {
	
	@Autowired
	private FeedbackRepo feedbackRepo;
	
	@Autowired
	private POC_FeedBackRepo poc_FeedBackRepo;
	
	
	public List<Feedback> getTaskFeedback(){
		return feedbackRepo.findAll();
		
	}
	
	public List<POC_Feedback> getPocFeedback(){
		return poc_FeedBackRepo.findAll();
		
	}

}
