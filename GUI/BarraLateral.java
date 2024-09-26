package GUI;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.ToggleSwitch;

import MIPS.Memory;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

public class BarraLateral {
    Pane rootLayout;
    public static final Color COR_BAR = Color.LIMEGREEN; //[NOTA]: A cor do nome dos registradores é alterada diretamente no 'estilos.css', na classe '.text-box-registers'

    public static final int POS_X_BAR_LEFT = 0; // coordenadas de posição da barra lateral esquerda
    public static final int POS_Y_BAR_LEFT = 270;

    public static final int POS_X_BAR_RIGHT = 1640; // coordenadas de posição da barra lateral direita
    public static final int POS_Y_BAR_RIGHT = 0;

    public static final int DIM_X_INSTR_BAR = 237; // dimensões da barra de instruções da barra lateral direita
    public static final int DIM_Y_INSTR_BAR = 50;

    public static final int DIM_X_REGSET_BAR = 237; // dimensões do background da seção Register set.
    public static final int DIM_Y_REGSET_BAR = 510;

    public static final int DIM_X_REGBOX_BAR = 85; // dimensões de cada caixinha onde vai ficar os registradores.
    public static final int DIM_Y_REGBOX_BAR = 23;

    public static final String URL_IMG_TEXT = "file:recursos/text-background.png";
    
    //memorias:
    private ListView<String> TextMemory;
    private ListView<String> DynamicMemory;
    private ListView<String> GlobalMemory;
    //registradores:
    private static ArrayList<Text> registradores; // tal como no RegisterFile.java, os registradores serão representados por um ArrayList.
    private static final ArrayList<String> NAME_REGISTERS = new ArrayList<String>(Arrays.asList(
        " $0","$at","$v0","$v1","$a0","$a1","$a2","$a3","$t0","$t1","$t2","$t3","$t4","$t5","$t6","$t7",
            "$s0","$s1","$s2","$s3","$s4","$s5","$s6","$s7","$t8","$t9","$k0","$k1","$gp","$sp","$fp","$ra"
            ));;
    //atributos relacionados aos appearances
    private static Group neonFrameGroup;
    private static Group goldenFrameGroup;
    private static Text TEXTSegment, DYNAMICSegment, GLOBALSegment, textMoldura, textNeonFrame, instruction, registerSet;
    //demais atributos:
    private static Text instr; //irá conter a instrução atual
    private static Text instr_bin; //irá conter a instrução atual em binario
    private static Text type_instr; //contem o tipo da instrução
    private static Text type_text;
    private static TextArea TEXTArea;
    private static List<Text> regList;
    public static ToggleSwitch toggleSwitch1;
    public static ToggleSwitch toggleSwitch2;



    public BarraLateral(Pane rootLayout) {

        this.rootLayout = rootLayout;

        

        // #####################################- (Definindo a Barra Lateral esquerda) -########################################
        Image imageLabel = new Image("file:recursos/background-lateral.png");
        ImageView imageViewBarLeft= new ImageView(imageLabel);
        imageViewBarLeft.setFitWidth(POS_Y_BAR_LEFT+10); 
        imageViewBarLeft.setFitHeight(Main.back_y);
        imageViewBarLeft.getStyleClass().add("image-view-custom");

        Label labelBarraLateral = new Label();
        labelBarraLateral.setGraphic(imageViewBarLeft);
        labelBarraLateral.setLayoutX(POS_X_BAR_LEFT);
        labelBarraLateral.setLayoutY(0);
        labelBarraLateral.setPrefWidth(POS_Y_BAR_LEFT+10);
        labelBarraLateral.setPrefHeight(Main.back_y);
        labelBarraLateral.getStyleClass().add("label-custom");
        rootLayout.getChildren().addAll(labelBarraLateral); //adiciona a imagem de background da barra lateral esquerda

        // ________________________________ (Criando a caixa de inserção de texto assembly) ________________________________________
        TEXTArea = createTextArea(10,35,250,198); //cria uma caixa de texto já com os estilos aplicados
        
        

        
        
        // ________________________________ (Criando a estrutura de memoria e demais itens gráficos) ________________________________________
        
        //TEXT Segment
        TEXTSegment = createTitle("TEXT Segment", COR_BAR, 94, 360, 14, 0, true, "low"); //cria um texto de titulo já com efeitos de CSS e imagem (Label) de background
        TextMemory = createListMemory(28, 395, 0.45, 1.5, Color.ORANGE, "text"); //cria uma listview que conterá os dados do Segmento de memoria especificado.
        //DYNAMIC DATA Segment
        DYNAMICSegment = createTitle("DYNAMIC DATA Segment", COR_BAR, 57, 570, 14, 0, true, "low");
        DynamicMemory = createListMemory(28, 605, 0.45, 1.5, Color.ORANGE, "dynamic");
        //GLOBAL DATA Segment
        GLOBALSegment = createTitle("GLOBAL DATA Segment", COR_BAR, 60, 780, 14, 0, true, "low");
        GlobalMemory = createListMemory(28, 815, 0.45, 1.5, Color.ORANGE, "global");


        // ________________________________ (Texto "MEMORY") ________________________________________
        textMoldura = new Text(103, 292, "MEMORY");
        textMoldura.setStroke(COR_BAR);
        textMoldura.setFont(new Font(17));

        rootLayout.getChildren().add(textMoldura);


        // #####################################- (Definindo a Barra Lateral Direita) -########################################

        // ____________________________________________ (imagem de background) ________________________________________________
    
        ImageView imageViewBarRight = new ImageView(imageLabel);
        imageViewBarRight.setLayoutX(POS_X_BAR_RIGHT);
        imageViewBarRight.setLayoutY(POS_Y_BAR_RIGHT);
        imageViewBarRight.setFitHeight(Main.back_y);
        imageViewBarRight.setFitWidth(POS_Y_BAR_LEFT-10); 
        imageViewBarRight.getStyleClass().add("image-view-custom-2");

        rootLayout.getChildren().add(imageViewBarRight);

        // ____________________________________________ (seção da instrução e seu tipo) ________________________________________________

        instruction = createTitle("Instruction", COR_BAR, POS_X_BAR_RIGHT+82, POS_Y_BAR_RIGHT+38, 18, 1, true, "medium"); 
        this.type_text = createTitle("Type:", COR_BAR, POS_X_BAR_RIGHT+190, POS_Y_BAR_RIGHT+75, 12, 1, false, "medium");  // texto adicional, portanto false.
        this.type_instr = createTitle("?", Color.ORANGE, POS_X_BAR_RIGHT+222, POS_Y_BAR_RIGHT+75, 12, 1, false, "medium"); 

        createbackgroundToText(POS_X_BAR_RIGHT+10, POS_Y_BAR_RIGHT+80, DIM_X_INSTR_BAR, DIM_Y_INSTR_BAR, false, null);
        this.instr = createTitle("wait instr...", Color.WHITE, POS_X_BAR_RIGHT+25, POS_Y_BAR_RIGHT+102, 18, 1, false, "medium"); // texto da instrução, e.g: add $t0, $t1, $t2
        this.instr_bin = createTitle("00000000000000000000000000000000", Color.GRAY, POS_X_BAR_RIGHT+25, POS_Y_BAR_RIGHT+117, 12, 1, false, "medium");

        this.instr.getStyleClass().add("text-normal");
        this.instr_bin.getStyleClass().add("text-normal-2");
        
        // ____________________________________________ (seção do conjunto dos 32 registradores) ________________________________________________
        
        registerSet = createTitle("Register Set", COR_BAR, POS_X_BAR_RIGHT+80, POS_Y_BAR_RIGHT+190, 18, 1, true, "medium"); 
        createbackgroundToText(POS_X_BAR_RIGHT+10, POS_Y_BAR_RIGHT+230, DIM_X_REGSET_BAR, DIM_Y_REGSET_BAR, false, null);
        

        this.registradores = new ArrayList<>();
        for (int i = 0; i < 32; i++) { 
            this.registradores.add(new Text("00000000")); //inicializa com 8 digitos hexadecimais
        } 
        // --------------------------- (criando as boxes para cada registrador e adicionando em uma linked list para fazer modificações futuras) -----------------------------
        regList = new ArrayList<Text>();
        // Fileira 1:
        regList.add(createBoxRegister(0, COR_BAR, 30, 250));
        regList.add(createBoxRegister(1, COR_BAR, 30, 280));
        regList.add(createBoxRegister(2, COR_BAR, 30, 310));
        regList.add(createBoxRegister(3, COR_BAR, 30, 340));
        regList.add(createBoxRegister(4, COR_BAR, 30, 370));
        regList.add(createBoxRegister(5, COR_BAR, 30, 400));
        regList.add(createBoxRegister(6, COR_BAR, 30, 430));
        regList.add(createBoxRegister(7, COR_BAR, 30, 460));
        regList.add(createBoxRegister(8, COR_BAR, 30, 490));
        regList.add(createBoxRegister(9, COR_BAR, 30, 520));
        regList.add(createBoxRegister(10, COR_BAR, 30, 550));
        regList.add(createBoxRegister(11, COR_BAR, 30, 580));
        regList.add(createBoxRegister(12, COR_BAR, 30, 610));
        regList.add(createBoxRegister(13, COR_BAR, 30, 640));
        regList.add(createBoxRegister(14, COR_BAR, 30, 670));
        regList.add(createBoxRegister(15, COR_BAR, 30, 700));
        //fileira 2:
        regList.add(createBoxRegister(16, COR_BAR, 130, 250));
        regList.add(createBoxRegister(17, COR_BAR, 130, 280));
        regList.add(createBoxRegister(18, COR_BAR, 130, 310));
        regList.add(createBoxRegister(19, COR_BAR, 130, 340));
        regList.add(createBoxRegister(20, COR_BAR, 130, 370));
        regList.add(createBoxRegister(21, COR_BAR, 130, 400));
        regList.add(createBoxRegister(22, COR_BAR, 130, 430));
        regList.add(createBoxRegister(23, COR_BAR, 130, 460));
        regList.add(createBoxRegister(24, COR_BAR, 130, 490));
        regList.add(createBoxRegister(25, COR_BAR, 130, 520));
        regList.add(createBoxRegister(26, COR_BAR, 130, 550));
        regList.add(createBoxRegister(27, COR_BAR, 130, 580));
        regList.add(createBoxRegister(28, COR_BAR, 130, 610));
        regList.add(createBoxRegister(29, COR_BAR, 130, 640));
        regList.add(createBoxRegister(30, COR_BAR, 130, 670));
        regList.add(createBoxRegister(31, COR_BAR, 130, 700));


        // _________________________________ (Seção p/ criação de Toggle Switchs) ________________________________________
        toggleSwitch1 = createToggleSwitch("PARALLEL SIMULATION", 1830, 925, 1694, 937, DataPath::setEnableParallel, DataPath::setEnableParallel, true);
        toggleSwitch2 = createToggleSwitch("SHOW DETAILS", 1830, 960, 1734, 973, InfoPath::TextoON, InfoPath::TextoOFF, true);


        // ________________________________ (Criando molduras para agrupar os segmentos de memoria) ________________________________________
        createNeonFrame(Color.LIME, Color.GREEN); 
        showNeonFrame(false); //não mostra a appearance secundaria
        
        createGoldenFrame();//Appearance principal
        showGoldenFrame(true); 

        // ________________________________ (Destaque de itens em execução no segmento TEXT) ________________________________________
        // Observa mudanças na propriedade current_instruction para destacar os itens do segmento TEXT
        Memory.current_instruction.addListener((obs, oldVal, newVal) -> {
            TextMemory.refresh(); // Atualiza a lista
        });

        Memory.current_instruction.set(0);

        
        
    }   










    public TextArea createTextArea(double layoutX, double layoutY, double prefWidth, double prefHeight){
        TextArea TEXTArea = new TextArea();
        TEXTArea.setLayoutX(layoutX);
        TEXTArea.setLayoutY(layoutY);
        TEXTArea.setPrefWidth(prefWidth); // Ajusta a largura do TextArea
        TEXTArea.setPrefHeight(prefHeight); // Ajusta a altura do TextArea
        TEXTArea.getStyleClass().add("text-area-custom"); // Aplica o estilo CSS
        this.rootLayout.getChildren().addAll(TEXTArea);
        return TEXTArea;
    }

    public Text createTitle(String nomeTitulo, Color cor, double layoutX, double layoutY, int TAM_font, int bar, boolean enableBackgroundImage, String size){
        int layoutX_background = 0;
        if(bar == 0){ // parametro bar especifica qual barra irá ser considerada pra posicionar a imagem de background
            layoutX_background = POS_X_BAR_LEFT; //barra esquerda
        } else{
            layoutX_background = POS_X_BAR_RIGHT; //barra direita
        }
        Text TEXTsegment = new Text(nomeTitulo);
        TEXTsegment.setStroke(cor);
        TEXTsegment.setLayoutX(layoutX);
        TEXTsegment.setLayoutY(layoutY);
        TEXTsegment.setFont(new Font(TAM_font));

        if(enableBackgroundImage){ // "enableBackgroundImage" habilita ou nao a inserção de imagem de fundo.
            Image imageBackgroundTexts = new Image(URL_IMG_TEXT); //para o background dos textos
            ImageView imageView = new ImageView(imageBackgroundTexts);
            Label backgroundTEXTsegment = new Label();

            if(size.equals("low")){
                imageView.setFitHeight(30);
                imageView.setFitWidth(POS_Y_BAR_LEFT-30);
                backgroundTEXTsegment.setLayoutX(layoutX_background+20);
                backgroundTEXTsegment.setLayoutY(layoutY-19);
                backgroundTEXTsegment.setPrefHeight(30); 
                backgroundTEXTsegment.setPrefWidth(240); 
            }else if(size.equals("medium")){
                imageView.setFitHeight(40);
                imageView.setFitWidth(POS_Y_BAR_LEFT+8); 
                backgroundTEXTsegment.setLayoutX(layoutX_background);
                backgroundTEXTsegment.setLayoutY(layoutY-25);
                backgroundTEXTsegment.setPrefHeight(40); 
                backgroundTEXTsegment.setPrefWidth(250); 
            }

            imageView.getStyleClass().add("image-view-custom");
            backgroundTEXTsegment.setGraphic(imageView);
            backgroundTEXTsegment.getStyleClass().add("label-custom");
            this.rootLayout.getChildren().addAll(backgroundTEXTsegment, TEXTsegment);

        } else {
            this.rootLayout.getChildren().addAll(TEXTsegment);
        }

        return TEXTsegment;
    }

    public ListView<String> createListMemory(double layoutX, double layoutY, double transparencia, double diametro_linha, Color cor_linha, String segmento){
        Line TEXTlinha = new Line(layoutX+90, layoutY+20, layoutX+90, layoutY+123);
        TEXTlinha.setStrokeWidth(diametro_linha);
        TEXTlinha.setStroke(cor_linha);

        // --------------------------- (Criando a parte superior da listview para agrupar a descrição dos bytes e endereço) -----------------------
        HBox fixedHeader = new HBox();
        //fixedHeader.getStyleClass().add("list-view-custom");
        Text headerText = new Text("    Address     0   1   2   3");
        headerText.setStyle("-fx-text-fill: white; -fx-font-size: 9pt; -fx-opacity: 0.5; -fx-font-family: 'Consolas';");
        fixedHeader.getChildren().add(headerText);


        // --------------------------- (Criando o listview que irá representar os segmentos de memoria) -----------------------
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("0x00000000   00  00  00  00", "0x00000000   00  00  00  00", 
        "0x00000000   00  00  00  00", "0x00000000   00  00  00  00", "0x00000000   00  00  00  00"); //texto padrão apenas para inicializar a lista. Pra inserir valores na lista usar metodo "insertOnListMemory()".           
        listView.setLayoutX(layoutX); 
        listView.setLayoutY(layoutY); 
        listView.setPrefWidth(220);//padrão 250
        listView.setPrefHeight(120); //padrão 178
        listView.setOpacity(transparencia);
        listView.getStyleClass().add("list-view-custom"); // Aplica o estilo CSS
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item);
                            if (getIndex() == Memory.current_instruction.get() && segmento.equals("text")) { // colocar cor diferente apenas nas palavras carregadas
                                setStyle("-fx-background-color: lightblue; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: black; -fx-border-width: 1; -fx-opacity: 1.0; -fx-font-family: 'Consolas';");
                                
                            } else {
                                setStyle("-fx-text-fill: white; -fx-font-family: 'Consolas';"); // maior precedencia de acordo com o criador da Jmetro.
                            }
                            //configura cor e tipo de fonte do texto.
                            
                        }
                    }
                };
            }
        });

        // Layout principal
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(fixedHeader);
        borderPane.setCenter(listView);
        borderPane.setLayoutX(layoutX);
        borderPane.setLayoutY(layoutY);

        
        this.rootLayout.getChildren().addAll(borderPane, TEXTlinha);
        return listView;
    }



    public Label createbackgroundToText(double layoutX, double layoutY, double pref_X, double pref_Y, boolean enableImage, ImageView imageView){
        Label backgroundTEXTsegment = new Label();
        if(enableImage){
            backgroundTEXTsegment.setGraphic(imageView); // Se a imagem não tiver as dimensões (pref_X, pref_Y), configure com setFitHeight() e setFitWidth().
        }
        backgroundTEXTsegment.setLayoutX(layoutX);
        backgroundTEXTsegment.setLayoutY(layoutY);
        backgroundTEXTsegment.setPrefWidth(pref_X); //tamanho da label X
        backgroundTEXTsegment.setPrefHeight(pref_Y); //tamanho da label Y
        backgroundTEXTsegment.getStyleClass().add("label-text-background");
        this.rootLayout.getChildren().add(backgroundTEXTsegment);
        return backgroundTEXTsegment;
    }

    public Text createBoxRegister(int indexReg, Color cor, int deslocamentoX, int deslocamentoY){ //cria as caixas onde o valor dos registradores serão mostrados. 

        Text TEXTreg = new Text(NAME_REGISTERS.get(indexReg)+":");
        TEXTreg.setLayoutX(POS_X_BAR_RIGHT+deslocamentoX+5);
        TEXTreg.setLayoutY(POS_Y_BAR_RIGHT+deslocamentoY+17);
        TEXTreg.setFont(new Font(12));
        TEXTreg.getStyleClass().add("text-box-registers");

        registradores.get(indexReg).setLayoutX(POS_X_BAR_RIGHT+deslocamentoX+30);
        registradores.get(indexReg).setLayoutY(POS_Y_BAR_RIGHT+deslocamentoY+17);
        registradores.get(indexReg).setFont(new Font(11));
        //this.registradores.get(indexReg).setText("00000000"); //inicializa com 8 digitos hexadecimais

        Label backgroundRegister = new Label();
        backgroundRegister.setGraphic(new ImageView("file:recursos/background_regbox.png")); // o innershadow do css do javafx é muito ruim, então tive que pegar uma imagem com innershadow aplicado do figma.com 
        backgroundRegister.setLayoutX(POS_X_BAR_RIGHT+deslocamentoX);
        backgroundRegister.setLayoutY(POS_Y_BAR_RIGHT+deslocamentoY);
        backgroundRegister.setPrefWidth(DIM_X_REGBOX_BAR); 
        backgroundRegister.setPrefHeight(DIM_Y_REGBOX_BAR); 
        
        this.rootLayout.getChildren().addAll(backgroundRegister, TEXTreg, registradores.get(indexReg));

        return TEXTreg;
    } 

    public ToggleSwitch createToggleSwitch(String nameAction, double layoutX, double layoutY, double layoutTextX, double layoutTextY, Runnable onAction, Runnable offAction, boolean enableSelect){
        //switch para habilitar ou nao o texto nos módulos
        ToggleSwitch toggleSwitch = new ToggleSwitch(); 
        toggleSwitch.setLayoutX(layoutX);
        toggleSwitch.setLayoutY(layoutY);
        toggleSwitch.setSelected(enableSelect); //parametro 'enableSelect' determina se a chave vai ser iniciada em 'ON' ou em 'OFF'

        Text text1 = new Text(layoutTextX, layoutTextY, nameAction);
        text1.setFont(new Font(11));
        Label statusLabel1 = new Label();
        statusLabel1.setLayoutX(1810); 
        statusLabel1.setLayoutY(layoutTextY-5); 
        statusLabel1.setFont(new Font(9));
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                statusLabel1.setText("ON");
                statusLabel1.setTextFill(Color.GREEN);
                onAction.run();  // Executa a ação "ON"
                //InfoPath.visibilidadeTextoON();
            } else {
                statusLabel1.setText("OFF");
                statusLabel1.setTextFill(Color.RED);
                offAction.run();  // Executa a ação "OFF"
                //InfoPath.visibilidadeTextoOFF();
            }
        });

        rootLayout.getChildren().addAll(toggleSwitch, statusLabel1, text1);

        Platform.runLater(() -> {
            statusLabel1.setText("ON");
            statusLabel1.setTextFill(Color.GREEN);
        });
        
        return toggleSwitch;
    }

    public void setInstructionBIN(String instruction){
        this.instr_bin.setText(instruction);
    }

    public void setInstruction(String instruction){
        this.instr.setText(instruction);
    }
    
    public void setTypeInstr(String type){
        this.type_instr.setText(type);
    }
    
    //para inserção nas memorias
    public void insertOnTextMemory(String palavra){
        this.TextMemory.getItems().add(palavra); 
    }

    public void insertOnDynamicMemory(String palavra){
        this.DynamicMemory.getItems().add(palavra);
    }

    public void insertOnGlobalMemory(String palavra){
        this.GlobalMemory.getItems().add(palavra);
    }

    //para inserção nos registradores
    public static void insertOnRegister(int num_reg, String valor){
        registradores.get(num_reg).setText("0x"+valor);
        InfoPath.animacaoTextual(registradores.get(num_reg), Main.animacao); //insere animação ao atualizar o valor de um registrador na barra lateral direita
    }

    public void resetRegisters(){
        for (int i = 0; i < 32; i++) { 
            registradores.get(i).setText("00000000");//reinicializa com 8 digitos hexadecimais
        } 
    }

    public void resetList(String segment){ //exclui todos os dados graficamente do segmento de memoria informado. Nao exclui os dados na memoria de endereçamento do MIPS -> Memory
        if(segment.equals("Text")){
            this.TextMemory.getItems().clear();
        }else if(segment.equals("Dynamic")){
            this.DynamicMemory.getItems().clear();
        }else if(segment.equals("Global")){
            this.GlobalMemory.getItems().clear();
        }/* insira para Reserved 1 e Reserved 2 quando os implementar */
        else{
            throw new IllegalArgumentException("[BarraLateral.java]: resetList() - ERRO: Segmento de memória inválido - "+segment);
        }
    }

    public void saveTextToFile(String text) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("code.asm"))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

    public TextArea getTEXTArea(){ return this.TEXTArea;}

    public void createNeonFrame(Color primary, Color secondary){ //moldura principal para destacar as memorias.
        neonFrameGroup = new Group(); // Contêiner para agrupar todos os elementos

        StackPane stackPaneNeonFrame = new StackPane();
        stackPaneNeonFrame.setStyle("-fx-background-color: black;  -fx-border-radius: 10; -fx-background-radius: 10; -fx-opacity: 0.70; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,1.0), 10, 0, 0, 5);"); // Define o plano de fundo como preto
        textNeonFrame = new Text(103, 292, "  MEMORY  ");
        textNeonFrame.setStroke(COR_BAR);
        textNeonFrame.setFont(new Font(17));// Define a cor do texto como amarelo
        stackPaneNeonFrame.getChildren().add(textNeonFrame);
        stackPaneNeonFrame.setLayoutX(94);
        stackPaneNeonFrame.setLayoutY(274);

         // Criação do retângulo maior para desfoque
        Rectangle blurFrame = new Rectangle(14, 286, 252, 700);
        blurFrame.setStroke(primary);
        blurFrame.setFill(null);
        //blurFrame.setFill(Color.TRANSPARENT); // Interior transparente
        blurFrame.setStrokeWidth(10);
        blurFrame.setArcWidth(50); 
        blurFrame.setArcHeight(50); 

        // Criação da moldura neon com bordas arredondadas
        Rectangle neonFrame = new Rectangle(14, 286, 252, 700);
        neonFrame.setStroke(secondary);
        neonFrame.setFill(null);
        neonFrame.setStrokeWidth(10);
        neonFrame.setArcWidth(50); // Bordas arredondadas
        neonFrame.setArcHeight(50); // Bordas arredondadas

        // Efeito de brilho
        Glow glow = new Glow(0.0);
        GaussianBlur blur = new GaussianBlur(0.0);
        blurFrame.setEffect(blur);// Aplicando o desfoque ao retângulo maior
        neonFrame.setEffect(glow);// Empilhando os efeitos no retângulo menor

        // Animação do brilho aumentando lentamente
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), 
                new KeyValue(glow.levelProperty(), 0.0),
                new KeyValue(blur.radiusProperty(), 0.0)
            ),
            new KeyFrame(Duration.seconds(5), 
                new KeyValue(glow.levelProperty(), 2.0),
                new KeyValue(blur.radiusProperty(), 50.0) // Ajuste este valor conforme necessário
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();

        neonFrameGroup.getChildren().addAll(blurFrame, neonFrame, stackPaneNeonFrame);
        rootLayout.getChildren().add(neonFrameGroup);
        //rootLayout.getChildren().addAll(blurFrame, neonFrame, stackPaneNeonFrame);
    }


    public void createGoldenFrame(){
        //createNeonFrame(Color.YELLOWGREEN, Color.YELLOW);
        goldenFrameGroup = new Group();

        Image imageLabel2 = new Image("file:recursos/moldura-memoria.png");
        ImageView imageViewMoldura = new ImageView(imageLabel2);
        imageViewMoldura.setFitWidth(285); 
        imageViewMoldura.setFitHeight(740);
        imageViewMoldura.setLayoutX(-5);
        imageViewMoldura.setLayoutY(270);

        goldenFrameGroup.getChildren().addAll(imageViewMoldura);
        rootLayout.getChildren().add(goldenFrameGroup);
        //showNeonFrame(true);
    }


    public static void showNeonFrame(boolean show){ 
        neonFrameGroup.setVisible(show); //habilita ou nao a appearance NeonFrame

        if(show){ //necessario pois nao pode alterar a cor quando show = false.
            String estilo = "-fx-stroke: limegreen; -fx-fill: limegreen; -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;  ";
            //String estilo = "-fx-stroke: rgb(255, 215, 0); -fx-fill: rgb(255, 215, 0); -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;";
            //alterando a cor dos textos
            TEXTSegment.setStyle(estilo);
            DYNAMICSegment.setStyle(estilo);
            GLOBALSegment.setStyle(estilo);
            textMoldura.setStyle(estilo);
            textNeonFrame.setStyle(estilo);
            instruction.setStyle(estilo);
            registerSet.setStyle(estilo);
            type_text.setStyle(estilo);


            estilo = "-fx-stroke: none; -fx-fill: limegreen; -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;  ";
            for (Text a : regList){ //altera a cor dos registradores
                a.setStyle(estilo);
            }
            type_instr.setStroke(Color.ORANGE);
        }

    }


    public static void showGoldenFrame(boolean show){
        goldenFrameGroup.setVisible(show); //habilita ou nao a appearance GoldenFrame

        if(show){ 
            String estilo = "-fx-stroke: rgb(255, 215, 0); -fx-fill: rgb(255, 215, 0); -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;";
            //String estilo = "-fx-stroke: limegreen; -fx-fill: limegreen; -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;  ";
            //alterando a cor dos textos
            TEXTSegment.setStyle(estilo);
            DYNAMICSegment.setStyle(estilo);
            GLOBALSegment.setStyle(estilo);
            textMoldura.setStyle(estilo);
            textNeonFrame.setStyle(estilo);
            instruction.setStyle(estilo);
            registerSet.setStyle(estilo);
            type_text.setStyle(estilo);

            estilo = "-fx-stroke: none; -fx-fill: rgb(255, 215, 0); -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;";
            for (Text a : regList){
                a.setStyle(estilo);
            }

            type_instr.setStroke(Color.ORANGERED);
        }
    }
}




    
