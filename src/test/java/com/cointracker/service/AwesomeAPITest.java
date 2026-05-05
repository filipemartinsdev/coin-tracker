package com.cointracker.service;

import com.cointracker.dto.ConversaoResponse;
import com.cointracker.excetion.MoedaNaoEncontradaException;
import com.cointracker.excetion.RequisicaoFalhouException;
import com.cointracker.model.Cotacao;
import com.cointracker.model.Moeda;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AwesomeAPITest {
    @Mock private Gson gson;

    @Mock private HttpClient client;

    @InjectMocks
    private AwesomeAPI awesomeAPI;

    @Test @DisplayName("Se moeda não existe deve lançar MoedaNaoEncontradaException")
    void converterTestCase1() throws Exception{
        HttpResponse response = Mockito.mock(HttpResponse.class);

        Mockito.when(response.statusCode())
                .thenReturn(404);

        Mockito.when(client.send(any(), any()))
                .thenReturn(response);

        assertThrows(MoedaNaoEncontradaException.class, () -> {
            awesomeAPI.converter("ABC", "ABC", 2.0);
        });
    }

    @Test
    @DisplayName("Buscar moedas deve retornar lista de moedas")
    void buscarMoedasTestCase1() throws Exception {
        HttpResponse response = Mockito.mock(HttpResponse.class);
        Map<String, String> mockMap = Map.of("USD", "Dólar Americano", "EUR", "Euro");

        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        Mockito.when(response.body())
                .thenReturn("{\"USD\":\"Dólar Americano\",\"EUR\":\"Euro\"}");
        Mockito.when(gson.fromJson(Mockito.anyString(), Mockito.any(Type.class)))
                .thenReturn(mockMap);

        List<Moeda> result = awesomeAPI.buscarMoedas();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Buscar histórico de cotação deve retornar lista de cotações")
    void buscarHistoricoCotacaoTestCase1() throws Exception {
        HttpResponse response = Mockito.mock(HttpResponse.class);
        ConversaoResponse.ConversaoData data = new ConversaoResponse.ConversaoData();
        data.code = "USD";
        data.name = "Dólar Americano/Real Brasileiro";
        data.bid = "5.00";
        data.high = "5.10";
        data.low = "4.90";
        data.varBid = "0.10";
        data.timestamp = "1609459200";
        List<ConversaoResponse.ConversaoData> mockList = List.of(data);

        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        Mockito.when(response.body())
                .thenReturn("[{\"code\":\"USD\",\"name\":\"Dólar Americano/Real Brasileiro\",\"bid\":\"5.00\",\"high\":\"5.10\",\"low\":\"4.90\",\"varBid\":\"0.10\",\"timestamp\":\"1609459200\"}]");
        Mockito.when(gson.fromJson(Mockito.anyString(), Mockito.any(Type.class)))
                .thenReturn(mockList);

        List<Cotacao> result = awesomeAPI.buscarHistoricoCotacao("USD-BRL", 1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Buscar histórico de cotação deve lançar RequisicaoFalhouException em caso de erro")
    void buscarHistoricoCotacaoTestCase2() throws Exception {
        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Erro de conexão"));

        assertThrows(RequisicaoFalhouException.class, () -> {
            awesomeAPI.buscarHistoricoCotacao("USD-BRL", 1);
        });
    }

    @Test
    @DisplayName("Buscar conversoes disponíveis de deve retornar lista de códigos")
    void buscarConversoesDisponiveisDeTestCase1() throws Exception {
        HttpResponse response = Mockito.mock(HttpResponse.class);

        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        Mockito.when(response.body())
                .thenReturn("{\"USD-BRL\":\"Dólar Americano/Real\",\"EUR-BRL\":\"Euro/Real\"}");
        Mockito.when(gson.fromJson(Mockito.anyString(), Mockito.any(Type.class)))
                .thenReturn(Map.of("USD-BRL", "Dólar Americano/Real", "EUR-BRL", "Euro/Real"));

        List<String> result = awesomeAPI.buscarConversoesDisponiveisDe("BRL");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Buscar conversoes disponíveis de deve lançar RequisicaoFalhouException em caso de erro")
    void buscarConversoesDisponiveisDeTestCase2() throws Exception {
        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Erro de conexão"));

        assertThrows(RequisicaoFalhouException.class, () -> {
            awesomeAPI.buscarConversoesDisponiveisDe("BRL");
        });
    }

    @Test
    @DisplayName("Buscar conversoes disponíveis para deve retornar lista de códigos")
    void buscarConversoesDisponiveisParaTestCase1() throws Exception {
        HttpResponse response = Mockito.mock(HttpResponse.class);

        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        Mockito.when(response.body())
                .thenReturn("{\"BRL-USD\":\"Real/Dólar Americano\",\"BRL-EUR\":\"Real/Euro\"}");
        Mockito.when(gson.fromJson(Mockito.anyString(), Mockito.any(Type.class)))
                .thenReturn(Map.of("BRL-USD", "Real/Dólar Americano", "BRL-EUR", "Real/Euro"));

        List<String> result = awesomeAPI.buscarConversoesDisponiveisPara("BRL");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Buscar conversoes disponíveis para deve lançar RequisicaoFalhouException em caso de erro")
    void buscarConversoesDisponiveisParaTestCase2() throws Exception {
        Mockito.when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Erro de conexão"));

        assertThrows(RequisicaoFalhouException.class, () -> {
            awesomeAPI.buscarConversoesDisponiveisPara("BRL");
        });
    }


}