package com.douglas.SCAS.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.SCAS.DTO.AulaFORM;
import com.douglas.SCAS.model.Aula;
import com.douglas.SCAS.model.Turma;
import com.douglas.SCAS.repository.AulaRepository;
import com.douglas.SCAS.service.Exception.ObjectNotFoundException;

@Service
public class AulaService {

	@Autowired
	private AulaRepository repository;

	@Autowired
	private TurmaService turmaService;

	public List<Aula> createAll(Integer idTurma, List<AulaFORM> aulasFORM) {
		List<Aula> aulas = new ArrayList<>();
		Turma turma = turmaService.findById(idTurma);
		aulasFORM.forEach(aulaFORM -> {
			LocalDate diaAula = LocalDate.parse(aulaFORM.getAula());
			aulas.add(new Aula(diaAula, turma));
		});
		List<Aula> oldAulas = repository.findAllByTurma_id(idTurma);
		repository.deleteAll(oldAulas);

		return (List<Aula>) repository.saveAll(aulas);

	}

	public void delete(Integer id) {
		findById(id);
		repository.deleteById(id);
		return;
	}

	public void deleteAllByTurma_id(Integer id) {
		repository.deleteAllByTurma_id(id);
		return;

	}

	public Aula findById(Integer id) {
		Optional<Aula> aula = repository.findById(id);
		return aula.orElseThrow(
				() -> new ObjectNotFoundException("Aula com id " + id + "não encontrado na base de dados"));
	}

	public List<Aula> findByDiaAula(String diaAula) {
		LocalDate localDate = LocalDate.parse(diaAula);
		return (List<Aula>) repository.findAllByDiaAula(localDate);
	}

	public List<Aula> findAllByTurma_id(Integer idTurma) {
		return (List<Aula>) repository.findAllByTurma_id(idTurma);
	}

}
