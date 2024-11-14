package br.com.streaming.screenmatch.principal;

import br.com.streaming.screenmatch.model.DadosEpisodio;
import br.com.streaming.screenmatch.model.DadosSerie;
import br.com.streaming.screenmatch.model.DadosTemporada;
import br.com.streaming.screenmatch.model.Episodio;
import br.com.streaming.screenmatch.service.ConsumoApi;
import br.com.streaming.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversao = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=3d9b35cf";
    private final String SEASON = "&Season=";
    private final String EPISODE = "&episode=";
    private String serieEscolhida;

    public void buscarSerie() {
//        /*
//         * usuario digita a serie
//         * o sistema realize a busca dos dados da série
//         * guardar o ConsumiApi em uma variavel json
//         * conversao de json para DadosSerie
//         *
//         * e se o usuraio desejar ver as temporadas com os episodios?
//         * converver o json para DadosTemporada
//         * mostras as temporadas com a lista de episodios
//         * */
//

        System.out.println("Digite uma série para realizar a busca:");
        serieEscolhida = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + serieEscolhida.replace(" ","+") + APIKEY);

        DadosSerie dadosSerie = conversao.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        /*
        * 1-quero que mostre os dados da temporada: numero e a lista de episodios
        * */
        List<DadosTemporada> listaTemporada = new ArrayList<>();


        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + serieEscolhida.replace(" ","+") + SEASON + i + APIKEY);
            DadosTemporada dadosTemporada = conversao.obterDados(json,DadosTemporada.class);
            listaTemporada.add(dadosTemporada);
        }

        //percorre a lista de temporadas e lista de episodios e mostra para o usuario os episodios daquela determinada série
        listaTemporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.title())));

        //pega a listaTemporada , percorre a lista e a lista de episodios e guarda em uma variavel listaEpisodio
        List<DadosEpisodio> listaEpisodio = listaTemporada.stream()
                        .flatMap(t -> t.episodios().stream())//flatMap percorre lista dentro de lista
                                .collect(Collectors.toList());//guarda uma lista na variavel q eh uma lista

        System.out.println("\n=======Top 10 "+serieEscolhida+"==========\n");

        //percorre a lista de episodio, filtrando avaliações diferentes de nao avaliados (N/A), ordenando em ordem decrescente
//        listaEpisodio.stream()
//                .filter(e-> !e.avaliacao().equalsIgnoreCase("N/A")) //ignora episodios nao avaliados (N/A)
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())//reversed faz uma lista decrescente e nao crescente que eh o padrão
//                .map(e -> e.title().toUpperCase()) o titulo vai estar em letras maiusculas
//                .limit(10) //limita em 10 melhores episodios
//                .forEach(System.out::println);




        //vai ser guardado a lista de episodios a nova classe Episodio, para sabermos a temporada de cada episodio
        List<Episodio> episodios = listaTemporada.stream()
                .flatMap(t -> t.episodios().stream() //percorre a lista de temporada e lista de episodio
                        .map(d -> new Episodio(t.numeroTemporada(),d)) //d: dadosEpisodio; map transforma dados
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite o nome do episódio ou trecho: ");
        var trechoTitulo = leitura.nextLine(); //digita apenas um trecho do episodio da serie

        //optional é um container que ve se o episodio existe ou nao, é uma coisa ou outra
        Optional<Episodio> episodioBuscado = episodios.stream()
                //filtra episodios que contenha o trecho digitado, tudo em maiusculo (toUpperCase()), transformando o que o usuario digitou em letras
                //maiusculas e convertendo os titulos do episodio em maiusculo também
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst(); //método que encontra o primeiro elemento da stream, ou seja, busca o primeiro elemento de uma sequencia
        //quando nao há ordem de encontro, o findFirst() encontra qualquer elemento do Stream

        if(episodioBuscado.isPresent()){ //verifica se o episodio existe ou não, se nao ele será nulo
            System.out.println("Episódio encontrado!\nTemporada: "+episodioBuscado.get().getTemporada());

        }else{
            System.out.println("Episódio não encontrado!!");
        }


        // o map pega a chave-valor de uma determinada série (Integer = temporada, Double = avaliacao), filtra as avaliações
        //que tem nota > 0.0, e agrupa  os episodios em sua respectiva temporada, e a avaliação de cada episodio, calculando
        //a média de cada temporada
        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoPorTemporada);

        /*
        * Criando estatísticas
        * DoubleSummaryStatistics é uma classe q faz as estatisticas de uma forma direta
        * Assim como Collectors.summarizingDouble pega os valores para geras as estatisticas de um tipo Double, tambem existe o
        * Integer*/

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

       System.out.println("Média: "+est.getAverage()+"\n" +
               "Pior episódio: "+est.getMin()+"\n" +
               "Melhor episódio: "+est.getMax()+"\n" +
               "Quantidade: "+est.getCount());
//        System.out.println("A partir de qual ano gostaria de ver o episódio?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        //variavel que ao digitar o ano realiza a operação de busca daquele ano
//        LocalDate dataBusca = LocalDate.of(ano,1,1);//digita o ano e o ano começa no mes 1 e dia 1
//
//        //formata do padrao (Ingles) para Brasileiro o formato da data
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");//formata para estilo padrao Brasileiro ao inves de ano/dia/mes
//
//        //filtra os episodios em que a data de lançamento eh diferente de null e venha DEPOIS DA DATA DE BUSCA
//        //ou seja, se digitar o ano 2015, sera buscado episodios apenas após este ano escolhido
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(String.format("Temporada: " + e.getTemporada() +
//                        " Episódio: " + e.getTitulo() +
//                        " Data de lançamento: " + e.getDataLancamento().format(formatador) +
//                        " Avaliação: " + e.getAvaliacao() +
//                        " Número do eppisódio: " + e.getNumeroEpisodio())));
//
//
//
//

   }
}
