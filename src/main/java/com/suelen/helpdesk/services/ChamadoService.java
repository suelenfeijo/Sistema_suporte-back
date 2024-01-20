package com.suelen.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.Cliente;
import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.dtos.ChamadoDTO;
import com.suelen.helpdesk.domain.dtos.ChamadoDsDTO;
import com.suelen.helpdesk.domain.enums.Prioridade;
import com.suelen.helpdesk.domain.enums.Status;
import com.suelen.helpdesk.repositories.ChamadoDsRepository;
import com.suelen.helpdesk.repositories.ChamadoRepository;
import com.suelen.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository repository;
	
	@Autowired
	private ChamadoDsRepository repositoryds;
	@Autowired
	private TecnicoService tecnicoService;
	@Autowired
	private ClienteService clienteService;

	
	/*
	 * @Param Método Criar chamado menos boilerPlate
	 * 
	 * */
	public Chamado create(ChamadoDTO obj) {
		return repository.save(newChamado(obj));
	}

	/*
	 * @Param Método Atualizar chamado
	 * 
	 * */
	public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
		objDTO.setId(id);
		Chamado oldObj = findById(id);
		oldObj = newChamado(objDTO);
		return repository.save(oldObj);
	}
	
	/*
	 * @Param Método procurar por id chamado
	 * 
	 * */
	public Chamado findById(Integer id) {
		Optional<Chamado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
	}
	
	
	/*
	 * @Param Método procurar chamados de desassociados (clientes e funcionários exluidos
	 * com chamados associados ) 
	 * 
	 * */
	public List<ChamadoDsDTO> findDessaciadosChamados() {
	    return repositoryds.findAll();
	}

	/*
	 * @Param Método listar todos chamados ) 
	 * 
	 * */
	public List<Chamado> findAll() {
		return repository.findAll();
	}

	/*
	 * @Param Método criar chamado ) 
	 * 
	 * */
	private Chamado newChamado(ChamadoDTO obj) {
		
		// tecnico = entityManager.find(Tecnico.class, 1L);
		// cliente = entityManager.find(Cliente.class, 1L);

			Chamado chamado = new Chamado();
		    Tecnico tecnico = tecnicoService.findById(obj.getTecnico());
		    Cliente cliente = clienteService.findById(obj.getCliente());
		    
		
	
		if(obj.getId() != null) {
			chamado.setId(obj.getId());
		}
		
		if(obj.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
	    chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
		chamado.setStatus(Status.toEnum(obj.getStatus()));
		chamado.setTitulo(obj.getTitulo());
		chamado.setObservacoes(obj.getObservacoes());
		return chamado;
	}

	/*
	 * @Param Método 
	 * Procurar desassociados por id (view) ) 
	 * 
	 * */
	public ChamadoDsDTO findByIdDesassociados(Integer id) {
		Optional<ChamadoDsDTO> obj = repositoryds.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
	
	}

}