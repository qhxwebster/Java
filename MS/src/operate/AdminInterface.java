package operate;

import java.awt.event.*;
import javax.swing.*;

import Main.*;
import SQL.*;

/**
 * 管理员选择相应操作界面
 */
public class AdminInterface extends JFrame implements ActionListener {

    private static final long serialVersionUID = -3710517705508093806L;
    JButton btnStuManage = new JButton("学生信息管理");
    JButton btnCourse = new JButton("课程信息管理");
    JButton btnGradeManage = new JButton("成绩信息管理");
    JButton btnCourselose = new JButton("退出管理系统");
    JLabel Jadmin = new JLabel("管理员");

    public AdminInterface() {
        super("学生信息管理系统");
        this.setLayout(null);
        this.setSize(750, 450);
        this.add(Jadmin);
        Jadmin.setBounds(340, 30, 100, 30);
        Jadmin.setFont(new java.awt.Font("宋体", 1, 25));
        this.add(btnStuManage);
        btnStuManage.setFont(new java.awt.Font("宋体", 0, 15));
        btnStuManage.setBounds(160, 120, 200, 45);
        this.add(btnCourse);
        btnCourse.setFont(new java.awt.Font("宋体", 0, 15));
        btnCourse.setBounds(400, 120, 200, 45);
        this.add(btnGradeManage);
        btnGradeManage.setFont(new java.awt.Font("宋体", 0, 15));
        btnGradeManage.setBounds(160, 220, 200, 45);
        this.add(btnCourselose);
        btnCourselose.setFont(new java.awt.Font("宋体", 0, 15));
        btnCourselose.setBounds(400, 220, 200, 45);
        btnStuManage.addActionListener(this);// 增加监听器
        btnCourse.addActionListener(this);
        btnGradeManage.addActionListener(this);
        btnCourselose.addActionListener(this);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == "学生信息管理") {
            this.dispose();
            MKey.CloseUser = true;
            MKey.ifCooikeUser = false; // 设置不可修改
            new StuManage("");
        }

        if (e.getActionCommand() == "课程信息管理") {
            this.dispose();
            MKey.CloseCourse = true;
            new CourseManage("教师任课表");
        }

        if (e.getActionCommand() == "成绩信息管理") {
            this.dispose();
            MKey.CloseGrade = true;
            MKey.ifCooikeGrade = false; // 设置不可修改
            new MarkManage("成绩信息管理");
        }

        if (e.getActionCommand() == "退出管理系统") {
            this.dispose();
            System.exit(0);
        }
    }

}