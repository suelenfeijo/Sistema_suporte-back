package com.suelen.helpdesk.resources;

import java.net.URI;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.dtos.TecnicoDTO;
import com.suelen.helpdesk.services.TecnicoService;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {
	
	//Response entity representa toda a resposta http, podemos controlar qualquer coisa que acontece aqui
	//corpo, cabeçalho, e etc, e também trabalha melhor a segurança da informação, além de ser boa prática
	
	@Autowired
	private TecnicoService service;

	
	@PreAuthorize("hasAnyRole('ADMIN')")
	/*
	 * requestBody = no corpo da req deve vir um tecDTO
	 * uri = quando cria um obj, ele recebe um id. a url retorna a url de acesso
	 * , no path, o endereço de acesso, e aí pega o metodo que trás um id, e passa para uri.
		a anotação valid = valida se todos os campos estão notnull
		
	 */
	@PostMapping
	public ResponseEntity<TecnicoDTO> create(@Valid @RequestBody TecnicoDTO objDTO) {
		Tecnico newObj = service.create(objDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
		Tecnico obj = service.findById(id);
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}

	
	
	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll() {
		List<Tecnico> list = service.findAll();
		List<TecnicoDTO> listDTO = list.stream().map(obj -> new TecnicoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO objDTO) {
		Tecnico obj = service.update(id, objDTO);
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}
	
	/*
	 * um tipo delete é um noContent, não retorna nada, poderia ser um void também o retorno
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> delete(@PathVariable Integer id) {
		service.delete(id); 
		
		return ResponseEntity.noContent().build();
	}
}
