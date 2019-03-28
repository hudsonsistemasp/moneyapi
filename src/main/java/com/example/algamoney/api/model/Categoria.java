package com.example.algamoney.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity // notação que quer dizer que é uma entidade 
@Table(name = "categoria")
public class Categoria {
	//Mapear as colunas da tabela
	@Id // notação que quer dizer que esse é o id da tabela 
	@GeneratedValue(strategy = GenerationType.IDENTITY)//notação quer dizer que o cód é gerado através da tabela
	private long codigo;
	
	@NotNull//Com essa notação eu faço o spring data jpa tratar a exceção antes de tentar dar input no banco
	private String nome;
	
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	
	public long getCodigo() {
		return codigo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (codigo ^ (codigo >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (codigo != other.codigo)
			return false;
		return true;
	}
}
