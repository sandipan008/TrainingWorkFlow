package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.workflow;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.DroolsEngineException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ProjectWorkflowRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.WorkflowRepo;

class TaskServiceTest {

	@Mock
	private TaskRepo taskRepo;

	@Mock
	private WorkflowRepo workflowRepo;
	
	@Mock
	private ModuleRepo moduleRepo;
	
	@Mock
	 private ProjectWorkflowRepo pr;	
	@Mock
	private KieContainer kieContainer;

	@InjectMocks
	private TaskServiceImpl taskService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Disabled("Test case is currently under development")
	void testGetStatus() throws DroolsEngineException {
		// Create test data
		Task task = Task.builder().id(2).taskId(4).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		Category category = new Category(111L, "group2");
		List<workflow> workflows = new ArrayList<>();
		workflow workflow = new workflow();
		workflow.setTaskId(4);
		workflows.add(workflow);
		
		Modules module=Modules.builder()
				.moduleId(1)
				.moduleName("JAVA")
				.Exam("1")
				.groupId("1")
				.POC("1")
				.build();
		// Configure mock repositories
		when(workflowRepo.findBycategory(111L)).thenReturn(workflows);
		when(moduleRepo.getBymoduleName("JAVA")).thenReturn(module);
//		KieSession kieSession = mock(KieSession.class);
//		when(kieContainer.newKieSession()).thenReturn(kieSession);
		// Invoke the method under test
		Task result = taskService.getStatus(task, category);
//		Task t1=new Task();
//		Modules m1 = new Modules();
//		verify(kieContainer).newKieSession();
//		verify(kieSession).setGlobal("m",m1);
//		verify(kieSession).setGlobal("t1", t1);
//		verify(kieSession).insert(task);
//		verify(kieSession).insert(category);
//		verify(kieSession).fireAllRules();
//		verify(kieSession).dispose();
		// Assert the result
		assertEquals(task, result);
	}

	@Test
	@Disabled("Test case is currently under development")
	void testGetStatusLastWorkflow() throws DroolsEngineException {

		Task task = Task.builder().id(2).taskId(5).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		Category category = new Category(111L, "group2");
		List<workflow> workflows = new ArrayList<>();
		workflow workflow = new workflow();
		workflow.setTaskId(4);
		workflows.add(workflow);
		Modules module=Modules.builder()
				.moduleId(1)
				.moduleName("JAVA")
				.Exam("1")
				.groupId("1")
				.POC("1")
				.build();
		when(workflowRepo.findBycategory(111L)).thenReturn(workflows);
		when(moduleRepo.getBymoduleName("DB")).thenReturn(module);
		 
		Task result=taskService.getStatus(task, category);

		
		
		assertEquals("Completed", result.getStatus());
		assertEquals(task, result);
	}

	@Test
	void testSave_TaskNotCompleted() {

		Task task = Task.builder().taskId(1).userId(111).Status("InProgress").task("Java")
				.duedate(LocalDate.of(2023, 6, 4)).build();

		taskService.save(task);

		verify(taskRepo).save(task);
	}

	@Test
	void testSave_TaskCompleted() {

		Task task = Task.builder().taskId(2).userId(112).Status("Completed").task("DB")
				.duedate(LocalDate.of(2023, 6, 4)).build();

		taskService.save(task);

		verifyNoInteractions(taskRepo);
	}

	@Test
	void testSetComplete() {

		Task task = Task.builder().id(2).taskId(4).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		List<Task> tasks = new ArrayList<>();
		tasks.add(task);
	
		// Configure mock repositories
		when(taskRepo.getByuserId(111L)).thenReturn(tasks);
		when(taskRepo.getById(2L)).thenReturn(task);
	
		// Invoke the method under test
		taskService.setComplete(task);
	
		// Verify the interactions
		verify(taskRepo).getByuserId(111L);
		verify(taskRepo).getById(2L);
		verify(taskRepo).save(task);
	}
	
	@Test
	void testGetByUserId() throws Exception {
		// Create test data
		Task task = Task.builder().id(2).taskId(4).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		List<Task> tasks = new ArrayList<>();
		tasks.add(task);

		when(taskRepo.getByuserId(111L)).thenReturn(tasks);

		List<Task> result = taskService.getByUserId(111L);

		verify(taskRepo).getByuserId(111L);

		assertEquals(tasks, result);
	}
	@Test
	void testGetByUserId_Exception() throws Exception {
		// Create test data
		Task task = Task.builder().id(2).taskId(4).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		List<Task> tasks = new ArrayList<>();
		tasks.add(task);
	
		// Configure mock repository
		when(taskRepo.getByuserId(111L)).thenThrow(new RuntimeException());
	
		// Invoke the method under test
		
	
		Assertions.assertThrows(Exception.class, () ->{  
				taskService.getByUserId(111L);
			});
	
		// Assert the result
		
	}
}
