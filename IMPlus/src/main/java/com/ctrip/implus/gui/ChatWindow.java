package com.ctrip.implus.gui;

import com.ctrip.implus.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by chengyq on 2016/10/26.
 */
public class ChatWindow extends JFrame implements ActionListener, WindowListener {

    public ChatWindow(ChatListener listener) {

        super();
        this.setSize(880, 600);
        this.getContentPane().setLayout(null);
        this.add(getJLabel(), null);
        this.add(getJTextArea(), null);
        this.add(getjTextField(), null);
        this.add(getJButton(), null);
        this.add(getJLabel2());
        this.add(getJTextArea2());
        this.setTitle("聊天");
        this.setLocationRelativeTo(null);

        this.addWindowListener(this);

        this.listener = listener;
    }

    public void warn(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void appendMessage(String from, String message) {
        this.jTextArea.append(from + "  说: " + message + "\r\n");
        JScrollBar jsb = this.areaScrollPane.getVerticalScrollBar();
        jsb.setValue(jsb.getMaximum());
    }

    public void addMember(String member) {
        this.jTextArea2.append(member + "\r\n");
        JScrollBar jsb = this.areaScrollPane2.getVerticalScrollBar();
        jsb.setValue(jsb.getMaximum());
    }

    public void removeMember(String member){
        String members = this.jTextArea2.getText();
        this.jTextArea2.setText(members.replace(member+"\r\n",""));
    }

    public void actionPerformed(ActionEvent e) {
        //selectionButtonPressed();
        String input = this.jTextField.getText().trim();
        this.jTextField.setText("");
        this.listener.sendMessage(input);
    }

    //window listener
    public void windowOpened(WindowEvent e) {
        this.jTextField.requestFocus();
    }

    public void windowClosing(WindowEvent e) {
        this.listener.onExit();
    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
    //window listener


    private javax.swing.JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setBounds(34, 49, 53, 18);
            jLabel.setText("消息:");
        }
        return jLabel;
    }

    private javax.swing.JScrollPane getJTextArea() {
        if (areaScrollPane == null) {
            jTextArea = new javax.swing.JTextArea();
            //jTextArea.setBounds(96, 49, 460, 400);

            jTextArea.setColumns(20);
            jTextArea.setLineWrap(true);
            jTextArea.setRows(5);
            jTextArea.setWrapStyleWord(true);
            jTextArea.setEditable(false);

            areaScrollPane = new JScrollPane(jTextArea);
            areaScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            areaScrollPane.setBounds(96, 49, 460, 400);
            //areaScrollPane.setPreferredSize(new Dimension(250, 250));
        }
        return areaScrollPane;
    }

    private JTextField getjTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setBounds(96, 49 + 410, 380, 27);
            jTextField.grabFocus();
            jTextField.addActionListener(this);
        }

        return jTextField;
    }

    private javax.swing.JButton getJButton() {
        if (jButton == null) {
            jButton = new javax.swing.JButton();
            jButton.setBounds(96 + 390, 49 + 410, 70, 27);
            jButton.setText("发送");

            jButton.addActionListener(this);

        }
        return jButton;
    }

    private javax.swing.JLabel getJLabel2() {
        if (jLabel2 == null) {
            jLabel2 = new javax.swing.JLabel();
            //left,top,width,height
            jLabel2.setBounds(34 + 560, 49, 53, 18);
            jLabel2.setText("成员:");
        }
        return jLabel2;
    }

    private javax.swing.JScrollPane getJTextArea2() {
        if (areaScrollPane2 == null) {
            jTextArea2 = new javax.swing.JTextArea();

            jTextArea2.setColumns(20);
            jTextArea2.setLineWrap(true);
            jTextArea2.setRows(5);
            jTextArea2.setWrapStyleWord(true);
            jTextArea2.setEditable(false);

            areaScrollPane2 = new JScrollPane(jTextArea2);
            areaScrollPane2.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            areaScrollPane2.setBounds(34 + 560 + 60, 49, 200, 400);
        }
        return areaScrollPane2;
    }

    private JLabel jLabel;
    private JTextArea jTextArea;
    private JTextField jTextField;
    private JButton jButton;
    private JScrollPane areaScrollPane;

    private JLabel jLabel2;
    private JTextArea jTextArea2;
    private JScrollPane areaScrollPane2;

    private ChatListener listener;
}
