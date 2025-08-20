package com.douglas.SCAS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.douglas.SCAS.model.PontoFuncionario;

public interface PontoFuncionarioRepository extends CrudRepository<PontoFuncionario, Integer> {

	List<PontoFuncionario> findByFuncionarioId(Integer id);

	List<PontoFuncionario> findByFuncionarioMatricula(Integer matricula);

}
