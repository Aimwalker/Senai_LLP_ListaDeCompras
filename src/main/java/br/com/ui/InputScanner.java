package br.com.senai.shoppinglist.ui;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputScanner implements AutoCloseable {
    private final Scanner scanner;

    public InputScanner() {
        this.scanner = new Scanner(System.in);
    }

    public String lerTexto() {
        return scanner.nextLine();
    }

    public String lerTextoObrigatorio(String mensagemErro) {
        while (true) {
            String texto = lerTexto().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.print(mensagemErro);
        }
    }

    public int lerInteiro(String mensagemErro) {
        while (true) {
            String linha = lerTexto().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.print(mensagemErro);
            }
        }
    }

    public double lerDouble(String mensagemErro) {
        while (true) {
            String linha = lerTexto().trim();
            try {
                // Converte vírgula brasileira em ponto decimal se necessário
                linha = linha.replace(",", ".");
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                System.out.print(mensagemErro);
            }
        }
    }

    public double lerDoubleComPadrao(double valorPadrao, String mensagemErro) {
        while (true) {
            String linha = lerTexto().trim();
            if (linha.isEmpty()) {
                return valorPadrao;
            }
            try {
                linha = linha.replace(",", ".");
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                System.out.print(mensagemErro);
            }
        }
    }

    public BigDecimal lerBigDecimal(String mensagemErro) {
        while (true) {
            String linha = lerTexto().trim();
            try {
                linha = linha.replace(",", ".");
                return new BigDecimal(linha);
            } catch (NumberFormatException | ArithmeticException e) {
                System.out.print(mensagemErro);
            }
        }
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}