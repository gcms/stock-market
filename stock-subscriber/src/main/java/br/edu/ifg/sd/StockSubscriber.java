package br.edu.ifg.sd;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class StockSubscriber implements Runnable {


    public static void main(String[] args) {
        // StockSubscriber <ID_CLIENTE> <ACAO> <ORDEM> <QTD> <PREÇO>
        if (args.length < 5) {
            System.err.println("Usage: StockSubscriber <ID_CLIENTE> <AÇÃO> <ORDEM> <QTD> <PREÇO>");
            System.exit(1);
            return;
        }

        Ordem ordem = new Ordem(args[1], Ordem.TipoOrdem.valueOf(args[2]), Integer.parseInt(args[3]), new BigDecimal(args[4]));

        StockSubscriber subs = new StockSubscriber("localhost:9092", args[0], ordem);
        subs.run();
    }

    private final String kafkaServer;
    private final String idCliente;
    private final Ordem ordem;

    private boolean stop;


    public StockSubscriber(String kafkaServer, String idCliente, Ordem ordem) {
        this.kafkaServer = kafkaServer;
        this.idCliente = idCliente;
        this.ordem = ordem;
    }

    public void stop() {
        stop = true;
    }

    public void run() {
        //https://docs.confluent.io/current/installation/configuration/consumer-configs.html
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", idCliente);
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("stock-market"));

        while (!stop) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("%s: %.2f\n", record.key(), Double.parseDouble(record.value()));
            }

        }

    }

    static class Ordem {
        enum TipoOrdem {
            COMPRA, VENDA;
        }

        private final TipoOrdem ordem;
        private final String acao;
        private final int qtd;
        private final BigDecimal preco;

        public Ordem(String acao, TipoOrdem ordem, int qtd, BigDecimal preco) {
            this.acao = acao;
            this.ordem = ordem;
            this.qtd = qtd;
            this.preco = preco;
        }

        public TipoOrdem getOrdem() {
            return ordem;
        }

        public String getAcao() {
            return acao;
        }

        public int getQtd() {
            return qtd;
        }

        public BigDecimal getPreco() {
            return preco;
        }
    }
}