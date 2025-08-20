package com.douglas.SCAS.controller;

import java.io.IOException;
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

import com.douglas.SCAS.DTO.AlunoDTO;
import com.douglas.SCAS.DTO.AlunoFORM;
import com.douglas.SCAS.DTO.ImageFORM;
import com.douglas.SCAS.enums.AlunoStatus;
import com.douglas.SCAS.enums.EntradaSaida;
import com.douglas.SCAS.model.Aluno;
import com.douglas.SCAS.service.AlunoService;

@RestController
@RequestMapping(value = "/aluno")
public class AlunoController {

	@Autowired
	private AlunoService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<Aluno> create(@Valid @RequestBody AlunoFORM alunoFORM) {
		Aluno newAluno = service.create(alunoFORM);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newAluno.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create/all")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<List<Aluno>> createAll(@Valid @RequestBody List<AlunoFORM> listAlunoFORM) {
		List<Aluno> newListAlunoFORM = service.createAll(listAlunoFORM);
		return ResponseEntity.ok().body(newListAlunoFORM);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<Aluno> update(@PathVariable Integer id, @Valid @RequestBody AlunoFORM alunoFORM) {
		Aluno aluno = service.update(id, alunoFORM);
		return ResponseEntity.ok().body(aluno);
	}
	
	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PutMapping(value = "/tag/{id}/{tag}")
	public ResponseEntity<Aluno> updateTag(@PathVariable Integer id, @PathVariable Integer tag) {
		Aluno aluno = service.updateTag(id, tag);
		return ResponseEntity.ok().body(aluno);
	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PutMapping(value = "/entradaSaida/{id}/{entradaSaida}")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<AlunoDTO> UpdateEntradaSaida(@PathVariable Integer id,
			@PathVariable EntradaSaida entradaSaida) {
		AlunoDTO alunoDTO = service.updateEntradaSaida(id, entradaSaida);
		return ResponseEntity.ok().body(alunoDTO);
	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PutMapping(value = "/desbloqueioTemporario/{id}/{desbloqueioTemporario}")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<AlunoDTO> updateDesbloqueioTemporario(@PathVariable Integer id,
			@PathVariable boolean desbloqueioTemporario) {
		AlunoDTO alunoDTO = service.updateDesbloqueioTemporario(id, desbloqueioTemporario);
		return ResponseEntity.ok().body(alunoDTO);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}/status/{status}")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<Aluno> updateStatus(@PathVariable Integer id, @PathVariable AlunoStatus status) {
		Aluno aluno = service.updateStatus(id, status);
		return ResponseEntity.ok().body(aluno);
	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PutMapping(value = "{id}/termoInternet/{termoInternet}")
	public ResponseEntity<AlunoDTO> updateTermoInternet(@PathVariable Integer id, @PathVariable boolean termoInternet) {
		AlunoDTO alunoDTO = service.updatetermoInternet(id, termoInternet);
		return ResponseEntity.ok().body(alunoDTO);

	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PutMapping(value = "{id}/internetLiberada/{internetLiberada}")
	public ResponseEntity<AlunoDTO> updateInternetLiberada(@PathVariable Integer id,
			@PathVariable boolean internetLiberada) {
		AlunoDTO alunoDTO = service.updateInternetLiberada(id, internetLiberada);
		return ResponseEntity.ok().body(alunoDTO);

	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PutMapping(value = "all/internetLiberada/{internetLiberada}")
	public ResponseEntity<List<AlunoDTO>> updateAllInternetLiberada(@RequestBody List<Aluno> alunos,
			@PathVariable boolean internetLiberada) {
		List<AlunoDTO> alunosDTO = service.updateAllInternetLiberada(alunos, internetLiberada);
		return ResponseEntity.ok().body(alunosDTO);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<AlunoDTO>> findAll() throws IOException {
		List<AlunoDTO> alunosDTO = service.findAll();
		return ResponseEntity.ok().body(alunosDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/status/{status}")
	@Cacheable(value = "alunoFindAllByStatus")
	public ResponseEntity<List<AlunoDTO>> findAllByStatus(@PathVariable AlunoStatus status) throws IOException {
		List<AlunoDTO> alunos = service.findAllByStatus(status);
		return ResponseEntity.ok().body(alunos);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/status/{status}/lazy")
	@Cacheable(value = "alunoFindAllByStatusLazy")
	public ResponseEntity<List<AlunoDTO>> findAllByStatusLazy(@PathVariable AlunoStatus status) throws IOException {
		List<AlunoDTO> alunos = service.findAllByStatusLazy(status);
		return ResponseEntity.ok().body(alunos);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<AlunoDTO> findByIdDTO(@PathVariable Integer id) {
		AlunoDTO alunoDTO = service.findByIdDTO(id);
		return ResponseEntity.ok().body(alunoDTO);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/tag/{tag}")
	public ResponseEntity<AlunoDTO> findByTag(@PathVariable Integer tag) {
		AlunoDTO alunoDTO = service.findByTag(tag);
		return ResponseEntity.ok().body(alunoDTO);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<List<Aluno>> findAllByNomeContaining(@PathVariable String nome) {
		List<Aluno> aluno = service.findAllByNomeContaining(nome);
		return ResponseEntity.ok().body(aluno);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/turma/{id}")
	public ResponseEntity<List<AlunoDTO>> findByTurma(@PathVariable Integer id) {
		List<AlunoDTO> alunosDTO = service.findByTurmaDTO(id);
		return ResponseEntity.ok().body(alunosDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/turma/{codigo}/status/{status}")
	public ResponseEntity<List<AlunoDTO>> findByTurmaAndStatus(@PathVariable String codigo,
			@PathVariable AlunoStatus status) {
		List<AlunoDTO> alunosDTO = service.findByTurmaAndStatus(codigo, status);
		return ResponseEntity.ok().body(alunosDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/curso/{idCurso}")
	public ResponseEntity<List<AlunoDTO>> findByCursoId(@PathVariable Integer idCurso) {
		List<AlunoDTO> alunosDTO = service.findByCurso(idCurso);
		return ResponseEntity.ok().body(alunosDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/curso/{idCurso}/status/{status}")
	public ResponseEntity<List<AlunoDTO>> findByCursoAndStatusOrderByNome(@PathVariable Integer idCurso,
			@PathVariable AlunoStatus status) {
		List<AlunoDTO> alunosDTO = service.findByCursoAndStatusOrderByNome(idCurso, status);
		return ResponseEntity.ok().body(alunosDTO);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/move/{idTurmaAtual}/to/{idTurmaDestino}")
	public ResponseEntity<List<Aluno>> move(@PathVariable Integer idTurmaAtual, @PathVariable Integer idTurmaDestino) {
		List<Aluno> alunos = service.move(idTurmaAtual, idTurmaDestino);
		return ResponseEntity.ok().body(alunos);
	}

	@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
	@PostMapping(value = "/saveImage/{id}")
	@CacheEvict(value = {"alunoFindAllByStatusLazy", "alunoFindAllByStatus"}, allEntries = true )
	public ResponseEntity<Void> saveImage(@PathVariable Integer id, @RequestBody ImageFORM imageFORM) {
		service.saveImage(id, imageFORM);
		return ResponseEntity.ok().build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlunoController other = (AlunoController) obj;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}

}
