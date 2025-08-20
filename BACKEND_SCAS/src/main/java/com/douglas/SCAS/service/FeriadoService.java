package com.douglas.SCAS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.SCAS.model.Feriado;
import com.douglas.SCAS.repository.FeriadoRepository;
import com.douglas.SCAS.service.Exception.DataIntegratyViolationException;
import com.douglas.SCAS.service.Exception.ObjectNotFoundException;

@Service
public class FeriadoService {

	@Autowired
	private FeriadoRepository repository;

	public Feriado create(Feriado feriado) {
		if (repository.findByData(feriado.getData()).isPresent()) {
			throw new DataIntegratyViolationException(
					"O feriado " + feriado.getDescricao() + " já está cadastrado na base de dados!");
		}

		return repository.save(feriado);

	}

	public void delete(Integer id) {
		findById(id);
		repository.deleteById(id);
	}

	public List<Feriado> findAll() {
		return (List<Feriado>) repository.findAll();

	}

	public Feriado findById(Integer id) {
		Optional<Feriado> feriado = repository.findById(id);
		return feriado.orElseThrow(
				() -> new ObjectNotFoundException("Feriado com id " + id + " não encontrado na base de dados!"));
	}

}
