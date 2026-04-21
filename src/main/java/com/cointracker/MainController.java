package com.cointracker;

import com.cointracker.dto.TableHistoricoConversaoItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;

public class MainController {
    private CotacaoClient cotacaoClient = new CotacaoClient();

    @FXML
    private Button btnCalcularConversao;

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<String> cBoxMoeda1Conversor;

    @FXML
    private ComboBox<String> cBoxMoeda2Conversor;

    @FXML
    private BarChart<String, Double> chartVariacaoHoje;

    @FXML
    private TextField inputPesquisaCotacao;

    @FXML
    private Spinner<Integer> spinQtdConversao;

    @FXML
    private TableView<?> tableCotacao;

    @FXML
    private TableView<TableHistoricoConversaoItem> tableHistoricoConversao;

    @FXML
    private TableColumn<TableHistoricoConversaoItem, String> colQuantidadeConversao;

    @FXML
    private TableColumn<TableHistoricoConversaoItem, String> colDeConversao;

    @FXML
    private TableColumn<TableHistoricoConversaoItem, String> colParaConversao;

    @FXML
    private TableColumn<TableHistoricoConversaoItem, Double> colResultadoConversao;

    private final ObservableList<TableHistoricoConversaoItem> tableItems = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999, 1);

        spinQtdConversao.setValueFactory(valueFactory);
        spinQtdConversao.setEditable(true);

        XYChart.Series<String, Double> serie1 = new XYChart.Series<>();

        serie1.getData().add(new XYChart.Data<>("min", 3.5021));
        serie1.getData().add(new XYChart.Data<>("now", 3.5900));
        serie1.getData().add(new XYChart.Data<>("max", 4.1112));

        chartVariacaoHoje.getData().add(serie1);


        Platform.runLater(this::applyChartColors);
        Platform.runLater(this::setupCBoxMoeda1Conversor);
        Platform.runLater(this::setupTableHistoricoConversao);
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

    private void setupCBoxMoeda1Conversor() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return cotacaoClient.buscarMoedas().keySet().stream().toList();
            }
        };

        task.setOnSucceeded(e -> {
            List<String> coins = task.getValue();
            cBoxMoeda1Conversor.getItems().addAll(coins);
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void setupTableHistoricoConversao() {
        tableHistoricoConversao.setItems(tableItems);

        colQuantidadeConversao.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colDeConversao.setCellValueFactory(new PropertyValueFactory<>("de"));
        colParaConversao.setCellValueFactory(new PropertyValueFactory<>("para"));
        colResultadoConversao.setCellValueFactory(new PropertyValueFactory<>("resultado"));
    }

    // finish setup ☝

    @FXML
    void pesquisarCotacoes(ActionEvent event) {
        String response;

        Task<Map<String, String>> task = new Task<>() {
            @Override
            protected Map<String, String> call() throws Exception {
                return cotacaoClient.buscarMoedas();
            }
        };

        task.setOnSucceeded(e -> {
            System.out.println(task.getValue());
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    void clickCBoxMoeda2(){
        cBoxMoeda2Conversor.getItems().clear();
        String moeda1 = cBoxMoeda1Conversor.getSelectionModel().getSelectedItem();

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return cotacaoClient.buscarCoversoesDisponiveisDe(moeda1);
            }
        };

        task.setOnSucceeded(e -> {

            cBoxMoeda2Conversor.getItems().addAll(task.getValue());
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    void calcularConversao(ActionEvent event) {
        Integer qtd = spinQtdConversao.getValue();
        String moeda1 = cBoxMoeda1Conversor.getSelectionModel().getSelectedItem();
        String moeda2 = cBoxMoeda2Conversor.getSelectionModel().getSelectedItem();

        Task<Double> task = new Task<>() {
            @Override
            protected Double call() throws Exception {
                return cotacaoClient.converter(qtd, moeda1, moeda2);
            }
        };

        task.setOnSucceeded(e -> {
            tableItems.add(new TableHistoricoConversaoItem(
                    qtd, moeda1, moeda2, task.getValue()
            ));
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    void refreshCotacoes(ActionEvent event) {}
}
