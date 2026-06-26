package br.com.senai.shoppinglist;

import br.com.senai.shoppinglist.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Inicializa e executa a interface CLI do console
        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.iniciar();
    }
}