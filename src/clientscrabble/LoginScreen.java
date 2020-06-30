package clientscrabble;

//import static clientscrabble.ClientScrabble.BoardSize;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

public class LoginScreen {

    private JDialog loginDialog;
    private JTextField usernameField;
    private JPasswordField passwordField;
    public boolean flag = false;
    //int BoardSize = 20;
    // GameWindow gameWindow = new GameWindow(true);
    GameMainController controller;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // try {

//					LoginScreen window = new LoginScreen();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginScreen(GameMainController controller) {
        this.controller = controller;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        Player player = new Player();

        loginDialog = new JDialog();
        loginDialog.setBounds(100, 100, 450, 300);
        loginDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginDialog.getContentPane().setLayout(null);

        JLabel lblEnterUsernameAnd = new JLabel("Enter Username and Password to Join the Game");
        lblEnterUsernameAnd.setBounds(44, 19, 303, 16);
        loginDialog.getContentPane().add(lblEnterUsernameAnd);

        JLabel lblUserName = new JLabel("User Name");
        lblUserName.setBounds(44, 62, 74, 16);
        loginDialog.getContentPane().add(lblUserName);

        usernameField = new JTextField();
        usernameField.setBounds(130, 56, 209, 28);
        loginDialog.getContentPane().add(usernameField);
        usernameField.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(44, 106, 61, 16);
        loginDialog.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 96, 209, 28);
        loginDialog.getContentPane().add(passwordField);

        JCheckBox chckbxShowPassword = new JCheckBox("Show Password");

        chckbxShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chckbxShowPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });
        chckbxShowPassword.setBounds(130, 136, 128, 23);
        loginDialog.getContentPane().add(chckbxShowPassword);

        JButton LoginButton = new JButton("Login");
        JButton RegisterButton = new JButton("Register");
        RegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter the User Name and Password to Proceed", "Error",
                            JOptionPane.PLAIN_MESSAGE);
                    flag = false;
                } else {
                    // Password Encryption Algorithm Implementation
                    flag = true;
                    player.setUsername(usernameField.getText());
                    String passwordToEncrpt = passwordField.getText();
                    player.setPassword(passwordToEncrpt);
                    String generatedPassword = null;
                    try {
                        // Create MessageDigest instance for MD5
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        // Add password bytes to digest
                        md.update(passwordToEncrpt.getBytes());
                        // Get the hash's bytes
                        byte[] bytes = md.digest();
                        // This bytes[] has bytes in decimal format;
                        // Convert it to hexadecimal format
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < bytes.length; i++) {
                            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                        }
                        // Get complete hashed password in hex format
                        generatedPassword = sb.toString();

                        controller.registerUser(player.getUsername(), generatedPassword);

                        loginDialog.dispatchEvent(new WindowEvent(loginDialog, WindowEvent.WINDOW_CLOSING));

                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Password to encrypt " + passwordToEncrpt);
                    System.out.println(generatedPassword);
                    // gameWindow = new GameWindow(BoardSize);
                }
            }
        });

        LoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter the User Name and Password to Proceed", "Error",
                            JOptionPane.PLAIN_MESSAGE);
                    flag = false;
                } else {
                    // Password Encryption Algorithm Implementation
                    flag = true;
                    //player.setUsername(usernameField.getText());
                    String passwordToEncrpt = passwordField.getText();
                    //player.setPassword(passwordToEncrpt);
                    String generatedPassword = null;
                    try {
                        // Create MessageDigest instance for MD5
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        // Add password bytes to digest
                        md.update(passwordToEncrpt.getBytes());
                        // Get the hash's bytes
                        byte[] bytes = md.digest();
                        // This bytes[] has bytes in decimal format;
                        // Convert it to hexadecimal format
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < bytes.length; i++) {
                            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                        }
                        // Get complete hashed password in hex format
                        generatedPassword = sb.toString();
                        
                        

                        controller.loginUser(usernameField.getText(), generatedPassword);

                        loginDialog.dispatchEvent(new WindowEvent(loginDialog, WindowEvent.WINDOW_CLOSING));

                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Password to encrypt " + passwordToEncrpt);
                    System.out.println(generatedPassword);
                    // gameWindow = new GameWindow(BoardSize);
                }
            }
        });

        RegisterButton.setBounds(100, 180, 117, 29);
        loginDialog.getContentPane().add(RegisterButton);

        LoginButton.setBounds(230, 180, 117, 29);
        loginDialog.getContentPane().add(LoginButton);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
                flag = false;
            }
        });
        btnClear.setBounds(168, 221, 117, 29);
        loginDialog.getContentPane().add(btnClear);

        loginDialog.setAlwaysOnTop(true);
        loginDialog.setVisible(true);
    }
}
