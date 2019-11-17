package loginactivity.register;

import dao.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.BaseController;
import util.MD5Util;

import java.io.IOException;

public class RegisterController extends BaseController {

    @FXML
    Pane rootPane;

    @FXML
    TextField tf_register_user_account;

    @FXML
    TextField tf_register_user_name;

    @FXML
    PasswordField pf_register_user_password;

    @FXML
    PasswordField pf_register_user_password1;

    @FXML
    public void register() {
        String userAccount = tf_register_user_account.getText();
        String userPassword = pf_register_user_password.getText();
        String userPassword1 = pf_register_user_password1.getText();
        String userName = tf_register_user_name.getText();
        if (userAccount.equals("")) {
            alert("账号为空！", Alert.AlertType.WARNING);
            return;
        }
        if (userPassword.equals("")) {
            alert("密码为空！", Alert.AlertType.WARNING);
            return;
        }
        if (userPassword1.equals("")) {
            alert("请再次输入密码！", Alert.AlertType.WARNING);
            return;
        }
        if (!userPassword1.equals(userPassword)) {
            alert("两次输入的密码不一致！", Alert.AlertType.WARNING);
            return;
        }
        if (userName.equals("")) {
            alert("姓名为空！", Alert.AlertType.WARNING);
            return;
        }
        int statusCode = UserDao.register(userAccount, MD5Util.getEncryption(userPassword), userName);
        switch (statusCode) {
            case 1:
                alert("用户名非法！", Alert.AlertType.ERROR);
                break;
            case 2:
                alert("系统异常！", Alert.AlertType.ERROR);
                break;
            case 3:
                alert("注册成功！", Alert.AlertType.INFORMATION);
                back();
                break;
        }
    }

    @FXML
    public void back() {
        try {
            //加载界面B的fxml文件
            Parent root = FXMLLoader.load(getClass().getResource("/loginactivity/login/login.fxml"));
            //将一个新的fxml文件（界面B）加入到界面A的根布局上。
            rootPane.getChildren().setAll(root);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("CTMS登录");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
