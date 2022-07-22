package br.com.agendamento.enumerated;

import java.util.Arrays;
import java.util.function.Predicate;

public enum TipoTransferencia {

    A(0, "Transação Tipo A"),
    B(1, "Transação Tipo B"),
    C(2, "Transação tipo C"),
    ERRO(3, "Sem Taxa Aplicável");

    private final int value;
    private final String description;

    TipoTransferencia(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TipoTransferencia fromValue(final int value) {
        return from(type -> type.value == value);
    }

    private static TipoTransferencia from(final Predicate<TipoTransferencia> predicate) {
        return Arrays.asList(values()).stream().filter(predicate).findAny().orElse(null);
    }

    public int getValue() { return this.value; }

}
