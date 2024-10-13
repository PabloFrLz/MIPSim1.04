package MIPS;
//import GUI.*;

import java.util.ArrayList;
import java.util.Collections;

import GUI.BarraLateral;
import GUI.DataPath;

public class Mips{
    public UC uc;
    public RegisterFile registerFile;
    public Memory memoria;
    public ULA ula;
    public PC pc;

    //Registradores não-arquiteturais
    public NARegistrador A_B;
    public NARegistrador Instr;
    public NARegistrador Data;
    public NARegistrador ALUOut;
    //demais sinais do MIPS
    String instrucaoBIN;
    public static short SignImm;
    short SignImm_x4;
    public int PCJump;
    public String Addr;
    public String AddrX2;
    public boolean AND_out;
    public static boolean OR_out;
    //clock
    public Clock clock; 
    //atributos necessarios para a classe InfoPath
    public int MUX_IorD_0=0;
    public int MUX_IorD_1=0;
    public int MUX_RegDst_0=0;
    public int MUX_RegDst_1=0;
    public int MUX_MemtoReg_0=0;
    public int MUX_MemtoReg_1=0;
    public int MUX_MemtoReg_2=0;
    public int MUX_ALUSrcA_0=0;
    public int MUX_ALUSrcA_1=0;
    public int MUX_ALUSrcB_0=0;
    public int MUX_ALUSrcB_1=0;
    public int MUX_ALUSrcB_2=0;
    public int MUX_ALUSrcB_3=0;
    public int MUX_PCSrc_0=0;
    public int MUX_PCSrc_1=0;
    public int MUX_PCSrc_2=0;
    //atributos adicionais 
    public int ciclos_clock = 0;

    public Mips(){
        
        this.uc = new UC();//inicializa a unidade de controle e seus decodificadores internos
        this.registerFile = new RegisterFile(new ArrayList<Integer>(Collections.nCopies(32, 0)), this.uc); //inicializa um arraylist com 32 posicoes com o valor 0 em cada uma.
        this.memoria = new Memory(this.uc, this.registerFile);
        this.ula = new ULA(this.uc);
        this.pc = new PC();

        this.A_B = new NARegistrador();
        this.Instr = new NARegistrador();
        this.Data = new NARegistrador();
        this.ALUOut = new NARegistrador();

        this.clock = new Clock();

        //demais sinais usados no MIPS
        this.instrucaoBIN = "";
        this.SignImm = 0;
        this.SignImm_x4 = 0;
        this.PCJump = 0;
        this.Addr = "";
        this.AddrX2 = "";
        this.AND_out = false;
        this.OR_out = false;

    }

    

    public void execute(Mips mips){
        

        // ||| ****************************************** ( MIPS ) ********************************************** |||


        AND_out = (mips.uc.getBranch()==1 && mips.ula.getFlagZero()==1);
        OR_out = (AND_out || mips.uc.getPCWrite()==1);
        //PC
        mips.uc.setPCEn(OR_out); //sinal PCEn da UC que habilita o program counter
        if(mips.uc.getPCEn()){  //PCEn
            if( !(mips.uc.state_UC.equals("S12") || mips.uc.state_UC.equals("S13"))){
                if( !mips.uc.state_UC.equals("S0")){ //não atualiza o PC quando estiver no estado S0, pois quando estiver neste estado, já haverá um endereço sendo lido no RI.
                    mips.pc.clock(); // PC = PC'.
                }
            }

            if(mips.uc.state_UC.equals("S1")){
                mips.uc.setIRWrite(0); //desabilita IRWrite pra evitar que escreva a instrução de PC+4 no lugar da instrução PC atual. 
                mips.uc.setPCWrite(0); //desabilita PCWrite pra manter a instrução de PC+4 no RI e evitar que seja sobrescrita com lixo do ALUResult.
                OR_out = (AND_out || mips.uc.getPCWrite()==1); //atualiza a saída OR_out novamente para que não dê problemas na execução.
            }
            mips.uc.setPCEn(false);
            DataPath.enableFlagZero = false; //não executa o datapath da flag zero na interface
            //mips.uc.setPCWrite((short) 0);
        }

        MUX_IorD(mips);

        //[NOTA]: A etapa de verificar MemWrite para escrita na memoria de dados está no metodo clock() da class Memory, que é executado ao usar "mips.clock.clock();"

        // Memoria I/D 
        mips.memoria.clock(); //clock implicito isolado pra atualizar a saída da memoria com a instrução ou efetuar escrita na Memoria de dados.
        //NA Registrador Instr e Data
        mips.Instr.setPortaA(mips.memoria.getRD()); //obtem a instrução
        mips.Data.setPortaA(mips.memoria.getRD());

        if(mips.uc.getIRWrite() == 1){ //IRWrite
            mips.Instr.clock(); // clock apenas para o Registrador não-arquitetural Instr.
        }

        mips.instrucaoBIN = mips.memoria.convertDecimalInstructionToBin(mips.Instr.getSaidaA()); //obtem a instrução em binario do registrador NA: Instr

        //Unidade de Controle 
        mips.uc.setOpcode(mips.instrucaoBIN); //extrai opcode de 6 bits
        mips.uc.setFunct(mips.instrucaoBIN); //extrai funct de 6 bits 
        if(mips.uc.flagEnableDecoder){ //evita que ocorra a decodificação e identificação da instrução enquanto uma instrução está sendo executada.
            mips.uc.main_controller.decode_opcode(); // decode do main_controller da UC
            BarraLateral.setCycles(UC.cycles);//mostra na barra lateral direita a instrução atual em binário
        }

        // neste ponto a instrução já deve ser identificada.

        MUX_RegDst(mips);

        if(mips.uc.instr.equals("jal")){ //apos o mux RegDst pois se não há sobrescrita.
            mips.registerFile.setA3(31); //registrador $ra na porta de endereçamento A3

        }

        if( !(mips.uc.getOpcode().equals("000000") || mips.uc.getOpcode().equals("000010"))  ){ // não obtem o imediato extendido quando for instruções do Tipo-R e a instrução "j Addr".
            //extraindo imm extendido (Sign Extend)
            mips.SignImm = (short) Integer.parseUnsignedInt(mips.instrucaoBIN.substring(16, 32), 2); //o sinal já é extendido dentro da memoria na função extraiImm(), então só preciso extrair o imm
            
            // quando for as instruções ADDI, LW, SW, LB, SB não pode deslocar por <<2, caso contrário, ao inserir uma instrução como "addi $t0, $t1, 32767" dará um erro já que o imediato máximo para esses casos tem que estar entre 8191 e -8192.
            if(mips.uc.getOpcode().equals("000100")){ // então permitir apenas quando for instrução BEQ. A instrução J também usa deslocamento, só que está usa outro metodo chamado "desloca_2bits_Addr()" que é chamado mais abaixo no codigo.
                // Caso insira novas instruções que use deslocamento (<<2) no imediato como a BEQ, favor inserir na condição acima.
                // deslocando 2 bits à esquerda:
                mips.SignImm_x4 = mips.uc.desloca_2bits(mips.SignImm);
                //
            }
        }
        
        //Register File
        mips.registerFile.setA1(Integer.parseInt(mips.registerFile.getRegistrador(mips.instrucaoBIN, "rs"), 2)); // rs -> A1 (RegisterFile)
        mips.registerFile.setA2(Integer.parseInt(mips.registerFile.getRegistrador(mips.instrucaoBIN, "rt"), 2)); // rt -> A2 (RegisterFile)
        
        
        mips.registerFile.clock(); //clock implicito isolado pra atualizar a saída do register file com os valores lidos dos registradores.
        //NA Registrador A_B
        mips.A_B.setPortaA(mips.registerFile.getRD1()); // RD1 -> A
        mips.A_B.setPortaB(mips.registerFile.getRD2()); // RD2 -> B
        

        //escrita memoria
        mips.memoria.setWD(mips.A_B.getSaidaB()); //B -> WD 
        
        MUX_ALUSrcA(mips);

        MUX_ALUSrcB(mips);

        //ULA
        mips.ula.ALU_Control_Decoder(); //metodo que define a operação com base no sinal ALU_Control e a executa dentro da ULA.
        
        //NA Registrador ALUOut
        mips.ALUOut.setPortaA(mips.ula.getALU_Result());

        MUX_MemToReg(mips);
        mips.registerFile.clock(); //clock implicito isolado pra efetuar escrita no registrador - a verificação de 'RegWrite' ocorre dentro do metodo clock()

        //Addr (26bits)
        Addr = mips.instrucaoBIN.substring(6, 32);
        AddrX2 = mips.uc.desloca_2bits_Addr(Addr);
        //PCJump
        mips.PCJump = mips.uc.concatena_4bits_AddrX2(AddrX2); 
        // extrai os 26 bits, e desloca 2 bits à esquerda, em seguida concatena com 4 bits MSB do PC atual, que é sempre "0000". totalizando 32 bits.
        
        MUX_PCSrc(mips);

        if(mips.uc.state_UC.equals("S0")){ // condição p/ evitar erros causados pelo estado S8 ao usar a instrução BEQ.
            mips.uc.flagEnableDecoder = true; // habilita a decodificação do opcode e funct na UC (Unidade de controle) 
            MUX_IorD(mips); // chama o multiplexador IorD pela segunda vez pois se nao dá valor incoerente na interface gráfica. chamá-lo duas vezes não altera em nada o fluxo da instrução.
            mips.memoria.clock(); // outro metodo chamado pela segunda vez 
            mips.Instr.setPortaA(mips.memoria.getRD()); // outro metodo chamado pela segunda vez 
        }

        if(mips.uc.state_UC.equals("S8") || mips.uc.state_UC.equals("S11") 
            || mips.uc.state_UC.equals("S12") || mips.uc.state_UC.equals("S13")){ //trecho para atualizar o PC
            // naturalmente o PC é atualizado no inicio desse metodo execute().
            // mas em instruções como BEQ e J, o PC precisa ser atualizado ao fim do estado S8 e S11
            AND_out = (mips.uc.getBranch()==1 && mips.ula.getFlagZero()==1);
            OR_out = (AND_out || mips.uc.getPCWrite()==1);
            if(OR_out){ // Atualiza o PC se PCEn for true.
                mips.pc.clock(); // PC = PC'.
            }
        }

            
        logs(mips); // p/ debug - remover ou comentar quando o software estiver finalizado.
    }
       











    public void nextState(Mips mips){
        //logs(mips);
        Runnable estado = mips.uc.fila.poll(); 
        if(estado != null){
            estado.run(); //executa o estado da FSM, habilitando os sinais daquele estado, e o remove da fila.
        }
        mips.clock.clock(); //dá o clock em todos os módulos (com exceção de PC e Instr - estes são verificados manualmente em trecho adequado quando o sinal de enable estiver habilitado).
          
    }

    public static void logs(Mips mips){    
        System.out.println("[PC]: "+Integer.toHexString(mips.pc.getPC()));
        System.out.println("[PC']: "+Integer.toHexString(mips.pc.getPC_linha()));
        System.out.println("[Instrução]: "+mips.uc.instr);
        System.out.println("[Tipo-R]: "+mips.uc.instr_tipoR);
        System.out.println("[Operacao_ULA]: ("+mips.ula.getInfo_ULA()[0]+") '"+mips.ula.getInfo_ULA()[1]+"'");
        System.out.println("[ULA]: (SrcA: "+mips.ula.getSrcA()+") (SrcB: "+mips.ula.getSrcB()+") (ALUResult: "+mips.ula.getALU_Result()+") (FlagZero: "+mips.ula.getFlagZero()+")");
        System.out.println("[InstrucaoBIN]: "+mips.instrucaoBIN);
        System.out.println("[SignImm]: "+mips.SignImm);
        System.out.println("[SignImm_x4]: "+mips.SignImm_x4);
        System.out.println("[PCJump]: "+mips.PCJump);
        System.out.println("[flagEnableDecoder]: "+mips.uc.flagEnableDecoder);
        System.out.println("[Endereço da instrução final]: "+Integer.toHexString(mips.memoria.final_instruction));
        System.out.println("[NOP Instruction FLAG]: "+mips.memoria.NOPFlag);
        System.out.println("\n******** (MULTIPLEXADORES)*******");
        System.out.println("[MUX_IorD_0]: "+mips.MUX_IorD_0);
        System.out.println("[MUX_IorD_1]: "+mips.MUX_IorD_1);
        System.out.println("[MUX_RegDst_0]: "+mips.MUX_RegDst_0);
        System.out.println("[MUX_RegDst_1]: "+mips.MUX_RegDst_1);
        System.out.println("[MUX_MemtoReg_0]: "+mips.MUX_MemtoReg_0);
        System.out.println("[MUX_MemtoReg_1]: "+mips.MUX_MemtoReg_1);
        System.out.println("[MUX_MemtoReg_2]: "+mips.MUX_MemtoReg_2);
        System.out.println("[MUX_ALUSrcA_0]: "+mips.MUX_ALUSrcA_0);
        System.out.println("[MUX_ALUSrcA_1]: "+mips.MUX_ALUSrcA_1);
        System.out.println("[MUX_ALUSrcB_0]: "+mips.MUX_ALUSrcB_0);
        System.out.println("[MUX_ALUSrcB_1]: "+mips.MUX_ALUSrcB_1);
        System.out.println("[MUX_ALUSrcB_2]: "+mips.MUX_ALUSrcB_2);
        System.out.println("[MUX_ALUSrcB_3]: "+mips.MUX_ALUSrcB_3);
        System.out.println("[MUX_PCSrc_0]: "+mips.MUX_PCSrc_0);
        System.out.println("[MUX_PCSrc_1]: "+mips.MUX_PCSrc_1);
        System.out.println("[MUX_PCSrc_2]: "+mips.MUX_PCSrc_2);
        System.out.println("\n******** (REGISTROS NÃO-ARQUITETURAIS)*******");
        System.out.println("[NAREG_Instr_IN]: "+mips.Instr.getPortaA());
        System.out.println("[NAREG_Instr_OUT]: "+mips.Instr.getSaidaA());
        System.out.println("[NAREG_Data_IN]: "+mips.Data.getPortaA());
        System.out.println("[NAREG_Data_OUT]: "+mips.Data.getSaidaA());
        System.out.println("[NAREG_A_IN]: "+mips.A_B.getPortaA());
        System.out.println("[NAREG_A_OUT]: "+mips.A_B.getSaidaA());
        System.out.println("[NAREG_B_IN]: "+mips.A_B.getPortaB());
        System.out.println("[NAREG_B_OUT]: "+mips.A_B.getSaidaB());
        System.out.println("[NAREG_ALUOut_IN]: "+mips.ALUOut.getPortaA());
        System.out.println("[NAREG_ALUOut_OUT]: "+mips.ALUOut.getSaidaA());
        System.out.println("\n******** (INSTR/DATA MEMORY)*******");
        System.out.println("[A]: "+mips.memoria.getA());
        System.out.println("[RD]: "+mips.memoria.getRD());
        System.out.println("[WD]: "+mips.memoria.getWD());
        System.out.println("\n******** (REGISTER FILE)*******");
        System.out.println("[A1]: "+mips.registerFile.getA1());
        System.out.println("[RD1]: "+mips.registerFile.getRD1());
        System.out.println("[A2]: "+mips.registerFile.getA2());
        System.out.println("[RD2]: "+mips.registerFile.getRD2());
        System.out.println("[A3]: "+mips.registerFile.getA3());
        System.out.println("[WD3]: "+mips.registerFile.getWD3());

        mips.uc.showAllSignals();
        mips.registerFile.showAllRegisters();
        mips.memoria.showAddress("Dynamic", 64); // mostra apenas os 64 primeiros bytes na memoria de dados
        //mips.memoria.showAddress("Text", 52); // mostra apenas os 52 primeiros bytes na memoria de Instruções
    }

    public static void MUX_IorD(Mips mips){
        mips.MUX_IorD_0 = mips.pc.getPC();
        mips.MUX_IorD_1 = mips.ALUOut.getSaidaA();

        if(mips.uc.getIord() == 0){
            mips.memoria.setA(mips.MUX_IorD_0); // porta de endereçamento A da memoria I/D recebe o valor de PC 
            
        } else {
            mips.memoria.setA(mips.MUX_IorD_1);
            
        } 
    }

    public static void MUX_RegDst(Mips mips){
        mips.MUX_RegDst_1 = Integer.parseInt(mips.registerFile.getRegistrador(mips.instrucaoBIN, "rd"), 2);
        mips.MUX_RegDst_0 = Integer.parseInt(mips.registerFile.getRegistrador(mips.instrucaoBIN, "rt"), 2);

        if(mips.uc.getRegDst() == 1){ //definindo porta A3 do Register File 
            mips.registerFile.setA3(mips.MUX_RegDst_1);
        }else{ 
            mips.registerFile.setA3(mips.MUX_RegDst_0);
        }
    }

    public static void MUX_ALUSrcA(Mips mips){
        mips.MUX_ALUSrcA_1 = mips.A_B.getSaidaA();
        mips.MUX_ALUSrcA_0 = mips.pc.getPC();

        if(mips.uc.getALUSrcA() == 1){
            mips.ula.setSrcA(mips.MUX_ALUSrcA_1); 
        } else {
            mips.ula.setSrcA(mips.MUX_ALUSrcA_0); //caminho feito pelo estado S0
        }
    }

    public static void MUX_ALUSrcB(Mips mips){
        mips.MUX_ALUSrcB_0 = mips.A_B.getSaidaB();
        mips.MUX_ALUSrcB_1 = 4;
        mips.MUX_ALUSrcB_2 = mips.SignImm;
        mips.MUX_ALUSrcB_3 = mips.SignImm_x4;

        if(mips.uc.getALUSrcB() == 0){
            mips.ula.setSrcB(mips.MUX_ALUSrcB_0);
        } else if(mips.uc.getALUSrcB() == 1){
            mips.ula.setSrcB(mips.MUX_ALUSrcB_1);
        } else if(mips.uc.getALUSrcB() == 2){
            mips.ula.setSrcB(mips.MUX_ALUSrcB_2);
        } else{
            mips.ula.setSrcB(mips.MUX_ALUSrcB_3);
        }
    }

    public static void MUX_MemToReg(Mips mips){
        mips.MUX_MemtoReg_1 = mips.Data.getSaidaA();
        mips.MUX_MemtoReg_0 = mips.ALUOut.getSaidaA();
        mips.MUX_MemtoReg_2 = mips.pc.getPC();

        if(mips.uc.getMemtoReg() == 1){
            mips.registerFile.setWD3(mips.MUX_MemtoReg_1);
        }else if(mips.uc.getMemtoReg() == 2){
            mips.registerFile.setWD3(mips.MUX_MemtoReg_2);
        }else{
            mips.registerFile.setWD3(mips.MUX_MemtoReg_0);
        }
    }

    public static void MUX_PCSrc(Mips mips){
        mips.MUX_PCSrc_0 = mips.ula.getALU_Result();
        mips.MUX_PCSrc_1 = mips.ALUOut.getSaidaA();
        mips.MUX_PCSrc_2 = mips.PCJump;

        if(mips.uc.getPCSrc() == 0){
            mips.pc.setPC_linha(mips.MUX_PCSrc_0);
        } else if(mips.uc.getPCSrc() == 1){
            mips.pc.setPC_linha(mips.MUX_PCSrc_1);
        } else{
            mips.pc.setPC_linha(mips.MUX_PCSrc_2);
        }
    }

    

}
