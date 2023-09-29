package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;

public interface ProjectWorkflowRepo extends JpaRepository<ProjectWorkflow, Long> {

	List<ProjectWorkflow> findBySapId(long sapId);

	@Query("SELECT p FROM ProjectWorkflow p WHERE p.sapId = :sapId ORDER BY p.id")
	List<ProjectWorkflow> findprojectWorkflowBySapId(@Param("sapId") long sapId);
}
