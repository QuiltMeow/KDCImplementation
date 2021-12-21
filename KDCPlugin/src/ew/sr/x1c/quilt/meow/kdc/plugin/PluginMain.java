package ew.sr.x1c.quilt.meow.kdc.plugin;

import ew.sr.x1c.quilt.meow.kdc.constant.PluginConstant;
import ew.sr.x1c.quilt.meow.kdc.plugin.database.SQLiteConnection;
import ew.sr.x1c.quilt.meow.kdc.plugin.listener.PacketListener;
import ew.sr.x1c.quilt.meow.plugin.ExternalLibraryLoader;
import ew.sr.x1c.quilt.meow.plugin.Plugin;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;

public class PluginMain extends Plugin {

    @Override
    public void onEnable() {
        loadLibrary();
        SQLiteConnection.setFile(PluginConstant.DATABASE);
        getPluginManager().registerListener(this, new PacketListener());
    }

    private void loadLibrary() {
        try {
            ExternalLibraryLoader.loadLibrary(ExternalLibraryLoader.getJarURL(new File("./lib/SQLite JDBC.jar")));
        } catch (Exception ex) {
            getGeneralManager().getLogger().log(Level.SEVERE, "外部函式庫無法載入", ex);
        }
    }

    @Override
    public void onDisable() {
        if (SQLiteConnection.hasConnection()) {
            try {
                SQLiteConnection.closeAll();
            } catch (SQLException ex) {
                getGeneralManager().getLogger().log(Level.SEVERE, "關閉資料庫連線時發生例外狀況", ex);
            }
        }
    }
}
