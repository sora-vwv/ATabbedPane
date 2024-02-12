package org.example;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import com.github.weisj.darklaf.theme.OneDarkTheme;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main extends JFrame {

    public Main() {
        super("ATabbedPane");
        LafManager.install(new DarculaTheme());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((dimension.getWidth() - getWidth()) / 2);
            int y = (int) ((dimension.getHeight() - getHeight()) / 2);
            setLocation(x, y);
        }

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        {
            JPanel panel1 = new JPanel(new GridLayout());
            panel1.setMinimumSize(new Dimension(50, (int) panel1.getMinimumSize().getHeight()));
            panel1.setBackground(panel1.getBackground().darker());
            panel.add(panel1, BorderLayout.WEST);
            panel1.add(new Label("    "));
        }

        ATabbedPane pane = new ATabbedPane();
        panel.add(pane, BorderLayout.CENTER);


        for (int i = 0; i < 20; i++)
            pane.addTab("Editor" + "_".repeat(new Random().nextInt(1, 10)) + i, new JEditorPane());

        {
            JPanel panel1 = new JPanel(new GridLayout());
            panel1.setMinimumSize(new Dimension(50, (int) panel1.getMinimumSize().getHeight()));
            panel1.setBackground(panel1.getBackground().darker());
            panel.add(panel1, BorderLayout.EAST);
            panel1.add(new Label("    "));
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

}
