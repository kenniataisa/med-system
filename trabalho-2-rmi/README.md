# Sistema de Especialidades Medicas com RMI

Este projeto contem a refatoracao do sistema de especialidades medicas para a disciplina de Sistemas Distribuidos, utilizando **Java RMI** como mecanismo de comunicacao cliente-servidor.

A aplicacao mantem o contexto de medicos, especialidades e consultas, mas substitui a comunicacao direta por sockets por uma chamada remota organizada em mensagens de requisicao e resposta.

## Objetivo

Demonstrar, de forma estruturada, os seguintes conceitos:

- comunicacao cliente-servidor com Java RMI
- uso de stub remoto como referencia para o objeto servidor
- envio de requisicoes por meio de uma mensagem padronizada
- empacotamento e desempacotamento de argumentos em `byte[]`
- representacao externa de dados usando XML
- separacao entre cliente, proxy, servidor, manipulador de requisicoes, servico e modelos
- modelagem de entidades com relacoes "e-um" e "tem-um"

## Estrutura do projeto

```text
src/
├── client/        -> cliente em modo texto e proxy de chamada remota
├── common/        -> mensagem, referencia remota, marshaller e interface RMI
├── model/         -> classes de dominio medico
└── server/        -> servidor RMI, request handler e servico local
```

## Principais classes

### client

- `Client` — menu em modo texto para demonstrar as operacoes remotas.
- `Proxy` — implementa `doOperation`, monta a mensagem de requisicao, chama o stub RMI e retorna os bytes da resposta.

### common

- `Message` — representa a mensagem trafegada entre cliente e servidor, com `messageType`, `requestId`, `objectReference`, `methodId` e `arguments`.
- `RemoteObjectRef` — guarda nome, host e porta do objeto remoto usado pelo cliente.
- `Marshaller` — converte objetos em XML e depois em `byte[]`, e faz o caminho inverso no recebimento.
- `ServicoMedico` — interface remota RMI com o metodo `invocar(Message requisicao)`.

### model

- `Funcionario` — classe base abstrata.
- `Medico` — funcionario medico, associado a uma especialidade.
- `Recepcionista` — segunda especializacao de funcionario, usada para cumprir a relacao estrutural "e-um".
- `Especialidade` — representa a especialidade medica.
- `Paciente` — representa o paciente atendido.
- `Consulta` — agrega medico e paciente em um agendamento.

### server

- `Server` — inicia o `rmiregistry`, cria o objeto remoto e registra o servico.
- `RequestHandler` — recebe a mensagem, chama o servico e monta a resposta.
- `MedicoServiceImpl` — contem o dispatcher e a logica das operacoes medicas.

## Operacoes disponiveis

| Operacao | Argumento | Retorno |
|---|---|---|
| `cadastrarMedico` | `Medico` | `Medico` com ID |
| `buscarMedico` | `Integer` | `Medico` ou vazio |
| `listarMedicos` | nenhum | lista de medicos |
| `agendarConsulta` | `Consulta` | `Consulta` agendada |

## Comunicacao

| Elemento | Uso | Classe |
|---|---|---|
| Java RMI | Transporte remoto entre cliente e servidor | `Server`, `Client`, `ServicoMedico` |
| Proxy local | Ponto de chamada usado pelo cliente | `Proxy` |
| Mensagem | Padroniza requisicao e resposta | `Message` |
| XML | Representacao externa dos argumentos e resultados | `Marshaller` |
| Dispatcher | Escolhe a operacao pelo `methodId` | `MedicoServiceImpl` |

O projeto nao cria `Socket` diretamente. O transporte fica sob responsabilidade do Java RMI.

## Formato da mensagem

Cada requisicao ou resposta usa a classe `Message`:

| Campo | Tipo | Descricao |
|---|---|---|
| `messageType` | `int` | `0` para requisicao e `1` para resposta |
| `requestId` | `int` | identificador sequencial da chamada |
| `objectReference` | `String` | nome do servico remoto |
| `methodId` | `String` | nome da operacao desejada |
| `arguments` | `byte[]` | argumentos ou resultado empacotados |

O `requestId` enviado pelo cliente e copiado na resposta. O cliente valida esse valor antes de aceitar o retorno.

## Entidades e relacionamentos

### Extensoes "e-um"

- `Medico extends Funcionario`
- `Recepcionista extends Funcionario`

### Agregacoes "tem-um"

- `Medico` tem uma `Especialidade`
- `Consulta` tem um `Medico`
- `Consulta` tem um `Paciente`

## Compilacao

No diretorio `projeto-rmi-conforme`, execute:

```powershell
$files = Get-ChildItem -Recurse -File src -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $files
```

Tambem ha um `pom.xml` para ambientes com Maven:

```bash
mvn clean package
```

## Execucao

Abra dois terminais. Inicie sempre o servidor antes do cliente.

```powershell
# Terminal 1 - servidor
java -cp out server.Server
```

```powershell
# Terminal 2 - cliente
java -cp out client.Client
```

O cliente exibe um menu com as quatro operacoes remotas. Durante a execucao, os logs mostram o envio da requisicao, o recebimento no servidor, o roteamento da operacao e o retorno da resposta.

## Exemplo de fluxo

```text
[doOperation] Enviando requisicao...
  messageType     : REQUEST (0)
  requestId       : 1
  objectReference : ServicoMedico
  methodId        : listarMedicos

[getRequest] Requisicao recebida:
  messageType     : REQUEST (0)
  requestId       : 1

[Dispatcher] Roteando para: listarMedicos

[sendReply] Resposta enviada:
  messageType     : REPLY (1)
  requestId       : 1

[doOperation] Resposta recebida.
  requestId       : 1 [OK - mesmo ID]
```

## Decisoes de projeto

**Java RMI em vez de sockets diretos**: atende ao requisito do trabalho e deixa o runtime RMI responsavel pelo transporte remoto.

**Metodo remoto unico `invocar`**: centraliza a comunicacao em uma mensagem generica. As operacoes do sistema sao escolhidas pelo campo `methodId`.

**XML como representacao externa**: o enunciado aceita XML, JSON ou Protocol Buffers. XML foi escolhido por permitir uma implementacao sem dependencias externas, usando recursos nativos do Java.

**Modelos sem transporte direto por RMI**: entidades como `Medico`, `Paciente` e `Consulta` sao convertidas em `byte[]` pelo `Marshaller`; quem trafega diretamente pelo RMI e a `Message`.

**Dados em memoria**: o servico usa colecoes em memoria para facilitar a demonstracao das operacoes sem exigir banco de dados.
