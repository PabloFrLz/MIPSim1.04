// javaFX.jar  (22.0.1) todos os arquivos dentro do diretorio LIB
// jmetro-11.6.16.jar
// controlsfx-11.2.1.jar

package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import MIPS.*;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Main extends Application {
    String version = "1.04";
    Mips mips;
    //coordenadas da imagem de background
    public static final int back_x = 1900;
    public static final int back_y = 1002;
    //coordenadas da imagem dos modulos do MIPS - Os terminais mudam de posição junto com os modulos do MIPS
    public static final int mod_x = 290;
    public static final int mod_y = 20;
    //coordenadas da imagem da barra de estados inferior
    public static final int bar_x = 570;
    public static final int bar_y = 740; 
    //coordenadas da imagem de background dos botoes
    public static final int BACKPLATE_BTN_X = 693;
    public static final int BACKPLATE_BTN_Y = 862; 
    //_____________________________________________
    public DataPath dataPath = null;
    public InfoPath infoPath = null;
    public BarraLateral barraLateral = null;
    public static ArrayList<StateActive> stateActive;
    public static ArrayList<StateActive> clockActivate;
    public static Button clockButton;
    public static Button stopButton;

    public JMetro jMetro;
    private static Color cor_energia = Color.ORANGERED; //cor do terminal quando é energizado. 
    public static Color color_states_fsm = Color.GREEN; // cor dos estados S0 ao S11 da FSM na barra de estado abaixo dos modulos.
    private static boolean gradiente = false; //habilita um gradiente no terminal quando é energizado
    public static boolean gradientetoStates = false; //habilita um gradiente no terminal quando é energizado
    private static double speed_terminal = 3.0; //velocidade da energia nos terminais
    public static int animacao = 1; // controla a animação textual
    public static int end_limit = 128; //quantidade de bytes que serão imprimidos na tela da interface nos segmentos Text, Dynamic, e Global. a quantidade de palavras é dado por:   end_limit / 4 
    public static int font_text = InfoPath.TAM_FONTE; //Tamanho da fonte dos textos
    public static int counter_current_instruction=0;

    public static boolean getGradiente(){ return gradiente;}
    public static Color getCorEnergia(){ return cor_energia;}
    public static double getSpeedTerminal() { return speed_terminal;}

    @Override
    public void start(Stage primaryStage) throws IOException{

        jMetro = new JMetro(Style.DARK);// JMetro theme
        this.mips = new Mips(); // instancia o objeto Mips que terá a lógica interna do microprocessador
        Pane rootLayout = new Pane(); //objeto do tipo Pane para trabalhar com objetos na GUI

        // _____________________________ (Configuração de imagens de background) _______________________________

        //adiciona background
        Image backgroundImage = new Image("file:recursos/background-6.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(back_x); // largura img
        backgroundView.setFitHeight(back_y); // altura img
        backgroundView.setPreserveRatio(true); 
        backgroundView.setSmooth(true); 

        //adiciona os modulos do mips
        Image modulosMIPSImage1 = new Image("file:recursos/modulos-mips.png"); //diagram view
        //Image modulosMIPSImage2 = new Image("file:recursos/modulos-mips-theme2.png"); // circuit view foi descontinuada.
        ImageView modulosMIPSView = new ImageView(modulosMIPSImage1); // por padrão é Diagram view
        modulosMIPSView.setFitWidth(1347); // largura img
        modulosMIPSView.setFitHeight(678); // altura img
        modulosMIPSView.setLayoutX(mod_x); // coordenada X no plano
        modulosMIPSView.setLayoutY(mod_y); // coordenada Y no plano
        modulosMIPSView.setPreserveRatio(true); 
        modulosMIPSView.setSmooth(true); 
        //Effects.efeitoShadow(modulosMIPSView);

        //adiciona bases de estado
        Image baseEstadosImage2 = new Image("file:recursos/base-estados-3.png");
        ImageView baseEstadosView2 = new ImageView(baseEstadosImage2);
        baseEstadosView2.setFitWidth(800); // largura img
        baseEstadosView2.setFitHeight(80); // altura img
        baseEstadosView2.setLayoutX(bar_x); // coordenada X no plano
        baseEstadosView2.setLayoutY(bar_y); // coordenada Y no plano
        baseEstadosView2.setPreserveRatio(true); 
        baseEstadosView2.setSmooth(true); 
        //_______________________________________________________________________________________________________







        // _________________________________________ (adiciona uma barra de menus superior) _____________________
        MenuBar menuBar = new MenuBar();// cria a barra de menu
        Menu fileMenu = new Menu("File");// cria os menus
        Menu settingsMenu = new Menu("Settings");
        Menu textAnimationMenu = new Menu("Text animation");
        Menu themesMenu = new Menu("Themes");
        Menu appearanceMenu = new Menu("Appearance");
        Menu memoryMenu = new Menu("Memory");
        Menu aboutMenu = new Menu("About");


        // ______________________________ (Cria as opções de "File") _______________________________
        MenuItem fileItem1 = new MenuItem("Open");
        MenuItem fileItem2 = new MenuItem("Exit");
        fileItem1.setOnAction(e ->{
            FileChooser fileChooser = new FileChooser();// Cria um FileChooser
            fileChooser.setTitle("Open Assembly File");
            
            fileChooser.getExtensionFilters().add(// Define um filtro para mostrar apenas arquivos de assembly
                new FileChooser.ExtensionFilter("Assembly Files", "*.asm")
            );
            
            File selectedFile = fileChooser.showOpenDialog(null);// Abre a caixa de diálogo para o usuário selecionar um arquivo
            
            if (selectedFile != null) {// Verifica se um arquivo foi selecionado  
                String nomeArquivo = selectedFile.getName();// Obtém o nome do arquivo selecionado
                mips.memoria.instruction_memory.carrega_instrucoes_na_memoria(nomeArquivo);//carrega o codigo em assembly na memoria de instruções.
                showAlert(AlertType.INFORMATION, "Information Dialog", "Open", "Code loaded successfully!");
            }
        });

        fileItem2.setOnAction(e ->{
            System.exit(0); //codigo 0
        });


        // ______________________________ (Cria as opções de "Themes") _____________________________
        Menu terminalColor = new Menu("Terminal color");
        Menu stateColor = new Menu("State color");
        MenuItem themeItem1 = new MenuItem("Orangered terminal");
        MenuItem themeItem2 = new MenuItem("Blue terminal");
        MenuItem themeItem3 = new MenuItem("Green terminal");
        MenuItem themeItem4 = new MenuItem("Red terminal");
        MenuItem themeItem5 = new MenuItem("Yellow terminal");
        MenuItem themeItem6 = new MenuItem("Gradient on terminal");

        MenuItem themeItem7 = new MenuItem("Orangered state");
        MenuItem themeItem8 = new MenuItem("Blue state");
        MenuItem themeItem9 = new MenuItem("Green state");
        MenuItem themeItem10 = new MenuItem("Red state");
        MenuItem themeItem11 = new MenuItem("Yellow state");

        // adicionando ação nas opções de "Themes"
        themeItem1.setOnAction(e -> { gradiente = false; cor_energia = Color.ORANGERED;});
        themeItem2.setOnAction(e -> { gradiente = false; cor_energia = Color.BLUE;});
        themeItem3.setOnAction(e -> { gradiente = false; cor_energia = Color.GREEN;});
        themeItem4.setOnAction(e -> { gradiente = false; cor_energia = Color.RED;});
        themeItem5.setOnAction(e -> { gradiente = false; cor_energia = Color.YELLOW;});
        themeItem6.setOnAction(e ->{ 
            if(gradiente == true){
                gradiente = false;
            } else{
                gradiente = true;
            }}); 

        themeItem7.setOnAction(e -> { color_states_fsm = Color.ORANGERED;});
        themeItem8.setOnAction(e -> { color_states_fsm = Color.BLUE;});
        themeItem9.setOnAction(e -> { color_states_fsm = Color.GREEN;});
        themeItem10.setOnAction(e -> { color_states_fsm = Color.RED;});
        themeItem11.setOnAction(e -> { color_states_fsm = Color.YELLOW;});

        // _______________________________ (Cria as opções de Settings) _________________________________
        
        // ------------------------------- ( Cria um Slider - Simulation speed) ------------------------
        Slider speedSlider = new Slider(0.1, 6.0, 3.0); // velocidades possiveis no intervalo de 0.1 a 6.0, valor inicial 2.0.
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(0.5);
        speedSlider.setMinorTickCount(4);
        speedSlider.setBlockIncrement(0.1);
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            speed_terminal = newValue.doubleValue(); //atualiza o valor da variavel que controla a velocidade da energia.
        });
        Label sliderLabel = new Label("Simulation speed:");
        VBox vBox = new VBox(sliderLabel, speedSlider); // Cria um VBox para agrupar o Label com o Text e o Slider
        CustomMenuItem speedMenuItem = new CustomMenuItem(vBox); // Cria um CustomMenuItem que contém o Slider Simulation speed e a label com a descrição "Simulation speed".
        speedMenuItem.setHideOnClick(false);
        

        // ------------------------------- ( Cria um Slider - Font size) ------------------------
        Slider fontSlider = new Slider(8, 16, 10);
        fontSlider.setShowTickLabels(true);
        fontSlider.setShowTickMarks(true);
        fontSlider.setMajorTickUnit(1);
        fontSlider.setMinorTickCount(0);
        fontSlider.setBlockIncrement(1);
        fontSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            font_text = newValue.intValue(); //atualiza o valor da variavel que controla o tamanho dos textos.
            InfoPath.alternateFont();
        });
        Label sliderLabel2 = new Label("Font size:");
        VBox vBox2 = new VBox(sliderLabel2, fontSlider); // Cria um VBox para agrupar o Label com o Text e o Slider
        CustomMenuItem fontMenuItem = new CustomMenuItem(vBox2); // Cria um CustomMenuItem que contém o Slider Font size e a label com a descrição "Font size".
        fontMenuItem.setHideOnClick(false);

        // ------------------------------- (Cria um TextinputDialog pra lê do usuario a quantidade de bytes a ser impressos pela memoria - Memory) --------------------------
        Label memoryItemLabel = new Label("Set number of bytes printed");
        CustomMenuItem memoryCustomMenuItem = new CustomMenuItem(memoryItemLabel);
        memoryCustomMenuItem.setHideOnClick(false);

        memoryItemLabel.setOnMouseClicked(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Number of bytes");
            dialog.setHeaderText("Set Number of Bytes Printed");
            dialog.setContentText("insert a number...");
            jMetro.setScene(dialog.getDialogPane().getScene()); //estiliza com jmetro
            
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    end_limit = Integer.parseInt(result.get());
                    System.out.println("Número de bytes a serem impressos definido para: " + end_limit);
                } catch (NumberFormatException err) {
                    showAlert(AlertType.ERROR, "ERROR", "NumberFormatException", "Insert a valid number!");
                }
            }
        });
        

        // __________________________________ (Cria as opções de Animation) ____________________________________
        
        MenuItem animationItem1 = new MenuItem("Fade text animation");
        MenuItem animationItem2 = new MenuItem("Scale text animation");
        MenuItem animationItem3 = new MenuItem("Rotation text animation");
        animationItem1.setOnAction(e -> animacao = 0);
        animationItem2.setOnAction(e -> animacao = 1);
        animationItem3.setOnAction(e -> animacao = 2);


        // _______________________________ (Cria as opções de Appearance) _________________________________

        MenuItem appearanceItem1 = new MenuItem("Neon view");
        MenuItem appearanceItem2 = new MenuItem("Golden view");
        appearanceItem1.setOnAction(e -> {
            BarraLateral.showNeonFrame(true); //habilita neon view
            BarraLateral.showGoldenFrame(false); //desabilita golden view
        });
        appearanceItem2.setOnAction(e -> {
            BarraLateral.showNeonFrame(false);
            BarraLateral.showGoldenFrame(true);
        });


        // ________________________________ (Cria uma janela com informações sobre o software - About) ____________________

        
        MenuItem aboutMenuItem = new MenuItem("Info");
        aboutMenuItem.setOnAction(event -> {
            Alert aboutAlert = new Alert(AlertType.INFORMATION);
            aboutAlert.setTitle("Info");
            aboutAlert.setHeaderText("Software developed to aid teaching in the discipline of\n"+
                                    "Computer Architecture and related areas. \n"+
                                    "© 2024 UFMT. All rights reserved.");
            aboutAlert.setContentText("Software Version: "+version+"\n" +
            "FAQ: https://computacao.cua.ufmt.br\n" +
            "Contact: pablo-tr1@hotmail.com");

            ImageView icon = new ImageView(new Image("file:recursos/logo0.png"));
            icon.setFitHeight(80);
            icon.setFitWidth(80);
            aboutAlert.setGraphic(icon);

            Stage stage = (Stage) aboutAlert.getDialogPane().getScene().getWindow();
            stage.setOpacity(0.95);// Define a opacidade desejada (por exemplo, 0.90 para 90% de opacidade)
            // Aplica o estilo JMetro ao Alert
            //JMetro jMetro = new JMetro(Style.LIGHT);
            //jMetro.setScene(aboutAlert.getDialogPane().getScene());
            aboutAlert.showAndWait();
        });
        
        aboutMenu.getItems().add(aboutMenuItem);
        
        // ================================ (Adicionar os itens de menu aos menus correspondentes) ========================
        fileMenu.getItems().addAll(fileItem1, fileItem2);
        memoryMenu.getItems().addAll(memoryCustomMenuItem);
        terminalColor.getItems().addAll(themeItem1, themeItem2, themeItem3, themeItem4, themeItem5, themeItem6);
        stateColor.getItems().addAll(themeItem7, themeItem8, themeItem9, themeItem10, themeItem11);
        themesMenu.getItems().addAll(terminalColor, stateColor);
        textAnimationMenu.getItems().addAll(animationItem1, animationItem2, animationItem3);
        appearanceMenu.getItems().addAll(appearanceItem1, appearanceItem2);
        settingsMenu.getItems().addAll(speedMenuItem, fontMenuItem, memoryMenu, themesMenu, textAnimationMenu, appearanceMenu);
        //appearanceMenu.getItems().addAll(appearanceItem1);

        // Adicionar os menus à barra de menu
        menuBar.getMenus().addAll(fileMenu, settingsMenu, aboutMenu);
        
        rootLayout.getChildren().addAll(backgroundView); // [NOTA]: insere parcialmente os objetos na GUI - demais inserções estarão dentro do metodos ou na seção de inserção no fim do codigo.
        

        //____________________________________________________________________________________________________________________







        // _____________________________ (Configuração da Barra de Estados e clock) _______________________________
        //objetos usados para ativar os estados na barra de estados da FSM na interface grafica
        //[NOTA]: a posição dos circulos foi modificada pra permitir que mova o objeto da barra de estados junto aos circulos verdes, facilitando a manutenção posterior
        stateActive = new ArrayList<StateActive>();
        StateActive S0 = new StateActive(rootLayout, bar_x+31, bar_y+33, 22);
        StateActive S1 = new StateActive(rootLayout, bar_x+88, bar_y+33, 22);
        StateActive S2 = new StateActive(rootLayout, bar_x+145, bar_y+33, 22);
        StateActive S3 = new StateActive(rootLayout, bar_x+201, bar_y+33, 22);
        StateActive S4 = new StateActive(rootLayout, bar_x+258, bar_y+33, 22);
        StateActive S5 = new StateActive(rootLayout, bar_x+315, bar_y+33, 22);
        StateActive S6 = new StateActive(rootLayout, bar_x+373, bar_y+33, 22);
        StateActive S7 = new StateActive(rootLayout, bar_x+430, bar_y+33, 22);
        StateActive S8 = new StateActive(rootLayout, bar_x+487, bar_y+33, 22);
        StateActive S9 = new StateActive(rootLayout, bar_x+543, bar_y+33, 22);
        StateActive S10 = new StateActive(rootLayout, bar_x+600, bar_y+33, 22);
        StateActive S11 = new StateActive(rootLayout, bar_x+655, bar_y+33, 22);
        StateActive S12 = new StateActive(rootLayout, bar_x+712, bar_y+33, 22);
        StateActive S13 = new StateActive(rootLayout, bar_x+770, bar_y+33, 22);

        stateActive.add(S0);
        stateActive.add(S1);
        stateActive.add(S2);
        stateActive.add(S3);
        stateActive.add(S4);
        stateActive.add(S5);
        stateActive.add(S6);
        stateActive.add(S7);
        stateActive.add(S8);
        stateActive.add(S9);
        stateActive.add(S10);
        stateActive.add(S11);
        stateActive.add(S12);
        stateActive.add(S13);

        //seção para gerar fundo verde para os clocks dos modulos MIPS
        clockActivate = new ArrayList<StateActive>();
        StateActive pc_clock = new StateActive(rootLayout,                     mod_x+79, mod_y+344, 12);
        StateActive IDmemory_clock = new StateActive(rootLayout,               mod_x+277, mod_y+368, 12);
        StateActive NAREGInstr_clock = new StateActive(rootLayout,             mod_x+449, mod_y+365, 12);
        StateActive NAREGData_clock = new StateActive(rootLayout,              mod_x+457, mod_y+487, 12);
        StateActive RegisterFile_clock = new StateActive(rootLayout,           mod_x+667, mod_y+363, 14);
        StateActive UC_clock = new StateActive(rootLayout,                     mod_x+558, mod_y+58, 13);
        StateActive NAREGAB_clock = new StateActive(rootLayout,                mod_x+820, mod_y+362, 12);
        StateActive NAREGALUOut_clock = new StateActive(rootLayout,            mod_x+1160, mod_y+378, 11);
        
        clockActivate.add(pc_clock);
        clockActivate.add(IDmemory_clock);
        clockActivate.add(NAREGInstr_clock);
        clockActivate.add(NAREGData_clock);
        clockActivate.add(RegisterFile_clock);
        clockActivate.add(UC_clock);
        clockActivate.add(NAREGAB_clock);
        clockActivate.add(NAREGALUOut_clock);
        //____________________________________________________________________________________________________________







        // _____________________________ (Configuração dos terminais) _______________________________
        
        dataPath = new DataPath(rootLayout, infoPath, stateActive);
        rootLayout.getChildren().addAll(modulosMIPSView, baseEstadosView2); // [NOTA]: insere parcialmente os objetos na GUI - demais inserções estarão dentro do metodos ou na seção de inserção no fim do codigo.

        // _____________________________ (Configuração dos valores dentro dos módulos MIPS) _________________________________________
        
        infoPath = new InfoPath(rootLayout, dataPath, mips);
        dataPath.setInfoPath(infoPath); //codigo adicional pra corrigir o problema de dependencia circular entre dataPath e infoPath
        infoPath.setDataPath(dataPath);

         // _____________________________ (Criando a barra lateral do simulador) ______________________________
        // na barra latera irá ficar a caixa de texto pra inserir código em assembly mips e terá os 3 segmentos de memoria, GLOBAL DATA, DYNAMIC DATA, TEXT.

        barraLateral = new BarraLateral(rootLayout);
        mips.registerFile.setBarraLateral(barraLateral); // para modificar o valor dos registradores na barra lateral direita
        mips.memoria.setBarraLateral(barraLateral); // para modificar o valor nas memorias na barra lateral esquerda e valor atual da instrução na barra direita
        mips.uc.setBarraLateral(barraLateral); // para modificar tipo da instrução e demais informações futuras.
        //_____________________________________________________________________________________________________




        
        // _____________________________ (Configuração dos Botões) _______________________________

        //antes de configurar o botão de execução, inicializar os modulos que escutarão o pulso de clock:
        mips.clock.addListener(mips.A_B);
        mips.clock.addListener(mips.Data);
        mips.clock.addListener(mips.ALUOut);
        mips.clock.addListener(mips.uc);
        mips.uc.setInfoPath(infoPath); //configurando o infoPath na UC

        // _________________________________ (Botão Load) ______________________________________________
        Button loadButton = new Button("Load"); //botão para carregar codigo assembly inserido pelo usuario no campo de textos da interface do simulador.
        loadButton.setLayoutX(11);
        loadButton.setLayoutY(230);
        loadButton.setPrefHeight(25);
        loadButton.setPrefWidth(248);
        loadButton.getStyleClass().add("button-2");
        loadButton.setOnAction(event -> {
            barraLateral.saveTextToFile(barraLateral.getTEXTArea().getText()); //gera o arquivo "code.asm" que contém o codigo em assembly inserido pelo usuario na area de texto do simulador.
            mips.memoria.instruction_memory.carrega_instrucoes_na_memoria("code.asm");
            showAlert(AlertType.INFORMATION, "Information Dialog", "Load", "Code loaded successfully!");
        });



        // _________________________________ (Imagem de background dos botões ) ______________________________________________
        ImageView backplateImageView = new ImageView("file:recursos/backplate_button.png");
        backplateImageView.setFitWidth(550); 
        backplateImageView.setFitHeight(150);

        Label backplate_button = new Label();
        backplate_button.setGraphic(backplateImageView); // o innershadow do css do javafx é muito ruim, então tive que pegar uma imagem com innershadow aplicado do figma.com 
        backplate_button.setLayoutX(BACKPLATE_BTN_X);
        backplate_button.setLayoutY(BACKPLATE_BTN_Y);
        



        //_____________________________________________ (Botão step) __________________________________________________________
        // Botão para avançar pelos estados da FSM e datapath das instruções
        clockButton = new Button();
        clockButton.setLayoutX(BACKPLATE_BTN_X+197);
        clockButton.setLayoutY(BACKPLATE_BTN_Y+58);
        ImageView btn_img_clock = new ImageView(new Image("file:recursos/button-clock.png"));
        Effects.efeitoLighting("Point",btn_img_clock); //efeito 
        clockButton.setGraphic(btn_img_clock); //insere imagem no button
        clockButton.getStyleClass().add("button"); //necessário para adicionar a transparencia em volta do button, igual na imagem original.
        clockButton.setBackground(null);
        Tooltip tooltipStep = new Tooltip("Clock Button");
        clockButton.setTooltip(tooltipStep);
        clockButton.setOnAction(e -> {

            clockButton.setDisable(true); // Desabilita o botão step e stop inicialmente para que o usuario nao pressione enquanto o estado esta em execução.
            stopButton.setDisable(true); 
            infoPath.notShowWaitClock(); //interrompe a mensagem "waiting clock..."

            if(mips.memoria.NOPFlag){ //instrução NOP - equivalente a pressionar o botão 'stopButton'
                showAlert(AlertType.WARNING, "Warning Dialog", "NOP instruction (0x00000000)", "The program was be reset."); 
                resetProgram();
                mips.memoria.NOPFlag = false;
                System.out.println("\n *** (reset) ***"); 
                infoPath.showWaitClock(); // mostra a mensagem "waiting clock..." novamente. 
                clockButton.setDisable(false); //habilita os botoes
                stopButton.setDisable(false); 
                return;
            } 

            for(StateActive clock : clockActivate){ //dá clock "graficamente" em todos os modulos adicionados com "mips.clock.addListener()".
                clock.rising_edge();
            }  
        
            mips.nextState(mips); //habilita os sinais do proximo estado.
            mips.execute(mips); //***LÓGICA INTERNA DO SIMULADOR - percorre o circuito do MIPS alterando os dados com base nos sinais ativados no estado atual. Digamos que seria o back-end da aplicação

            

            //executa o caminho de dados configurado para a instrução especifica na interface grafica. Digamos que seria o front-end da aplicação
            if(mips.uc.instr != ""){
                if(mips.uc.instr.equals("Tipo-R")){
                    dataPath.tipoR(mips.ciclos_clock); //o caminho para as instruções do tipo-R são iguais. Pelo menos para as instruções do tipo_R iniciais, como add,sub,and,or,slt. Para "jr" é diferente.
                }else if(mips.uc.instr.equals("lw")){
                    dataPath.lw(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("sw")){
                    dataPath.sw(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("addi")){ 
                    dataPath.addi(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("beq")){
                    dataPath.beq(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("j")){
                    dataPath.j(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("jal")){
                    dataPath.jal(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("jr")){ 
                    dataPath.jr(mips.ciclos_clock);
                }else if(mips.uc.instr.equals("lb")){ 
                    dataPath.lw(mips.ciclos_clock); // lb possui mesmo caminho da instrução lw
                }else if(mips.uc.instr.equals("sb")){ 
                    dataPath.sw(mips.ciclos_clock); // sb possui mesmo caminho da instrução sw
                }/*else if(mips.uc.instr.equals("nome da nova instrução")){ 
                    dataPath.sw(mips.ciclos_clock); // inserir o metodo de DataPath.java que executa a animação para esta nova instrução.
                    NOTA: se a nova instrução for do Tipo-R, não é preciso inserir um novo bloco aqui.
                    exceto se a nova instrução do tipo-R necessitar de uma animação especifica, tal como "jr".
                }*/ else{
                    throw new IllegalArgumentException("[Main.java]: clockButton.setOnAction() - ERRO: Caminho de dados indisponível para essa instrução - "+mips.uc.instr);
                }
            }
            
            if(mips.uc.state_UC.equals("S0")){
                dataPath.reset_Terminais_BarraEstados(); //reseta os terminais carregados e os estados ativados.
                mips.ciclos_clock = 0; //reinicializa a variavel que contabiliza os ciclos de clock.
                barraLateral.setInstruction("wait instr..."); 
                barraLateral.setInstructionBIN("00000000000000000000000000000000");
                barraLateral.setTypeInstr("?");
                mips.ula.setFlagZero((short) 0); //reset flagzero
                infoPath.atualizaInfo(52); //reset entradas das portas logicas - as demais entradas já são resetadas no metodo resetaSinais da UC
                infoPath.atualizaInfo(124);
                dataPath.FSMStates(0); // executa o data path do estado S0.
                mips.uc.instr = "";
                Memory.current_instruction.set(counter_current_instruction++); //incrementa o item da listview que representa o segmento TEXT, destacando a instrução atual em execução.
                
            } 
            mips.ciclos_clock++; //incrementa o clock
            
        });
        



        //_____________________________________________ (Botão Stop) __________________________________________________________
        // Botão para desenergizar o terminal1
        stopButton = new Button();
        stopButton.setLayoutX(BACKPLATE_BTN_X+307);
        stopButton.setLayoutY(BACKPLATE_BTN_Y+58);
        ImageView btn_img_stop = new ImageView(new Image("file:recursos/button-stop.png"));
        Effects.efeitoLighting("Point",btn_img_stop);
        stopButton.setGraphic(btn_img_stop); //insere imagem no button
        stopButton.getStyleClass().add("button-3");
        stopButton.setBackground(null);
        Tooltip tooltipStop = new Tooltip("Stop Button");
        stopButton.setTooltip(tooltipStop);
        stopButton.setOnAction(e -> {

            resetProgram(); //reinicializa do inicio o conjunto de instruções
            
        });
        //_____________________________________________________________________________________________________________________







        // ___________________________________________ (Configurações finais) _________________________________________________
        rootLayout.getChildren().addAll(menuBar, backplate_button); // barra de menus e background dos botoes

        //seção para gerar fundo verde de ativação para os botões
        StateActive buttonPlay = new StateActive(rootLayout, BACKPLATE_BTN_X+222, BACKPLATE_BTN_Y+83, 27);
        clockActivate.add(buttonPlay);
    
        // _____________________________ (inserção dos ultimos objetos na GUI) _______________________________
        
        rootLayout.getChildren().addAll(clockButton, stopButton, loadButton); // botoes de start e stop

        //efeitos no rootlayout:
        //Effects.efeitoBloom(rootLayout);
        

        Scene scene = new Scene(rootLayout, back_x, back_y);
        jMetro.setScene(scene);
        scene.getStylesheets().add("file:estilos.css");
        primaryStage.setTitle("MIPSim simulator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

       

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        jMetro.setScene(alert.getDialogPane().getScene());
        alert.showAndWait();
    }


    public void resetProgram() {
        dataPath.reset_Terminais_BarraEstados(); //reseta os terminais habilitados na GUI.
        infoPath.resetText();
        mips.ciclos_clock = 0;

        barraLateral.setInstruction("wait instr...");
        barraLateral.setInstructionBIN("00000000000000000000000000000000");
        barraLateral.setTypeInstr("?");
        mips.uc.instr = "";
        mips.ula.setFlagZero((short) 0); 
    
        mips.uc.resetAllSignals(); //reseta todos os sinais
        mips.pc.setPC(mips.memoria.endereco_inicial_TEXT); //atualiza PC com endereço inicial do segmento TEXT
        mips.registerFile.resetRegisters(); //reseta os registradores da classe RegisterFile e da classe BarraLateral

        mips.uc.fila.clear(); //remove todos os estados da fila de execução.
        mips.uc.fila.add(() -> mips.uc.S0()); //insere o estado S0 na fila.
        mips.uc.fila.add(() -> mips.uc.S1()); //insere o estado S1 na fila.

        barraLateral.resetList("Dynamic"); // reseta os dados presentes no segmento Dynamic e Global Data.
        barraLateral.resetList("Global");

        Memory.current_instruction.set(0);
        counter_current_instruction = 0;
    }
}
