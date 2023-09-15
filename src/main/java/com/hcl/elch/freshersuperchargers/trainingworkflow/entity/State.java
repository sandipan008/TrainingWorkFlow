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
@Table(name = "master_state")
public class State implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2836647268096360617L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer uid;

	@Id
	private String key;
	private String value;

}
