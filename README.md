No desenvolvimento de um back-end de uma solução fullstack para um helpdesk, noções aprimoradas:

* Docker
* Conceitos de Spring Boot 2.x.x
* Spring Data JPA
* Hibernate
* Autenticação com Tokens JWT 
* Autorização com Tokens JWT
* Git
* Banco de Dados H2 em tempo de compilação
* Consumo de API via Postman
* Criar uma API RESTful
* Framework Lombok Criptografia e descriptografia de senhas com Bcrypt
* Deploy do serviço na nuvem (Render)
* Padrão DTO (Data Transfer Objects)
* MySQL em projeto Spring Boot
* Validações com Validations

Link da API | https://helpdesk-back.onrender.com
---|-----------

Pequena amostra da api:

```POST``` Acessando o login e recebendo um token jwt no header <br/>
 uri: | https://helpdesk-back.onrender.com/login
 --|--
 ```
 arquivo JSON
{
    "email":"suelen@mail.com",
    "senha":"123"
}
```



## Tecnico

> Create: <br/> criação de tecnico com validação de cpf e campos não nulos, também formatamos como é mostrado a data puxada do banco inserindo um pattern (dd/mm/yyyy).
> Update: <br/> .
> findAll:  <br/>.
> findById:  <br/>.
> delete: <br/>

## Cliente

> Create: <br/> 
> Update: <br/> .
> findAll:  <br/>.
> findById:  <br/>.
> delete: <br/>

## Chamado
> Create: <br/> criação de tecnico com validação de cpf e campos não nulos, também formatamos como é mostrado a data puxada do banco inserindo um pattern (dd/mm/yyyy).
> Update: <br/> .
> findAll:  <br/>.
> findById:  <br/>.


 ###### Abaixo imagem da api ativa rodando online


![render-api-ativa](https://i.postimg.cc/YCtQMk98/Captura-de-tela-2023-10-21-223206.png)
