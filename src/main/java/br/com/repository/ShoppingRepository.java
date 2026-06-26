package br.com.senai.shoppinglist.repository;

import br.com.senai.shoppinglist.model.ListaCompras;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingRepository {
    private static final String DATA_DIR = "data";
    private static final String FILE_NAME = DATA_DIR + "/shopping_lists.dat";

    private List<ListaCompras> listas;

    public ShoppingRepository() {
        this.listas = new ArrayList<>();
        carregarDados();
    }

    public List<ListaCompras> obterTodas() {
        return Collections.unmodifiableList(listas);
    }

    public ListaCompras obterPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        String nomeNormalizado = nome.trim().toLowerCase();
        for (ListaCompras lc : listas) {
            if (lc.getNome().toLowerCase().equals(nomeNormalizado)) {
                return lc;
            }
        }
        return null;
    }

    public void salvar(ListaCompras lista) {
        ListaCompras existente = obterPorNome(lista.getNome());
        if (existente != null) {
            listas.remove(existente);
        }
        listas.add(lista);
        salvarDados();
    }

    public boolean existeListas() {
        return !listas.isEmpty();
    }

    private synchronized void salvarDados() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(listas);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados das listas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void carregarDados() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                this.listas = (List<ListaCompras>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            // Se o arquivo estiver corrompido ou houver incompatibilidade, reinicia vazio
            System.err.println("Aviso: Não foi possível carregar as listas salvas anteriormente. Iniciando banco de dados vazio. " + e.getMessage());
            this.listas = new ArrayList<>();
        }
    }
}