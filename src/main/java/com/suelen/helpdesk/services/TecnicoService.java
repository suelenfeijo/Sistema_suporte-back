package com.suelen.helpdesk.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.Pessoa;
import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.dtos.ChamadoDsDTO;
import com.suelen.helpdesk.domain.dtos.TecnicoDTO;
import com.suelen.helpdesk.domain.enums.Status;
import com.suelen.helpdesk.repositories.ChamadoDsRepository;
import com.suelen.helpdesk.repositories.ChamadoRepository;
import com.suelen.helpdesk.repositories.PessoaRepository;
import com.suelen.helpdesk.repositories.TecnicoRepository;
import com.suelen.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.suelen.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ChamadoRepository chamadoRepository;

	@Autowired
	private ChamadoDsRepository chamadoDsRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public Tecnico create(TecnicoDTO objDTO) {
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}

	public Tecnico findById(Integer id) {

		// opcional -> pode ou não encontrar o elemento pelo id
		Optional<Tecnico> obj = repository.findById(id);

		// ou se não encontrar, retorna nulo
		// return obj.orElse(null);

		// ou se não encontrar, retorna uma exceção
		return obj.orElseThrow(() -> new ObjectNotFoundException("objeto não encontrado Id: " + id));
	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}

	/*
	 * @Param Método update usando o bean enconder bCrypt, serviço de 
	 * encriptografia usando o 512.
	 * 
	 * 
	 * */
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		Tecnico oldObj = findById(id);

		if (!objDTO.getSenha().equals(oldObj.getSenha())) {
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		}
		validaPorCpfEEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		return repository.save(oldObj);
	}

	/*
	 * @Param Método deleta um técnico.
	 * esse método procura por id um tecnico
	 * antes da exclusão, copia todos os chamados do tecnico
	 * e salva em uma lista : dsList
	 * atualiza a tabela chamadoDsRepository.saveAll(dsList);
	 * 
	 * verifica se o tecnico possui algum chamado em aberto, caso ! 
	 * de encerrado E NÃO é vazio a lista de chamados do tecnico
	 * realiza desassociação (remove chave estrangeira) e realiza o delete, caso não,
	 * dispara uma exceção que o tecnico possui chamados
	 * 
	 * 
	 * */
	public void delete(Integer id) {
		Tecnico obj = findById(id);
		Chamado chama = new Chamado(obj.getChamados());

		if (obj.getChamados().size() > 0) {
		    chama.setId(obj.getChamados().get(0).getId());
		}

		chama.setTecnico(obj);
		
		List<ChamadoDsDTO> dsList = new ArrayList<>();
		for (Chamado chamado : obj.getChamados()) {
		    ChamadoDsDTO ds = new ChamadoDsDTO(chamado);
		    dsList.add(ds);
		    
		}
		chamadoDsRepository.saveAll(dsList);

		List<Chamado> chamados = obj.getChamados();
		for (int i = 0; i < chamados.size(); i++) {
			
		    Chamado chamado = chamados.get(i);
		    if (chamado.getStatus() != Status.ENCERRADO && !obj.getChamados().isEmpty()) {
		        throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
		    }
		}

		

		obj.deleteChamados();

	
		repository.deleteById(id);
		
	}

	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}

}
