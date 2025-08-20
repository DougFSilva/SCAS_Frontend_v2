package com.douglas.SCAS.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.SCAS.DTO.AapmDTO;
import com.douglas.SCAS.DTO.AapmFORM;
import com.douglas.SCAS.model.Aapm;
import com.douglas.SCAS.model.Aluno;
import com.douglas.SCAS.model.InvestimentoAapm;
import com.douglas.SCAS.repository.AapmRepository;
import com.douglas.SCAS.repository.AlunoRepository;
import com.douglas.SCAS.repository.InvestimentoAapmRepository;
import com.douglas.SCAS.service.Exception.ObjectNotFoundException;

@Service
public class AapmService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AapmRepository repository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private InvestimentoAapmRepository investimentoAapmRepository;

	public Aapm create(Integer idAluno, AapmFORM aapmFORM) {
		return fromFORM(idAluno, aapmFORM);
	}

	public void delete(Integer id) {
		findById(id);
		repository.deleteById(id);
	}

	public Aapm update(Integer id, @Valid AapmFORM aapmFORM) {
		Aapm aapm = findById(id);
		aapm.setDataPagamento(aapmFORM.getDataPagamento());
		aapm.setRecibo(aapmFORM.getRecibo());
		aapm.setSementre(aapmFORM.getSemestre());
		aapm.setValor(aapmFORM.getValor());
		return repository.save(aapm);
	}

	public Aapm findById(Integer id) {
		Optional<Aapm> aapm = repository.findById(id);
		if (aapm.isEmpty()) {
			throw new ObjectNotFoundException("Aapm com id: " + id + " não encontrada na base de dados!");
		}
		return aapm.get();
	}

	public AapmDTO findByIdDTO(Integer id) {
		Optional<Aapm> aapm = repository.findById(id);
		if (aapm.isEmpty()) {
			throw new ObjectNotFoundException("Aapm com id: " + id + " não encontrada na base de dados!");
		}
		return toDTO(aapm.get());
	}

	public List<AapmDTO> findAll() {

		return toDTOList(repository.findAll());
	}

	public List<AapmDTO> findAllByAluno(Integer idAluno) {
		Optional<Aluno> aluno = alunoRepository.findById(idAluno);
		if (aluno.isEmpty()) {
			throw new ObjectNotFoundException("Aluno com id: " + idAluno + " não encontrado na base de dados!");
		}

		return toDTOList(repository.findAllByAluno(aluno.get()));
	}

	private AapmDTO toDTO(Aapm aapm) {
		return new AapmDTO(aapm);
	}

	private List<AapmDTO> toDTOList(List<Aapm> aapm) {
		List<AapmDTO> aapmDTO = new ArrayList<>();
		aapm.forEach(a -> {
			aapmDTO.add(new AapmDTO(a));
		});
		return aapmDTO;
	}

	private Aapm fromFORM(Integer idAluno, AapmFORM aapmFORM) {
		Optional<Aluno> aluno = alunoRepository.findById(idAluno);
		if (aluno.isEmpty()) {
			throw new ObjectNotFoundException("Aluno com id: " + idAluno + " não encontrado na base de dados!");
		}
		Aapm aapm = new Aapm();
		aapm.setAluno(aluno.get());
		aapm.setDataPagamento(aapmFORM.getDataPagamento());
		aapm.setSementre(aapmFORM.getSemestre());
		aapm.setRecibo(aapmFORM.getRecibo());
		aapm.setValor(aapmFORM.getValor());
		return repository.save(aapm);
	}

	public InvestimentoAapm investimentoCreate(InvestimentoAapm investimentoAapm) {

		return investimentoAapmRepository.save(investimentoAapm);
	}

	public void investimentoDelete(Integer id) {
		investimentoFindById(id);
		investimentoAapmRepository.deleteById(id);
		return;
	}

	public InvestimentoAapm investimentoUpdate(Integer id, InvestimentoAapm investimento) {
		InvestimentoAapm investimentoAapm = investimentoFindById(id);
		investimentoAapm.setData(investimento.getData());
		investimentoAapm.setInvestimento(investimento.getInvestimento());
		investimentoAapm.setValor(investimento.getValor());
		return investimentoAapmRepository.save(investimentoAapm);
	}

	public List<InvestimentoAapm> investimentoFindAll() {
		List<InvestimentoAapm> investimentosAapm = investimentoAapmRepository.findAll();
		return (List<InvestimentoAapm>) investimentosAapm;
	}

	public InvestimentoAapm investimentoFindById(Integer id) {
		Optional<InvestimentoAapm> investimento = investimentoAapmRepository.findById(id);
		return investimento.orElseThrow(
				() -> new ObjectNotFoundException("Objeto com id " + id + " não encontrado na base de dados"));

	}

}
