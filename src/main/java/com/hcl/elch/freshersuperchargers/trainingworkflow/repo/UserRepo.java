package com.hcl.elch.freshersuperchargers.trainingworkflow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{

	User findBysapId(long userId);
	List<Modules> findModulesBysapId(Long userId);
}
