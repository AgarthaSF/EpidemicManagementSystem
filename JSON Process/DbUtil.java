import java.sql.*;

public class DbUtil {
    public static final String URL = "jdbc:mysql://localhost:3306/epidemic_prevention";
    public static final String USER = "root";
    public static final String PASSWORD = "liuyuyang020516";
    private static Connection conn = null;
    private static Statement statement=null;
    static{
        try {
            // load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // connect db
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return conn;
    }
    public static Statement getStatement(){
        return statement;
    }



    public static int insertDistrict(int adcode, String name, int current_confirmed_count, int suspected_count) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "insert into district_epidemic(district_adcode, district_name, current_confirmed_count, suspected_count) values(?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, adcode);
            ps.setObject(2, name);
            ps.setObject(3, current_confirmed_count);
            ps.setObject(4, suspected_count);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        }
    }


    public static int insertCity(int adcode, String name, int current_confirmed_count, int suspected_count,
                                  int parent_district, int risk_level) throws SQLException{
        try (PreparedStatement ps = getConnection().prepareStatement(
                "insert into city_epidemic(city_adcode, city_name, current_confirmed_count, suspected_count, parent_adcode, risk_level) " +
                        "values(?,?,?,?,?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, adcode);
            ps.setObject(2, name);
            ps.setObject(3, current_confirmed_count);
            ps.setObject(4, suspected_count);
            ps.setObject(5, parent_district);
            ps.setObject(6, risk_level);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        }
    }




}