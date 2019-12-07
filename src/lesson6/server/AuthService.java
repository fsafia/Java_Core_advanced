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

    public static String getNickByLoginAndPass(String login, String password)  {
        String sql = String.format("select nickname from users where login = '%s' and password = '%s'", login, password);
        //String sql = String.format("select * from users ");
        ResultSet rs = null; //по нашему запросу может вернуться 1 конкретная строка или 0
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()){ //если вернется 1 строка ->усл выполн, если вернется 0 строк в if не зайдет
                return  rs.getString(1); //при работе с jdbc индексация начин с 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }
}
