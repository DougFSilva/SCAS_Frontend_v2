package com.douglas.SCAS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.SCAS.model.PontoFuncionario;
import com.douglas.SCAS.repository.PontoFuncionarioRepository;

@Service
public class PontoFuncionarioService {

	@Autowired
	private PontoFuncionarioRepository repository;

	@Autowired
	private FuncionarioService funcionarioService;

	public void register(PontoFuncionario pontoFuncionario) {
		repository.save(pontoFuncionario);
	}

	public List<PontoFuncionario> findByFuncionarioId(Integer id) {
		List<PontoFuncionario> pontoFuncionario = repository.findByFuncionarioId(id);
		return pontoFuncionario;
	}

	public List<PontoFuncionario> findByFuncionarioMatricula(Integer matricula) {
		funcionarioService.findByMatricula(matricula);
		List<PontoFuncionario> pontoFuncionario = repository.findByFuncionarioMatricula(matricula);
		return pontoFuncionario;
	}

}
