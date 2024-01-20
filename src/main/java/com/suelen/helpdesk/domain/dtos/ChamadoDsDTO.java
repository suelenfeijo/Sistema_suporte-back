package com.suelen.helpdesk.domain.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.Cliente;
import com.suelen.helpdesk.domain.Tecnico;
import com.suelen.helpdesk.domain.enums.Prioridade;
import com.suelen.helpdesk.domain.enums.Status;

@Entity
public class ChamadoDsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void main (String[]args) {
		
		
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataAbertura = LocalDate.now();
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataFechamento;
	private Integer prioridade;
	private Integer status;
	private String titulo;
	private String observacoes;
	private Integer tecnico;
	

	private Integer cliente;
	private String nomeTecnico;
	private String nomeCliente;
	
	public ChamadoDsDTO() {
		super();
	}

	public ChamadoDsDTO(Chamado obj) {
		this.id = obj.getId();
		this.dataAbertura = obj.getDataAbertura();
		this.dataFechamento = obj.getDataFechamento();
		this.prioridade = obj.getPrioridade().getCodigo();
		this.status = obj.getStatus().getCodigo();
		this.titulo = obj.getTitulo();
		this.observacoes = obj.getObservacoes();
		this.tecnico = obj.getTecnico().getId();
		this.cliente = obj.getCliente().getId();

		this.nomeTecnico = obj.getTecnico().getNome();
		this.nomeCliente = obj.getCliente().getNome();
	}
	
	




	

	public ChamadoDsDTO(ChamadoDsDTO obj) {
		BeanUtils.copyProperties(obj, this);

	}

	public ChamadoDsDTO(List<Chamado> obj) {
	
		
		for (int i = 0; i < obj.size(); i++) {		
		this.id = obj.get(i).getId();
		this.dataAbertura = obj.get(i).getDataAbertura();
		this.dataFechamento = obj.get(i).getDataFechamento();
		this.prioridade = obj.get(i).getPrioridade().getCodigo();
		this.status = obj.get(i).getStatus().getCodigo();
		this.titulo = obj.get(i).getTitulo();
		this.observacoes = obj.get(i).getObservacoes();
		this.tecnico = obj.get(i).getTecnico().getId();
		this.cliente = obj.get(i).getCliente().getId();

		this.nomeTecnico = obj.get(i).getTecnico().getNome();
		this.nomeCliente = obj.get(i).getCliente().getNome();
		}

			 
	}
		
		
	

	public ChamadoDsDTO(Cliente cliente2, Tecnico tecnico2, String titulo2, LocalDate dataAbertura2,
			LocalDate dataFechamento2, String observacoes2, Status status2, Prioridade prioridade2) {
	}
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public LocalDate getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(LocalDate dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Integer getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Integer getTecnico() {
		return tecnico;
	}

	public void setTecnico(Integer tecnico) {
		this.tecnico = tecnico;
	}

	public Integer getCliente() {
		return cliente;
	}

	public void setCliente(Integer cliente) {
		this.cliente = cliente;
	}

	public String getNomeTecnico() {
		return nomeTecnico;
	}

	public void setNomeTecnico(String nomeTecnico) {
		this.nomeTecnico = nomeTecnico;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

}