# Sistema de Especialidades Médicas

Este repositório contém o trabalho desenvolvido para a disciplina de Sistemas Distribuídos, utilizando o contexto de especialidades médicas como base para a implementação.

O projeto está organizado em um módulo principal:

- `src`: responsável pelo envio e recebimento de dados de médicos e especialidades por meio de streams personalizados, comunicação TCP e notificações via multicast UDP.

## Objetivo

Demonstrar, de forma estruturada, conceitos fundamentais de comunicação distribuída em Java, incluindo:

- modelagem de dados com classes POJO
- separação entre camadas de modelo, serviço, streams e rede
- serialização manual — empacotamento e desempacotamento de mensagens
- leitura e escrita de dados em arquivo
- comunicação cliente-servidor com sockets TCP
- notificações em broadcast via multicast UDP
- concorrência com threads e acesso seguro a dados compartilhados

## Estrutura do projeto

```
src/
├── model/         → classes POJO (Medico, Especialidade, Resposta)
├── service/       → lógica de negócio e configurações
├── stream/        → serialização manual via OutputStream/InputStream
└── network/       → comunicação TCP e multicast UDP
```

## Principais classes

### model
- `Medico` — POJO com id, nome e especialidade
- `Especialidade` — POJO com id e nome
- `Resposta` — representa o retorno do servidor ao cliente

### service
- `MedicoService` — carrega médicos do arquivo `medicos.txt` e processa envios
- `EspecialidadeService` — gerencia a lista de especialidades disponíveis
- `ConfiguracaoSistema` — centraliza constantes de rede (host, portas, grupo multicast)

### stream
- `MedicoOutputStream` — serializa array de médicos para bytes
- `MedicoInputStream` — desserializa bytes para array de médicos
- `EspecialidadeOutputStream` — serializa array de especialidades para bytes
- `EspecialidadeInputStream` — desserializa bytes para array de especialidades
- `RespostaOutputStream` — serializa a resposta do servidor
- `RespostaInputStream` — desserializa a resposta recebida pelo cliente

### network
- `Server` — servidor TCP multi-threaded com menu interativo
- `Client` — cliente TCP com menu interativo
- `MulticastNotificador` — envia notificações UDP para o grupo multicast
- `ClienteMulticast` — recebe notificações UDP do grupo multicast

## Arquivo de dados

Antes de executar, crie o arquivo `medicos.txt` no diretório `med-system` com o seguinte formato:

```
1,Joao,Cardiologia
2,Maria,Pediatria
3,Carlos,Ortopedia
```

## Compilação

No diretório `med-system`, execute:

```bash
javac src/model/*.java src/service/*.java src/stream/*.java src/network/*.java
```

## Execução

### Testes de Stream com arquivo

```bash
# Gera o arquivo binário medicos.bin
java -cp src stream.MedicoOutputStreamFileTest

# Lê e imprime o conteúdo do arquivo binário
java -cp src stream.MedicoInputStreamFileTest

# Lê os dados a partir da entrada padrão
java -cp src stream.MedicoInputStreamConsoleTest < medicos.bin
```

### Comunicação TCP — cliente e servidor

Abra dois terminais. Inicie sempre o servidor antes do cliente:

```bash
# Terminal 1 — servidor
java -cp src network.Server

# Terminal 2 — cliente
java -cp src network.Client
```

O cliente envia os médicos ao servidor via TCP. O servidor desserializa, processa e devolve uma resposta de confirmação. Após receber, o servidor dispara uma notificação multicast.

### Notificações multicast UDP

```bash
# Terminal adicional — pode abrir quantos quiser
java -cp src network.ClienteMulticast
```

A cada envio do cliente, todos os `ClienteMulticast` ativos recebem automaticamente a notificação. O servidor também pode enviar mensagens manuais pelo menu (opção 2).

## Comunicação

| Protocolo | Uso | Classes envolvidas |
|---|---|---|
| TCP unicast | Envio de médicos e resposta de confirmação | `Client`, `Server` |
| UDP multicast | Notificações automáticas e manuais | `MulticastNotificador`, `ClienteMulticast` |

- Grupo multicast: `230.0.0.1`
- Porta multicast: `9999`
- Porta TCP: `12345`

## Decisões de projeto

**Serialização manual** em vez de `Serializable`: garante controle total sobre o protocolo binário e independência de linguagem — qualquer sistema que conheça o protocolo consegue ler os dados, não apenas Java.

**Thread por cliente** no servidor: cada conexão TCP é tratada em uma thread independente, permitindo múltiplos clientes simultâneos sem bloqueio.

**`Collections.synchronizedList`**: protege a lista de médicos recebidos contra acesso concorrente de múltiplas threads.

**`ConfiguracaoSistema`**: centraliza todas as constantes de rede em um único arquivo — mudar porta ou grupo multicast requer alteração em apenas um lugar.
