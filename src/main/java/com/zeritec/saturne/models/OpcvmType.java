package com.zeritec.saturne.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "opcvm_type")
@JsonIgnoreProperties({"parent"})
@Data
public class OpcvmType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String label;
	
	private String code; 

	@ManyToOne(fetch = FetchType.LAZY)
	private OpcvmType parent;
	
	@OneToMany(mappedBy = "parent")
	private List<OpcvmType> subType;
}
