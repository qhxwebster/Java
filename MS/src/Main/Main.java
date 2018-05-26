package Main;

import javax.swing.UIManager;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 * main 调用Beautyeye界面美化包 实例登陆界面
 */
public class Main {

    public static void main(String[] args) {
        try {
            // 设置本属性将改变窗口边框样式定义----连接beautyeye(美化包)
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置按钮不可见
        UIManager.put("RootPane.setupButtonVisible", false);

        new Login();
    }
}
