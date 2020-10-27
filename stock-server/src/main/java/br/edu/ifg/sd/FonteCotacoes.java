package br.edu.ifg.sd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FonteCotacoes {
    public static FonteCotacoes createDefault() {
        return new FonteCotacoes(new String[]{"BBDC4", "VALE4", "ABEV3", "PETR4", "VALE3"},
                new Double[]{20.0, 62.0, 15.0, 20.0, 62.0});
    }

    private final Map<String, Double> cotacoes = new LinkedHashMap<>();

    public FonteCotacoes(String[] codigos, Double[] valores) {
        for (int i = 0; i < codigos.length; i++)
            cotacoes.put(codigos[i], valores[i]);
    }


    public double atualizaCotacao(double cotacao, double fatorMudanca) {
        return cotacao * (1 + (Math.random() - 0.5) * fatorMudanca);
    }

    public void atualizaCotacoes(double fatorMudanca) {
        for (String acao : cotacoes.keySet()) {
            cotacoes.put(acao, atualizaCotacao(cotacoes.get(acao), fatorMudanca));
        }
    }

    public Map<String, Double> getAtualizacoes() {
        atualizaCotacoes(0.1); // alteração de 0 a 10%

        // atualiza apenas parte das cotações
        return cotacoes.entrySet().stream()
                .filter(it -> Math.random() < 0.5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
