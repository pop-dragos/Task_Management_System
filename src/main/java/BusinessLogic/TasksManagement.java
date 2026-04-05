package BusinessLogic;

import DataAccess.SerializationOperations;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement implements Serializable {
    private Map<Employee, List<Task>> map;

    public TasksManagement() {
        this.map = new HashMap<>();
    }

    public void setMap(Map<Employee, List<Task>> map) {
        this.map = map;
    }

    public Map<Employee, List<Task>> getMap() {
        return map;
    }

    public void addEmployeeToMap(Employee emp) {
        if(findEmployeeByID(emp.getIdEmployee()) == null)
            map.put(emp,new ArrayList<>());
    }

    public Employee findEmployeeByID(int idEmployee){
        for(Employee emp : map.keySet()){
            if(emp.getIdEmployee() == idEmployee)
                return emp;
        }
        return null;
    }

    public void assignTaskToEmployee(int idEmployee, Task task) {
        Employee emp = findEmployeeByID(idEmployee);
        if(emp != null)
            map.get(emp).add(task);
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        int workDuration = 0;
        Employee emp = findEmployeeByID(idEmployee);
         if(emp != null) {
             for (Task t : map.get(emp))
                 if (t.getStatusTask().equals("Completed"))
                     workDuration += t.estimateDuration();
         }

        return workDuration;
    }

    public void modifyTaskStatus(int idEmployee, int idTask) {
        Employee emp = findEmployeeByID(idEmployee);
        List<Task> tasks = map.get(emp);

        Task taskToModify = searchTaskRec(tasks, idTask);

        if(taskToModify != null) {
            if(taskToModify.getStatusTask().equals("Completed"))
                taskToModify.setStatusTask("Uncompleted");
            else
                taskToModify.setStatusTask("Completed");
        }
    }

    public void moveTaskToComplex(int idEmployee, int idParentTask, int idChildTask) {
        Employee emp = findEmployeeByID(idEmployee);
        if(emp != null) {
            List<Task> tasks = map.get(emp);
            Task parentTask = searchTaskRec(tasks, idParentTask);
            Task childTask = searchTaskRec(tasks, idChildTask);

            if (parentTask instanceof ComplexTask && childTask != null && idParentTask != idChildTask) {
                ((ComplexTask) parentTask).addTask(childTask);
                tasks.remove(childTask);
            }
        }
    }

    public Task searchTaskRec(List<Task> tasks, int idTask){
        for(Task t : tasks){
            if(t.getIdTask() == idTask)
                return t;
            if(t instanceof ComplexTask complexTask){
                Task searchInSubtasks = searchTaskRec(complexTask.getTaskList(), idTask);
                if(searchInSubtasks != null)
                    return searchInSubtasks;
            }
        }
        return null;
    }

    public void write() {
        try {
            SerializationOperations.writeData(this.map, "data.txt");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void read() {
        try {
            this.map = SerializationOperations.readData("data.txt");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
