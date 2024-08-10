package MIPS;

import GUI.DataPath;

public class ULA {
    int SrcA;
    int SrcB;
    int ALU_Result;
    short flagZero;
    UC unit_control;
    Memory memoria;
    String[] info_ULA; // referente a debug
    public String signalOperation;

    public ULA(UC unit_control){
        this.unit_control = unit_control;
        this.info_ULA = new String[2];
        signalOperation = "?";
    }
    
    public void setSrcA(int SrcA) { this.SrcA = SrcA; }
    public void setSrcB(int SrcB) { this.SrcB = SrcB; }
    public void setFlagZero(short i) {this.flagZero = i;}
    public int getSrcA(){ return this.SrcA;}
    public int getSrcB(){ return this.SrcB;}
    public int getALU_Result() { return this.ALU_Result; }
    public short getFlagZero() { return this.flagZero; }

    public void ALU_Control_Decoder(){ // função que ao ser chamada realiza a operação considerando o valor em ALU_Control
        int ALU_Control = this.unit_control.alu_decoder.Config_ALUControl(); //obtendo o ALU_Control no Alu_decoder.
        if(ALU_Control == 2){ // add = 010
            add();
            info_ULA[0] = "add"; 
            signalOperation = "+";
        }else if(ALU_Control == 6){ // sub = 110
            sub();
            info_ULA[0] = "sub"; 
            signalOperation = "-";
        }else if(ALU_Control == 0){
            and();
            info_ULA[0] = "and"; 
            signalOperation = "&";
        }else if(ALU_Control == 1){
            or();
            info_ULA[0] = "or"; 
            signalOperation = "|";

        }else if(ALU_Control == 7){
            slt();
            info_ULA[0] = "slt"; 
            signalOperation = "<";
        }else if(ALU_Control == 4){
            xor();
            info_ULA[0] = "xor";
            signalOperation = "^";
        }/*else if(ALU_Control == (valor inteiro do ALU_Control para a nova operação da ULA)){
            xor(); //nome do metodo que realiza a operação na ULA.
            info_ULA[0] = "xor"; //nome da instrução.
            signalOperation = "^"; // sinal de operação.
        } */

        info_ULA[1] = Integer.toBinaryString(ALU_Control);
    }




    public void add(){ 
        if(this.SrcB == 4){ // para quando PC + 4. 
            if (this.SrcA > Memory.endereco_final_TEXT){ //endereco_final_text contem o endereço da ultima instrução (0x0007FF32) do segmento text.
                System.out.println("[Mips.java]: PCPlus4() - ERRO: Excedeu a faixa de endereços disponiveis.");
            }
        }
        this.ALU_Result = this.SrcA + this.SrcB;
        verificaZero();
    }
    public void sub(){ 
        this.ALU_Result = this.SrcA - this.SrcB;
        verificaZero();
    }

    public void and(){
        this.ALU_Result = this.SrcA & this.SrcB;
        verificaZero();
    }

    public void or(){
        this.ALU_Result = this.SrcA | this.SrcB;
        verificaZero();
    }

    public void xor(){
        this.ALU_Result = this.SrcA ^ this.SrcB;
        verificaZero();
    }

    public void slt(){
        // "slt rd, rs, rt"
        // Se o valor em rs é menor que o valor em rt, rd é definido como 1.
        // Caso contrário, rd é definido como 0.
        if(this.SrcA < this.SrcB){
            this.ALU_Result = 1;
            this.flagZero = 0;
        }else{
            this.ALU_Result = 0;
            this.flagZero = 1; 
        }
        
    }
    
    //implemente o metodo da nova operação da ULA
    /*public void nova_operacao(){
        this.ALU_Result = this.SrcA + this.SrcB; //substitua o sinal "+" pelo novo sinal da operação.
        verificaZero(); // manter como está.
    }*/

    public void verificaZero(){ //ativa flagZero
        if(this.ALU_Result == 0){
            this.flagZero = 1; 
            DataPath.enableFlagZero = true;
        } else {
            this.flagZero = 0;
        }
    }

    public String[] getInfo_ULA(){
        return this.info_ULA;
    }

    public String getSignalOperation() {return this.signalOperation;}

}
