package GUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MIPS.Memory;
import MIPS.Mips;
import MIPS.PC;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DataPath{
    private int mod_x = Main.mod_x;
    private int mod_y = Main.mod_y;
    private Terminal a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,a33,a34,a35,a36,a37,a38,a39,a40,a41,a42,a43,a44,a45,a46,a47,a48,a49,a50,
    a51,a52,a53,a54,a55,a56,a57,a58,a59,a60,a61,a62,a63,a64,a65,a66,a67,a68,a69,a70,a71,a72,a73,a74,a75,a76,a77,a78,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26,t27;
    private ArrayList<StateActive> ativarEstado;
    private double speed_terminal = 1.0;
    private double speed_terminal_uc = 1.0; //velocidade da energia nos terminais da UC é mais rápido.
    private ArrayList<Terminal> terminaisHabilitados;
    public static boolean enableFlagZero; //variavel que habilita o caminho dos dados da flagzero.
    private InfoPath infoPath;
    static List<Terminal> TerminalList;
    private static int last_terminal = 0; 
    private static int simultaneous_operation = 2; // 2 pois há "terminaisList" e "terminaisList2". Se houver mais listas, mudar o valor.
    public static boolean enable_parallel = true;

    public DataPath(Pane rootLayout, InfoPath infoPath, ArrayList<StateActive> ativarEstado){
        this.terminaisHabilitados = new ArrayList<Terminal>();
        this.infoPath = infoPath;
        this.ativarEstado = ativarEstado; //array que ativa os estados da barra de estados graficamente inserindo cor verde em seu interior.
        // cada estado representa um indice:
        // indice 0 -> estado S0;
        // indice 1 -> estado S1;
        // etc...


        // _____________________________ (Configuração dos terminais) _______________________________
        // coordenada de inicio (startX, startY) e coordenada de fim (endX, endY)
        // mod_x = 362;  mod_y = 0;
        //                             ⬌          ⬍           ⬌          ⬍     
        //[NOTA]: foi inserido a variavel mod_x e mod_y para permitir que os terminais se movam junto aos modulos do mips, representado pela view "modulosMIPSView".
        this.a1 = new Terminal(1,mod_x+119, mod_y+364, mod_x+169, mod_y+364, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     // PC → MUX_Iord(0)
        this.a2 = new Terminal(2,mod_x+218, mod_y+383, mod_x+248, mod_y+383, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     // Mux_Iord → I/D_Memory(A)
        this.a3 = new Terminal(3,mod_x+368, mod_y+383, mod_x+431, mod_y+383, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(),5.0, speed_terminal,false);     // I/D_Memory(RD) → NA_Reg_Instr(input)
        this.a4 = new Terminal(4,mod_x+394, mod_y+387, mod_x+394, mod_y+505, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);         // I/D_Memory(RD) → NA_Reg_Data(input)¹
        this.a5 = new Terminal(5,mod_x+398, mod_y+505, mod_x+431, mod_y+505, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     // I/D_Memory(RD) → NA_Reg_Data(input)²
        this.a6 = new Terminal(6,mod_x+488, mod_y+384, mod_x+509, mod_y+384, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     // NA_Reg_Instr(output) → NA_Reg_Instr(output)
        this.a7 = new Terminal(7,mod_x+509, mod_y+202, mod_x+509, mod_y+380, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);          // NA_Reg_Instr(output) → UC(opcode, funct)
        this.a8 = new Terminal(8,mod_x+513, mod_y+222, mod_x+522, mod_y+222, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     // NA_Reg_Instr(output) → UC(funct)
        this.a9 = new Terminal(9,mod_x+513, mod_y+202, mod_x+522, mod_y+202, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     // NA_Reg_Instr(output) → UC(opcode)
        this.a10 = new Terminal(10,mod_x+509, mod_y+388, mod_x+509, mod_y+647, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        // NA_Reg_Instr(output) → NA_Reg_Instr(output)
        this.a11 = new Terminal(11,mod_x+513, mod_y+376, mod_x+632, mod_y+376, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // NA_Reg_Instr(output) → RegisterFile(A1)
        this.a12 = new Terminal(12,mod_x+513, mod_y+404, mod_x+632, mod_y+404, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // NA_Reg_Instr(output) → RegisterFile(A2)
        this.a13 = new Terminal(13,mod_x+513, mod_y+436, mod_x+546, mod_y+436, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // NA_Reg_Instr(output) → MUX_RegDst(0)
        this.a14 = new Terminal(14,mod_x+513, mod_y+458, mod_x+546, mod_y+458, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // NA_Reg_Instr(output) → MUX_RegDst(1)
        this.a15 = new Terminal(15,mod_x+582, mod_y+447, mod_x+632, mod_y+447, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // MUX_RegDst(output) → RegisterFile(A3)
        this.a16 = new Terminal(16,mod_x+488, mod_y+504, mod_x+586, mod_y+504, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // NA_Reg_Data(output) → MUX_MemtoReg(1)
        this.a17 = new Terminal(17,mod_x+621, mod_y+493, mod_x+632, mod_y+493, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    // MUX_MemtoReg(output) → RegisterFile(WD3)
        this.a18 = new Terminal(18,mod_x+513, mod_y+621, mod_x+643, mod_y+621, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   // NA_Reg_Instr(output) → Sign_extend
        this.a19 = new Terminal(19,mod_x+739, mod_y+617, mod_x+814, mod_y+617, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   // Sign_extend → <<2
        this.a20 = new Terminal(20,mod_x+777, mod_y+498, mod_x+777, mod_y+613, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        //  Sign_extend → MUX_ALUSrcB(2)¹
        this.a21 = new Terminal(21,mod_x+781, mod_y+498, mod_x+889, mod_y+498, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  Sign_extend → MUX_ALUSrcB(2)²
        this.a22 = new Terminal(22,mod_x+830, mod_y+525, mod_x+830, mod_y+589, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        //  <<2 → MUX_ALUSrcB(3)¹
        this.a23 = new Terminal(23,mod_x+834, mod_y+525, mod_x+889, mod_y+525, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  <<2 → MUX_ALUSrcB(3)²
        this.a24 = new Terminal(24,mod_x+838, mod_y+474, mod_x+889, mod_y+474, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  "4" → MUX_ALUSrcB(1)
        this.a25 = new Terminal(25,mod_x+771, mod_y+376, mod_x+796, mod_y+376, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  RegisterFile(RD1) → NAReg_A_B(A)
        this.a26 = new Terminal(26,mod_x+771, mod_y+404, mod_x+796, mod_y+404, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  RegisterFile(RD2) → NAReg_A_B(B)
        this.a27 = new Terminal(27,mod_x+852, mod_y+376, mod_x+890, mod_y+376, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NAReg_A_B(A) → MUX_ALUSrcA(1)
        this.a28 = new Terminal(28,mod_x+852, mod_y+404, mod_x+868, mod_y+404, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NAReg_A_B(B) → MUX_ALUSrcB(0)¹
        this.a29 = new Terminal(29,mod_x+868, mod_y+408, mod_x+868, mod_y+447, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       //  NAReg_A_B(B) → MUX_ALUSrcB(0)²
        this.a30 = new Terminal(30,mod_x+872, mod_y+447, mod_x+889, mod_y+447, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NAReg_A_B(B) → MUX_ALUSrcB(0)³
        this.a31 = new Terminal(31,mod_x+144, mod_y+324, mod_x+144, mod_y+360, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);          //  PC → MUX_ALUSrcA(0)¹
        this.a32 = new Terminal(32,mod_x+148, mod_y+324, mod_x+868, mod_y+324, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    //  PC → MUX_ALUSrcA(0)²
        this.a33 = new Terminal(33,mod_x+868, mod_y+328, mod_x+868, mod_y+342, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       //  PC → MUX_ALUSrcA(0)³
        this.a34 = new Terminal(34,mod_x+872, mod_y+342, mod_x+890, mod_y+342, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  PC → MUX_ALUSrcA(0)⁴
        this.a35 = new Terminal(35,mod_x+803, mod_y+447, mod_x+864, mod_y+447, "horizontal", "esquerda", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);  //  NAReg_A_B(B) → I/D_Memory(WD)¹
        this.a36 = new Terminal(36,mod_x+803, mod_y+451, mod_x+803, mod_y+562, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       //  NAReg_A_B(B) → I/D_Memory(WD)²
        this.a37 = new Terminal(37,mod_x+218, mod_y+562, mod_x+799, mod_y+562, "horizontal", "esquerda", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NAReg_A_B(B) → I/D_Memory(WD)³
        this.a38 = new Terminal(38,mod_x+218, mod_y+470, mod_x+218, mod_y+558, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);          //  NAReg_A_B(B) → I/D_Memory(WD)⁴
        this.a39 = new Terminal(39,mod_x+222, mod_y+470, mod_x+248, mod_y+470, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     //  NAReg_A_B(B) → I/D_Memory(WD)⁵
        this.a40 = new Terminal(40,mod_x+939, mod_y+357, mod_x+1010, mod_y+357, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  MUX_ALUSrcA(output) → ULA(SrcA)
        this.a41 = new Terminal(41,mod_x+962, mod_y+480, mod_x+983, mod_y+480, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  MUX_ALUSrcB(output) → ULA(SrcB)¹
        this.a42 = new Terminal(42,mod_x+983, mod_y+433, mod_x+983, mod_y+476, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        //  MUX_ALUSrcB(output) → ULA(SrcB)²
        this.a43 = new Terminal(43,mod_x+987, mod_y+433, mod_x+1010, mod_y+433, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  MUX_ALUSrcB(output) → ULA(SrcB)³
        this.a44 = new Terminal(44,mod_x+1089, mod_y+396, mod_x+1135, mod_y+396, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  ULA(ALUResult) → NAReg_ALUOut(input)
        this.a45 = new Terminal(45,mod_x+1118, mod_y+340, mod_x+1118, mod_y+392, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        //  ULA(ALUResult) → MUX_PCSrc(0)¹
        this.a46 = new Terminal(46,mod_x+1122, mod_y+340, mod_x+1208, mod_y+340, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  ULA(ALUResult) → MUX_PCSrc(0)²
        this.a47 = new Terminal(47,mod_x+1208, mod_y+344, mod_x+1208, mod_y+374, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       //  ULA(ALUResult) → MUX_PCSrc(0)³
        this.a48 = new Terminal(48,mod_x+1212, mod_y+374, mod_x+1240, mod_y+374, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  ULA(ALUResult) → MUX_PCSrc(0)⁴
        this.a49 = new Terminal(49,mod_x+1192, mod_y+397, mod_x+1240, mod_y+397, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NAReg_ALUOut(output) → MUX_PCSrc(1)
        this.a50 = new Terminal(50,mod_x+1088, mod_y+374, mod_x+1103, mod_y+374, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);   //  FlagZero → AND(1)¹
        this.a51 = new Terminal(51,mod_x+1103, mod_y+117, mod_x+1103, mod_y+373, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);        //  FlagZero → AND(1)²
        this.a52 = new Terminal(52,mod_x+1104, mod_y+117, mod_x+1145, mod_y+117, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);   //  FlagZero → AND(1)³
        this.a53 = new Terminal(53,mod_x+1224, mod_y+401, mod_x+1224, mod_y+670, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       //  NAReg_ALUOut(output) → MUX_MemtoReg(0)¹
        this.a54 = new Terminal(54,mod_x+568, mod_y+670, mod_x+1220, mod_y+670, "horizontal", "esquerda", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NAReg_ALUOut(output) → MUX_MemtoReg(0)²
        this.a55 = new Terminal(55,mod_x+568, mod_y+482, mod_x+568, mod_y+666, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);          //  NAReg_ALUOut(output) → MUX_MemtoReg(0)³
        this.a56 = new Terminal(56,mod_x+572, mod_y+482, mod_x+586, mod_y+482, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     //  NAReg_ALUOut(output) → MUX_MemtoReg(0)⁴
        this.a57 = new Terminal(57,mod_x+148, mod_y+670, mod_x+564, mod_y+670, "horizontal", "esquerda", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    //  NAReg_ALUOut(output) → MUX_Iord(1)¹
        this.a58 = new Terminal(58,mod_x+148, mod_y+398, mod_x+148, mod_y+666, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);          //  NAReg_ALUOut(output) → MUX_Iord(1)²
        this.a59 = new Terminal(59,mod_x+152, mod_y+398, mod_x+169, mod_y+398, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     //  NAReg_ALUOut(output) → MUX_Iord(1)³
        this.a60 = new Terminal(60,mod_x+513, mod_y+647, mod_x+1038, mod_y+647, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);    //  NA_Reg_Instr(output) → Addr(26 bits)¹
        this.a61 = new Terminal(61,mod_x+1038, mod_y+570, mod_x+1038, mod_y+643, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        //  NA_Reg_Instr(output) → Addr(26 bits)²
        this.a62 = new Terminal(62,mod_x+1042, mod_y+570, mod_x+1144, mod_y+570, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NA_Reg_Instr(output) → Addr(26 bits)³
        this.a63 = new Terminal(63,mod_x+1189, mod_y+570, mod_x+1207, mod_y+570, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NA_Reg_Instr(output) → PCJump¹
        this.a64 = new Terminal(64,mod_x+1207, mod_y+420, mod_x+1207, mod_y+566, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        //  NA_Reg_Instr(output) → PCJump²
        this.a65 = new Terminal(65,mod_x+1211, mod_y+420, mod_x+1240, mod_y+420, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  NA_Reg_Instr(output) → PCJump³
        this.a66 = new Terminal(66,mod_x+869, mod_y+298, mod_x+869, mod_y+322, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);        //  PC(4 bits) → PCJump¹
        this.a67 = new Terminal(67,mod_x+870, mod_y+298, mod_x+1001, mod_y+298, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);   //  PC(4 bits) → PCJump²
        this.a68 = new Terminal(68,mod_x+1001, mod_y+300, mod_x+1001, mod_y+500, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);       //  PC(4 bits) → PCJump³
        this.a69 = new Terminal(69,mod_x+1002, mod_y+500, mod_x+1203, mod_y+500, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 2.0, speed_terminal,false);   //  PC(4 bits) → PCJump³
        this.a70 = new Terminal(70,mod_x+1305, mod_y+397, mod_x+1318, mod_y+397, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  MUX_PCSrc(output) → PC'¹
        this.a71 = new Terminal(71,mod_x+1318, mod_y+401, mod_x+1318, mod_y+700, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       //  MUX_PCSrc(output) → PC'²
        this.a72 = new Terminal(72,mod_x+38, mod_y+700, mod_x+1314, mod_y+700, "horizontal", "esquerda", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   //  MUX_PCSrc(output) → PC'³
        this.a73 = new Terminal(73,mod_x+38, mod_y+364, mod_x+38, mod_y+696, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);          //  MUX_PCSrc(output) → PC'⁴
        this.a74 = new Terminal(74,mod_x+42, mod_y+364, mod_x+58, mod_y+364, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);     //  MUX_PCSrc(output) → PC'⁵

        //terminais a75 até a78 fazem parte da versão 1.02 que da suporte a instruções jal e jr
        this.a75 = new Terminal(75,mod_x+129, mod_y+368, mod_x+129, mod_y+580, "vertical", "baixo", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);       // PC → MUX_MemtoReg(2)¹
        this.a76 = new Terminal(76,mod_x+133, mod_y+580, mod_x+540, mod_y+580, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   // PC → MUX_MemtoReg(2)²
        this.a77 = new Terminal(77,mod_x+540, mod_y+526, mod_x+540, mod_y+576, "vertical", "cima", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);        // PC → MUX_MemtoReg(2)³
        this.a78 = new Terminal(78,mod_x+544, mod_y+526, mod_x+580, mod_y+526, "horizontal", "direita", Main.getCorEnergia(), Main.getGradiente(), 5.0, speed_terminal,false);   // PC → MUX_MemtoReg(2)⁴

        //terminais de enable da UC
        this.t1 = new Terminal(101,mod_x+203, mod_y+73, mod_x+524, mod_y+73, "horizontal", "esquerda", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);       //  IorD → MUX_IorD¹
        this.t2 = new Terminal(102,mod_x+203, mod_y+75, mod_x+203, mod_y+350, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  IorD → MUX_IorD²
        this.t3 = new Terminal(103,mod_x+313, mod_y+93, mod_x+524, mod_y+93, "horizontal", "esquerda", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);       //  MemWrite → I/D_Memory(WE)¹
        this.t4 = new Terminal(104,mod_x+313, mod_y+95, mod_x+313, mod_y+348, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  MemWrite → I/D_Memory(WE)²
        this.t5 = new Terminal(105,mod_x+472, mod_y+113, mod_x+524, mod_y+113, "horizontal", "esquerda", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);     //  IRWrite → NA_Reg_Instr(EN)¹
        this.t6 = new Terminal(106,mod_x+472, mod_y+115, mod_x+472, mod_y+349, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);          //  IRWrite → NA_Reg_Instr(EN)²
        this.t7 = new Terminal(107,mod_x+566, mod_y+255, mod_x+566, mod_y+423, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);          //  RegDst → MUX_RegDst
        this.t8 = new Terminal(108,mod_x+606, mod_y+255, mod_x+606, mod_y+469, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);          //  MemtoReg → MUX_MemtoReg
        this.t9 = new Terminal(109,mod_x+654, mod_y+78, mod_x+1220, mod_y+78, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);       //  PCWrite → OR(0)
        this.t10 = new Terminal(110,mod_x+654, mod_y+98, mod_x+1144, mod_y+98, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  Branch → AND(0)
        this.t11 = new Terminal(111,mod_x+654, mod_y+118, mod_x+1058, mod_y+118, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);    //  PCSrc → MUX_PCSrc¹
        this.t12 = new Terminal(112,mod_x+1058, mod_y+120, mod_x+1058, mod_y+130, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);        //  PCSrc → MUX_PCSrc²
        this.t13 = new Terminal(113,mod_x+1060, mod_y+130, mod_x+1278, mod_y+130, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);    //  PCSrc → MUX_PCSrc³
        this.t14 = new Terminal(114,mod_x+1278, mod_y+132, mod_x+1278, mod_y+351, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);        //  PCSrc → MUX_PCSrc⁴
        this.t15 = new Terminal(115,mod_x+654, mod_y+138, mod_x+1048, mod_y+138, "horizontal", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  ALU_Control → ULA¹
        this.t16 = new Terminal(116,mod_x+1048, mod_y+140, mod_x+1048, mod_y+335, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);        //  ALU_Control → ULA²
        this.t17 = new Terminal(117,mod_x+654, mod_y+157, mod_x+948, mod_y+157, "horizontal", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  ALUSrcB → MUX_ALUSrcB¹
        this.t18 = new Terminal(118,mod_x+948, mod_y+159, mod_x+948, mod_y+442, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);        //  ALUSrcB → MUX_ALUSrcB²
        this.t19 = new Terminal(119,mod_x+654, mod_y+177, mod_x+918, mod_y+177, "horizontal", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  ALUSrcA → MUX_ALUSrcA¹
        this.t20 = new Terminal(120,mod_x+918, mod_y+179, mod_x+918, mod_y+325, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);        //  ALUSrcA → MUX_ALUSrcA²
        this.t21 = new Terminal(121,mod_x+654, mod_y+197, mod_x+705, mod_y+197, "horizontal", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  RegWrite → RegisterFile(WE3)¹
        this.t22 = new Terminal(122,mod_x+705, mod_y+199, mod_x+705, mod_y+348, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);        //  RegWrite → RegisterFile(WE3)²
        this.t23 = new Terminal(123,mod_x+1200, mod_y+97, mod_x+1200, mod_y+107, "vertical", "cima", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  AND(output) → OR(1)¹
        this.t24 = new Terminal(124,mod_x+1202, mod_y+97, mod_x+1220, mod_y+97, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  AND(output) → OR(1)²
        this.t25 = new Terminal(125,mod_x+1275, mod_y+25, mod_x+1275, mod_y+88, "vertical", "cima", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  OR(output) → PC(en)¹
        this.t26 = new Terminal(126,mod_x+103, mod_y+23, mod_x+1275, mod_y+23, "horizontal", "esquerda", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);      //  OR(output) → PC(en)²
        this.t27 = new Terminal(127,mod_x+103, mod_y+25, mod_x+103, mod_y+328, "vertical", "baixo", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  OR(output) → PC(en)³

        //terminais para delay (atraso) no carregamento de alguns terminais
        //this.delay1 = new Terminal(200,Main.back_x+100, Main.back_y, Main.back_x+200, Main.back_y, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  OR(output) → PC(en)³
        //this.delay2 = new Terminal(201,Main.back_x+100, Main.back_y+50, Main.back_x+300, Main.back_y+50, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  OR(output) → PC(en)³
        //this.delay3 = new Terminal(202,Main.back_x+100, Main.back_y+100, Main.back_x+400, Main.back_y+100, "horizontal", "direita", Color.AQUA, Main.getGradiente(), 2.0, speed_terminal_uc, true);           //  OR(output) → PC(en)³
        
        
        
        // inserção dos objetos na GUI
        rootLayout.getChildren().addAll(      a1.getTerminal(),  a1.getEnergia(),  a2.getTerminal(),  a2.getEnergia(),  a3.getTerminal(),  a3.getEnergia(),
         a4.getTerminal(),  a4.getEnergia(),  a5.getTerminal(),  a5.getEnergia(),  a6.getTerminal(),  a6.getEnergia(),  a7.getTerminal(),  a7.getEnergia(),
         a8.getTerminal(),  a8.getEnergia(),  a9.getTerminal(),  a9.getEnergia(), a10.getTerminal(), a10.getEnergia(), a11.getTerminal(), a11.getEnergia(),
        a12.getTerminal(), a12.getEnergia(), a13.getTerminal(), a13.getEnergia(), a14.getTerminal(), a14.getEnergia(), a15.getTerminal(), a15.getEnergia(),
        a16.getTerminal(), a16.getEnergia(), a17.getTerminal(), a17.getEnergia(), a18.getTerminal(), a18.getEnergia(), a19.getTerminal(), a19.getEnergia(),
        a20.getTerminal(), a20.getEnergia(), a21.getTerminal(), a21.getEnergia(), a22.getTerminal(), a22.getEnergia(), a23.getTerminal(), a23.getEnergia(),
        a24.getTerminal(), a24.getEnergia(), a25.getTerminal(), a25.getEnergia(), a26.getTerminal(), a26.getEnergia(), a27.getTerminal(), a27.getEnergia(),
        a28.getTerminal(), a28.getEnergia(), a29.getTerminal(), a29.getEnergia(), a30.getTerminal(), a30.getEnergia(), a31.getTerminal(), a31.getEnergia(),
        a32.getTerminal(), a32.getEnergia(), a33.getTerminal(), a33.getEnergia(), a34.getTerminal(), a34.getEnergia(), a35.getTerminal(), a35.getEnergia(),
        a36.getTerminal(), a36.getEnergia(), a37.getTerminal(), a37.getEnergia(), a38.getTerminal(), a38.getEnergia(), a39.getTerminal(), a39.getEnergia(),
        a40.getTerminal(), a40.getEnergia(), a41.getTerminal(), a41.getEnergia(), a42.getTerminal(), a42.getEnergia(), a43.getTerminal(), a43.getEnergia(),
        a44.getTerminal(), a44.getEnergia(), a45.getTerminal(), a45.getEnergia(), a46.getTerminal(), a46.getEnergia(), a47.getTerminal(), a47.getEnergia(),
        a48.getTerminal(), a48.getEnergia(), a49.getTerminal(), a49.getEnergia(), a50.getTerminal(), a50.getEnergia(), a51.getTerminal(), a51.getEnergia(),
        a52.getTerminal(), a52.getEnergia(), a53.getTerminal(), a53.getEnergia(), a54.getTerminal(), a54.getEnergia(), a55.getTerminal(), a55.getEnergia(),
        a56.getTerminal(), a56.getEnergia(), a57.getTerminal(), a57.getEnergia(), a58.getTerminal(), a58.getEnergia(), a59.getTerminal(), a59.getEnergia(),
        a60.getTerminal(), a60.getEnergia(), a61.getTerminal(), a61.getEnergia(), a62.getTerminal(), a62.getEnergia(), a63.getTerminal(), a63.getEnergia(),
        a64.getTerminal(), a64.getEnergia(), a65.getTerminal(), a65.getEnergia(), a66.getTerminal(), a66.getEnergia(), a67.getTerminal(), a67.getEnergia(),
        a68.getTerminal(), a68.getEnergia(), a69.getTerminal(), a69.getEnergia(), a70.getTerminal(), a70.getEnergia(), a71.getTerminal(), a71.getEnergia(),
        a72.getTerminal(), a72.getEnergia(), a73.getTerminal(), a73.getEnergia(), a74.getTerminal(), a74.getEnergia(), a75.getTerminal(), a75.getEnergia(), 
        a76.getTerminal(), a76.getEnergia(), a77.getTerminal(), a77.getEnergia(), a78.getTerminal(), a78.getEnergia(),  t1.getTerminal(),  t1.getEnergia(),  
         t2.getTerminal(),  t2.getEnergia(),  t3.getTerminal(),  t3.getEnergia(),  t4.getTerminal(),  t4.getEnergia(),  t5.getTerminal(),  t5.getEnergia(),  
         t6.getTerminal(),  t6.getEnergia(),  t7.getTerminal(),  t7.getEnergia(),  t8.getTerminal(),  t8.getEnergia(),  t9.getTerminal(),  t9.getEnergia(), 
        t10.getTerminal(), t10.getEnergia(), t11.getTerminal(), t11.getEnergia(), t12.getTerminal(), t12.getEnergia(), t13.getTerminal(), t13.getEnergia(), 
        t14.getTerminal(), t14.getEnergia(), t15.getTerminal(), t15.getEnergia(), t16.getTerminal(), t16.getEnergia(), t17.getTerminal(), t17.getEnergia(), 
        t18.getTerminal(), t18.getEnergia(), t19.getTerminal(), t19.getEnergia(), t20.getTerminal(), t20.getEnergia(), t21.getTerminal(), t21.getEnergia(), 
        t22.getTerminal(), t22.getEnergia(), t23.getTerminal(), t23.getEnergia(), t24.getTerminal(), t24.getEnergia(), t25.getTerminal(), t25.getEnergia(), 
        t26.getTerminal(), t26.getEnergia(), t27.getTerminal(), t27.getEnergia());


        //[OPCIONAL]: lista criada coma finalidade de aplicar efeitos nos terminais - Basta descomentar e selecionar o efeito dentro do metodo "applyEffectsOnTerminal()".
        TerminalList = Arrays.asList(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,
        a33,a34,a35,a36,a37,a38,a39,a40,a41,a42,a43,a44,a45,a46,a47,a48,a49,a50,a51,a52,a53,a54,a55,a56,a57,a58,a59,a60,a61,a62,a63,a64,a65,a66,a67,a68,a69,
        a70,a71,a72,a73,a74,a75,a76,a77,a78,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26,t27);

        //applyEffectsOnTerminal();
        //trecho apenas para aplicar a cor de terminal energizado nesses 4 terminais.
        a66.setEnergizado(true);
        a67.setEnergizado(true);
        a68.setEnergizado(true);
        a69.setEnergizado(true);
    }

    public void setInfoPath(InfoPath infoPath) { this.infoPath = infoPath;}

    public void setEnableFlagZero(boolean bool){ this.enableFlagZero = bool;}
    public ArrayList<Terminal> getTerminaisHabilitados() { return this.terminaisHabilitados;}




    public void executaAnimacao(ArrayList<Terminal> terminais){ //energiza os terminais de forma sequencial, um após o outro
        terminais.get(0).energizar(); //executa o primeiro terminal
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), event -> { //verifica a condição de 200 em 200ms
            if (!terminais.isEmpty() && terminais.get(0).getStatusTimeline() == Timeline.Status.STOPPED) { // só executa quando o método energizar() do objeto anterior terminar de executar
                terminaisHabilitados.add(terminais.get(0)); //salva os terminais que estão habilitados para posteriormente desabilitar.
                //verifica id para atualizar texto vinculado a entrada do terminal
                infoPath.atualizaInfo(terminais.get(0).getId()); //cada info nos modulos (Text) está vinculado ao ID do terminal que se conecta a ele. Basta então passar o ID que o valor no Text será atualizado.
                terminais.get(0).setDelay(1.0); //volta ao valor padrão 1.0
                terminais.remove(0); // remove o primeiro terminal que já foi executado para que outro tome seu lugar no índice 0
                if (!terminais.isEmpty()) {
                    //terminais.get(0).animacaoEspelhamento(); //testando nova animação de espelhamento.
                    terminais.get(0).energizar(); // executa o próximo
                } else {
                    timeline.stop(); // para a Timeline quando todos os terminais forem processados
                    terminais.clear();
                    last_terminal++; //variavel que gantirá que apenas o ultimo terminal carregado em uma execução paralela, execute o if abaixo
                    if(last_terminal == simultaneous_operation || !enable_parallel){ 

                        //___________ (Trecho para atualizar a posição do CURSOR na memória de instruções (TEXT Segment)) ___________ 
                        // beq - endereço relativo - atualiza o cursor
                        if((MIPS.UC.state_UC.equals("S8") /* || MIPS.UC.state_UC.equals("estado no formato de string - "S1", "S2", etc...")*/ )  && Mips.OR_out){ 
                            infoPath.atualizaInfo(150); //mostra graficamente PC = PC'
                            Memory.current_instruction.set( Memory.current_instruction.get() + Mips.SignImm+1); 
                            Main.counter_current_instruction = Memory.current_instruction.get();
    
                        // j, jal, jr - endereço absoluto e indireto - atualiza o cursor
                        }else if((MIPS.UC.state_UC.equals("S11") || MIPS.UC.state_UC.equals("S12") 
                                || MIPS.UC.state_UC.equals("S13") /* || MIPS.UC.state_UC.equals("estado no formato de string - "S1", "S2", etc...") */  ) && Mips.OR_out){ 
                            infoPath.atualizaInfo(150); 
                            int calc = (PC.getPC() - Memory.endereco_inicial_TEXT) / 4; //calculo para obter o deslocamento para o cursor da listview da memoria Text.
                            Memory.current_instruction.set( calc ); 
                            Main.counter_current_instruction = Memory.current_instruction.get();
                        }
                        
                        // _________________ (trecho que prepara a próxima simulação) ________________________
                        infoPath.showWaitClock(); // mostra a mensagem "waiting clock..." novamente. 
                        Main.clockButton.setDisable(false); // habilita o botão de clock e stop novamente para ser pressionado.
                        Main.stopButton.setDisable(false); 
                        BarraLateral.toggleSwitch1.setDisable(false); //habilita os toggle switchs novamente.
                        BarraLateral.toggleSwitch2.setDisable(false);
                        for(StateActive clock : Main.clockActivate){ //desabilita a animação de clock, indicando que houve uma borda de descida e o estado atual está finalizado.
                            clock.falling_edge();
                        }  
                        last_terminal = 0;
                        simultaneous_operation = 2; // por padrão é 2 pois na maioria dos casos da pra simular com apenas 2 operações em paralelo.
                    }
                }
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }





    //______________________________________________________________________________________________________________________________________________________________
    public void FSMStates(int state) {
        ArrayList<Terminal> terminais = new ArrayList<Terminal>();
        List<Terminal> terminaisList = null;

        //definição
        if(state == 0){ // estado S0
            terminaisList = Arrays.asList(t9,t25,t26,t27,a1,t1,t2,a2,a3,t5,t6,a31,a32,a33,a34,t19,t20,a40,a24,t17,t18,a41,a42,a43,t15,t16,a44,a45,a46,a47,a48,t11,t12,t13,t14,a70,a71,a72,a73,a74); //os terminais serão executados na sequencia que foram inseridos em "terminaisList".


        }else if(state == 1){ //estado S1
            infoPath.atualizaInfo(150); //mostra graficamente PC = PC'
            infoPath.atualizaInfo(6); //mostra graficamente RI(output) = mem[PC] 
            if(MIPS.UC.instr.equals("Tipo-R")){  // Tipo-R     
                terminaisList = Arrays.asList(a1,a2,t5,t6,a3,t9,t25,t26,t27,a6,a7,a8,a9,a10,a11,a12,a14,a25,a26); 
            } 
            else if(MIPS.UC.instr.equals("addi") || MIPS.UC.instr.equals("lw") || MIPS.UC.instr.equals("lb")){  // addi, lw, lb
                terminaisList = Arrays.asList(a1,a2,t5,t6,a3,t9,t25,t26,t27,a6,a7,a9,a10,a11,a13,a25,a18,a19,a20,a21);
            } 
            else if(MIPS.UC.instr.equals("sw") || MIPS.UC.instr.equals("sb")){  // sw, sb
                terminaisList = Arrays.asList(a1,a2,t5,t6,a3,t9,t25,t26,t27,a6,a7,a9,a10,a11,a12,a25,a26,a18,a19,a20,a21);
            } 
            else if(MIPS.UC.instr.equals("beq")){  // beq
                terminaisList = Arrays.asList(a1,a2,t5,t6,a3,t9,t25,t26,t27,a6,a7,a9,a10,a11,a12,a25,a26,a18,a19,a22,a23,a31,a32,a33,a34,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44);
            } 
            else if(MIPS.UC.instr.equals("j") || MIPS.UC.instr.equals("jal")){ // j, jal 
                terminaisList = Arrays.asList(a1,a2,t5,t6,a3,t9,t25,t26,t27,a6,a7,a8,a9,a10,a31,a32,a66,a67,a68,a69,a60,a61,a62,a63,a64,a65);
            } 
            else if(MIPS.UC.instr.equals("jr")){  // jr
                terminaisList = Arrays.asList(a1,a2,t5,t6,a3,t9,t25,t26,t27,a6,a7,a8,a9,a10,a11,a12,a25,a26);
            } 


        }else if(state == 2){ //estado S2
            if(MIPS.UC.instr.equals("lw") || MIPS.UC.instr.equals("lb")){ //lw, lb
                if(enableFlagZero){  terminaisList = Arrays.asList(a27,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a50,a51,a52,a44);}
                else{                terminaisList = Arrays.asList(a27,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44);}
            }else if(MIPS.UC.instr.equals("sw") || MIPS.UC.instr.equals("sb")){ //sw, sb
                if(enableFlagZero){  terminaisList = Arrays.asList(a27,a28,a29,a35,a36,a37,a38,a39,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a50,a51,a52,a44);}
                else{                terminaisList = Arrays.asList(a27,a28,a29,a35,a36,a37,a38,a39,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44);}
            }
            

        }else if(state == 3){ //estado s3
            terminaisList = Arrays.asList(a49,a53,a54,a57,a58,a59,t1,t2,a2,a3,a4,a5);
            

        }else if(state == 4){ //estado s4
            terminaisList = Arrays.asList(t7,a15,a16,t8,a17,t21,t22); 
            

        }else if(state == 5){ //estado s5
            terminaisList = Arrays.asList(a49,a53,a54,a57,a58,a59,t1,t2,a2,t3,t4); 
            

        }else if(state == 6){ //estado s6
            if(enableFlagZero){ terminaisList = Arrays.asList(a27,a28,a29,a30,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a50,a51,a52,a44);}
            else{               terminaisList = Arrays.asList(a27,a28,a29,a30,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44);}
            

        }else if(state == 7){ //estado s7
            terminaisList = Arrays.asList(a49,a53,a54,a55,a56,t8,a17,t7,a15,t21,t22);
             

        }else if(state == 8){ //estado s8
            terminaisList = Arrays.asList(a49,t11,t12,t13,t14,a70,a71,a72,a73,a74,a27,a28,a29,a30,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44,a50,a51,a52,t10,t23,t24,t25,t26,t27);


        }else if(state == 9){ //estado s9
            if(enableFlagZero){   terminaisList = Arrays.asList(a27,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a50,a51,a52,a44);} 
            else{                 terminaisList = Arrays.asList(a27,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44); }


        }else if(state == 10){ //estado s10
            terminaisList = Arrays.asList(a49,a53,a54,a55,a56,t8,a17,t7,a15,t21,t22);
        

        }else if(state == 11){ //estado s11
            terminaisList = Arrays.asList(t11,t12,t13,t14,a70,a71,a72,a73,a74,t9,t25,t26,t27);


        }else if(state == 12){ //estado s12
            terminaisList = Arrays.asList(a75,a76,a77,a78,t8,a17,t7,a15,t21,t22,t11,t12,t13,t14,a70,a71,a72,a73,a74,t9,t25,t26,t27);


        }else if(state == 13){ //estado s13
            terminaisList = Arrays.asList(a27,a28,a29,a30,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44,a45,a46,a47,a48,t11,t12,t13,t14,a70,a71,a72,a73,a74,t9,t25,t26,t27);


        }/*else if(state == (inserir numero inteiro do estado )){ 
            terminaisList = Arrays.asList(a1, ..., a78, t1, ..., t27); //inserir a variavel que representa os terminais que voce deseja que seja carregado sequencialmente na GUI.
            // Para isso, verificar a imagem no diretorio do projeto (.../Simulador1-04/extras/mapeamento-dos-terminais.png) 

        }*/else {
            throw new IllegalArgumentException("[DataPath.java]: FSMStates() - ERRO: Estado inválido - state is " + state);
        }

        ativarEstado.get(state).ativar(); //acende o estado "S0" na barra de estados quando state == 0, state == 1 acende o S1, e assim por diante.
        terminais.addAll(terminaisList);
        executaAnimacao(terminais);
        
    }







    //_____________________________________________________________________________________________________________________________________________________________________________________
    //metodo em fase de testes para implementar uma simulação paralela.
    public void FSMStatesParallel(int state) {
        ArrayList<Terminal> terminais = new ArrayList<Terminal>();
        List<Terminal> terminaisList = null;
        ArrayList<Terminal> terminais2 = new ArrayList<Terminal>();
        List<Terminal> terminaisList2 = null;
        ArrayList<Terminal> terminais3 = new ArrayList<Terminal>();
        List<Terminal> terminaisList3 = null;
        ArrayList<Terminal> terminais4 = new ArrayList<Terminal>();
        List<Terminal> terminaisList4 = null;

        //definição
        if(state == 0){ // estado S0
            a1.setDelay(5.0); //metodo usado para se adedquar ao tempo atrasando a propagação nos terminais. Quanto maior o valor mais lento será a carga.
            a2.setDelay(10.0);
            a3.setDelay(10.0);
            t18.setDelay(2.0);
            a24.setDelay(2.0);
            a40.setDelay(7.0);
            a41.setDelay(5.0);
            a42.setDelay(5.0);
            a43.setDelay(5.0);
            a44.setDelay(2.0);
            a45.setDelay(2.0);
            a46.setDelay(2.0);
            a47.setDelay(2.0);
            a48.setDelay(2.0);
            terminaisList = Arrays.asList(t9,t25,t26,t27,a1,a2,a3,a41,a42,a43,t11,t12,t13,t14); //os terminais serão executados na sequencia que foram inseridos em "terminaisList" e "terminaisList2".
            terminaisList2 = Arrays.asList(t1,t2,t5,t6,t19,t20,t17,t18,a31,a32,a33,a34,a24,a40,t15,t16,a44,a45,a46,a47,a48,a70,a71,a72,a73,a74);    
    
        }else if(state == 1){ //estado S1
            infoPath.atualizaInfo(150); //mostra graficamente PC = PC'
            infoPath.atualizaInfo(6); //mostra graficamente RI(output) = mem[PC] 
            simultaneous_operation = 3; // 3 pois irá usar "terminaisList3".

            if(MIPS.UC.instr.equals("Tipo-R")){  // Tipo-R    
                terminaisList = Arrays.asList(t5,t6,a1,a2,a3,t9,t25,t26,t27); 
                terminaisList2 = Arrays.asList(a6,a7,a8,a9,a12,a25); 
                terminaisList3 = Arrays.asList(a10,a11,a14,a26);

            } else if(MIPS.UC.instr.equals("addi") || MIPS.UC.instr.equals("lw") || MIPS.UC.instr.equals("lb")){  // addi, lw e lb
                a1.setDelay(3.0); 
                a2.setDelay(3.0);
                a3.setDelay(3.0);
                terminaisList = Arrays.asList(t5,t6,a1,a2,a3,t9,t25,t26,t27);
                terminaisList2 = Arrays.asList(a6,a7,a9,a13,a25);
                terminaisList3 = Arrays.asList(a10,a11,a18,a19,a20,a21); 

            } else if(MIPS.UC.instr.equals("sw") || MIPS.UC.instr.equals("sb")){  // sw e sb
                terminaisList = Arrays.asList(t5,t6,a1,a2,a3,t9,t25,t26,t27);
                terminaisList2 = Arrays.asList(a6,a7,a9,a12,a25);
                terminaisList3 = Arrays.asList(a10,a11,a26,a18,a19,a20,a21);
                
            } else if(MIPS.UC.instr.equals("beq")){ // beq
                simultaneous_operation = 4; // 4 pois irá usar "terminaisList4".
                a33.setDelay(2.0);
                a34.setDelay(2.0);
                t20.setDelay(2.0);
                t19.setDelay(1.5);
                terminaisList = Arrays.asList(a1,t5,t6,a2,a3,t9,t25,t26,t27);
                terminaisList2 = Arrays.asList(a6,a7,a9,a12,a25,t19,t20,a40,t15,t16,a44);
                terminaisList3 = Arrays.asList(a10,a11,a26,a18,a19,a22,a23);
                terminaisList4 = Arrays.asList(a31,a32,a33,a34,t17,t18,a41,a42,a43);

            } else if(MIPS.UC.instr.equals("j") || MIPS.UC.instr.equals("jal")){ // j e jal
                simultaneous_operation = 4;
                a61.setDelay(2.0);
                a62.setDelay(2.0);
                a64.setDelay(2.0);
                terminaisList = Arrays.asList(a1,t5,t6,a2,a3,t9,t25,t26,t27);
                terminaisList2 = Arrays.asList(a6,a7,a8,a9);
                terminaisList3 = Arrays.asList(a10,a60,a61,a62,a63,a64,a65);
                terminaisList4 = Arrays.asList(a31,a32,a66,a67,a68,a69);

            } else if(MIPS.UC.instr.equals("jr")){ // jr
                terminaisList = Arrays.asList(t5,t6,a1,a2,a3,t9,t25,t26,t27); 
                terminaisList2 = Arrays.asList(a6,a7,a8,a9,a12,a25); 
                terminaisList3 = Arrays.asList(a10,a11,a26);

            } 

        
        }else if(state == 2){ //estado S2
            t20.setDelay(2.0);
            if(MIPS.UC.instr.equals("lw") || MIPS.UC.instr.equals("lb")){ //lw e lb
                terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44);
                if(enableFlagZero){  
                    terminaisList2 = Arrays.asList(t17,t18,a41,a42,a43,a50,a51,a52);
                }else{                
                    terminaisList2 = Arrays.asList(t17,t18,a41,a42,a43);
                }

            }else if(MIPS.UC.instr.equals("sw") || MIPS.UC.instr.equals("sb")){ //sw e sb
                simultaneous_operation = 3; // 3 pois irá usar "terminaisList3".
                terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44);
                terminaisList3 = Arrays.asList(a28,a29,a35,a36,a37,a38,a39);
                if(enableFlagZero){  
                    terminaisList2 = Arrays.asList(t17,t18,a41,a42,a43,a50,a51,a52);
                }else{                
                    terminaisList2 = Arrays.asList(t17,t18,a41,a42,a43);
                }
            }
            
        
        }else if(state == 3){ //estado s3
            t1.setDelay(2.0);
            t2.setDelay(2.0);
            terminaisList = Arrays.asList(a49,a53,a54,a57,a58,a59,a2,a3,a4,a5);
            terminaisList2 = Arrays.asList(t1,t2);
        
        }else if(state == 4){ //estado s4
            t7.setDelay(2.0);
            terminaisList = Arrays.asList(t7,a15,t21,t22); 
            terminaisList2 = Arrays.asList(a16,t8,a17);
        
        }else if(state == 5){ //estado s5
            t1.setDelay(2.0);
            t2.setDelay(2.0);
            t3.setDelay(2.0);
            t4.setDelay(2.0);
            terminaisList = Arrays.asList(a49,a53,a54,a57,a58,a59,a2); 
            terminaisList2 = Arrays.asList(t1,t2,t3,t4);
        
        }else if(state == 6){ //estado s6
            a40.setDelay(2.0);
            t20.setDelay(2.0);
            if(enableFlagZero){ 
                terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44,a50,a51,a52);
                terminaisList2 = Arrays.asList(a28,a29,a30,t17,t18,a41,a42,a43);
            }else{
                terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44);
                terminaisList2 = Arrays.asList(a28,a29,a30,t17,t18,a41,a42,a43);
            }
            
        
        }else if(state == 7){ //estado s7
            t7.setDelay(3.0);
            a15.setDelay(3.0);
            t8.setDelay(2.0);
            terminaisList = Arrays.asList(a49,a53,a54,a55,a56,a17,t21,t22);
            terminaisList2 = Arrays.asList(t7,a15,t8);
             
        
        }else if(state == 8){ //estado s8
            //terminaisList = Arrays.asList(a49,t11,t12,t13,t14,a70,a71,a72,a73,a74,a27,a28,a29,a30,t19,t20,a40,t17,t18,a41,a42,a43,t15,t16,a44,a50,a51,a52,t10,t23,t24,t25,t26,t27,a1);
            simultaneous_operation = 3;
            t20.setDelay(2.0);
            t10.setDelay(2.0);
            terminaisList = Arrays.asList(t11,t12,t13,t14,a49,a70,a71,a72,a73,a74);
            terminaisList2 = Arrays.asList(a27,t19,t20,a40,t15,t16,a44,a50,a51,a52);
            terminaisList3 = Arrays.asList(a28,a29,a30,t17,t18,a41,a42,a43,t10,t23,t24,t25,t26,t27);

        
        }else if(state == 9){ //estado s9
            t20.setDelay(2.0);
            if(enableFlagZero){   
                terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44,a50,a51,a52); 
                terminaisList2 = Arrays.asList(t17,t18,a41,a42,a43); 
            }else{                 
                terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44); 
                terminaisList2 = Arrays.asList(t17,t18,a41,a42,a43); 
            }

        
        }else if(state == 10){ //estado s10
            t7.setDelay(3.0);
            a15.setDelay(3.0);
            t8.setDelay(2.0);
            terminaisList = Arrays.asList(a49,a53,a54,a55,a56,a17,t21,t22);
            terminaisList2 = Arrays.asList(t7,a15,t8);
        
        
        }else if(state == 11){ //estado s11
            t27.setDelay(2.0);
            t9.setDelay(2.0);
            t25.setDelay(2.0);
            terminaisList = Arrays.asList(t11,t12,t13,t14,a70,a71,a72,a73,a74);
            terminaisList2 = Arrays.asList(t9,t25,t26,t27);

        
        }else if(state == 12){ //estado s12
            simultaneous_operation = 3;
            t7.setDelay(2.0);
            a15.setDelay(2.0);
            t9.setDelay(2.0);
            t25.setDelay(2.0);
            terminaisList = Arrays.asList(a75,a76,a77,a78,t8,a17,t21,t22);
            terminaisList2 = Arrays.asList(t7,a15,t9,t25,t26,t27);
            terminaisList3 = Arrays.asList(t11,t12,t13,t14,a70,a71,a72,a73,a74);
        
        }else if(state == 13){ //estado s13
            t20.setDelay(2.0);
            a40.setDelay(2.0);
            a48.setDelay(2.0);
            terminaisList = Arrays.asList(a27,t19,t20,a40,t15,t16,a44,a45,a46,a47,a48,t9,t25,t26,t27);
            terminaisList2 = Arrays.asList(a28,a29,a30,t17,t18,a41,a42,a43,t11,t12,t13,t14,a70,a71,a72,a73,a74);

        
        }else {
            throw new IllegalArgumentException("[DataPath.java]: FSMStatesParallel() - ERRO: Estado inválido - state is " + state);
        }

        ativarEstado.get(state).ativar(); //acende o estado "S0" na barra de estados quando state == 0, state == 1 acende o S1, e assim por diante.
        terminais.addAll(terminaisList);
        executaAnimacao(terminais);

        if(terminaisList2 != null){
            terminais2.addAll(terminaisList2);
            executaAnimacao(terminais2);
        }

        if(terminaisList3 != null){
            terminais3.addAll(terminaisList3);
            executaAnimacao(terminais3);
        }

        if(terminaisList4 != null){
            terminais4.addAll(terminaisList4);
            executaAnimacao(terminais4);
        }

    }


    //_____________________________________________________________________________________________________________
    //Caminho dos dados para instruções do Tipo-R
    public void tipoR(int cycle){ //add, sub, and, or, slt
        //s0, s1, s6, s7
        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0); //executa o datapath do estado S0
            }else if(cycle == 1){ FSMStatesParallel(1); //executa o datapath do estado S1, que para instruções do tipo-R, apenas decodifica a instrução
            }else if(cycle == 2){ FSMStatesParallel(6); //executa o datapath do estado S6
            }else if(cycle == 3){ FSMStatesParallel(7);} //executa o datapath do estado S7
        }else{
            if(cycle == 0){ FSMStates(0); //executa o datapath do estado S0
            }else if(cycle == 1){ FSMStates(1); //executa o datapath do estado S1, que para instruções do tipo-R, apenas decodifica a instrução
            }else if(cycle == 2){ FSMStates(6); //executa o datapath do estado S6
            }else if(cycle == 3){ FSMStates(7);} //executa o datapath do estado S7
        }
        
    }


    //_____________________________________________________________________________________________________________
    public void addi(int cycle){
        //s0, s1, s9, s10
        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0); 
            }else if(cycle == 1){ FSMStatesParallel(1); // S1
            }else if(cycle == 2){ FSMStatesParallel(9); // S9  
            }else if(cycle == 3){ FSMStatesParallel(10);} // S10
        }else{
            if(cycle == 0){ FSMStates(0); 
            }else if(cycle == 1){ FSMStates(1); // S1
            }else if(cycle == 2){ FSMStates(9); // S9  
            }else if(cycle == 3){ FSMStates(10);} // S10
        }
        
    }


    //_____________________________________________________________________________________________________________
    public void lw(int cycle){
        //s0, s1, s2, s3, s4
        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0); 
            }else if(cycle == 1){  FSMStatesParallel(1); // S1
            }else if(cycle == 2){  FSMStatesParallel(2); // S2
            }else if(cycle == 3){  FSMStatesParallel(3); // S3
            }else if(cycle == 4){  FSMStatesParallel(4);}// S4
        }else{
            if(cycle == 0){ FSMStates(0); 
            }else if(cycle == 1){  FSMStates(1); // S1
            }else if(cycle == 2){  FSMStates(2); // S2
            }else if(cycle == 3){  FSMStates(3); // S3
            }else if(cycle == 4){  FSMStates(4);}// S4
        }
        
    }


    //_____________________________________________________________________________________________________________
    public void sw(int cycle){
        //s0, s1, s2, s5

        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0); //S0 
            }else if(cycle == 1){ FSMStatesParallel(1); // S1
            }else if(cycle == 2){ FSMStatesParallel(2); // S2
            }else if(cycle == 3){ FSMStatesParallel(5);} // S5
        }else{
            if(cycle == 0){ FSMStates(0); //S0 
            }else if(cycle == 1){ FSMStates(1); // S1
            }else if(cycle == 2){ FSMStates(2); // S2
            }else if(cycle == 3){ FSMStates(5);} // S5
        }

    }


    //_______________________________________________________________________________________________________________________________
    public void beq(int cycle){
        //s0, s1, s8
        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0); // S0
            }else if(cycle == 1){ FSMStatesParallel(1); // S1
            }else if(cycle == 2){ FSMStatesParallel(8);} // S8
        }else{
            if(cycle == 0){ FSMStates(0); // S0
            }else if(cycle == 1){ FSMStates(1); // S1
            }else if(cycle == 2){ FSMStates(8);} // S8
        }
            
    }


    //_______________________________________________________________________________________________________________________________
    public void j(int cycle){
        //s0, s1, s11
        if(enable_parallel){
            if(cycle == 0){  FSMStatesParallel(0); // S0
            }else if(cycle == 1){ FSMStatesParallel(1); // S1
            }else if(cycle == 2){  FSMStatesParallel(11);} // S11
        }else{
            if(cycle == 0){  FSMStates(0); // S0
            }else if(cycle == 1){ FSMStates(1); // S1
            }else if(cycle == 2){  FSMStates(11);} // S11
        }
        
    }


    //_______________________________________________________________________________________________________________________________
    public void jal(int cycle){
        //s0, s1, s12, s11
        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0); // S0
            }else if(cycle == 1){ FSMStatesParallel(1);  // S1
            }else if(cycle == 2){ FSMStatesParallel(12);} // S12
        }else{
            if(cycle == 0){ FSMStates(0); // S0
            }else if(cycle == 1){ FSMStates(1);  // S1
            }else if(cycle == 2){ FSMStates(12);} // S12
        }
        
    }


    //_______________________________________________________________________________________________________________________________
    public void jr(int cycle){
        //s0, s1, s13
        if(enable_parallel){
            if(cycle == 0){ FSMStatesParallel(0);  // S0
            }else if(cycle == 1){ FSMStatesParallel(1); // S1
            }else if(cycle == 2){ FSMStatesParallel(13);} // S13 
        }else{
            if(cycle == 0){ FSMStates(0);  // S0
            }else if(cycle == 1){ FSMStates(1); // S1
            }else if(cycle == 2){ FSMStates(13);} // S13  
        }

    }


    //_______________________________________________________________________________________________________________________________
    /*public void nome_nova_instrucao(int cycle){ //implemente um novo metodo que executara o caminho de dados da instrução na GUI. 
        if(cycle == 0){ FSMStates(0);  // FSMStates(0) implica que o estado S0 será executado na GUI.
        }else if(cycle == 1){ FSMStates(1); // FSMStates(1) implica que o estado S1 será executado na GUI.
        }else if(cycle == 2){ FSMStates(13); // FSMStates(13) implica que o estado S13 será executado na GUI.
        }else if(cycle == ...){ FSMStates(...); //insira quantos ciclos for necessário para a execução da instrução.  
        }
        //NOTA: Se for instrução do Tipo-R, o metodo relacionado ao tipo-R será executado por padrão. Portanto, não há necessidade
                de implementar um novo metodo.
    }*/

    public void reset_Terminais_BarraEstados(){
        for(Terminal a : terminaisHabilitados){//desabilita cada um dos terminais ativados.
            a.desenergizar(); 
            a.setEnergizado(false); //volta a cor padrão do terminal energizado.
        }

        for(StateActive a: ativarEstado){ // desabilita os estados da barra de estados.
            a.desativar();
        }
    }


    public static void applyEffectsOnTerminal(){
        for(Terminal a : TerminalList){//aplica efeito nos terminais - pode selecionar qualquer efeito da classe Effects
            Effects.efeitoDropShadow(a.getTerminal()); // aplica efeito no terminal (cinza)
            //Effects.efeitoGlow(a.getEnergia()); // aplica efeito na energia (laranjado por padrão)
        }

    }

    public static void setEnableParallel() { 
        if(enable_parallel){
            enable_parallel = false;
            //Terminal.setDelay(1); //seta para 1 novamente a variavel que controla o tempo de carga nos terminais
        }else{
            enable_parallel = true;
        }
    }

}
