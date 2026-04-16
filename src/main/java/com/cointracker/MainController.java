package com.cointracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class MainController {

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnRefresh;

    @FXML
    private TextField inputPesquisaMoeda;

    @FXML
    private TableView<?> tableCotacao;

    @FXML
    void pesquisarCotaMoeda(ActionEvent event) {
        System.out.println("PESQUISANDOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }

}
