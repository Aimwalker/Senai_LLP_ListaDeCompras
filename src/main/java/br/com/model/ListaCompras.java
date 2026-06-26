package br.com.senai.shoppinglist.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListaCompras implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private LocalDate dataCriacao;
    private List<ItemLista> itens;
    private List<ItemCompra> itensComprados;
    private boolean realizada;

    public ListaCompras(String nome) {
        this.nome = nome;
        this.dataCriacao = LocalDate.now();
        this.itens = new ArrayList<>();
        this.itensComprados = new ArrayList<>();
        this.realizada = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<ItemLista> getItens() {
        return itens;
    }

    public void adicionarItem(ItemLista item) {
        this.itens.add(item);
    }

    public List<ItemCompra> getItensComprados() {
        return itensComprados;
    }

    public void setItensComprados(List<ItemCompra> itensComprados) {
        this.itensComprados = itensComprados;
        this.realizada = true;
    }

    public boolean isRealizada() {
        return realizada;
    }

    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }

    public BigDecimal calcularTotalCompra() {
        if (!realizada) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCompra ic : itensComprados) {
            total = total.add(ic.getTotalItem());
        }
        return total;
    }

    public double calcularQuantidadeTotalComprada() {
        if (!realizada) {
            return 0;
        }
        double totalQtd = 0;
        for (ItemCompra ic : itensComprados) {
            if (!ic.isEmFalta()) {
                totalQtd += ic.getQuantidadeComprada();
            }
        }
        return totalQtd;
    }

    public String getQuantidadeTotalCompradaFormatada() {
        double qtd = calcularQuantidadeTotalComprada();
        if (qtd == (long) qtd) {
            return String.format("%d", (long) qtd);
        } else {
            return String.format("%.2f", qtd).replace(",", ".");
        }
    }
}