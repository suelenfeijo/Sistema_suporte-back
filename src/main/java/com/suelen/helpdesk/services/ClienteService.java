package com.suelen.helpdesk.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.Cliente;
import com.suelen.helpdesk.domain.Pessoa;
import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.dtos.ChamadoDsDTO;
import com.suelen.helpdesk.domain.dtos.ClienteDTO;
import com.suelen.helpdesk.domain.enums.Status;
import com.suelen.helpdesk.repositories.ChamadoDsRepository;
import com.suelen.helpdesk.repositories.ChamadoRepository;
import com.suelen.helpdesk.repositories.ClienteRepository;
import com.suelen.helpdesk.repositories.PessoaRepository;
import com.suelen.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.suelen.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ChamadoRepository chamadoRepository;
	
	@Autowired
	private ChamadoDsRepository chamadoDsRepository;
	
	
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente create(ClienteDTO objDTO) {
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return repository.save(newObj);
	}

	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		Cliente oldObj = findById(id);
		
		if(!objDTO.getSenha().equals(oldObj.getSenha())) 
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		
		validaPorCpfEEmail(objDTO);
		oldObj = new Cliente(objDTO);
		return repository.save(oldObj);
	}

	public void delete(Integer id) {
		Cliente obj = findById(id);
		Chamado chama = new Chamado(obj.getChamados());

		if (obj.getChamados().size() > 0) {
		    chama.setId(obj.getChamados().get(0).getId());
		}

		chama.setCliente(obj);
		
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
		        throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		    }
		}

		

		obj.deleteChamados();

	
		repository.deleteById(id);
		
	}
	
	
	/*Métodos auxiliares de validação de email e cpf*/
	private void validaPorCpfEEmail(ClienteDTO objDTO) {
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