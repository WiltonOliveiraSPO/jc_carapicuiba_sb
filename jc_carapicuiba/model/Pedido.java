package com.jc_carapicuiba.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_pedidos")
    private Integer codPedidos;

    public Integer getCodPedidos() {
		return codPedidos;
	}

	public void setCodPedidos(Integer codPedidos) {
		this.codPedidos = codPedidos;
	}

	public LocalDate getDtPedidos() {
		return dtPedidos;
	}

	public void setDtPedidos(LocalDate dtPedidos) {
		this.dtPedidos = dtPedidos;
	}

	public Integer getQtdeAgradecimento() {
		return qtdeAgradecimento;
	}

	public void setQtdeAgradecimento(Integer qtdeAgradecimento) {
		this.qtdeAgradecimento = qtdeAgradecimento;
	}

	public Integer getQtdeGraca() {
		return qtdeGraca;
	}

	public void setQtdeGraca(Integer qtdeGraca) {
		this.qtdeGraca = qtdeGraca;
	}

	public Integer getQtdeElevacao() {
		return qtdeElevacao;
	}

	public void setQtdeElevacao(Integer qtdeElevacao) {
		this.qtdeElevacao = qtdeElevacao;
	}

	public Integer getQtdeAnivFalec() {
		return qtdeAnivFalec;
	}

	public void setQtdeAnivFalec(Integer qtdeAnivFalec) {
		this.qtdeAnivFalec = qtdeAnivFalec;
	}

	@Column(name = "dt_pedidos", nullable = false)
    private LocalDate dtPedidos;

    @Column(name = "qtde_agradecimento", nullable = false)
    private Integer qtdeAgradecimento;

    @Column(name = "qtde_graca", nullable = false)
    private Integer qtdeGraca;

    @Column(name = "qtde_elevacao", nullable = false)
    private Integer qtdeElevacao;

    @Column(name = "qtde_anivfalec", nullable = false)
    private Integer qtdeAnivFalec;

    // getters e setters
}