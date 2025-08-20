package com.douglas.SCAS.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.douglas.SCAS.model.Feriado;
import com.douglas.SCAS.service.FeriadoService;

@RestController
@RequestMapping(value = "/feriado")
public class FeriadoController {

	@Autowired
	private FeriadoService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Feriado> create(@Valid @RequestBody Feriado feriado) {

		Feriado newFeriado = service.create(feriado);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newFeriado.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<Feriado>> findAll() {
		List<Feriado> feriados = service.findAll();
		return ResponseEntity.ok().body(feriados);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Feriado> findById(@PathVariable Integer id) {
		Feriado feriado = service.findById(id);
		return ResponseEntity.ok().body(feriado);
	}

}
