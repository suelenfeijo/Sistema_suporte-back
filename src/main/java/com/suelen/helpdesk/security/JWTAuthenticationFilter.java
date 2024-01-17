package com.suelen.helpdesk.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.suelen.helpdesk.domain.dtos.CredenciaisDTO;

/*
 * quando cria uma classe que extende UsernamePasswordAuthenticationFilter
 * o spring vai entender que esse filtro intercepta a requisição post
 * do endpoint /login , que é um endpoint reservado do spring security
 * ao acessar a classe UsernamePasswordAuthenticationFilter é possível visualizar o método /login
 * essa é uma autenticação bem completa.
 *  
 * 
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	/*
	 * AuthenticationManager é a principal interface de estratégia de autenticação 
	 * se o principal da autenticação de entrada for válido e verificado, o método
	 * autenticate que ele possui , retorna uma instância de authentication fazendo sinal
	 * que foi autenticado definido como verdadeiro. OBS: principal : usuário e senha, no caso, email e senha
	 * 
	 *
	 * não sendo válido, o sinal é retornado valor nulo, e não consegue decidir se tá ou não autenticado
	 *  
	 */
	
	private AuthenticationManager authenticationManager;
	
	/*
	 * JWTUtil vamos precisar por que tem o metodo generateToken , caso consiga autenticar, chama esse metodo 
	 * generateToken
	 */
	private JWTUtil jwtUtil;

	
	/*
	 * construtor
	 */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	
	/*
	 * metodo que tenta autenticar o usuário
	 * vamos pegar as informações passadas na requisição post do endpoint /login
	 * e vamos converter em credenciaisDTO 
	 * 
	 * instância um UsernamePasswordAuthenticationToken , e passa esse objeto como 
	 * parametro pro método authenticate que contém dentro de authenticationManager,
	 * esse método tenta autenticar o usuário.
	 * 
	 *No caso, esse método vai utilizar as classes que implementamos: UserDetailsService, UserSS
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		/*
		 * try para caso de dar certo, ou catch para caso de não sucesso
		 */
		try {
			
			/*
			 * ObjectMapper.readValue passa o request, que é a requisição , o pedido , vinda do front,
			 * com isso -> request.getInputStream() , conseguimos pegar os valores do corpo da requisição 
			 * com esse carinha.
			 * no segundo parametro do readValue, é: você quer converter os valores da requisição em qual classe?
			 * aí uso o CredenciaisDTO e .class , dizendo que é uma classe
			 * 
			 *  authenticationToken recebe -> new UsernamePasswordAuthenticationToken , passando o primeiro e segundo valor sendo o email e senha do creds(credenciaisDTO),
			 *  , além disso o UsernamePasswordAuthenticationToken recebe também uma lista de authorities m mas não estamos trabalhando com authorities, e sim os perfis do usuário, então pode ser um array vazio.
			 * 
			 * criamos agora um Authentication authentication, que recebe o authenticationManager.authenticate , que tenta efetuar o autenticate com os dados que foi passados em
			 * UsernamePasswordAuthenticationToken authenticationToken
			 *  
			 *  
			 */
			
			
			CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return authentication;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * método de sucesso de autenticação, caso autenticação ocorra com sucesso
	 *
	 */
	@Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
	
		/*
		 * String username , fazendo um passe do UserSS , chama o auth , que é o resultado da Authentication, e pega o getUsername que é o email
		 * String token = gera o token a partir do email do Username
		 * e agora passa as respostas do header para devolver no front
		 * e passa também o Authorization + bearer	+ token gerado
		 */
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		response.addHeader("Access-control-expose-headers ", "Authorization");

        	response.addHeader("Authorization", "Bearer " + token);
	}
	
	/*
	 * método de não sucesso de autenticação, caso autenticação ocorra sem sucesso
	 * seta status 401 = não autorizado
	 * response.setContentType("application/json") o tipo do conteúdo é json;
	 * 
	 * 
	 * 		response.getWriter().append(json()) -> chamando o método json() no padrão do timestamp,message, status, error, trace, path
	 */
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		response.setStatus(401);
		response.setContentType("application/json");
		response.getWriter().append(json());
	}

	private CharSequence json() {
		long date = new Date().getTime();
		return "{"
				+ "\"timestamp\": " + date + ", " 
				+ "\"status\": 401, "
				+ "\"error\": \"Não autorizado\", "
				+ "\"message\": \"Email ou senha inválidos\", "
				+ "\"path\": \"/login\"}";
	}
	
}