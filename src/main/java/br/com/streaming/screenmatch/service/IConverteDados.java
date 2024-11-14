package br.com.streaming.screenmatch.service;

public interface IConverteDados {
    //interface que recebe uma classe genérica e um json
    <T>T obterDados(String json, Class<T>classe);
}
