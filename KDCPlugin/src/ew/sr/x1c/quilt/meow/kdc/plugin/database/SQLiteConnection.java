package ew.sr.x1c.quilt.meow.kdc.plugin.database;

import ew.sr.x1c.quilt.meow.server.GeneralManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;

public class SQLiteConnection {

    private static ThreadLocal<Connection> CON;

    @Getter
    @Setter
    private static String file;

    public static Connection getConnection() {
        if (CON == null) {
            CON = new SQLiteConnection.ThreadLocalConnection();
        }
        return CON.get();
    }

    public static boolean hasConnection() {
        return CON != null;
    }

    public static void closeAll() throws SQLException {
        for (Connection connection : SQLiteConnection.ThreadLocalConnection.ALL_CONNECTION) {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static final class ThreadLocalConnection extends ThreadLocal<Connection> {

        private static final Collection<Connection> ALL_CONNECTION = new LinkedList<>();

        @Override
        protected final Connection initialValue() {
            try {
                Connection con = DriverManager.getConnection("jdbc:sqlite:" + file);
                ALL_CONNECTION.add(con);
                return con;
            } catch (SQLException ex) {
                GeneralManager.getInstance().getLogger().log(Level.WARNING, "建立資料庫連線時發生例外狀況", ex);
                return null;
            }
        }
    }
}
