package common.interfaces;

import common.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoMedico extends Remote {

    

    Message invocar(Message requisicao) throws RemoteException;
}
