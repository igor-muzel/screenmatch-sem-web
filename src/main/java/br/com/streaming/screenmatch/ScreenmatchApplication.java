package br.com.streaming.screenmatch;

import br.com.streaming.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
//a interface CommandLineRunner pode servir para carregar dados para um Banco de Dados
//CommandLineRunner é executada apenas na inicialização da aplicação,
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.buscarSerie();
//

	}
}