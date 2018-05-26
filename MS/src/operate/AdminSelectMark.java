package operate;

import java.awt.AWTEvent;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

import SQL.*;
import Main.*;

/**
 * 查询时输入学号的界面----学生成绩信息管理
 */
public class AdminSelectMark extends JFrame implements ActionListener {

    JLabel ltitle = new JLabel("学号：");
    JTextField tsno = new JTextField(8);
    JButton btnOK = new JButton("确定");
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Log log = new Log();

    public AdminSelectMark() {
        this.setLayout(null);
        this.add(ltitle);
        ltitle.setBounds(70, 90, 150, 30);
        ltitle.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(tsno);
        tsno.setBounds(70, 150, 150, 30);
        this.add(btnOK);
        btnOK.setBounds(70, 210, 70, 35);
        this.setBounds(100, 100, 400, 550);
        btnOK.addActionListener(this);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // 激活窗口事件
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    // 重写关闭方法
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
            new MarkManage("成绩信息管理");
        }
    }

    public void actionPerformed(ActionEvent e) {
        String un = null;
        boolean LoginSuccess = false;
        MKey.SelectGrade = tsno.getText();// 取得当前输入学号的值
        if (tsno.getText().equals("")) {// 判断是否输入了学号
            JOptionPane.showMessageDialog(null, "学号不能为空，请重新输入！");
        } else {
            try {
                con = SqlCoon.getConection(); // 连接数据库
                stmt = con.createStatement();// 创建一个Statement语句对象
                rs = stmt.executeQuery("select * from show_mark");
                String temp ="IN SelectMark():  " + "select * from show_mark";
                log.addLog(temp);
                while (rs.next()) {
                    un = rs.getString("stu_id").trim();
                    // 去除Text首尾空格，对getPassword()无效
                    if (tsno.getText().equals(un)) {
                        LoginSuccess = true;
                        this.setVisible(false);
                        this.dispose();
                        MKey.StuGrade = true;
                        MKey.ifCooikeGrade = true; // 设置可修改
                        new MarkManage("成绩信息管理").select();
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
            }
            if (!LoginSuccess) {
                JOptionPane.showMessageDialog(null, "学号不存在，请重新输入！");
            }
        }
    }
}