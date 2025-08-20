package com.douglas.SCAS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglas.SCAS.model.PontoFuncionario;
import com.douglas.SCAS.service.PontoFuncionarioService;

@RestController
@RequestMapping(value = "pontoFuncionario")
@PreAuthorize("isAuthenticated()")
public class PontoFuncionarioController {

	@Autowired
	private PontoFuncionarioService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<List<PontoFuncionario>> findByFuncionarioId(@PathVariable Integer id) {
		List<PontoFuncionario> pontoFuncionario = service.findByFuncionarioId(id);
		return ResponseEntity.ok().body(pontoFuncionario);
	}

	@GetMapping(value = "/nif/{nif}")
	public ResponseEntity<List<PontoFuncionario>> findByFuncionarioNif(@PathVariable Integer matricula) {
		List<PontoFuncionario> pontoFuncionario = service.findByFuncionarioMatricula(matricula);
		return ResponseEntity.ok().body(pontoFuncionario);
	}
}
