package com.cointracker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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
    private NumberAxis chartCotacao;


    @FXML
    private BarChart<String, Double> chartVariacaoHoje;

    @FXML
    private TextField inputPesquisaMoeda;

    @FXML
    private TableView<?> tableCotacao;

    @FXML
    void initialize() {
        XYChart.Series<String, Double> serie1 = new XYChart.Series<>();

        serie1.getData().add(new XYChart.Data<>("min", 3.5021));
        serie1.getData().add(new XYChart.Data<>("now", 3.5900));
        serie1.getData().add(new XYChart.Data<>("max", 4.1112));

        chartVariacaoHoje.getData().add(serie1);

        Platform.runLater(this::applyChartColors);
    }

    private void applyChartColors() {
        String[] cores = {"#e74c3c", "#3498db", "#2ecc71"}; // Vermelho, Azul, Verde

        int indice = 0;
        for (XYChart.Data<String, Double> data :
                chartVariacaoHoje.getData().get(0).getData()) {
            data.getNode().setStyle("-fx-bar-fill: " + cores[indice] + ";");
            indice++;
        }

    }

    @FXML
    void pesquisarCotaMoeda(ActionEvent event) {
        System.out.println("PESQUISANDOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }

}
