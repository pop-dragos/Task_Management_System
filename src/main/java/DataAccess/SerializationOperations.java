package DataAccess;

import DataModel.Employee;
import DataModel.Task;

import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class SerializationOperations {
    public static void writeData(Map<Employee, List<Task>> map, String fileName) throws IOException,  ClassNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(map);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public static Map<Employee, List<Task>> readData(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Map<Employee, List<Task>> restoredMap = (Map<Employee, List<Task>>) objectInputStream.readObject();
        objectInputStream.close();
        return restoredMap;
    }
}
