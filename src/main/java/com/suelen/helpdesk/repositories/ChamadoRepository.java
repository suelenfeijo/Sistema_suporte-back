package com.suelen.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suelen.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
	

}
