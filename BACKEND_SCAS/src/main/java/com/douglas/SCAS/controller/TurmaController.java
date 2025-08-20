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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.douglas.SCAS.DTO.TurmaDTO;
import com.douglas.SCAS.DTO.TurmaFORM;
import com.douglas.SCAS.model.Turma;
import com.douglas.SCAS.service.TurmaService;

@RestController
@RequestMapping(value = "turma")
public class TurmaController {

	@Autowired
	private TurmaService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "create/{idCurso}")
	public ResponseEntity<Turma> create(@PathVariable Integer idCurso, @Valid @RequestBody TurmaFORM turmaFORM) {
		Turma turma = service.create(idCurso, turmaFORM);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(turma.getId()).toUri();
		return ResponseEntity.created(uri).build();

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Turma> update(@PathVariable Integer id, @Valid @RequestBody TurmaFORM turmaFORM) {
		Turma turma = service.update(id, turmaFORM);
		return ResponseEntity.ok().body(turma);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<TurmaDTO>> findAll() {
		List<TurmaDTO> turmasDTO = service.findAllDTO();
		return ResponseEntity.ok().body(turmasDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<TurmaDTO> findById(@PathVariable Integer id) {
		TurmaDTO turmaDTO = service.findByIdDTO(id);
		return ResponseEntity.ok().body(turmaDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/curso/{idCurso}")
	public ResponseEntity<List<TurmaDTO>> findAllByCursoId(@PathVariable Integer idCurso) {
		List<TurmaDTO> turmasDTO = service.findAllByCursoIdDTO(idCurso);
		return ResponseEntity.ok().body(turmasDTO);
	}

}
