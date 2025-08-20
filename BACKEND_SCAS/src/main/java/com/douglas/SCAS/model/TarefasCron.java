package com.douglas.SCAS.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.douglas.SCAS.DTO.OcorrenciaFORM;
import com.douglas.SCAS.enums.EntradaSaida;
import com.douglas.SCAS.enums.TipoOcorrencia;
import com.douglas.SCAS.repository.AlunoRepository;
import com.douglas.SCAS.service.AlarmService;
import com.douglas.SCAS.service.AlunoService;
import com.douglas.SCAS.service.OcorrenciaService;
import com.douglas.SCAS.service.PontoAlunoService;
import com.douglas.SCAS.service.TurmaService;

@Component
public class TarefasCron {

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private TurmaService turmaService;

	@Autowired
	private PontoAlunoService pontoAlunoService;

	@Autowired
	private OcorrenciaService ocorrenciaService;

	@Autowired
	private AlarmService alarmService;

	private Float faltas;

	private Float aulas;

	private boolean contain;

	@Scheduled(cron = "0 59 23 * * *")
	public void uptadeBloqueio() {
		HashSet<Aluno> alunos = (HashSet<Aluno>) alunoService.findAllByDesbloqueioTemporario(true);
		alunos.forEach(aluno -> {
			aluno.setDesbloqueioTemporario(false);
			alunoRepository.save(aluno);

		});
	}

	@Scheduled(cron = "15 59 23 * * *")
	public void updateEntradaSaida() {
		HashSet<Aluno> alunos = (HashSet<Aluno>) alunoService.findAllByEntradaSaida(EntradaSaida.SAIDA);
		alunos.addAll((HashSet<Aluno>) alunoService.findAllByEntradaSaida(EntradaSaida.ALMOCO_ENTRADA));
		alunos.addAll((HashSet<Aluno>) alunoService.findAllByEntradaSaida(EntradaSaida.ALMOCO_SAIDA));
		alunos.forEach(aluno -> {
			aluno.setEntradaSaida(EntradaSaida.ENTRADA);
			alunoRepository.save(aluno);

		});
	}

	@Scheduled(cron = "30 59 23 * * *")
	public void updateFaltas() {
		List<Aluno> alunosEmAula = new ArrayList<>();
		List<Aluno> alunosPresentes = new ArrayList<>();

		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDateTime localDateTimeAfter = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
				localDateTime.getDayOfMonth(), 0, 0, 0);
		LocalDateTime localDateTimeBefore = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
				localDateTime.getDayOfMonth(), 23, 59, 59);

		List<Turma> turmas = turmaService.findAllByAulasDiaAula(localDateTime.toLocalDate());

		turmas.forEach(turma -> {
			List<Aluno> alunos = alunoService.findByTurma(turma.getCodigo());
			alunosEmAula.addAll(alunos);
		});

		List<PontoAluno> pontoAlunos = pontoAlunoService.findByTimestampBetween(localDateTimeAfter,
				localDateTimeBefore);

		pontoAlunos.forEach(ponto -> {
			alunosPresentes.add(ponto.getAluno());
		});

		alunosEmAula.removeAll(alunosPresentes);

		alunosEmAula.forEach(aluno -> {
			OcorrenciaFORM ocorrencia = new OcorrenciaFORM(false, TipoOcorrencia.FALTA, "Falta no dia", true);
			ocorrencia.setRegistrador("sistema");
			ocorrenciaService.create(aluno.getId(), ocorrencia);
			alunoService.updateFaltasConsecutivas(aluno);

		});

	}

	@Scheduled(cron = "45 59 23 * * *")
	public void alarmFaltas() {
		List<Turma> turmasAtivas = new ArrayList<>();
		List<Turma> turmas = turmaService.findAll();
		turmas.forEach(turma -> {
			if (!turma.getCodigo().equals("EGRESSO") && !turma.getCodigo().equals("EVADIDO")) {
				turmasAtivas.add(turma);
			}
		});
		turmasAtivas.forEach(turma -> {
			List<Aluno> alunos = alunoService.findByTurma(turma.getCodigo());
			if (alunos.size() > 0) {
				this.aulas = (float) turma.getAulas().size();
				alunos.forEach(aluno -> {
					this.faltas = (float) 0;
					List<Ocorrencia> ocorrencias = ocorrenciaService.findAllByAlunoIdSystem(aluno.getId());
					ocorrencias.forEach(ocorrencia -> {
						if (ocorrencia.getTipo() == TipoOcorrencia.FALTA) {
							this.faltas = this.faltas + 1;
						}
					});

					if (this.faltas > 0 && this.aulas > 0) {
						float porcentagemFaltas = (this.faltas / this.aulas) * 100;
						if (porcentagemFaltas > 15) {
							List<Alarm> alarmes = alarmService.findAllByAluno(aluno);
							this.contain = false;
							alarmes.forEach(alarme -> {
								if (alarme.getDescricao().contains((int) porcentagemFaltas + "%")) {
									;
									this.contain = true;
								}
							});
							if (this.contain == false) {
								Alarm alarme = new Alarm(LocalDate.now(), aluno,
										"Aluno tem " + (int) porcentagemFaltas + "% de faltas", true);
								alarmService.create(alarme);
							}

						}
					}
				});
			}

		});

	}

}
