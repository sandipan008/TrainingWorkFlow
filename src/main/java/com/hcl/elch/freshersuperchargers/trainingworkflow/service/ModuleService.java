package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;

@Service
public class ModuleService {
	
	@Autowired
	private ModuleRepo moduleRepo;

	public List<Modules> getModules() {
		
		return moduleRepo.findAll();
	}
	
	public String addModule(Modules m) {
		Modules m1;
		if (m.getDuration() == 0 || m.getExam() == null || m.getGroupId() == null || m.getModuleId() == 0
				|| m.getModuleName() == null || m.getPOC() == null) {
			return "Some Values are missing...";
		}
		if (moduleRepo.getByModuleId(m.getModuleId()) != null) {
			return "Module id " + m.getModuleId() + " already exist";
		}
		if (moduleRepo.getBymoduleName(m.getModuleName().toUpperCase()) != null) {
			return "ModuleName " + m.getModuleName() + "already exist";
		}
		if(!(m.getExam().equals("0") || m.getExam().equals("1")) || !(m.getPOC().equals("0")) || m.getPOC().equals("1")) {
			return "Exam or POC value should be '0' or '1' only.";
		}
		m1 = new Modules();
		m1.setModuleId(m.getModuleId());
		m1.setDuration(m.getDuration());
		m1.setExam(m.getExam());
		m1.setGroupId(m.getGroupId());
		m1.setModuleName(m.getModuleName().toUpperCase());
		m1.setPOC(m.getPOC());

		moduleRepo.save(m1);

		return "Modules added succesfully";
	}
	
	public String update(Modules m, long id) {
	
		Modules m1=moduleRepo.getByModuleId(id);
				
		if(m1==null) {
			return "Module with id "+id+" doesn't exist";
		}
	
		if(m.getDuration()==0 ||m.getExam()==null || m.getGroupId()==null || m.getModuleId()==0 || m.getModuleName()==null || m.getPOC()==null) {
			return "Some Values are missing...";
		}
		if(!(m.getExam().equals("0") || m.getExam().equals("1")) || !(m.getPOC().equals("0")) || m.getPOC().equals("1")) {
			return "Exam or POC value should be '0' or '1' only.";
		}
		
			m1.setDuration(m.getDuration());
			m1.setExam(m.getExam());
			m1.setGroupId(m.getGroupId());
			m1.setModuleName(m.getModuleName().toUpperCase());
			m1.setPOC(m.getPOC());
			
			moduleRepo.save(m1);
			
			return "Updated Successfully ...";
		}

}
