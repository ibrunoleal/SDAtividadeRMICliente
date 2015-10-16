package br.ufc.arida.bcl.sd20152.atividadermi.cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;

@SuppressWarnings("serial")
public class ChatCliente extends UnicastRemoteObject implements InterfaceDeCliente{
	
	private String nickname;
	
	private InterfaceDeServidor servidor;
	
	private List<Mensagem> caixaDeEntrada;
	
	private List<Mensagem> caixaDeSaida;

	protected ChatCliente(String nickname, InterfaceDeServidor servidor) throws RemoteException {
		this.nickname = nickname;
		this.servidor = servidor;
		caixaDeEntrada = new ArrayList<Mensagem>();
		caixaDeSaida = new ArrayList<Mensagem>();
	}

	@Override
	public synchronized void receberMensagem(Mensagem mensagem) throws RemoteException {
		System.out.println(mensagem);
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

	public List<Mensagem> getCaixaDeEntrada() {
		return caixaDeEntrada;
	}

	public List<Mensagem> getCaixaDeSaida() {
		return caixaDeSaida;
	}
	
	

}
