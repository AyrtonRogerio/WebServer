package br.ufrpe.uast.webserver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {
		Servidor servidor = new Servidor(8080);
		

		
		try {
			while(true) {
				servidor.ouvir();	
			}
		} catch (IOException e) {
			System.err.println("Não foi possível iniciar o servidor:");
			e.printStackTrace();
		}
	}
}
