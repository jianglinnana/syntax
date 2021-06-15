package syntax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class MainFrame extends JFrame{
    JTextArea testText;
    JTextArea inputText;
    JTextArea outputText;
    JButton lexicalButton;
    JButton syntaxButton;
    JButton clearButton;

    {
        testText = new JTextArea();
        inputText = new JTextArea();
        outputText = new JTextArea();
        lexicalButton = new JButton("词法分析");
        syntaxButton = new JButton("语法分析");
        clearButton = new JButton("清除");

        testText.setBounds(100,100,300,400);
        inputText.setBounds(100,100,300,400);
        outputText.setBounds(500,100,300,400);
        lexicalButton.setBounds(300,530,100,70);
        syntaxButton.setBounds(300,530,100,70);
        clearButton.setBounds(500,530,100,70);

        this.setTitle("语法分析器");
        this.setLayout(new GridLayout(2,3,10,10));
        this.add(testText);
        this.add(inputText);
        this.add(outputText);
        this.add(lexicalButton);
        this.add(syntaxButton);
        this.add(clearButton);
        this.setBounds(200,200,1200,1000);
        this.setVisible(true);
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
            }
        });
    }
}
