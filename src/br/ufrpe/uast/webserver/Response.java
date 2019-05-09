package br.ufrpe.uast.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Response implements Runnable {
	
	class Conteudo {
		byte[] bytes;
		String contentType;
		
		public Conteudo(byte[] bytes, String contentType) {
			this.bytes = bytes;
			this.contentType = contentType;
		}
		public byte[] getBytes() {
			return bytes;
		}
		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
	}
	
	private Socket socketServidor;
	private Request request;
	
	public Response(Socket socketServidor, Request req) {
		this.socketServidor = socketServidor;
		this.request = req;
	}
	
	private Conteudo procurarArquivo(String caminho) throws FileNotFoundException {
		if (caminho.equals("/"))
			caminho = "index.html";
		
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		Path currentRelativePath = Paths.get(currentPath.toString(), caminho);
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		
		File f = new File(s);
		
		InputStream in = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[(int) f.length()];
		try {
			in.read(buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String ct = null;
		try {
			ct = Files.probeContentType(Paths.get(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Conteudo(buf, ct);
	}
	
	private void formatarResposta(OutputStream saida) {
		String data = java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC));
		Conteudo conteudo = null;
		
		try {
			conteudo = procurarArquivo(this.request.reqLine.getCaminho());
		} catch (FileNotFoundException e) {
			// Nao é arquivo, entao mostrar a listagem
			e.printStackTrace();
		}
				
		String s = Config.VERSAO_HTTP + " 200 OK\r\n" +
				"Date: " + data + "\r\n" +
				"Server: " + Config.NOME_SERVIDOR + "\r\n" +
				"Content-Type: " + conteudo.getContentType() + "\r\n" + 
				"\r\n";
		
		try {
			saida.write(s.getBytes());
			saida.write(conteudo.getBytes());
			saida.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {		
		try {
			formatarResposta(this.socketServidor.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
