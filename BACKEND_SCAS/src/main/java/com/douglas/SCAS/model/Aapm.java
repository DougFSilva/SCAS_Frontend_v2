package com.douglas.SCAS.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.douglas.SCAS.enums.Semestre;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Aapm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_id")
	@JsonIgnore
	private Aluno aluno;

	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataPagamento;

	@Enumerated(EnumType.STRING)
	private Semestre sementre;

	private String recibo;

	private Float valor;

	public Aapm() {
		super();
	}

	public Aapm(Aluno aluno, LocalDate dataPagamento, Semestre sementre, String recibo, Float valor) {
		super();
		this.aluno = aluno;
		this.dataPagamento = dataPagamento;
		this.sementre = sementre;
		this.recibo = recibo;
		this.valor = valor;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Semestre getSementre() {
		return sementre;
	}

	public void setSementre(Semestre sementre) {
		this.sementre = sementre;
	}

	public String getRecibo() {
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}

	public Integer getId() {
		return id;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

}
