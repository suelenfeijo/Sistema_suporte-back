package com.suelen.helpdesk.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suelen.helpdesk.domain.Chamado;
import com.suelen.helpdesk.domain.dtos.ChamadoDsDTO;

public interface ChamadoDsRepository extends JpaRepository<ChamadoDsDTO, Integer> {
	
	
	


}
