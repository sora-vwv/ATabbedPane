package org.example;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.OneDarkTheme;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        super("ATabbedPane");
        LafManager.install(new OneDarkTheme());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);

        JPanel panel = new JPanel(new GridLayout());
        add(panel);

        ATabbedPane pane = new ATabbedPane();
        panel.add(pane);

        {
            JEditorPane editorPane1 = new JEditorPane();
            pane.addTab("Editor_1", editorPane1);
            JEditorPane editorPane2 = new JEditorPane();
            pane.addTab("Editor_2", editorPane2);
            JEditorPane editorPane3 = new JEditorPane();
            pane.addTab("Editor_3", editorPane3);
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

}
