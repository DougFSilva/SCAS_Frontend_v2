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

import com.douglas.SCAS.DTO.AapmDTO;
import com.douglas.SCAS.DTO.AapmFORM;
import com.douglas.SCAS.model.Aapm;
import com.douglas.SCAS.model.InvestimentoAapm;
import com.douglas.SCAS.service.AapmService;

@RestController
@RequestMapping(value = "/aapm")
public class AapmController {

	@Autowired
	private AapmService service;

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PostMapping(value = "/create/{idAluno}")
	public ResponseEntity<Aapm> create(@PathVariable Integer idAluno, @Valid @RequestBody AapmFORM aapmFORM) {
		Aapm aapm = service.create(idAluno, aapmFORM);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(aapm.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Aapm> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Aapm> update(@PathVariable Integer id, @Valid @RequestBody AapmFORM aapmFORM) {
		System.out.println(aapmFORM.getSemestre());
		System.out.println(aapmFORM.getRecibo());
		System.out.println(aapmFORM.getValor());
		System.out.println(aapmFORM.getDataPagamento());
		Aapm aapm = service.update(id, aapmFORM);
		return ResponseEntity.ok().body(aapm);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<AapmDTO>> findAll() {
		List<AapmDTO> aapmDTO = service.findAll();
		return ResponseEntity.ok().body(aapmDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/aluno/{idAluno}")
	public ResponseEntity<List<AapmDTO>> findAllByAluno(@PathVariable Integer idAluno) {
		List<AapmDTO> aapmDTO = service.findAllByAluno(idAluno);
		return ResponseEntity.ok().body(aapmDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<AapmDTO> findById(@PathVariable Integer id) {
		AapmDTO aapmDTO = service.findByIdDTO(id);
		return ResponseEntity.ok().body(aapmDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/investimento/create")
	public ResponseEntity<InvestimentoAapm> investimentoCreate(@RequestBody InvestimentoAapm investimento) {
		InvestimentoAapm investimentoAapm = service.investimentoCreate(investimento);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(investimentoAapm.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/investimento/{id}")
	public ResponseEntity<InvestimentoAapm> investimentoDeleteById(@PathVariable Integer id) {
		service.investimentoDelete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/investimento/{id}")
	public ResponseEntity<InvestimentoAapm> investimentoUpdate(@PathVariable Integer id,
			@RequestBody InvestimentoAapm investimento) {
		InvestimentoAapm investimentoAapm = service.investimentoUpdate(id, investimento);
		return ResponseEntity.ok().body(investimentoAapm);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/investimento")
	public ResponseEntity<List<InvestimentoAapm>> investimentoFindAll() {
		List<InvestimentoAapm> investimentoAapm = service.investimentoFindAll();
		return ResponseEntity.ok().body(investimentoAapm);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/investimento/{id}")
	public ResponseEntity<InvestimentoAapm> investimentoFindById(@PathVariable Integer id) {
		InvestimentoAapm investimentoAapm = service.investimentoFindById(id);
		return ResponseEntity.ok().body(investimentoAapm);
	}

}
