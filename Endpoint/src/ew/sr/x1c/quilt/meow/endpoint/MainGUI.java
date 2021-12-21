package ew.sr.x1c.quilt.meow.endpoint;

import ew.sr.x1c.quilt.meow.endpoint.client.CommunicateClient;
import ew.sr.x1c.quilt.meow.endpoint.client.kdc.KDCSession;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.endpoint.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.endpoint.key.KeyData;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.CryptMode;
import ew.sr.x1c.quilt.meow.endpoint.server.CommunicateServer;
import ew.sr.x1c.quilt.meow.endpoint.util.ImageUtil;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Document;
import lombok.Getter;
import lombok.Setter;

public class MainGUI extends JFrame {

    private static final String[] THEME = {
        "javax.swing.plaf.nimbus.NimbusLookAndFeel",
        "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
        "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
        "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel",
        "com.jtattoo.plaf.acryl.AcrylLookAndFeel",
        "com.jtattoo.plaf.aero.AeroLookAndFeel",
        "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel",
        "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel",
        "com.jtattoo.plaf.fast.FastLookAndFeel",
        "com.jtattoo.plaf.graphite.GraphiteLookAndFeel",
        "com.jtattoo.plaf.hifi.HiFiLookAndFeel",
        "com.jtattoo.plaf.luna.LunaLookAndFeel",
        "com.jtattoo.plaf.mcwin.McWinLookAndFeel",
        "com.jtattoo.plaf.mint.MintLookAndFeel",
        "com.jtattoo.plaf.noire.NoireLookAndFeel",
        "com.jtattoo.plaf.smart.SmartLookAndFeel",
        "com.jtattoo.plaf.texture.TextureLookAndFeel"
    };

    private final Image icon;

    @Getter
    private static MainGUI instance;

    @Getter
    private Logger logger;

    @Getter
    private Logger messageLogger;

    @Getter
    private final UserStorage userStorage;

    @Getter
    private KDCSession kdcSession;

    @Getter
    @Setter
    private String localAccount;

    @Getter
    private CommunicateServer server;

    @Getter
    private final File downloadFolder;

    private final DefaultListModel listModel;

    public KDCSession getKDCSession() {
        return kdcSession;
    }

    private void registerLogHandler(Logger logger, Handler... handlerList) {
        for (Handler handler : handlerList) {
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        }
    }

    public boolean showEncryptMessage() {
        return chkShowEncryptMessage.isSelected();
    }

    private void setupLogger(Logger logger) {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    private void initLogger() {
        logger = Logger.getLogger(getClass().getName());
        messageLogger = Logger.getLogger("message");
        setupLogger(logger);
        setupLogger(messageLogger);
        registerLogHandler(logger, new ConsoleHandler(), new TextLogHandler(new TextAreaOutputStream(txtAreaLog)));
        registerLogHandler(messageLogger, new ConsoleHandler(), new TextLogHandler(new TextPaneOutputStream(txtPaneMessageReceive)));
    }

    public void updateLoginStatus(boolean success) {
        if (success) {
            localAccount = txtAccount.getText();
            processConnectEnable();
        } else {
            enableMemberControl(true);
        }
    }

    private void enableMemberControl(boolean enable) {
        txtAccount.setEnabled(enable);
        pfPassword.setEnabled(enable);
        btnKDCRegister.setEnabled(enable);
        btnKDCLogin.setEnabled(enable);
    }

    private void enableKDCConnectControl(boolean enable) {
        txtKDCIP.setEnabled(enable);
        txtKDCPort.setEnabled(enable);
        btnConnectKDC.setEnabled(enable);
    }

    private MainGUI() {
        icon = new ImageIcon(getClass().getClassLoader().getResource("ew/sr/x1c/image/Icon.png")).getImage();
        instance = this;

        initComponents();
        initLogger();

        userStorage = new UserStorage();
        downloadFolder = new File("./download");
        downloadFolder.mkdir();

        listModel = new DefaultListModel();
        listUser.setModel(listModel);
    }

    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().length() == 0;
    }

    public void userAdd(String name) {
        listModel.addElement(name);
    }

    public void userRemove(String name) {
        listModel.removeElement(name);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgCryptMode = new javax.swing.ButtonGroup();
        tpMain = new javax.swing.JTabbedPane();
        panelSetting = new javax.swing.JPanel();
        labelKDCConnect = new javax.swing.JLabel();
        labelKDCIP = new javax.swing.JLabel();
        txtKDCIP = new javax.swing.JTextField();
        labelKDCPort = new javax.swing.JLabel();
        txtKDCPort = new javax.swing.JTextField();
        btnConnectKDC = new javax.swing.JButton();
        btnBrowsePublicKey = new javax.swing.JButton();
        labelKey = new javax.swing.JLabel();
        labelPublicKey = new javax.swing.JLabel();
        labelPrivateKey = new javax.swing.JLabel();
        btnBrowsePrivateKey = new javax.swing.JButton();
        labelAccount = new javax.swing.JLabel();
        txtAccount = new javax.swing.JTextField();
        labelPassword = new javax.swing.JLabel();
        btnKDCRegister = new javax.swing.JButton();
        btnUpdateKDCPublicKey = new javax.swing.JButton();
        labelKDCMember = new javax.swing.JLabel();
        pfPassword = new javax.swing.JPasswordField();
        btnKDCLogin = new javax.swing.JButton();
        labelSkin = new javax.swing.JLabel();
        cbSkin = new javax.swing.JComboBox<>();
        panelConnect = new javax.swing.JPanel();
        labelClient = new javax.swing.JLabel();
        labelConnectIP = new javax.swing.JLabel();
        txtConnectIP = new javax.swing.JTextField();
        labelConnectPort = new javax.swing.JLabel();
        txtConnectPort = new javax.swing.JTextField();
        labelRemoteName = new javax.swing.JLabel();
        txtRemoteName = new javax.swing.JTextField();
        labelServer = new javax.swing.JLabel();
        labelServerPort = new javax.swing.JLabel();
        txtServerPort = new javax.swing.JTextField();
        btnClientConnect = new javax.swing.JButton();
        btnServerStart = new javax.swing.JButton();
        panelMessage = new javax.swing.JPanel();
        labelListUser = new javax.swing.JLabel();
        spListUser = new javax.swing.JScrollPane();
        listUser = new javax.swing.JList<>();
        labelMessageSend = new javax.swing.JLabel();
        btnSendMessage = new javax.swing.JButton();
        labelFileSend = new javax.swing.JLabel();
        btnBrowseFile = new javax.swing.JButton();
        labelMessageReceive = new javax.swing.JLabel();
        spMessageSend = new javax.swing.JScrollPane();
        txtAreaMessageSend = new javax.swing.JTextArea();
        labelCryptMode = new javax.swing.JLabel();
        rbAES = new javax.swing.JRadioButton();
        rbRSA = new javax.swing.JRadioButton();
        chkShowEncryptMessage = new javax.swing.JCheckBox();
        btnKick = new javax.swing.JButton();
        spMessageReceive = new javax.swing.JScrollPane();
        txtPaneMessageReceive = new javax.swing.JTextPane();
        panelLog = new javax.swing.JPanel();
        labelLog = new javax.swing.JLabel();
        spLog = new javax.swing.JScrollPane();
        txtAreaLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("終端");
        setIconImage(icon);
        setName("MainGUI"); // NOI18N
        setResizable(false);

        labelKDCConnect.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelKDCConnect.setText("KDC 連線");

        labelKDCIP.setText("IP");

        txtKDCIP.setText("127.0.0.1");

        labelKDCPort.setText("端口");

        txtKDCPort.setText("8092");

        btnConnectKDC.setText("連線");
        btnConnectKDC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectKDCActionPerformed(evt);
            }
        });

        btnBrowsePublicKey.setText("瀏覽");
        btnBrowsePublicKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowsePublicKeyActionPerformed(evt);
            }
        });

        labelKey.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelKey.setText("金鑰設定");

        labelPublicKey.setText("公開金鑰");

        labelPrivateKey.setText("秘密金鑰");

        btnBrowsePrivateKey.setText("瀏覽");
        btnBrowsePrivateKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowsePrivateKeyActionPerformed(evt);
            }
        });

        labelAccount.setText("帳號");

        labelPassword.setText("密碼");

        btnKDCRegister.setText("註冊");
        btnKDCRegister.setEnabled(false);
        btnKDCRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKDCRegisterActionPerformed(evt);
            }
        });

        btnUpdateKDCPublicKey.setText("上傳至 KDC");
        btnUpdateKDCPublicKey.setEnabled(false);
        btnUpdateKDCPublicKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateKDCPublicKeyActionPerformed(evt);
            }
        });

        labelKDCMember.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelKDCMember.setText("KDC 會員");

        btnKDCLogin.setText("登入");
        btnKDCLogin.setEnabled(false);
        btnKDCLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKDCLoginActionPerformed(evt);
            }
        });

        labelSkin.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelSkin.setText("外觀選擇");

        cbSkin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nimbus", "Motif", "Windows", "Windows Classic", "Acryl", "Aero", "Aluminium", "Bernstein", "Fast", "Graphite", "HiFi", "Luna", "McWin", "Mint", "Noire", "Smart", "Texture" }));
        cbSkin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSkinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingLayout = new javax.swing.GroupLayout(panelSetting);
        panelSetting.setLayout(panelSettingLayout);
        panelSettingLayout.setHorizontalGroup(
            panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingLayout.createSequentialGroup()
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingLayout.createSequentialGroup()
                        .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelKDCConnect)
                                    .addGroup(panelSettingLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelKDCPort)
                                            .addComponent(labelKDCIP))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtKDCIP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtKDCPort, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(btnConnectKDC, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(panelSettingLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelKDCMember)
                                    .addGroup(panelSettingLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelSettingLayout.createSequentialGroup()
                                                .addComponent(labelPassword)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelSettingLayout.createSequentialGroup()
                                                .addComponent(labelAccount)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelSettingLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(btnKDCRegister)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnKDCLogin)))))))
                        .addGap(127, 127, 127)
                        .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelKey)
                            .addGroup(panelSettingLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelSettingLayout.createSequentialGroup()
                                        .addComponent(labelPrivateKey)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBrowsePrivateKey))
                                    .addGroup(panelSettingLayout.createSequentialGroup()
                                        .addComponent(labelPublicKey)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBrowsePublicKey)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnUpdateKDCPublicKey))))))
                    .addGroup(panelSettingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(cbSkin, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelSkin))))
                .addContainerGap(270, Short.MAX_VALUE))
        );
        panelSettingLayout.setVerticalGroup(
            panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKDCConnect)
                    .addComponent(labelKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKDCIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelKDCIP)
                    .addComponent(labelPublicKey)
                    .addComponent(btnBrowsePublicKey)
                    .addComponent(btnUpdateKDCPublicKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKDCPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelKDCPort)
                    .addComponent(labelPrivateKey)
                    .addComponent(btnBrowsePrivateKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConnectKDC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(labelKDCMember)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAccount)
                    .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPassword)
                    .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKDCRegister)
                    .addComponent(btnKDCLogin))
                .addGap(78, 78, 78)
                .addComponent(labelSkin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );

        tpMain.addTab("基本參數設定", panelSetting);

        labelClient.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelClient.setText("通訊客戶端");

        labelConnectIP.setText("IP");

        txtConnectIP.setText("127.0.0.1");

        labelConnectPort.setText("端口");

        txtConnectPort.setText("8093");

        labelRemoteName.setText("遠端名稱");

        labelServer.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelServer.setText("通訊服務端");

        labelServerPort.setText("端口");

        txtServerPort.setText("8093");

        btnClientConnect.setText("連線");
        btnClientConnect.setEnabled(false);
        btnClientConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientConnectActionPerformed(evt);
            }
        });

        btnServerStart.setText("啟動服務端");
        btnServerStart.setEnabled(false);
        btnServerStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServerStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelConnectLayout = new javax.swing.GroupLayout(panelConnect);
        panelConnect.setLayout(panelConnectLayout);
        panelConnectLayout.setHorizontalGroup(
            panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConnectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnServerStart)
                    .addGroup(panelConnectLayout.createSequentialGroup()
                        .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelClient)
                            .addGroup(panelConnectLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnClientConnect)
                                    .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelConnectLayout.createSequentialGroup()
                                            .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(labelConnectIP)
                                                .addComponent(labelConnectPort))
                                            .addGap(30, 30, 30)
                                            .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtConnectPort, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtConnectIP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(panelConnectLayout.createSequentialGroup()
                                            .addComponent(labelRemoteName)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtRemoteName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(100, 100, 100)
                        .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelConnectLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(labelServerPort)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelServer))))
                .addContainerGap(298, Short.MAX_VALUE))
        );
        panelConnectLayout.setVerticalGroup(
            panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConnectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelClient)
                    .addComponent(labelServer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelConnectIP)
                    .addComponent(txtConnectIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelServerPort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelConnectPort)
                    .addComponent(txtConnectPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnServerStart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRemoteName)
                    .addComponent(txtRemoteName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClientConnect)
                .addContainerGap(403, Short.MAX_VALUE))
        );

        tpMain.addTab("連線設定", panelConnect);

        labelListUser.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelListUser.setText("連線用戶");

        listUser.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spListUser.setViewportView(listUser);

        labelMessageSend.setText("訊息傳送");

        btnSendMessage.setText("傳送");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });

        labelFileSend.setText("檔案傳送");

        btnBrowseFile.setText("瀏覽");
        btnBrowseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseFileActionPerformed(evt);
            }
        });

        labelMessageReceive.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        labelMessageReceive.setText("訊息接收");

        txtAreaMessageSend.setColumns(20);
        txtAreaMessageSend.setLineWrap(true);
        txtAreaMessageSend.setRows(5);
        txtAreaMessageSend.setWrapStyleWord(true);
        spMessageSend.setViewportView(txtAreaMessageSend);

        labelCryptMode.setText("加密模式");

        bgCryptMode.add(rbAES);
        rbAES.setSelected(true);
        rbAES.setText("AES");

        bgCryptMode.add(rbRSA);
        rbRSA.setText("RSA");

        chkShowEncryptMessage.setText("顯示加密訊息");

        btnKick.setText("驅逐用戶");
        btnKick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKickActionPerformed(evt);
            }
        });

        txtPaneMessageReceive.setEditable(false);
        spMessageReceive.setViewportView(txtPaneMessageReceive);

        javax.swing.GroupLayout panelMessageLayout = new javax.swing.GroupLayout(panelMessage);
        panelMessage.setLayout(panelMessageLayout);
        panelMessageLayout.setHorizontalGroup(
            panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMessageLayout.createSequentialGroup()
                .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMessageLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMessageLayout.createSequentialGroup()
                                .addComponent(labelMessageSend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spMessageSend, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSendMessage))
                            .addGroup(panelMessageLayout.createSequentialGroup()
                                .addComponent(labelFileSend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowseFile)
                                .addGap(18, 18, 18)
                                .addComponent(labelCryptMode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbAES)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbRSA))))
                    .addGroup(panelMessageLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelListUser)
                            .addGroup(panelMessageLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spListUser, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                    .addComponent(btnKick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(24, 24, 24)
                        .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMessageLayout.createSequentialGroup()
                                .addComponent(labelMessageReceive)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 511, Short.MAX_VALUE)
                                .addComponent(chkShowEncryptMessage))
                            .addGroup(panelMessageLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(spMessageReceive)))))
                .addContainerGap())
        );
        panelMessageLayout.setVerticalGroup(
            panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMessageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelListUser)
                    .addComponent(labelMessageReceive)
                    .addComponent(chkShowEncryptMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMessageLayout.createSequentialGroup()
                        .addComponent(spListUser, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKick))
                    .addComponent(spMessageReceive, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(spMessageSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSendMessage))
                    .addComponent(labelMessageSend))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFileSend)
                    .addComponent(btnBrowseFile)
                    .addComponent(labelCryptMode)
                    .addComponent(rbAES)
                    .addComponent(rbRSA))
                .addGap(22, 22, 22))
        );

        tpMain.addTab("訊息傳輸", panelMessage);

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
                        .addComponent(spLog))
                    .addGroup(panelLogLayout.createSequentialGroup()
                        .addComponent(labelLog)
                        .addGap(0, 732, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLogLayout.setVerticalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelLog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spLog, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpMain.addTab("系統輸出", panelLog);

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

    private void btnBrowsePublicKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowsePublicKeyActionPerformed
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] keyByte = Files.readAllBytes(chooser.getSelectedFile().toPath());
                RSAPublicKey key = (RSAPublicKey) PublicKeyHandler.loadPublicKey(keyByte);
                if (key.getModulus().bitLength() != EndpointConstant.KEY_LENGTH) {
                    JOptionPane.showMessageDialog(this, "公開金鑰長度必須為 " + EndpointConstant.KEY_LENGTH + " 位元");
                    return;
                }
                KeyData.getInstance().setLocalPublicKey(keyByte, key);
                JOptionPane.showMessageDialog(this, "公開金鑰檔案載入完成");
                processConnectEnable();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "公開金鑰檔案讀取失敗", ex);
                JOptionPane.showMessageDialog(this, "公開金鑰檔案讀取失敗");
            }
        }
    }//GEN-LAST:event_btnBrowsePublicKeyActionPerformed

    private void processConnectEnable() {
        if (localAccount != null && KeyData.getInstance().getLocalPublicKey() != null && KeyData.getInstance().getLocalPrivateKey() != null) {
            btnUpdateKDCPublicKey.setEnabled(true);
            btnClientConnect.setEnabled(true);
            btnServerStart.setEnabled(true);
        }
    }

    public void appendImageMessage(String file) {
        try {
            ImageIcon image = new ImageIcon(file);
            if (image.getIconWidth() <= EndpointConstant.IMAGE_RESIZE_WIDTH && image.getIconHeight() <= EndpointConstant.IMAGE_RESIZE_HEIGHT) {
                txtPaneMessageReceive.insertIcon(image);
            } else {
                ImageIcon resize = ImageUtil.resizeImage(image, new Dimension(EndpointConstant.IMAGE_RESIZE_WIDTH, EndpointConstant.IMAGE_RESIZE_HEIGHT));
                txtPaneMessageReceive.insertIcon(resize);
            }
            Document doc = txtPaneMessageReceive.getDocument();
            doc.insertString(doc.getLength(), System.getProperty("line.separator"), null);
            txtPaneMessageReceive.setCaretPosition(doc.getLength());
        } catch (Exception ex) {
        }
    }

    private void btnUpdateKDCPublicKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateKDCPublicKeyActionPerformed
        if (KeyData.getInstance().getLocalPublicKey().getModulus().bitLength() != EndpointConstant.KEY_LENGTH) {
            JOptionPane.showMessageDialog(this, "公開金鑰長度必須為 " + EndpointConstant.KEY_LENGTH + " 位元");
            return;
        }
        kdcSession.getSession().write(PacketCreator.updatePublicKey(KeyData.getInstance().getLocalPublicKeyByte()));
    }//GEN-LAST:event_btnUpdateKDCPublicKeyActionPerformed

    private void btnBrowsePrivateKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowsePrivateKeyActionPerformed
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] keyByte = Files.readAllBytes(chooser.getSelectedFile().toPath());
                RSAPrivateKey privateKey = (RSAPrivateKey) PublicKeyHandler.loadPrivateKey(keyByte);
                if (privateKey.getModulus().bitLength() != EndpointConstant.KEY_LENGTH) {
                    JOptionPane.showMessageDialog(this, "秘密金鑰長度必須為 " + EndpointConstant.KEY_LENGTH + " 位元");
                    return;
                }
                KeyData.getInstance().setLocalPrivateKey(privateKey);
                JOptionPane.showMessageDialog(this, "秘密金鑰檔案載入完成");
                processConnectEnable();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "秘密金鑰檔案讀取失敗", ex);
                JOptionPane.showMessageDialog(this, "秘密金鑰檔案讀取失敗");
            }
        }
    }//GEN-LAST:event_btnBrowsePrivateKeyActionPerformed

    private void btnConnectKDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectKDCActionPerformed
        String host = txtKDCIP.getText();
        if (isNullOrBlank(host)) {
            JOptionPane.showMessageDialog(this, "請輸入連線主機");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(txtKDCPort.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "端口輸入數值錯誤");
            return;
        }
        if (port <= 0 || port > 65535) {
            JOptionPane.showMessageDialog(this, "端口輸入範圍錯誤");
            return;
        }

        enableKDCConnectControl(false);
        new Thread(() -> {
            try {
                kdcSession = new KDCSession(host, port);
                kdcSession.start();
                enableMemberControl(true);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "無法與 KDC 伺服器建立連線", ex);
                JOptionPane.showMessageDialog(this, "無法與 KDC 伺服器建立連線");
                enableKDCConnectControl(true);
            }
        }).start();
    }//GEN-LAST:event_btnConnectKDCActionPerformed

    private void btnKDCRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKDCRegisterActionPerformed
        String account = txtAccount.getText();
        String password = pfPassword.getText();
        if (isNullOrBlank(account) || isNullOrBlank(password)) {
            JOptionPane.showMessageDialog(this, "請輸入帳戶資訊");
            return;
        }
        if (account.length() > EndpointConstant.MAX_ACCOUNT_LENGTH || password.length() > EndpointConstant.MAX_ACCOUNT_LENGTH) {
            JOptionPane.showMessageDialog(this, "帳號或密碼長度不得超過 " + EndpointConstant.MAX_ACCOUNT_LENGTH + " 字元");
            return;
        }
        kdcSession.getSession().write(PacketCreator.register(account, password));
    }//GEN-LAST:event_btnKDCRegisterActionPerformed

    private void btnKDCLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKDCLoginActionPerformed
        String account = txtAccount.getText();
        String password = pfPassword.getText();
        if (isNullOrBlank(account) || isNullOrBlank(password)) {
            JOptionPane.showMessageDialog(this, "請輸入帳戶資訊");
            return;
        }
        if (account.length() > EndpointConstant.MAX_ACCOUNT_LENGTH || password.length() > EndpointConstant.MAX_ACCOUNT_LENGTH) {
            JOptionPane.showMessageDialog(this, "帳號或密碼長度不得超過 " + EndpointConstant.MAX_ACCOUNT_LENGTH + " 字元");
            return;
        }
        enableMemberControl(false);
        kdcSession.getSession().write(PacketCreator.login(account, password));
    }//GEN-LAST:event_btnKDCLoginActionPerformed

    private void btnClientConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientConnectActionPerformed
        String host = txtConnectIP.getText();
        String remoteName = txtRemoteName.getText();
        if (isNullOrBlank(host) || isNullOrBlank(remoteName)) {
            JOptionPane.showMessageDialog(this, "請輸入連線資訊");
            return;
        }
        if (remoteName.length() > EndpointConstant.MAX_ACCOUNT_LENGTH) {
            JOptionPane.showMessageDialog(this, "遠端名稱過長");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(txtConnectPort.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "端口輸入數值錯誤");
            return;
        }
        if (port <= 0 || port > 65535) {
            JOptionPane.showMessageDialog(this, "端口輸入範圍錯誤");
            return;
        }

        RemoteUser user = userStorage.getUserByName(remoteName);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "已與該使用者建立連線");
            return;
        }

        new Thread(() -> {
            CommunicateClient client = new CommunicateClient(host, port, remoteName);
            client.start();
            logger.log(Level.INFO, "客戶端連線已建立 目標使用者 : {0}", remoteName);
        }).start();
    }//GEN-LAST:event_btnClientConnectActionPerformed

    private void enableServerControl(boolean enable) {
        txtServerPort.setEnabled(enable);
        btnServerStart.setEnabled(enable);
    }

    private void btnServerStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServerStartActionPerformed
        int port;
        try {
            port = Integer.parseInt(txtServerPort.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "目標端口輸入數值錯誤");
            return;
        }
        if (port <= 0 || port > 65535) {
            JOptionPane.showMessageDialog(this, "目標端口輸入範圍錯誤");
            return;
        }

        enableServerControl(false);
        new Thread(() -> {
            try {
                server = new CommunicateServer(port);
                server.start();
                logger.log(Level.INFO, "服務端啟動完成");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "服務端初始化失敗", ex);
                JOptionPane.showMessageDialog(this, "服務端初始化失敗");

                server.stop();
                server = null;
                enableServerControl(true);
            }
        }).start();
    }//GEN-LAST:event_btnServerStartActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        String user = listUser.getSelectedValue();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "請選擇使用者");
            return;
        }

        String message = txtAreaMessageSend.getText();
        if (isNullOrBlank(message)) {
            JOptionPane.showMessageDialog(this, "請輸入傳送訊息");
            return;
        }
        if (message.length() > EndpointConstant.MAX_MESSAGE_LENGTH) {
            JOptionPane.showMessageDialog(this, "最大訊息長度不得大於 " + EndpointConstant.MAX_MESSAGE_LENGTH + " 字元");
            return;
        }

        RemoteUser target = userStorage.getUserByName(user);
        CryptMode mode = rbAES.isSelected() ? CryptMode.AES_CBC : CryptMode.RSA;
        try {
            target.getSession().write(PacketCreator.message(mode, target, message));
            messageLogger.log(Level.INFO, "{0} -> {1} : {2}", new Object[]{
                localAccount, user, message
            });
            txtAreaMessageSend.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "發送訊息時發生例外狀況 : " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void btnBrowseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseFileActionPerformed
        String user = listUser.getSelectedValue();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "請選擇使用者");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String fileName = chooser.getSelectedFile().getName();
                byte[] data = Files.readAllBytes(chooser.getSelectedFile().toPath());
                if (data.length > EndpointConstant.MAX_FILE_LENGTH) {
                    JOptionPane.showMessageDialog(this, "傳輸檔案太大");
                    return;
                }

                RemoteUser target = userStorage.getUserByName(user);
                CryptMode mode = rbAES.isSelected() ? CryptMode.AES_CBC : CryptMode.RSA;
                try {
                    target.getSession().write(PacketCreator.file(mode, target, fileName, data));
                    messageLogger.log(Level.INFO, "{0} -> {1} : 檔案傳送 {2}", new Object[]{
                        localAccount, user, fileName
                    });
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "發送檔案時發生例外狀況 : " + ex.getMessage());
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, "檔案讀取失敗", ex);
                JOptionPane.showMessageDialog(this, "檔案讀取失敗");
            }
        }
    }//GEN-LAST:event_btnBrowseFileActionPerformed

    private void btnKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKickActionPerformed
        String user = listUser.getSelectedValue();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "請選擇使用者");
            return;
        }
        RemoteUser target = userStorage.getUserByName(user);
        try {
            target.getSession().close();
            logger.log(Level.INFO, "已驅逐指定用戶 : {0}", user);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnKickActionPerformed

    private void cbSkinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSkinActionPerformed
        try {
            UIManager.setLookAndFeel(THEME[cbSkin.getSelectedIndex()]);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
    }//GEN-LAST:event_cbSkinActionPerformed

    private static void initLookAndFeel() {
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
    private javax.swing.ButtonGroup bgCryptMode;
    private javax.swing.JButton btnBrowseFile;
    private javax.swing.JButton btnBrowsePrivateKey;
    private javax.swing.JButton btnBrowsePublicKey;
    private javax.swing.JButton btnClientConnect;
    private javax.swing.JButton btnConnectKDC;
    private javax.swing.JButton btnKDCLogin;
    private javax.swing.JButton btnKDCRegister;
    private javax.swing.JButton btnKick;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JButton btnServerStart;
    private javax.swing.JButton btnUpdateKDCPublicKey;
    private javax.swing.JComboBox<String> cbSkin;
    private javax.swing.JCheckBox chkShowEncryptMessage;
    private javax.swing.JLabel labelAccount;
    private javax.swing.JLabel labelClient;
    private javax.swing.JLabel labelConnectIP;
    private javax.swing.JLabel labelConnectPort;
    private javax.swing.JLabel labelCryptMode;
    private javax.swing.JLabel labelFileSend;
    private javax.swing.JLabel labelKDCConnect;
    private javax.swing.JLabel labelKDCIP;
    private javax.swing.JLabel labelKDCMember;
    private javax.swing.JLabel labelKDCPort;
    private javax.swing.JLabel labelKey;
    private javax.swing.JLabel labelListUser;
    private javax.swing.JLabel labelLog;
    private javax.swing.JLabel labelMessageReceive;
    private javax.swing.JLabel labelMessageSend;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelPrivateKey;
    private javax.swing.JLabel labelPublicKey;
    private javax.swing.JLabel labelRemoteName;
    private javax.swing.JLabel labelServer;
    private javax.swing.JLabel labelServerPort;
    private javax.swing.JLabel labelSkin;
    private javax.swing.JList<String> listUser;
    private javax.swing.JPanel panelConnect;
    private javax.swing.JPanel panelLog;
    private javax.swing.JPanel panelMessage;
    private javax.swing.JPanel panelSetting;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JRadioButton rbAES;
    private javax.swing.JRadioButton rbRSA;
    private javax.swing.JScrollPane spListUser;
    private javax.swing.JScrollPane spLog;
    private javax.swing.JScrollPane spMessageReceive;
    private javax.swing.JScrollPane spMessageSend;
    private javax.swing.JTabbedPane tpMain;
    private javax.swing.JTextField txtAccount;
    private javax.swing.JTextArea txtAreaLog;
    private javax.swing.JTextArea txtAreaMessageSend;
    private javax.swing.JTextField txtConnectIP;
    private javax.swing.JTextField txtConnectPort;
    private javax.swing.JTextField txtKDCIP;
    private javax.swing.JTextField txtKDCPort;
    private javax.swing.JTextPane txtPaneMessageReceive;
    private javax.swing.JTextField txtRemoteName;
    private javax.swing.JTextField txtServerPort;
    // End of variables declaration//GEN-END:variables
}
