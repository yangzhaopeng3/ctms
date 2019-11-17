package mainactivity.hall;

import dao.HallDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.BaseController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;

public class HallController extends BaseController implements Initializable {

    @FXML
    private TableColumn colHallNo;
    @FXML
    private TableColumn colSeatNum;
    @FXML
    private TableColumn colIsWork;
    @FXML
    private TableView tbview;
    @FXML
    private Button btn_alter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_alter.setDisable(true);
        showList();
        tbview.setRowFactory(tbview -> {
            TableRow<HallInfo> row = new TableRow<HallInfo>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    btn_alter.setDisable(false);
                    HallInfo hallInfo = row.getItem();

                    Integer hall_no = new Integer(hallInfo.getHallNo());

                    btn_alter.setOnAction(event1 -> {
                        if (grant(2) == -1) {
                            return;
                        }
                        alter(new Object[]{hall_no});
                        btn_alter.setDisable(true);
                    });
                }
            });
            return row;
        });


    }

    public void alter(Object[] value) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("影厅维护信息修改");
        VBox vBox = new VBox(10);
        ButtonType addButtonType = new ButtonType("提交", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        ComboBox isWork = new ComboBox();
        isWork.getItems().addAll(0, 1);
        vBox.getChildren().addAll(new Label("设置影厅工作状态，0维护，1正常使用"), isWork);
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setOnMousePressed(event -> {
            if (isWork == null) {
                alert("输入非法！", Alert.AlertType.WARNING);
                return;
            }

            Integer iw = (Integer) isWork.getValue();
            int statusCode = -1;
            try {
                statusCode = HallDao.update(value, new Object[]{iw});
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (statusCode == -1) {
                alert("修改失败！", Alert.AlertType.ERROR);
            } else if (statusCode == 1) {
                alert("修改成功！", Alert.AlertType.INFORMATION);
                showList();
                dialog.close();
            }
        });
        dialog.getDialogPane().setContent(vBox);
        dialog.showAndWait();
    }

    public void showList() {
        colHallNo.setCellValueFactory(new PropertyValueFactory("hallNo"));
        colSeatNum.setCellValueFactory(new PropertyValueFactory("hallSeat"));
        colIsWork.setCellValueFactory(new PropertyValueFactory("isWork"));
        ObservableList<HallInfo> list = FXCollections.observableArrayList();
        try {
            Vector<Vector<Object>> vectors = HallDao.queryHall();
            for (int i = 0; i < vectors.size(); i++) {
                HallInfo hallInfo = new HallInfo();
                hallInfo.setHallNo(Integer.parseInt((String) vectors.get(i).get(0)));
                hallInfo.setHallSeat(Integer.parseInt((String) vectors.get(i).get(1)));
                hallInfo.setIsWork(Integer.parseInt((String) vectors.get(i).get(2)));
                list.add(hallInfo);
            }
            tbview.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
