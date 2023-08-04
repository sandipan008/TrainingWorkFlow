package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmailSenderServiceTest {

	@Autowired
	private JavaMailSender mailSender;

	@InjectMocks
	private EmailSenderService emailSenderService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Disabled
	void testMailSendingForTask() throws Exception {
// Setup test data
		String username = "Sandipan";
		String email = "sandipan@hcl.com";
		String task = "JAVA";
		String assessmentLink = "https://practice.geeksforgeeks.org/problems/search-an-element-in-an-array-1587115621/1?page=1&difficulty[]=-1&category[]=Arrays&sortBy=submissions";
		String expectedSubject = "Assessment for JAVA";

// Configure mocks
		MimeMessage mimeMessage = mock(MimeMessage.class);
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

// Invoke the method under test
		emailSenderService.mailSendingForTask(username, email, task, assessmentLink);

// Verify the interactions
		verify(mailSender).createMimeMessage();
		verify(mailSender).send(mimeMessage);
		verify(mimeMessage).setSubject(expectedSubject);
		verify(mimeMessage).setText("ytuygi");
		verify(mimeMessageHelper).setTo(email);
	}

// Add more test methods for other methods in EmailSenderService

}