package com.suelen.helpdesk.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suelen.helpdesk.domain.enums.Perfil;

@Entity
// a classe pessoa não poderá ser instânciada por que é abstrata, apenas pode ser herdade
public abstract class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY )
	protected Integer id;
	
	protected String nome;
	
	//Faz todo o cálculo para validar se é um cpf
	@CPF
	
	@Column (unique = true)
	protected String cpf;
	


    
	@Column (unique = true)
	protected String email;
	
	
	protected String senha;
	
	//assegura que a lista perfil virá imediamente quando chamada junta com toda a entidade
	@ElementCollection (fetch = FetchType.EAGER)
	//aqui eu digo que quero ter uma tabela apenas com os valores
	//dessa lista
	@CollectionTable(name = "PERFIS")
	// o new Hash faz uma verificação para evitar de nullpoint exception
	// e o Set não permite dois perfis de valores iguais
	protected Set<Integer> perfis = new HashSet<>();

	//pattern = padrão = mês precisa tá com M maiúsculo
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate dataCriacao =  LocalDate.now();
	

		
	public Pessoa() {
		super();
		addPerfil(Perfil.CLIENTE);


	}
	
	
	public Pessoa(Integer id, String nome, String cpf, String email, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);


	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmaill( String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Set<Perfil> getPerfis() {
		//Convertendo um Perfil(ENUM) em uma lista de perfis
		return perfis.stream().map( x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	//não recebe uma lista, e apenas um perfil.
	public void addPerfil(Perfil perfil) {
		this.perfis.add (perfil.getCodigo());
	}
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	
		
//	o hashCode equals faz comparação de um objeto pelo valor dos atributos, não do valor em memória
	
	}
	@Override
	public int hashCode() {
		return Objects.hash(cpf, id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(id, other.id);
	}
	
	
}