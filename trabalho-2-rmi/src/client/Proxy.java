package client;

import common.Message;
import common.RemoteObjectRef;
import common.interfaces.ServicoMedico;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.atomic.AtomicInteger;

public class Proxy {

    private static final AtomicInteger REQUEST_SEQUENCE = new AtomicInteger(1);

    private final RemoteObjectRef ref;
    private ServicoMedico stub;

    public Proxy(RemoteObjectRef ref) {
        this.ref = ref;
    }

    public void conectar() throws Exception {
        Registry registry = LocateRegistry.getRegistry(ref.getHost(), ref.getPort());
        stub = (ServicoMedico) registry.lookup(ref.getServiceName());
        System.out.println("[Proxy] Referencia remota obtida: " + ref.toLookupUrl());
    }

    public byte[] doOperation(RemoteObjectRef o, String methodId, byte[] arguments)
            throws Exception {

        if (stub == null) {
            throw new IllegalStateException("Proxy nao conectado. Chame conectar() primeiro.");
        }

        byte[] argumentosEmpacotados = arguments != null ? arguments : new byte[0];

        int requestId = REQUEST_SEQUENCE.getAndIncrement();
        Message requisicao = new Message(
                requestId,
                o.getServiceName(),
                methodId,
                argumentosEmpacotados
        );

        System.out.println("\n[doOperation] Enviando requisicao...");
        System.out.println("  messageType     : " + requisicao.getMessageTypeName()
                + " (" + requisicao.getMessageType() + ")");
        System.out.println("  requestId       : " + requisicao.getRequestId());
        System.out.println("  objectReference : " + requisicao.getObjectReference());
        System.out.println("  methodId        : " + requisicao.getMethodId());
        System.out.println("  arguments (bytes): " + argumentosEmpacotados.length + " bytes");

        Message resposta = stub.invocar(requisicao);

        System.out.println("[doOperation] Resposta recebida.");
        System.out.println("  messageType: " + resposta.getMessageTypeName()
                + " (" + resposta.getMessageType() + ")");

        if (!resposta.isReply()) {
            throw new IllegalStateException("Mensagem recebida nao e REPLY: " + resposta);
        }
        if (resposta.getRequestId() != requestId) {
            throw new IllegalStateException("requestId divergente. Esperado "
                    + requestId + ", recebido " + resposta.getRequestId());
        }
        System.out.println("  requestId  : " + resposta.getRequestId() + " [OK - mesmo ID]");

        return resposta.getArguments();
    }

    public byte[] doOperation(String methodId, byte[] arguments) throws Exception {
        return doOperation(ref, methodId, arguments);
    }
}
