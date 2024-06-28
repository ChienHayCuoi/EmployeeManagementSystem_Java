/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author ADMIN
 */
public class DataConnection {

    public static void writeObjectToFile(String fileName, Object obj) throws FileNotFoundException, IOException {
        FileOutputStream fout = new FileOutputStream(fileName);

        ObjectOutputStream oos = new ObjectOutputStream(fout);

        oos.writeObject(obj);

        fout.close();
        oos.close();
    }

    public static Object readObjectFromFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(fileName);

        ObjectInputStream ois = new ObjectInputStream(fin);

        Object obj = ois.readObject();

        fin.close();
        ois.close();

        return obj;
    }

    
}
