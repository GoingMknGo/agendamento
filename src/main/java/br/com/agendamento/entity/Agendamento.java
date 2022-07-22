package br.com.agendamento.entity;

import br.com.agendamento.enumerated.TipoTransferencia;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Agendamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;

    private int contaOrigem;

    private int contaDestino;

    private LocalDate dataTransferencia;

    private LocalDate dataAgendamento;

    private double valorTransferencia;

    private double taxaTransferencia;

    @Enumerated(value = EnumType.STRING)
    private TipoTransferencia tipoTransferencia;


}
