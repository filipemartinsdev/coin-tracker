package com.cointracker;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChaveAPIController {
    @FXML
    private Button btnLimparChaveAPI;

    @FXML
    private Button btnSalvarChaveAPI;

    @FXML
    private TextField inputChaveAPI;

    @FXML
    void initialize(){
        if (Env.getChaveAPI() != null)
            inputChaveAPI.setText(Env.getChaveAPI());
    }

    @FXML
    void limparChaveAPI(ActionEvent event) {
        inputChaveAPI.clear();
    }

    @FXML
    void salvarChaveAPI(ActionEvent event) {
        Env.setChaveAPI(inputChaveAPI.getText());
    }

}
