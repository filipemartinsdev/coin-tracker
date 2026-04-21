package com.cointracker;

import com.cointracker.dto.ConversaoResponse;
import com.cointracker.dto.TableHistoricoConversaoItem;
import com.cointracker.dto.TableHistoricoCotacaoItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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

    private final ObservableList<TableHistoricoConversaoItem> tableHistoricoConversaoItens = FXCollections.observableArrayList();


    @FXML
    private Spinner<Integer> spinDiasHistoricoCotacao;

    @FXML
    private ComboBox<String> cBoxMoedaHistoricoCotacao;

    @FXML
    private Button btnBuscarHistoricoCotacao;


    @FXML
    private TableView<TableHistoricoCotacaoItem> tableHistoricoCotacao;

    @FXML
    private TableColumn<TableHistoricoCotacaoItem, LocalDate> colDataHistoricoCotacao;

    @FXML
    private TableColumn<TableHistoricoCotacaoItem, Double> colValorHistoricoCotacao;

    @FXML
    private TableColumn<TableHistoricoCotacaoItem, Double> colVariacaoHistoricoCotacao;

    @FXML
    private TableColumn<TableHistoricoCotacaoItem, Double> colAltaHistoricoCotacao;

    @FXML
    private TableColumn<TableHistoricoCotacaoItem, Double> colBaixaHistoricoCotacao;

    private final ObservableList<TableHistoricoCotacaoItem> tableHistoricoCotacaoItens = FXCollections.observableArrayList();

    @FXML
    private AreaChart<String, Double> chartHistoricoCotacao;

    @FXML
    private NumberAxis yAxisChartHistoricoCotacao;

    @FXML
    void initialize() {
        Platform.runLater(this::setupChartVariacaoHoje);
        Platform.runLater(this::applyChartColors);
        Platform.runLater(this::setupCBoxMoeda1Conversor);
        Platform.runLater(this::setupTableHistoricoConversao);
        Platform.runLater(this::setupSpinQtdConversao);
        Platform.runLater(this::setupSpinDiasHistoricoCotacao);
        Platform.runLater(this::setupCBoxMoedaHistorioCotacao);
        Platform.runLater(this::setupTableHistoricoCotacao);
        Platform.runLater(this::setupAreaChartHistoricoCotacao);
    }

    private void setupSpinQtdConversao() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999, 1);

        spinQtdConversao.setValueFactory(valueFactory);
        spinQtdConversao.setEditable(true);
    }

    private void setupSpinDiasHistoricoCotacao() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 365, 1);

        spinDiasHistoricoCotacao.setValueFactory(valueFactory);
        spinDiasHistoricoCotacao.setEditable(true);
    }

    private void setupChartVariacaoHoje() {
        XYChart.Series<String, Double> serie1 = new XYChart.Series<>();

        serie1.getData().add(new XYChart.Data<>("min", 3.5021));
        serie1.getData().add(new XYChart.Data<>("now", 3.5900));
        serie1.getData().add(new XYChart.Data<>("max", 4.1112));

        chartVariacaoHoje.getData().add(serie1);
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

    private void setupCBoxMoedaHistorioCotacao() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return cotacaoClient.buscarCoversoesDisponiveisPara("BRL").stream().toList();
            }
        };

        task.setOnSucceeded(e -> {
            List<String> coins = task.getValue();
            cBoxMoedaHistoricoCotacao.getItems().addAll(coins);
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void setupTableHistoricoConversao() {
        tableHistoricoConversao.setItems(tableHistoricoConversaoItens);

        colQuantidadeConversao.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colDeConversao.setCellValueFactory(new PropertyValueFactory<>("de"));
        colParaConversao.setCellValueFactory(new PropertyValueFactory<>("para"));
        colResultadoConversao.setCellValueFactory(new PropertyValueFactory<>("resultado"));
    }

    private void setupTableHistoricoCotacao() {
        tableHistoricoCotacao.setItems(tableHistoricoCotacaoItens);
        setupColVariacaoHistoricoCotacao();
        setupColDataHistoricoCotacao();
        setupColValorHistoricoCotacao();
        setupColAltaHistoricoCotacao();
        setupColBaixaHistoricoCotacao();
    }

    private void setupColVariacaoHistoricoCotacao() {
        colVariacaoHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("variacao"));


        colVariacaoHistoricoCotacao.setCellFactory(column -> new TableCell<TableHistoricoCotacaoItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format(Locale.of("pt", "BR"), "R$ %.6f", item));
                }
            }
        });
    }

    private void setupColDataHistoricoCotacao() {
        colDataHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("data"));

        colDataHistoricoCotacao.setCellFactory(column -> new TableCell<TableHistoricoCotacaoItem, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String dataFormatada = item.format(formatador);
                    setText(dataFormatada);
                }
            }
        });
    }

    private void setupColValorHistoricoCotacao() {
        colValorHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colValorHistoricoCotacao.setCellFactory(column -> new TableCell<TableHistoricoCotacaoItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format(Locale.of("pt", "BR"), "R$ %.6f", item));
                }
            }
        });
    }

    private void setupColAltaHistoricoCotacao() {
        colAltaHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("alta"));
        colAltaHistoricoCotacao.setCellFactory(column -> new TableCell<TableHistoricoCotacaoItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format(Locale.of("pt", "BR"), "R$ %.6f", item));
                }
            }
        });
    }

    private void setupColBaixaHistoricoCotacao() {
        colBaixaHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("baixa"));
        colBaixaHistoricoCotacao.setCellFactory(column -> new TableCell<TableHistoricoCotacaoItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format(Locale.of("pt", "BR"), "R$ %.6f", item));
                }
            }
        });
    }

    private void setupAreaChartHistoricoCotacao() {
        yAxisChartHistoricoCotacao.setTickLabelFormatter(new StringConverter<Number>() {
            private final NumberFormat format = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

            @Override public String toString(Number number) {
                return format.format(number);
            }

            @Override public Number fromString(String string) {
                try {
                    return format.parse(string);
                } catch (Exception e) {
                    return 0;
                }
            }
        });
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
                return cotacaoClient.buscarCoversoesDisponiveisPara(moeda1);
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
            tableHistoricoConversaoItens.add(new TableHistoricoConversaoItem(
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

    @FXML
    void buscarHistoricoCotacao(ActionEvent event) {
        tableHistoricoCotacaoItens.clear();

        String moeda = cBoxMoedaHistoricoCotacao.getSelectionModel().getSelectedItem();
        Integer qtd = spinDiasHistoricoCotacao.getValue();

        Task<List<ConversaoResponse.ConversaoData>> task = new Task<>() {
            @Override
            protected List<ConversaoResponse.ConversaoData> call() throws Exception {
                return cotacaoClient.buscarHistoricoCotacao(moeda, qtd);
            }
        };

        task.setOnSucceeded(e -> {
            tableHistoricoCotacaoItens.addAll(
                    task.getValue().stream()
                        .map(from -> converterParaTabela(from))
                        .toList()
            );

            chartHistoricoCotacao.getData().clear();

            XYChart.Series<String, Double> series = new XYChart.Series<>();

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for(int i = tableHistoricoCotacaoItens.size()-1; i >= 0; i--){
                var item = tableHistoricoCotacaoItens.get(i);
                String dataFormatada = item.getData().format(formatador);
                series.getData().add(new XYChart.Data<>(dataFormatada, item.getValor()));
            }

            chartHistoricoCotacao.getData().add(series);
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }


    private TableHistoricoCotacaoItem converterParaTabela(ConversaoResponse.ConversaoData conversaoData){
        long timestamp = Long.parseLong(conversaoData.timestamp);

        LocalDate data = Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Double valor = Double.parseDouble(conversaoData.bid);
        Double alta = Double.parseDouble(conversaoData.high);
        Double baixa = Double.parseDouble(conversaoData.low);
        Double variacao = Double.parseDouble(conversaoData.varBid);

        return new TableHistoricoCotacaoItem(data, valor, alta, baixa, variacao);
    }
}


