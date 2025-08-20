package com.douglas.SCAS.DTO;

import java.io.Serializable;
import java.time.LocalDate;

import com.douglas.SCAS.enums.Semestre;

public class AapmFORM implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private LocalDate dataPagamento;

	private Semestre semestre;

	private String recibo;

	private Float valor;

	public AapmFORM() {
		super();
	}

	public AapmFORM(LocalDate dataPagamento, Semestre sementre, String recibo, Float valor) {
		super();
		this.dataPagamento = dataPagamento;
		this.semestre = sementre;
		this.recibo = recibo;
		this.valor = valor;
	}

	public Integer getId() {
		return id;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public Semestre getSemestre() {
		return semestre;
	}

	public String getRecibo() {
		return recibo;
	}

	public Float getValor() {
		return valor;
	}

}
