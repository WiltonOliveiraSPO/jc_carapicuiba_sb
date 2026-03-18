package com.jc_carapicuiba.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "membro")
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_membro")
    private Integer codMembro;

    public Integer getCodMembro() {
		return codMembro;
	}

	public void setCodMembro(Integer codMembro) {
		this.codMembro = codMembro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDateTime getDataOutorga() {
		return dataOutorga;
	}

	public void setDataOutorga(LocalDateTime dataOutorga) {
		this.dataOutorga = dataOutorga;
	}

	@Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String endereco;

    @Column(length = 15)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(length = 14, unique = true)
    private String cpf;

    @Column(name = "data_outorga")
    private LocalDateTime dataOutorga;

    @PrePersist
    public void prePersist() {
        if (dataOutorga == null) {
            dataOutorga = LocalDateTime.now();
        }
    }

    // getters e setters
}