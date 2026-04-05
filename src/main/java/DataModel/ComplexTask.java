package DataModel;

import java.util.ArrayList;

public final class ComplexTask extends Task {
    private ArrayList<Task> taskList;

    public ComplexTask(int idTask, String statusTask) {
        super(idTask, statusTask);
        taskList = new ArrayList<Task>();
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    @Override
    public int estimateDuration() {
        int duration = 0;
        for (Task t : taskList)
            duration += t.estimateDuration();
        return duration;
    }
}
