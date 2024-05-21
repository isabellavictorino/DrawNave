package objetos;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

public class Jogador {
    
    public Jogador(){
        this.image = new ImageIcon(getClass().getResource("/imagens/plane.png")).getImage();
        this.image_speed = new ImageIcon(getClass().getResource("/imagens/plane_speed.png")).getImage();;
    }

    public static final double TAMANHO_J = 64;
    private double x;
    private double y;
    private final float MAX_VELOCIDADE = 1f;
    private float velocidade = 0f;
    private float angulo = 0f;
    private final Image image;
    private final Image image_speed;
    private boolean velocidadeON;

    public void mover(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void atualizar(){
        x += Math.cos(Math.toRadians(angulo)) * velocidade;
        y += Math.sin(Math.toRadians(angulo)) * velocidade;
    }

    public void mudarAngulo(float angulo){
        if(angulo < 0){
            angulo = 359;
        }
        else if(angulo > 359){
            angulo = 0;
        }
        this.angulo = angulo;
    }

    public void draw(Graphics2D g2){
        AffineTransform transforme = g2.getTransform();
        g2.translate(x,y);
        AffineTransform tr = new AffineTransform();
        tr.rotate(Math.toRadians(angulo+45), TAMANHO_J / 2, TAMANHO_J / 2);
        g2.drawImage(velocidadeON ? image_speed : image, tr, null);
        g2.setTransform(transforme);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public float getAngulo(){
        return angulo;
    }

    public void velocidadeON(){
        velocidadeON = true;
        if(velocidade > MAX_VELOCIDADE){
            velocidade = MAX_VELOCIDADE;
        }
        else{
            velocidade += 0.01f;
        }
    }

    public void devagar(){
        velocidadeON = false;
        if(velocidade <= 0 ){
            velocidade = 0;
        }
        else{
            velocidade -= 0.003f;
        }
    }
}