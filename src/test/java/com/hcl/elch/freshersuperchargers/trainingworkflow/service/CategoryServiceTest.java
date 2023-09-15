package com.hcl.elch.freshersuperchargers.trainingworkflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Category;
import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.CategoryRepo;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryServiceTest {

	@InjectMocks
	private CategoryService categoryService;

	@InjectMocks
	private TaskServiceImpl taskService;

	@Mock
	private CategoryRepo categoryRepo;

	@Mock
	private TaskRepo taskRepo;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetCategory_Success() throws Exception {
		long userId = 111L;
		long id = 1L;
		Category category = Category.builder().userId(111L).category("group1").build();
		Task task = new Task();
		task.setId(1);
		when(categoryRepo.findById(111L)).thenReturn(Optional.of(category));
		Category found = categoryService.get(userId, id);

		assertEquals(userId, found.getUserId());

	}

	@Test
	void testGetCategory_Exception() {
		Task task = Task.builder().id(2).taskId(4).userId(111).Status("Completed").task("JAVA")
				.duedate(LocalDate.of(2023, 6, 4)).approver("Sandipan").build();
		when(categoryRepo.findById(110L)).thenThrow(new RuntimeException());
		when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
		Task savedTask = taskRepo.findById(1L).get();
		Assertions.assertThrows(Exception.class, () -> categoryService.get(111L,1L));

		verify(categoryRepo, times(1)).findById(111L);

		verify(taskRepo).save(savedTask);
		Assertions.assertEquals("Error", savedTask.getStatus());
	}
}
