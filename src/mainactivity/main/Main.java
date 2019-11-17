package mainactivity.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.UserInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("CTMS管理系统");
        primaryStage.setScene(new Scene(root, 1100, 660));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - 1000) / 2);
        primaryStage.setY((screenBounds.getHeight() - 660) / 2);
        primaryStage.setResizable(false);
        primaryStage.show();
        Label label = (Label) root.lookup("#lb_main_info");
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        EventHandler<ActionEvent> eventHandler = e -> {
            String labelSt = "请退出系统，" + UserInfo.USER_TITILE0;
            switch (UserInfo.userPermission) {
                case 0:
                    labelSt = "请退出系统，" + UserInfo.USER_TITILE0;
                    break;
                case 1:
                    labelSt = "欢迎你，" + UserInfo.UESR_TITLE1;
                    break;
                case 2:
                    labelSt = "欢迎你，" + UserInfo.USER_TITLE2;
                    break;
            }
            label.setText(labelSt + "  " + UserInfo.userName + "    系统时间：" + df.format(new Date()));
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
}
