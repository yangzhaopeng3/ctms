package loginactivity.login;

import dao.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mainactivity.main.Main;
import util.BaseController;
import util.UserInfo;

import java.io.IOException;
import java.util.Map;

public class LoginController extends BaseController {
    @FXML
    Pane rootPane;

    @FXML
    TextField tf_login_user_account;

    @FXML
    PasswordField pf_login_user_password;

    @FXML
    public void login() {
        String userAccount = tf_login_user_account.getText();
        String userPassword = pf_login_user_password.getText();
        if (userAccount.equals("")) {
            alert("用户名为空！", Alert.AlertType.WARNING);
            return;
        } else if (userPassword.equals("")) {
            alert("密码为空！", Alert.AlertType.WARNING);
            return;
        }
        Map map = UserDao.verifyLogin(userAccount, userPassword);
        switch ((int) map.get("statusCode")) {
            case 1:
                alert("用户不存在！", Alert.AlertType.ERROR);
                break;
            case 2:
                alert("密码错误！", Alert.AlertType.ERROR);
                break;
            case 3:
                UserInfo.userName = (String) map.get("userName");
                UserInfo.userPermission = (int) map.get("userPermission");
                UserInfo.userId = (int) map.get("userId");
                alert("欢迎" + UserInfo.userName, Alert.AlertType.INFORMATION);
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
                break;
            default:
                alert("服务器异常！", Alert.AlertType.ERROR);
                break;
        }
    }

    @FXML
    public void register() {
        try {
            //加载界面B的fxml文件
            Parent root = FXMLLoader.load(getClass().getResource("/loginactivity/register/register.fxml"));
            //将一个新的fxml文件（界面B）加入到界面A的根布局上。
            rootPane.getChildren().setAll(root);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("CTMS注册");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
