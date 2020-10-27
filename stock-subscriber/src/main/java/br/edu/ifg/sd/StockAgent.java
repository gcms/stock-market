package br.edu.ifg.sd;

import org.apache.kafka.clients.producer.Producer;

// Sugestão de classe para tratar as ordens de compra e venda a partir do stocksubscriber
public class StockAgent {
    private Producer<String, String> stockBuy;
    private Producer<String, String> stockSell;


    public void buy(String clientId, StockSubscriber.Ordem ordem) {

    }

    public void sell(String clientId, StockSubscriber.Ordem ordem) {

    }
}
