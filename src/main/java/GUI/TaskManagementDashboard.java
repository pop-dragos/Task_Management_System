package GUI;

import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskManagementDashboard extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JPanel dataEntry;
    private JPanel viewModify;
    private JTextField employeeId;
    private JTextField employeeName;
    private JButton addEmployeeButton;
    private JTextField taskID;
    private JComboBox taskStatus;
    private JButton createTaskButton;
    private JComboBox taskType;
    private JComboBox selectEmployeeID;
    private JComboBox selectTaskID;
    private JButton asignTaskButton;
    private JTextField startHour;
    private JTextField endHour;
    private JComboBox tasksFromEmployee;
    private JTable tasksTable;
    private JLabel totalHoursLabel;
    private JButton modifyStatusButton;
    private JButton viewSubtasksButton;
    private JComboBox possibleSubtasks;
    private JButton addSubtaskButton;
    private JPanel stats;
    private JButton showMapButton;
    private JButton sortEmployeesButton;
    private JTable showMapTable;
    private JScrollPane tasksMapPane;
    private JComboBox selectTaskToModify;
    private JComboBox subtasksOfComplex;
    private JComboBox parentTasks;

    private DefaultTableModel taskModel;
    private DefaultTableModel mapModel;

    private TasksManagement tasksManagement =  new TasksManagement();
    private Utility utility =  new Utility();

    private List<Task> unassignedTasks = new ArrayList<>();

    public TaskManagementDashboard() {
        setTitle("Task Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);

        tasksManagement.read();
        repopulateComboBoxes();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tasksManagement.write();
            }
        });

        taskModel = new DefaultTableModel(new String[]{"ID Task", "Type", "Status", "Duration (h)"}, 0);
        tasksTable.setModel(taskModel);

        mapModel = new DefaultTableModel(new String[]{"Employee Name", "Completed Tasks", "Uncompleted Tasks"}, 0);
        showMapTable.setModel(mapModel);
        tasksMapPane.setVisible(false);

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(employeeId.getText());
                    String name = employeeName.getText();

                    if(tasksManagement.findEmployeeByID(id) != null){
                        JOptionPane.showMessageDialog(TaskManagementDashboard.this, "Employee with ID " + id + " already exists!");
                        return;
                    }

                    Employee emp = new Employee(id, name);

                    tasksManagement.addEmployeeToMap(emp);
                    selectEmployeeID.addItem(emp.getIdEmployee());
                    tasksFromEmployee.addItem(emp.getIdEmployee());

                    employeeId.setText("");
                    employeeName.setText("");
                    JOptionPane.showMessageDialog(TaskManagementDashboard.this, "Employee Added Successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid ID!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        createTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                   int id = Integer.parseInt(taskID.getText());
                   String type = (String) taskType.getSelectedItem();
                   String status = (String) taskStatus.getSelectedItem();

                   Task newTask = null;

                   if (type.equals("Simple")) {
                       try {
                           int start = Integer.parseInt(startHour.getText());
                           int end = Integer.parseInt(endHour.getText());
                           newTask = new SimpleTask(id, status, start, end);
                       } catch (NumberFormatException ex) {
                           JOptionPane.showMessageDialog(null, "Invalid start or end hour!", "Error", JOptionPane.WARNING_MESSAGE);
                           return;
                       }
                   } else {
                       newTask = new ComplexTask(id, status);
                   }

                   unassignedTasks.add(newTask);
                   selectTaskID.addItem(newTask.getIdTask());

                   taskID.setText("");
                   taskType.setSelectedIndex(0);
                   taskStatus.setSelectedIndex(0);
                   startHour.setText("");
                   endHour.setText("");
                   JOptionPane.showMessageDialog(TaskManagementDashboard.this, "Task Added Successfully!");
               } catch (NumberFormatException ex) {
                   JOptionPane.showMessageDialog(null, "Invalid ID!", "Error", JOptionPane.WARNING_MESSAGE);
               }
            }
        });

        taskType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) taskType.getSelectedItem();

                boolean complex;
                if(type.equals("Complex"))
                    complex = true;
                else
                    complex = false;

                startHour.setEnabled(!complex);
                endHour.setEnabled(!complex);
            }
        });

        asignTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idEmployee = (int)selectEmployeeID.getSelectedItem();
                int idTask = (int)selectTaskID.getSelectedItem();

                Task taskToAssign = null;

                for(Task t: unassignedTasks){
                    if(t.getIdTask() == idTask){
                        taskToAssign = t;
                        break;
                    }
                }

                if(taskToAssign != null){
                    tasksManagement.assignTaskToEmployee(idEmployee, taskToAssign);
                    unassignedTasks.remove(taskToAssign);
                    selectTaskID.removeItem(taskToAssign.getIdTask());

                    int selectedIDTab2 = (int)tasksFromEmployee.getSelectedItem();
                    if(selectedIDTab2 == idEmployee)
                        refreshTasksTable();

                    JOptionPane.showMessageDialog(null, "Task " + idTask + " has been assigned to employee " + idEmployee);
                }
            }
        });

        tasksFromEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tasksFromEmployee.getSelectedItem()!= null)
                    refreshTasksTable();
            }
        });

        modifyStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idTask = (int)selectTaskToModify.getSelectedItem();
                int idEmployee = (int)tasksFromEmployee.getSelectedItem();

                tasksManagement.modifyTaskStatus(idEmployee, idTask);

                refreshTasksTable();

                JOptionPane.showMessageDialog(null, "Status for task " + idTask + " has been modified successfully!");
            }
        });

        viewSubtasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idTask = (int)subtasksOfComplex.getSelectedItem();
                int idEmployee = (int)tasksFromEmployee.getSelectedItem();

                List<Task> tasks = tasksManagement.getMap().get(tasksManagement.findEmployeeByID(idEmployee));
                Task selectedTask = tasksManagement.searchTaskRec(tasks, idTask);

                showSubtasksDialog((ComplexTask) selectedTask);
            }
        });

        addSubtaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int parentTaskID =  (int)parentTasks.getSelectedItem();
                int employeeID = (int)tasksFromEmployee.getSelectedItem();
                int childTaskID = (int)possibleSubtasks.getSelectedItem();

                tasksManagement.moveTaskToComplex(employeeID, parentTaskID, childTaskID);
                refreshTasksTable();

                JOptionPane.showMessageDialog(null, "Task " + childTaskID + " has been added to the subtasks list for task " +  parentTaskID + "!");
            }
        });

        parentTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(parentTasks.getSelectedItem() != null && tasksFromEmployee.getSelectedItem() != null) {
                    int idEmployee = (int)tasksFromEmployee.getSelectedItem();
                    int idParentTask = (int)parentTasks.getSelectedItem();

                    possibleSubtasks.removeAllItems();

                    List<Task> tasks = tasksManagement.getMap().get(tasksManagement.findEmployeeByID(idEmployee));
                    for(Task t: tasks){
                        if(t.getIdTask() == idParentTask)
                            continue;
                        if(t instanceof ComplexTask complexTask)
                            if(tasksManagement.searchTaskRec(complexTask.getTaskList(), idParentTask) != null)
                                continue;
                        possibleSubtasks.addItem(t.getIdTask());
                    }
                }
            }
        });

        sortEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskModel.setColumnIdentifiers(new Object[]{"Employee ID", "Employee Name", "Duration (h)"});
                taskModel.setRowCount(0);

                List<Employee> sortedEmployees = utility.sortEmployees(tasksManagement);

                if(sortedEmployees.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No employees with over 40 hours!");
                    refreshTasksTable();
                }
                else{
                    for(Employee emp: sortedEmployees){
                        int workHours = tasksManagement.calculateEmployeeWorkDuration(emp.getIdEmployee());
                        taskModel.addRow(new Object[]{
                                emp.getIdEmployee(),
                                emp.getName(),
                                workHours
                        });
                    }
                }
            }
        });

        showMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Map<String, Integer>> stats = utility.tasksMapByStatus(tasksManagement);

                mapModel.setRowCount(0);

                if(stats.isEmpty()){
                    tasksMapPane.setVisible(false);
                    JOptionPane.showMessageDialog(null, "No stats available!");
                    return;
                }

                for(String name: stats.keySet()){
                    Map<String, Integer> tasksCount = stats.get(name);

                    mapModel.addRow(new Object[]{
                            name,
                            tasksCount.get("Completed"),
                            tasksCount.get("Uncompleted")
                    });
                }

                tasksMapPane.setVisible(true);
            }
        });

        if(tasksFromEmployee.getItemCount() > 0) {
            tasksFromEmployee.setSelectedIndex(0);
            refreshTasksTable();
        }
    }

    private void refreshTasksTable() {
        taskModel.setColumnIdentifiers(new Object[]{"ID Task", "Type", "Status", "Duration (h)"});
        taskModel.setRowCount(0);

        selectTaskToModify.removeAllItems();
        parentTasks.removeAllItems();
        subtasksOfComplex.removeAllItems();
        possibleSubtasks.removeAllItems();

        int selectedID = (int)tasksFromEmployee.getSelectedItem();
        Employee emp = tasksManagement.findEmployeeByID(selectedID);

        if (emp != null) {
                List<Task> tasks = tasksManagement.getMap().get(emp);

                addTasksToModifyRec(tasks);
                populateComplexComboBoxes(tasks);

                for (Task t : tasks) {
                    String type;
                    if (t instanceof SimpleTask)
                        type = "Simple";
                    else
                        type = "Complex";
                    int duration = t.estimateDuration();
                    taskModel.addRow(new Object[]{
                            t.getIdTask(),
                            type,
                            t.getStatusTask(),
                            duration
                    });
                }
            }

        int totalDuration = tasksManagement.calculateEmployeeWorkDuration(selectedID);
        totalHoursLabel.setText("Total Work Duration: " + totalDuration);
    }

    private void showSubtasksDialog(ComplexTask complexTask) {
        JDialog subtasksDialog = new JDialog(this, "Subtasks for ID: " + complexTask.getIdTask(), true);
        subtasksDialog.setSize(300, 200);
        subtasksDialog.setLocationRelativeTo(this);

        DefaultTableModel subtasksModel = new DefaultTableModel(new String[]{"Task ID", "Type", "Status", "Duration (h)"}, 0);
        JTable subtasksTable = new JTable(subtasksModel);

        for(Task subt: complexTask.getTaskList()){
            String type;
            if (subt instanceof SimpleTask)
                type = "Simple";
            else
                type = "Complex";
            int duration = subt.estimateDuration();
            subtasksModel.addRow(new Object[]{
                    subt.getIdTask(),
                    type,
                    subt.getStatusTask(),
                    duration
            });
        }

        subtasksDialog.add(new JScrollPane(subtasksTable));
        subtasksDialog.setVisible(true);
    }

    private void repopulateComboBoxes() {
        selectEmployeeID.removeAllItems();
        tasksFromEmployee.removeAllItems();

        Map<Employee, List<Task>> map = tasksManagement.getMap();
        if(map != null){
            for(Employee emp: map.keySet()){
                selectEmployeeID.addItem(emp.getIdEmployee());
                tasksFromEmployee.addItem(emp.getIdEmployee());
            }
        }
    }

    private void addTasksToModifyRec(List<Task> tasks) {
        for(Task t: tasks){
            selectTaskToModify.addItem(t.getIdTask());
            if(t instanceof ComplexTask complexTask)
                addTasksToModifyRec(complexTask.getTaskList());
        }
    }

    private void populateComplexComboBoxes(List<Task> tasks) {
        for(Task t: tasks){
            if(t instanceof ComplexTask complexTask){
                parentTasks.addItem(complexTask.getIdTask());
                subtasksOfComplex.addItem(complexTask.getIdTask());

                populateComplexComboBoxes(complexTask.getTaskList());
            }
        }
    }

    public static void main(String[] args){
        TaskManagementDashboard frame = new TaskManagementDashboard();
        frame.setVisible(true);
    }
}
