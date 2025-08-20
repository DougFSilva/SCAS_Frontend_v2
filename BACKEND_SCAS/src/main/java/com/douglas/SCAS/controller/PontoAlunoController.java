package com.douglas.SCAS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.douglas.SCAS.model.PontoAluno;
import com.douglas.SCAS.service.PontoAlunoService;

@Controller
@RequestMapping(value = "/pontoAluno")
@PreAuthorize("isAuthenticated()")
public class PontoAlunoController {

	@Autowired
	private PontoAlunoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<List<PontoAluno>> findByAlunoId(@PathVariable Integer id) {
		List<PontoAluno> pontoAluno = service.findByAlunoId(id);
		return ResponseEntity.ok().body(pontoAluno);
	}

	@GetMapping(value = "/matricula/{matricula}")
	public ResponseEntity<List<PontoAluno>> findByAlunoMatricula(@PathVariable Integer matricula) {
		List<PontoAluno> pontoAluno = service.findByAlunoMatricula(matricula);
		return ResponseEntity.ok().body(pontoAluno);
	}

}
