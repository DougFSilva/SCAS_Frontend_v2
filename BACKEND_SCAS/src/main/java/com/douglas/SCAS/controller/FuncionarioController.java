package com.douglas.SCAS.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

import com.douglas.SCAS.DTO.FuncionarioDTO;
import com.douglas.SCAS.DTO.ImageFORM;
import com.douglas.SCAS.model.Funcionario;
import com.douglas.SCAS.service.FuncionarioService;

@RestController
@RequestMapping(value = "/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create")
	@CacheEvict(value = {"funcionarioFindAll"}, allEntries = true )
	public ResponseEntity<Funcionario> create(@Valid @RequestBody Funcionario funcionario) {
		Funcionario newFuncionario = service.create(funcionario);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newFuncionario.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	@CacheEvict(value = {"funcionarioFindAll"}, allEntries = true )
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}")
	@CacheEvict(value = {"funcionarioFindAll"}, allEntries = true )
	public ResponseEntity<Funcionario> update(@PathVariable Integer id, @Valid @RequestBody Funcionario funcionario) {
		Funcionario newFuncionario = service.update(id, funcionario);
		return ResponseEntity.ok().body(newFuncionario);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	@Cacheable(value = "funcionarioFindAll")
	public ResponseEntity<List<FuncionarioDTO>> findAll() {
		List<FuncionarioDTO> funcionariosDTO = service.FindAll();
		return ResponseEntity.ok().body(funcionariosDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<FuncionarioDTO> findById(@PathVariable Integer id) {
		FuncionarioDTO funcionarioDTO = service.findByIdDTO(id);
		return ResponseEntity.ok().body(funcionarioDTO);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/tag/{tag}")
	public ResponseEntity<Funcionario> findByTag(@PathVariable Integer tag) {
		Funcionario funcionario = service.findByTag(tag);
		return ResponseEntity.ok().body(funcionario);
	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PostMapping(value = "/saveImage/{id}")
	@CacheEvict(value = {"funcionarioFindAll"}, allEntries = true )
	public ResponseEntity<Void> saveImage(@PathVariable Integer id, @RequestBody ImageFORM imageFORM) {
		service.saveImage(id, imageFORM);
		return ResponseEntity.ok().build();

	}
}
