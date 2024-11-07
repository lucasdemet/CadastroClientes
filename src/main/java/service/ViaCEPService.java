package service;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ViaCEPService {

	public static class Endereco {
		private String cep;
		private String logradouro;
		private String complemento;
		private String bairro;
		private String localidade;
		private String uf;

		public String getCep() {
			return cep;
		}

		public void setCep(String cep) {
			this.cep = cep;
		}

		public String getLogradouro() {
			return logradouro;
		}

		public void setLogradouro(String logradouro) {
			this.logradouro = logradouro;
		}

		public String getComplemento() {
			return complemento;
		}

		public void setComplemento(String complemento) {
			this.complemento = complemento;
		}

		public String getBairro() {
			return bairro;
		}

		public void setBairro(String bairro) {
			this.bairro = bairro;
		}

		public String getLocalidade() {
			return localidade;
		}

		public void setLocalidade(String localidade) {
			this.localidade = localidade;
		}

		public String getUf() {
			return uf;
		}

		public void setUf(String uf) {
			this.uf = uf;
		}
	}

	public static Endereco buscarEnderecoPorCep(String cep) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			StringBuilder jsonString = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}

			return new Gson().fromJson(jsonString.toString(), Endereco.class);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar CEP: " + e.getMessage());
		}
	}
}
