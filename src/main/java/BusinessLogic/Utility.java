package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.util.*;

public class Utility {
        public List<Employee> sortEmployees(TasksManagement tm) {
        Map<Employee, List<Task>> map = tm.getMap();

        ArrayList<Employee> sortedEmployees = new ArrayList<Employee>();
        Map<Integer, Integer> tempWorkDurations = new HashMap<>();

        for(Employee emp: map.keySet()) {
            int workDuration = tm.calculateEmployeeWorkDuration(emp.getIdEmployee());
            if(workDuration > 40) {
                tempWorkDurations.put(emp.getIdEmployee(), workDuration);
                sortedEmployees.add(emp);
            }
        }

        Collections.sort(sortedEmployees, (emp1, emp2) -> {
            return Integer.compare(tempWorkDurations.get(emp1.getIdEmployee()), tempWorkDurations.get(emp2.getIdEmployee()));
        });

        return sortedEmployees;
    }

    public Map<String, Map<String, Integer>> tasksMapByStatus(TasksManagement tm) {
        Map<Employee, List<Task>> map = tm.getMap();
        Map<String, Map<String, Integer>> tasksMap = new HashMap<>();

        for(Employee emp : map.keySet()) {
            Map<String, Integer> statusMap = new HashMap<>();

            int completedTasks = 0;
            int uncompletedTasks = 0;

            for(Task task: map.get(emp)) {
                if(task.getStatusTask().equals("Completed"))
                    completedTasks++;
                else
                    uncompletedTasks++;
            }

            statusMap.put("Completed", completedTasks);
            statusMap.put("Uncompleted", uncompletedTasks);

            tasksMap.put(emp.getName(), statusMap);
        }
        return tasksMap;
    }
}
