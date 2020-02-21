package fr.ensibs.product;



import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "ProductService", portName = "ProductPort", endpointInterface = "fr.ensibs.product.ProductService")
public class ProductServiceImpl implements ProductService {
    private static final String SECRET_KEY = "secret";
    private static final String AUTHENTICATION_FAILED = "Authentication failed : Invalid credentials.";
    private static final String AUTHENTICATION_SUCCESSFULL = "Authentication successful !";
    private static final String DATABASE_URL = "jdbc:sqlite:boulangerie.db";

    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS products ( \n"
            + " id integer PRIMARY KEY, \n"
            + " productname text UNIQUE, \n"
            + " price double , \n"
            + ");";

    private static final String CREATE_COMMAND_TABLE = "CREATE TABLE IF NOT EXISTS commands ( \n"
            + " id integer PRIMARY KEY, \n"
            + " productname text UNIQUE, \n"
            + " price double , \n"
            + " quantity integer , \n"
            + " isPaid integer NOT NULL"
            + ");";

    public ProductServiceImpl() {
        BakeryDBConnect.initDB();
    }


    /**
     * create a new Command with the given parameters
     * @param productname
     * @param price
     * @param quantity
     * @param isPaid
     */
    @Override
    public void createCommand(String productname, Double price, int quantity, boolean isPaid) {
        String sql = "INSERT INTO commands (productname, price, quantity, isPaid) VALUES (?,?,?,?)";
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.setDouble(2,price);
            pstmt.setInt(3,quantity);
            if(isPaid) pstmt.setInt(4, 1);
            else pstmt.setInt(4, 0);
            pstmt.executeUpdate();
            System.out.println("Command added successfully");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void CommandProduct(int product_id, int quantity) {

    }

    @Override
    public void cancelCommand(int command_id) {
        String sql = "DELETE FROM commands WHERE id = ? ";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(0, command_id);
            pstmt.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Command> getCommands() {
        String sql = "SELECT id,productname FROM commands";
        List<Command> commands = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                //int id = rs.getInt("id");
                String productname = rs.getString("productname");
                double productprice = rs.getDouble("price");
                int quantity =rs.getInt("quantity");
                int isPaid = rs.getInt("isPaid");
                if(isPaid==1)commands.add(new Command(productname,productprice,quantity,true));
                else commands.add(new Command(productname,productprice,quantity,false));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    /**
     * create a new product with the given parameters

     * @param productname
     * @param price
     */
    @Override
    public void createProduct(String productname, Double price) {
        String sql = "INSERT INTO products (productname, price) VALUES (?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.setDouble(2,price);
            pstmt.executeUpdate();
            System.out.println("Product added successfully");
        } catch (SQLException e) { e.printStackTrace(); }
    }


    @Override
    public void removeProduct(String productname) {
        String sql = "DELETE FROM products WHERE productname = ? ";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Product> getProductsCard() {
        String sql = "SELECT id,productname FROM products";
        List<Product> users = new ArrayList<Product>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                //int id = rs.getInt("id");
                String productname = rs.getString("productname");
                double productprice = rs.getDouble("price");
                users.add(new Product(productname,productprice));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Product getProduct(String product_name) {
        String sql = "SELECT id,productname FROM products";
        Product product = new Product();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            //int id = rs.getInt("id");
             String productname = rs.getString("productname");
             double productprice = rs.getDouble("price");

             product.setProduct_Name(product_name);
             product.setPrice(productprice);



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Initialise the database
     */
    private void initDB() {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_PRODUCT_TABLE);
            stmt.execute(CREATE_COMMAND_TABLE);
            System.out.println("USER TABLE created successfully.");
        } catch (SQLException e) { e.printStackTrace(); }
    }


    /**
     * Connect to the DATABASE_URL
     * @return {@Link Connection} object
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection((DATABASE_URL));
            System.out.println("Connection to database has been established.");
        } catch(SQLException e) { e.printStackTrace(); }
        return conn;
    }
}
