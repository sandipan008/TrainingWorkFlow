package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.exceptions.CamundaException;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

@SpringBootTest
public class EmailForUnSuccessTest {

	@Mock
	private DelegateExecution execution;

	@Mock
	private EmailSenderService senderService;

	@Mock
	private TaskRepo tr;

	@InjectMocks
	private EmailForUnSuccess emailForUnSuccess;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testExecute() throws CamundaException {
// Mock variables
		String email = "Sandipan@hcl";
		String task = "JAVA";
		String username = "Sandipan";
		Task taskMock = Task.builder().id(2).taskId(4).userId(111).Status("InProgress").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();

// Set up execution variable mocks
		when(execution.getVariable("Email")).thenReturn(email);
		when(execution.getVariable("task")).thenReturn(task);
		when(execution.getVariable("username")).thenReturn(username);
		when(execution.getVariable("mainid")).thenReturn(taskMock);

// Set up repository mock
		when(tr.save(taskMock)).thenReturn(taskMock);

// Mock the email sending success
		when(senderService.mailSendingFailureTask(username, email, task.toUpperCase()))
				.thenReturn("Email sent successfully.");

// Call the execute method
		emailForUnSuccess.execute(execution);

// Verify that the task entity is updated correctly
		verify(tr, times(1)).save(taskMock);
//		assertEquals(LocalDate.of(2023, 7, 15), LocalDate.of(2023, 7, 15).plusDays(2));
		assertEquals(taskMock.getStatus(), "InProgress");

// Verify that the email sending service is called
		verify(senderService, times(1)).mailSendingFailureTask(username, email, task.toUpperCase());
	}
}