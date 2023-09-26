package com.hcl.elch.freshersuperchargers.trainingworkflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name="Workflow")
public class workflow {
	

	@Id
	@Column(name="Id")
	long Id;
	
	@Column(name = "Name")
	private String Name;
	
	@Column(name="category_Id")
	private long category;
	
	@Column(name="taskId")
	private long taskId;
	
}
