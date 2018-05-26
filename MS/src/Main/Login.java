package Main;

import SQL.*;
import operate.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * 登陆界面 判断管理员,用户 对应权限
 */
public class Login extends JFrame implements ActionListener, ItemListener {

    JLabel userName = new JLabel("用户：");
    JTextField txtUser = new JTextField();
    JLabel passWord = new JLabel("密码：");
    JPasswordField txtPwd = new JPasswordField(6);
    JLabel role = new JLabel("角色：");
    JComboBox cbrole = new JComboBox();
    JButton btnLogin = new JButton("登录");
    JButton btnCz = new JButton("重置");
    JButton btnCancel = new JButton("取消");

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    public int UesrPermissions = 0; // 判断是管理员or学生

    public Login() {
        super("登录界面");
        cbrole.addItem("管理员");
        cbrole.addItem("学生");
        this.setLayout(null);
        this.setBounds(100, 100, 400, 550);
        this.add(userName);
        userName.setBounds(100, 30, 70, 50);
        userName.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(txtUser);
        txtUser.setBounds(200, 42, 100, 30);
        this.add(passWord);
        passWord.setBounds(100, 90, 70, 50);
        passWord.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(txtPwd);
        txtPwd.setBounds(200, 102, 100, 30);
        this.add(role);
        role.setBounds(100, 150, 70, 50);
        role.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(cbrole);
        cbrole.setBounds(200, 159, 101, 30);
        this.add(btnLogin);
        btnLogin.setBounds(80, 250, 60, 40);
        this.add(btnCz);
        btnCz.setBounds(170, 250, 60, 40);
        this.add(btnCancel);
        btnCancel.setBounds(264, 250, 60, 40);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        btnLogin.addActionListener(this);
        cbrole.addItemListener(this);
        btnCz.addActionListener(this);
        btnCancel.addActionListener(this);

    }

    // 获取用户选择的角色
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox jcb = (JComboBox) e.getSource();
            UesrPermissions = jcb.getSelectedIndex();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String un = null;
        String pw = null;
        if (source == btnLogin) {
            String password = new String(txtPwd.getPassword());// 密码框getText已过时,调用getPassword
            if (txtUser.getText().equals("") || password.equals("")) {// 判断是否输入了用户名和密码
                JOptionPane.showMessageDialog(null, "登录名和密码不能为空！");
            } else {
                try {
                    con = SqlCoon.getConection(); // 连接数据库
                    stmt = con.createStatement(); // 创建一个Statement语句对象
                    rs = stmt.executeQuery("select * from users where user_authority ='" + UesrPermissions + "'");
                    while (rs.next()) {
                        un = rs.getString("user_name").trim();
                        // 去除Text首尾空格，对getPassword()无效
                        pw = rs.getString("user_pwd").trim();
                        if (txtUser.getText().equals(un)) {
                            if (password.equals(pw)) {
                                this.setVisible(false);
                                if (UesrPermissions == 0) {
                                    MKey.OnlyStuId = txtUser.getText();
                                    new AdminInterface();// 进入管理员界面
                                }
                                if (UesrPermissions == 1) {
                                    MKey.OnlyStuId = txtUser.getText();
                                    System.out.println("in");
                                    new StuManage(null, "");// 进入学生界面
                                }
                                MKey.IfSuccess = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "密码错误！");
                                txtPwd.setText("");
                            }
                        }
                    }
                    if (!MKey.IfSuccess) {
                        JOptionPane.showMessageDialog(null, "登录名错误！");
                        txtUser.setText("");
                        txtPwd.setText("");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            // 点击重置
        } else if (source == btnCz) {
            txtUser.setText("");
            txtPwd.setText("");
            // 退出
        } else if (source == btnCancel) {
            System.exit(0);
        }
    }

}