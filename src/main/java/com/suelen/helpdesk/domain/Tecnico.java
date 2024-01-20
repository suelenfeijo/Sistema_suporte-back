package com.suelen.helpdesk.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PostRemove;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suelen.helpdesk.domain.dtos.TecnicoDTO;
import com.suelen.helpdesk.domain.enums.Perfil;

@Entity
public class Tecnico extends Pessoa {
	private static final long serialVersionUID = 1L;
	
	

	/*anotação cascade = CascadeType.ALL determina quais as ações*/

	@JsonIgnore
	/*curiosamente, a anotação orphanRemoval = true e cascade = CascadeType.ALL funcionaram
	 * no sentido contrário da descrição que encontrei que é remover os dados filhos do banco, 
	 * e quando ativadas, os chamados permaneceram, caso para estudar. Mais informações no arquivo relembrar.txt*/
	
	
	@OneToMany(mappedBy = "tecnico" ,cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Chamado> chamados = new ArrayList<>();

	public Tecnico() {
		super();
		addPerfil(Perfil.TECNICO);

	}

	public Tecnico(Integer id, String nome, String cpf, String email, String senha) {
		super(id, nome, cpf, email, senha );
		addPerfil(Perfil.TECNICO);


	}
	
	public Tecnico(TecnicoDTO obj) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.cpf = obj.getCpf();
		this.email = obj.getEmail();
		this.senha = obj.getSenha();
		this.perfis = obj.getPerfis().stream().map(x -> x.getCodigo()).collect(Collectors.toSet());
		this.dataCriacao = obj.getDataCriacao();
		addPerfil(Perfil.CLIENTE);

	}

	public List<Chamado> getChamados() {
		return chamados;
	}

	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}
	
	
	/*Removendo associação de chamados, a anotação @PostRemove
	 * @PostRemove: Indica que um método específico deve ser
	 *  executado após a remoção de uma entidade.
	 * */
	@PostRemove
	public void deleteChamados() {
		
		for (int i = 0; i < chamados.size(); i++) {
		this.getChamados().remove(i).setTecnico(null);
		}
	
	}

	
}
