package br.ufrpe.uast.webserver;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Request {
	private BufferedReader header;
	public RequestLine reqLine;
	public ArrayList<HeaderField> headers = new ArrayList<>();
	
	public Request(BufferedReader requestHeader) throws IOException {
		// Fazer o parser do header do request
		String linha = requestHeader.readLine();
		
		this.reqLine = parseMetodo(linha);
		this.headers = new ArrayList<HeaderField>();

		while (true) {
			linha = requestHeader.readLine();
			
			if (linha == null || linha.equals("")) {
				break;
			}
			
			this.headers.add(parseHeaders(linha));
		}
	}
	
	private RequestLine parseMetodo(String str) {
		String[] partes = str.split(" ");
		
		return new RequestLine(partes[0], partes[1], partes[2]);
	}
	
	private HeaderField parseHeaders(String str) {
		String[] partes = str.split(":");
		
		return new HeaderField(partes[0], partes[1].trim());
	}
	
	class HeaderField {
		private String header;
		private String campos;
		
		public HeaderField(String header, String campos) {
			this.header = header;
			this.campos = campos;
		}

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public String getCampos() {
			return campos;
		}

		public void setCampos(String campos) {
			this.campos = campos;
		}

		@Override
		public String toString() {
			return this.header + ": " + this.campos + "\r\n";
		}
		
		
	}
	
	class RequestLine {
		private String metodo;
		private String caminho;
		private String versao;
		
		public String getMetodo() {
			return metodo;
		}

		public void setMetodo(String metodo) {
			this.metodo = metodo;
		}

		public String getCaminho() {
			return caminho;
		}

		public void setCaminho(String caminho) {
			this.caminho = caminho;
		}

		public String getVersao() {
			return versao;
		}

		public void setVersao(String versao) {
			this.versao = versao;
		}

		public RequestLine(String metodo, String caminho, String versao) {
			this.metodo = metodo;
			this.caminho = caminho;
			this.versao = versao;
		}

		@Override
		public String toString() {
			return this.metodo + " " + this.caminho + " " + this.versao + "\r\n";
		}
		
		
	}
}
