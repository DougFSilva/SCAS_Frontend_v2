package com.douglas.SCAS.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InvestimentoAapm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate data;

	private String investimento;

	private float valor;

	public InvestimentoAapm() {
		super();
	}

	public InvestimentoAapm(LocalDate data, String investimento, float valor) {
		super();
		this.data = data;
		this.investimento = investimento;
		this.valor = valor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getInvestimento() {
		return investimento;
	}

	public void setInvestimento(String investimento) {
		this.investimento = investimento;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

}
