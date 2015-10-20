package br.ufc.arida.bcl.sd20152.atividadermi.cliente.chat;

import java.util.ArrayList;
import java.util.List;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;

public class ChatCliente {

    private String nickname;

    private List<Mensagem> caixaDeEntrada;
    
    private List<String> mensagensDeLog;

    protected ChatCliente(String nickname) {
        this.nickname = nickname;
        caixaDeEntrada = new ArrayList<Mensagem>();
        mensagensDeLog = new ArrayList<String>();
    }
    
    protected ChatCliente() {
        caixaDeEntrada = new ArrayList<Mensagem>();
        mensagensDeLog = new ArrayList<String>();
    }

    protected boolean receberMensagem(Mensagem mensagem) {
        if (caixaDeEntrada.add(mensagem)) {
            return true;
        }
        return false;
    }

    protected String getNickname() {
        return nickname;
    }

    protected void setNickname(String nickname) {
        this.nickname = nickname;
    }

    protected synchronized List<Mensagem> getCaixaDeEntrada() {
        List<Mensagem> listaDeMensagens = new ArrayList<Mensagem>(caixaDeEntrada);
        return listaDeMensagens;
    }

    protected synchronized List<String> getMensagensDeLog() {
        List<String> listaDeLogs = new ArrayList<>(mensagensDeLog);
        return listaDeLogs;
    }
    
    protected void adicionarRegistroDeLog(String registroDeLog) {
        String log = "#" + registroDeLog;
        mensagensDeLog.add(log);
        System.out.println(log);
    }

}
