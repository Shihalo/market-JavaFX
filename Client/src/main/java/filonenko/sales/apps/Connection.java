package filonenko.sales.apps;

import filonenko.sales.entities.Product;
import filonenko.sales.entities.Sale;
import filonenko.sales.entities.User;
import javafx.scene.control.PasswordField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Connection {

    private static Connection instance = null;

    public static Connection getInstance() {
        if(instance == null) { instance = new Connection(); }
        return instance;
    }

    private PrintStream printStream; //Поток отправки команды
    private ObjectInputStream objectInputStream; //Поток получения объектов
    private ObjectOutputStream objectOutputStream;

    private Connection() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 8071);
            printStream = new PrintStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<User> getUserList() throws IOException, ClassNotFoundException {
        printStream.println("getAllUsers");
        return (List<User>)objectInputStream.readObject();
    }
    public User login(User user) throws IOException, ClassNotFoundException {
        printStream.println("login");
        objectOutputStream.writeObject(user);
        return (User)objectInputStream.readObject();
    }
    public User registration(User user) throws IOException, ClassNotFoundException {
        printStream.println("registration");
        objectOutputStream.writeObject(user);
        return (User)objectInputStream.readObject();
    }
    public User editName(User user, String newName) throws IOException, ClassNotFoundException {
        printStream.println("editName");
        objectOutputStream.writeObject(user);
        printStream.println(newName);
        return (User)objectInputStream.readObject();
    }
    public User editPassword(User user, String newPassword) throws IOException, ClassNotFoundException {
        printStream.println("editPassword");
        objectOutputStream.writeObject(user);
        printStream.println(newPassword);
        return (User)objectInputStream.readObject();
    }
    public void remove(User user) throws IOException {
        printStream.println("remove");
        objectOutputStream.writeObject(user);
    }

    public List<Product> getProductList() throws IOException, ClassNotFoundException {
        printStream.println("getAllProducts");
        return (List<Product>)objectInputStream.readObject();
    }
    public Product editProduct(Product selectedProduct, String name, String firm) throws IOException, ClassNotFoundException {
        printStream.println("editProduct");
        objectOutputStream.writeObject(selectedProduct);
        printStream.println(name);
        printStream.println(firm);
        selectedProduct = (Product)objectInputStream.readObject();
        return selectedProduct;
    }
    public void deleteProduct(Product selectedProduct) throws IOException {
        printStream.println("deleteProduct");
        objectOutputStream.writeObject(selectedProduct);
    }
    public Product addProduct(Product product) throws IOException, ClassNotFoundException {
        printStream.println("addProduct");
        objectOutputStream.writeObject(product);
        return (Product)objectInputStream.readObject();
    }

    public List<Sale> getSalesList() throws IOException, ClassNotFoundException {
        printStream.println("getAllSales");
        return (List<Sale>)objectInputStream.readObject();
    }
}
