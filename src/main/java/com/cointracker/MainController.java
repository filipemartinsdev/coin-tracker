package com.cointracker;

import com.cointracker.dto.TableHistoricoConversaoItem;
import com.cointracker.model.Cotacao;
import com.cointracker.model.Moeda;
import com.cointracker.service.AwesomeAPI;
import com.cointracker.service.CoinClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MainController {
    private final CoinClient coinClient = new AwesomeAPI();

    @FXML
    private Button btnRefresh;

    @FXML
    private ProgressBar progressBarCotacao;

    @FXML
    private TableView<Cotacao> tableCotacao;

    @FXML
    private TableColumn<Cotacao, String> colCotacaoNome;

    @FXML
    private TableColumn<Cotacao, String> colCotacaoCodigo;

    @FXML
    private TableColumn<Cotacao, Double> colCotacaoValor;

    @FXML
    private TableColumn<Cotacao, Double> colCotacaoAlta;

    @FXML
    private TableColumn<Cotacao, Double> colCotacaoBaixa;

    @FXML
    private TableColumn<Cotacao, Double> colCotacaoVariacao;

    private final ObservableList<Cotacao> tableCotacaoItens = FXCollections.observableArrayList();


    @FXML
    private Button btnCalcularConversao;

    @FXML
    private ComboBox<String> cBoxMoeda1Conversor;

    @FXML
    private ComboBox<String> cBoxMoeda2Conversor;

    @FXML
    private BarChart<String, Double> chartVariacaoHoje;

    @FXML
    private TextField inputPesquisaCotacao;

    @FXML
    private Spinner<Double> spinQtdConversao;

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
    private TableView<Cotacao> tableHistoricoCotacao;

    @FXML
    private TableColumn<Cotacao, LocalDate> colDataHistoricoCotacao;

    @FXML
    private TableColumn<Cotacao, Double> colFechamentoHistoricoCotacao;

    @FXML
    private TableColumn<Cotacao, Double> colVariacaoHistoricoCotacao;

    @FXML
    private TableColumn<Cotacao, Double> colAltaHistoricoCotacao;

    @FXML
    private TableColumn<Cotacao, Double> colBaixaHistoricoCotacao;

    private final ObservableList<Cotacao> tableHistoricoCotacaoItens = FXCollections.observableArrayList();

    @FXML
    private AreaChart<String, Double> chartHistoricoCotacao;

    @FXML
    private NumberAxis yAxisChartHistoricoCotacao;

    @FXML
    void initialize() {
        Platform.runLater(this::setupTableCotacao);
        Platform.runLater(this::setupCBoxMoeda1Conversor);
        Platform.runLater(this::setupTableHistoricoConversao);
        Platform.runLater(this::setupSpinQtdConversao);
        Platform.runLater(this::setupSpinDiasHistoricoCotacao);
        Platform.runLater(this::setupCBoxMoedaHistorioCotacao);
        Platform.runLater(this::setupTableHistoricoCotacao);
        Platform.runLater(this::setupAreaChartHistoricoCotacao);
    }

    private void setupTableCotacao(){
        tableCotacao.setItems(tableCotacaoItens);

        setupColCotacaoNome();
        setupColCotacaoCodigo();
        setupColCotacaoValor();
        setupColCotacaoAlta();
        setupColCotacaoBaixa();
        setupColCotacaoVariacao();

        refreshCotacoes(new ActionEvent());
    }

    private void setupColCotacaoNome(){
        colCotacaoNome.setCellValueFactory(new PropertyValueFactory<>("nomeMoeda"));
    };

    private void setupColCotacaoCodigo(){
        colCotacaoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoMoeda"));
    }

    private void setupColCotacaoValor(){
        colCotacaoValor.setCellValueFactory(new PropertyValueFactory<>("fechamento"));

        colCotacaoValor.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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

    private void setupColCotacaoAlta(){
        colCotacaoAlta.setCellValueFactory(new PropertyValueFactory<>("alta"));

        colCotacaoAlta.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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

    private void setupColCotacaoBaixa(){
        colCotacaoBaixa.setCellValueFactory(new PropertyValueFactory<>("baixa"));

        colCotacaoBaixa.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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

    private void setupColCotacaoVariacao(){
        colCotacaoVariacao.setCellValueFactory(new PropertyValueFactory<>("variacao"));

        colCotacaoVariacao.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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


    private void setupSpinQtdConversao() {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 999_999_999, 1);

        spinQtdConversao.setValueFactory(valueFactory);
        spinQtdConversao.setEditable(true);
    }

    private void setupSpinDiasHistoricoCotacao() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 365, 1);

        spinDiasHistoricoCotacao.setValueFactory(valueFactory);
        spinDiasHistoricoCotacao.setEditable(true);
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
                return coinClient.buscarMoedas().stream()
                        .map(Moeda::codigo)
                        .toList();
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
                return coinClient.buscarConversoesDisponiveisPara("BRL");
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

        colQuantidadeConversao.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colDeConversao.setCellValueFactory(new PropertyValueFactory<>("de"));
        colParaConversao.setCellValueFactory(new PropertyValueFactory<>("para"));
        colResultadoConversao.setCellValueFactory(new PropertyValueFactory<>("resultado"));
    }

    private void setupTableHistoricoCotacao() {
        tableHistoricoCotacao.setItems(tableHistoricoCotacaoItens);
        setupColVariacaoHistoricoCotacao();
        setupColDataHistoricoCotacao();
        setupColFechamentoHistoricoCotacao();
        setupColAltaHistoricoCotacao();
        setupColBaixaHistoricoCotacao();
    }

    private void setupColVariacaoHistoricoCotacao() {
        colVariacaoHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("variacao"));

        colVariacaoHistoricoCotacao.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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

        colDataHistoricoCotacao.setCellFactory(column -> new TableCell<Cotacao, LocalDate>() {
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

    private void setupColFechamentoHistoricoCotacao() {
        colFechamentoHistoricoCotacao.setCellValueFactory(new PropertyValueFactory<>("fechamento"));

        colFechamentoHistoricoCotacao.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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
        colAltaHistoricoCotacao.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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
        colBaixaHistoricoCotacao.setCellFactory(column -> new TableCell<Cotacao, Double>() {
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

            @Override
            public String toString(Number number) {
                return format.format(number);
            }

            @Override
            public Number fromString(String string) {
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
    void atualizarGraficoCotacao(){
        chartVariacaoHoje.getData().clear();

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Cotacao cotacaoSelecionada = tableCotacao.getSelectionModel().getSelectedItem();

        series.getData().add(new XYChart.Data<>("baixa", cotacaoSelecionada.getBaixa()));
        series.getData().add(new XYChart.Data<>("atual", cotacaoSelecionada.getFechamento()));
        series.getData().add(new XYChart.Data<>("alta", cotacaoSelecionada.getAlta()));

        chartVariacaoHoje.getData().add(series);
        applyChartColors();
        applyTooltipGraficoCotacao(series);
    }

    void applyTooltipGraficoCotacao(XYChart.Series<String, Double> series){
        for (XYChart.Data<String, Double> data : series.getData()) {
            Double valor = data.getYValue();

            Tooltip tooltip = new Tooltip("Valor: R$ "+valor);

            tooltip.setShowDelay(javafx.util.Duration.ZERO);

            Tooltip.install(data.getNode(), tooltip);

            data.getNode().setOnMouseEntered(mouseEvent -> data.getNode().setCursor(Cursor.HAND));
        }
    }

    @FXML
    void clickCBoxMoeda2(){
        cBoxMoeda2Conversor.getItems().clear();
        String moeda1 = cBoxMoeda1Conversor.getSelectionModel().getSelectedItem();

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return coinClient.buscarConversoesDisponiveisDe(moeda1);
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
        Double qtd = spinQtdConversao.getValue();
        String moeda1 = cBoxMoeda1Conversor.getSelectionModel().getSelectedItem();
        String moeda2 = cBoxMoeda2Conversor.getSelectionModel().getSelectedItem();

        Task<Double> task = new Task<>() {
            @Override
            protected Double call() throws Exception {
                return coinClient.converter(moeda1, moeda2, qtd);
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
    void refreshCotacoes(ActionEvent event) {
        progressBarCotacao.setOpacity(1);

        tableCotacaoItens.clear();

        Task<List<Cotacao>> task = new Task<>() {
            @Override
            protected List<Cotacao> call() throws Exception {
                return coinClient.buscarCotacaoMoedas();
            }
        };

        task.setOnSucceeded(e -> {
            tableCotacaoItens.addAll(task.getValue());
            progressBarCotacao.setOpacity(0);
        });

        task.setOnFailed(e -> {
            System.out.println("Deu algum erro ai zé");
            progressBarCotacao.setOpacity(0);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    void buscarHistoricoCotacao(ActionEvent event) {
        tableHistoricoCotacaoItens.clear();

        String moeda = cBoxMoedaHistoricoCotacao.getSelectionModel().getSelectedItem();
        Integer qtd = spinDiasHistoricoCotacao.getValue();

        Task<List<Cotacao>> task = new Task<>() {
            @Override
            protected List<Cotacao> call() throws Exception {
                return coinClient.buscarHistoricoCotacao(moeda, qtd);
            }
        };

        task.setOnSucceeded(e -> {
            tableHistoricoCotacaoItens.addAll(task.getValue());

            chartHistoricoCotacao.getData().clear();

            XYChart.Series<String, Double> series = new XYChart.Series<>();

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for(int i = tableHistoricoCotacaoItens.size()-1; i >= 0; i--){
                var item = tableHistoricoCotacaoItens.get(i);
                String dataFormatada = item.getData().format(formatador);
                series.getData().add(new XYChart.Data<>(dataFormatada, item.getFechamento()));
            }

            chartHistoricoCotacao.getData().add(series);

            for (XYChart.Data<String, Double> data : series.getData()) {
                Tooltip tooltip = new Tooltip("Data: " + data.getXValue() + "\nFechamento: " + data.getYValue());

                tooltip.setShowDelay(javafx.util.Duration.ZERO);

                Tooltip.install(data.getNode(), tooltip);

                data.getNode().setOnMouseEntered(mouseEvent -> data.getNode().setCursor(Cursor.HAND));
            }
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}


