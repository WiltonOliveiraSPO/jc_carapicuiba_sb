package com.jc_carapicuiba.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "primvez")
public class PrimeiraVez {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_primvez")
    private Integer codPrimVez;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_membro", nullable = false)
    private Membro membro;

    @Column(nullable = false, length = 100)
    private String endereco;

    @Column(length = 15)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(name = "data_primvez")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataPrimVez;

    @PrePersist
    public void prePersist() {
        if (dataPrimVez == null) {
            dataPrimVez = LocalDateTime.now();
        }
    }

    public Integer getCodPrimVez() {
        return codPrimVez;
    }

    public void setCodPrimVez(Integer codPrimVez) {
        this.codPrimVez = codPrimVez;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataPrimVez() {
        return dataPrimVez;
    }

    public void setDataPrimVez(LocalDateTime dataPrimVez) {
        this.dataPrimVez = dataPrimVez;
    }
}
