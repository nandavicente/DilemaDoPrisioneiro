/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.mulhermineira;

import io.github.guisso.meleeinterface.IPlayer;
import io.github.guisso.meleeinterface.Decision;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author nandi
 */
public class MulherMineira
        implements IPlayer {
    
    // Lista que guarda o histórico das decisões do oponente
    private final List<Decision> historico = new ArrayList<>();

    private int cooperacoes = 0;
    private int defections = 0;


    @Override
    public String getDeveloperName() {
       return "Maria Fernanda Vicente";
    }

    @Override
    public String getEngineName() {
        return "Mulher Mineira";
    }

    @Override
    public Decision makeMyMove(Decision previousMove) {
        if (previousMove == null || previousMove == Decision.NONE) {
            historico.clear();
            cooperacoes = 0;
            defections = 0;
            return Decision.COOPERATE; // Começo educada
        }

        historico.add(previousMove);

        // Conto cooperacoes e traições totais
        if (previousMove == Decision.COOPERATE) {
            cooperacoes++;
        } else if (previousMove == Decision.DEFECT) {
            defections++;
        }

        int total = cooperacoes + defections;
        double taxaCooperacao = total > 0 ? (double) cooperacoes / total : 1.0;

        // Detecto se o oponente parece aleatório (variações demais nas últimas 5 jogadas)
        boolean comportamentoAleatorio = false;
        if (historico.size() >= 5) {
            List<Decision> ultimas5 = historico.subList(historico.size() - 5, historico.size());
            long diferentes = ultimas5.stream().distinct().count();
            comportamentoAleatorio = (diferentes >= 3);
        }

        // Detecto padrão de perdão (3 cooperadas seguidas)
        boolean oponenteMudou = false;
        if (historico.size() >= 3) {
            List<Decision> ultimas3 = historico.subList(historico.size() - 3, historico.size());
            oponenteMudou = ultimas3.stream().allMatch(d -> d == Decision.COOPERATE);
        }

        // Ações de acordo com as observações
        if (taxaCooperacao > 0.8) {
            return Decision.COOPERATE; // Muito confiável
        } else if (taxaCooperacao < 0.3) {
            return Decision.DEFECT; // Muito traíra
        } else if (comportamentoAleatorio) {
            // Alterno para não ser explorada
            return historico.size() % 2 == 0 ? Decision.COOPERATE : Decision.DEFECT;
        } else if (oponenteMudou) {
            return Decision.COOPERATE; // Dar segunda chance
        }

        return Decision.COOPERATE; // padrão: gentil
    }
}
   