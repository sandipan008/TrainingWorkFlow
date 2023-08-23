package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import com.hcl.elch.freshersuperchargers.trainingworkflow.controller.TaskController;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;


@SpringBootTest
public class SchedulerServiceTest {


	@Mock
	private TaskController cc;
	
	@Mock
	private TaskRepo taskRepo;

	@InjectMocks
	private SchedulerServiceImpl schedulerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFetchTask_CurrentDateGreaterThanDueDate() {
		// Prepare test data
		LocalDate currDate=LocalDate.now();
		List<Task> tasks = new ArrayList<>();
		Task task1 = Task.builder()
				.id(1)
				.task("Java")
				.Status("InProgress")
				.duedate(currDate.minusDays(1))
				.taskId(1)
				.userId(111L)
				.build();
		
		tasks.add(task1);

		when(taskRepo.findByStatus("InProgress")).thenReturn(tasks);

		schedulerService.fetchTask();

		verify(taskRepo).findByStatus("InProgress");
		
		verify(taskRepo).save(task1);
		boolean currDateGreaterThanDuedate=true;
		assertEquals(currDateGreaterThanDuedate, (task1.getDuedate().compareTo(currDate))<0);
	}

	@Test
	void testFetchTask_CurrentDateNotGreaterThanDueDate() {
		// Prepare test data
		LocalDate currDate=LocalDate.now();
		List<Task> tasks = new ArrayList<>();
		Task task1 = Task.builder()
				.id(1)
				.task("Java")
				.Status("Completed")
				.duedate(currDate.plusDays(2))
				.taskId(1)
				.userId(111L)
				.build();
		
		tasks.add(task1);

		when(taskRepo.findByStatus("InProgress")).thenReturn(tasks);

		schedulerService.fetchTask();

		
		verify(taskRepo).findByStatus("InProgress");
		
		boolean currDateNotGreaterThanDuedate=true;
		assertEquals(currDateNotGreaterThanDuedate, (task1.getDuedate().compareTo(currDate))>0);
	}
}
