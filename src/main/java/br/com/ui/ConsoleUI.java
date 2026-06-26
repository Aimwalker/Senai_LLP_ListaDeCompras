package br.com.senai.shoppinglist.ui;

import br.com.senai.shoppinglist.model.ItemCompra;
import br.com.senai.shoppinglist.model.ItemLista;
import br.com.senai.shoppinglist.model.ListaCompras;
import br.com.senai.shoppinglist.model.UnidadeMedida;
import br.com.senai.shoppinglist.service.ShoppingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConsoleUI {
    private final ShoppingService service;
    private final InputScanner input;

    public ConsoleUI() {
        this.service = new ShoppingService();
        this.input = new InputScanner();
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            exibirMenuPrincipal();
            System.out.print(">> Opção: ");
            opcao = input.lerInteiro("Opção inválida. Digite um número inteiro correspondente à opção: ");

            switch (opcao) {
                case 1:
                    executarNovaLista();
                    break;
                case 2:
                    executarFazerCompras();
                    break;
                case 3:
                    executarRelatorio();
                    break;
                case 0:
                    System.out.println("\nSaindo do programa. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println(); // Linha em branco para espaçamento
        }
        input.close();
    }

    private void exibirMenuPrincipal() {
        System.out.println(".-------------------.");
        System.out.println("| Gestão de compras |");
        System.out.println("'-------------------'");
        System.out.println("Selecione a opção:");
        System.out.println("1. Nova lista");
        System.out.println("2. Fazer compras");
        System.out.println("3. Relatório");
        System.out.println("0. Sair");
    }

    private void executarNovaLista() {
        String dataSugerida = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nomePadrao = "lista_" + dataSugerida;

        System.out.println();
        System.out.print(">> Nova lista, informe o nome [" + nomePadrao + "]: ");
        String nome = input.lerTexto().trim();

        if (nome.isEmpty()) {
            nome = nomePadrao;
        }

        try {
            ListaCompras novaLista = service.criarNovaLista(nome);

            // Loop para adicionar itens
            while (true) {
                System.out.println(">> ---Informe o item---------");
                System.out.print(">> Descrição: ");
                String descricao = input.lerTexto().trim();

                if (descricao.isEmpty()) {
                    System.out.println(">> ---Lista salva!---------");
                    break;
                }

                // Leitura e validação da Unidade de Medida
                UnidadeMedida unidade = null;
                while (unidade == null) {
                    System.out.print(">> Unidade (UN, KG, LT): ");
                    String sigla = input.lerTexto().trim();
                    unidade = UnidadeMedida.obterPorSigla(sigla);
                    if (unidade == null) {
                        System.out.println("Unidade inválida! Opções válidas: UN, CX, KG, LT.");
                    }
                }

                // Leitura e validação da Quantidade
                double quantidade = -1;
                while (quantidade <= 0) {
                    System.out.print(">> Quantidade: ");
                    quantidade = input.lerDouble("Entrada inválida. Digite um número positivo para quantidade: ");
                    if (quantidade <= 0) {
                        System.out.println("A quantidade deve ser maior que zero.");
                    }
                }

                ItemLista item = new ItemLista(descricao, unidade, quantidade);
                novaLista.adicionarItem(item);
                service.salvarLista(novaLista);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao criar lista: " + e.getMessage());
        }
    }

    private void executarFazerCompras() {
        if (!service.temListas()) {
            System.out.println("\nNenhuma lista de compras cadastrada. Crie uma lista primeiro (Opção 1).");
            return;
        }

        ListaCompras listaSelecionada = selecionarLista("Fazer Compras");
        if (listaSelecionada == null) {
            return; // Usuário escolheu voltar
        }

        if (listaSelecionada.getItens().isEmpty()) {
            System.out.println("Esta lista não possui itens cadastrados!");
            return;
        }

        System.out.println("\n>> ---Fazer compras [" + listaSelecionada.getNome() + "]---");
        List<ItemCompra> itensComprados = new ArrayList<>();
        List<ItemLista> itensDaLista = listaSelecionada.getItens();
        int totalItens = itensDaLista.size();

        for (int i = 0; i < totalItens; i++) {
            ItemLista item = itensDaLista.get(i);
            int indiceItem = i + 1;

            System.out.println(">> (" + indiceItem + "/" + totalItens + ") Produto " + item.getDescricao() + " " + item.getQuantidadeFormatada() + " " + item.getUnidade());

            // Solicita a quantidade comprada (com o padrão da lista original)
            System.out.print(">> Quantidade [" + item.getQuantidadeFormatada() + " " + item.getUnidade() + "]: ");
            double qtdComprada = input.lerDoubleComPadrao(item.getQuantidade(), "Entrada inválida. Digite a quantidade da compra: ");

            // Solicita o preço unitário
            System.out.print(">> Preço: ");
            BigDecimal preco = input.lerBigDecimal("Entrada inválida. Digite o preço da unidade (ex: 29.55): ");

            ItemCompra itemCompra = new ItemCompra(item);
            itemCompra.setQuantidadeComprada(qtdComprada);
            itemCompra.setPrecoUnitario(preco);

            if (itemCompra.isEmFalta()) {
                System.out.println(">> Item em falta! Pulando para o próximo...");
            } else {
                System.out.println(">> Total do item: R$ " + String.format("%.2f", itemCompra.getTotalItem()));
            }

            itensComprados.add(itemCompra);
        }

        // Salva os resultados da compra no serviço
        service.registrarCompra(listaSelecionada.getNome(), itensComprados);

        System.out.println(">> ---Total------------------");
        System.out.println(">> R$: " + String.format("%.2f", listaSelecionada.calcularTotalCompra()));
    }

    private void executarRelatorio() {
        if (!service.temListas()) {
            System.out.println("\nNenhuma lista de compras cadastrada. Crie uma lista primeiro (Opção 1).");
            return;
        }

        ListaCompras listaSelecionada = selecionarLista("Relatório");
        if (listaSelecionada == null) {
            return; // Usuário escolheu voltar
        }

        if (!listaSelecionada.isRealizada()) {
            System.out.println("\nA compra para a lista '" + listaSelecionada.getNome() + "' ainda não foi realizada.");
            System.out.println("Por favor, execute as compras nesta lista primeiro (Opção 2).");
            return;
        }

        System.out.println("\n>> ---Relatório [" + listaSelecionada.getNome() + "]---");
        System.out.println(">> Item, Descrição, Qtd, UN, Preço");

        List<ItemCompra> itensComprados = listaSelecionada.getItensComprados();
        int itemIndex = 1;
        for (ItemCompra ic : itensComprados) {
            if (!ic.isEmFalta()) {
                System.out.println(">> " + itemIndex + ", " +
                        ic.getItemLista().getDescricao() + ", " +
                        ic.getQuantidadeCompradaFormatada() + ", " +
                        ic.getItemLista().getUnidade() + ", " +
                        String.format("%.2f", ic.getPrecoUnitario()).replace(",", "."));
                itemIndex++;
            }
        }

        // Imprime a linha final de TOTAL
        System.out.println(">> ...");
        System.out.println(">> 0, TOTAL, " +
                listaSelecionada.getQuantidadeTotalCompradaFormatada() + ", UN, " +
                String.format("%.2f", listaSelecionada.calcularTotalCompra()).replace(",", "."));
    }

    private ListaCompras selecionarLista(String contexto) {
        List<ListaCompras> listas = service.obterTodasListas();

        System.out.println("\n--- Selecione a Lista para " + contexto + " ---");
        for (int i = 0; i < listas.size(); i++) {
            ListaCompras l = listas.get(i);
            String status = l.isRealizada() ? "[Comprada]" : "[Pendente]";
            System.out.println((i + 1) + ". " + l.getNome() + " " + status);
        }
        System.out.println("0. Voltar");

        int escolha = -1;
        while (escolha < 0 || escolha > listas.size()) {
            System.out.print(">> Seleção: ");
            escolha = input.lerInteiro("Opção inválida. Digite o número correspondente à lista: ");
            if (escolha < 0 || escolha > listas.size()) {
                System.out.println("Opção fora do intervalo válido (0 a " + listas.size() + ").");
            }
        }

        if (escolha == 0) {
            return null;
        }

        return listas.get(escolha - 1);
    }
}