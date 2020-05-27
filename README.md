# Cooperative Assembly Voting API

Quick guide to build, test, run, and use this application.

## How to build the application

```shell script
$ mvn install
$ mvn package
```

## How to run the tests

```shell script
$ mvn test
```

## How to run the application

### Dependencies

#### PostgreSQL database

TODO

### Running application

Build the application (See [build](#how-to-build-the-application) section)

```shell script
$ export DB_URL=jdbc:postgresql://localhost:5432/cooperative_assembly
$ export DB_USERNAME=postgres
$ export DB_PASSWORD=postgres
$ java -jar ./target/cooperative-assemble-0.0.1.SNAPSHOT.jar
```

## How to use the application

The application will be listening by default on HTTP port 8080.

## Brief Endpoints documentation

### Topics API
#### Create Topic
```http request
POST http://localhost:8080/api/v1/topics/
{
    "topic": "A new topic to vote.",
    "description": "Some pretty description about this topic."
}
``` 

#### List Topics
```http request
GET http://localhost:8080/api/v1/topics/
``` 

#### Get Topic
```http request
GET http://localhost:8080/api/v1/topics/{id}
``` 

### Voting Sessions API
#### Open Voting Session
```http request
POST http://localhost:8080/api/v1/voting-sessions/
{
	"topicId": 1,
	"expires": "2020-11-30T12:30:00"
}
``` 

#### List Voting Sessions
```http request
GET http://localhost:8080/api/v1/voting-sessions/
``` 

#### Get Voting Session
```http request
GET http://localhost:8080/api/v1/voting-sessions/{id}
``` 

#### Voting Session Report
```http request
GET http://localhost:8080/api/v1/voting-sessions/{id}/report
``` 

### Votes API
#### Add New Vote
```http request
POST http://localhost:8080/api/v1/votes/
{
	"cpf": "123456789-10",
	"vote": "Sim",
	"votingSessionId": 1
}
``` 

#### List Votes
```http request
GET http://localhost:8080/api/v1/votes/
``` 

#### Get Vote
```http request
GET http://localhost:8080/api/v1/votes/{id}
``` 

## Swagger documentation
This API is build with Swagger documentation generator. The complete API documentation can be retrieved in:
```http request
http://localhost:8080/swagger-ui.html
``` 

## Examples
A Postman exported Collection with some examples of API call is available on the project source tree.

- [postman_collection.json](Postman/CooperativeAssembly.postman_collection.json)

# Desafio Técnico
## Objetivo
No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso, você precisa criar uma solução back-end para gerenciar essas sessões de votação. Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:
- Cadastrar uma nova pauta;
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default);
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
- Contabilizar os votos e dar o resultado da votação na pauta.

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A escolha da linguagem, frameworks e bibliotecas é livre (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

### Tarefas bônus
As tarefas bônus não são obrigatórias, mas nos permitem avaliar outros conhecimentos que você possa ter.

A gente sempre sugere que o candidato pondere e apresente até onde consegue fazer, considerando o seu
nível de conhecimento e a qualidade da entrega.
#### Tarefa Bônus 1 - Integração com sistemas externos
Integrar com um sistema que verifique, a partir do CPF do associado, se ele pode votar
- GET https://user-info.herokuapp.com/users/{cpf}
- Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos;
- Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação
Exemplos de retorno do serviço

#### Tarefa Bônus 2 - Mensageria e filas
Classificação da informação: Uso Interno
O resultado da votação precisa ser informado para o restante da plataforma, isso deve ser feito preferencialmente através de mensageria. Quando a sessão de votação fechar, poste uma mensagem com o resultado da votação.

#### Tarefa Bônus 3 - Performance
Imagine que sua aplicação possa ser usada em cenários que existam centenas de milhares de votos. Ela deve se comportar de maneira performática nesses cenários;
- Testes de performance são uma boa maneira de garantir e observar como sua aplicação se comporta.

#### Tarefa Bônus 4 - Versionamento da API
Como você versionaria a API da sua aplicação? Que estratégia usar?

### O que será analisado
- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits

### Observações importantes
- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e deixe claro caso haja instruções especiais para execução do mesmo
- Teste bem sua solução, evite bugs
