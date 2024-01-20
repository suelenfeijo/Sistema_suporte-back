package com.suelen.helpdesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.suelen.helpdesk.services.DBService;

@Configuration
@Profile ("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	 
	/*Obtendo valor de spring.jpa.hibernate.ddl-auto do @Profile dev*/
	@Value ("${spring.jpa.hibernate.ddl-auto}")
	private String value;
	
	@Bean
	public boolean instaciaDB() {
		if(value.equals("create") || value.equals("update")) {
			this.dbService.instanciaDB();
		}
		return false;

		
	}
		
	}