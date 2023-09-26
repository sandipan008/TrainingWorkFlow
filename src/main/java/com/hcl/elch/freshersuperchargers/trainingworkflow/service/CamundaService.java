package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CamundaService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String camundaUserUrl = "http://localhost:9002/engine-rest/user";
	private final String camundaGroupUrl = "http://localhost:9002/engine-rest/group";

	
//    private final ProcessEngine processEngine;
//    
//    public CamundaService() {
//        this.processEngine = ProcessEngines.getDefaultProcessEngine();
//    }
//
//    public List<User> getCamundaUsers() {
//        IdentityService identityService = processEngine.getIdentityService();
//        return identityService.createUserQuery().list();
//    }
//
//    public List<Group> getCamundaGroups() {
//        IdentityService identityService = processEngine.getIdentityService();
//        return identityService.createGroupQuery().list();
//    }
		
		public String getAllCamundaUsers() {
			return restTemplate.getForObject(camundaUserUrl, String.class);	
		}
		
		public String getAllCamundaGroups() {
			return restTemplate.getForObject(camundaGroupUrl, String.class);
		}
}
