package mainactivity.schedule;

import dao.FilmlibDao;
import dao.ScheduleDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import util.BaseController;

import java.math.BigInteger;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

public class ScheduleController extends BaseController implements Initializable {
    @FXML private TableView tbview;
    @FXML private TableColumn colFilmName;
    @FXML private TableColumn colShowTime;
    @FXML private TableColumn colShowHall;
    @FXML private TableColumn colTicketPrice;
    @FXML private Button btn_delete;
    @FXML private Button btn_alter;
    @FXML private TextField tf_queryInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showList(null);
        btn_delete.setDisable(true);
        btn_alter.setDisable(true);
        tbview.setRowFactory(tbview -> {
            TableRow<ScheduleInfo> row = new TableRow<ScheduleInfo>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    btn_delete.setDisable(false);
                    btn_alter.setDisable(false);
                    ScheduleInfo scheduleInfo = row.getItem();
                    btn_delete.setOnAction(event1 -> {
                        if (grant(2) == -1) {
                            return;
                        }

                        if (!alert("确认删除？", Alert.AlertType.CONFIRMATION)) {
                            return;
                        }

                        int i = -1;
                        try {
                            BigInteger bigInteger = null;
                            Vector<Vector<Object>> v = ScheduleDao.queryFilmName();
                            for (int j = 0; j < v.size(); j++) {
                                if ((v.get(j).get(1)).equals(scheduleInfo.getFilmName())) {
                                    bigInteger = new BigInteger(((String) v.get(j).get(0)));
                                    break;
                                }
                            }
                            i = ScheduleDao.delete(new Object[]{bigInteger, scheduleInfo.getShowTime(), scheduleInfo.getShowHall()});
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (i == 1) {
                            alert("删除成功！", Alert.AlertType.INFORMATION);
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

                        try {
                            BigInteger no = new BigInteger((String) FilmlibDao.queryFilmNo(new Object[]{scheduleInfo.getFilmName()}));
                            add(new Object[]{no, scheduleInfo.getShowTime(), scheduleInfo.getShowHall()});
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            return row;
        });
    }

    @FXML
    private void add() {
        if (grant(2) == -1) {
            return;
        }
        add(null);
    }

    @FXML
    private void query() {
        String queryName = tf_queryInput.getText();
        showList(queryName);
    }

    public void add(Object[] values) {
        Dialog dialog = new Dialog();
        dialog.setTitle("添加排片信息");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 10, 10, 10));

        ButtonType addButtonType = new ButtonType("提交", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        ComboBox filmName = new ComboBox();
        Vector<Vector<Object>> vectors = null;
        try {
            vectors = ScheduleDao.queryFilmName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < vectors.size(); i++) {
            filmName.getItems().add(vectors.get(i).get(1));
        }
        final Vector<Vector<Object>> v = vectors;
        DatePicker showTime = new DatePicker();

        ComboBox showHall = new ComboBox();
        Vector<Vector<Object>> vector = null;
        try {
            vector = ScheduleDao.queryHall();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < vector.size(); i++) {
            showHall.getItems().add(vector.get(i).get(0));
        }

        TextField ticketPrice = new TextField();

        ComboBox hour = new ComboBox();
        hour.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24);
        ComboBox mini = new ComboBox();
        mini.getItems().addAll(0, 10, 20, 30, 40, 50);

        grid.add(new Label("    影片名称:"), 0, 0);
        grid.add(filmName, 1, 0);
        grid.add(new Label("    放映时间:"), 0, 1);
        grid.add(showTime, 1, 1);
        grid.add(new Label("                    :"), 0, 2);
        grid.add(hour, 0, 2);
        grid.add(mini, 1, 2);
        grid.add(new Label("    放映场地:"), 0, 3);
        grid.add(showHall, 1, 3);
        grid.add(new Label("    影片票价:"), 0, 4);
        grid.add(ticketPrice, 1, 4);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setOnMousePressed(event -> {
            if (filmName.getValue() == null || showTime.getValue() == null || showHall.getValue() == null
                    || hour.getValue() == null || mini.getValue() == null
                    || ticketPrice.getText().equals("")) {
                alert("输入不能为空！", Alert.AlertType.WARNING);
                return;
            }
            int selectIndex = filmName.getSelectionModel().getSelectedIndex();
            String no = (String) v.get(selectIndex).get(0);
            LocalDate date = showTime.getValue();
            String shall = (String) showHall.getValue();
            String price = ticketPrice.getText();

            if (!shall.matches("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])")) {
                alert("影片时长输入非法！", Alert.AlertType.WARNING);
                return;
            }

            if (!price.matches("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])")) {
                alert("影片票价输入非法！", Alert.AlertType.WARNING);
                return;
            }

            int h = new Integer(shall);
            double p = new Double(price);
            int statusCode = -1;
            try {
                ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
                Calendar cal = Calendar.getInstance();
                cal.setTime(Date.from(zonedDateTime.toInstant()));
                cal.set(Calendar.HOUR_OF_DAY, (Integer) hour.getValue() + 8);
                cal.set(Calendar.MINUTE, (Integer) mini.getValue());
                if (compareTime(cal.getTime(), new Date()) == 1) {
                    alert("时光无法倒流", Alert.AlertType.WARNING);
                    return;
                }

                int filmRuntime = FilmlibDao.queryFilmRuntime(new Object[]{no});
                Date endDate = new Date(cal.getTimeInMillis() + filmRuntime);

                //检验排片冲突
                endDate.setHours(endDate.getHours() - 8);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str1 = sdf.format(endDate);
                cal.set(Calendar.HOUR_OF_DAY, (Integer) hour.getValue());
                String str2 = sdf.format(cal.getTime());

                Vector<Vector<Object>> vectorConfict = ScheduleDao.queryConfict(new Object[]{h, str2, str1});
                Vector<Vector<Object>> vectorConfict1 = ScheduleDao.queryConfictEnd(new Object[]{h, str2, str1});

                cal.set(Calendar.HOUR_OF_DAY, (Integer) hour.getValue() + 8);
                endDate.setHours(endDate.getHours() + 8);
                if (vectorConfict.size() != 0 || vectorConfict1.size() != 0) {
                    alert("排片冲突！", Alert.AlertType.ERROR);
                    return;
                }
                if (values == null) {
                    statusCode = ScheduleDao.insert(new Object[]{no, cal.getTime(), h, p, endDate});
                } else {
                    statusCode = ScheduleDao.update(new Object[]{no, cal.getTime(), h, p, endDate}, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (statusCode == -1) {
                alert("添加失败！", Alert.AlertType.ERROR);
            } else if (statusCode == 1) {
                alert("添加成功！", Alert.AlertType.INFORMATION);
                showList(null);
                dialog.close();
            }
        });
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }

    private void showList(String queryWhere) {
        colFilmName.setCellValueFactory(new PropertyValueFactory("filmName"));
        colShowTime.setCellValueFactory(new PropertyValueFactory("showTime"));
        colShowHall.setCellValueFactory(new PropertyValueFactory("showHall"));
        colTicketPrice.setCellValueFactory(new PropertyValueFactory("ticketPrice"));
        ObservableList<ScheduleInfo> list = FXCollections.observableArrayList();
        try {
            Vector<Vector<Object>> vectors = ScheduleDao.querySchedule(queryWhere);
            for (int i = 0; i < vectors.size(); i++) {
                ScheduleInfo scheduleInfo = new ScheduleInfo();
                scheduleInfo.setFilmName((String) vectors.get(i).get(0));
                scheduleInfo.setShowTime((String) vectors.get(i).get(1));
                scheduleInfo.setShowHall(new Integer((String) vectors.get(i).get(2)));
                scheduleInfo.setTicketPrice((new Double((String) vectors.get(i).get(3))));
                list.add(scheduleInfo);
            }
            tbview.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
