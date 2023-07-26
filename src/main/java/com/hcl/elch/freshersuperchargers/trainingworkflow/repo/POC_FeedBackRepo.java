package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.POC_Feedback;

@Repository
public interface POC_FeedBackRepo extends JpaRepository<POC_Feedback, Long> {

}
