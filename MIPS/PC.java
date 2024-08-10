package MIPS;
public class PC implements ClockListener{
    static int PC;
    static int PC_linha;

    public PC(){
        //Para o MIPS de 8MB de memória (VEJA IMAGEM estrutura-memoria.png), o endereço inicial será:
        // ____________|      end     |  end_decimal
        // end_inicio  |  0x00001FFC  |     8188
        // end_final   |  0x0007FF34  |    524084
        PC = Memory.endereco_inicial_TEXT;
        //PC_linha = Memory.endereco_inicial_TEXT;
        
    }

    public static int getPC(){ return PC;}
    public static void setPC(int Pc) {PC = Pc;}
    public static int getPC_linha(){ return PC_linha;}
    public static void setPC_linha(int Pc_linha) {PC_linha = Pc_linha;}

    @Override
    public void clock() {
        // atualizar o PC com o valor em PC'
        PC = PC_linha;
        
    }

}
