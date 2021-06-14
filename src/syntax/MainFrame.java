package syntax;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class MainFrame extends JFrame{
    JTextArea inputText;
    JTextArea outputText;
    JButton startButton;
    JButton clearButton;

    {
        inputText = new JTextArea();
        outputText = new JTextArea();
        startButton = new JButton("开始分析");
        clearButton = new JButton("清除");

        inputText.setBounds(100,100,300,400);
        outputText.setBounds(500,100,300,400);
        startButton.setBounds(300,530,100,70);
        clearButton.setBounds(500,530,100,70);

        this.setTitle("语法分析器");
        this.setLayout(null);
        this.add(inputText);
        this.add(outputText);
        this.add(startButton);
        this.add(clearButton);
        this.setBounds(200,200,900,700);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        LexicalAnalyzer.lexicalAnalyal(".\\test.txt");
        SyntaxAnalyzer.syntaxAnalyzer(".\\input.txt",".\\output.txt");
    }
}
