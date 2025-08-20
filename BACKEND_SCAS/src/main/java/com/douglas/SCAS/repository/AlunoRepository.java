package com.douglas.SCAS.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.douglas.SCAS.enums.AlunoStatus;
import com.douglas.SCAS.enums.EntradaSaida;
import com.douglas.SCAS.model.Aluno;
import com.douglas.SCAS.model.Turma;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer> {

	Optional<Aluno> findByMatricula(Integer matricula);

	Optional<Aluno> findByTag(Integer tag);

	List<Aluno> findAllByNomeContaining(String nome);

	List<Aluno> findAllByTurmaOrderByNome(Turma turma);

	Set<Aluno> findAllByEntradaSaidaOrderByNome(EntradaSaida entradaSaida);

	List<Aluno> findAllByTurmaCodigoOrderByNome(String codigo);

	Set<Aluno> findAllByDesbloqueioTemporarioOrderByNome(boolean resolvido);

	List<Aluno> findAllByStatusOrderByNome(AlunoStatus alunoStatus);

	List<Aluno> findByTurmaAndStatusOrderByNome(Turma turma, AlunoStatus status);

	List<Aluno> findAllByTurmaCursoIdOrderByNome(Integer idCurso);

	List<Aluno> findAllByTurmaCursoIdAndStatusOrderByNome(Integer idCurso, AlunoStatus status);

	List<Aluno> findByTurma(Turma turma);

	List<Aluno> findAllByTurmaIdOrderByNome(Integer idTurmaAtual);

}
