package com.douglas.SCAS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.douglas.SCAS.model.Alarm;
import com.douglas.SCAS.model.Aluno;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {

	List<Alarm> findAllByAluno(Aluno aluno);

}
