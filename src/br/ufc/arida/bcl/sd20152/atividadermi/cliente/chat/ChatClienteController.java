/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.arida.bcl.sd20152.atividadermi.cliente.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author brunoleal
 */
public class ChatClienteController  extends UnicastRemoteObject implements InterfaceDeCliente {
    
    private Registry registroCliente = null;
    
    ChatCliente chatCliente;
    
    private InterfaceDeServidor servidor;
    
    public ChatClienteController() throws RemoteException{
        chatCliente = new ChatCliente();
    }
    
    public void entrarNoChat() {
        String log;
        
        try {
            log = "registrando...";
            registroCliente = LocateRegistry.getRegistry(1099);
            chatCliente.adicionarRegistroDeLog(log);
        } catch (RemoteException e) {
            log = "erro: servidor nao encontrado para registro";
            chatCliente.adicionarRegistroDeLog(log);
            e.printStackTrace();
            //System.exit(1);
        }

        try {
            log = "criando interface de comunicacao remota";
            chatCliente.adicionarRegistroDeLog(log);
            servidor = (InterfaceDeServidor)registroCliente.lookup(InterfaceDeServidor.ID_DO_CHAT_RMI);
            log = "interface de comunicacao remota criada";
            chatCliente.adicionarRegistroDeLog(log);
        } catch (RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        String nick = JOptionPane.showInputDialog("Digite um nickname para utilizar no chat:");

        try {
            chatCliente.setNickname(nick);
            servidor.adicionarCliente(this, nick);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void receberMensagem(Mensagem mensagem) throws RemoteException {
        chatCliente.receberMensagem(mensagem);
    }
    
    public void enviarMensagem(Mensagem mensagem) {
        try {
            servidor.enviarMensagem(mensagem);
        } catch (RemoteException ex) {
            String log = "Nao foi possivel enviar a mensagem.";
            chatCliente.adicionarRegistroDeLog(log);
            Logger.getLogger(ChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarMensagem(String mensagem) {
        Mensagem msg = new Mensagem(chatCliente.getNickname(), mensagem);
        enviarMensagem(msg);
    }
    
    public void sairDoChat() {
        String log;
        try {
            servidor.removerCliente(this);
            log = "enviado pedido de remocao da lista de subscribers do chat.";
            chatCliente.adicionarRegistroDeLog(log);
        } catch (RemoteException ex) {
            log = "falha ao enviar pedido de remocao da lista de subscribers do chat.";
            chatCliente.adicionarRegistroDeLog(log);
            Logger.getLogger(ChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Mensagem> getCaixaDeEntrada() {
        return chatCliente.getCaixaDeEntrada();
    }
    
    public List<String> getMensagensDeLog() {
        return chatCliente.getMensagensDeLog();
    }

    public String mensagemToString(Mensagem m) {
        return "<" + m.getId() + ">-<" + m.getNickname() + ">:" + m.getConteudo();
    }
    
}
