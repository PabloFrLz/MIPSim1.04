package MIPS;
import java.util.LinkedList;
import java.util.Queue;

import GUI.BarraLateral;
import GUI.InfoPath;

public class UC implements ClockListener{
    //por haver sinais com 2 bits, decidi por short ao inves de boolean
    // MUX seletores
    protected String opcode;
    protected String funct;
    protected short MemtoReg;
    protected short RegDst;
    protected short Iord;
    protected short PCSrc; // 2 bits
    protected short ALUSrcB; // 2 bits
    protected short ALUSrcA;
    //register enables
    protected short IRWrite;
    protected short MemWrite;
    protected short PCWrite;
    protected short Branch;
    protected short RegWrite;
    //ULA
    protected short ALUOp; // 2bits
    protected short ALUControl; // 3 bits
    //PC
    protected boolean PCEn;
    //fila de estados
    public Queue<Runnable> fila = new LinkedList<>(); // estrutura de fila porque o primeiro a entrar é o primeiro a sair. Isso servirá para executar os estados da FSM.
    protected boolean flagEnableDecoder = false;
    public static String state_UC; // terá o estado atual da FSM
    public static String instr = ""; // identifica a instrução em execução no momento
    public String instr_tipoR = ""; // identifica a instrução do tipo-R
    //decodificadores internos
    protected Main_controller main_controller;
    protected Alu_decoder alu_decoder;
    protected BarraLateral barraLateral;
    protected InfoPath infoPath;
    public static String cycles;

    public UC(){
        this.main_controller = new Main_controller();
        this.alu_decoder = new Alu_decoder();
        fila.add(() -> S0()); //insere o primeiro estado S0 na fila.
        fila.add(() -> S1()); //insere o segundo estado S1 na fila. 

    }


    public short getMemtoReg() {return MemtoReg;}
    public short getRegDst() {return RegDst;}
    public short getIord() {return Iord;}
    public short getPCSrc() {return PCSrc;}
    public short getALUSrcB() {return ALUSrcB;}
    public short getALUSrcA() {return ALUSrcA;}
    public short getIRWrite() {return IRWrite;}
    public short getMemWrite() {return MemWrite;}
    public short getPCWrite() {return PCWrite;}
    public short getBranch() {return Branch;}
    public short getRegWrite() {return RegWrite;}
    public short getALUControl() {return ALUControl;}
    public boolean getPCEn() {return PCEn;}
    public String getOpcode() {return this.opcode; }
    public String getFunct() {return this.funct; }
    public short getALUOp() {return this.ALUOp; }

    public void setPCEn(boolean PCEn){ this.PCEn = PCEn;}
    public void setPCWrite(int i){ this.PCWrite = (short) i;}
    public void setIRWrite(int i){ this.IRWrite = (short) i;}
    public void setOpcode(String instrucaoBIN){ this.opcode = instrucaoBIN.substring(0, 6);}
    public void setFunct(String instrucaoBIN){ this.funct = instrucaoBIN.substring(26, 32);}
    public void setBarraLateral(BarraLateral bar) {this.barraLateral = bar;}
    public void setInfoPath(InfoPath infoPath){this.infoPath = infoPath;}


    public short desloca_2bits(short SignImm){ 
        //valor máximo de imm permitido é 8191 e -8192. Pois ao multiplicar por 4, é preciso que fique dentro da representação máxima do short em java, que é +32767 e -32768).   
        int signImm_x4 = SignImm * 4;//desloca 2 bits
        if( signImm_x4 <= Short.MAX_VALUE && signImm_x4 >= Short.MIN_VALUE){
            return (short) signImm_x4;
        }else{
            throw new IllegalArgumentException("[UC.java]: desloca_2bits() - ERRO: Valor do campo imediato excedeu os 16 bits ao deslocar <<2. Valores permitidos de imediato estão entre 8191 e -8192 - "+ signImm_x4);
        }
    }

    public String desloca_2bits_Addr(String Addr){
        // 26 bits + 2 bits  =  28 bits  
        return Addr + "00";
    }

    public int concatena_4bits_AddrX2(String AddrX2){
        // 4 bits MSB do PC que são sempre "0000" devido ao endereço do segmento TEXT ir do 0x00001FFA ao 0x0007FF32.
        //  4 bits + 28 bits   =  32 bits
        String temp = "0000" + AddrX2; 
        return Integer.parseUnsignedInt(temp, 2); //retorna inteiro pra agilizar o cálculo que se segue
    }

    
    public void showAllSignals(){ // referente a debug
        System.out.println("[Estado da FSM]: "+this.state_UC);
        System.out.println("\t opcode: "+this.opcode);
        System.out.println("\t funct: "+this.funct);
        System.out.println("\t MemToReg: "+this.MemtoReg);
        System.out.println("\t RegDst: "+this.RegDst);
        System.out.println("\t IorD: "+this.Iord);
        System.out.println("\t PCSrc: "+this.PCSrc);
        System.out.println("\t ALUSrcA: "+this.ALUSrcA);
        System.out.println("\t ALUSrcB: "+this.ALUSrcB);
        System.out.println("\t IRWrite: "+this.IRWrite);
        System.out.println("\t MemWrite: "+this.MemWrite);
        System.out.println("\t PCWrite: "+this.PCWrite);
        System.out.println("\t Branch: "+this.Branch);
        System.out.println("\t RegWrite: "+this.RegWrite);
        System.out.println("\t ALUOp: "+this.ALUOp);
        System.out.println("\t ALUControl: "+this.ALUControl);
        System.out.println("\t PCEn: "+this.PCEn);

    }

    public void resetAllSignals(){ //debug
        this.MemtoReg = 0;
        this.RegDst = 0;
        this.Iord = 0;
        this.PCSrc = 0;
        this.ALUSrcB = 0; 
        this.ALUSrcA = 0;
        this.IRWrite = 0;
        this.MemWrite = 0;
        this.PCWrite = 0;
        this.Branch = 0;
        this.RegWrite = 0;
        this.ALUOp = 0;
        this.ALUControl = 0; 
        this.PCEn = false;
        this.state_UC = "S0";
        //this.flagOpcode = false;
    }

    public void resetaSinais(){ //debug
        this.MemWrite = 0;
        this.RegWrite = 0;
        this.Branch = 0;
        //atualizando manualmente os sinais para deixar a UC coerente no decorrer das instruções
        infoPath.atualizaInfo(103);
        infoPath.atualizaInfo(121);
        infoPath.atualizaInfo(110);
        infoPath.atualizaInfo(109);

    }

    
    // __________________________________ (estados da FSM) __________________________________________
    public void S0(){
        //estado s0
        resetaSinais(); //reseta os sinais de escrita e outros relevantes
        this.Iord = 0;
        this.ALUSrcA = 0;
        this.ALUSrcB = 1;
        this.ALUOp = 0; 
        this.PCSrc = 0;
        this.IRWrite = 1;
        this.PCWrite = 1;
        this.state_UC = "S0";
    }
    
    public void S1(){
        //estado s1
        resetaSinais();
        this.ALUSrcA = 0;
        this.ALUSrcB = 3;
        this.ALUOp = 0;
        this.state_UC = "S1";
    }

    public void S2(){
        //estado s2
        resetaSinais();
        this.ALUSrcA = 1;
        this.ALUSrcB = 2;
        this.ALUOp = 0;
        this.state_UC = "S2";
    }

    public void S3(){
        //estado s3
        resetaSinais();
        this.Iord = 1;
        this.IRWrite = 0; //configuração adicional que não compromete o caminho de dados para a instrução lw e nem outra.
        this.state_UC = "S3";
    }

    public void S4(){
        //estado s4
        resetaSinais();
        this.RegDst = 0;
        this.MemtoReg = 1;
        this.RegWrite = 1;
        this.state_UC = "S4";
    }

    public void S5(){
        //estado s5
        resetaSinais();
        this.Iord = 1;
        this.MemWrite = 1;
        this.state_UC = "S5";
    }

    public void S6(){
        //estado S6
        resetaSinais();
        this.ALUSrcA = 1;
        this.ALUSrcB = 0;
        this.ALUOp = 2;
        this.state_UC = "S6";
    }

    public void S7(){
        //estado s7
        resetaSinais();
        this.RegDst = 1;
        this.MemtoReg = 0;
        this.RegWrite = 1;
        this.state_UC = "S7";
    }

    public void S8(){
        //estado s8
        resetaSinais();
        this.ALUSrcA = 1;
        this.ALUSrcB = 0;
        this.ALUOp = 1;
        this.PCSrc = 1;
        this.Branch = 1;
        this.state_UC = "S8";
    }

    public void S9(){
        //estado s9
        resetaSinais();
        this.ALUSrcA = 1;
        this.ALUSrcB = 2;
        this.ALUOp = 0;
        this.state_UC = "S9";
    }

    public void S10(){
        //estado s10
        resetaSinais();
        this.RegDst = 0;
        this.MemtoReg = 0;
        this.RegWrite = 1;
        this.state_UC = "S10";
    }

    public void S11(){
        //estado s11
        resetaSinais();
        this.PCSrc = 2;
        this.PCWrite = 1;
        this.state_UC = "S11";
    }

    public void S12(){
        //estado s12
        resetaSinais();
        this.MemtoReg = 2;
        this.PCSrc = 2;
        this.RegWrite = 1;
        this.PCWrite = 1;
        this.state_UC = "S12";
    }

    public void S13(){
        //estado s13
        resetaSinais();
        this.ALUSrcA = 1;
        this.ALUSrcB = 0;
        this.ALUOp = 2;
        this.PCSrc = 0;
        this.PCWrite = 1;
        this.state_UC = "S13";
    }


    // se julgar necessário, insira um novo estado da FSM. Mas lembre-se que a inserção de um novo estado
    // envolverá uma adaptação mais profunda no simulador, tendo que editar as imagens que representam os 
    // estados, e tendo que gerar codigo para mostrar graficamente o fundo verde de ativação.
    /*public void s14(){} */ 

    @Override
    public void clock() {
        
    }

    // MAIN_CONTROLLER
    public class Main_controller{

        public Main_controller(){}

        public void decode_opcode(){
            if(opcode.equals("000000")){ //tipo-R
                if(funct.equals("001000")){ //jr
                    fila.add(() -> S13()); //estado proposto para 'jr'
                    instr = "jr";
                    cycles = "3 cycles";
                }else{
                    fila.add(() -> S6());
                    fila.add(() -> S7());
                    instr = "Tipo-R";
                    cycles = "4 cycles";

                }
                fila.add(() -> S0());
                fila.add(() -> S1());
                
                barraLateral.setTypeInstr("Type-R");

            } else if(opcode.equals("100011")){ //lw
                fila.add(() -> S2());
                fila.add(() -> S3());
                fila.add(() -> S4());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "lw";
                barraLateral.setTypeInstr("Type-I");
                cycles = "5 cycles";

            } else if(opcode.equals("101011")){ //sw
                fila.add(() -> S2());
                fila.add(() -> S5());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "sw";
                barraLateral.setTypeInstr("Type-I");
                cycles = "4 cycles";
                
                
            } else if(opcode.equals("000100")){ //beq
                fila.add(() -> S8());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "beq";
                barraLateral.setTypeInstr("Type-I");
                cycles = "3 cycles";
                
            } else if(opcode.equals("001000")){ //addi
                fila.add(() -> S9());
                fila.add(() -> S10());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "addi";
                barraLateral.setTypeInstr("Type-I");
                cycles = "4 cycles";
                
            } else if(opcode.equals("000010")){ //j
                fila.add(() -> S11());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "j";
                barraLateral.setTypeInstr("Type-J");
                cycles = "3 cycles";
                
            } else if(opcode.equals("000011")){ //jal
                fila.add(() -> S12());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "jal";
                barraLateral.setTypeInstr("Type-J"); 
                cycles = "3 cycles";

            } else if(opcode.equals("100000")){ //lb
                fila.add(() -> S2());
                fila.add(() -> S3());
                fila.add(() -> S4());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "lb";
                barraLateral.setTypeInstr("Type-I");
                cycles = "5 cycles";

            } else if(opcode.equals("101000")){ //sb
                fila.add(() -> S2());
                fila.add(() -> S5());
                fila.add(() -> S0());
                fila.add(() -> S1());
                instr = "sb";
                barraLateral.setTypeInstr("Type-I");
                cycles = "4 cycles";
                
            }/*else if(opcode.equals("insira o opcode da nova instrução")){ // ..;
                fila.add(() -> Sn()); // substitua Sn() por um estado válido e, se necessário, insira os demais n estados da instrução. 
                //...
                //fila.add(() -> S0()); // apenas descomente e mantenha como está
                //fila.add(() -> S1()); // apenas descomente e mantenha como está
                instr = "insira o nome da instrução"; //exemplo "jr", "addi", "sub".
                barraLateral.setTypeInstr("Type-I"); // insira o tipo da instrução: I, R ou J.
                
                NOTA: Se for instrução do Tipo-R, desconsidere este bloco e modifique apenas o campo de interesse em Config_ALUControl()
            }*/

            flagEnableDecoder = false;
        }
 
    }


    // ALU_DECODER
    public class Alu_decoder{
    
        public Alu_decoder(){}
    
        public int Config_ALUControl() {
            if (ALUOp == 0){
                ALUControl = 2; //add
                instr_tipoR = "add";
    
            }else if (ALUOp == 1){ 
                ALUControl = 6; //sub
                instr_tipoR = "sub";
    
            }else if (ALUOp == 2 && funct.equals("100000")){ 
                ALUControl = 2; //add
                instr_tipoR = "add";

            }else if (ALUOp == 2 && funct.equals("100010")){ 
                ALUControl = 6; //sub
                instr_tipoR = "sub";
    
            }else if (ALUOp == 2 && funct.equals("100100")){ 
                ALUControl = 0; //and
                instr_tipoR = "and";
    
            }else if (ALUOp == 2 && funct.equals("100101")){ 
                ALUControl = 1; //or
                instr_tipoR = "or";
    
            }else if (ALUOp == 2 && funct.equals("100110")){ 
                ALUControl = 4; //xor
                instr_tipoR = "xor";
    
            }else if (ALUOp == 2 && funct.equals("101010")){ 
                ALUControl = 7; //slt
                instr_tipoR = "slt";

            }/*else if (ALUOp == (insira o valor de ALUOp desta instrução no formato inteiro) && funct.equals("insira o funct da instrução")){ 
                ALUControl = 2; // insira o codigo de controle da ULA (ALUControl) para esta instrução.
                instr_tipoR = "jr"; // insira o nome da instrução do tipo-R.
            }*/


            return ALUControl;
        }
    
    }

}