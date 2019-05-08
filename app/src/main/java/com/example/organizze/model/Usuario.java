package com.example.organizze.model;

import com.example.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String nome, email, senha, idUsuario;
    private Double despesasTotal = 0.0, receitaTotal = 0.0;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);
    }

    public Double getDespesasTotal() {
        return despesasTotal;
    }

    public void setDespesasTotal(Double despesasTotal) {
        this.despesasTotal = despesasTotal;
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
