package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;

public interface ModuleRepo extends JpaRepository<Modules, Long>{
	Modules getBymoduleName(String Name);

}
