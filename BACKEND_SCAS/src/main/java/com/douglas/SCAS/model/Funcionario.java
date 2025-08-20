package com.douglas.SCAS.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.douglas.SCAS.enums.EntradaSaida;

@Entity
public class Funcionario extends Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "O campo matrícula é requerido")
	private Integer matricula;

	private String empresa;

	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.REMOVE)
	private List<PontoFuncionario> ponto;

	@Enumerated(EnumType.STRING)
	private EntradaSaida entradaSaida;

	public Funcionario() {

	}

	public Funcionario(String nome, String sexo, String rg, String email, String telefone, String cidade,
			String dataNascimento, Integer tag, Integer matricula, String empresa) {
		super(nome, sexo, rg, email, telefone, cidade, dataNascimento, tag);
		this.matricula = matricula;
		this.empresa = empresa;
		this.entradaSaida = EntradaSaida.LIVRE_ACESSO;
	}

	public Integer getMatricula() {
		return matricula;
	}

	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Integer getId() {
		return id;
	}

	public EntradaSaida getEntradaSaida() {
		return entradaSaida;
	}

	public void setEntradaSaida(EntradaSaida entradaSaida) {
		this.entradaSaida = entradaSaida;
	}

}
