package org.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String avatar;
    private String senha;
    private Integer quantidadeSimulacoesBemSucedidas;
    private Integer quantidadeSimulacoes;
    private Float mediaSimulacoesBemSucedidas;
    private Double pontuacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getQuantidadeSimulacoesBemSucedidas() {
        return quantidadeSimulacoesBemSucedidas;
    }

    public void setQuantidadeSimulacoesBemSucedidas(Integer quantidadeSimulacoesBemSucedidas) {
        this.quantidadeSimulacoesBemSucedidas = quantidadeSimulacoesBemSucedidas;
    }

    public Integer getQuantidadeSimulacoes() {
        return quantidadeSimulacoes;
    }

    public void setQuantidadeSimulacoes(Integer quantidadeSimulacoes) {
        this.quantidadeSimulacoes = quantidadeSimulacoes;
    }

    public Float getMediaSimulacoesBemSucedidas() {
        return mediaSimulacoesBemSucedidas;
    }

    public void setMediaSimulacoesBemSucedidas(Float mediaSimulacoesBemSucedidas) {
        this.mediaSimulacoesBemSucedidas = mediaSimulacoesBemSucedidas;
    }

    public Double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Override
    public String toString() {
        return String.format(
                "Usuario %s :\n" +
                        " avatar = '%s'\n" +
                        " quantidade de simulações = %d\n" +
                        " média de simulações bem sucedidas = %.4f\n" +
                        " pontuação = %.2f\n",
                login != null ? login : "(sem login)",
                avatar != null ? avatar : "(sem avatar)",
                quantidadeSimulacoes,
                mediaSimulacoesBemSucedidas != null ? mediaSimulacoesBemSucedidas : 0.0,
                quantidadeSimulacoesBemSucedidas != null ? quantidadeSimulacoesBemSucedidas : 0.0
        );
    }

}
