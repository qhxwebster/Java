package operate;

import javax.swing.*;

import SQL.*;
import Main.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * 用于学生信息管理中增加或修改某条记录的界面
 */
public class StuUpdate extends JFrame implements ActionListener, ItemListener {
    private static final long serialVersionUID = -4282201188867153635L;

    JLabel lsno = new JLabel("学号：");
    JLabel lsname = new JLabel("姓名：");
    JLabel lssex = new JLabel("性别：");
    JLabel lsdept = new JLabel("院系：");
    JTextField tsno = new JTextField(14);
    JTextField tsname = new JTextField(14);
    JComboBox cbssex = new JComboBox();
    JComboBox cbsdept = new JComboBox();
    JButton btnOK = new JButton(" 确 定 ");
    JButton btnCancel = new JButton(" 取 消 ");
    JPanel p = new JPanel();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    String[] depnames = {"CS", "IT", "SE", "IS"};
    String[] depids = {"001", "003", "004","002" };
    String xb = "男";
    String yx = depnames[0];
    Log log = new Log();
    public StuUpdate(String xb, String yx) {
        this.xb = xb;
        this.yx = yx;
        this.setTitle("增加");
        this.setBounds(100, 100, 350, 500);
        if (xb.trim().equals("男")) {
            cbssex.addItem("男");
            cbssex.addItem("女");
        } else {
            cbssex.addItem("女");
            cbssex.addItem("男");
        }

        for(int i=0;i<depnames.length;i++){
            cbsdept.addItem(depnames[i]);
        }

        p.setLayout(null);
        p.add(lsno);
        lsno.setBounds(55, 50, 80, 30);
        p.add(tsno);
        tsno.setBounds(130, 52, 150, 25);
        p.add(lsname);
        lsname.setBounds(55, 90, 80, 30);
        p.add(tsname);
        tsname.setBounds(130, 95, 150, 25);
        p.add(lssex);
        lssex.setBounds(55, 170, 80, 30);
        p.add(cbssex);
        cbssex.setBounds(130, 173, 60, 25);
        p.add(lsdept);
        lsdept.setBounds(55, 210, 80, 30);
        p.add(cbsdept);
        cbsdept.setBounds(130, 213, 80, 25);
        p.add(btnOK);
        btnOK.setBounds(70, 300, 70, 30);
        p.add(btnCancel);
        btnCancel.setBounds(190, 300, 70, 30);
        this.add(p);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cbssex.addItemListener(this);
        cbsdept.addItemListener(this);
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
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

    @SuppressWarnings("unused")
    public void insertst() throws SQLException { // 插入记录
        String xh = null;
        String xm = null;
        String depid = "001";
        xh = tsno.getText();
        xm = tsname.getText();
        xb = xb.equals("男")? "1":"0";
        if (this.getTitle() == "修改") {
            con = SqlCoon.getConection(); // 连接数据库
            stmt = con.createStatement(); // 创建一个Statement语句对象
            int rs1 = stmt.executeUpdate("delete from Student where stu_id ='" + xh + "'");
            String temp ="IN INSERTST():  " + "delete from Student where stu_id ='" + xh + "'" ;
            log.addLog(temp);
        }


        con = SqlCoon.getConection(); // 连接数据库
        stmt = con.createStatement();
        String str = "";
        String temp1 = "";
        String temp2 = "";
        str = "select * from show_dep";
        rs = stmt.executeQuery(str);
        String temp ="IN INSERTST():  " + "select * from show_dep" ;
        log.addLog(temp);
        while(rs.next()){
            temp1 = rs.getString("dep_id").trim();
            temp2 = rs.getString("dep_name").trim();
            if(temp2.equals(yx)){
                depid = temp1;
            }
            rs.getString("dep_intro");
        }
        try {
            str = " INSERT INTO Student values ('" + xh
                    + "','" + depid + "','" + xm + "','" + xb  + "', null)";
            stmt.executeUpdate(str);
            temp ="IN INSERTST():  " + " INSERT INTO Student values ('" + xh + "','" + depid + "','" + xm + "','" + xb  + "', null)" ;
            log.addLog(temp);
            JOptionPane.showMessageDialog(null, this.getTitle() + "成功！", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
            tsno.setText("");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == " 确 定 ") {
            try {
                this.insertst();
                this.dispose();
                MKey.StuUser = false;
                new StuManage("").display();
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, e1.toString(), "SQLException", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getActionCommand() == " 取 消 ") {
            this.dispose();
            this.setVisible(false);
            new StuManage("").display();
        }
    }

    // 下拉框的监听
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox jcb = (JComboBox) e.getSource();
            if ((jcb.getSelectedItem() == "男")
                    || (jcb.getSelectedItem() == "女")) {
                xb = (String) jcb.getSelectedItem();
            } else {
                yx = (String) jcb.getSelectedItem();
            }
        }
    }

}