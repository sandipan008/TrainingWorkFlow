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
@Table(name="Modules")
public class Modules {
	
	@Id
	@Column(name="moduleId")
	long moduleId;
	
	@Column(name = "moduleName")
	private String moduleName;
	
	@Column(name="Exam")
	private String Exam;
	
	@Column(name="groupId")
	private String groupId;
	
	@Column(name="duration")
	private int duration;
	
	@Column(name="POC")
	private String POC;

}
