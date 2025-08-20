package com.douglas.SCAS.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.SCAS.enums.EntradaSaida;
import com.douglas.SCAS.model.AcessoCatraca;
import com.douglas.SCAS.model.Aluno;
import com.douglas.SCAS.model.Aula;
import com.douglas.SCAS.model.Feriado;
import com.douglas.SCAS.model.Funcionario;
import com.douglas.SCAS.model.Ocorrencia;
import com.douglas.SCAS.model.PontoAluno;
import com.douglas.SCAS.model.PontoFuncionario;
import com.douglas.SCAS.repository.AlunoRepository;
import com.douglas.SCAS.repository.AulaRepository;
import com.douglas.SCAS.repository.FeriadoRepository;
import com.douglas.SCAS.repository.FuncionarioRepository;

@Service
public class AcessoCatracaService {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private FeriadoRepository feriadoRepository;

	@Autowired
	private AulaRepository aulaRepository;

	@Autowired
	private PontoAlunoService pontoAlunoService;

	@Autowired
	private PontoFuncionarioService pontoFuncionarioService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private OcorrenciaService ocorrenciaService;

	public AcessoCatraca acessoCatraca(Integer tag) {

		LocalDateTime timestamp = LocalDateTime.now();
		Optional<Aluno> aluno = alunoRepository.findByTag(tag);

		if (aluno.isPresent()) {
			List<Ocorrencia> ocorrencia = ocorrenciaService.findAllByAlunoIdAndBloqueio(aluno.get().getId(), true);
			if (ocorrencia.size() > 0) {
				AcessoCatraca acessoCatraca = new AcessoCatraca(aluno.get().getNome(), "Aluno", timestamp, false,
						"Aluno bloqueado por ocorrência", aluno.get().getEntradaSaida());
				return acessoCatraca;
			}

			if (aluno.get().isDesbloqueioTemporario() == true) {
				PontoAluno pontoAluno = new PontoAluno(timestamp, aluno.get().getEntradaSaida(), aluno.get());
				pontoAlunoService.register(pontoAluno);
				AcessoCatraca acessoCatraca = new AcessoCatraca(aluno.get().getNome(), "Aluno", timestamp, true,
						"Acesso permitido", aluno.get().getEntradaSaida());
				alunoService.updateEntradaSaida(aluno.get());
				aluno.get().setDesbloqueioTemporario(false);
				aluno.get().setFaltasConsecutivas(0);
				alunoService.save(aluno.get());
				return acessoCatraca;

			}

			Optional<Feriado> feriado = feriadoRepository.findByData(timestamp.toLocalDate());
			if (feriado.isPresent()) {
				AcessoCatraca acessoCatraca = new AcessoCatraca(aluno.get().getNome(), "Aluno", timestamp, false,
						"Acesso negado, hoje é feriado de " + feriado.get().getDescricao(),
						aluno.get().getEntradaSaida());
				return acessoCatraca;
			}
			Optional<Aula> aula = aulaRepository.findByDiaAulaAndTurma_id(timestamp.toLocalDate(),
					aluno.get().getTurma().getId());
			if (aula.isEmpty()) {
				AcessoCatraca acessoCatraca = new AcessoCatraca(aluno.get().getNome(), "Aluno", timestamp, false,
						"Acesso negado, hoje a turma " + aluno.get().getTurma().getCodigo() + " não tem aula",
						aluno.get().getEntradaSaida());
				return acessoCatraca;
			}

			if (validaAcessoAluno(aluno.get(), timestamp)) {

				PontoAluno pontoAluno = new PontoAluno(timestamp, aluno.get().getEntradaSaida(), aluno.get());
				pontoAlunoService.register(pontoAluno);
				AcessoCatraca acessoCatraca = new AcessoCatraca(aluno.get().getNome(), "Aluno", timestamp, true,
						"Acesso permitido", aluno.get().getEntradaSaida());
				alunoService.updateEntradaSaida(aluno.get());
				aluno.get().setFaltasConsecutivas(0);
				alunoService.save(aluno.get());
				return acessoCatraca;

			}

			AcessoCatraca acessoCatraca = new AcessoCatraca(aluno.get().getNome(), "Aluno", timestamp, false,
					"Acesso negado, fora do horário definido", aluno.get().getEntradaSaida());
			return acessoCatraca;

		}

		Optional<Funcionario> funcionario = funcionarioRepository.findByTag(tag);
		if (funcionario.isPresent()) {

			PontoFuncionario pontoFuncionario = new PontoFuncionario(timestamp, funcionario.get().getEntradaSaida(),
					funcionario.get());
			pontoFuncionarioService.register(pontoFuncionario);
			AcessoCatraca acessoCatraca = new AcessoCatraca(funcionario.get().getNome(), "Funcionario", timestamp, true,
					"Acesso permitido", funcionario.get().getEntradaSaida());
			funcionarioService.updateEntradaSaida(funcionario.get());
			return acessoCatraca;
		}

		AcessoCatraca acessoCatraca = new AcessoCatraca("Não existente", "Aluno", timestamp, false,
				"Acesso negado, tag não cadastrada na base de dados", EntradaSaida.INEXISTENTE);
		return acessoCatraca;
	}

	private boolean validaAcessoAluno(Aluno aluno, LocalDateTime timestamp) {
		Integer timeNow = timestamp.toLocalTime().toSecondOfDay();
		Integer timeEntrada = aluno.getTurma().getEntrada().toSecondOfDay();
		Integer timeSaida = aluno.getTurma().getSaida().toSecondOfDay();
		Integer timeAlmocoSaida = aluno.getTurma().getAlmocoSaida().toSecondOfDay();
		Integer timeAlmocoEntrada = aluno.getTurma().getAlmocoEntrada().toSecondOfDay();
		Integer toleranciaEntrada = aluno.getTurma().getToleranciaEntrada() * 60;
		Integer toleranciaSaida = aluno.getTurma().getToleranciaSaida() * 60;
		switch (aluno.getEntradaSaida()) {
		case ENTRADA:
			if (timeEntrada - timeNow <= toleranciaEntrada && timeEntrada - timeNow >= 0) {
				return true;
			}
			return false;

		case SAIDA:
			if (timeNow - timeSaida >= 0 && timeNow - timeSaida <= toleranciaSaida) {
				return true;
			}
			return false;

		case ALMOCO_ENTRADA:

			if (timeAlmocoEntrada - timeNow >= 0) {
				return true;
			}
			return false;

		case ALMOCO_SAIDA:
			if (timeNow > timeAlmocoEntrada && (timeNow - timeSaida >= 0 && timeNow - timeSaida <= toleranciaSaida)) {
				aluno.setEntradaSaida(EntradaSaida.SAIDA);
				return true;
			}

			if (timeNow - timeAlmocoSaida >= 0 && timeNow < timeAlmocoEntrada) {
				return true;
			}

			return false;

		case LIVRE_ACESSO:

			return true;

		case INEXISTENTE:
			return false;
		}

		return false;
	}

}
