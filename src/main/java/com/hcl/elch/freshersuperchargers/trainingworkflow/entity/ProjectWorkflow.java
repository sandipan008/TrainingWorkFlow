package com.hcl.elch.freshersuperchargers.trainingworkflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="ProjectWorkflow")
public class ProjectWorkflow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	long Id;
	
	@Column(name = "Name")
	private String Name;
	
	@ManyToOne()
    @JoinColumn(name = "user_sapId", referencedColumnName="sapId")
    private User user;
	
	@Column(name="taskId")
	private long taskId;

}
