package GUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MIPS.Mips;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class InfoPath {
    private int mod_x = Main.mod_x;
    private int mod_y = Main.mod_y;
    private int animacao;
    private Timeline timeline; // atributo apenas para o Text 'wait_clock'
    public static Font fonte;
    public static final int TAM_FONTE = 10; //tamanho da fonte padrão = 9
    public static final double TIME_ANIMATION = 1.0; //duração da animação dos textos
    static List<Text> TextList;
    public DataPath dataPath;
    public Mips mips;
    private Text RegisterFile_A1, RegisterFile_A2, RegisterFile_A3, RegisterFile_WD3, RegisterFile_RD1, RegisterFile_RD2, SignExtend_input, SignExtend_output, PC_PClinha,
        PC_PC, IDMemory_A, IDMemory_RD, IDMemory_WD, ULA_SrcA, ULA_SrcB, ULA_FlagZero, ULA_ALUResult, MUXIorD_0, MUXIorD_1, MUXRegDst_0, MUXRegDst_1, MUXMemtoReg_0, 
        MUXMemtoReg_1, MUXALUSrcA_0, MUXALUSrcA_1, MUXALUSrcB_0, MUXALUSrcB_1, MUXALUSrcB_2, MUXALUSrcB_3, MUXPCSrc_0, MUXPCSrc_1, MUXPCSrc_2, NAREGInstr_IN,
        NAREGInstr_OUT,NAREGData_IN, NAREGData_OUT, NAREGA_IN, NAREGA_OUT, NAREGB_IN, NAREGB_OUT, NAREGALUOut_IN, NAREGALUOut_OUT, Addr, Addr_x2, PCJump, PC_4bits,
        AND_0, AND_1, OR_0, OR_1, OR_OUT, UC_IorD, UC_MemWrite, UC_IRWrite, UC_Opcode, UC_Funct, UC_PCWrite, UC_Branch, UC_PCSrc, UC_ALUControl, UC_ALUSrcB, UC_ALUSrcA,
        UC_RegWrite, UC_RegDst, UC_MemtoReg, UC_ALUOp, signal_ALUOp, wait_clock, MUXMemtoReg_2, PC_en;
    
    public InfoPath(Pane rootLayout, DataPath dataPath, Mips mips){
        this.mips = mips;
        this.dataPath = dataPath;
        // Posicionando os textos com os valores das entradas e saídas dos módulos
        //NomeModulo_NomeEntrada
        RegisterFile_A1 = new Text(mod_x+648, mod_y+387, "00"); // code = 11 -> Terminal a11
        RegisterFile_A2 = new Text(mod_x+648, mod_y+416, "00"); // code = 12 -> Terminal a12
        RegisterFile_A3 = new Text(mod_x+648, mod_y+456, "00"); // code = 15 -> Terminal a15
        RegisterFile_WD3 = new Text(mod_x+648, mod_y+503, "00000000"); // code = 17
        RegisterFile_RD1 = new Text(mod_x+716, mod_y+387, "00000000"); // setado no RegisterFile_A1
        RegisterFile_RD2 = new Text(mod_x+716, mod_y+416, "00000000"); // setado no RegisterFile_A2
        SignExtend_input = new Text(mod_x+670, mod_y+623, "00000000"); // code = 18
        //SignExtend_output = new Text(mod_x+715, mod_y+623, "0000"); // setado no SignExtend_input
        PC_PClinha = new Text(mod_x+60, mod_y+359, "00000000"); // code = 74 - [NOTA]: daria pra por 5 digitos hexadecimais pois a memoria MIPS de 8MB proposta endereça no segmento Text de 0x00001ffc até 0x0007ff34.
        PC_PC = new Text(mod_x+78, mod_y+377, "00000000");  // code = 1
        IDMemory_A = new Text(mod_x+261, mod_y+396, "00000000"); // code = 2
        IDMemory_RD = new Text(mod_x+314, mod_y+396, "00000000"); // setado em IDMemory_A
        IDMemory_WD = new Text(mod_x+261, mod_y+480, "00000000"); // setado em IDMemory_A
        ULA_SrcA = new Text(mod_x+1021, mod_y+368, "00000000"); // code = 40
        ULA_SrcB = new Text(mod_x+1021, mod_y+445, "00000000"); // code = 43
        ULA_FlagZero = new Text(mod_x+1070, mod_y+386, "0");  // setado em ULA_SrcB por ser o caminho mais demorado
        ULA_ALUResult = new Text(mod_x+1039, mod_y+406, "00000000"); // setado em ULA_SrcB por ser o caminho mais demorado
        MUXIorD_0 = new Text(mod_x+179, mod_y+376, "00000000");  // setado no PC_PC
        MUXIorD_1 = new Text(mod_x+179, mod_y+410, "00000000");  // code = 59
        MUXRegDst_0 = new Text(mod_x+554, mod_y+446, "00");  // code = 13
        MUXRegDst_1 = new Text(mod_x+554, mod_y+468, "00");  // code = 14
        MUXMemtoReg_0 = new Text(mod_x+587, mod_y+490, "00000000"); // code = 56
        MUXMemtoReg_1 = new Text(mod_x+587, mod_y+513, "00000000"); // code = 16
        MUXALUSrcA_0 = new Text(mod_x+896, mod_y+354, "00000000");  // code = 34
        MUXALUSrcA_1 = new Text(mod_x+896, mod_y+388, "00000000");  // code = 27
        MUXALUSrcB_0 = new Text(mod_x+901, mod_y+459, "00000000");  // code = 30
        MUXALUSrcB_1 = new Text(mod_x+902, mod_y+484, "4");   // code = 24
        MUXALUSrcB_2 = new Text(mod_x+901, mod_y+510, "00000000");  // code = 21
        MUXALUSrcB_3 = new Text(mod_x+901, mod_y+536, "00000000");  // code = 23
        MUXPCSrc_0 = new Text(mod_x+1251, mod_y+386, "00000000");   // code = 48
        MUXPCSrc_1 = new Text(mod_x+1251, mod_y+409, "00000000");  // code = 49
        MUXPCSrc_2 = new Text(mod_x+1251, mod_y+432, "00000000");   // code = 65
        NAREGInstr_IN = new Text(mod_x+432, mod_y+378, "00000000"); // code = 3
        NAREGInstr_OUT = new Text(mod_x+447, mod_y+396, "00000000"); // code = 6
        NAREGData_IN = new Text(mod_x+432, mod_y+499, "00000000"); // code = 5
        NAREGData_OUT = new Text(mod_x+447, mod_y+515, "00000000");  // setado no MUXMemtoReg_1
        NAREGA_IN = new Text(mod_x+795, mod_y+372, "00000000"); // code = 25
        NAREGA_OUT = new Text(mod_x+809, mod_y+387, "00000000"); // setado em MUXALUSrcA_1
        NAREGB_IN = new Text(mod_x+795, mod_y+400, "00000000");  //code = 26
        NAREGB_OUT = new Text(mod_x+809, mod_y+415, "00000000");  // code = 28
        NAREGALUOut_IN = new Text(mod_x+1135, mod_y+392, "00000000");  //code = 41
        NAREGALUOut_OUT = new Text(mod_x+1148, mod_y+408, "00000000"); // coe = 42
        Addr = new Text(mod_x+945, mod_y+642, "0000000");     // 26 bits endereça até 3FF FFFF (7 digitos hexadecimais) - code = 60
        Addr_x2 = new Text(mod_x+1166, mod_y+528, "0000000"); // 28 bits endereça até FFF FFFF (7 digitos hexadecimais) - code = 63
        PCJump = new Text(mod_x+1161, mod_y+472, "00000000"); // 32 bits (8 digitos hexadecimais) - code = 64
        PC_4bits = new Text(mod_x+870, mod_y+296, "0");  // code = 66
        AND_0 = new Text(mod_x+1144, mod_y+95, "0");  // code = 110
        AND_1 = new Text(mod_x+1144, mod_y+115, "0");  // code = 52
        OR_0 = new Text(mod_x+1217, mod_y+76, "0");  // code = 109
        OR_1 = new Text(mod_x+1217, mod_y+96, "0");  // code = 124
        OR_OUT = new Text(mod_x+1278, mod_y+87, "0");  // code = 51
        UC_IorD = new Text(mod_x+537, mod_y+85, "0");   // code = 101
        UC_MemWrite = new Text(mod_x+537, mod_y+104, "0"); // code = 103
        UC_IRWrite = new Text(mod_x+537, mod_y+124, "0");  // code = 105
        UC_Opcode = new Text(mod_x+537, mod_y+214, "00");  // code = 9
        UC_Funct = new Text(mod_x+537, mod_y+233, "00");  // code = 8
        UC_PCWrite = new Text(mod_x+623, mod_y+89, "0");  // code = 109
        UC_Branch = new Text(mod_x+623, mod_y+109, "0");  // code = 110
        UC_PCSrc = new Text(mod_x+623, mod_y+129, "0");  // code = 111
        UC_ALUControl = new Text(mod_x+623, mod_y+148, "0");  // code = 115
        UC_ALUSrcB = new Text(mod_x+623, mod_y+168, "0"); // code = 117
        UC_ALUSrcA = new Text(mod_x+623, mod_y+188, "0");  // code = 119
        UC_RegWrite = new Text(mod_x+623, mod_y+208, "0"); // code = 121
        UC_RegDst = new Text(mod_x+563, mod_y+236, "0"); // code = 107
        UC_MemtoReg = new Text(mod_x+603, mod_y+236, "0"); // code = 108
        UC_ALUOp = new Text(mod_x+587, mod_y+71, "0"); // setado em UC_ALUControl
        signal_ALUOp = new Text(mod_x+1010, mod_y+400, "?"); //representa o sinal verde que fica proximo à ULA
        wait_clock = new Text(((Main.back_x / 3)-200), Main.back_y-20, "🕒 Waiting clock...");
        MUXMemtoReg_2 = new Text(mod_x+587, mod_y+536, "00000000"); // code = 16 = new Text(mod_x+587, mod_y+71, "0"); // setado em UC_ALUControl
        PC_en = new Text(mod_x+1200, mod_y+18, "PCen");



        // Configuração de fonte
        fonte = new Font(TAM_FONTE);
        RegisterFile_A1.setFont(fonte); 
        RegisterFile_A2.setFont(fonte); 
        RegisterFile_A3.setFont(fonte);
        RegisterFile_WD3.setFont(fonte);
        RegisterFile_RD1.setFont(fonte);
        RegisterFile_RD2.setFont(fonte);
        SignExtend_input.setFont(fonte);
        //SignExtend_output.setFont(font);
        PC_PClinha.setFont(fonte);
        PC_PC.setFont(fonte);
        IDMemory_A.setFont(fonte);
        IDMemory_RD.setFont(fonte);
        IDMemory_WD.setFont(fonte);
        ULA_SrcA.setFont(fonte);
        ULA_SrcB.setFont(fonte);
        ULA_FlagZero.setFont(fonte);
        ULA_ALUResult.setFont(fonte);
        MUXIorD_0.setFont(fonte);
        MUXIorD_1.setFont(fonte);
        MUXRegDst_0.setFont(fonte);
        MUXRegDst_1.setFont(fonte);
        MUXMemtoReg_0.setFont(fonte);
        MUXMemtoReg_1.setFont(fonte);
        MUXALUSrcA_0.setFont(fonte);
        MUXALUSrcA_1.setFont(fonte);
        MUXALUSrcB_0.setFont(fonte);
        MUXALUSrcB_1.setFont(fonte);
        MUXALUSrcB_2.setFont(fonte);
        MUXALUSrcB_3.setFont(fonte);
        MUXPCSrc_0.setFont(fonte);
        MUXPCSrc_1.setFont(fonte);
        MUXPCSrc_2.setFont(fonte);
        NAREGInstr_IN.setFont(fonte);
        NAREGInstr_OUT.setFont(fonte);
        NAREGData_IN.setFont(fonte);
        NAREGData_OUT.setFont(fonte);
        NAREGA_IN.setFont(fonte);
        NAREGA_OUT.setFont(fonte);
        NAREGB_IN.setFont(fonte);
        NAREGB_OUT.setFont(fonte);
        NAREGALUOut_IN.setFont(fonte);
        NAREGALUOut_OUT.setFont(fonte);
        Addr.setFont(fonte);
        Addr_x2.setFont(fonte);
        PCJump.setFont(fonte);
        PC_4bits.setFont(fonte);
        AND_0.setFont(fonte);
        AND_1.setFont(fonte);
        OR_0.setFont(fonte);
        OR_1.setFont(fonte);
        OR_OUT.setFont(fonte);
        UC_IorD.setFont(fonte);
        UC_MemWrite.setFont(fonte);
        UC_IRWrite.setFont(fonte);
        UC_Opcode.setFont(fonte);
        UC_Funct.setFont(fonte);
        UC_PCWrite.setFont(fonte);
        UC_Branch.setFont(fonte);
        UC_PCSrc.setFont(fonte);
        UC_ALUControl.setFont(fonte);
        UC_ALUSrcB.setFont(fonte);
        UC_ALUSrcA.setFont(fonte); 
        UC_RegWrite.setFont(fonte);
        UC_RegDst.setFont(fonte);
        UC_MemtoReg.setFont(fonte);
        UC_ALUOp.setFont(fonte);
        signal_ALUOp.setFont(new Font(TAM_FONTE+2));
        wait_clock.setFont(new Font(TAM_FONTE+10)); 
        MUXMemtoReg_2.setFont(fonte);
        PC_en.setFont(new Font(TAM_FONTE+3));



        // Configuração de cor
        signal_ALUOp.setStroke(Color.LIMEGREEN);// define cor apenas para o texto que vai representar o sinal da operação atual na ULA
        

        
        // Inserindo os textos na interface.
        rootLayout.getChildren().addAll(
            RegisterFile_A1, RegisterFile_A2, RegisterFile_A3, RegisterFile_WD3, RegisterFile_RD1, RegisterFile_RD2, SignExtend_input,
            PC_PClinha, PC_PC, IDMemory_A, IDMemory_RD, IDMemory_WD, ULA_SrcA, ULA_SrcB, ULA_FlagZero, ULA_ALUResult, MUXIorD_0, MUXIorD_1, MUXRegDst_0, 
            MUXRegDst_1, MUXMemtoReg_0, MUXMemtoReg_1, MUXALUSrcA_0, MUXALUSrcA_1, MUXALUSrcB_0, MUXALUSrcB_1, MUXALUSrcB_2, MUXALUSrcB_3, MUXPCSrc_0,
            MUXPCSrc_1, MUXPCSrc_2, NAREGInstr_IN, NAREGInstr_OUT, NAREGData_IN, NAREGData_OUT, NAREGA_IN, NAREGA_OUT, NAREGB_IN, NAREGB_OUT, NAREGALUOut_IN,
            NAREGALUOut_OUT, Addr, Addr_x2, PCJump, PC_4bits, AND_0,AND_1, OR_0, OR_1, OR_OUT, UC_IorD, UC_MemWrite, UC_IRWrite, UC_Opcode, UC_Funct, UC_PCWrite,
            UC_Branch, UC_PCSrc, UC_ALUControl, UC_ALUSrcB, UC_ALUSrcA, UC_RegWrite, UC_RegDst, UC_MemtoReg, UC_ALUOp, signal_ALUOp, wait_clock, MUXMemtoReg_2, PC_en
            ); 
            


        TextList = Arrays.asList(RegisterFile_A1, RegisterFile_A2, RegisterFile_A3, RegisterFile_WD3, RegisterFile_RD1, RegisterFile_RD2, SignExtend_input,
            PC_PClinha, PC_PC, IDMemory_A, IDMemory_RD, IDMemory_WD, ULA_SrcA, ULA_SrcB, ULA_FlagZero, ULA_ALUResult, MUXIorD_0, MUXIorD_1, MUXRegDst_0, 
            MUXRegDst_1, MUXMemtoReg_0, MUXMemtoReg_1, MUXALUSrcA_0, MUXALUSrcA_1, MUXALUSrcB_0, MUXALUSrcB_1, MUXALUSrcB_2, MUXALUSrcB_3, MUXPCSrc_0,
            MUXPCSrc_1, MUXPCSrc_2, NAREGInstr_IN, NAREGInstr_OUT, NAREGData_IN, NAREGData_OUT, NAREGA_IN, NAREGA_OUT, NAREGB_IN, NAREGB_OUT, NAREGALUOut_IN,
            NAREGALUOut_OUT, Addr, Addr_x2, PCJump, PC_4bits, AND_0,AND_1, OR_0, OR_1, OR_OUT, UC_IorD, UC_MemWrite, UC_IRWrite, UC_Opcode, UC_Funct, UC_PCWrite,
            UC_Branch, UC_PCSrc, UC_ALUControl, UC_ALUSrcB, UC_ALUSrcA, UC_RegWrite, UC_RegDst, UC_MemtoReg, UC_ALUOp, signal_ALUOp, MUXMemtoReg_2
            );


        applyDropShadow(); //será adicionado sombra por padrão pra corrige problemas com trechos da interface muito brancos, onde o texto desaparecia.

        wait_clock.getStyleClass().add("text-normal"); //adicionando formatação do estilos.css apenas no objeto 'wait_clock'
        showWaitClock(); 
    }

    public void setDataPath(DataPath dataPath) { this.dataPath = dataPath;}

    public void atualizaInfo(int codeModule){
        this.animacao = Main.animacao;

        switch (codeModule) {
            // codeModule é vinculado ao ID dos terminais, o Terminal a11 que tem ID=11, faria com que RegisterFile_A1 fosse atualizado.
            case 11:  
                RegisterFile_A1.setText(Integer.toHexString(mips.registerFile.getA1()).toUpperCase());
                animacaoTextual(RegisterFile_A1, animacao);
                RegisterFile_RD1.setText(bits8(Integer.toHexString(mips.registerFile.getRD1())).toUpperCase());
                animacaoTextual(RegisterFile_RD1, animacao);
                break;
            case 12:
                RegisterFile_A2.setText(Integer.toHexString(mips.registerFile.getA2()).toUpperCase());
                animacaoTextual(RegisterFile_A2, animacao);
                RegisterFile_RD2.setText(bits8(Integer.toHexString(mips.registerFile.getRD2())).toUpperCase());
                animacaoTextual(RegisterFile_RD2, animacao);
                break;
            case 15:  
                RegisterFile_A3.setText(Integer.toHexString(mips.registerFile.getA3()).toUpperCase());
                animacaoTextual(RegisterFile_A3, animacao);
                break;
            case 17:  
                RegisterFile_WD3.setText(bits8(Integer.toHexString(mips.registerFile.getWD3())).toUpperCase());
                animacaoTextual(RegisterFile_WD3, animacao);
                break;
            case 18:
                SignExtend_input.setText(bits8(Integer.toHexString(mips.SignImm)).toUpperCase());
                animacaoTextual(SignExtend_input, animacao);
                /*SignExtend_output.setText(Integer.toString(mips.SignImm));
                animacaoTextual(SignExtend_output, animacao);*/
                break;
            case 74:
                PC_PClinha.setText(bits8(Integer.toHexString(mips.pc.getPC_linha())).toUpperCase());
                animacaoTextual(PC_PClinha, animacao);
                break;
            case 1: 
                PC_PC.setText(bits8(Integer.toHexString(mips.pc.getPC())).toUpperCase());
                animacaoTextual(PC_PC, animacao);
                MUXIorD_0.setText(bits8(Integer.toHexString(mips.MUX_IorD_0)).toUpperCase());
                animacaoTextual(MUXIorD_0, animacao);
                break;
            case 2: 
                IDMemory_A.setText(bits8(Integer.toHexString(mips.memoria.getA())).toUpperCase());
                animacaoTextual(IDMemory_A, animacao);
                IDMemory_RD.setText(bits8(Integer.toHexString(mips.memoria.getRD())).toUpperCase());
                animacaoTextual(IDMemory_RD, animacao);
                /*IDMemory_WD.setText(bits8(Integer.toHexString(mips.memoria.getWD())).toUpperCase());
                animacaoTextual(IDMemory_WD, animacao);*/
                break;
            case 39:
                IDMemory_WD.setText(bits8(Integer.toHexString(mips.memoria.getWD())).toUpperCase());
                animacaoTextual(IDMemory_WD, animacao);
                break;
            case 40: 
                ULA_SrcA.setText(bits8(Integer.toHexString(mips.ula.getSrcA())).toUpperCase());
                animacaoTextual(ULA_SrcA, animacao);
                break; 
            case 43: 
                ULA_SrcB.setText(bits8(Integer.toHexString(mips.ula.getSrcB())).toUpperCase());
                animacaoTextual(ULA_SrcB, animacao);
                break; 
            case 116:
                ULA_ALUResult.setText(bits8(Integer.toHexString(mips.ula.getALU_Result())).toUpperCase());
                animacaoTextual(ULA_ALUResult, animacao);  
                ULA_FlagZero.setText(Integer.toHexString(mips.ula.getFlagZero()).toUpperCase());
                animacaoTextual(ULA_FlagZero, animacao);
                signal_ALUOp.setText(mips.ula.getSignalOperation().toUpperCase());
                animacaoTextual(signal_ALUOp, animacao);
                break;
            case 59: 
                MUXIorD_1.setText(bits8(Integer.toHexString(mips.MUX_IorD_1)).toUpperCase());
                animacaoTextual(MUXIorD_1, animacao);
                break;
            case 13: 
                MUXRegDst_0.setText(Integer.toHexString(mips.MUX_RegDst_0).toUpperCase());
                animacaoTextual(MUXRegDst_0, animacao);
                break;
            case 14: 
                MUXRegDst_1.setText(Integer.toHexString(mips.MUX_RegDst_1).toUpperCase());
                animacaoTextual(MUXRegDst_1, animacao);
                break;
            case 56: 
                MUXMemtoReg_0.setText(bits8(Integer.toHexString(mips.MUX_MemtoReg_0)).toUpperCase());
                animacaoTextual(MUXMemtoReg_0, animacao);
                break;
            case 16: 
                NAREGData_OUT.setText(bits8(Integer.toHexString(mips.Data.getSaidaA())).toUpperCase());
                animacaoTextual(NAREGData_OUT, animacao);
                MUXMemtoReg_1.setText(bits8(Integer.toHexString(mips.MUX_MemtoReg_1)).toUpperCase());
                animacaoTextual(MUXMemtoReg_1, animacao);
                break;
            case 34: 
                MUXALUSrcA_0.setText(bits8(Integer.toHexString(mips.MUX_ALUSrcA_0)).toUpperCase());
                animacaoTextual(MUXALUSrcA_0, animacao);
                break;
            case 27: 
                NAREGA_OUT.setText(bits8(Integer.toHexString(mips.A_B.getSaidaA())).toUpperCase());
                animacaoTextual(NAREGA_OUT, animacao);
                MUXALUSrcA_1.setText(bits8(Integer.toHexString(mips.MUX_ALUSrcA_1)).toUpperCase());
                animacaoTextual(MUXALUSrcA_1, animacao);
                break;
            case 30: 
                MUXALUSrcB_0.setText(bits8(Integer.toHexString(mips.MUX_ALUSrcB_0)).toUpperCase());
                animacaoTextual(MUXALUSrcB_0, animacao);
                break;
            case 24: 
                MUXALUSrcB_1.setText(bits8(Integer.toHexString(mips.MUX_ALUSrcB_1)).toUpperCase());
                animacaoTextual(MUXALUSrcB_1, animacao);
                break;
            case 21: 
                MUXALUSrcB_2.setText(bits8(Integer.toHexString(mips.MUX_ALUSrcB_2)).toUpperCase());
                animacaoTextual(MUXALUSrcB_2, animacao);
                break;
            case 23: 
                MUXALUSrcB_3.setText(bits8(Integer.toHexString(mips.MUX_ALUSrcB_3)).toUpperCase());
                animacaoTextual(MUXALUSrcB_3, animacao);
                break;
            case 48: 
                MUXPCSrc_0.setText(bits8(Integer.toHexString(mips.MUX_PCSrc_0)).toUpperCase());
                animacaoTextual(MUXPCSrc_0, animacao);
                break;
            case 49: 
                NAREGALUOut_OUT.setText(bits8(Integer.toHexString(mips.ALUOut.getSaidaA())).toUpperCase());
                animacaoTextual(NAREGALUOut_OUT, animacao);
                MUXPCSrc_1.setText(bits8(Integer.toHexString(mips.MUX_PCSrc_1)).toUpperCase());
                animacaoTextual(MUXPCSrc_1, animacao);
                break;
            case 65: 
                MUXPCSrc_2.setText(bits8(Integer.toHexString(mips.MUX_PCSrc_2)).toUpperCase());
                animacaoTextual(MUXPCSrc_2, animacao);
                break;
            case 3: 
                NAREGInstr_IN.setText(bits8(Integer.toHexString(mips.Instr.getPortaA())).toUpperCase());
                animacaoTextual(NAREGInstr_IN, animacao);
                break;
            case 6: 
                NAREGInstr_OUT.setText(bits8(Integer.toHexString(mips.Instr.getSaidaA())).toUpperCase());
                animacaoTextual(NAREGInstr_OUT, animacao);
                break;
            case 5: 
                NAREGData_IN.setText(bits8(Integer.toHexString(mips.Data.getPortaA())).toUpperCase());
                animacaoTextual(NAREGData_IN, animacao);
                break;
            case 25: 
                NAREGA_IN.setText(bits8(Integer.toHexString(mips.A_B.getPortaA())).toUpperCase());
                animacaoTextual(NAREGA_IN, animacao);
                break;
            case 26: 
                NAREGB_IN.setText(bits8(Integer.toHexString(mips.A_B.getPortaB())).toUpperCase());
                animacaoTextual(NAREGB_IN, animacao);
                break;
            case 28: 
                NAREGB_OUT.setText(bits8(Integer.toHexString(mips.A_B.getSaidaB())).toUpperCase());
                animacaoTextual(NAREGB_OUT, animacao);
                break;
            case 44: 
                NAREGALUOut_IN.setText(bits8(Integer.toHexString(mips.ALUOut.getPortaA())).toUpperCase());
                animacaoTextual(NAREGALUOut_IN, animacao);
                break;
            case 60: 
                Addr.setText(bits8(Integer.toHexString(Integer.parseInt(mips.Addr, 2))).toUpperCase());
                animacaoTextual(Addr, animacao);
                break;
            case 63: 
                Addr_x2.setText(bits8(Integer.toHexString(Integer.parseInt(mips.AddrX2, 2))).toUpperCase());
                animacaoTextual(Addr_x2, animacao);
                break;
            case 64: 
                PCJump.setText(bits8(Integer.toHexString(mips.PCJump)).toUpperCase());
                animacaoTextual(PCJump, animacao);
                break;
            case 66: 
                PC_4bits.setText("0"); //tanto na arquitetura original de memoria quanto na proposta, os 4 ultimos bits do segmento Text sempre serão 0000.
                animacaoTextual(PC_4bits, animacao);
                break;
            case 110: 
                UC_Branch.setText(Integer.toHexString(mips.uc.getBranch()));
                animacaoTextual(UC_Branch, animacao);
                AND_0.setText(Integer.toHexString(mips.uc.getBranch()));
                animacaoTextual(AND_0, animacao);
                break;
            case 52: 
                AND_1.setText(Integer.toHexString(mips.ula.getFlagZero()));
                animacaoTextual(AND_1, animacao);
                break;
            case 109: 
                UC_PCWrite.setText(Integer.toHexString(mips.uc.getPCWrite()));
                animacaoTextual(UC_PCWrite, animacao);
                OR_0.setText(Integer.toHexString(mips.uc.getPCWrite()));
                animacaoTextual(OR_0, animacao);
                OR_OUT.setText(String.valueOf(mips.OR_out ? 1 : 0));
                animacaoTextual(OR_OUT, animacao);
                break;
            case 124: 
                OR_1.setText(String.valueOf(mips.AND_out ? 1 : 0));
                animacaoTextual(OR_1, animacao);
                OR_OUT.setText(String.valueOf(mips.OR_out ? 1 : 0));
                animacaoTextual(OR_OUT, animacao);
                break;
            case 101: 
                UC_IorD.setText(Integer.toHexString(mips.uc.getIord()));
                animacaoTextual(UC_IorD, animacao);
                break;
            case 103: 
                UC_MemWrite.setText(Integer.toHexString(mips.uc.getMemWrite()));
                animacaoTextual(UC_MemWrite, animacao);
                break;
            case 105: 
                UC_IRWrite.setText(Integer.toHexString(mips.uc.getIRWrite()));
                animacaoTextual(UC_IRWrite, animacao);
                break;
            case 9: 
                UC_Opcode.setText(Integer.toHexString(Integer.parseInt(mips.uc.getOpcode(), 2)).toUpperCase());
                animacaoTextual(UC_Opcode, animacao);
                break;
            case 8: 
                UC_Funct.setText(Integer.toHexString(Integer.parseInt(mips.uc.getFunct(), 2)).toUpperCase());
                animacaoTextual(UC_Funct, animacao);
                break;
            case 111: 
                UC_PCSrc.setText(Integer.toHexString(mips.uc.getPCSrc()));
                animacaoTextual(UC_PCSrc, animacao);
                break;
            case 115: 
                UC_ALUControl.setText(Integer.toHexString(mips.uc.getALUControl()));
                animacaoTextual(UC_ALUControl, animacao);
                UC_ALUOp.setText(Integer.toHexString(mips.uc.getALUOp()));
                animacaoTextual(UC_ALUOp, animacao);
                break;
            case 117: 
                UC_ALUSrcB.setText(Integer.toHexString(mips.uc.getALUSrcB()));
                animacaoTextual(UC_ALUSrcB, animacao);
                break;
            case 119: 
                UC_ALUSrcA.setText(Integer.toHexString(mips.uc.getALUSrcA()));
                animacaoTextual(UC_ALUSrcA, animacao);
                break;
            case 121: 
                UC_RegWrite.setText(Integer.toHexString(mips.uc.getRegWrite()));
                animacaoTextual(UC_RegWrite, animacao);
                break;
            case 122:
                //BarraLateral.insertOnRegister(MIPS.RegisterFile.bar_info[0], bits8(Integer.toHexString(MIPS.RegisterFile.bar_info[1]))); //insere o valor atualizado nos registradores.
                BarraLateral.insertOnRegister(MIPS.RegisterFile.bar_info[0], Integer.toHexString(MIPS.RegisterFile.bar_info[1]).toUpperCase()); //insere o valor atualizado nos registradores.
                break;
            case 107: 
                UC_RegDst.setText(Integer.toHexString(mips.uc.getRegDst()));
                animacaoTextual(UC_RegDst, animacao);
                break;
            case 108: 
                UC_MemtoReg.setText(Integer.toHexString(mips.uc.getMemtoReg()));
                animacaoTextual(UC_MemtoReg, animacao);
                break;
            case 78: 
                MUXMemtoReg_2.setText(bits8(Integer.toHexString(mips.MUX_MemtoReg_2)).toUpperCase());
                animacaoTextual(MUXMemtoReg_2, animacao);
                break;
            /*case 106:
                NAREGInstr_OUT.setText(bits8(Integer.toHexString(mips.Instr.getSaidaA())).toUpperCase());
                animacaoTextual(NAREGInstr_OUT, animacao);
                break;*/

            case 150: //esse numero não representa o ID de um terminal, foi escolhido ao acaso para atualizar apenas o PC quando estiver na borda de subida do estado S1
                PC_PC.setText(bits8(Integer.toHexString(mips.pc.getPC())).toUpperCase());
                animacaoTextual(PC_PC, animacao);
            default: 
                //System.out.println("Nenhum texto atualizado.");
                
        } 
    }

    public String bits8(String text){
        while(text.length() != 8){
            text = "0"+text;
        }
        return text;
    }



    public static void animacaoTextual(Text text, int animacao){
        if(animacao == 0){
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TIME_ANIMATION), text);  
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setCycleCount(1);
            fadeTransition.setOnFinished(event -> { text.setOpacity(1.0); });// Restaura a opacidade para 1 (100%) após a animação acabar
            fadeTransition.play(); //executa a animação

        } else if(animacao == 1) {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(TIME_ANIMATION), text);
            scaleTransition.setFromX(1.0);
            scaleTransition.setToX(2.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToY(2.0);
            scaleTransition.setCycleCount(2);
            scaleTransition.setAutoReverse(true); // Faz a transição voltar ao estado original
            scaleTransition.play();

        } else if(animacao == 2){
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(TIME_ANIMATION), text);
            rotateTransition.setByAngle(360);
            rotateTransition.setCycleCount(1);
            rotateTransition.play();
        
        }else {
            throw new IllegalArgumentException("[InfoPath.java]: animacaoTextual() - ERRO: Animação não disponível.");
        }
    }

    public static void applyDropShadow(){ //melhora a leitura do texto aplicar sombra projetada.
        for (Text a : TextList){
            Effects.efeitoDropShadow(a);
            //a.setFill(Color.BLACK);
            //a.setStyle("-fx-fill: gray; ");
        }
        
    }

    public static void TextoOFF(){ //desabilita a visibilidade do texto, deixando a interface sem as informações nos modulos do MIPS
        for (Text a : TextList){
            a.setVisible(false);
        }
    }

    public static void TextoON(){ //desabilita a visibilidade do texto, deixando a interface sem as informações nos modulos do MIPS
        for (Text a : TextList){
            a.setVisible(true);
        }
    }

    public void resetText(){ //reseta o texto
        for (Text a : TextList){
            a.setText("0");
        }
        signal_ALUOp.setText("?");
    }

    public void showWaitClock(){
        this.wait_clock.setVisible(true);
        //animação
        this.timeline = new Timeline( //mesma animação dos estados da classe StateActive
            new KeyFrame(Duration.ZERO, new KeyValue(this.wait_clock.opacityProperty(), 1.0)),
            new KeyFrame(new Duration(500), new KeyValue(this.wait_clock.opacityProperty(), 0.5)),
            new KeyFrame(new Duration(1000), new KeyValue(this.wait_clock.opacityProperty(), 1.0))
        );

        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.setAutoReverse(true);
        this.timeline.play();
    }

    public void notShowWaitClock(){
        if (this.timeline != null) {
            this.timeline.stop(); // para a animação
        }
        this.wait_clock.setVisible(false);
    }

    public static void alternateFont(){ //modifica a fonte do texto
        fonte = new Font(Main.font_text);
        
        for (Text a : TextList){
            a.setFont(fonte);
        }
    }
}
