package com.douglas.SCAS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.douglas.SCAS.model.Aapm;
import com.douglas.SCAS.model.Aluno;

@Repository
public interface AapmRepository extends JpaRepository<Aapm, Integer> {

	List<Aapm> findAllByAluno(Aluno aluno);

}
