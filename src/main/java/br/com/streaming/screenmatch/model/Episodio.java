package br.com.streaming.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Double avaliacao;
    private Integer numeroEpisodio;
    private LocalDate dataLancamento;

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio) {

                this.temporada = temporada;
                this.titulo = dadosEpisodio.title();

        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch(NumberFormatException n){
            this.avaliacao = 0.0;
        }
            this.numeroEpisodio = dadosEpisodio.numeroEpisodio();
        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());

        }catch (DateTimeParseException d){
            this.dataLancamento = null;
        }


    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "temporada: " + temporada +
                ", titulo: '" + titulo + '\'' +
                ", avaliacao: " + avaliacao +
                ", numeroEpisodio: " + numeroEpisodio +
                ", dataLancamento: " + dataLancamento;
    }
}
