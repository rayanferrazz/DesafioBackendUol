# UOL Backend Challenge — Cadastro de Jogadores

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-6DB33F?logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-template-005F0F?logo=thymeleaf&logoColor=white)
![H2](https://img.shields.io/badge/H2-em%20mem%C3%B3ria-blue)

Implementação do **desafio backend da UOL**: uma API REST para cadastro de jogadores que recebem automaticamente um **codinome** exclusivo, sorteado a partir das listas da **Liga da Justiça** (XML) e dos **Vingadores** (JSON).

O projeto foi desenvolvido para fins de estudo, seguindo as instruções do desafio original da UOL.

> ⚠️ Projeto educacional, sem fins lucrativos ou promocionais.

---

## Sumário

- [Sobre o desafio](#sobre-o-desafio)
- [Funcionalidades](#funcionalidades)
- [Como funciona a distribuição de codinomes](#como-funciona-a-distribuição-de-codinomes)
- [Tecnologias](#tecnologias)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como executar](#como-executar)
- [Endpoints](#endpoints)
- [Modelo de dados](#modelo-de-dados)
- [Configuração](#configuração)
- [Testes](#testes)
- [Limitações e próximos passos](#limitações-e-próximos-passos)
- [Referências](#referências)

---

## Sobre o desafio

Cada jogador escolhe um grupo — **Liga da Justiça** ou **Vingadores** — e o sistema atribui a ele um codinome ainda não utilizado daquele grupo. O codinome é removido da lista interna, garantindo que não seja distribuído duas vezes.

Além da API, o projeto inclui uma **página web simples** (Thymeleaf) para cadastrar e listar jogadores.

## Funcionalidades

- Cadastro de jogadores com nome, e-mail, telefone e grupo;
- Validação de nome e e-mail (`@NotBlank`, `@Email`);
- Sorteio e atribuição automática de codinome único;
- Listagem de todos os jogadores cadastrados;
- Página inicial com formulário de cadastro e listagem via `fetch`;
- Tratamento do esgotamento de codinomes com resposta `400`;
- Persistência em banco H2 em memória;
- Console do H2 habilitado.

## Como funciona a distribuição de codinomes

Na inicialização, o `CodinameService` baixa e processa duas fontes externas configuradas em `application.properties`:

| Grupo | Formato | Origem |
| --- | --- | --- |
| `JUSTICE_LEAGUE` | XML | `justice.league` |
| `AVENGERS` | JSON | `avangers` |

Os codinomes são armazenados em listas em memória. A cada cadastro, o `CodinameHandler`:

1. Seleciona o **primeiro** codinome da lista do grupo escolhido;
2. Remove esse codinome da lista;
3. Persiste o jogador com o codinome atribuído.

Quando a lista do grupo se esgota, a aplicação lança `NoSuchElementException`, tratada pelo `ControllerExceptionHandler` com HTTP **400** e a mensagem *"Essa lista não possui mais usuários disponíveis!"*.

> Como as listas ficam em memória, reiniciar a aplicação “devolve” todos os codinomes.

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 17 |
| Framework | Spring Boot 3.2.4 (Web, Data JPA, Validation, Thymeleaf) |
| Banco de dados | H2 (em memória) |
| Persistência | Spring Data JPA / Hibernate |
| Consumo HTTP | `RestTemplate` |
| Parsing | Jackson (JSON) e DOM (XML) |
| Build | Maven Wrapper (`mvnw`) |
| Utilitários | Lombok, Spring Boot DevTools |

## Estrutura do projeto

```text
src/main/java/desafio/backend/uol
├── UolApplication.java
├── config/
│   ├── AppConfig.java              # Bean do RestTemplate
│   └── CodinameHandler.java        # Escolhe e remove o codinome
├── controller/
│   ├── HomeController.java         # GET / (página Thymeleaf)
│   └── PlayerController.java       # GET/POST /players
├── domain/
│   ├── GroupType.java              # JUSTICE_LEAGUE | AVENGERS
│   └── Player.java
├── dto/
│   ├── ExceptionDTO.java
│   └── PlayerDTO.java
├── exception/
│   └── ControllerExceptionHandler.java
├── repository/
│   └── PlayerRepository.java
└── service/
    ├── CodinameService.java        # Carrega XML e JSON
    └── PlayerService.java
```

## Como executar

### Pré-requisitos

- **Java 17** ou superior;
- Conexão com a internet na primeira subida (download das listas de codinomes).

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Acesse:

- Página de cadastro: `http://localhost:8080/`
- Console H2: `http://localhost:8080/h2-console`

| Campo H2 | Valor |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:testdb` |
| Usuário | `user` |
| Senha | `password` |

## Endpoints

Base: `http://localhost:8080`

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/` | Página web de cadastro/listagem |
| `GET` | `/players` | Lista todos os jogadores |
| `POST` | `/players` | Cadastra um jogador e gera o codinome |

### Cadastrar jogador

```http
POST /players
Content-Type: application/json
```

```json
{
  "name": "Rayan Ferraz",
  "email": "rayan@email.com",
  "phoneNumber": 21999999999,
  "groupType": "JUSTICE_LEAGUE"
}
```

Resposta: `201 Created`

```json
{
  "id": 1,
  "name": "Rayan Ferraz",
  "email": "rayan@email.com",
  "phoneNumber": 21999999999,
  "codiname": "Batman",
  "groupType": "JUSTICE_LEAGUE"
}
```

Para os Vingadores, use `"groupType": "AVENGERS"`.

### Listar jogadores

```http
GET /players
```

Resposta: `200 OK` com a lista de jogadores e seus codinomes.

### Erros de validação

Nome vazio ou e-mail inválido resultam em `400 Bad Request` com os detalhes do Bean Validation.

### Lista esgotada

```json
{
  "message": "Essa lista não possui mais usuários disponíveis!",
  "status": "400"
}
```

## Modelo de dados

**Player (`players`)**

| Campo | Tipo | Observação |
| --- | --- | --- |
| `id` | `Long` | Chave primária |
| `name` | `String` | Obrigatório |
| `email` | `String` | Obrigatório e válido |
| `phoneNumber` | `Integer` | Telefone |
| `codiname` | `String` | Gerado automaticamente |
| `groupType` | `GroupType` | `JUSTICE_LEAGUE` ou `AVENGERS` |

## Configuração

Arquivo: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=user
spring.datasource.password=password
spring.h2.console.enabled=true

justice.league=https://raw.githubusercontent.com/uolhost/test-backEnd-Java/master/referencias/liga_da_justica.xml
avangers=https://raw.githubusercontent.com/uolhost/test-backEnd-Java/master/referencias/vingadores.json
```

As URLs podem ser sobrescritas por variáveis de ambiente ou argumentos do Spring, por exemplo:

```bash
java -jar app.jar --justice.league=https://minha-url/liga.xml
```

## Testes

```bash
# Windows
mvnw.cmd test

# Linux/macOS
./mvnw test
```

O projeto contém apenas o teste de contexto gerado pelo Spring Initializr.

## Limitações e próximos passos

- **Codinomes apenas em memória** — reiniciar a aplicação reinicia as listas; não há controle persistente de codinomes já usados.
- **Página web com valor divergente** — o formulário envia `AVANGERS`, mas o enum aceita `AVENGERS`. Ao cadastrar pelo navegador no grupo Vingadores, a requisição falha. Use `AVENGERS` via API ou corrija o `value` no `home.html`.
- **Telefone como `Integer`** — perde zeros à esquerda e não comporta formatos internacionais; o ideal seria `String`.
- **E-mail não é único** — o mesmo e-mail pode ser cadastrado várias vezes.
- **Dependência de rede** — se o download das listas falhar, as listas ficam vazias e os cadastros retornam erro.
- **Sem autenticação** — a API é aberta.
- **Sem paginação** na listagem.
- **Sem testes de regra de negócio** — apenas o teste de contexto.

## Referências

- Desafio original: [uolhost/test-backEnd-Java](https://github.com/uolhost/test-backEnd-Java)
- Documentação Spring Boot: [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

## Autor

Desenvolvido por **Rayan Ferraz** para fins educacionais.

- GitHub: [@rayanferrazz](https://github.com/rayanferrazz)
