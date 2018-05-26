package operate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import javax.lang.model.element.NestingKind;
import javax.swing.*;

import SQL.*;
import Main.*;
/**
 * 学生信息管理 管理员与用户公用,相应功能区别
 */
public class StuManage extends JFrame implements ActionListener {
    private static final long serialVersionUID = -7095553224509340642L;

    JButton btnAdd = new JButton("增加");
    JButton btnDelete = new JButton("删除");
    JButton btnAlter = new JButton("修改");
    JButton btnSearch;
    JButton btnDisplay = new JButton("显示");
    JButton cj = new JButton("成绩");
    JButton kc = new JButton("课表");
    JMenuBar mb = new JMenuBar();
    JTable sTable;
    JScrollPane scroll;
    Log log = new Log();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Object[][] playerInfo;
    String stuSelect = null; // 获取是谁请求的查询,给出相应权限

    // 管理员所有
    public StuManage(String title) {
        super("学生信息管理系统");
        mb.add(btnAdd);
        mb.add(btnDelete);
        btnSearch = new JButton("查询");
        mb.add(btnSearch);
        mb.add(btnAlter);
        mb.add(btnDisplay);
        try {
            con = SqlCoon.getConection(); // 连接数据库
            stmt = con.createStatement();// 创建一个Statement语句对象
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        this.setBounds(100, 100, 750, 450);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnAlter.addActionListener(this);
        btnDisplay.addActionListener(this);
        btnSearch.addActionListener(this);
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
            if (MKey.CloseUser) {
                this.dispose();
                new AdminInterface();
            } else
                this.dispose();
        }
    }

    // 学生所有
    public StuManage(String string, String title) {
        super("学生个人信息查询");
        MKey.StuUser = true;
        btnSearch = new JButton("查看");
        mb.add(btnSearch);
        mb.add(kc);
        mb.add(cj);
        try {
            con = SqlCoon.getConection(); // 连接数据库
            stmt = con.createStatement();// 创建一个Statement语句对象
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        this.setBounds(100, 100, 750, 450);
        cj.addActionListener(this);
        btnSearch.addActionListener(this);
        kc.addActionListener(this);
        this.setJMenuBar(mb);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // 激活窗口事件
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    /**
     * 显示所有学生的基本信息
     */
    public void display() {
        int i = 0;
        int j = 0;
        ArrayList<String> al = new ArrayList<String>();
        try {
            rs = stmt.executeQuery("select * from show_stu");
            String temp ="IN DISPLAY():  " + "select * from show_stu" ;
            log.addLog(temp);
            // 找出表中的记录数赋给i
            while (rs.next()) {
                al.add(rs.getString("stu_id"));
                al.add(rs.getString("stu_name"));
                al.add(rs.getString("stu_sex"));
                al.add(rs.getString("dep_name"));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }
        playerInfo = new Object[i][4];
        String[] columnNames = { "学号", "姓名", "性别", "院系专业" };
        try {
            rs = stmt.executeQuery("select * from show_stu order by stu_id");
            String temp ="IN DISPLAY():  " + "select * from show_stu order by stu_id" ;
            log.addLog(temp);
            while (rs.next()) {
                playerInfo[j][0] = rs.getString("stu_id");
                playerInfo[j][1] = rs.getString("stu_name");
                playerInfo[j][2] = rs.getString("stu_sex").equals("1") ? "男" : "女";
                playerInfo[j][3] = rs.getString("dep_name");
                j++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }

        // 创建网格
        sTable = new JTable(playerInfo, columnNames);
        scroll = new JScrollPane(sTable);
        this.add(scroll);
        MKey.ifCooikeUser = true; // 设置可修改
    }

    /**
     * 删除某个学生的基本信息
     */
    @SuppressWarnings("unused")
    public void delete() {
        String xh = null;
        String xm = null;
        String nl = null;
        String xb = null;
        String yx = null;
        int row = -1;
        int option;
        MKey.ifCooikeGrade = true; // 设置可修改
        row = sTable.getSelectedRow();
        // 判断要删除的信息是否被选中
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "请选择要删除的记录！");
        } else {
            // 判断选择的是不是查询后的结果
            if (!MKey.StuUser) {
                int j1 = 0;
                try {
                    rs = stmt.executeQuery("select * from show_stu order by stu_id" );
                    String temp ="IN DELETE():  " + "select * from show_stu" ;
                    log.addLog(temp);
                    // 找出当前被选中的记录在数据库中的对应
                    while (rs.next() && j1 <= row) {
                        xh = rs.getString("stu_id");
                        xm = rs.getString("stu_name");
                        xb = rs.getString("stu_sex");
                        yx = rs.getString("dep_name");
                        j1++;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                xh = MKey.SelectUser;
            }
            try {
                // 删除数据库中当前被选中的记录
                stmt.execute("begin tran tran1");
                String temp ="IN DELETE():  " + "begin tran tran1" ;
                log.addLog(temp);
                int rs1 = stmt.executeUpdate("delete from Student where stu_id = '" + xh + "'");
                // 删除对应的用户表中的记录
                temp ="IN DELETE():  " + "delete from Student where stu_id = '" + xh + "'";
                log.addLog(temp);
                stmt.executeUpdate("delete from users where user_name = '" + xh + "'");
                temp ="IN DELETE():  " + "delete from users where user_name = '" + xh + "'" ;
                log.addLog(temp);
                option = JOptionPane.showConfirmDialog(null, "确认删除？？？");
                if(option == JOptionPane.YES_OPTION){
                    option = JOptionPane.showConfirmDialog(null, "确认删除？？？");
                    if(option == JOptionPane.YES_OPTION){
                        stmt.execute("commit tran tran1");
                        temp ="IN DISPLAY():  " + "commit tran tran1" ;
                        log.addLog(temp);
                        JOptionPane.showMessageDialog(null, "学生信息删除成功！");
                    }else{
                        stmt.execute("rollback tran tran1");
                        temp ="IN DISPLAY():  " + "rollback tran tran1" ;
                        log.addLog(temp);
                    }
                }else{
                    stmt.execute("rollback tran tran1");
                    temp ="IN DISPLAY():  " + "rollback tran tran1" ;
                    log.addLog(temp);
                }
                this.dispose();
                new StuManage("").display();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * 修改某个学生的基本信息
     */
    public void update() {
        String xh = null;
        String xm = null;
        String nl = null;
        String xb = null;
        String yx = null;
        int row = -1;
        row = sTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "请选择要修改的信息！");
        } else {
            int j1 = 0;
            try {
                if (!MKey.StuUser) {// 判断选择的是不是查询后的结果
                    rs = stmt.executeQuery("select * from show_stu order by stu_id");
                    String temp ="IN UPDATE():  " + "select * from show_stu order by stu_id" ;
                    log.addLog(temp);
                } else {
                    rs = stmt.executeQuery("select * from show_stu where stu_id = '"
                            + MKey.SelectUser + "'");
                    String temp ="IN UPDATE():  " + "select * from show_stu where stu_id = '" + MKey.SelectUser + "'" ;
                    log.addLog(temp);
                }
                while (rs.next() && j1 <= row) {// 找出当前被选中的记录在数据库中的对应
                    xh = rs.getString("stu_id");
                    xm = rs.getString("stu_name");
                    xb = rs.getString("stu_sex");
                    yx = rs.getString("dep_name");
                    j1++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
            }
            StuUpdate sadd = new StuUpdate(xb, yx);
            sadd.setTitle("修改");
            sadd.tsno.setText(xh);
            sadd.tsname.setText(xm);
            sadd.tsno.setEnabled(false);
            this.dispose();
            MKey.ifCooikeGrade = true; // 设置可修改
        }
    }

    /**
     * 显示某个查询的结果
     */
    public void select() {

        playerInfo = new Object[1][4];
        String[] columnNames = { "学号", "姓名", "性别", "院系专业" };
        try {
            con = SqlCoon.getConection(); // 连接数据库
            stmt = con.createStatement(); // 创建一个Statement语句对象
            rs = stmt.executeQuery("select * from show_stu where stu_id = '" + MKey.SelectUser + "'");
            String temp ="IN SELECT():  " + "select * from show_stu where stu_id = '" + MKey.SelectUser + "'" ;
            log.addLog(temp);
            while (rs.next()) {
                playerInfo[0][0] = rs.getString("stu_id");
                playerInfo[0][1] = rs.getString("stu_name");
                playerInfo[0][2] = rs.getString("stu_sex");
                playerInfo[0][3] = rs.getString("dep_name");
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == "增加") {
            new StuUpdate("男","CS");
            this.dispose();
        }
        if (e.getActionCommand() == "删除") {
            if (MKey.ifCooikeUser) {
                this.delete();
            } else {
                JOptionPane.showMessageDialog(null, "请先显示您要修改的信息！");
            }
        }
        if (e.getActionCommand() == "修改") {
            if (MKey.ifCooikeUser) {
                this.update();
            } else {
                JOptionPane.showMessageDialog(null, "请先显示您要修改的信息！");
            }
        }
        if (e.getActionCommand() == "显示") {
            this.dispose();
            MKey.StuUser = false;
            new StuManage("").display();
        }
        if (e.getActionCommand() == "查询") {
            this.dispose();
            new AdminSelectStu("请输入学生学号");
        }
        if (e.getActionCommand() == "查看") {
            this.dispose();
            new StuManage(null, "").select();
        }
        if (e.getActionCommand() == "课表") {
            new CourseManage("教师任课表", "返回学生");
            this.dispose();
        }
        if (e.getActionCommand() == "成绩") {
            new MarkManage(null, "学生个人成绩查询");
            this.dispose();
        }
    }
}
