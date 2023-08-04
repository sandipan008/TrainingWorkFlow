package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hcl.elch.freshersuperchargers.trainingworkflow.service.EmailSenderService;

import static org.mockito.Mockito.*;

class TaskAssignmentListenerTest {

	@Mock
	private DelegateTask delegateTask;
	
	@Mock
	private EmailSenderService senderService;
	
	@Mock
	private RuntimeService runtimeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Disabled
	void testNotify() {
		TaskAssignmentListener listener = new TaskAssignmentListener();

//		List<User> userList=new ArrayList<>(Arrays.asList(
//					createUser("sandipan","sandipan@hcl"),
//					createUser("abc","abc@hcl")
//				));
		listener.rs = runtimeService;
		listener.senderService = senderService;
		listener.url = "http://example.com/";

		when(delegateTask.getVariable("groupId")).thenReturn("group1");
		when(delegateTask.getId()).thenReturn("taskId");
		when(delegateTask.getAssignee()).thenReturn("assignee");
		when(delegateTask.getVariable("task")).thenReturn("task");
		when(delegateTask.getName()).thenReturn("taskName");
		when(delegateTask.getVariable("username")).thenReturn("username");
//		when(delegateTask.getProcessEngineServices().getIdentityService().createUserQuery().memberOfGroup("1").list()).thenReturn(userList);

		listener.notify(delegateTask);

		verify(delegateTask, times(1)).getVariable("groupId");

	}
//	private User createUser(String firstName,String email) {
//		User user=mock(User.class);
//		when(user.getFirstName()).thenReturn(firstName);
//		when(user.getEmail()).thenReturn(email);
//		return user;
//	}
}