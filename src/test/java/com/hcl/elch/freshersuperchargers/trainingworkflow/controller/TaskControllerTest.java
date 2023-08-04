package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Modules;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.UserTaskException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.ModuleRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.UserRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.CategoryService;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.TaskServiceImpl;

class TaskControllerTest {

	@Mock
	private RuntimeService runtimeServiceMock;

	@Mock
	private DelegateExecution execution;

	@Mock
	private TaskServiceImpl taskService;

	@Mock
	private CategoryService categoryService;

	@Mock
	private UserRepo userRepo;

	@Mock
	private ModuleRepo moduleRepo;

	@Mock
	private TaskRepo tr;

	@Mock
	private RuntimeService runtimeServiceMock2;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private TaskController taskController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Disabled
	public void testGetDetails() throws CamundaException {
		// Arrange
		Task task = Task.builder().id(2).taskId(5).userId(111L).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Task> httpEntity = new HttpEntity<>(task, httpHeaders);

		ResponseEntity<String> responseEntity = new ResponseEntity<>("response_body", HttpStatus.OK);

		when(restTemplate.postForEntity(eq("http://localhost:9002/engine-rest/process-definition/key/Decision/start"),
				eq(httpEntity), eq(String.class))).thenReturn(responseEntity);

		// Act
		taskController.getDetails(task);

		// Assert
//		verify(restTemplate, times(1)).postForEntity(eq("http://localhost:9002/engine-rest/process-definition/key/Decision/start"), eq(httpEntity), eq(String.class));
		verifyNoMoreInteractions(restTemplate);
	}

	@Test
	void testCamundaTask() throws UserTaskException {
		// Mock data
		Task task = Task.builder().id(2).taskId(5).userId(111L).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();

		User user = User.builder().sapId(111L).name("Sandipan").email("sandipan@hcl").build();

		when(userRepo.findBysapId(111L)).thenReturn(user);

		User result = taskController.camundaTask(task);

		verify(userRepo, times(1)).findBysapId(111L);

		assertEquals("sandipan@hcl", result.getEmail());
	}

	@Test
	public void testCategory_ModuleFound() throws UserTaskException {

		Task task = Task.builder().id(2).taskId(5).userId(111L).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();

		Modules module = Modules.builder().moduleId(1).moduleName("JAVA").Exam("1").POC("1").groupId("1").build();

		when(moduleRepo.getBymoduleName(task.getTask().toUpperCase())).thenReturn(module);

		Modules result = taskController.category(task);

		assertEquals(module, result);
		verify(moduleRepo, times(1)).getBymoduleName(task.getTask().toUpperCase());

	}

	@Test
	public void testCategory_ModuleNotFound() throws UserTaskException, CamundaException {

		Task task = Task.builder().id(2).taskId(5).userId(111L).Status("InProgress").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 24)).build();

		when(moduleRepo.getBymoduleName("JAVA")).thenReturn(null);
		when(tr.getById(2L)).thenReturn(task);

//		taskController.getDetails(task);
//		Modules result = taskController.category(task);//2
		assertThrows(UserTaskException.class, () -> taskController.category(task));
//		assertEquals(module, result);
//		verify(moduleRepo, times(1)).getBymoduleName("JAVA");
//		verify(tr, times(1)).getById(1L);
//		verify(tr, times(1)).save(task);
//		verify(taskMock,times(1)).setStatus(eq("Error"));	
//		verify(tr,times(1)).save(eq(taskMock));
//		assertNull(result);
//		assertEquals("Error", task.getStatus());
	}

	@Test
	@Disabled
	void testExecute() throws Exception {
		DelegateExecution executionMock = mock(DelegateExecution.class);
		
		Modules modules = Modules.builder().moduleId(1).moduleName("JAVA").Exam("1").POC("1").groupId("1").build();
		Task task = Task.builder().id(2).taskId(5).userId(111L).Status("InProgress").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 24)).build();
		User user = User.builder().sapId(111L).name("Sandipan").email("sandipan@hcl").build();
		
		when(executionMock.getVariable("glob")).thenReturn(task);
		when(moduleRepo.getBymoduleName("JAVA")).thenReturn(modules);
		when(userRepo.findBysapId(111L)).thenReturn(user);
//		when(tr.getById(taskController.glob.getId())).thenReturn(task);
		when(taskController.camundaTask(task)).thenReturn(user);
		when(taskController.category(task)).thenReturn(modules);
		
	
		
		taskController.execute(executionMock);
	
		
		verify(executionMock, times(1)).setVariable("Email", "sandipan@hcl");
		verify(executionMock, times(1)).setVariable("mainid", task);
		verify(executionMock, times(1)).setVariable("test","1");
		verify(executionMock, times(1)).setVariable("poc", "1");
		verify(executionMock, times(1)).setVariable("groupId", "1");
		verify(executionMock, times(1)).setVariable("moduleId", 1);
		verify(executionMock, times(1)).setVariable("task", "JAVA");
		verify(executionMock, times(1)).setVariable("TaskId", 5);
		verify(executionMock, times(1)).setVariable("userId", 111L);
		verify(executionMock, times(1)).setVariable("status", "InProgress");
		verify(executionMock, times(1)).setVariable("duedate", LocalDate.of(2023, 6, 24));
	}
}