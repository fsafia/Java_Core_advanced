package lesson6.server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() {   //метод подключения
        try {
            Class.forName("org.sqlite.JDBC"); //создается экземпляр драйвер, к которому будем обращаться
            connection = DriverManager.getConnection("jdbc:sqlite:main.db"); //если бы база находилась в другом месте писали бы полный URL
            stmt = connection.createStatement();                          //наша база находится в корне проекта-> запись такая
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void disconnect() {   //метод отключен.ия
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addUser(String login, String pass, String nick) {
        try {
            String query = "INSERT INTO users (login, password, nickname) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setInt(2, pass.hashCode());
            ps.setString(3, nick);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String password) {
        String sql = String.format("select nickname, password from users where login = '%s'", login);
        //String sql = String.format("select * from users ");
        ResultSet rs = null; //по нашему запросу может вернуться 1 конкретная строка или 0
        try {
            rs = stmt.executeQuery(sql);
            int myHash = password.hashCode();
            if (rs.next()) { //если вернется 1 строка ->усл выполн, если вернется 0 строк в if не зайдет
                String nick = rs.getString(1);
                int dbHash = rs.getInt(2);
                if (myHash == dbHash) {
                    return nick;
                }
                //return  rs.getString(1); //при работе с jdbc индексация начин с 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //для проверки уникальности ника в базе данных
    public static boolean isLoginUnique(String login) {
        boolean rezult = true;
        String sql = String.format("select id from users where login = '%s'", login);
        ResultSet rs = null; //по нашему запросу может вернуться 1 строка или 0
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                rezult = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezult;
    }


}
