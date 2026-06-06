package com.cointracker;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.cointracker.dto.TableHistoricoConversaoItem;
import com.cointracker.exception.ClientMoedaException;
import com.cointracker.model.Cotacao;
import com.cointracker.model.Moeda;
import com.cointracker.service.AwesomeAPI;
import com.cointracker.service.CoinClient;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class MainController {
    private final CoinClient coinClient = new AwesomeAPI(
            new Gson(), HttpClient.newHttpClient()
    );

    @FXML
    private MenuItem menuItemChaveAPI;

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
    private MenuItem temaClaroBtn;

    @FXML
    private MenuItem temaEscuroBtn;

    @FXML
    private Text conversorInfoValor;

    @FXML
    void initialize() {
        Platform.runLater(this::setupTableCotacao);
        Platform.runLater(this::setupTableHistoricoConversao);
        Platform.runLater(this::setupSpinQtdConversao);
        Platform.runLater(this::setupSpinDiasHistoricoCotacao);
        Platform.runLater(this::setupTableHistoricoCotacao);
        Platform.runLater(this::setupAreaChartHistoricoCotacao);
        Platform.runLater(this::setupConversorInfoValor);
        Platform.runLater(this::setupConfirmacaoDeSaida);
    }

    private void setupConversorInfoValor() {
        Tooltip tooltip = new Tooltip("Números decimais utilizando \".\"\nExemplo: 50.99");

        tooltip.setShowDelay(javafx.util.Duration.millis(500));

        Tooltip.install(conversorInfoValor, tooltip);
    }

    private void setupTableCotacao(){
        tableCotacao.setCursor(Cursor.HAND);

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

    @FXML
    private void setupCBoxMoeda1Conversor() {
        cBoxMoeda1Conversor.getItems().clear();

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                try {
                    return coinClient.buscarMoedas().stream()
                            .map(Moeda::codigo)
                            .toList();
                } catch (ClientMoedaException e){
                    return new ArrayList<>();
                }
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

    @FXML
    private void setupCBoxMoedaHistorioCotacao() {
        cBoxMoedaHistoricoCotacao.getItems().clear();

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
        tableCotacao.setCursor(Cursor.HAND);

        tableHistoricoConversao.setItems(tableHistoricoConversaoItens);

        colQuantidadeConversao.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colDeConversao.setCellValueFactory(new PropertyValueFactory<>("de"));
        colParaConversao.setCellValueFactory(new PropertyValueFactory<>("para"));
        colResultadoConversao.setCellValueFactory(new PropertyValueFactory<>("resultado"));
    }

    private void setupTableHistoricoCotacao() {
        tableCotacao.setCursor(Cursor.HAND);

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
        chartHistoricoCotacao.setHorizontalGridLinesVisible(false);
        chartHistoricoCotacao.setVerticalGridLinesVisible(false);

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

    private void setupConfirmacaoDeSaida(){
        Platform.runLater(()->{
            Stage stage = (Stage) tableCotacao.getScene().getWindow();

            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação");
                alert.setHeaderText("Você realmente deseja sair?");
                alert.initOwner(stage);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                    stage.close();
                else event.consume();
            });
        });
    }

    // finish setup ☝

    @FXML
    public void ativarTemaClaro(){
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    @FXML
    public void ativarTemaEscuro(){
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    @FXML
    public void limparConversoes(){
        tableHistoricoConversaoItens.clear();
    }

    @FXML
    public void exportarCSV(){
        if (tableHistoricoCotacaoItens.isEmpty()) {
            mostrarErro("Operação inválida", "Tabela de histórico vazia", "Consulte o histórico de uma moeda antes de exportar.");
            return;
        }

        Path path = escolherPathParaExportarCSV();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                escreverCSV(path);
                return null;
            }
        };

        task.setOnFailed(event -> {
            mostrarErro("Erro", "Erro ao exportar CSV", "");
        });

        task.setOnSucceeded(event -> {
            mostrarAlerta("Exportação concluída", "CSV exportado com sucesso", "");
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void escreverCSV(Path path) throws IOException {
        StringBuilder csv = new StringBuilder().append("Data;Fechamento;Alta;Baixa;Variacao\n");

        for(Cotacao cotacao : tableHistoricoCotacaoItens) {
            csv.append(cotacao.getData()).append(";")
                    .append(cotacao.getFechamento()).append(";")
                    .append(cotacao.getAlta()).append(";")
                    .append(cotacao.getBaixa()).append(";")
                    .append(cotacao.getVariacao()).append("\n");
        }
        Files.writeString(path, csv);
    }

    private Path escolherPathParaExportarCSV(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Escolha o destino");

        chooser.setInitialFileName("historico.csv");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = chooser.showSaveDialog(tableCotacao.getScene().getWindow());

        Path path = file.toPath();

        if (!path.toString().toLowerCase().endsWith(".csv")) {
            path = Path.of(path + ".csv");
        }

        return path;
    }

    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void mostrarErro(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void abrirConfiguracaoChaveAPI() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CoinTrackerApplication.class.getResource("chave-api.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setResizable(false);
        stage.setTitle("Coin Tracker");
        stage.setScene(scene);
        stage.show();
    }

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
            tableHistoricoConversaoItens.add(0, new TableHistoricoConversaoItem(
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


