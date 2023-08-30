import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Objects;

public class ATabbedPane extends JPanel {

    private static final int WIDTH_TAB_MARGIN = 10;

    ArrayList<ATabbedPaneTab> tabs = new ArrayList<>(1);

    private final JPanel rootTabsPanel;
    private final JScrollPane scrollTabsPanel;

    private final JPanel tabsPanel;
    private final JPanel currentPanel;
    private ATabbedPaneTab selected = null;

    public ATabbedPane() {
        super(new BorderLayout());

        rootTabsPanel = new JPanel();
        rootTabsPanel.setLayout(new BoxLayout(rootTabsPanel, BoxLayout.X_AXIS));
        rootTabsPanel.setMaximumSize(new Dimension(this.getMaximumSize().width, 25));
        rootTabsPanel.setMinimumSize(new Dimension(this.getMinimumSize().width, 25));

        tabsPanel = new JPanel();
        tabsPanel.setLayout(new BoxLayout(tabsPanel, BoxLayout.X_AXIS));
        tabsPanel.setMaximumSize(new Dimension(this.getMaximumSize().width, 25));
        scrollTabsPanel = new AScrollPane(tabsPanel);

        rootTabsPanel.add(scrollTabsPanel);
        {
            Color background = new JButton().getBackground();

            JLabel left = new JLabel("<");
            rootTabsPanel.add(left);
            left.setOpaque(true);
            left.setBackground(background);
            left.setFont(left.getFont().deriveFont(Font.BOLD));
            left.setBorder(new EmptyBorder(0,WIDTH_TAB_MARGIN,0,WIDTH_TAB_MARGIN));
            left.setMaximumSize(new Dimension(25, 25));
            left.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    left();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    left.setBackground(background.brighter());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    left.setBackground(background);
                }
            });

            JLabel right = new JLabel(">");
            rootTabsPanel.add(right);
            right.setOpaque(true);
            right.setBackground(background);
            right.setFont(right.getFont().deriveFont(Font.BOLD));
            right.setBorder(new EmptyBorder(0,WIDTH_TAB_MARGIN,0,WIDTH_TAB_MARGIN));
            right.setMaximumSize(new Dimension(25, 25));
            right.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    right();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    right.setBackground(background.brighter());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    right.setBackground(background);
                }
            });
        }
        add(rootTabsPanel, BorderLayout.NORTH);

        currentPanel = new JPanel(new GridLayout());
        add(currentPanel, BorderLayout.CENTER);
    }

    public void addTab(String name, Component component) {
        ATabbedPaneTab tab = new ATabbedPaneTab(name, component);
        tabsPanel.add(tab);
        tabs.add(tab);
    }

    public ATabbedPaneTab get(int index) {
        if (index >= 0 && index < tabs.size())
            return tabs.get(index);
        return null;
    }

    public ATabbedPaneTab get(Component component) {
        for (ATabbedPaneTab tab: tabs)
            if (Objects.equals(tab.component, component))
                return tab;
        return null;
    }

    public void select(int index) {
        if (index >= 0 && index < tabs.size())
            tabs.get(index).select();
    }

    public void select(Component component) {
        for (ATabbedPaneTab tab: tabs)
            if (Objects.equals(tab.component, component))
                tab.select();
    }

    public void select(ATabbedPaneTab tab) {
        tab.select();
    }

    public void removeTab(Component component) {
        ArrayList<ATabbedPaneTab> tabs_temp = new ArrayList<>();
        for (int index = 0; index < tabs.size(); index++) {
            ATabbedPaneTab tab = tabs.get(index);
            if (Objects.equals(tab.component, component)) {
                tabsPanel.remove(tab);

                if (selected == tab) {
                    if (tabs_temp.size() > 0)
                        tabs_temp.get(tabs_temp.size() - 1).select();
                    else if (index + 1 < tabs.size()) {
                        tabs.get(index + 1).select();
                        selected = tabs.get(index + 1);
                    } else {
                        currentPanel.removeAll();
                        currentPanel.revalidate();
                        currentPanel.repaint();
                        selected = null;
                    }
                }
            } else
                tabs_temp.add(tab);
        }
        tabsPanel.revalidate();
        tabsPanel.repaint();
        rootTabsPanel.repaint();
        tabs = tabs_temp;
    }

    public void removeTab(int index) {
        removeTab(tabs.get(index));
    }

    public void removeTab(ATabbedPaneTab index) {
        removeTab(index.component);
    }

    private void left() {
        if (selected == null) return;
        if (tabs.size() < 2) return;
        int index = tabs.indexOf(selected);
        if (index == 0) return;

        tabs.set(index, tabs.get(index-1));
        tabs.set(index-1, selected);

        tabsPanel.removeAll();
        tabsPanel.revalidate();
        tabsPanel.repaint();

        for (ATabbedPaneTab tab: tabs)
            tabsPanel.add(tab);
        rootTabsPanel.repaint();

        int x = selected.getX() - selected.getWidth() - scrollTabsPanel.getX();
        if (x < scrollTabsPanel.getHorizontalScrollBar().getValue())
            scrollTabsPanel.getHorizontalScrollBar().setValue(x);
    }

    private void right() {
        if (selected == null) return;
        if (tabs.size() < 2) return;
        int index = tabs.indexOf(selected);
        if (index == tabs.size()-1) return;

        tabs.set(index, tabs.get(index+1));
        tabs.set(index+1, selected);

        tabsPanel.removeAll();
        tabsPanel.revalidate();
        tabsPanel.repaint();

        for (ATabbedPaneTab tab: tabs)
            tabsPanel.add(tab);
        rootTabsPanel.repaint();

        int x = selected.getX() - scrollTabsPanel.getWidth() + selected.getWidth() + tabs.get(index).getWidth() - scrollTabsPanel.getX();
        if (x > scrollTabsPanel.getHorizontalScrollBar().getValue())
            scrollTabsPanel.getHorizontalScrollBar().setValue(x);
    }

    public class ATabbedPaneTab extends JPanel {
        private final Component component;

        private final Color unselect;
        private final Color select;

        private ATabbedPaneTab(String title, Component component) {
            super();

            JLabel label = new JLabel(title);
            label.setBorder(new EmptyBorder(0,WIDTH_TAB_MARGIN,0,WIDTH_TAB_MARGIN));
            setMaximumSize(new Dimension(label.getMinimumSize().width+WIDTH_TAB_MARGIN, getMaximumSize().height));
            unselect = getBackground();
            select = getBackground().brighter();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1)
                        select();
                    if (e.getButton() == MouseEvent.BUTTON3)
                        removeTab(component);
                }
            });
            add(label);
            this.component = component;
        }

        void select() {
            if (!tabs.contains(this))
                return;
            for(ATabbedPaneTab tab: tabs)
                tab.unselect();
            selected = this;
            currentPanel.removeAll();
            currentPanel.revalidate();
            currentPanel.repaint();
            currentPanel.add(component);
            setBackground(select);
        }

        private void unselect() {
            if (selected != this) return;
            setBackground(unselect);
        }
    }

    private class AScrollPane extends JScrollPane {
        public AScrollPane(Component component) {
            super(component);
            final JScrollBar horizontalScrollBar = getHorizontalScrollBar();
            setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            setWheelScrollingEnabled(false);
            addMouseWheelListener(new MouseAdapter() {
                public void mouseWheelMoved(MouseWheelEvent evt) {
                    if (evt.getWheelRotation() >= 1) {
                        int iScrollAmount = evt.getScrollAmount();
                        int iNewValue = horizontalScrollBar.getValue() + horizontalScrollBar.getBlockIncrement() * iScrollAmount * Math.abs(evt.getWheelRotation());
                        horizontalScrollBar.setValue(Math.min(iNewValue, horizontalScrollBar.getMaximum()));
                        rootTabsPanel.repaint();
                    } else if (evt.getWheelRotation() <= -1)
                    {
                        int iScrollAmount = evt.getScrollAmount();
                        int iNewValue = horizontalScrollBar.getValue() - horizontalScrollBar.getBlockIncrement() * iScrollAmount * Math.abs(evt.getWheelRotation());
                        horizontalScrollBar.setValue(Math.max(iNewValue, 0));
                        rootTabsPanel.repaint();
                    }
                }
            });
        }
    }

}
