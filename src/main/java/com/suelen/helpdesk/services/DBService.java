package com.suelen.helpdesk.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.Cliente;
import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.enums.Perfil;
import com.suelen.helpdesk.domain.enums.Prioridade;
import com.suelen.helpdesk.domain.enums.Status;
import com.suelen.helpdesk.repositories.ChamadoRepository;
import com.suelen.helpdesk.repositories.ClienteRepository;
import com.suelen.helpdesk.repositories.TecnicoRepository;

/*Mockagem de dados*/
@Service
public class DBService {
	@Autowired
	TecnicoRepository tecnicoRepository;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	ChamadoRepository chamadoRepository;
	
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public void instanciaDB() {
	Tecnico tec1 = new Tecnico(null, "Michael Jackson", "20858233517", "billie@mail.com", encoder.encode("123"));
	tec1.addPerfil(Perfil.TECNICO);
	
	Tecnico tec2 = new Tecnico(2, "feijo", "70362484406", "suelen@mail.com", encoder.encode("123"));
	tec2.addPerfil(Perfil.ADMIN);

	
	Cliente cli1 = new Cliente(null, "João Campos", "66354895252", "joao@mail.com", encoder.encode("123"));

	/*Chamado c1 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Conserto pc", "hd quebrou", tec1, cli1);*/

	Chamado c2 = new Chamado(null, Prioridade.ALTA, Status.ENCERRADO, "Conserto impressora", "cabeça de impressão ruim", tec1, cli1);

	tecnicoRepository.saveAll(Arrays.asList(tec1,tec2));
	clienteRepository.saveAll(Arrays.asList(cli1));
	chamadoRepository.saveAll(Arrays.asList(c2));
}
}