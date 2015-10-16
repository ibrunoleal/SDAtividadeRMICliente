package br.ufc.arida.bcl.sd20152.atividadermi.cliente;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;

public class ClienteTeste {

	public static void main(String[] args) {
		
		Registry registroCliente = null;
		InterfaceDeServidor servidor = null;
		
		try {
			System.out.println("#registrando...");
			registroCliente = LocateRegistry.getRegistry(1099);
			System.out.println("#registrado :)");
		} catch (RemoteException e) {
			System.out.println("#erro: servidor nao encontrado para registro");
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			System.out.println("#criando interface de comunicacao remota");
			servidor = (InterfaceDeServidor) registroCliente.lookup(InterfaceDeServidor.ID_DO_CHAT_RMI);
			System.out.println("#interface de comunicacao remota criada :)");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("#Digite o nick que deseja usar:");
		String nick = scanner.nextLine();
		
		ChatCliente chatcliente;
		try {
			chatcliente = new ChatCliente(nick, servidor);
			chatcliente.entrarNoChat(nick);
			String msg;
			while(true) {
				msg = scanner.nextLine();
				if (msg.equalsIgnoreCase("sair")) {
					chatcliente.sairDoChat();
					scanner.close();
					break;
				}
				Mensagem m = new Mensagem(chatcliente.getNickname(), msg);
				chatcliente.enviarMensagem(m);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	
	}

}
