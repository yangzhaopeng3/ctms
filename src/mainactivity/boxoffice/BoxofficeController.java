package mainactivity.boxoffice;

import dao.BoxofficeDao;
import dao.FilmlibDao;
import dao.ScheduleDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.BaseController;

import java.math.BigInteger;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;

public class BoxofficeController extends BaseController implements Initializable {

    @FXML private TableView tbview;
    @FXML private Button btn_delete;
    @FXML private TableColumn colFilmNo;
    @FXML private TableColumn colFilmName;
    @FXML private TableColumn colShowTime;
    @FXML private TableColumn colShowHall;
    @FXML private TableColumn colTicketNum;
    @FXML private Button btn_alter;
    @FXML private TableColumn colBoxoffice;
    @FXML private TextField tf_queryInput;


    @FXML
    private void add() {
        if (grant(2) == -1) {
            return;
        }
        add(null);
    }

    private void add(Object[] value) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("录入售票信息");
        VBox vBox = new VBox(10);
        ButtonType addButtonType = new ButtonType("提交", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        ComboBox filmName = new ComboBox();
        TextField ticketNum = new TextField();

        if (value == null) {
            Vector<Vector<Object>> vectors = null;
            try {
                vectors = BoxofficeDao.querySchedule();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (vectors.size() == 0) {
                alert("暂无需要增加的票房信息", Alert.AlertType.INFORMATION);
                return;
            }
            for (int i = 0; i < vectors.size(); i++) {
                filmName.getItems().add(vectors.get(i).get(0) + "," + vectors.get(i).get(1) + "," +
                        vectors.get(i).get(2) + "号厅");
            }
            vBox.getChildren().addAll(new Label("影片信息:"), filmName);
        }

        vBox.getChildren().addAll(new Label("售出票数:"), ticketNum);
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setOnMousePressed(event -> {

            String show_time = null;
            int show_hall = 0;
            BigInteger no = null;


            if ((value == null && filmName == null) || ticketNum.getText().equals("")) {
                alert("输入数据不能为空！", Alert.AlertType.WARNING);
                return;
            }
            if (!ticketNum.getText().matches("[0-9]+")) {
                alert("售票数量输入非法！", Alert.AlertType.WARNING);
                return;
            }
            int statusCode = -1;
            try {
                if (value == null) {
                    String info = (String) filmName.getValue();
                    String[] infos = info.split(",");
                    String film_name = infos[0];
                    show_time = infos[1];
                    show_hall = new Integer(infos[2].charAt(0) - '0');
                    no = null;
                    try {
                        no = new BigInteger((String) FilmlibDao.queryFilmNo(new Object[]{film_name}));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    statusCode = BoxofficeDao.insert(new Object[]{no, show_time, show_hall, new Integer((ticketNum.getText()))});
                } else {
                    statusCode = BoxofficeDao.update(new Object[]{new Integer((ticketNum.getText()))}, value);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (statusCode == -1) {
                alert("添加失败！", Alert.AlertType.ERROR);
            } else if (statusCode == 1) {
                alert("添加成功！", Alert.AlertType.INFORMATION);
                try {
                    ScheduleDao.updateChecked(new Object[]{no, show_time, show_hall});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                showList(null);
                dialog.close();
            }
        });
        dialog.getDialogPane().setContent(vBox);
        dialog.showAndWait();
    }

    @FXML
    public void query() {
        showList(tf_queryInput.getText());
    }


    private void showList(String queryWhere) {
        colFilmNo.setCellValueFactory(new PropertyValueFactory("filmNo"));
        colFilmName.setCellValueFactory(new PropertyValueFactory("filmName"));
        colShowTime.setCellValueFactory(new PropertyValueFactory("showTime"));
        colShowHall.setCellValueFactory(new PropertyValueFactory("showHall"));
        colTicketNum.setCellValueFactory(new PropertyValueFactory("ticketNum"));
        colBoxoffice.setCellValueFactory(new PropertyValueFactory("boxoffice"));
        ObservableList<BoxofficeInfo> list = FXCollections.observableArrayList();
        try {
            Vector<Vector<Object>> vectors = BoxofficeDao.queryBoxoffice(queryWhere);
            for (int i = 0; i < vectors.size(); i++) {
                String filmNo = (String) vectors.get(i).get(0);
                String showTime = (String) vectors.get(i).get(2);
                int showHall = Integer.parseInt((String) vectors.get(i).get(3));
                //calculate total_boxoffice;
                int ticketNum = Integer.parseInt((String) vectors.get(i).get(4));
                Vector<Vector<Object>> v = BoxofficeDao.queryTicketPrice(new Object[]{filmNo, showTime, showHall});
                double ticktPrice = new Double((String) v.get(0).get(0));

                BoxofficeInfo boxofficeInfo = new BoxofficeInfo();
                boxofficeInfo.setFilmNo(filmNo);
                boxofficeInfo.setFilmName((String) vectors.get(i).get(1));
                boxofficeInfo.setShowTime(showTime);
                boxofficeInfo.setShowHall(showHall);
                boxofficeInfo.setTicketNum(ticketNum);
                boxofficeInfo.setBoxoffice(ticketNum * ticktPrice);
                list.add(boxofficeInfo);
            }
            tbview.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showList(null);
        btn_delete.setDisable(true);
        btn_alter.setDisable(true);
        tbview.setRowFactory(tbview -> {
            TableRow<BoxofficeInfo> row = new TableRow<BoxofficeInfo>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    btn_delete.setDisable(false);
                    btn_alter.setDisable(false);

                    BoxofficeInfo boxofficeInfo = row.getItem();

                    btn_delete.setOnAction(event1 -> {
                        if (grant(2) == -1) {
                            return;
                        }

                        if (!alert("确认删除？", Alert.AlertType.CONFIRMATION)) {
                            return;
                        }


                        int i = -1;
                        try {
                            i = BoxofficeDao.delete(new Object[]{boxofficeInfo.getFilmNo(), boxofficeInfo.getShowTime(), boxofficeInfo.getShowHall()});
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (i == 1) {
                            alert("删除成功！", Alert.AlertType.INFORMATION);
                            try {
                                ScheduleDao.updateCheckedBack(new Object[]{new BigInteger(boxofficeInfo.getFilmNo()), boxofficeInfo.getShowTime(), boxofficeInfo.getShowHall()});
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            btn_delete.setDisable(true);
                            showList(null);
                        } else {
                            alert("删除失败！", Alert.AlertType.ERROR);
                            btn_delete.setDisable(true);
                        }
                    });

                    btn_alter.setOnAction(event1 -> {
                        if (grant(2) == -1) {
                            return;
                        }
                        add(new Object[]{boxofficeInfo.getFilmNo(), boxofficeInfo.getShowTime(), boxofficeInfo.getShowHall()});
                    });
                }
            });
            return row;
        });
    }
}
