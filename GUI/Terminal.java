package GUI;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;

public class Terminal {
    private Timeline timeline; 
    private Line terminal;
    private Line energia;
    private Color cor_energia;
    private String plano;
    private String direcao;
    private double largura_terminal;
    private double VELOCIDADE_ENERGIA = 1.0;
    private boolean uc_terminal;
    private boolean energizado; 
    private int id; //identificado dos terminais
    private static boolean efeitoBoxBlur = false;


    public Terminal(int id, double startX, double startY, double endX, double endY, String plano, String direcao, Color cor_energia, boolean gradiente, double largura_terminal, double velocidade_energia, boolean uc_terminal) {
        terminal = new Line(startX, startY, endX, endY);
        terminal.setStrokeWidth(largura_terminal);
        terminal.setStroke(Color.GRAY); //por padrão, os terminais são cinza.
        this.plano = plano; // o plano da reta (terminal), se é horizontal ou vertical
        this.direcao = direcao; // o sentido do terminal, se é: cima; baixo; direita; esquerda.
        
        if(this.direcao.equals("esquerda") || this.direcao.equals("cima")){
            energia = new Line(endX, endY, endX, endY); //começa da extrema direita e vem sentido esquerda.
        }else{
            energia = new Line(startX, startY, startX, startY); //começa da extrema esquerda e vem sentido direita.
        }
        this.id = id;
        this.uc_terminal = uc_terminal; //indica se é um terminal da Unidade de controle ou não.
        this.energizado = false; //indica se esta energizado ou nao, por padrão é falso.
        this.cor_energia = cor_energia;
        this.VELOCIDADE_ENERGIA = velocidade_energia;
        this.largura_terminal = largura_terminal;
        energia.setStrokeWidth(largura_terminal);
        energia.setStroke(cor_energia);
        energia.setVisible(false);

        /*if(this.cor_energia == Color.AQUA){ //condição para terminais da UC que possuem cor padrão diferente.
        }*/

    }

    public Line getTerminal() { return terminal; }
    public Line getEnergia() { return energia; }
    public int getId() { return id;}
    public Status getStatusTimeline() { return this.timeline.getStatus();}
    public Timeline getTimeline() { return this.timeline;}
    public boolean getEnergizado() { return this.energizado;}

    public void setEnergizado(boolean bool){ this.energizado = bool;}

    public static void setEfeitoBoxBlur() { 
        if(efeitoBoxBlur){
            efeitoBoxBlur = false;
        }else{
            efeitoBoxBlur = true;
        }
    }



    public Timeline energizar() {
        this.cor_energia = Main.getCorEnergia(); //atualiza a cor caso o usuario tenha selecionado uma nova cor
        energia.setStrokeWidth(largura_terminal);
        
        //atualiza a configuração da cor da energia que foi inicializada no construtor na linha 35 e 36).
        if(Main.getGradiente() && !(uc_terminal)){ //opção do menu settings para adicionar um gradiente na energia. Condição adicionada para impedir que altere a cor da anergia de terminais da UC
            Stop[] stops;
            if(energizado){
                stops = new Stop[] { new Stop(0, Color.RED), new Stop(1, Color.YELLOW) };
                desenergizar();
                energizado = false;
            }else{
                stops = new Stop[] { new Stop(0, Color.ORANGERED), new Stop(1, Color.ORANGE) };
                desenergizar();
                energizado = true;
            }
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            energia.setStroke(gradient);
            energia.setStrokeWidth(2);
            energizado = true;
        }else{ // opção de cor padrão 
            if(uc_terminal){ //condição para manter a cor padrão dos terminais da UC.
                if(energizado){
                    energia.setStroke(Color.AQUAMARINE); //cor alternativa para quando o terminal já estiver energizado.
                    desenergizar(); //desenergiza os terminais, faz com que o endereço (endX, endY) da energia passe a ser (startX, startY)
                    energizado = false; //ainda continuara energizado, mas seta como false para alternar para Color.AQUA na proxima vez que chamar este metodo energizar().
                }else{
                    energia.setStroke(Color.AQUA); 
                    desenergizar();
                    energizado = true;
                }

            }else{ 
                if(energizado){
                    energia.setStroke(Color.ORANGE); //cor alternativa para quando o terminal já estiver energizado.
                    desenergizar();
                    energizado = false;
                } else{
                    energia.setStroke(cor_energia); //cor padrão ou a selecionada em "Theme"
                    desenergizar();
                    energizado = true;
                }        
            }
        }
        energia.setVisible(true);

        VELOCIDADE_ENERGIA = Main.getSpeedTerminal(); //atualiza velocidade a depender da velocidade escolhida pelo usuario em settings.
        double incremento;
        double[] end = new double[2];
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
        }

        // determinando o fluxo da energia com base nos parametros passados na chamada da função.
        if(direcao.equals("direita") || direcao.equals("baixo")){
            incremento = VELOCIDADE_ENERGIA; //velocidade com que a energia vai avançar nos terminais.
            end[0] = terminal.getEndX();
            end[1] = terminal.getEndY();
        }else if(direcao.equals("esquerda") || direcao.equals("cima")){
            incremento = VELOCIDADE_ENERGIA * -1;
            end[0] = terminal.getStartX();
            end[1] = terminal.getStartY();
        } else{
            throw new IllegalArgumentException("[Terminal.java]: energizar() - ERRO: direcao inválida - " + direcao);
        }

        if(efeitoBoxBlur){ //aplica efeito ao carregar o terminal
            Effects.efeitoBoxBlur(energia); //legal esse efeito
            //Effects.efeitoReflection(energia);
            //Effects.efeitoBloom(energia); //sem efeito
            //Effects.efeitoDisplacement(energia); //sem efeito, pelo menos para esse tipo de objeto
            //Effects.efeitoGaussianBlur(energia); //mesma coisa do BoxBlur
            //Effects.efeitoGlow(energia); 
            //Effects.efeitoInnerShadow(energia); 
            //Effects.efeitoLighting("Distant", energia); 
            //Effects.efeitoLighting("Point", energia); 
            //Effects.efeitoLighting("Spot", energia); 
            //Effects.efeitoMotionBlur(energia); //mesma coisa que o BoxBlur e GaussianBlur
            //Effects.efeitoSepiaTone(energia); //bom
            //Effects.efeitoShadow(energia);
            //Effects.efeitoColorAdjust(energia);
            //Effects.efeitoBlend(Effects.efeitoMotionBlur(energia), Effects.efeitoGlow(energia));
        }



        //animação
        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            if (plano.equals("horizontal")) {
                double atual = energia.getEndX();
                // logica de Movimento com incremento sem ultrapassar o limite (end[0]) - Suponha atual=450 e end[0]=500, plano horizontal e VELOCIDADE_ENERGIA=10.0
                // 1º -> MIN(atual=460, end[0]=500) return atual
                // 2º -> MIN(atual=470, end[0]=500) return atual
                // 3º -> MIN(atual=480, end[0]=500) return atual
                // 4º -> MIN(atual=490, end[0]=500) return atual
                // 5º -> MIN(atual=500, end[0]=500) return atual
                // 6º -> MIN(atual=510, end[0]=500) return end[0]     (o calculo de movimento se limitaria ao limite end[0] como deve ser)
                if (atual < end[0]) {
                    energia.setEndX(Math.min(atual + incremento, end[0])); 
                    if (energia.getEndX() == end[0]) {
                        timeline.stop();
                    }
                } else if (atual > end[0]) {
                    energia.setEndX(Math.max(atual + incremento, end[0])); 
                    if (energia.getEndX() == end[0]) {
                        timeline.stop();
                    }
                } else {
                    timeline.stop();
                }
            } else if (plano.equals("vertical")) {
                double atual = energia.getEndY();
                if (atual < end[1]) {
                    energia.setEndY(Math.min(atual + incremento, end[1])); 
                    if (energia.getEndY() == end[1]) {
                        timeline.stop();
                    }
                } else if (atual > end[1]) {
                    energia.setEndY(Math.max(atual + incremento, end[1])); 
                    if (energia.getEndY() == end[1]) {
                        timeline.stop();
                    }
                } else {
                    timeline.stop();
                }
            } else {
                throw new IllegalArgumentException("[Terminal.java]: energizar() - ERRO: plano inválido - " + plano);
            }

            

        });

        //efeitoPisca(energia, keyFrame);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return timeline; //o retorno não é usado, mas se for usar futuramente em um objeto SequentialTransition, pode ser necessário.
    }

    public void desenergizar() {
        energia.setEndX(energia.getStartX()); //seta as coordenadas para voltar pra posição inicial da energia.
        energia.setEndY(energia.getStartY());
        energia.setVisible(false);
        
    }

    public void setLayoutX(double x) {
        terminal.setLayoutX(x);
        energia.setLayoutX(x);
    }

    public void setLayoutY(double y) {
        terminal.setLayoutY(y);
        energia.setLayoutY(y);
    }


    /*public void efeitoPisca(Line line, KeyFrame keyFrame){ // Criação da animação com o efeito de pisca
       

        // Adiciona o KeyFrame de movimento
        timeline.getKeyFrames().add(keyFrame);

        // Cria e adiciona o KeyFrame de piscar
        KeyFrame blinkKeyFrame = new KeyFrame(Duration.millis(50), event -> {
            energia.setVisible(!energia.isVisible());
        });
        timeline.getKeyFrames().add(blinkKeyFrame);

        // Define o ciclo da animação para repetir até que o movimento seja concluído
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Evento para parar a animação de piscar quando o movimento terminar
        timeline.setOnFinished(event -> {
            energia.setVisible(true); // Garante que a linha energia fique visível ao final
        });

        timeline.play();
    }*/

    
}
