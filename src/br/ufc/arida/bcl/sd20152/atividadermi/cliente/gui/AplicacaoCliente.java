/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.arida.bcl.sd20152.atividadermi.cliente.gui;

import br.ufc.arida.bcl.sd20152.atividadermi.cliente.chat.ChatClienteController;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author brunoleal
 */
public class AplicacaoCliente extends javax.swing.JFrame {
    
    private ChatClienteController chatClienteController;
    
    private int contadorDeMensagensDeLog;
    
    private int contadorDeMensagens;

    /**
     * Creates new form AplicacaoCliente
     */
    public AplicacaoCliente() {
        initComponents();

        try {
            chatClienteController = new ChatClienteController();
        } catch (RemoteException ex) {
            Logger.getLogger(AplicacaoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        contadorDeMensagensDeLog = 0;
        contadorDeMensagens = 0;
    }

    public void entrarNoChat() {
        chatClienteController.entrarNoChat();
        try {
            threadAtualizador.start();
        } catch (IllegalThreadStateException e) {
            /*
            captura o erro se a thread já esta iniciada, se o usuario tentou conectar novamente.
            */
            //nao precisa fazer nada
        }
    }
    
    public void enviarMensagem(String msg) {
        chatClienteController.enviarMensagem(msg);
    }
    
    public void atualizarTelaDeMensagens() {
        
        int contLogTemp = chatClienteController.getMensagensDeLog().size();
        while (contadorDeMensagensDeLog < contLogTemp) {
            jTextArea1.append(chatClienteController.getMensagensDeLog().get(contadorDeMensagensDeLog) + "\n");
            contadorDeMensagensDeLog++;
        }
        
        int contMsgsTemp = chatClienteController.getCaixaDeEntrada().size();
        while (contadorDeMensagens < contMsgsTemp) {
            Mensagem m = chatClienteController.getCaixaDeEntrada().get(contadorDeMensagens);
            jTextArea1.append(chatClienteController.mensagemToString(m) + "\n");
            contadorDeMensagens++;
        }
    }
    
    public void atualizarTabelaDeUsuarios() {
        String log;
        /*
         atualiza a lista de usuarios conectados no sentido da lista de usuarios para a tabela:
         adiciona os que conectaram
         */
        DefaultTableModel modeloDeTabelaUsuarios = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < chatClienteController.getListaDeNicknameDosUsuarios().size(); i++) {
            String nicktemp = chatClienteController.getListaDeNicknameDosUsuarios().get(i);
            if (!isNickNaTabela(nicktemp)) {
                modeloDeTabelaUsuarios.addRow(new Object[]{nicktemp});
                log = "usuario " + nicktemp + " entrou no chat.";
                chatClienteController.adicionarRegistroDeLog(log);
            }
        }

        /*
         atualiza a lista de usuarios conectados no sentido da tabela para a lista de usuarios:
         remove os que desconectaram.
         */
        DefaultTableModel modeloDeTabelaUsuarios2 = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < modeloDeTabelaUsuarios2.getRowCount(); i++) {
            String nicktemp = modeloDeTabelaUsuarios2.getValueAt(i, 0).toString();
            if (!chatClienteController.isUsuarioNaListaDeUsuarios(nicktemp)) {
                modeloDeTabelaUsuarios2.removeRow(i);
                log = "usuario " + nicktemp + " saiu no chat.";
                chatClienteController.adicionarRegistroDeLog(log);
            }
        }
    }
    
    Thread threadAtualizador = new Thread() {

        @Override
        public void run() {
            while (true) {
                if (chatClienteController.isConectado()) {
                    /*
                     atualiza as mensagens de log
                     */
                    atualizarTelaDeMensagens();
                    atualizarTabelaDeUsuarios();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AplicacaoCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("debug: threa acha que chatClientController nao estao conectado");
                }
            }
        }
    };
    
    public void desconectarDoChat() {
        chatClienteController.sairDoChat();
        limparTabelaDeUsuarios();
    }
    
    public void limparAreaDeConversa() {
        jTextArea1.setText("");
    }
    
    public void limparTabelaDeUsuarios() {
        DefaultTableModel modeloDeTabelaUsuarios = (DefaultTableModel) jTable1.getModel();
        while(modeloDeTabelaUsuarios.getRowCount() > 0) {
            modeloDeTabelaUsuarios.removeRow(0);
        }
    }
    
    private boolean isNickNaTabela(String nicktemp) {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (nicktemp.equalsIgnoreCase(jTable1.getModel().getValueAt(i, 0).toString())) {
                return true;
            }
        }
        return false;
    }
    
    public void enviarMensagem() {
        String texto = jTextField1.getText();
        enviarMensagem(texto);
        jTextField1.setText("");
    }
    
    private void exibirTelaSobre() {
        String sobre = "UFC"
                + "\nMDCC"
                + "\nSistemas Distribuídos e Redes de Computadores - 2015-2"
                + "\nAtividade RMI - Aplicacao Cliente"
                + "\nAutor: Bruno Leal";
        JOptionPane.showMessageDialog(this, sobre);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nickname"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
        }

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        fileMenu.setMnemonic('f');
        fileMenu.setText("Arquivo");

        jMenuItem1.setText("Entrar no Chat");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        jMenuItem2.setText("Desconectar do Chat");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);
        fileMenu.add(jSeparator1);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Sair");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText("Editar");

        jMenuItem3.setText("Limpar Tela de Conversa");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        menuBar.add(jMenu1);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Ajuda");

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (chatClienteController.isConectado()) {
            desconectarDoChat();
        }
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        entrarNoChat();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        enviarMensagem();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        desconectarDoChat();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        limparAreaDeConversa();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        exibirTelaSobre();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AplicacaoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AplicacaoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AplicacaoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AplicacaoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplicacaoCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
 
}
