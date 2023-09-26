package com.hcl.elch.freshersuperchargers.trainingworkflow.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Master_Gender")
public class Gender implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2185102443411015457L;
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer uid;
	@Id
	private String key;
	private String value;

}
