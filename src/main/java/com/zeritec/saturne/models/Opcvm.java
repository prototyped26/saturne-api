package com.zeritec.saturne.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "opcvm")
@Data
public class Opcvm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String label;
	
	private Double number;
	
	private Double cours;
	
	private Double value;
	
	private Double percent;
	
	private String status; // T for total or E for element
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "opc_id")
	private Opc opc;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "opcvm_type_id")
	private OpcvmType opcvmType;
}
