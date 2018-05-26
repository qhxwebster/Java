package operate;

import java.awt.AWTEvent;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

import SQL.*;
import Main.*;

/**
 * 成绩信息管理 管理员与用户公用,相应功能区别
 */
public class MarkManage extends JFrame implements ActionListener {
    private static final long serialVersionUID = 5446543284394170291L;

    JButton btnAdder = new JButton("添加成绩");
    JButton btnAlter = new JButton("修改成绩");
    JButton btnSearch;

    JButton btnDisplay = new JButton("显示");
    JButton fanhui1 = new JButton("返回");
    JMenuBar mb = new JMenuBar();
    JTable sTable;
    JScrollPane scroll;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Object[][] playerInfo;
    String stuGrade = null; // 获取是谁请求的查询,给出相应权限
    Log log = new Log();

    public MarkManage(String title) {
        super(title);
        mb.add(btnAdder);
        mb.add(btnAlter);
        btnSearch = new JButton("查询");
        mb.add(btnSearch);
        mb.add(btnDisplay);
        con = SqlCoon.getConection(); // 连接数据库
        try {
            stmt = con.createStatement();// 创建一个Statement语句对象
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        this.setBounds(100, 100, 750, 450);
        btnAdder.addActionListener(this);
        btnAlter.addActionListener(this);
        btnSearch.addActionListener(this);
        btnDisplay.addActionListener(this);
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
            if(MKey.CloseGrade){
                this.dispose();
                new AdminInterface();
            }else
                this.dispose();
        }
    }

    public MarkManage(String string, String title) {// 构造方法
        super(title);
        btnSearch = new JButton("查看");
        mb.add(btnSearch);
        mb.add(fanhui1);
        con = SqlCoon.getConection(); // 连接数据库
        try {
            stmt = con.createStatement();// 创建一个Statement语句对象
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        this.setBounds(100, 100, 750, 450);
        btnSearch.addActionListener(this);
        fanhui1.addActionListener(this);
        this.setJMenuBar(mb);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    // 显示所有的成绩信息
    public void display() {
        int i = 0;
        int j = 0;
        List<String> al = new ArrayList<String>();
        try {
            rs = stmt.executeQuery("select * from show_mark");
            String temp ="IN DISPLAY():  " + "select * from show_mark";
            log.addLog(temp);
            while (rs.next()) {// 找出表中的记录数赋给i
                al.add(rs.getString("stu_id"));
                al.add(rs.getString("stu_name"));
                al.add(rs.getString("course_name"));
                al.add(rs.getString("mark"));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        playerInfo = new Object[i][4];
        String[] columnNames = { "学号","姓名", "课程名", "成绩" };
        try {
            rs = stmt.executeQuery("select * from show_mark order by course_name");
            String temp ="IN DISPLAY():  " + "select * from show_mark order by course_name";
            log.addLog(temp);
            while (rs.next()) {
                playerInfo[j][0] = rs.getString("stu_id");
                playerInfo[j][1] = rs.getString("stu_name");
                playerInfo[j][2] = rs.getString("course_name");
                playerInfo[j][3] = rs.getString("mark");
                j++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        sTable = new JTable(playerInfo, columnNames);// 创建网格;
        scroll = new JScrollPane(sTable);
        this.add(scroll);
        MKey.ifCooikeGrade = true; // 设置可修改
    }

    public void insert(){

    }

    // 修改某个学生的成绩信息
    public void update() {
        String xh = null;
        String xm = null;
        String km = null;
        String mark = null;
        int row = -1;
        row = sTable.getSelectedRow();

        if (row == -1) {// 判断要修改的信息是否被选中
            JOptionPane.showMessageDialog(null, "请选择要修改的成绩！");
        } else {
            int j1 = 0;
            try {
                if (!MKey.StuGrade) {// 判断选择的是不是查询后的结果
                    rs = stmt.executeQuery("select * from show_mark order by course_name");
                    String temp ="IN UPDATE():  " + "select * from show_mark order by course_name";
                    log.addLog(temp);
                } else {
                    rs = stmt.executeQuery("select * from show_mark where stu_id ='" + MKey.SelectUser + "'");
                    String temp ="IN UPDATE():  " + "select * from show_mark where stu_id ='" + MKey.SelectUser + "'";
                    log.addLog(temp);
                }
                while (rs.next() && j1 <= row) {
                    xh = rs.getString("stu_id");
                    xm = rs.getString("stu_name");
                    km = rs.getString("course_name");
                    mark = rs.getString("mark");
                    j1++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
            }
            MarkUpdate gadd = new MarkUpdate("修改成绩");
            gadd.tSid.setText(xh);
            gadd.tCname.setText(km);
            gadd.tMark.setText(mark);
            gadd.tSid.setEnabled(false);
            gadd.tCname.setEnabled(false);
            this.dispose();
            MKey.ifCooikeGrade = true; // 设置可修改
        }
    }

    // 显示某个学生的成绩查询结果
    public void select() {
        int j = 0;
        int i = 0;
        List<String> al = new ArrayList<String>();
        if(MKey.StuUser){
            MKey.SelectUser = MKey.OnlyStuId;
        }
        try {
            rs = stmt.executeQuery("select * from show_mark where stu_id ='" + MKey.SelectUser + "'");
            String temp ="IN SELECT():  " + "select * from show_mark where stu_id ='" + MKey.SelectUser + "'";
            log.addLog(temp);
            while (rs.next()) {
                al.add(rs.getString("stu_id"));
                al.add(rs.getString("stu_name"));
                al.add(rs.getString("course_name"));
                al.add(rs.getString("mark"));
                i++;// 把符合条件的记录数赋给i
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }

        playerInfo = new Object[i][4];
        String[] columnNames = { "学号", "课程名", "成绩" };
        try {
            rs = stmt.executeQuery("select * from show_mark where stu_id ='" + MKey.SelectUser + "'");
            String temp ="IN SELECT():  " + "select * from show_mark where stu_id ='" + MKey.SelectUser + "'";
            log.addLog(temp);
            while (rs.next()) {
                playerInfo[j][0] = rs.getString("stu_id");
                playerInfo[j][1] = rs.getString("stu_name");
                playerInfo[j][2] = rs.getString("course_name");
                playerInfo[j][3] = rs.getString("mark");
                j++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }

        sTable = new JTable(playerInfo, columnNames);
        scroll = new JScrollPane(sTable);
        this.add(scroll);
        MKey.ifCooikeGrade = true; // 设置可修改
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "添加成绩"){
            new MarkUpdate("添加成绩");
            this.insert();
            this.dispose();
        }
        if (e.getActionCommand() == "修改成绩") {
            if (MKey.ifCooikeGrade) {
                this.update();
            } else {
                JOptionPane.showMessageDialog(null, "请先显示您要修改的成绩！");
            }
        }
        if (e.getActionCommand() == "查询") {
            this.dispose();
            new AdminSelectMark();
        }
        if (e.getActionCommand() == "查看") {
            this.dispose();
            new MarkManage(null, "学生个人成绩查询").select();
        }
        if (e.getActionCommand() == "显示") {
            this.dispose();
            MKey.StuGrade = false;
            new MarkManage("成绩管理系统").display();
        }
        if (e.getActionCommand() == "返回") {
            this.dispose();
            MKey.ifCooikeUser = false;
            MKey.ifCooikeGrade = false; // 设置不可修改
            new StuManage(null, "");
        }
    }
}
