package MainProject.src.Utlity;

import java.util.ArrayList;
import java.util.List;

public class copyFun {
    public static List<ProcessClass> copyList(List<ProcessClass> oldList) {
        List<ProcessClass> newList = new ArrayList<ProcessClass>();

        for (ProcessClass row : oldList) {
            newList.add(new ProcessClass(row.getpn(), row.getAt(), row.getbt(), row.getPriority()));
        }

        return newList;
    }
}
