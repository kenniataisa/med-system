package server;

import common.Marshaller;
import common.Message;
import common.interfaces.ServicoMedico;
import server.services.MedicoServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RequestHandler extends UnicastRemoteObject implements ServicoMedico {

    private static final long serialVersionUID = 1L;

    private final MedicoServiceImpl service;

    public RequestHandler() throws RemoteException {
        super();
        this.service = new MedicoServiceImpl();
    }

    

    @Override
    public Message invocar(Message requisicao) throws RemoteException {
        Message req = getRequest(requisicao);
        byte[] resultado = service.processar(req);
        return sendReply(resultado, req.getRequestId(), req.getObjectReference(), req.getMethodId());
    }

    

    private Message getRequest(Message requisicao) throws RemoteException {
        if (requisicao == null) {
            throw new RemoteException("Requisicao nula recebida pelo RequestHandler.");
        }
        if (!requisicao.isRequest()) {
            throw new RemoteException("Mensagem recebida nao e REQUEST: " + requisicao);
        }
        System.out.println("\n[getRequest] Requisicao recebida:");
        System.out.println("  messageType     : " + requisicao.getMessageTypeName()
                + " (" + requisicao.getMessageType() + ")");
        System.out.println("  requestId       : " + requisicao.getRequestId());
        System.out.println("  objectReference : " + requisicao.getObjectReference());
        System.out.println("  methodId        : " + requisicao.getMethodId());
        System.out.println("  arguments       : " + requisicao.getArguments().length + " bytes");
        return requisicao;
    }

    

    private Message sendReply(byte[] resultado, int requestId,
                              String objectReference, String methodId) {
        Message resposta = new Message(resultado, requestId, objectReference, methodId);
        System.out.println("[sendReply] Resposta enviada:");
        System.out.println("  messageType     : " + resposta.getMessageTypeName()
                + " (" + resposta.getMessageType() + ")");
        System.out.println("  requestId       : " + resposta.getRequestId());
        System.out.println("  objectReference : " + resposta.getObjectReference());
        System.out.println("  methodId        : " + resposta.getMethodId());
        System.out.println("  arguments       : " + resposta.getArguments().length + " bytes");
        return resposta;
    }
}
