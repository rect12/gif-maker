package sample;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class Controller {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private GifMaker gifMakerImpl = new GifMaker();

    @FXML
    private Slider animationSlider;
    @FXML
    private Label animationValue;
    @FXML
    private Slider heightSlider;
    @FXML
    private Label heightValue;
    @FXML
    private Slider widthSlider;
    @FXML
    private Label widthValue;
    @FXML
    private TableView<File> imageTable;
    @FXML
    private TableColumn<File, String> imageNameColumn;


    public void uploadImage() {
        Stage stage = Main.getPrimaryStage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            gifMakerImpl.addList(list);
        }
    }

    public void downloadGif() {
        if (gifMakerImpl.getList().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, there is no images to make gif!");

            alert.showAndWait();
        }
        else {
            Stage stage = Main.getPrimaryStage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save gif");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                    "Gif files (*.gif)", "*.gif");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                if (!file.getPath().endsWith(".gif")) {
                    file = new File(file.getPath() + ".gif");
                }
                gifMakerImpl.makeGif(file.getAbsolutePath());
            }
        }
    }

    public void deleteImage() {
        File selectedFile = imageTable.getSelectionModel().getSelectedItem();
        gifMakerImpl.deleteImage(selectedFile);
    }

    public void initialize() {
        animationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            animationValue.setText(Integer.toString(newValue.intValue()));
            gifMakerImpl.setSpeedValue(newValue.intValue());
        });

        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            heightValue.setText(Integer.toString(newValue.intValue()));
            gifMakerImpl.setHeight(newValue.intValue());
        });

        widthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            widthValue.setText(Integer.toString(newValue.intValue()));
            gifMakerImpl.setWidth(newValue.intValue());
        });

        imageNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        imageTable.setItems(gifMakerImpl.getList());

        imageTable.setRowFactory(tv -> {
            TableRow<File> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    File draggedPerson = imageTable.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = imageTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    imageTable.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    imageTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row ;
        });
    }
}