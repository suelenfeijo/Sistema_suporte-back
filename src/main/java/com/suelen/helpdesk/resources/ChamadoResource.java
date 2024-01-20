package com.suelen.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.dtos.ChamadoDTO;
import com.suelen.helpdesk.domain.dtos.ChamadoDsDTO;
import com.suelen.helpdesk.services.ChamadoService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {

	@Autowired
	private ChamadoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id) {
		Chamado obj = service.findById(id);
		return ResponseEntity.ok().body(new ChamadoDTO(obj));
	}

	@GetMapping
	public ResponseEntity<List<ChamadoDTO>> findAll() {
		List<Chamado> list = service.findAll();
		List<ChamadoDTO> listDTO = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	
	
	/*Anotação que verica as roles do spring security e concede as permissões de acordo 
	 * configuradas e pre declaradas, nesse caso, essas estão no enum Perfil*/
	@PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
	@GetMapping(value = "/desassociados/{id}")
	public ResponseEntity<ChamadoDsDTO> findByIdDesassociados(@PathVariable Integer id) {
		ChamadoDsDTO obj = service.findByIdDesassociados(id);
		return ResponseEntity.ok().body(new ChamadoDsDTO(obj));
	}
	
	@GetMapping(value = "/desassociados")
	public ResponseEntity<List<ChamadoDsDTO>> findDesassociados() {
		    List<ChamadoDsDTO> listaDTO =  service.findDessaciadosChamados().stream().map(obj -> new ChamadoDsDTO(obj)).collect(Collectors.toList());
		    return ResponseEntity.ok().body(listaDTO);
	}

	@PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
	@PostMapping
	public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO obj) {
		Chamado newObj = service.create(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> update(@PathVariable Integer id, @Valid @RequestBody ChamadoDTO objDTO) {
		Chamado newObj = service.update(id, objDTO);
		return ResponseEntity.ok().body(new ChamadoDTO(newObj));
	}
}