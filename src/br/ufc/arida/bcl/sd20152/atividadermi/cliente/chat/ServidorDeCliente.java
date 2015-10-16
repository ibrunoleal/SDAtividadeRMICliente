/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.arida.bcl.sd20152.atividadermi.cliente.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author brunoleal
 */
public class ServidorDeCliente {

    private Registry registroCliente = null;
    
    private InterfaceDeServidor servidor = null;
    
    private List<String> mensagensDeLog;
    
    private ChatCliente chatcliente;
    
    public ServidorDeCliente(){
        mensagensDeLog = new ArrayList<String>();
    }
    
    public void entrarNoChat() {
        try {
            registrarOcorrenciaNoLog("#registrando...");
            registroCliente = LocateRegistry.getRegistry(1099);
            registrarOcorrenciaNoLog("#registrado");
        } catch (RemoteException e) {
            registrarOcorrenciaNoLog("#erro: servidor nao encontrado para registro");
            e.printStackTrace();
            //System.exit(1);
            return;
        }

        try {
            registrarOcorrenciaNoLog("#criando interface de comunicacao remota");
            servidor = (InterfaceDeServidor) registroCliente.lookup(InterfaceDeServidor.ID_DO_CHAT_RMI);
            registrarOcorrenciaNoLog("#interface de comunicacao remota criada");
        } catch (RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        String nick = JOptionPane.showInputDialog("Digite um nickname para utilizar no chat:");

        try {
            chatcliente = new ChatCliente(nick, servidor);
            chatcliente.entrarNoChat(nick);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void enviarMensagem(String msg) {			
        try {
            Mensagem m = new Mensagem(chatcliente.getNickname(), msg);
            chatcliente.enviarMensagem(m);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorDeCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void sairDoChat() {
        try {
            chatcliente.sairDoChat();
        } catch (RemoteException ex) {
            registrarOcorrenciaNoLog("#erro ao sair do chat");
            Logger.getLogger(ServidorDeCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getMensagensDeLog() {
        return mensagensDeLog;
    }
    

    public void registrarOcorrenciaNoLog(String texto) {
        mensagensDeLog.add(texto);
        System.out.println(texto);
    }
    
    public String mensagemToString(Mensagem m) {
        return "<"+ m.getId() + ">-<" + m.getNickname() + ">:" + m.getConteudo();
    }
    
    public List<Mensagem> getCaixaDeEntrada() {
       return chatcliente.getCaixaDeEntrada();
    }
}
