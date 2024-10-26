package GUI;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.ToggleSwitch;

import MIPS.Memory;
import MIPS.PC;
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
    private static Text cycles; //irá conter a quantidade de ciclos da instrução atual
    private static Text type_instr; //contem o tipo da instrução
    //private static Text type_text;
    private static TextArea TEXTArea;
    private static List<Text> regList;
    public static ToggleSwitch toggleSwitch1;
    public static ToggleSwitch toggleSwitch2;
    public static Line line1, line2, line3, line4, line5, line6;
    public static Text opcode, rs, rt, rd, shamt, funct, imm, Addr;
    public static Text text_opcode, text_rs, text_rt, text_rd, text_shamt, text_funct, text_imm, text_Addr;
    public static int pos;
    public static ImageView imageViewBarLeft;
    public static Image imageLabel;
    public static Image imageLabel2;
    public static ImageView imageViewBarRight;



    public BarraLateral(Pane rootLayout) {

        this.rootLayout = rootLayout;

        // #####################################- (Definindo a Barra Lateral esquerda) -########################################
        imageLabel = new Image("file:recursos/background-lateral.png");
        imageLabel2 = new Image("file:recursos/background-lateral-2.png");
        imageViewBarLeft = new ImageView(imageLabel);
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
    
        imageViewBarRight = new ImageView(imageLabel);
        imageViewBarRight.setLayoutX(POS_X_BAR_RIGHT);
        imageViewBarRight.setLayoutY(POS_Y_BAR_RIGHT);
        imageViewBarRight.setFitHeight(Main.back_y);
        imageViewBarRight.setFitWidth(POS_Y_BAR_LEFT-10); 
        imageViewBarRight.getStyleClass().add("image-view-custom-2");

        rootLayout.getChildren().add(imageViewBarRight);

        // ____________________________________________ (seção da instrução e seu tipo) ________________________________________________

        instruction = createTitle("Instruction", COR_BAR, POS_X_BAR_RIGHT+82, POS_Y_BAR_RIGHT+38, 18, 1, true, "medium"); 
        //this.type_text = createTitle("Type:", Color.GRAY, POS_X_BAR_RIGHT+190, POS_Y_BAR_RIGHT+90, 12, 1, false, "medium");  // texto adicional, portanto false.
        
        createbackgroundToText(POS_X_BAR_RIGHT+10, POS_Y_BAR_RIGHT+80, DIM_X_INSTR_BAR, DIM_Y_INSTR_BAR+50, false, null);
        this.instr = createTitle("wait instr...", Color.WHITE, POS_X_BAR_RIGHT+25, POS_Y_BAR_RIGHT+104, 12, 1, false, "medium"); // texto da instrução, e.g: add $t0, $t1, $t2
        this.type_instr = createTitle("", Color.GRAY, POS_X_BAR_RIGHT+200, POS_Y_BAR_RIGHT+104, 12, 1, false, "medium"); 
        this.cycles = createTitle("", Color.GRAY, POS_X_BAR_RIGHT+25, POS_Y_BAR_RIGHT+119, 12, 1, false, "medium"); 

        this.instr.getStyleClass().add("text-normal");
        this.cycles.getStyleClass().add("text-normal-2");
        this.type_instr.getStyleClass().add("text-normal-2");
        //this.type_text.getStyleClass().add("text-normal-2");

        // campos da instrução, como opcode, funct, imm, etc..
        init_binary_fields(1650, 150, 1, Color.GRAY, Color.WHITE);

        // ____________________________________________ (seção do conjunto dos 32 registradores) ________________________________________________
        
        registerSet = createTitle("Register Set", COR_BAR, POS_X_BAR_RIGHT+80, POS_Y_BAR_RIGHT+230, 18, 1, true, "medium"); 
        createbackgroundToText(POS_X_BAR_RIGHT+10, POS_Y_BAR_RIGHT+270, DIM_X_REGSET_BAR, DIM_Y_REGSET_BAR, false, null);
        

        this.registradores = new ArrayList<>();
        for (int i = 0; i < 32; i++) { 
            this.registradores.add(new Text("00000000")); //inicializa com 8 digitos hexadecimais
        } 
        // --------------------------- (criando as boxes para cada registrador e adicionando em uma linked list para fazer modificações futuras) -----------------------------
        regList = new ArrayList<Text>();
        // Fileira 1:
        regList.add(createBoxRegister(0, COR_BAR, 30, 290));
        regList.add(createBoxRegister(1, COR_BAR, 30, 320));
        regList.add(createBoxRegister(2, COR_BAR, 30, 350));
        regList.add(createBoxRegister(3, COR_BAR, 30, 380));
        regList.add(createBoxRegister(4, COR_BAR, 30, 410));
        regList.add(createBoxRegister(5, COR_BAR, 30, 440));
        regList.add(createBoxRegister(6, COR_BAR, 30, 470));
        regList.add(createBoxRegister(7, COR_BAR, 30, 500));
        regList.add(createBoxRegister(8, COR_BAR, 30, 530));
        regList.add(createBoxRegister(9, COR_BAR, 30, 560));
        regList.add(createBoxRegister(10, COR_BAR, 30, 590));
        regList.add(createBoxRegister(11, COR_BAR, 30, 620));
        regList.add(createBoxRegister(12, COR_BAR, 30, 650));
        regList.add(createBoxRegister(13, COR_BAR, 30, 680));
        regList.add(createBoxRegister(14, COR_BAR, 30, 710));
        regList.add(createBoxRegister(15, COR_BAR, 30, 740));
        //fileira 2:
        regList.add(createBoxRegister(16, COR_BAR, 130, 290));
        regList.add(createBoxRegister(17, COR_BAR, 130, 320));
        regList.add(createBoxRegister(18, COR_BAR, 130, 350));
        regList.add(createBoxRegister(19, COR_BAR, 130, 380));
        regList.add(createBoxRegister(20, COR_BAR, 130, 410));
        regList.add(createBoxRegister(21, COR_BAR, 130, 440));
        regList.add(createBoxRegister(22, COR_BAR, 130, 470));
        regList.add(createBoxRegister(23, COR_BAR, 130, 500));
        regList.add(createBoxRegister(24, COR_BAR, 130, 530));
        regList.add(createBoxRegister(25, COR_BAR, 130, 560));
        regList.add(createBoxRegister(26, COR_BAR, 130, 590));
        regList.add(createBoxRegister(27, COR_BAR, 130, 620));
        regList.add(createBoxRegister(28, COR_BAR, 130, 650));
        regList.add(createBoxRegister(29, COR_BAR, 130, 680));
        regList.add(createBoxRegister(30, COR_BAR, 130, 710));
        regList.add(createBoxRegister(31, COR_BAR, 130, 740));


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
        backgroundRegister.setGraphic(new ImageView("file:recursos/background_regbox.png")); 
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

    public static void setCycles(String ciclos){
        cycles.setText(ciclos);
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
            //type_text.setStyle(estilo);


            estilo = "-fx-stroke: none; -fx-fill: limegreen; -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;  ";
            for (Text a : regList){ //altera a cor dos registradores
                a.setStyle(estilo);
            }
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
            //type_text.setStyle(estilo);

            estilo = "-fx-stroke: none; -fx-fill: rgb(255, 215, 0); -fx-font-family: 'Inria Sans'; -fx-font-weight: lighter;";
            for (Text a : regList){
                a.setStyle(estilo);
            }

        }
    }

    public static void showGrayFrame(){
        if(Main.gray_enable){
            Main.backgroundView.setImage(Main.backgroundImage);
            Main.baseEstadosView.setImage(Main.baseEstadosImage);
            BarraLateral.imageViewBarLeft.setImage(BarraLateral.imageLabel);
            BarraLateral.imageViewBarRight.setImage(BarraLateral.imageLabel);
            Main.backplateImageView.setImage(Main.backplateImage);
            Main.gray_enable = false;
        } else{
            Main.backgroundView.setImage(Main.backgroundImage2);
            Main.baseEstadosView.setImage(Main.baseEstadosImage2);
            BarraLateral.imageViewBarLeft.setImage(BarraLateral.imageLabel2);
            BarraLateral.imageViewBarRight.setImage(BarraLateral.imageLabel2);
            Main.backplateImageView.setImage(Main.backplateImage2);
            Main.gray_enable = true;
        }
    }


    public void init_binary_fields(int layoutX, int layoutY, double diametro_linha, Color cor_linha, Color cor_texto){

        opcode = new Text();
        opcode.setLayoutX(layoutX+10);
        opcode.setLayoutY(layoutY+20);
        opcode.setFont(new Font(10));
        opcode.setStroke(null);

        rs = new Text();
        rs.setLayoutX(layoutX+56);
        rs.setLayoutY(layoutY+20);
        rs.setFont(new Font(10));
        rs.setStroke(null);

        rt = new Text();
        rt.setLayoutX(layoutX+90);
        rt.setLayoutY(layoutY+20);
        rt.setFont(new Font(10));
        rt.setStroke(null);

        rd = new Text();
        rd.setLayoutX(layoutX+126);
        rd.setLayoutY(layoutY+20);
        rd.setFont(new Font(10));
        rd.setStroke(null);

        shamt = new Text();
        shamt.setLayoutX(layoutX+163);
        shamt.setLayoutY(layoutY+20);
        shamt.setFont(new Font(10));
        shamt.setStroke(null);

        funct = new Text();
        funct.setLayoutX(layoutX+201);
        funct.setLayoutY(layoutY+20);
        funct.setFont(new Font(10));
        funct.setStroke(null);

        imm = new Text();
        imm.setLayoutX(layoutX+130);
        imm.setLayoutY(layoutY+20);
        imm.setFont(new Font(10));
        imm.setStroke(null);

        Addr = new Text();
        Addr.setLayoutX(layoutX+69);
        Addr.setLayoutY(layoutY+20);
        Addr.setFont(new Font(10));
        Addr.setStroke(null);

        //____________________________________________________________________________________

        text_opcode = new Text("OPCODE");
        text_opcode.setLayoutX(layoutX+5);
        text_opcode.setLayoutY(layoutY);
        text_opcode.setFont(new Font(11));
        text_opcode.setStroke(null);
        text_opcode.getStyleClass().add("text-normal-2");

        text_rs = new Text("  RS  ");
        text_rs.setLayoutX(layoutX+57);
        text_rs.setLayoutY(layoutY);
        text_rs.setFont(new Font(11));
        text_rs.setStroke(null);
        text_rs.getStyleClass().add("text-normal-2");

        text_rt = new Text("  RT  ");
        text_rt.setLayoutX(layoutX+92);
        text_rt.setLayoutY(layoutY);
        text_rt.setFont(new Font(11));
        text_rt.setStroke(null);
        text_rt.getStyleClass().add("text-normal-2");

        text_rd = new Text("  RD  ");
        text_rd.setLayoutX(layoutX+126);
        text_rd.setLayoutY(layoutY);
        text_rd.setFont(new Font(11));
        text_rd.setStroke(null);
        text_rd.getStyleClass().add("text-normal-2");

        text_shamt = new Text("SHAMT");
        text_shamt.setLayoutX(layoutX+159);
        text_shamt.setLayoutY(layoutY);
        text_shamt.setFont(new Font(11));
        text_shamt.setStroke(null);
        text_shamt.getStyleClass().add("text-normal-2");

        text_funct = new Text("FUNCT");
        text_funct.setLayoutX(layoutX+200);
        text_funct.setLayoutY(layoutY);
        text_funct.setFont(new Font(11));
        text_funct.setStroke(null);
        text_funct.getStyleClass().add("text-normal-2");

        text_imm = new Text("IMM");
        text_imm.setLayoutX(layoutX+159);
        text_imm.setLayoutY(layoutY);
        text_imm.setFont(new Font(11));
        text_imm.setStroke(null);
        text_imm.getStyleClass().add("text-normal-2");

        text_Addr = new Text("  ADDR  ");
        text_Addr.setLayoutX(layoutX+126);
        text_Addr.setLayoutY(layoutY);
        text_Addr.setFont(new Font(11));
        text_Addr.setStroke(null);
        text_Addr.getStyleClass().add("text-normal-2");
        
        //____________________________________________________________________________________

        line1 = new Line(layoutX+52, layoutY-10, layoutX+52, layoutY+21);
        line2 = new Line(layoutX+86, layoutY-10, layoutX+86, layoutY+21);
        line3 = new Line(layoutX+121, layoutY-10, layoutX+121, layoutY+21);
        line4 = new Line(layoutX+156, layoutY-10, layoutX+156, layoutY+21);
        line5 = new Line(layoutX+197, layoutY-10, layoutX+197, layoutY+21);
        line6 = new Line(layoutX+5, layoutY-12, layoutX+231, layoutY-12);

        line1.setStrokeWidth(diametro_linha);
        line2.setStrokeWidth(diametro_linha);
        line3.setStrokeWidth(diametro_linha);
        line4.setStrokeWidth(diametro_linha);
        line5.setStrokeWidth(diametro_linha);
        line6.setStrokeWidth(diametro_linha);

        line1.setStroke(cor_linha);
        line2.setStroke(cor_linha);
        line3.setStroke(cor_linha);
        line4.setStroke(cor_linha);
        line5.setStroke(cor_linha);
        line6.setStroke(cor_linha);

        rootLayout.getChildren().addAll(opcode,rs,rt,rd,shamt,funct,line1,line2,line3,line4,line5,text_opcode,text_rs,text_rt,text_rd,text_shamt,text_funct,line6,imm,text_imm, Addr, text_Addr);

        opcode.setVisible(false);
        rs.setVisible(false);
        rt.setVisible(false);
        rd.setVisible(false);
        shamt.setVisible(false);
        funct.setVisible(false);
        imm.setVisible(false);
        Addr.setVisible(false);

        line1.setVisible(false);
        line2.setVisible(false);
        line3.setVisible(false);
        line4.setVisible(false);
        line5.setVisible(false);
        line6.setVisible(false);

        text_opcode.setVisible(false);
        text_rs.setVisible(false);
        text_rt.setVisible(false);
        text_rd.setVisible(false);
        text_shamt.setVisible(false);
        text_funct.setVisible(false);
        text_imm.setVisible(false);
        text_Addr.setVisible(false);

    }


    public static void binary_deactivate(){ //usado para desativar todas as informações graficas relativas aos campos da instrução
        opcode.setVisible(false);
        rs.setVisible(false);
        rt.setVisible(false);
        rd.setVisible(false);
        shamt.setVisible(false);
        funct.setVisible(false);
        imm.setVisible(false);
        Addr.setVisible(false);

        line1.setVisible(false);
        line2.setVisible(false);
        line3.setVisible(false);
        line4.setVisible(false);
        line5.setVisible(false);
        line6.setVisible(false);

        text_opcode.setVisible(false);
        text_rs.setVisible(false);
        text_rt.setVisible(false);
        text_rd.setVisible(false);
        text_shamt.setVisible(false);
        text_funct.setVisible(false);
        text_imm.setVisible(false);
        text_Addr.setVisible(false);
    }

    public static void typeR_binary_activate(){ // ativa campos da instrução tipo R
        //desativa todos
        binary_deactivate(); 
        //atualiza com as informações binarias
        String[] typeR_binary_infos = null;
        if (pos < Memory.binary_infos_list.size()) {
            typeR_binary_infos = Memory.binary_infos_list.get(pos);
            opcode.setText(typeR_binary_infos[0]);
            rs.setText(typeR_binary_infos[1]);
            rt.setText(typeR_binary_infos[2]);
            rd.setText(typeR_binary_infos[3]);
            shamt.setText(typeR_binary_infos[4]);
            funct.setText(typeR_binary_infos[5]);
        } 
        //ativa graficamente
        opcode.setVisible(true);
        rs.setVisible(true);
        rt.setVisible(true);
        rd.setVisible(true);
        shamt.setVisible(true);
        funct.setVisible(true);

        line1.setVisible(true);
        line2.setVisible(true);
        line3.setVisible(true);
        line4.setVisible(true);
        line5.setVisible(true);
        line6.setVisible(true);

        text_opcode.setVisible(true);
        text_rs.setVisible(true);
        text_rt.setVisible(true);
        text_rd.setVisible(true);
        text_shamt.setVisible(true);
        text_funct.setVisible(true);
    }

    public static void typeI_binary_activate(){ // ativa campos da instrução tipo I
        //desativa todos
        binary_deactivate(); 
        //atualiza com as informações binarias
        String[] typeI_binary_infos = null;
        if (pos < Memory.binary_infos_list.size()) {
            typeI_binary_infos = Memory.binary_infos_list.get(pos);
            opcode.setText(typeI_binary_infos[0]);
            rs.setText(typeI_binary_infos[1]);
            rt.setText(typeI_binary_infos[2]);
            imm.setText(typeI_binary_infos[3]);
        } 
        //ativa graficamente
        opcode.setVisible(true);
        rs.setVisible(true);
        rt.setVisible(true);
        imm.setVisible(true);

        line1.setVisible(true);
        line2.setVisible(true);
        line3.setVisible(true);
        line6.setVisible(true);

        text_opcode.setVisible(true);
        text_rs.setVisible(true);
        text_rt.setVisible(true);
        text_imm.setVisible(true);
    }

    public static void typeJ_binary_activate(){ // ativa campos da instrução tipo J
        //desativa todos
        binary_deactivate(); 
        //atualiza com as informações binarias
        String[] typeJ_binary_infos = null;
        if (pos < Memory.binary_infos_list.size()) {
            typeJ_binary_infos = Memory.binary_infos_list.get(pos);
            opcode.setText(typeJ_binary_infos[0]);
            Addr.setText(typeJ_binary_infos[1]);
        } 
        //ativa graficamente
        opcode.setVisible(true);
        Addr.setVisible(true);

        line1.setVisible(true);
        line6.setVisible(true);

        text_opcode.setVisible(true);
        text_Addr.setVisible(true);
    }

}




    
