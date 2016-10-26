package com.ctrip.implus.gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by chengyq on 2016/10/26.
 */
public class LoginWindow extends JFrame implements ActionListener {

    public LoginWindow(LoginInterface li) {

        super();
        this.setSize(300, 200);
        this.getContentPane().setLayout(null);
        this.add(getJLabel(), null);
        this.add(getJTextField(), null);
        this.add(getJButton(), null);
        this.setTitle("登录");
        this.setLocationRelativeTo(null);

        this.li = li;
    }


    private javax.swing.JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setBounds(34, 49, 53, 18);
            jLabel.setText("昵称:");
        }
        return jLabel;
    }

    private javax.swing.JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new javax.swing.JTextField();
            jTextField.setBounds(96, 49, 160, 20);
        }
        return jTextField;
    }

    private javax.swing.JButton getJButton() {
        if (jButton == null) {
            jButton = new javax.swing.JButton();
            jButton.setBounds(103, 110, 71, 27);
            jButton.setText("登录");

            jButton.addActionListener(this);

        }
        return jButton;
    }

    public void actionPerformed(ActionEvent e) {
        this.li.login(this.jTextField.getText().trim());
    }

    public void warn(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private JLabel jLabel;
    private JTextField jTextField;
    private JButton jButton;

    private LoginInterface li;
}
