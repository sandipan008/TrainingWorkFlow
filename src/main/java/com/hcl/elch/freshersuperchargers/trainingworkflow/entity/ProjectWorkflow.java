package com.hcl.elch.freshersuperchargers.trainingworkflow.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
	@Column(name="Id")
	long Id;
	
	@Column(name = "Name")
	private String Name;
	
	@ManyToOne()
	@JoinColumn(name = "user_sapId", referencedColumnName="sapId")
	private User user;
	
	@Column(name="taskId")
	private long taskId;
	
	//	public long getId() {
	//	return Id;
	//}
	//
	//public void setId(long id) {
	//	Id = id;
	//}
	//
	//public String getName() {
	//	return Name;
	//}
	//
	//public void setName(String name) {
	//	Name = name;
	//}
	//
	//public User getUser() {
	//	return user;
	//}
	//
	//public void setUser(User user) {
	//	this.user = user;
	//}
	//
	//public long getTaskId() {
	//	return taskId;
	//}
	//
	//public void setTaskId(long taskId) {
	//	this.taskId = taskId;
	//}
	//

}
