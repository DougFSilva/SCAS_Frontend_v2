package com.douglas.SCAS.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.SCAS.model.PontoAluno;
import com.douglas.SCAS.repository.PontoAlunoRepository;

@Service
public class PontoAlunoService {

	@Autowired
	private PontoAlunoRepository repository;

	public void register(PontoAluno pontoAluno) {
		repository.save(pontoAluno);

	}

	public List<PontoAluno> findByAlunoId(Integer id) {
		List<PontoAluno> pontoAluno = repository.findByAlunoId(id);
		return pontoAluno;
	}

	public List<PontoAluno> findByAlunoMatricula(Integer matricula) {
		List<PontoAluno> pontoAluno = repository.findByAlunoMatricula(matricula);
		return pontoAluno;
	}

	public List<PontoAluno> findAll() {
		return (List<PontoAluno>) repository.findAll();
	}

	public List<PontoAluno> findByTimestampBetween(LocalDateTime localDateTimeAfter,
			LocalDateTime localDateTimeBefore) {
		return (List<PontoAluno>) repository.findByTimestampBetween(localDateTimeAfter, localDateTimeBefore);
	}

}
