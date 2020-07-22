package com.castle.play;

import com.castle.Main;
import com.castle.config.HustleCastleBotConfig;
import com.castle.image.processing.FindImage;
import com.castle.model.Batalha;
import com.castle.model.Pelotao;
import com.castle.utils.Utils;
import org.sikuli.api.*;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private static final Logger logger = LoggerFactory.getLogger(Arena.class);
    private final int MAX_PELOTAO = 15;
    private final int NUM_RODADAS = 5;
    private final String POSICAO_ARENA = "arena/posicao_";
    private final String ESTAGIO_ARENA = "arena/estagio_";
    private final String FORMATO_ARQUIVO = ".png";
    private final String PELOTAO_FECHAR = "arena/pelotao_fechar.png";
    private final String BOTAO_BATALHAR = "arena/botao_arena_batalhar.png";
    private final String BOTAO_RESGATAR_RECOMPENSA = "arena/botao_arena_resgatar_recompensa.png";
    private final String BOTAO_FIM = "arena/botao_arena_fim.png";
    private final String BOTAO_BATALHA_INICIO = "arena/botao_arena_batalha_inicio.png";
    private final String IMAGEM_EVENTO_1 = "arena/evento_lupa_arena.png";
    private final String IMAGEM_EVENTO_2 = "arena/evento_martelo_arena.png";
    List<ScreenLocation> screenLocationList = null;
    private int minhaPosicao;
    private static HustleCastleBotConfig config;
    private static Arena instance;

    private Arena() {};

    public static synchronized Arena getInstance() {
        if(instance == null) {
            instance = new Arena();
        }
        return instance;
    }

    public void iniciarArena() throws InterruptedException {
        screenLocationList = constroiPosicaoBatalha();
        config = HustleCastleBotConfig.getInstance();
        System.out.println("Bem-Vindo a ARENA!");
        boolean completouRodada = false;
        Batalha batalha = new Batalha(new ArrayList<>(MAX_PELOTAO), new ArrayList<>(NUM_RODADAS));

        while(batalha.getRodada() <= NUM_RODADAS && Main.continuar) {
            try {
                FindImage findImage = FindImage.getInstance();
                if (findImage.imageExists(ESTAGIO_ARENA + batalha.getRodada() + FORMATO_ARQUIVO)) {
                    System.out.println("Estagio " + batalha.getRodada());
                    if (!completouRodada) {
                        System.out.println("Mapeando adversarios... rodada: " + batalha.getRodada());
                        encontrarAdversariosNaBatalha(batalha);
                        seletorEstrategias(batalha);
                        Utils.esperarEventoAcontecerEClicar(BOTAO_BATALHA_INICIO,"Esperando o fim da batalha...", false);
                        completouRodada = true;
                        batalha.getPelotaoListAEnfrentar().clear();
                    }
                } else {
                    System.out.println("Estagio " + batalha.getRodada() + " da arena nao encontrado.");
                }

                if (findImage.imageExists(ESTAGIO_ARENA + (batalha.getRodada() + 1) + FORMATO_ARQUIVO)) { // se apareceu a proxima rodada.
                    System.out.println("Vamos ao estagio " + (batalha.getRodada() + 1));
                    batalha.setRodada(batalha.getRodada() + 1);
                    completouRodada = false;
                }

            } catch (Exception e) {
                logger.info("Erro", e);
            }

        }

        clickEvento();
        if(Utils.encontraImagemParaClicar(BOTAO_RESGATAR_RECOMPENSA)) {
            System.out.println("Resgatei a recompensa");
        } else if(Utils.encontraImagemParaClicar(BOTAO_FIM)) {
            System.out.println("Fim da arena");
        }
    }

    public void encontrarAdversariosNaBatalha(Batalha batalha) throws InterruptedException {
        FindImage findImage = FindImage.getInstance();
        Mouse mouse = new DesktopMouse();
        Thread.sleep(800);
        for(int i=1; i<=MAX_PELOTAO && Main.continuar; i++) {
            Thread.sleep(200);
            //ScreenRegion screenRegion = findImage.findImage(POSICAO_ARENA + i + FORMATO_ARQUIVO);
            //if(screenRegion != null) {
            mouse.move(screenLocationList.get(i-1));
            mouse.click(screenLocationList.get(i-1));
            Thread.sleep(1000);
            int poder = findImage.getPower(config.getPowerCut().get("x"), config.getPowerCut().get("y"), config.getPowerCut().get("width"), config.getPowerCut().get("height"));
            System.out.println("Posicao: " + i + " poder: "+ poder);
            if(poder > 1900000 || poder < 200000) {
                poder = findImage.getPower(config.getPowerCut().get("x"), config.getPowerCut().get("y"), config.getPowerCut().get("width")+10, config.getPowerCut().get("height"));
                System.out.println("Retentativa1 poder: "+ poder);
            }
            if(poder > 1900000 || poder < 200000) {
                poder = findImage.getPower(config.getPowerCut().get("x"), config.getPowerCut().get("y"), config.getPowerCut().get("width")-10, config.getPowerCut().get("height"));
                System.out.println("Retentativa2 poder: "+ poder);
            }

            Pelotao p = new Pelotao(poder);
            p.setScreenLocation(screenLocationList.get(i-1));
            batalha.getPelotaoListAEnfrentar().add(p);

            //Keyboard keyboard = new DesktopKeyboard();
            //keyboard.keyDown(Key.ESC);
            if(!Utils.encontraImagemParaClicar(PELOTAO_FECHAR)) {
                minhaPosicao = i;
                System.out.println("Minha posicao eh: "+i);
            }

            //}
        }
    }

    private Pelotao escolheAdversarioMenorPoder(Batalha batalha) {
        int menorPoder = Integer.MAX_VALUE;
        Pelotao pelotaoParaAtacar = null;
        int i=1;
        if(batalha != null && !batalha.getPelotaoListAEnfrentar().isEmpty()) {
            for (Pelotao pelotao : batalha.getPelotaoListAEnfrentar()) {
                if(pelotao.getPoder() < menorPoder && !batalha.getPelotaoListJaEnfrentado().contains(pelotao) && i != minhaPosicao) {
                    menorPoder = pelotao.getPoder();
                    pelotaoParaAtacar = pelotao;
                }
                i++;
            }
        }
        batalha.getPelotaoListJaEnfrentado().add(pelotaoParaAtacar);
        System.out.println("Pelotao a atacar: "+pelotaoParaAtacar);
        return pelotaoParaAtacar;
    }


    private Pelotao escolheAdversarioMenorPoderAcimaDoMeuPelotao(Batalha batalha) {
        int menorPoder = Integer.MAX_VALUE;
        Pelotao pelotaoParaAtacar = null;

        int i=1;
        if(batalha != null && !batalha.getPelotaoListAEnfrentar().isEmpty()) {
            for (Pelotao pelotao : batalha.getPelotaoListAEnfrentar()) {
                if(i == minhaPosicao)
                    break;
                if(pelotao.getPoder() < menorPoder && !batalha.getPelotaoListJaEnfrentado().contains(pelotao)) {
                    menorPoder = pelotao.getPoder();
                    pelotaoParaAtacar = pelotao;
                }
                i++;
            }
        }
        batalha.getPelotaoListJaEnfrentado().add(pelotaoParaAtacar);
        System.out.println("Pelotao a atacar: "+pelotaoParaAtacar);
        return pelotaoParaAtacar;
    }

    public void estrategiaPadrao(Batalha batalha, int qtdAtaqueMenorPoder) {
        Mouse mouse = new DesktopMouse();
        ScreenLocation screenLocation = null;
        Pelotao p = null;
        try {
            if (batalha.getRodada() <= qtdAtaqueMenorPoder) {
                p = escolheAdversarioMenorPoder(batalha);
            } else {
                p = escolheAdversarioMenorPoderAcimaDoMeuPelotao(batalha);
            }
            //se mesmo com a estrategia eu ja enfrentei todos acima ou todos abaixo então p == null
            if(p == null) {
                System.out.println("fugindo da estrategia p == null");
                p = escolheAdversarioMenorPoder(batalha);
            }

            screenLocation = p.getScreenLocation();
            mouse.move(screenLocation);
            mouse.click(screenLocation);
            Thread.sleep(1200);
            Utils.encontraImagemParaClicar(BOTAO_BATALHAR);
        } catch (Exception e) {
            logger.error("Problemas ao executar a estrategia, pelotao ={}", p, e);
        }
    }

    public void seletorEstrategias(Batalha batalha) {
        if(config.getSeletorEstrategia() == 32) {
            estrategiaPadrao(batalha,3);
        } else if (config.getSeletorEstrategia() == 41) {
            estrategiaPadrao(batalha,4);
        } else if (config.getSeletorEstrategia() == 50) {
            estrategiaPadrao(batalha,5);
        }

    }

    public List<ScreenLocation> constroiPosicaoBatalha() {
        List<ScreenLocation> screenLocations = new ArrayList<>(MAX_PELOTAO);
        Screen screen = new DesktopScreenRegion(0, 0, 32, 1020, 578).getScreen();
        for(int i=1; i<=MAX_PELOTAO; i++) {
            if(i==1){
                screenLocations.add(new DefaultScreenLocation(screen, 440, 120));
            } else if(i==2){
                screenLocations.add(new DefaultScreenLocation(screen, 345, 230));
            }else if(i==3){
                screenLocations.add(new DefaultScreenLocation(screen, 540, 230));
            }else if(i==4){
                screenLocations.add(new DefaultScreenLocation(screen, 240, 345));
            }else if(i==5){
                screenLocations.add(new DefaultScreenLocation(screen, 440, 345));
            }else if(i==6){
                screenLocations.add(new DefaultScreenLocation(screen, 640, 345));
            }else if(i==7){
                screenLocations.add(new DefaultScreenLocation(screen, 145, 460));
            }else if(i==8){
                screenLocations.add(new DefaultScreenLocation(screen, 345, 460));
            }else if(i==9){
                screenLocations.add(new DefaultScreenLocation(screen, 545, 460));
            }else if(i==10){
                screenLocations.add(new DefaultScreenLocation(screen, 745, 460));
            }else if(i==11){
                screenLocations.add(new DefaultScreenLocation(screen, 125, 525));
            }else if(i==12){
                screenLocations.add(new DefaultScreenLocation(screen, 325, 525));
            }else if(i==13){
                screenLocations.add(new DefaultScreenLocation(screen, 525, 525));
            }else if(i==14){
                screenLocations.add(new DefaultScreenLocation(screen, 725, 525));
            }else if(i==15){
                screenLocations.add(new DefaultScreenLocation(screen, 925, 525));
            }

        }
        return screenLocations;
    }

    public void clickEvento() {
        Utils.encontraImagemParaClicar(IMAGEM_EVENTO_1);
        Utils.encontraImagemParaClicar(IMAGEM_EVENTO_2);
    }
}
