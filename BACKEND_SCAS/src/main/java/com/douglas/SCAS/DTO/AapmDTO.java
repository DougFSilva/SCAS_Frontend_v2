package com.douglas.SCAS.DTO;

import java.time.LocalDate;

import com.douglas.SCAS.enums.Semestre;
import com.douglas.SCAS.model.Aapm;

public class AapmDTO {

	private Integer id;

	private String aluno;

	private String curso;

	private String turma;

	private LocalDate dataPagamento;

	private Semestre semestre;

	private String recibo;

	private Float valor;

	public AapmDTO(Aapm aapm) {
		super();
		this.id = aapm.getId();
		this.aluno = aapm.getAluno().getNome();
		this.curso = aapm.getAluno().getTurma().getCurso().getAreaTecnologica();
		this.turma = aapm.getAluno().getTurma().getCodigo();
		this.dataPagamento = aapm.getDataPagamento();
		this.semestre = aapm.getSementre();
		this.recibo = aapm.getRecibo();
		this.valor = aapm.getValor();
	}

	public Integer getId() {
		return id;
	}

	public String getAluno() {
		return aluno;
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

	public String getCurso() {
		return curso;
	}

	public String getTurma() {
		return turma;
	}

}
