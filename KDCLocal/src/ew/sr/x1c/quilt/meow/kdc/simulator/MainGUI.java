package ew.sr.x1c.quilt.meow.kdc.simulator;

import java.awt.EventQueue;
import java.awt.Image;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.Getter;

public class MainGUI extends JFrame {

    private final Image icon;

    @Getter
    private static MainGUI instance;

    @Getter
    private Logger logger;

    @Getter
    private Logger AliceLogger;

    @Getter
    private Logger BobLogger;

    @Getter
    private Logger KDCLogger;

    private void initEndpoint() {
        Alice.getInstance();
        Bob.getInstance();
    }

    private void registerLogHandler(Logger logger, Handler... handlerList) {
        for (Handler handler : handlerList) {
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        }
    }

    private void setupLogger(Logger logger) {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    private void initLogger() {
        logger = Logger.getLogger(getClass().getName());
        AliceLogger = Logger.getLogger("Alice");
        BobLogger = Logger.getLogger("Bob");
        KDCLogger = Logger.getLogger("KDC");
        setupLogger(logger);
        setupLogger(AliceLogger);
        setupLogger(BobLogger);
        setupLogger(KDCLogger);
        registerLogHandler(logger, new ConsoleHandler(), new TextAreaLogHandler(new TextAreaOutputStream(txtAreaLog)));
        registerLogHandler(AliceLogger, new ConsoleHandler(), new TextAreaLogHandler(new TextAreaOutputStream(txtAreaAlice)));
        registerLogHandler(BobLogger, new ConsoleHandler(), new TextAreaLogHandler(new TextAreaOutputStream(txtAreaBob)));
        registerLogHandler(KDCLogger, new ConsoleHandler(), new TextAreaLogHandler(new TextAreaOutputStream(txtAreaKDC)));
    }

    public MainGUI() {
        icon = new ImageIcon(getClass().getClassLoader().getResource("ew/sr/x1c/image/Icon.png")).getImage();
        instance = this;
        initComponents();
        initLogger();
        initEndpoint();
        showKeyInfo();
    }

    private void showKeyInfo() {
        Alice.getInstance().showKeyInfo();
        KDC.showKeyInfo();
        Bob.getInstance().showKeyInfo();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpMain = new javax.swing.JTabbedPane();
        panelDemo = new javax.swing.JPanel();
        labelKDCDemo = new javax.swing.JLabel();
        labelAlice = new javax.swing.JLabel();
        spAlice = new javax.swing.JScrollPane();
        txtAreaAlice = new javax.swing.JTextArea();
        labelKDC = new javax.swing.JLabel();
        labelBob = new javax.swing.JLabel();
        btnDemo = new javax.swing.JButton();
        spKDC = new javax.swing.JScrollPane();
        txtAreaKDC = new javax.swing.JTextArea();
        spBob = new javax.swing.JScrollPane();
        txtAreaBob = new javax.swing.JTextArea();
        panelLog = new javax.swing.JPanel();
        labelLog = new javax.swing.JLabel();
        spLog = new javax.swing.JScrollPane();
        txtAreaLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("KDC Demo");
        setIconImage(icon);
        setName("MainGUI"); // NOI18N
        setResizable(false);

        labelKDCDemo.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelKDCDemo.setText("KDC 範例");

        labelAlice.setText("Alice");

        txtAreaAlice.setEditable(false);
        txtAreaAlice.setColumns(20);
        txtAreaAlice.setLineWrap(true);
        txtAreaAlice.setRows(5);
        txtAreaAlice.setWrapStyleWord(true);
        spAlice.setViewportView(txtAreaAlice);

        labelKDC.setText("KDC");

        labelBob.setText("Bob");

        btnDemo.setText("Demo");
        btnDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDemoActionPerformed(evt);
            }
        });

        txtAreaKDC.setEditable(false);
        txtAreaKDC.setColumns(20);
        txtAreaKDC.setLineWrap(true);
        txtAreaKDC.setRows(5);
        txtAreaKDC.setWrapStyleWord(true);
        spKDC.setViewportView(txtAreaKDC);

        txtAreaBob.setEditable(false);
        txtAreaBob.setColumns(20);
        txtAreaBob.setLineWrap(true);
        txtAreaBob.setRows(5);
        txtAreaBob.setWrapStyleWord(true);
        spBob.setViewportView(txtAreaBob);

        javax.swing.GroupLayout panelDemoLayout = new javax.swing.GroupLayout(panelDemo);
        panelDemo.setLayout(panelDemoLayout);
        panelDemoLayout.setHorizontalGroup(
            panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDemoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDemoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spAlice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelAlice))
                        .addGap(18, 18, 18)
                        .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spKDC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelKDC))
                        .addGap(18, 18, 18)
                        .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDemoLayout.createSequentialGroup()
                                .addComponent(spBob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(68, Short.MAX_VALUE))
                            .addGroup(panelDemoLayout.createSequentialGroup()
                                .addComponent(labelBob)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelDemoLayout.createSequentialGroup()
                        .addComponent(labelKDCDemo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDemo)
                        .addContainerGap())))
        );
        panelDemoLayout.setVerticalGroup(
            panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDemoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKDCDemo)
                    .addComponent(btnDemo))
                .addGap(10, 10, 10)
                .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelAlice)
                    .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelKDC)
                        .addComponent(labelBob)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spKDC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addComponent(spAlice, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spBob))
                .addContainerGap())
        );

        tpMain.addTab("Demo", panelDemo);

        labelLog.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelLog.setText("系統紀錄");

        txtAreaLog.setEditable(false);
        txtAreaLog.setColumns(20);
        txtAreaLog.setLineWrap(true);
        txtAreaLog.setRows(5);
        txtAreaLog.setWrapStyleWord(true);
        spLog.setViewportView(txtAreaLog);

        javax.swing.GroupLayout panelLogLayout = new javax.swing.GroupLayout(panelLog);
        panelLog.setLayout(panelLogLayout);
        panelLogLayout.setHorizontalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLogLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(spLog, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                    .addGroup(panelLogLayout.createSequentialGroup()
                        .addComponent(labelLog)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLogLayout.setVerticalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelLog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spLog, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpMain.addTab("紀錄", panelLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpMain)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDemoActionPerformed
        btnDemo.setEnabled(false);
        txtAreaAlice.setText("");
        txtAreaKDC.setText("");
        txtAreaBob.setText("");

        Alice.getInstance().renewNonce();
        Bob.getInstance().renewNonce();
        showKeyInfo();
        new Thread(() -> {
            try {
                Alice.getInstance().start();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Demo 執行時發生例外狀況", ex);
            } finally {
                btnDemo.setEnabled(true);
            }
        }).start();
    }//GEN-LAST:event_btnDemoActionPerformed

    public static void initLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
    }

    public static void main(String args[]) {
        initLookAndFeel();
        EventQueue.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDemo;
    private javax.swing.JLabel labelAlice;
    private javax.swing.JLabel labelBob;
    private javax.swing.JLabel labelKDC;
    private javax.swing.JLabel labelKDCDemo;
    private javax.swing.JLabel labelLog;
    private javax.swing.JPanel panelDemo;
    private javax.swing.JPanel panelLog;
    private javax.swing.JScrollPane spAlice;
    private javax.swing.JScrollPane spBob;
    private javax.swing.JScrollPane spKDC;
    private javax.swing.JScrollPane spLog;
    private javax.swing.JTabbedPane tpMain;
    private javax.swing.JTextArea txtAreaAlice;
    private javax.swing.JTextArea txtAreaBob;
    private javax.swing.JTextArea txtAreaKDC;
    private javax.swing.JTextArea txtAreaLog;
    // End of variables declaration//GEN-END:variables
}
