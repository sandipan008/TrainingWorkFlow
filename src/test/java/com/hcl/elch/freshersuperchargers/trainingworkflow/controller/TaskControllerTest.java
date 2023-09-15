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
		
		Task task = Task.builder().id(2).taskId(4).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content_Type", "application/json");
		HttpEntity<Task> httpEntity = new HttpEntity<>(task, httpHeaders);

		when(tr.getById(1L)).thenReturn(task);

		taskController.getDetails(task);

		verify(tr).save(task);
		verify(restTemplate).postForEntity("http://localhost:9002/engine-rest/process-definition/key/Decision/start",
				httpEntity, String.class);
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
	@Disabled("Test case is currently under development")
	void testExecute() throws Exception {
		DelegateExecution executionMock = mock(DelegateExecution.class);

		Modules modules = Modules.builder().moduleId(1).moduleName("JAVA").Exam("1").POC("1").groupId("1").build();
		Task task = Task.builder().id(2).taskId(5).userId(111L).Status("InProgress").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 24)).build();
		User user = User.builder().sapId(111L).name("Sandipan").email("sandipan@hcl").ProjAssignedStatus(false)
				.build();

		when(tr.getById(1L)).thenReturn(task);
        when(userRepo.findBysapId(1234L)).thenReturn(user);
        when(moduleRepo.getBymoduleName("Test Task")).thenReturn(modules);

        taskController.execute(execution);

        verify(execution).setVariable("ErrorId", "1");
        verify(execution).setVariable("Email", "sandipan@hcl.com");
        verify(execution).setVariable("username", "Sandipan");
        verify(execution).setVariable("ProjectAssignation", false);
        verify(execution).setVariable("mainid", task);
        verify(execution).setVariable("test", "1");
        verify(execution).setVariable("groupId", 1L);
        verify(execution).setVariable("moduleId", 1L);
        verify(execution).setVariable("poc", "1");
        verify(execution).setVariable("task", "JAVA");
        verify(execution).setVariable("TaskId", "5");
        verify(execution).setVariable("userId", "111L");
        verify(execution).setVariable("status", "InProgress");
        verify(execution).setVariable("duedate", LocalDate.of(2023, 9, 01));
	}
}