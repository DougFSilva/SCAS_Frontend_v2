package com.douglas.SCAS.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.douglas.SCAS.model.PontoAluno;

public interface PontoAlunoRepository extends CrudRepository<PontoAluno, Integer> {

	List<PontoAluno> findByAlunoId(Integer id);

	List<PontoAluno> findByAlunoMatricula(Integer matricula);

	List<PontoAluno> findByTimestampBetween(LocalDateTime localDateTimeAfter, LocalDateTime localDateTimeBefore);

}
