package br.com.senai.shoppinglist.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemCompra implements Serializable {
    private static final long serialVersionUID = 1L;

    private ItemLista itemLista;
    private BigDecimal precoUnitario;
    private double quantidadeComprada;
    private boolean emFalta;

    public ItemCompra(ItemLista itemLista) {
        this.itemLista = itemLista;
        this.precoUnitario = BigDecimal.ZERO;
        this.quantidadeComprada = itemLista.getQuantidade();
        this.emFalta = false;
    }

    public ItemLista getItemLista() {
        return itemLista;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        if (precoUnitario.compareTo(BigDecimal.ZERO) == 0) {
            this.emFalta = true;
            this.quantidadeComprada = 0;
        } else {
            this.emFalta = false;
        }
    }

    public double getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(double quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }

    public boolean isEmFalta() {
        return emFalta;
    }

    public void setEmFalta(boolean emFalta) {
        this.emFalta = emFalta;
        if (emFalta) {
            this.precoUnitario = BigDecimal.ZERO;
            this.quantidadeComprada = 0;
        }
    }

    public BigDecimal getTotalItem() {
        if (emFalta) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidadeComprada));
    }

    public String getQuantidadeCompradaFormatada() {
        if (quantidadeComprada == (long) quantidadeComprada) {
            return String.format("%d", (long) quantidadeComprada);
        } else {
            return String.format("%.2f", quantidadeComprada).replace(",", ".");
        }
    }

    @Override
    public String toString() {
        if (emFalta) {
            return itemLista.getDescricao() + " (EM FALTA)";
        }
        return itemLista.getDescricao() + " - Qtd: " + getQuantidadeCompradaFormatada() +
                " " + itemLista.getUnidade() + " - Preço: R$ " + String.format("%.2f", precoUnitario) +
                " - Total: R$ " + String.format("%.2f", getTotalItem());
    }
}