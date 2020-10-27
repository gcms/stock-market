package br.edu.ifg.sd;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import sun.misc.Signal;

import java.util.Map;
import java.util.Properties;

public class StockServer implements Runnable {

    public static void main(String[] args) {
        String kafkaServer = args.length > 0 ? args[0] : "localhost:9092";
        StockServer server = new StockServer(kafkaServer);
        server.run();
    }


    private final String kafkaServer;
    private boolean stop;

    public StockServer(String kafkaServer) {
        this.kafkaServer = kafkaServer;
    }

    public void run() {
        // https://docs.confluent.io/current/installation/configuration/producer-configs.html
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServer);
        props.put("auto.create.topics.enable", true); // cria tópico se não existe
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // serializador de string
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        Signal.handle(new Signal("HUP"), signal -> {
            stop();
        });

        while (!stop) {
            Map<String, Double> cotacoes = obtemAtualizacoes();

            for (Map.Entry<String, Double> e : cotacoes.entrySet()) {
                String acao = e.getKey();
                Double cotacao = e.getValue();

                System.out.printf("%s: %.2f\n", acao, cotacao);
                ProducerRecord<String, String> rec = new ProducerRecord<>("stock-market", acao, cotacao.toString());
                producer.send(rec);
            }


            try {
                Thread.sleep((long) (Math.random() * 10 * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        producer.close();
    }

    public void stop() {
        stop = true;
    }

    private final FonteCotacoes dadosCotacoes = FonteCotacoes.createDefault();
    private Map<String, Double> obtemAtualizacoes() {
        return dadosCotacoes.getAtualizacoes();
    }
}
