package com.douglas.SCAS.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.douglas.SCAS.model.Feriado;

@Repository
public interface FeriadoRepository extends CrudRepository<Feriado, Integer> {

	Optional<Feriado> findByData(LocalDate data);

}
