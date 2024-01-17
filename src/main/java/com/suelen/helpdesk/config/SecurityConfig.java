package com.suelen.helpdesk.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.suelen.helpdesk.security.JWTAuthenticationFilter;
import com.suelen.helpdesk.security.JWTAuthorizationFilter;
import com.suelen.helpdesk.security.JWTUtil;

@EnableWebSecurity //essa anotação já possui @configuration
//essa anotação é quem permite ser possível utilizar a anotação preAutorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * tudo que vier após o /h2-console/ será liberada
	 */
	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" , "http://localhost:4200/**","https://helpdesk-back.onrender.com/**" };

	/*
	 * ele é uma interface que representa o ambiente do aplicativo, e consegue pegar todos os perfis ativos
	 */
	@Autowired
	private Environment env;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private UserDetailsService userDetailsService;

	
	/*
	 * metódo sobrescrito 
	 * qualquer endpoint que requeira uma defesa, ou configurações de filtro
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			/*
			 * desabilita o frameOptions 
			 */
			http.headers().frameOptions().disable();
		}

	
		http.cors().and().csrf().disable();
		/*
		 * adicionando um filter com a classe JWTAuthenticationFilter que foi criada, e ela recebe o authenticationManager() , estamos usando
		 * o contexto Spring Security, temos o WebSecurityConfigurerAdapter que tem o authenticationManager por padrão, faz todas as validações
		 * e retorna um authenticationManager  , e o jwtUtil
		 */
	    http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
	    
	    /*
	     * adicionando filtro de autorização que recebe um authenticationManager, um jwtUtil, e userDetailsService
	     * e o authenticationManager, é também do WebSecurityConfigurerAdapter
	     */
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		/*
		 * autorizando o h2
		 */
		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

		/*
		 * assegurando que sessão de usuário não será criada
		 */
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	
	/*
	 * fazendo a sobrecarga = overload  de configure 
	 * como a autenticação usando será a do framework Spring Security
	 * precisamos informar que a autenticação usada será a nossa 
	 * e o password enconder é o Bcrypt , que irá encriptografar as informações
	 * do userDetailsService, e temos que informar que é o nosso userDetailsService, 
	 * a minha classe criada.
	 * por está no mesmo pacote e classe do bcrypt não preciso criar uma injeção
	 * 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	/*
	 * cross origin = liberar requisições para o backend, applyPermitDefaultValues = 
	 * libera por padrão = configuration.setAllowedMethods , em um array libera = "POST", "GET", "PUT", "DELETE", "OPTIONS"
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		/*
		 * registrando configuração de cors para permitir receber requisições de tds os lugares
		 */
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	
	/*
	 * bean do bcrypt para poder injetar em qualquer outra parte do código
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
