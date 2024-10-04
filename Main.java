import java.awt.event.*;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * Modelo para desenvolvimento de exercícios criativos em Java 2D.
 * 
 * @author Prof. Dr. David Buzatto, alterado e finalizado por João Pedro Machado silva
 * @copyright Copyright (c) 2024
 */
public class Main extends Engine {
    // Classe para representar cada bolinha
    private class Bolinha {
        Point2D pos;
        Point2D vel;
        double raio;
        double atrito;
        double elasticidade;
        boolean emArraste;
        Color cor;

        void atualizar( double delta ) {

            if ( !emArraste ) {
                pos.x += vel.x * delta;
                pos.y += vel.y * delta;

                if ( pos.x + raio >= getScreenWidth() ) {
                    pos.x = getScreenWidth() - raio;
                    vel.x = -vel.x * elasticidade;
                } else if ( pos.x - raio <= 0 ) {
                    pos.x = raio;
                    vel.x = -vel.x * elasticidade;
                }

                if ( pos.y + raio >= getScreenHeight() ) {
                    pos.y = getScreenHeight() - raio;
                    vel.y = -vel.y * elasticidade;
                } else if ( pos.y - raio <= 0 ) {
                    pos.y = raio;
                    vel.y = -vel.y * elasticidade;
                }

                vel.x = vel.x * atrito;
                vel.y = vel.y * atrito + GRAVIDADE;
            }

        }

        void desenhar() {
            drawCircle( pos.x, pos.y, raio, cor );
        }

        boolean intercepta( double x, double y ) {
            double cat1 = x - pos.x;
            double cat2 = y - pos.y;

            //double distancia = Math.sqrt( cat1 * cat1 + cat2 * cat2 );
            //return distancia <= raio;

            return cat1 * cat1 + cat2 * cat2 <= raio * raio;
        }
    }

    private static final double GRAVIDADE = 50;
    private Bolinha bolinha;

    private double xOffset;
    private double yOffset;

    private double xAnterior;
    private double yAnterior;

    private List<Bolinha> bolinhas;

    public Main() {
        // cria a janela do jogo ou simulação
        super( 
            800,                  // 800 pixels de largura
            600,                  // 600 pixels de largura
            "Bolinhas",           // título da janela
            true,                 // ativa a suavização (antialiasing)
            60 );                 // 60 quadros por segundo

    }

    /**
     * Processa a entrada inicial fornecida pelo usuário e cria
     * e/ou inicializa os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void criar() {
        bolinha = new Bolinha();

        bolinha.pos = new Point2D( 
            getScreenWidth() / 2, 
            getScreenHeight() / 2
        );

        bolinha.vel = new Point2D( 200, 200 );
        bolinha.raio = 50;
        bolinha.atrito = 0.99;
        bolinha.elasticidade = 0.9;
        bolinha.cor = BLUE;

        bolinhas = new ArrayList<>();
        bolinhas.add( bolinha );
    }

    /**
     * Atualiza os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void atualizar() {
        double delta = getFrameTime();

        for ( Bolinha bolinha : bolinhas ) {
            bolinha.atualizar( delta );
        }
    }

    /**
     * Desenha o estado dos objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void desenhar() {

        for ( Bolinha bolinha : bolinhas ) {
            bolinha.desenhar();
        }
    }

    @Override
    public void tratarMouse( MouseEvent e, MouseEventType met ) {
        if ( met == MouseEventType.PRESSED && e.getButton() == MouseEvent.BUTTON1 ) {

            for(Bolinha b: bolinhas){

                if ( b.intercepta( e.getX(), e.getY() ) ) {
                    b.emArraste = true;
                    xOffset = b.pos.x - e.getX();
                    yOffset = b.pos.y - e.getY();
                    bolinha = b;
                    break;
                }
            }
            
        } else if ( met == MouseEventType.RELEASED && e.getButton() == MouseEvent.BUTTON1 ) {

            for(Bolinha b: bolinhas){
                b.emArraste = false;
            }
            
        } else if ( met == MouseEventType.DRAGGED ) {

            if ( bolinha != null && bolinha.emArraste ) {

                bolinha.pos.x = e.getX() + xOffset;
                bolinha.pos.y = e.getY() + yOffset;

                double delta = getFrameTime();
                bolinha.vel.x = ( bolinha.pos.x - xAnterior ) / delta;
                bolinha.vel.y = ( bolinha.pos.y - yAnterior ) / delta;

                xAnterior = bolinha.pos.x;
                yAnterior = bolinha.pos.y;
            }

        }

        if ( met == MouseEventType.PRESSED && e.getButton() == MouseEvent.BUTTON3 ) {
            Bolinha novaBolinha = new Bolinha();
            novaBolinha.pos = new Point2D( e.getX(), e.getY() );
            novaBolinha.vel = new Point2D( 
                ( Math.random() < 0.5 ? 1 : -1 ) * ( 100 + Math.random() * 200 ),
                ( Math.random() < 0.5 ? 1 : -1 ) * ( 100 + Math.random() * 200 )
            );

            novaBolinha.raio = 10 + Math.random() * 60;
            novaBolinha.atrito = 0.99;
            novaBolinha.elasticidade = 0.9;
            novaBolinha.cor = Color.getHSBColor( (float) Math.random(), 1.0f, 1.0f );

            bolinhas.add( novaBolinha );
        }

    }

    public static void main( String[] args ) {
        new Main();
    }

}