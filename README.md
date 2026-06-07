<div align="center">

<img src="https://img.shields.io/badge/version-1.4.0-b5451b?style=for-the-badge" alt="Version 1.2.0"/>
<img src="https://img.shields.io/badge/Java-21-b5451b?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
<img src="https://img.shields.io/badge/JavaFX-21-b5451b?style=for-the-badge&logo=java&logoColor=white" alt="JavaFX"/>
<img src="https://img.shields.io/badge/Maven-3.9+-b5451b?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>

# 💱 Coin Tracker

**Acompanhe cotações de moedas em tempo real, converta valores e visualize históricos de até 365 dias — tudo em um único aplicativo desktop.**

</div>

---

## Sobre o Projeto

O **Coin Tracker** é um aplicativo desenvolvido com **Java 21 + JavaFX**, que consome a [AwesomeAPI](https://docs.awesomeapi.com.br/).

Desenvolvido como projeto acadêmico, o sistema oferece uma interface intuitiva com três módulos principais: cotações atualizadas, conversor de moedas e histórico com gráfico de variações.

---

## Funcionalidades

### Cotações em Tempo Real
- Listagem de diversas moedas (fiats e criptomoedas) com valor atual em BRL (Real Brasileiro)
- Exibição de **Alta**, **Baixa** e **Variação** do dia
- Gráfico de variação diária atualizado ao clicar em "Atualizar"
- Indicador visual de carregamento durante a busca dos dados

### Conversor de Moedas  
- Conversão entre qualquer par de moedas disponíveis na API
- Histórico das conversões realizadas na sessão atual
- Cálculo com taxas de câmbio em tempo real

### Histórico de Cotações BRL
- Busca de histórico de até **365 dias** de qualquer moeda
- Tabela com dados de Fechamento, Alta, Baixa e Variação por data
- Gráfico de linha ilustrando a evolução da cotação no período
- Exportador para CSV para salvar os dados na sua máquina

### Configurações
- Gerenciamento da chave de API com opções de salvar e limpar a chave configurada
- Opção para alterar entre os temas Claro e Escuro

---

## Interface

<table>
  <tr>
    <td align="center"><b>Cotações</b></td>
    <td align="center"><b>Conversor</b></td>
  </tr>
  <tr>
    <td><img src="./images/cotacoes.png" width="400"/></td>
    <td><img src="./images/conversor.png" width="400"/></td>
  </tr>
  <tr>
    <td align="center"><b>Histórico de Cotações</b></td>
    <td align="center"><b>Chave da API</b></td>
  </tr>
  <tr>
    <td><img src="./images/historico-de-cotacoes.png" width="400"/></td>
    <td><img src="./images/chave-API.png" width="400"/></td>
  </tr>
</table>

---

## Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| Java 21 | Linguagem principal da aplicação |
| JavaFX | Framework para a interface gráfica desktop |
| Maven | Gerenciamento de dependências e build |
| AwesomeAPI | API pública de cotações de moedas e criptos |

---

## Como Executar

### Pré-requisitos

- Ter uma chave de API da [AwesomeAPI](https://docs.awesomeapi.com.br/) (Opcional/Gratuito)

### Windows

1. Baixe o instalador `.exe` na seção [Releases](../../releases)
2. Execute o instalador e siga as instruções
3. Abra o **Coin Tracker** pelo atalho criado
4. Vá em **Configurações**, insira sua chave de API e clique em **Salvar**

### Linux (RPM)

1. Baixe o pacote `.rpm` na seção [Releases](../../releases)

2. Instale o pacote:
  ```bash
  sudo dnf install ./cointracker-1.4.0.rpm
  ```
3. Execute o aplicativo pelo menu

### Linux (app-image)

Se preferir executar sem instalar, ou utilizar em distribuições sem suporte a RPM:

1. Baixe o pacote `.tar.gz` na seção [Releases](../../releases)

2. Decompacte o pacote

  ````bash
  tar -xzf CoinTracker-1.4.0-linux-x64.tar.gz
  ````

4. Execute o arquivo binário

  ````bash
  cd CoinTracker/bin
  ./cointracker
  ````


### Código-Fonte

Se preferir compilar manualmente:

1. Clone o repositório

```bash
git clone https://github.com/filipemartinsdev/coin-tracker.git
```

2. Compile e execute com Maven

````bash
cd coin-tracker
mvn clean javafx:run
````


---

## Configuração da API

O Coin Tracker utiliza a **AwesomeAPI** para obter as cotações em tempo real.

1. Acesse [docs.awesomeapi.com.br](https://docs.awesomeapi.com.br/) e obtenha sua chave gratuita
2. Abra o aplicativo, clique em **Configurações** (canto superior)
3. Cole sua chave no campo **Chave da API** e clique em **Salvar**

---

## Conventional Commits

Este projeto adota o padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/) para manter um histórico de commits claro e organizado.

### Formato

```
<tipo>(escopo opcional): <descrição curta>
```

---

## Equipe

<table>
  <tr>
    <td align="center">
      <b>Thiago Monteiro</b><br/>
      <sub>Líder de Documentação & Dev</sub><br/>
      <a href="https://github.com/ThiHughes">@thiago-monteiro</a>
    </td>
    <td align="center">
      <b>Filipe Martins</b><br/>
      <sub>Product Owner, Líder Técnico & Dev</sub><br/>
      <a href="https://github.com/filipemartinsdev">@filipe-martins</a>
    </td>
  </tr>
</table>

---

<div align="center">
  <sub>Desenvolvido com Java e muito ☕</sub>
</div>
