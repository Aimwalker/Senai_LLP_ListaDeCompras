package br.com.senai.shoppinglist.service;

import br.com.senai.shoppinglist.model.ItemCompra;
import br.com.senai.shoppinglist.model.ListaCompras;
import br.com.senai.shoppinglist.repository.ShoppingRepository;

import java.util.List;

public class ShoppingService {
    private final ShoppingRepository repository;

    public ShoppingService() {
        this.repository = new ShoppingRepository();
    }

    public List<ListaCompras> obterTodasListas() {
        return repository.obterTodas();
    }

    public ListaCompras obterListaPorNome(String nome) {
        return repository.obterPorNome(nome);
    }

    public ListaCompras criarNovaLista(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da lista não pode ser vazio.");
        }

        String nomeFormatado = nome.trim();
        if (repository.obterPorNome(nomeFormatado) != null) {
            throw new IllegalArgumentException("Já existe uma lista com o nome '" + nomeFormatado + "'.");
        }

        ListaCompras novaLista = new ListaCompras(nomeFormatado);
        repository.salvar(novaLista);
        return novaLista;
    }

    public void salvarLista(ListaCompras lista) {
        if (lista == null) {
            throw new IllegalArgumentException("A lista de compras não pode ser nula.");
        }
        repository.salvar(lista);
    }

    public void registrarCompra(String nomeLista, List<ItemCompra> itensComprados) {
        ListaCompras lista = repository.obterPorNome(nomeLista);
        if (lista == null) {
            throw new IllegalArgumentException("Lista de compras '" + nomeLista + "' não encontrada.");
        }

        lista.setItensComprados(itensComprados);
        repository.salvar(lista);
    }

    public boolean temListas() {
        return repository.existeListas();
    }
}