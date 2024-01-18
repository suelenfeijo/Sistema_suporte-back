package com.suelen.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.suelen.helpdesk.domain.Pessoa;
import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.dtos.TecnicoDTO;
import com.suelen.helpdesk.repositories.PessoaRepository;
import com.suelen.helpdesk.repositories.TecnicoRepository;
import com.suelen.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.suelen.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {

	@Autowired
	private	TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	
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
	
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		Tecnico oldObj = findById(id);
		
		if(!objDTO.getSenha().equals(oldObj.getSenha())) {
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		}
		validaPorCpfEEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		return repository.save(oldObj);
	}
	
	public void delete(Integer id) {
		Tecnico obj = findById(id);

		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}

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
