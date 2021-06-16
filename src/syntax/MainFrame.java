package syntax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{
    JTextArea testText;
    JTextArea inputText;
    JTextArea outputText;
    JButton lexicalButton;
    JButton syntaxButton;
    JButton clearButton;
    JPanel panel1;
    JPanel panel2;
    JPanel panel3;
    JScrollPane pane1;
    JScrollPane pane2;
    JScrollPane pane3;

    {
        testText = new JTextArea();
        inputText = new JTextArea();
        outputText = new JTextArea();
        lexicalButton = new JButton("词法分析");
        syntaxButton = new JButton("语法分析");
        clearButton = new JButton("清除");
        pane1 = new JScrollPane(outputText);
        pane2 = new JScrollPane(testText);
        pane3 = new JScrollPane(inputText);
        panel1 =new JPanel(new GridLayout(1, 2,10,10));
        panel2 =new JPanel(new GridLayout(1, 1));
        panel3 =new JPanel(new GridLayout(1, 3,10,10));

        testText.setBounds(100,100,300,400);
        inputText.setBounds(100,100,300,400);
        outputText.setBounds(500,100,300,400);
        lexicalButton.setBounds(300,530,100,70);
        syntaxButton.setBounds(300,530,100,70);
        clearButton.setBounds(500,530,100,70);

        panel1.add(pane2);
        panel1.add(pane3);
        panel2.add(pane1);
        panel3.add(lexicalButton);
        panel3.add(syntaxButton);
        panel3.add(clearButton);

        this.setTitle("语法分析器");
        this.setLayout(new GridLayout(3,1,10,10));
        this.add(panel1);
        this.add(panel2);
        this.add(panel3);
        this.setBounds(200,200,1200,1000);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.lexicalButton.addActionListener(e -> {
            mainFrame.inputText.setText("");
            String str = mainFrame.testText.getText();
            StringBuffer stringBuffer = LexicalAnalyzer.getInputText(str,".\\input.txt");
            mainFrame.inputText.setText(stringBuffer.toString());
        });
        mainFrame.syntaxButton.addActionListener(e -> {
            mainFrame.outputText.setText("");
            String str = mainFrame.inputText.getText();
            StringBuffer stringBuffer = SyntaxAnalyzer.getoutBuffer(".\\input.txt", ".\\output.txt");
            mainFrame.outputText.setText(stringBuffer.toString());
        });
        mainFrame.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.outputText.setText("");
                mainFrame.inputText.setText("");
                LexicalAnalyzer.outputBuffer = new StringBuffer();
                SyntaxAnalyzer.outputBuffer = new StringBuffer();
            }
        });
    }
}
