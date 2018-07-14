package user;

import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/*

I used this code from a homework assignment I had. https://sp18.datastructur.es/materials/hw/hw7/hw7

*/

public class ObjectReader {
    private FileInputStream fis;
    private ObjectInputStream ois;

    public ObjectReader(String filename) throws FileNotFoundException{
        try {
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
        } catch (java.io.IOException e) {
            System.out.println("Error creating ObjectReader: ");
            e.printStackTrace();
        }

    }

    public Object readObject() {
        try {
            return ois.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.out.println("Error reading object: ");
            e.printStackTrace();
            return null;
        }
    }
}

