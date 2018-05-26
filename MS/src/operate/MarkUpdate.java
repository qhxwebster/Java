package operate;
import javax.swing.*;

import SQL.*;
import Main.*;

import java.awt.AWTEvent;
import java.awt.event.*;
import java.sql.*;
import java.util.Scanner;

/**
 * 用于成绩信息管理中增加或修改某条记录的界面
 */
public class MarkUpdate extends JFrame implements ActionListener {
    private static final long serialVersionUID = -9086107424245858057L;

    public JLabel lSid = new JLabel("学  号：");
    public JLabel lCname = new JLabel("课程名：");
    public JLabel lMark = new JLabel("成  绩：");
    public JTextField tSid = new JTextField(10);
    public JTextField tCname = new JTextField(10);
    public JTextField tMark = new JTextField(10);
    JButton btnOK = new JButton("确定");
    JButton btnCancel = new JButton("取消");
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Log log = new Log();

    public MarkUpdate(String title) {
        this.setTitle(title);
        this.setBounds(100, 100, 350, 500);
        this.setLayout(null);
        this.add(lSid);
        lSid.setBounds(50, 50, 80, 40);
        lSid.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(tSid);
        tSid.setBounds(150, 57, 120, 30);
        this.add(lCname);
        lCname.setBounds(50, 110, 80, 40);
        lCname.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(tCname);
        tCname.setBounds(150, 115, 120, 30);
        this.add(lMark);
        lMark.setBounds(50, 166, 80, 40);
        lMark.setFont(new java.awt.Font("宋体", 0, 18));
        this.add(tMark);
        tMark.setBounds(150, 173, 120, 30);
        this.add(btnOK);
        btnOK.setBounds(70, 260, 60, 31);
        this.add(btnCancel);
        btnCancel.setBounds(190, 260, 60, 31);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        this.setVisible(true);

        // 激活窗口事件
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    // 重写关闭方法
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            JOptionPane.showMessageDialog(null, "请点击取消");
            return; // 直接返回，阻止默认动作，阻止窗口关闭
        } else
            super.processWindowEvent(e); // 该语句会执行窗口事件的默认动作(如：隐藏)
    }

    // 修改成绩
    public void insertst() {
        String xh = null;
        String km = null;
        String cid = null;
        String mark = null;
        xh = tSid.getText().trim();
        km = tCname.getText().trim();
        mark = tMark.getText().trim();
        try {
            con = SqlCoon.getConection(); // 连接数据库
            stmt = con.createStatement();// 创建一个Statement语句对象
            rs = stmt.executeQuery("select course_id from Course where course_name ='" + km + "'");
            String temp ="IN INSERTST():  " + "select course_id from Course where course_name ='" + km + "'";
            log.addLog(temp);
            while (rs.next()){
                cid = rs.getString("course_id");
                break;
            }
            if(this.getTitle() == "修改成绩"){
                stmt.executeUpdate("update Selects set Mark ='" + mark + "' where stu_id ='" + xh + "' AND course_id ='" + cid + "'");
                temp = "IN INSERTST():  " + "update Selects set Mark ='" + mark + "' where stu_id ='" + xh + "' AND course_id ='" + cid + "'";
                log.addLog(temp);
            }else if(this.getTitle() == "添加成绩"){
                temp = "IN INSERTST():  " + "insert into Selects values('" + cid.trim() + "', '" + xh + "', " + mark + ")";
                System.out.println(temp);
                stmt.execute("insert into Selects values('" + cid.trim() + "', '" + xh+ "', " + mark + ")");


                log.addLog(temp);
            }
            JOptionPane.showMessageDialog(null, this.getTitle() + "成功！",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "确定") {
            this.insertst();
            this.dispose();
            MKey.StuGrade = false;
            new MarkManage("成绩信息管理").display();
        }
        if (e.getActionCommand() == "取消") {
            this.dispose();
            new MarkManage("成绩信息管理").display();
        }
    }

}