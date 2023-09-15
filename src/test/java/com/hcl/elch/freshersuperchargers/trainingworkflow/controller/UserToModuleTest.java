package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.ProjectWorkflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ProjectWorkflowRepo;

@SpringBootTest
public class UserToModuleTest {
	
	@InjectMocks
	private UserToModuleController userToModuleController;
	
	@Mock
	private ProjectWorkflowRepo projectWorkflowRepo;
	
	@Mock
	private ModuleRepo moduleRepo;
	
	@BeforeEach
	public void setUp() {
		
	}
//	@Test
//    void testGetModulesBySapId() {
//        long userSapId = 12345L;
//        
//        Category category = Category.builder().userId(userSapId).category("2").build();
//        
//        User user = User.builder().sapId(userSapId).name("Sandipan").category(category).ProjAssignedStatus(true).build();
//        		
//
//        // Create a mock list of ProjectWorkflow objects
//        ProjectWorkflow projectWorkflow1 = ProjectWorkflow.builder().Id(1).Name("NEW_HIRE").taskId(1L).user(user).build();
//        ProjectWorkflow projectWorkflow2 = ProjectWorkflow.builder().Id(2).Name("JAVA").taskId(2L).user(user).build();
//
//        List<ProjectWorkflow> mockWorkflows=new ArrayList<>();
//        mockWorkflows.add(projectWorkflow1);
//        mockWorkflows.add(projectWorkflow2);
//        // Mock the behavior of projectWorkflowRepo.findprojectWorkflowByUserSapId
//        when(projectWorkflowRepo.findprojectWorkflowByUserSapId(userSapId)).thenReturn(mockWorkflows);
//        // Call the controller method
//        HashMap<Long, String> response = userToModuleController.getModulesBysapId(userSapId);
//
//        // Assert the expected response
//        assertNotNull(response);
//        assertEquals(2, response.size());
//        assertEquals("NEW_HIRE", response.get(1L));
//        assertEquals("JAVA", response.get(2L));
//    }

	    

	    @Test
	    public void testGetModulesBysapId() {
	        // Mock data
	        long userSapId = 521300L;
	        List<ProjectWorkflow> workflows = new ArrayList<>();
	        workflows.add(ProjectWorkflow.builder().Name("NEW_HIRE").taskId(1).build());
	        workflows.add(ProjectWorkflow.builder().Name("JAVA").taskId(2).build());
	        Mockito.when(projectWorkflowRepo.findprojectWorkflowByUserSapId(userSapId)).thenReturn(workflows);

	        // Perform the test
	        HashMap<Long, String> response = userToModuleController.getModulesBysapId(userSapId);

	        // Assertions
	        assertEquals(2, response.size());
	        assertEquals("NEW_HIRE", response.get(1L));
	        assertEquals("JAVA", response.get(2L));
	    }

	    @Test
	    public void testAddNameToPosition_ValidPosition() {
	        // Mock data
	    	Long userSapId = 5216001L;
	        int position = 2;
	        String name = "DATABASE";
	        List<ProjectWorkflow> workflows = new ArrayList<>();
	        User user = User.builder().sapId(userSapId).build();
	        workflows.add(ProjectWorkflow.builder().Name("NEW_HIRE").taskId(1).user(user).build());
	        workflows.add(ProjectWorkflow.builder().Name("JAVA").taskId(2).user(user).build());
	        Mockito.when(projectWorkflowRepo.findByuser_sapId(userSapId)).thenReturn(workflows);
	        Mockito.when(moduleRepo.getBymoduleName(name)).thenReturn(new Modules());

	        // Perform the test
	        ResponseEntity<Object> response = userToModuleController.addNameToPosition(userSapId, position, name);

	        // Assertions
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals("Name added successfully at position 2", response.getBody());
	    }

	    @Test
	    @Disabled("Test case is on progress")
	    public void testAddNameToPosition_InvalidPosition() {
	        // Mock data
	        Long userSapId = 521300L;
	        int position = 3;
	        String name = "DATABASE";
	        List<ProjectWorkflow> workflows = new ArrayList<>();
	        User user = User.builder().sapId(userSapId).build();
	        workflows.add(ProjectWorkflow.builder().Name("NEW_HIRE").taskId(1).user(user).build());
	        workflows.add(ProjectWorkflow.builder().Name("JAVA").taskId(2).user(user).build());
	        Mockito.when(projectWorkflowRepo.findByuser_sapId(userSapId)).thenReturn(workflows);

	        // Perform the test
	        ResponseEntity<Object> response = userToModuleController.addNameToPosition(userSapId, position, name);

	        // Assertions
	        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	        assertEquals("Invalid position", response.getBody());
	    }

	    @Test
	    public void testAddNameToPosition_ModuleNotFound() {
	        // Mock data
	        Long userSapId = 521300L;
	        int position = 3;
	        String name = "HIBERNATE";
	        List<ProjectWorkflow> workflows = new ArrayList<>();
	        User user = User.builder().sapId(userSapId).build();
	        workflows.add(ProjectWorkflow.builder().Name("NEW_HIRE").taskId(1).user(user).build());
	        workflows.add(ProjectWorkflow.builder().Name("JAVA").taskId(2).user(user).build());
	        Mockito.when(projectWorkflowRepo.findByuser_sapId(userSapId)).thenReturn(workflows);
	        Mockito.when(moduleRepo.getBymoduleName(name)).thenReturn(null);

	        // Perform the test
	        ResponseEntity<Object> response = userToModuleController.addNameToPosition(userSapId, position, name);

	        // Assertions
	        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	        assertEquals("Name is not present in Modules table", response.getBody());
	    }
	
//    @Test
//    void testAddNameToPosition() {
//        long userSapId = 12345L;
//        int position = 2;
//        String name = "New Module";
//
//        // Create a mock list of ProjectWorkflow objects
//        List<ProjectWorkflow> mockWorkflows = new ArrayList<>();
//        mockWorkflows.add(new ProjectWorkflow("Module 1", 1));
//        mockWorkflows.add(new ProjectWorkflow("Module 2", 2));
//
//        // Mock the behavior of projectWorkflowRepo.findByuser_sapId
//        when(projectWorkflowRepo.findByuser_sapId(userSapId)).thenReturn(mockWorkflows);
//
//        // Mock the behavior of moduleRepo.getBymoduleName
//        when(moduleRepo.getBymoduleName(name)).thenReturn(new Modules(name));
//
//        // Call the controller method
//        ResponseEntity<Object> response = userToModuleController.addNameToPosition(userSapId, position, name);
//
//        // Assert the expected response
//        assertNotNull(response);
//        assertEquals(ResponseEntity.ok().getBody(), "Name added successfully at position " + position);
//    }
}
