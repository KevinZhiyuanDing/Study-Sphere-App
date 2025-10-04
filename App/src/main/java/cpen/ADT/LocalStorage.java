// Move to cpen/adt/LocalStorage.java
package cpen.ADT;

import java.io.*;
import java.util.*;
import cpen.ADT.User;
import cpen.ADT.Session;

public class LocalStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_NAME = "study_sphere_data.ser";

    public Map<String, User> users = new HashMap<>();
    public List<Session> sessions = new ArrayList<>();

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LocalStorage load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (LocalStorage) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new LocalStorage();
        }
    }
}
