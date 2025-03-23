import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class QuizApp extends JFrame implements ActionListener {
    private String username;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;
    private JButton nextButton, submitButton;
    private JLabel questionLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup bg;
    private int score = 0, index = 0;
    private int correctOption;
    
    public QuizApp(String user) {
        this.username = user;
        setTitle("Java Quiz App");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        questionLabel = new JLabel("Question here...");
        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(questionLabel);
        
        bg = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            bg.add(options[i]);
            panel.add(options[i]);
        }
        
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");
        nextButton.addActionListener(this);
        submitButton.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        connectDB();
        loadQuestion();
        setVisible(true);
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_app", "root", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQuestion() {
        try {
            stmt = conn.prepareStatement("SELECT * FROM questions LIMIT ?,1");
            stmt.setInt(1, index);
            rs = stmt.executeQuery();
            if (rs.next()) {
                questionLabel.setText(rs.getString("question"));
                options[0].setText(rs.getString("option1"));
                options[1].setText(rs.getString("option2"));
                options[2].setText(rs.getString("option3"));
                options[3].setText(rs.getString("option4"));
                correctOption = rs.getInt("correct_option") - 1;
            } else {
                JOptionPane.showMessageDialog(this, "Quiz Finished! Your Score: " + score);
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == nextButton) {
            for (int i = 0; i < 4; i++) {
                if (options[i].isSelected() && i == correctOption) {
                    score++;
                }
            }
            index++;
            bg.clearSelection();
            loadQuestion();
        } else if (ae.getSource() == submitButton) {
            JOptionPane.showMessageDialog(this, "Quiz Finished! Your Score: " + score);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
