package com.castle.model;

import java.util.List;

public class Batalha {

    public Batalha(List<Pelotao> pelotaoListAEnfrentar, List<Pelotao> pelotaoListJaEnfrentado){
        this.rodada = 1;
        this.pelotaoListAEnfrentar = pelotaoListAEnfrentar;
        this.pelotaoListJaEnfrentado = pelotaoListJaEnfrentado;
    }

    List<Pelotao> pelotaoListAEnfrentar = null;
    List<Pelotao> pelotaoListJaEnfrentado = null;
    int rodada;

    public List<Pelotao> getPelotaoListAEnfrentar() {
        return pelotaoListAEnfrentar;
    }

    public void setPelotaoListAEnfrentar(List<Pelotao> pelotaoListAEnfrentar) {
        this.pelotaoListAEnfrentar = pelotaoListAEnfrentar;
    }

    public List<Pelotao> getPelotaoListJaEnfrentado() {
        return pelotaoListJaEnfrentado;
    }

    public void setPelotaoListJaEnfrentado(List<Pelotao> pelotaoListJaEnfrentado) {
        this.pelotaoListJaEnfrentado = pelotaoListJaEnfrentado;
    }

    public int getRodada() {
        return rodada;
    }

    public void setRodada(int rodada) {
        this.rodada = rodada;
    }

    @Override
    public String toString() {
        return "Batalha{" +
                "pelotaoListAEnfrentar=" + pelotaoListAEnfrentar +
                ", pelotaoListJaEnfrentado=" + pelotaoListJaEnfrentado +
                ", rodada=" + rodada +
                '}';
    }
}
