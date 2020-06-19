package com.castle.model;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;

import java.util.Objects;

public class Pelotao {

    private int poder;
    private String nome;
    private int vitorias;
    private int derrotas;
    private ScreenLocation screenLocation;

    public Pelotao(int poder) {
        this.poder = poder;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public ScreenLocation getScreenLocation() { return screenLocation; }

    public void setScreenLocation(ScreenLocation screenLocation) { this.screenLocation = screenLocation; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Pelotao.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Pelotao other = (Pelotao) obj;
        if (this.poder != other.poder) {
            return false;
        }

        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Pelotao{" +
                "poder=" + poder +
                ", nome='" + nome + '\'' +
                ", vitorias=" + vitorias +
                ", derrotas=" + derrotas +
                '}';
    }
}
