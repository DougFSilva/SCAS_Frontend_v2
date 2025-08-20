package com.douglas.SCAS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.douglas.SCAS.model.InvestimentoAapm;

@Repository
public interface InvestimentoAapmRepository extends JpaRepository<InvestimentoAapm, Integer> {

}
