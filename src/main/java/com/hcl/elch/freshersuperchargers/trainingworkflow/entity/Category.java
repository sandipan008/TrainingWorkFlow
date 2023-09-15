package com.hcl.elch.freshersuperchargers.trainingworkflow.entity;

import java.io.Serializable;

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
@Table(name="category_master")
public class Category implements Serializable {
	
	private static final long serialVersionUID = 5812545298367977722L;

	@Id
	@Column(name="Id")
	long userId;
	
	@Column(name = "name")
	private String category;
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String  getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}