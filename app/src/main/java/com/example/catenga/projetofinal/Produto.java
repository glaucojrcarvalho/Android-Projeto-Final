package com.example.catenga.projetofinal;

/**
 * Created by catenga on 11/26/16.
 */

public class Produto {

    private String nome;
    private String descricao;
    private String imagem;

    public Produto(){

    }

    public Produto(String nome, String imagem, String descricao) {
        this.nome = nome;
        this.imagem = imagem;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDesc(String desc) {
        this.descricao = desc;
    }
}
