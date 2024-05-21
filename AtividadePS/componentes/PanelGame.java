package componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import java.util.ArrayList;
import java.util.List;
import objetos.Atingir;
import objetos.Jogador;

public class PanelGame extends JComponent {

    private Graphics2D g2;
    private BufferedImage image;
    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private Teclado teclado;
    private int balaTime;

    //FPS
    private final int FPS = 60;
    private final int TEMPO_FINAl = 1000000000 / FPS;

    //Objetos
    private Jogador jogador;
    private List<Atingir> balas;

    public void start(){
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        thread = new Thread(new Runnable() {
           @Override
           public void run(){
            while(start){
                long startTime = System.nanoTime();
                Background();
                drawJogo();
                render();
                long time = System.nanoTime() - startTime;
                if(time < TEMPO_FINAl){
                    long sleep = (TEMPO_FINAl - time) / 1000000;
                    sleep(sleep);
                }
             }
           } 
        });
        initObj();
        initTeclado();
        initBalas();
        thread.start();
    }

    private void initObj(){
        jogador = new Jogador();
        jogador.mover(150,150);
    }

    public void initTeclado(){
        teclado = new Teclado(); 
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_A){
                    teclado.setEsquerda(true);
                } 
                else if(e.getKeyCode() == KeyEvent.VK_D){
                    teclado.setDireita(true);
                }
                else if(e.getKeyCode() == KeyEvent.VK_W){
                    teclado.setTecla_W(true);
                }
                else if(e.getKeyCode() == KeyEvent.VK_J){
                    teclado.setTecla_J(true);
                }
                else if(e.getKeyCode() == KeyEvent.VK_K){
                    teclado.setTecla_K(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_A){
                    teclado.setEsquerda(false);
                } 
                else if(e.getKeyCode() == KeyEvent.VK_D){
                    teclado.setDireita(false);
                }
                else if(e.getKeyCode() == KeyEvent.VK_W){
                    teclado.setTecla_W(false);
                }
                else if(e.getKeyCode() == KeyEvent.VK_J){
                    teclado.setTecla_J(false);
                }
                else if(e.getKeyCode() == KeyEvent.VK_K){
                    teclado.setTecla_K(false);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run(){
                float s = 0.5f;
                while (start) {
                    float angulo = jogador.getAngulo();
                    if(teclado.isEsquerda()){
                        angulo -= s;
                    }
                    if(teclado.isDireita()){
                        angulo += s;
                    }
                    if(teclado.isTecla_J() || teclado.isTecla_K()){
                        if(balaTime == 0){
                            if(teclado.isTecla_J()){
                                balas.add(0, new Atingir(jogador.getX(), jogador.getY(), jogador.getAngulo(), 5, 3f));
                            }
                            else{
                                balas.add(0, new Atingir(jogador.getX(), jogador.getY(), jogador.getAngulo(), 20, 3f));
                            }
                        }
                        balaTime++;
                        if(balaTime == 15){
                            balaTime = 0;
                        }
                        else{
                            balaTime = 0;
                        }
                    }
                    if(teclado.isTecla_W()){
                        jogador.velocidadeON();
                    }else{
                        jogador.devagar();
                    }
                    jogador.atualizar();
                    jogador.mudarAngulo(angulo);
                    sleep(5);
                }
            }
        }).start();
    }

    private void initBalas(){
        balas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run(){
                while (start) {
                    for(int i = 0; i < balas.size(); i++){
                        Atingir bala = balas.get(i);
                        if(balas != null){
                            bala.atualizar();
                            if(!bala.check(width, height)){
                                balas.remove(bala);
                            }
                        }
                        else{
                            balas.remove(bala);
                        }
                    }
                    sleep(1);
                }
            }
        });
    }

    private void Background(){
        g2.setColor(new Color(30,30,30));
        g2.fillRect(0,0, width, height);
    }

    private void drawJogo(){
        jogador.draw(g2);
        for(int i=0; i<balas.size();i++){
            Atingir bala = balas.get(i);
            if(bala != null){
                bala.draw(g2);
            }
        }
    }

    private void render(){
        Graphics g = getGraphics();
        g.drawImage(image, 0,0, null);
        g.dispose();
    }

    private void sleep(long speed){
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}