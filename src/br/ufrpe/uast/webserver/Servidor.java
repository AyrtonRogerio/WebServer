package br.ufrpe.uast.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	private ServerSocket socket;
	
	public Servidor(int porta) {
		try {
			this.socket = new ServerSocket(porta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ouvir() throws IOException {
		Socket s = this.socket.accept();
		System.out.println(s);
			
		BufferedReader entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		Request req = new Request(entrada);
			
		Thread t = new Thread(new Response(s, req));
			
		t.run();
		
		if (!t.isAlive()) {
			s.close();
		}
	}	
}
