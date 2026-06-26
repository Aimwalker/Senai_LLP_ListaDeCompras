package br.com.senai.shoppinglist.model;

public enum UnidadeMedida {
    UN("UN", "Unidade"),
    CX("CX", "Caixa"),
    KG("KG", "Quilograma"),
    LT("LT", "Litro");

    private final String sigla;
    private final String descricao;

    UnidadeMedida(String sigla, String descricao) {
        this.sigla = sigla;
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public static UnidadeMedida obterPorSigla(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        String siglaLimpa = valor.trim().toUpperCase();
        for (UnidadeMedida um : values()) {
            if (um.getSigla().equals(siglaLimpa)) {
                return um;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return sigla;
    }
}