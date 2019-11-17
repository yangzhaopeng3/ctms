package mainactivity.filmlib;

import dao.FilmlibDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.BaseController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Vector;

public class FlimlibController extends BaseController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private TableView tbview;
    @FXML private Button btn_delete;
    @FXML private Button btn_alter;
    @FXML private TextField tf_queryInput;
    @FXML private TableColumn colFilmNo;
    @FXML private TableColumn colFilmName;
    @FXML private TableColumn colReleaseTime;
    @FXML private TableColumn colRuntime;
    @FXML private TableColumn colPoster;

    private String imageStr = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_delete.setDisable(true);
        btn_alter.setDisable(true);
        showList(null);
        tbview.setRowFactory(tbview -> {
            TableRow<FilmInfo> row = new TableRow<FilmInfo>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    btn_delete.setDisable(false);
                    btn_alter.setDisable(false);
                    FilmInfo filmInfo = row.getItem();
                    btn_delete.setOnAction(event1 -> {
                        if (grant(2) == -1) {
                            return;
                        }
                        if (!alert("确认删除？", Alert.AlertType.CONFIRMATION)) {
                            return;
                        }
                        int i = -1;
                        try {
                            i = FilmlibDao.delete(new Object[]{filmInfo.getFilmNo()});
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
                        add(new Object[]{filmInfo.getFilmNo()});
                        btn_alter.setDisable(true);
                    });
                }
            });
            return row;
        });
    }

    private void add(Object[] values) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("数据录入");
        Button btn_fileChoose = new Button("选择图片");
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);
        grid.setPrefHeight(600);
        grid.setPadding(new Insets(20, 150, 10, 10));
        ButtonType addButtonType = new ButtonType("提交", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        TextField filmNo = new TextField();
        TextField filmName = new TextField();
        DatePicker releaseDate = new DatePicker();
        TextField filmRuntime = new TextField();
        releaseDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        grid.add(new Label("    影片编码:"), 0, 0);
        grid.add(filmNo, 1, 0);
        grid.add(new Label("    影片名称:"), 0, 1);
        grid.add(filmName, 1, 1);
        grid.add(new Label("    上映日期:"), 0, 2);
        grid.add(releaseDate, 1, 2);
        grid.add(new Label("    影片时长:"), 0, 3);
        grid.add(filmRuntime, 1, 3);
        grid.add(btn_fileChoose, 0, 4);

        btn_fileChoose.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择海报");
            File selectedFile = fileChooser.showOpenDialog((Stage) rootPane.getScene().getWindow());

            if (selectedFile != null) {
                try {
                    BufferedImage sourceImage = ImageIO.read(selectedFile);
                    BufferedImage targetImage = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
                    Graphics g = targetImage.getGraphics();
                    g.drawImage(sourceImage, 0, 0, 200, 300, null);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(targetImage, "jpg", bos);//把图片转换到ByteArrayOutputStream输出流中
                    byte[] imgeBuf = bos.toByteArray();//获得图片的二进制数据
                    ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imgeBuf)));
                    imageView.setFitHeight(300);//将图像视图宽度设置为100px
                    imageView.setFitWidth(200);
                    grid.add(imageView, 0, 5);
                    //把图片的二进制数据转换成字符串
                    BASE64Encoder encoder = new BASE64Encoder();
                    imageStr = encoder.encode(imgeBuf);//局部变量
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setOnMousePressed(event -> {
            String no = filmNo.getText();
            String name = filmName.getText();
            LocalDate date = releaseDate.getValue();
            if (no.equals("") || name.equals("") || date == null || filmRuntime.getText().equals("")) {
                alert("输入不能为空！", Alert.AlertType.WARNING);
                return;
            }
            if (!filmRuntime.getText().matches("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])")) {
                alert("影片时长输入非法！", Alert.AlertType.WARNING);
                return;
            }
            double ftime = new Double(filmRuntime.getText());
            int statusCode = -1;
            try {
                if (values == null) { //insert
                    statusCode = FilmlibDao.insert(new Object[]{no, name, date, ftime, imageStr});
                } else { //update
                    statusCode = FilmlibDao.update(new Object[]{no, name, date, ftime, imageStr}, values);
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
        colFilmNo.setCellValueFactory(new PropertyValueFactory("filmNo"));
        colFilmName.setCellValueFactory(new PropertyValueFactory("filmName"));
        colReleaseTime.setCellValueFactory(new PropertyValueFactory("releaseTime"));
        colRuntime.setCellValueFactory(new PropertyValueFactory("runtime"));
        colPoster.setCellValueFactory(new PropertyValueFactory("imageView"));
        ObservableList<FilmInfo> list = FXCollections.observableArrayList();
        try {
            Vector<Vector<Object>> vectors = FilmlibDao.queryFilm(queryWhere);
            for (int i = 0; i < vectors.size(); i++) {
                FilmInfo filmInfo = new FilmInfo();
                filmInfo.setFilmNo((String) vectors.get(i).get(0));
                filmInfo.setFilmName((String) vectors.get(i).get(1));
                filmInfo.setReleaseTime((String) vectors.get(i).get(2));
                filmInfo.setRuntime((new Double((String) vectors.get(i).get(3))));
                String imgStr = (String) vectors.get(i).get(4);
                if (imgStr != null && !imgStr.trim().equals("")) {
                    BASE64Decoder decoder = new BASE64Decoder();
                    try {
                        byte[] imgBuf = decoder.decodeBuffer(imgStr);
                        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imgBuf)));
                        imageView.setFitHeight(50);//将图像视图宽度设置为100px
                        imageView.setFitWidth(50);
                        filmInfo.setImageView(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                list.add(filmInfo);
            }
            tbview.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
