package GUI;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class StateActive {
    Circle objetoAtivacao;
    public static Color DEF_COLOR = Main.color_states_fsm; //por padrão é verde.
    public Pane rootLayout;
    Timeline timeline;

    public StateActive(Pane rootLayout, int x, int y, int radius){
        this.rootLayout = rootLayout;
        this.objetoAtivacao = new Circle(); //cria um circulo 
        objetoAtivacao.setRadius(radius); // radius 
        objetoAtivacao.setFill(DEF_COLOR); //cor do circulo

        objetoAtivacao.setLayoutX(x); 
        objetoAtivacao.setLayoutY(y);
        objetoAtivacao.setVisible(false);
        //Effects.efeitoBloom(objetoAtivacao);
        //Effects.efeitoBoxBlur(objetoAtivacao);
        Effects.efeitoGaussianBlur(objetoAtivacao);
        rootLayout.getChildren().add(objetoAtivacao);//insere o circulo no Pane
    }



    public void ativar(){
        modificaCor();
        objetoAtivacao.setVisible(true); // torna visível
        animacaoAtivacao();
    }

    public void desativar(){
        if (this.timeline != null) {
            this.timeline.stop(); // para a animação
        }
        objetoAtivacao.setVisible(false); // torna invisível
    }

    public void modificaCor(){
        this.DEF_COLOR = Main.color_states_fsm;
        objetoAtivacao.setFill(DEF_COLOR);
        
    }


    public void rising_edge(){ //faz o fundo do botão de clock brilhar verde até que o estado atual finalize
        objetoAtivacao.setVisible(true);
    }

    public void falling_edge(){ //faz a luz verde desaparecer
        objetoAtivacao.setVisible(false);
    }


    private void animacaoAtivacao() {
        desativaAnimacaoTodosEstados(Main.stateActive); // antes de executar a animação pro estado atual, desativa a animação dos estados que já foram executados.
        this.timeline = new Timeline( //cria uma nova animação
            new KeyFrame(Duration.ZERO, new KeyValue(objetoAtivacao.opacityProperty(), 1.0)),
            new KeyFrame(new Duration(500), new KeyValue(objetoAtivacao.opacityProperty(), 0.5)),
            new KeyFrame(new Duration(1000), new KeyValue(objetoAtivacao.opacityProperty(), 1.0))
        );
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.setAutoReverse(true);
        this.timeline.play();
    }


    public void desativaAnimacaoTodosEstados(ArrayList<StateActive> stateActive){
        for(StateActive a : stateActive){
            if (a.timeline != null) {
                a.timeline.stop(); // para a animação
            }
        }
    }
}
