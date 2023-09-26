package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Assessment;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Long>{
	
	List<Assessment> findByModuleId(long moduleId);
	@Query(value="SELECT e.assessment_link FROM assessment_master e WHERE e.module_id = ?1",nativeQuery = true)
	List<String> findAssesment(long moduleId);
}
