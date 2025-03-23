import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener {
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, registerButton;
    Connection conn;

    public LoginFrame() {
        setTitle("Quiz App Login");
        setSize(400, 300);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        userField = new JTextField();
        add(userField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        add(loginButton);
        add(registerButton);

        connectDB();
        setVisible(true);
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_app", "root", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == registerButton) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?,?)");
                stmt.setString(1, userField.getText());
                stmt.setString(2, new String(passField.getPassword()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration Successful!");
            } else {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                stmt.setString(1, userField.getText());
                stmt.setString(2, new String(passField.getPassword()));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    new QuizApp(userField.getText());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
