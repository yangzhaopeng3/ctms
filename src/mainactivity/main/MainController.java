package mainactivity.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import loginactivity.login.Login;
import util.BaseController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BaseController implements Initializable {

    @FXML
    private ImageView iv_main_back;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane rightPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("file:\\D:\\dev\\JetBrains\\IdeaProjects\\ctms\\image\\baseline_exit_to_app_black_18dp.png");
        iv_main_back.setImage(image);
    }

    @FXML
    private void back() {
        try {
            new Login().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void goToFilmlib() {
        if (grant(1) == -1) {
            return;
        }
        try {
            rightPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../filmlib/filmlib.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSchedule() {
        if (grant(1) == -1) {
            return;
        }
        try {
            rightPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../schedule/schedule.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToBoxoffice() {
        if (grant(1) == -1) {
            return;
        }
        try {
            rightPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../boxoffice/boxoffice.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToHall() {
        if (grant(1) == -1) {
            return;
        }
        try {
            rightPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../hall/hall.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
