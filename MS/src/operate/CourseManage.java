package operate;

import java.awt.AWTEvent;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

import SQL.*;
import Main.*;

/**
 * 课程信息管理 课程表没有添加增删改查方法 如若需要只需要添加方法到这个类(添加方法与StuManage跟GradeManage一样)
 * 在StuManage调用就OK
 */
public class CourseManage extends JFrame implements ActionListener {

    JButton btnLook = new JButton("查看");
    JButton btnDisplay = new JButton("显示");
    JButton btnClose;
    JMenuBar mb = new JMenuBar();
    JTable sTable;
    JScrollPane scroll;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Object[][] playerInfo;
    Log log = new Log();
    public CourseManage(String str) {

        super("教师任课表");
        mb.add(btnLook);
        btnClose = new JButton("返回");
        mb.add(btnClose);
        con = SqlCoon.getConection(); // 连接数据库
        try {
            stmt = con.createStatement();// 创建一个Statement语句对象
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        this.setBounds(100, 100, 750, 450);
        btnClose.addActionListener(this);
        btnLook.addActionListener(this);
        this.setJMenuBar(mb);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // 激活窗口事件
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    // 重写关闭方法
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if(MKey.CloseCourse){
                this.dispose();
                new AdminInterface();
            }else
                this.dispose();
        }
    }

    public CourseManage(String str, String str1) {

        super("教师任课表");
        mb.add(btnDisplay);
        btnClose = new JButton("退出");
        mb.add(btnClose);
        con = SqlCoon.getConection(); // 连接数据库
        try {
            stmt = con.createStatement();// 创建一个Statement语句对象
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        this.setBounds(100, 100, 750, 450);
        btnClose.addActionListener(this);
        btnDisplay.addActionListener(this);
        this.setJMenuBar(mb);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // 激活窗口事件
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    // 显示所有课程信息
    public void display() {
        int i = 0;
        int j = 0;
        List<String> al = new ArrayList<String>();
        try {
            rs = stmt.executeQuery("select * from show_course order by course_id");
            String temp ="IN DISPLAY():  " + "select * from show_course order by course_id";
            log.addLog(temp);
            while (rs.next()) {// 找出表中的记录数赋给I
                al.add(rs.getString("course_id"));
                al.add(rs.getString("course_name"));
                al.add(rs.getString("course_credit"));
                al.add(rs.getString("course_dep"));
                al.add(rs.getString("tea_name"));
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        playerInfo = new Object[i][5];
        String[] columnNames = { "课程号", "课程名", "学分", "开设院系", "任课老师" };
        try {
            rs = stmt.executeQuery("select * from show_course order by course_id");
            String temp ="IN PISPLAY():  " + "select * from show_course order by course_id";
            log.addLog(temp);
            while (rs.next()) {
                playerInfo[j][0] = rs.getString("course_id").trim();
                playerInfo[j][1] = rs.getString("course_name").trim();
                playerInfo[j][2] = rs.getString("course_credit").trim();
                playerInfo[j][3] = rs.getString("course_dep").trim();
                playerInfo[j][4] = rs.getString("tea_name").trim();
                j++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        sTable = new JTable(playerInfo, columnNames);// 创建网络
        this.add(sTable);
        scroll = new JScrollPane(sTable);
        this.add(scroll);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLook) {
            this.dispose();
            new CourseManage("教师任课表").display();
        }
        if (e.getSource() == btnDisplay) {
            this.dispose();
            new CourseManage("教师任课表", "").display();
        }
        if (e.getActionCommand() == "返回") {
            this.dispose();
            new AdminInterface();
        }
        if (e.getActionCommand() == "退出") {
            this.dispose();
            new StuManage(null, "学生个人信息查询");
        }
    }
}
