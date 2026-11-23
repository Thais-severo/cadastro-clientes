# Cadastro de Clientes

API REST de cadastro de clientes em Java 21, Spring Boot, Maven e H2.

1. Abra a pasta do projeto no IntelliJ IDEA e importe o Maven pelo pom.xml.
2. Selecione o JDK 21 e execute CadastroClientesApplication.
3. No Postman, use o endereço base `http://localhost:8080`.

O perfil test é ativado por padrão. O Spring carrega os dez registros de data.sql ao iniciar.
O controller recebe ClientRepository no construtor. O Spring fornece esse repositório, que busca o registro no H2.

| Método | Caminho | Resposta esperada |
| --- | --- | --- |
| GET | `/clients?page=0&linesPerPage=6&direction=ASC&orderBy=name` | 200 com a primeira página |
| GET | `/clients/1` | 200 com o cliente |
| POST | `/clients` | 201 com o cliente cadastrado |
| PUT | `/clients/1` | 200 com o cliente atualizado |
| DELETE | `/clients/1` | 204 |

Use o corpo JSON abaixo nas requisições POST e PUT.

```json
{
  "name": "Maria Silva",
  "cpf": "12345678901",
  "income": 6500.0,
  "birthDate": "1994-07-20T10:30:00Z",
  "children": 2
}
```

Abra `Client.asta` no Astah para ver o diagrama de classes.
