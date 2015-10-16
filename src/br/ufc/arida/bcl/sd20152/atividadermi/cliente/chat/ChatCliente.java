package br.ufc.arida.bcl.sd20152.atividadermi.cliente.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;

@SuppressWarnings("serial")
public class ChatCliente extends UnicastRemoteObject implements InterfaceDeCliente {

    private String nickname;

    private InterfaceDeServidor servidor;

    private List<Mensagem> caixaDeEntrada;

    public ChatCliente(String nickname, InterfaceDeServidor servidor) throws RemoteException {
        this.nickname = nickname;
        this.servidor = servidor;
        caixaDeEntrada = new ArrayList<Mensagem>();
    }

    @Override
    public synchronized void receberMensagem(Mensagem mensagem) throws RemoteException {
        caixaDeEntrada.add(mensagem);
    }

    public void enviarMensagem(Mensagem mensagem) throws RemoteException {
        servidor.enviarMensagem(mensagem);
    }

    public void entrarNoChat(String nickname) throws RemoteException {
        servidor.adicionarCliente(this, nickname);
        System.out.println("#conectado ao chat");
    }

    public void sairDoChat() throws RemoteException {
        servidor.removerCliente(this);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public synchronized List<Mensagem> getCaixaDeEntrada() {
        List<Mensagem> listaDeMensagens = new ArrayList<Mensagem>(caixaDeEntrada);
        return listaDeMensagens;
    }

}
