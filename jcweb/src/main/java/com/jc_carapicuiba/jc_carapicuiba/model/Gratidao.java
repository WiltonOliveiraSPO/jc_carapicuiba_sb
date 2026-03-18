package com.jc_carapicuiba.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gratidao")
public class Gratidao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_gratidao")
    private Integer codGratidao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_membro", nullable = false)
    private Membro membro;

    @Column(name = "dt_gratidao")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dtGratidao;

    @Column(name = "vl_gratidao", nullable = false, precision = 10, scale = 2)
    private BigDecimal vlGratidao;

    @Column(name = "tipo_gratidao", nullable = false, length = 20)
    private String tipoGratidao;

    @PrePersist
    public void prePersist() {
        if (dtGratidao == null) {
            dtGratidao = LocalDateTime.now();
        }
    }

    public Integer getCodGratidao() {
        return codGratidao;
    }

    public void setCodGratidao(Integer codGratidao) {
        this.codGratidao = codGratidao;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    public LocalDateTime getDtGratidao() {
        return dtGratidao;
    }

    public void setDtGratidao(LocalDateTime dtGratidao) {
        this.dtGratidao = dtGratidao;
    }

    public BigDecimal getVlGratidao() {
        return vlGratidao;
    }

    public void setVlGratidao(BigDecimal vlGratidao) {
        this.vlGratidao = vlGratidao;
    }

    public String getTipoGratidao() {
        return tipoGratidao;
    }

    public void setTipoGratidao(String tipoGratidao) {
        this.tipoGratidao = tipoGratidao;
    }
}
