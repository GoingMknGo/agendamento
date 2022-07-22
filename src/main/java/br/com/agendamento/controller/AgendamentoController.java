package br.com.agendamento.controller;

import br.com.agendamento.entity.Agendamento;
import br.com.agendamento.enumerated.TipoTransferencia;
import br.com.agendamento.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("api/agendamentos")
public class AgendamentoController {

    @Autowired
    AgendamentoRepository agendamentoRepository;

    @PostMapping("/cadastrar")
    public String salvar(@RequestBody Agendamento agendamento) {

        agendamento.setDataAgendamento(LocalDate.now());
        calcularTaxa(agendamento);

        if (agendamento.getTipoTransferencia().getValue() == TipoTransferencia.ERRO.getValue()) {
            return "Sem taxa aplicável para a combinação de datas e valores informados, por favor, consulte a tabela e tente novamente.";
        }

        agendamentoRepository.save(agendamento);
        return "Agendamento realizado com sucesso!";

    }

    @GetMapping
    public List<Agendamento> pesquisar() {
        return agendamentoRepository.findAll();
    }

    private void calcularTaxa(Agendamento agendamento) {

        long dias = ChronoUnit.DAYS.between(agendamento.getDataAgendamento(), agendamento.getDataTransferencia());

        agendamento.setValorTransferencia(arredondaValor(agendamento.getValorTransferencia()));

        double valorTransferencia = agendamento.getValorTransferencia();

        if (dias == 0 && valorTransferencia < 1001) {
            agendamento.setTipoTransferencia(TipoTransferencia.A);
            agendamento.setTaxaTransferencia(3 + arredondaValor(agendamento.getValorTransferencia() * 0.03));
        } else if (dias <= 10 && valorTransferencia > 1001 && valorTransferencia <= 2000) {
            agendamento.setTipoTransferencia(TipoTransferencia.B);
            agendamento.setTaxaTransferencia(12);
        } else if (valorTransferencia > 2000) {
            agendamento.setTipoTransferencia(TipoTransferencia.C);
            if (dias <= 10) {
                agendamento.setTipoTransferencia(TipoTransferencia.ERRO);
            } else if (dias > 10 && dias <= 20) {
                agendamento.setTaxaTransferencia(arredondaValor(agendamento.getValorTransferencia() * 0.082));
            } else if (dias > 20 && dias <= 30) {
                agendamento.setTaxaTransferencia(arredondaValor(agendamento.getValorTransferencia() * 0.069));
            } else if (dias > 30 && dias <= 40) {
                agendamento.setTaxaTransferencia(arredondaValor(agendamento.getValorTransferencia() * 0.047));
            } else if (dias > 40) {
                agendamento.setTaxaTransferencia(arredondaValor(agendamento.getValorTransferencia() * 0.017));
            }
        } else {
            agendamento.setTipoTransferencia(TipoTransferencia.ERRO);
        }

    }

    private Double arredondaValor(double valor) {

        DecimalFormat df = new DecimalFormat("###0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        String doubleFormatado = df.format(valor);

        return Double.parseDouble(doubleFormatado.replace(",", "."));

    }

}
