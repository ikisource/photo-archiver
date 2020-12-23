package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

/**
 *
 * @author olivier MATHE
 */
public class CopyBoxController implements Initializable {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label copyName;
    @FXML
    private Label counter;
    @FXML
    private Button cancelCopy;

    private MainController mainController;
    private Service service;
    private String name;
    private HBox hBox;

//    public void setMainController(MainController mainController) {
//        this.mainController = mainController;
//    }
    public void setService(MainController mainController, Service service, String name, HBox hBox) {

        this.mainController = mainController;
        this.service = service;
        this.name = name;
        this.hBox = hBox;
        progressBar.progressProperty().bind(service.progressProperty());
        counter.textProperty().bind(service.messageProperty());

        service.setOnCancelled(e -> {
            System.out.println("service annulé");

            //mainController.getCopyBox().getChildren().remove(hBox);
        });
        service.setOnSucceeded(e -> {
            System.out.println("service termné avec succès");
            //mainController.getServices().remove(name);
            //cancelCopy.setVisible(false);
        });
    }

    public void setCopyName(String copyName) {
        this.copyName.setText(copyName);
    }

    @Override
    public void initialize(URL location, final ResourceBundle resources) {
        System.out.println("initialize");

    }

    @FXML
    protected void cancelCopy(ActionEvent event) {

        if (service.getState().equals(State.RUNNING)) {
            service.cancel();
            System.out.println("cancelCopy");
        } else if (service.getState().equals(State.SUCCEEDED) || service.getState().equals(State.CANCELLED)) {
            mainController.getServices().remove(name);
            mainController.getCopyBox().getChildren().remove(hBox);
            System.out.println("remove task");
        }
    }

}
