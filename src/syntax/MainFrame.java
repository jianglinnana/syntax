package syntax;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    // 输入text
    JTextArea inputText;

    // 输出text
    JTextArea outputText;

    // 开始分析按钮
    JButton startButton;

    // 清除按钮
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
}
