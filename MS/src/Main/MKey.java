package Main;

/**
 * 判断用户对应权限
 */
public class MKey {
    public static String OnlyStuId = null;	/** 学生登入后的唯一ID */
    public static String SelectUser = null;		/** 传输查询学生信息的ID */
    public static String SelectGrade = null;	/** 传输查询学生成绩的ID */
    public static boolean IfSuccess = false;	/** 判断是否登录成功 */
    public static boolean StuUser = false;		/** 用于更正用于显示过数据,再次获取数据时,index位置不同步 */
    public static boolean StuGrade = false;		/** 用于更正用于显示过数据,再次获取数据时,index位置不同步 */
    public static boolean ifCooikeUser = false;
    public static boolean ifCooikeGrade = false;	/** 用于判断用户是否选择了数据,避免连接池异常----默认不可使用 */

    public static boolean CloseGrade = false;
    public static boolean CloseUser = false;
    public static boolean CloseCourse = false;		/** 监听关闭按钮,控制是否返回*/
}
