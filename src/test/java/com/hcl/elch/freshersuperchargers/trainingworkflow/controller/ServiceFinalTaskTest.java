//package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.hcl.elch.freshersuperchargers.trainingworkflow.controller.ServiceFinalTask;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.User;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.DroolsEngineException;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.CategoryRepo;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.UserRepo;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;
//import com.hcl.elch.freshersuperchargers.trainingworkflow.service.TaskServiceImpl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import javax.mail.MessagingException;
//
//import org.camunda.bpm.engine.delegate.DelegateExecution;
//
//public class ServiceFinalTaskTest {
//
//	@Mock
//	private TaskServiceImpl taskService;
//
//	@Mock
//	private TaskRepo taskRepo;
//
//	@Mock
//	private CategoryRepo categoryRepo;
//
//	@Mock
//	private UserRepo userRepo;
//
//	@Mock
//	private EmailSenderService emailSenderService;
//
//	@InjectMocks
//	private ServiceFinalTask serviceFinalTask;
//
//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//
////	@Test
////	@Disabled
////	public void testExecute() throws Exception {
////// Mock the necessary variables
////		DelegateExecution execution = mock(DelegateExecution.class);
////		when(execution.getVariable("duedate")).thenReturn("2023-07-10");
////		when(execution.getVariable("userId")).thenReturn("111");
////		when(execution.getVariable("TaskId")).thenReturn("5");
////		when(execution.getVariable("username")).thenReturn("Sandipan Dandapat");
////		when(execution.getVariable("Email")).thenReturn("sandipan@hcl.com");
////		when(execution.getVariable("task")).thenReturn("JAVA");
////		when(execution.getVariable("status")).thenReturn("InProgress");
////		when(execution.getVariable("approver")).thenReturn("Tausif");
////
////// Mock the necessary dependencies
////		Task task = Task.builder().id(1).taskId(4).userId(111L).Status("InProgress").task("JAVA")
////				.duedate(LocalDate.of(2023, 7, 10)).build();
////		
////		Task nextTask = Task.builder().id(2).taskId(5).userId(111L).Status("InProgress").task("DB")
////				.duedate(LocalDate.of(2023, 7, 20)).build();
////
////		Category category = new Category();
////		category.setUserId(111L);
////		category.setCategory("group1");
////		
////		User user = User.builder()
////				.sapId(123L)
////				.name("Sandipan Dandapat")
////				.email("sandipan@hcl.com")
////				.category(category)
////				.build();
////		
//////		when(userRepo.findBysapId(111L)).thenReturn(user);
////
////		when(categoryRepo.findById(111L)).thenReturn(Optional.of(category));
////		
////		
////		
////		when(taskService.getStatus(task, category,"Yes")).thenReturn(nextTask);
////
////// Run the method to be tested
////		serviceFinalTask.execute(execution);
////
////// Verify the interactions
////		verify(taskService, times(1)).setComplete(task);
////		verify(taskService, times(1)).save(nextTask);
////		verify(emailSenderService, times(1)).mailSendingForNextTask("Sandipan Dandapat", "sandipan@hcl.com", "JAVA",
////				LocalDate.parse("2023-07-15"));
////	}
//	
//	@Test
//	public void testNextTask() throws DroolsEngineException {
//		// Mock the necessary variables
//		Task task = Task.builder().id(1).taskId(5).userId(111L).Status("InProgress").task("JAVA")
//				.duedate(LocalDate.of(2023, 7, 10)).build();
//	
//
//		Category category = new Category();
//		category.setUserId(111L);
//		category.setCategory("group1");
//		
//		User user = User.builder()
//				.sapId(123L)
//				.ProjectAssignation("Yes")
//				.name("Sandipan Dandapat")
//				.email("sandipan@hcl.com")
//				.category(category)
//				.build();
//		when(userRepo.findBysapId(123L)).thenReturn(user);
//	
//		when(categoryRepo.findById(111L)).thenReturn(Optional.of(category));
//	
//		Task nextTask = Task.builder().id(2).taskId(1).userId(111L).Status("InProgress").task("DB")
//				.duedate(LocalDate.of(2023, 7, 20)).build();
//		when(taskService.getStatus(task, category, "Yes")).thenReturn(nextTask);
//	
//		// Run the method to be tested
//		serviceFinalTask.nextTask(task,"Yes");
//	
//		// Verify the interactions
//		verify(taskService, times(1)).setComplete(task);
//		verify(taskService, times(1)).save(nextTask);
//	
//		// Assert the result
//		assertEquals("DB", serviceFinalTask.newTask.getTask());
//		assertEquals(LocalDate.of(2023, 7, 20), serviceFinalTask.newTask.getDuedate());
//		
//	}
//}