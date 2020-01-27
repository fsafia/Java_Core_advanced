package lesson6.server;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //добавление в таблицу users новых зарегистрированных клиентов
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
    //добавление в таблицу blackList новых строк
    public static String addUserIntoDBtableBlackList(/*String login, String pass, String nick*/int idUser, int idBlockUser) {
        String msg = "";

        if (idBlockUser != 0){
            try {
                //проверим:  заблокирован ли idBlockUser уже у клиента idUser?
                //получение  списка заблокированных клиентов для текущего idUser
                Set<Integer> blockUsersList = getListIdBlockUsersById(idUser);
                if(blockUsersList.contains(idBlockUser)){
                    msg = " - уже в черном списке";
                }
                else{
                    String query = "INSERT INTO tableBlackList (id_user, id_blockUser) VALUES (?, ?);";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, idUser);
                    ps.setInt(2, idBlockUser);
                    ps.executeUpdate();
                    msg = "  - добавлен в черный список";
                }
                //
                /*String query = "INSERT INTO tableBlackList (id_user, id_blockUser) VALUES (?, ?);";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, idUser);
                ps.setInt(2, idBlockUser);
                ps.executeUpdate();
                msg = "  - добавлен в черный список";*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            msg = " - пользователь не зарегистрирован в чате";
        }
        return msg;
    }

    //получение ID по nick. по нашему запросу может вернуться 1 конкретная строка или 0 - если такого nick в DB users не существует
    public static int getIdByNick( String nick) {
        String sql = String.format("select id from users where nickname = '%s'", nick);
        ResultSet rs = null; //по нашему запросу может вернуться 1 конкретная строка или 0
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()) { //если вернется 1 строка ->усл выполн, если вернется 0 строк в if не зайдет
                int id = rs.getInt(1);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getNickByLoginAndPass(String login, String password) {
        String sql = String.format("select nickname, password from users where login = '%s'", login);
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

    //получение  списка заблокированных клиентов для текущего по нику  из объединенных таблиц users и tableBlackList
    public static Set<Integer> getListIdBlockUsersByNick(String nickThisClient){

        Set<Integer> blockUserList = new HashSet<>();
        String sql = String.format("select nickname, id, id_blockUser from users inner join tableBlackList on users.id = tableBlackList.id_user where nickname = '%s'", nickThisClient);
        ResultSet rs = null; //по нашему запросу может вернуться 0 или несколько строк
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                int id_blockUser = rs.getInt(3);
                blockUserList.add(id_blockUser);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return blockUserList;
    }

    //получение  списка заблокированных клиентов для текущего по id из объединенных таблиц users и tableBlackList
    public static Set<Integer> getListIdBlockUsersById(Integer idThisClient){

        Set<Integer> blockUserList = new HashSet<>();
        String sql = String.format("select nickname, id, id_blockUser from users inner join tableBlackList on users.id = tableBlackList.id_user where id = '%s'", idThisClient);
        ResultSet rs = null; //по нашему запросу может вернуться 0 или несколько строк
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                int id_blockUser = rs.getInt(3);
                blockUserList.add(id_blockUser);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return blockUserList;
    }

    //для проверки уникальности логина при регистрации в базе данных
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
