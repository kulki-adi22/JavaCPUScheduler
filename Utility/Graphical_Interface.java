package MainProject.src.Utlity;

import MainProject.src.Mainfun.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Graphical_Interface {
    private JFrame frame;
    private JPanel mainP;
    private CustomPanel ganttPanel;
    private JScrollPane scrollPanel1;
    private JScrollPane scrollPanel2;
    private JTable table;
    private JButton addBtn;
    private JButton remBtn;
    private JButton execBtn;
    private JLabel waitTBtn;
    private JLabel WTLabel;
    private JLabel TATLabel;
    private JLabel tatResultLabel;
    private JComboBox option;
    private DefaultTableModel tablem;

    public Graphical_Interface() {
        tablem = new DefaultTableModel(new String[] { "Process Name", "Arrival Time", "Burst Time", "Priority Order",
                "Waiting Time", "Turn Around Time" }, 0);

        table = new JTable(tablem);
        table.setFillsViewportHeight(true);
        scrollPanel1 = new JScrollPane(table);
        scrollPanel1.setBounds(25, 25, 1000, 300);

        addBtn = new JButton("Add");
        addBtn.setBounds(400, 340, 85, 25);
        addBtn.setFont(new Font("Verdana", Font.PLAIN, 11));
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablem.addRow(new String[] { "", "", "", "", "", "" });
            }
        });

        remBtn = new JButton("Remove");
        remBtn.setBounds(500, 340, 85, 25);
        remBtn.setFont(new Font("Verdana", Font.PLAIN, 11));
        remBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();

                if (row > -1) {
                    tablem.removeRow(row);
                }
            }
        });

        ganttPanel = new CustomPanel();
        ganttPanel.setBackground(Color.WHITE);
        scrollPanel2 = new JScrollPane(ganttPanel);
        scrollPanel2.setBounds(25, 420, 1000, 150);

        waitTBtn = new JLabel("Average Waiting Time :");
        waitTBtn.setBounds(27, 600, 198, 30);
        TATLabel = new JLabel("Average Turn Around Time :");
        TATLabel.setBounds(27, 630, 198, 30);
        WTLabel = new JLabel();
        WTLabel.setBounds(200, 600, 200, 30);
        tatResultLabel = new JLabel();
        tatResultLabel.setBounds(200, 630, 200, 30);

        option = new JComboBox(
                new String[] { "FIRST COME FIRST SERVE", "SHORTEST JOB FIRST", "PRIORITY PREEMPTIVE", "ROUND ROBIN" });
        option.setBounds(825, 600, 200, 20);

        execBtn = new JButton("Execute");
        execBtn.setBounds(940, 630, 85, 25);
        execBtn.setFont(new Font("Verdana", Font.PLAIN, 11));
        execBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) option.getSelectedItem();
                CPUSchedule scheduler;

                switch (selected) {
                    case "FIRST COME FIRST SERVE":
                        scheduler = new FirstComeFirstServe();
                        break;
                    case "SHORTEST JOB FIRST":
                        scheduler = new ShortestJobFirst();
                        break;
                    case "PRIORITY PREEMPTIVE":
                        scheduler = new PriorityPreemptive();
                        break;
                    case "ROUND ROBIN":
                        String tq = JOptionPane.showInputDialog("Time Quantum");
                        if (tq == null) {
                            return;
                        }
                        scheduler = new RoundRobin();
                        scheduler.setTimeQuantum(Integer.parseInt(tq));
                        break;
                    default:
                        return;
                }

                for (int i = 0; i < tablem.getRowCount(); i++) {
                    String process = (String) tablem.getValueAt(i, 0);
                    int at = Integer.parseInt((String) tablem.getValueAt(i, 1));
                    int bt = Integer.parseInt((String) tablem.getValueAt(i, 2));
                    int pl;

                    if (selected.equals("PRIORITY PREEMPTIVE")) {
                        if (!tablem.getValueAt(i, 3).equals("")) {
                            pl = Integer.parseInt((String) tablem.getValueAt(i, 3));
                        } else {
                            pl = 1;
                        }
                    } else {
                        pl = 1;
                    }

                    scheduler.addRow(new ProcessClass(process, at, bt, pl));
                }

                scheduler.process();

                for (int i = 0; i < tablem.getRowCount(); i++) {
                    String process = (String) tablem.getValueAt(i, 0);
                    ProcessClass row = scheduler.getRow(process);
                    tablem.setValueAt(row.getwt(), i, 4);
                    tablem.setValueAt(row.getTAT(), i, 5);
                }

                WTLabel.setText(Double.toString(scheduler.getAvgWaitTime()));
                tatResultLabel.setText(Double.toString(scheduler.getAvgTAT()));

                ganttPanel.settimechart(scheduler.getGanttChart());
            }
        });

        mainP = new JPanel(null);
        mainP.setPreferredSize(new Dimension(1050, 1000));
        mainP.add(scrollPanel1);
        mainP.add(addBtn);
        mainP.add(remBtn);
        mainP.add(scrollPanel2);
        mainP.add(waitTBtn);
        mainP.add(TATLabel);
        mainP.add(WTLabel);
        mainP.add(tatResultLabel);
        mainP.add(option);
        mainP.add(execBtn);

        frame = new JFrame("CPU Scheduler User Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(mainP);
        frame.pack();
    }

    public static void main(String[] args) {
        new Graphical_Interface();
    }

    class CustomPanel extends JPanel {
        private List<Instance> timechart;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (timechart != null) {

                for (int i = 0; i < timechart.size(); i++) {
                    Instance event = timechart.get(i);
                    int x = 30 * (i + 1);
                    int y = 20;

                    g.drawRect(x, y, 30, 30);
                    g.setFont(new Font("Verdana", Font.BOLD, 13));
                    g.drawString(event.getPName(), x + 10, y + 20);
                    g.setFont(new Font("Verdana", Font.PLAIN, 11));
                    g.drawString(Integer.toString(event.getbegTime()), x - 5, y + 45);

                    if (i == timechart.size() - 1) {
                        g.drawString(Integer.toString(event.getEndTime()), x + 27, y + 45);
                    }
                }
            }
        }

        public void settimechart(List<Instance> timechart) {
            this.timechart = timechart;
            repaint();
        }
    }
}
