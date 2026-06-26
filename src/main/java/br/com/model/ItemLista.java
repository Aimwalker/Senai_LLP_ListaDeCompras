package br.com.senai.shoppinglist.model;

import java.io.Serializable;

public class ItemLista implements Serializable {
    private static final long serialVersionUID = 1L;

    private String descricao;
    private UnidadeMedida unidade;
    private double quantidade;

    public ItemLista(String descricao, br.com.senai.shoppinglist.model.UnidadeMedida unidade, double quantidade) {
        this.descricao = descricao;
        this.unidade = unidade;
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UnidadeMedida getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeMedida unidade) {
        this.unidade = unidade;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getQuantidadeFormatada() {
        if (quantidade == (long) quantidade) {
            return String.format("%d", (long) quantidade);
        } else {
            return String.format("%.2f", quantidade).replace(",", ".");
        }
    }

    @Override
    public String toString() {
        return descricao + " (" + getQuantidadeFormatada() + " " + unidade + ")";
    }
}