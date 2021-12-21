package ew.sr.x1c.quilt.meow.kdc.plugin.handler;

import ew.sr.x1c.quilt.meow.kdc.plugin.database.SQLiteConnection;
import ew.sr.x1c.quilt.meow.server.Client;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class MemberHandler {

    public static String getCurrentFormatDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static boolean register(Client client, String name, String password) {
        if (queryAccountId(name) >= 0) {
            return false;
        }

        Connection con = SQLiteConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO account (name, password, session_ip, last_login) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, SHAHandler.SHA512(password));
            ps.setString(3, client.getSession().getChannel().remoteAddress().toString());
            ps.setString(4, getCurrentFormatDate());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "發生資料庫例外狀況", ex);
            return false;
        }
    }

    public static boolean login(Client client, String name, String password) {
        Connection con = SQLiteConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM account WHERE name = ? AND password = ?")) {
            ps.setString(1, name);
            ps.setString(2, SHAHandler.SHA512(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    client.setAccount(name);
                    return true;
                }
            }
        } catch (SQLException ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "發生資料庫例外狀況", ex);
        }
        return false;
    }

    public static int queryAccountId(String name) {
        Connection con = SQLiteConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT id FROM account WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "發生資料庫例外狀況", ex);
            return Integer.MIN_VALUE;
        }
        return -1;
    }

    public static byte[] queryKey(String name) {
        int id = queryAccountId(name);
        if (id < 0) {
            return null;
        }
        return queryKey(id);
    }

    public static byte[] queryKey(int id) {
        Connection con = SQLiteConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT key FROM key WHERE user_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("key");
                }
            }
        } catch (SQLException ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "發生資料庫例外狀況", ex);
            return null;
        }
        return new byte[0];
    }

    public static boolean updateKey(String name, byte[] key) {
        int id = queryAccountId(name);
        if (id < 0) {
            return false;
        }

        Connection con = SQLiteConnection.getConnection();
        try {
            byte[] originKey = queryKey(id);
            if (originKey != null) {
                if (originKey.length <= 0) {
                    try (PreparedStatement ps = con.prepareStatement("INSERT INTO key (user_id, key) VALUES (?, ?)")) {
                        ps.setInt(1, id);
                        ps.setBytes(2, key);
                        ps.executeUpdate();
                        return true;
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement("UPDATE key SET key = ? WHERE user_id = ?")) {
                        ps.setBytes(1, key);
                        ps.setInt(2, id);
                        ps.executeUpdate();
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "發生資料庫例外狀況", ex);
        }
        return false;
    }
}
