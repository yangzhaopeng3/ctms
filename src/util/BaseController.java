package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Date;
import java.util.Optional;

public class BaseController {

    public BaseController() {
    }

    /**
     * @return void
     * @author 杨肇鹏
     * @date 2019/6/24 14:05
     * @params [msg, alertType]
     * @desc 弹出提示框
     */
    public boolean alert(String msg, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, msg, new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));
        Optional<ButtonType> buttonType = alert.showAndWait();
//        根据点击结果返回
        if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return int
     * @author 杨肇鹏
     * @date 2019/6/28 15:09
     * @params [level]
     * @desc 用户权限校验，权限不足返回-1
     */
    public int grant(int level) {
        int i = 1;
        if (UserInfo.userPermission < level) {
            switch (UserInfo.userPermission) {
                case 0:
                    i = -1;
                    alert("未登录！", Alert.AlertType.ERROR);
                    break;
                case 1:
                    i = -1;
                    alert("权限不足！", Alert.AlertType.ERROR);
                    break;
            }
        }
        return i;
    }


    public int compareTime(Date date1, Date date2) {
        if (date1.getTime() < date2.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }

}
