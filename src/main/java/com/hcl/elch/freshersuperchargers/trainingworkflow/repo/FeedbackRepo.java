package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, Long>{

}
