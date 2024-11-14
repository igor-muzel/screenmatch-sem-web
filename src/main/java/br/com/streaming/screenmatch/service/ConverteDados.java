package br.com.streaming.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados{
    //ObjectMapper vai ler o json e tentar converter para uma classe (Record como: DadosEpisodio, DadosSerie, DadosTemporada)
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try{
            //le o valor passado por parametro e converte para a classe desejada (que mapeia os dados)
            return mapper.readValue(json, classe);

        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

    }
}
