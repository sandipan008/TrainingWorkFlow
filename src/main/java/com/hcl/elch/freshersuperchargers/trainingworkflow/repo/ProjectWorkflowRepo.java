package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;

public interface ProjectWorkflowRepo extends JpaRepository<ProjectWorkflow, Long> {

	List<ProjectWorkflow> findByuser_sapId(long sapId);

	@Query("SELECT p FROM ProjectWorkflow p WHERE p.user.sapId = :sapId ORDER BY p.id")
	List<ProjectWorkflow> findprojectWorkflowByUserSapId(@Param("sapId") long sapId);
}
