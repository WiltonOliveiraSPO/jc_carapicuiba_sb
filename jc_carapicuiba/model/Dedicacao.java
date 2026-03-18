package com.jc_carapicuiba.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dedicacao")
public class Dedicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_dedicacao")
    private Integer codDedicacao;

    @Column(name = "dt_dedicacao", nullable = false)
    private LocalDate dtDedicacao;

    @Column(name = "qtde_membros", nullable = false)
    private Integer qtdeMembros;

    @Column(name = "qtde_frequentadores", nullable = false)
    private Integer qtdeFrequentadores;

    @Column(name = "qtde_primvez", nullable = false)
    private Integer qtdePrimVez;

    // GETTERS E SETTERS

    public Integer getCodDedicacao() {
        return codDedicacao;
    }

    public void setCodDedicacao(Integer codDedicacao) {
        this.codDedicacao = codDedicacao;
    }

    public LocalDate getDtDedicacao() {
        return dtDedicacao;
    }

    public void setDtDedicacao(LocalDate dtDedicacao) {
        this.dtDedicacao = dtDedicacao;
    }

    public Integer getQtdeMembros() {
        return qtdeMembros;
    }

    public void setQtdeMembros(Integer qtdeMembros) {
        this.qtdeMembros = qtdeMembros;
    }

    public Integer getQtdeFrequentadores() {
        return qtdeFrequentadores;
    }

    public void setQtdeFrequentadores(Integer qtdeFrequentadores) {
        this.qtdeFrequentadores = qtdeFrequentadores;
    }

    public Integer getQtdePrimVez() {
        return qtdePrimVez;
    }

    public void setQtdePrimVez(Integer qtdePrimVez) {
        this.qtdePrimVez = qtdePrimVez;
    }
}