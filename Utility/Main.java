package MainProject.src.Utlity;

import MainProject.src.Mainfun.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("----------------FIRST COME FIRST SERVED----------------");
        fcfs();
        System.out.println("-----------------SHORTEST JOB FIRST-----------------");
        sjf();
        System.out.println("-----------------PRIORITY SCHEDULING (PREEMPTIVE)-----------------");
        psp();
        System.out.println("-----------------ROUND ROBIN SCHEDULING------------------");
        rr();
    }

    public static void fcfs() {
        CPUSchedule fcfs = new FirstComeFirstServe();
        fcfs.addRow(new ProcessClass("P1", 0, 5));
        fcfs.addRow(new ProcessClass("P2", 2, 4));
        fcfs.addRow(new ProcessClass("P3", 4, 3));
        fcfs.addRow(new ProcessClass("P4", 6, 6));
        fcfs.process();
        display(fcfs);
    }

    public static void sjf() {
        CPUSchedule sjf = new ShortestJobFirst();
        sjf.addRow(new ProcessClass("P1", 0, 5));
        sjf.addRow(new ProcessClass("P2", 2, 3));
        sjf.addRow(new ProcessClass("P3", 4, 2));
        sjf.addRow(new ProcessClass("P4", 6, 4));
        sjf.addRow(new ProcessClass("P5", 7, 1));
        sjf.process();
        display(sjf);
    }

    public static void psp() {
        CPUSchedule psp = new PriorityPreemptive();
        psp.addRow(new ProcessClass("P1", 8, 1));
        psp.addRow(new ProcessClass("P2", 5, 1));
        psp.addRow(new ProcessClass("P3", 2, 7));
        psp.addRow(new ProcessClass("P4", 4, 3));
        psp.addRow(new ProcessClass("P5", 2, 8));
        psp.addRow(new ProcessClass("P6", 4, 2));
        psp.addRow(new ProcessClass("P7", 3, 5));
        psp.process();
        display(psp);
    }

    public static void rr() {
        CPUSchedule rr = new RoundRobin();
        rr.setTimeQuantum(2);
        rr.addRow(new ProcessClass("P1", 0, 4));
        rr.addRow(new ProcessClass("P2", 1, 5));
        rr.addRow(new ProcessClass("P3", 2, 6));
        rr.addRow(new ProcessClass("P4", 4, 1));
        rr.addRow(new ProcessClass("P5", 6, 3));
        rr.addRow(new ProcessClass("P6", 7, 2));
        rr.process();
        display(rr);
    }

    public static void display(CPUSchedule object) {
        System.out.println("Process\tAT\tBT\tWT\tTAT");

        for (ProcessClass row : object.getRowList()) {
            System.out.println(
                    row.getpn() + "\t" + row.getAt() + "\t" + row.getbt() + "\t" + row.getwt() + "\t" + row.getTAT());
        }

        System.out.println();

        for (int i = 0; i < object.getGanttChart().size(); i++) {
            List<Instance> timeline = object.getGanttChart();
            System.out.print(timeline.get(i).getbegTime() + "(" + timeline.get(i).getPName() + ")");

            if (i == object.getGanttChart().size() - 1) {
                System.out.print(timeline.get(i).getEndTime());
            }
        }

        System.out.println("\n\nAverage WT: " + object.getAvgWaitTime() + "\nAverage TAT: " + object.getAvgTAT());
    }
}
