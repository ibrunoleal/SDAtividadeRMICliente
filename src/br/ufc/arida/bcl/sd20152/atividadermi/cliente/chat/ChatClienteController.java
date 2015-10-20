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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author brunoleal
 */
public class ChatClienteController  extends UnicastRemoteObject implements InterfaceDeCliente {
    
    private Registry registroCliente;
    
    ChatCliente chatCliente;
    
    private InterfaceDeServidor servidor;
    
    public ChatClienteController() throws RemoteException{
        chatCliente = new ChatCliente();
        registroCliente = null;
        servidor = null;
    }
    
    public void entrarNoChat() {
        String log;
        if (!isConectado()) {
           
            try {
                log = "registrando...";
                registroCliente = LocateRegistry.getRegistry(InterfaceDeServidor.IP_DO_SERVIDOR, 1099);
                adicionarRegistroDeLog(log);
            } catch (RemoteException e) {
                log = "erro: servidor nao encontrado para registro";
                adicionarRegistroDeLog(log);
                e.printStackTrace();
                //System.exit(1);
            }

            try {
                log = "criando interface de comunicacao remota";
                adicionarRegistroDeLog(log);
                servidor = (InterfaceDeServidor) registroCliente.lookup(InterfaceDeServidor.ID_DO_CHAT_RMI);
                log = "interface de comunicacao remota criada";
                adicionarRegistroDeLog(log);
            } catch (RemoteException | NotBoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String nick = JOptionPane.showInputDialog("Digite um nickname para utilizar no chat:");

            try {
                chatCliente.setNickname(nick);
                if (servidor.adicionarCliente(this, nick)) {
                    log = "adicionado com sucesso da lista de subscribers do chat.";
                    adicionarRegistroDeLog(log);
                } else {
                    log = "erro ao tentar entrar na lista de subscribers do chat.";
                    adicionarRegistroDeLog(log);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            log = "operacao nao permitida: ja esta conectado";
            chatCliente.adicionarRegistroDeLog(log);
        }
    }

    @Override
    public boolean receberMensagem(Mensagem mensagem) throws RemoteException {
        return chatCliente.receberMensagem(mensagem);
    }
    
    public void enviarMensagem(Mensagem mensagem) {
        String log;
        if (isConectado()) {
            try {
                servidor.enviarMensagem(mensagem);
            } catch (RemoteException ex) {
                log = "Nao foi possivel enviar a mensagem.";
                adicionarRegistroDeLog(log);
                Logger.getLogger(ChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            log = "operacao nao permitida: nao esta conectado";
            adicionarRegistroDeLog(log);
        }
    }
    
    public void enviarMensagem(String mensagem) {
        Mensagem msg = new Mensagem(chatCliente.getNickname(), mensagem);
        enviarMensagem(msg);
    }
    
    public void sairDoChat() {
        String log;
        if (isConectado()) {
            try {
                if (servidor.removerCliente(this)) {
                    log = "removido com sucesso da lista de subscribers do chat.";
                    adicionarRegistroDeLog(log);
                } else {
                    log = "erro: nao foi removido da lista de subscribers do chat.";
                    adicionarRegistroDeLog(log);
                }
            } catch (RemoteException ex) {
                log = "falha ao enviar pedido de remocao da lista de subscribers do chat.";
                chatCliente.adicionarRegistroDeLog(log);
                Logger.getLogger(ChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                desconectarServidor();
            }
        } else {
            log = "operacao nao permitida: nao esta conectado";
            adicionarRegistroDeLog(log);
        }
    }
    
    public List<String> getListaDeNicknameDosUsuarios() {
        if (isConectado()) {
            List<String> lista;
            try {
                lista = new ArrayList<>(servidor.getNicknamesDosUsuarios());
                return lista;
            } catch (RemoteException ex) {
                String log = "nao foi possivel recuperar a lista de nickname dos subscribers do chat.";
                adicionarRegistroDeLog(log);
                Logger.getLogger(ChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        } else {
            String log = "operacao nao permitida - getListaDeNicknameDosUsuarios(): nao esta conectado";
            adicionarRegistroDeLog(log);
            return null;
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

    public boolean isUsuarioNaListaDeUsuarios(String nicktemp) {
        for (String nick : getListaDeNicknameDosUsuarios()) {
            if (nick.equalsIgnoreCase(nicktemp)) {
                return true;
            }
        }
        return false;
    }
    
    public void adicionarRegistroDeLog(String registroDeLog) {
        chatCliente.adicionarRegistroDeLog(registroDeLog);
    }
    
    public void desconectarServidor() {
        if (registroCliente != null) {
            registroCliente = null;
        }
        if (servidor != null) {
            servidor = null;
        }
        String log = "finalizada a comunicacao com o servidor. "
                + "Para enviar mensagens se conecte novamente";
        adicionarRegistroDeLog(log);
    }
    
    public boolean isConectado() {
        if (registroCliente != null && servidor != null) {
            return true;
        } else {
            return false;
        }
    }
    
}
