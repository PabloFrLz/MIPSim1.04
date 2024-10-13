package MIPS;
//import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import GUI.BarraLateral;
import GUI.Main;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert.AlertType;

public class Memory implements ClockListener{
    public byte[] enderecamento;
    protected int A;
    protected short WE;
    protected int WD;
    protected int RD;
    protected UC unit_control;
    protected RegisterFile registerFile;
    public InstructionMemory instruction_memory;
    protected DataMemory data_memory;
    protected BarraLateral barraLateral;
    protected HashMap<String, String> instructionMap = new HashMap<>();
    // Segmento Dynamic Data
    public static int endereco_inicial_DYNAMIC_DATA; 
    public static int endereco_final_DYNAMIC_DATA;
    // Segmento Text
    public static int endereco_inicial_TEXT; 
    public static int endereco_final_TEXT;
    // Segmento Global Data
    public static int endereco_inicial_GLOBAL_DATA; 
    public static int endereco_final_GLOBAL_DATA;
    // Segmento Reserved 1
    public static int endereco_inicial_RESERVED_1; 
    public static int endereco_final_RESERVED_1;
    // Segmento Reserved 2
    public static int endereco_inicial_RESERVED_2; 
    public static int endereco_final_RESERVED_2;

    public boolean NOPFlag = false;
    public static int final_instruction;
    public static IntegerProperty current_instruction = new SimpleIntegerProperty(0); //variavel que contabiliza quantidade de instruções e dados carregados.

    public static ArrayList<String[]> binary_infos_list;
    
    
    
    
    public Memory(UC unit_control, RegisterFile registerFile){
        this.unit_control = unit_control;
        this.registerFile = registerFile;
        //inicializando as memorias de dados e de instruções.
        this.data_memory = new DataMemory();
        this.instruction_memory = new InstructionMemory();
        //inicializa a memoria do MIPS de 8MB
        this.enderecamento = new byte[(int) Math.pow(2, 23)]; 

        // endereço inicial da seção "TEXT" da estrutura de memoria para um MIPS de 8MB descrita na imagem "estrutura-memoria.png".
        this.endereco_inicial_TEXT = convertHexToDecimal("00001FFC"); 
        this.endereco_final_TEXT = convertHexToDecimal("0007FF34");
        // Dynamic Data
        this.endereco_inicial_DYNAMIC_DATA = convertHexToDecimal("0007FFB4");
        this.endereco_final_DYNAMIC_DATA = convertHexToDecimal("003FFFB0");
        // Global Data
        this.endereco_inicial_GLOBAL_DATA = convertHexToDecimal("0007FF38");
        this.endereco_final_GLOBAL_DATA = convertHexToDecimal("0007FFB0");
        // Reserved 1
        this.endereco_inicial_RESERVED_1 = convertHexToDecimal("00000000");
        this.endereco_final_RESERVED_1 = convertHexToDecimal("00001FF8");
        // Reserved 2
        this.endereco_inicial_RESERVED_2 = convertHexToDecimal("003FFFB4");
        this.endereco_final_RESERVED_2 = convertHexToDecimal("00800000");

        binary_infos_list = new ArrayList<>();

    }
    
    
    
    
    //classe interna (aninhada) que representa a MEMÓRIA DE DADOS.
    public class DataMemory{
        
        public DataMemory(){} 
        
        
    }
    
    
    //classe interna (aninhada) que representa a MEMÓRIA DE INSTRUÇÕES.
    public class InstructionMemory{
        
        public InstructionMemory(){} 
        
        public static String extrai_reg(String inst_asm){ // extrai os registradores da instrução no formato assembly, e.g., "lw $t0, 8($0)". 
            String reg_name = "";
            for(int i=0; i < inst_asm.length(); i++){
                if(inst_asm.charAt(i) == '$'){
                    //só tem um registrador dos 32 registradores que possui um unico caracter após '$', os demais tudo possuem 2 caracteres, portanto:
                    if(inst_asm.charAt(i+1) == '0'){
                        reg_name = "$0";
                    }else{
                        reg_name = "".concat(String.valueOf(inst_asm.charAt(i))).concat(String.valueOf(inst_asm.charAt(i+1))).concat(String.valueOf(inst_asm.charAt(i+2)));
                    }
                    return reg_name;
                }
            }
            throw new IllegalArgumentException("[Memory.java]: extrai_reg() - ERRO: Nao foi identificado registradores.");
        }

        
        public static String extrai_Imm(String inst_asm, int op){ //extrai o campo imm da instrução em assembly
            String imm = "";
            int bits = 0;
            if(op == 1){ // para instruções lw e sw
                String[] partes = inst_asm.split(","); // Divide a string pela metade a partir da vírgula
                imm = partes[1].replaceAll("\\(.*", ""); // Remove números após a ocorrencia de '('.
                imm = imm.replaceAll("\\s", ""); // Substitui todos os espaços em branco
                bits = 16;
            } else if(op == 0){ // para instruções addi e subi
                String[] partes = inst_asm.split(",");
                imm = partes[2].replaceAll("\\s", "");
                bits = 16;
            } else if (op == 2){
                String[] partes = inst_asm.split(" ");
                imm = partes[1].replaceAll("\\s", "");
                bits = 32;
            }
    
            int num = Integer.parseInt(imm);
            if(num < -32768 || num > 32767){ //verifica se o imm está no intervalo permitido para esse campo que é entre -32.768 a 32.767.
                Main.showAlert(AlertType.ERROR, "ERROR", "Invalid values IMM", "IMM Value is not in the range -32768 to 32767."); 
                throw new IllegalArgumentException("[Memory.java]: extrai_Imm() - ERRO: IMM Value is not in the range -32768 to 32767: "+num);
            }
            imm = Integer.toBinaryString(num); //converte para binario já em formato de string
            
            if(num >= 0){ // para numero positivos
                while(imm.length() != bits){ //preenche com zeros mais a esquerda até alcançar 16 bits (ou 32 bits para instruções de jump).
                    imm = "0" + imm; 
                } 
                //não é preciso fazer para quando o numero é negativo, pois a propria função toBinaryString() já te da o numero
                //convertido para complemento de 2 e com 1's preenchidos mais à esquerda até completar 32.
                //por exemplo, para uma entrada "-10", resultaria em "11111111111111111111111111110110".
                //basta apenas reduzir o numero de 32 bits para 16 bits que é o tamanho do campo IMM de instruções TIPO-I
            } else{
                if(bits == 16){ //Tipo-I
                    imm = imm.replaceAll("1111111111111111", ""); //remove os 16 bits mais significativos (MSB) do numero negativo de 32 bits em complemento de 2, restando 16 bits
                }
            }
            return imm;
        }
        
        public String decodifica_instrucao_em_bin(String inst_asm){ //não confundir com o modulo de decodificação que está ou será criado no RegisterFile.java e na UC.java
            //Tipo_R
            String op;
            String rs;
            String rt = "";
            String rd;
            String shamt;
            String funct;
            //Tipo_I
            String imm;
            //Tipo_J
            String Addr;

            String operacao = inst_asm.split(" ")[0]; // extrai os caracteres iniciais até encontrar um espaço em vazio. São os caracteres que identificam a operação.

            

            //tipo-R
            if(operacao.equals("add") || operacao.equals("sub") ||
                operacao.equals("and") || operacao.equals("or") || 
                operacao.equals("slt") || operacao.equals("jr") ||
                operacao.equals("xor") /*|| operacao.equals("insira o nome da instrução")*/){ 

                int index;
                op = "000000"; //opcode
                shamt = "00000";
                funct = "";
                String[] typeR_binary_infos = new String[6];

                if(operacao.equals("add")){
                    funct = "100000"; 
                } else if(operacao.equals("sub")){
                    funct = "100010"; 
                } else if(operacao.equals("and")){
                    funct = "100100"; 
                } else if(operacao.equals("or")){
                    funct = "100101"; 
                } else if(operacao.equals("xor")){
                    funct = "100110"; 
                } else if(operacao.equals("slt")){
                    funct = "101010"; 
                } else if(operacao.equals("jr")){ 
                    funct = "001000"; 
                    rs = extrai_reg(inst_asm); //essa instrução só usa o campo rs, os demais são 0s
                    rs = Memory.this.registerFile.registrador_num(rs);
                    rd = "00000";
                    rt = "00000";

                    
                    typeR_binary_infos[0] = op;
                    typeR_binary_infos[1] = rs;
                    typeR_binary_infos[2] = rt;
                    typeR_binary_infos[3] = rd;
                    typeR_binary_infos[4] = shamt;
                    typeR_binary_infos[5] = funct;
                    binary_infos_list.add(typeR_binary_infos); //adiciona na lista para evitarsobrescrita nos loops seguintes

                    return op+rs+rt+rd+shamt+funct; 
                }/*else if(operacao.equals("insira o nome da instrução")){
                    funct = "000000"; // insira o funct da instrução em binario 
                    //se necessário, faça demais adequações quando a instrução possuir peculiaridades, tal como a instrução jr.
                }*/

                rd = extrai_reg(inst_asm);
                index = inst_asm.indexOf('$');
                if (index != -1) { inst_asm = inst_asm.substring(index + 3);}

                rs = extrai_reg(inst_asm);
                index = inst_asm.indexOf('$');
                if (index != -1) { inst_asm = inst_asm.substring(index + 3);}
 
                rt = extrai_reg(inst_asm);  
                index = inst_asm.indexOf('$');
                if (index != -1) { inst_asm = inst_asm.substring(index + 3);}

                rt = Memory.this.registerFile.registrador_num(rt);
                rd = Memory.this.registerFile.registrador_num(rd);
                rs = Memory.this.registerFile.registrador_num(rs);

                typeR_binary_infos[0] = op; // codigo para mostrar os valores nos campos da instrução na interface grafica
                typeR_binary_infos[1] = rs;
                typeR_binary_infos[2] = rt;
                typeR_binary_infos[3] = rd;
                typeR_binary_infos[4] = shamt;
                typeR_binary_infos[5] = funct;
                binary_infos_list.add(typeR_binary_infos);

                return op+rs+rt+rd+shamt+funct; //campos de uma instrução tipo-R em ordem



            //tipo-I
            }else if(operacao.equals("sw") || operacao.equals("lw") || 
                    operacao.equals("addi") || operacao.equals("subi") ||
                    operacao.equals("beq") || operacao.equals("lb") ||
                    operacao.equals("sb")/*|| operacao.equals("insira o nome da instrução")*/){ 

                op = "";
                imm = "";
                String[] typeI_binary_infos = new String[4];

                if(operacao.equals("sw")){
                    op = "101011";
                    imm = extrai_Imm(inst_asm, 1); // usar op = 1 em instruções no formato "sw $t0, offset($0)"
                } else if(operacao.equals("lw")){
                    op = "100011";
                    imm = extrai_Imm(inst_asm, 1);
                } else if(operacao.equals("addi") || operacao.equals("subi")){
                    op = "001000";
                    imm = extrai_Imm(inst_asm, 0); // usar op = 0 em instruções no formato "addi $t0, $t1, offset"
                } else if(operacao.equals("beq")){
                    op = "000100";
                    imm = extrai_Imm(inst_asm, 0);
                } else if(operacao.equals("lb")){
                    op = "100000";
                    imm = extrai_Imm(inst_asm, 1);
                } else if(operacao.equals("sb")){
                    op = "101000";
                    imm = extrai_Imm(inst_asm, 1);
                }/*else if(operacao.equals("insira o nome da instrução")){
                    op = "000000"; //insira o opcode
                    imm = extrai_Imm(inst_asm, 1); //apenas descomente esta linha e verifique o formato da instrução, caso:
                    "instr $reg, offset($reg2)", o segundo parametro deve ser o valor inteiro 1;
                    "instr $reg, $reg2, offset", o segundo parametro deve ser o valor inteiro 0;
                } */

                rt = extrai_reg(inst_asm);
                int index = inst_asm.indexOf('$');
                if (index != -1) { inst_asm = inst_asm.substring(index + 3);}

                rs = extrai_reg(inst_asm);
                index = inst_asm.indexOf('$');
                if (index != -1) { inst_asm = inst_asm.substring(index + 3);}

                rt = Memory.this.registerFile.registrador_num(rt);
                rs = Memory.this.registerFile.registrador_num(rs);

                typeI_binary_infos[0] = op; // codigo para mostrar os valores nos campos da instrução na interface grafica
                typeI_binary_infos[1] = rs;
                typeI_binary_infos[2] = rt;
                typeI_binary_infos[3] = imm;
                binary_infos_list.add(typeI_binary_infos);

                return op+rs+rt+imm; //campos de uma instrução tipo-I em ordem



            //tipo-J
            }else if(operacao.equals("j") || operacao.equals("jal")
                    /*|| operacao.equals("insira o nome da instrução")*/){ 

                op = "";
                String[] typeJ_binary_infos = new String[6];

                if(operacao.equals("j")){
                    op = "000010";
                } else if(operacao.equals("jal")){
                    op = "000011";
                }/*else if(operacao.equals("insira o nome da instrução")){
                    op = "000000"; //insira o opcode
                }*/

                
                Addr = extrai_Imm(inst_asm, 2); //retorna 32 bits
                //instruções j e jal removem os 4 primeiros bits (MSB), e os 2 ultimos bits (LSB), e.g.:
                // "0000 0000 0100 0000 0000 0000 1010 0000"  = "0x004000A0" (totalizando 32 bits)
                // 0000 "0000 0100 0000 0000 0000 1010 00" 00 = "0x00100028" (totalizando 26 bits)
                Addr = Addr.substring(4);// remove os primeiros 4 bits
                Addr = Addr.substring(0, Addr.length() - 2); //remove os 2 últimos bits

                typeJ_binary_infos[0] = op; // codigo para mostrar os valores nos campos da instrução na interface grafica
                typeJ_binary_infos[1] = Addr;
                binary_infos_list.add(typeJ_binary_infos);

                return op+Addr; //campos de uma instrução tipo-J em ordem

            }else{
                Main.showAlert(AlertType.WARNING, "Warning Dialog", "Instruction not supported", "The '"+operacao+"' instruction is not supported by the simulator.");
                return null; //codigo de erro.
                //throw new IllegalArgumentException("[Memory.java]: decodifica_instrucao_em_bin() - ERRO: O Simulador não dá suporte à instrução inserida ou não há instrução - "+operacao);
            }



        }
        
        public int carrega_instrucoes_na_memoria(String codigo_asm){
            int endereco_inicial = Memory.this.endereco_inicial_TEXT; 
            try (BufferedReader br = new BufferedReader(new FileReader(codigo_asm))) {
                String inst;
                String decode_inst;
                int count_lines=0;
                byte[] Bytes = new byte[4];
                while ((inst = br.readLine()) != null) { //vai lendo cada linha do codigo em assembly MIPS
                    if(endereco_inicial <= Memory.this.endereco_final_TEXT){
                        decode_inst = decodifica_instrucao_em_bin(inst); // decode_inst terá a instrução decodificada em binario, e.g., "00000010000100010100000000100000".
                        if (decode_inst == null) { return 1;} // 1 indica erro.
                        instructionMap.put(decode_inst, inst); //salva em um HashMap para consultar a instrução atual posteriormente em convertDecimalInstructionToBin().
                        Bytes = convertBinToByte(decode_inst);
                        // insere o codigo em assemlby MIPS decodificado na posição 0x00001FFA em diante.
                        Memory.this.enderecamento[endereco_inicial] = Bytes[0];
                        Memory.this.enderecamento[endereco_inicial+1] = Bytes[1];
                        Memory.this.enderecamento[endereco_inicial+2] = Bytes[2];
                        Memory.this.enderecamento[endereco_inicial+3] = Bytes[3];
                        endereco_inicial += 4; //adiciona 4 bytes ao endereço para inserir na proxima palavra de 4 bytes
                        count_lines++; //conta as linhas para inserir no segmento de memoria na interface grafica
                    }
                }

                final_instruction = endereco_inicial_TEXT + (4*count_lines);
                //loaded_instructions = count_lines;
                insertAdressOnBar("Text"); //insere no segmento de memoria na interface grafica
                    
            } catch (IOException e) {
                System.err.println("[Memory.java]: InstructionMemory - carrega_instrucoes_na_memoria() - ERRO: Erro ao ler o arquivo: ");
            }
            return 0;
        }
    }
    
    public String convertDecimalInstructionToBin(int decimal){
        String bin = Integer.toBinaryString(decimal);
        while(bin.length() != 32){
            bin = "0" + bin;
        } // quando é negativo, o metodo toBinaryString() já preenche com 1's à esquerda implicitamente.

        this.barraLateral.setInstruction(instructionMap.get(bin));//mostra na barra lateral direita a instrução atual em assembly

        return bin;
    }
    
    //Metodos da classe externa Memory
    public String convertDecimalToHex(int val){ // converte de decimal para hexadecimal
        String hex = Integer.toHexString(val); 
        while(hex.length() != 8){ //preenche com zeros mais a esquerda até alcançar 8 caracteres hexadecimais (32 bits).
            hex = "0" + hex;
        }
        return hex;
    }
    
    public int convertHexToDecimal(String hexadecimal){ // converte de hexadecimal para decimal
        return Integer.parseUnsignedInt(hexadecimal, 16); 
    }

    public int convertByteToDecimal(byte[] Byte){ // converter 4 bytes em inteiro
        String numero;
        String[] bin = new String[4];
        for(int i = 0; i < 4; i++){
            bin[i] = Integer.toBinaryString(Byte[i]);
            if(bin[i].length() > 8){
                bin[i] = bin[i].replaceAll("111111111111111111111111", ""); //removendo o preenchimentos de 1's até 32 bits quando o numero é negativo.
            }
            while (bin[i].length() != 8 ){ // preenchendo 0's mais à esqueda quando o numero for positivo
                bin[i] = "0" + bin[i];
            }
        }
        numero = bin[0] + bin[1] + bin[2] + bin[3];// criando o numero binario de 32 bits
        int val = Integer.parseUnsignedInt(numero, 2); // converte o binario no formato string para um numero inteiro 
        return val; //retorna o equivalente a uma word de 4 bytes (int)
    }

    public byte[] convertDecimalToByte(int num_decimal){
        String numero;
        numero = Integer.toBinaryString(num_decimal);
        while(numero.length() != 32){ // não precisa pra negativo pois negativo já é convertido com 32 bits e 1's à esquerda
            numero = "0" + numero;
        }
        byte[] Byte = new byte[4]; 
        //pega de 8 em 8 bits da string em binario
        Byte[0] = (byte) Integer.parseInt(numero.substring(0, 8), 2);
        Byte[1] = (byte) Integer.parseInt(numero.substring(8, 16), 2);
        Byte[2] = (byte) Integer.parseInt(numero.substring(16, 24), 2);
        Byte[3] = (byte) Integer.parseInt(numero.substring(24, 32), 2);
        return Byte;
    }

    public String convertBinToHex(String str){ // converte numero binario em formato de String, para numero hexadecimal em formato de String
        int hex = Integer.parseUnsignedInt(str, 2);
        String hexa = Integer.toHexString(hex);
        return hexa.toUpperCase(); //converte para letra maiuscula
    }

    public byte[] convertBinToByte(String bin){
        byte[] Byte = new byte[4]; 
        //pega de 8 em 8 bits da string em binario
        Byte[0] = (byte) Integer.parseInt(bin.substring(0, 8), 2);
        Byte[1] = (byte) Integer.parseInt(bin.substring(8, 16), 2);
        Byte[2] = (byte) Integer.parseInt(bin.substring(16, 24), 2);
        Byte[3] = (byte) Integer.parseInt(bin.substring(24, 32), 2);
        return Byte;
    }
    
    
    
    /*public int getRD_Word(){ // obtem uma palavra na saída RD - pode ser usado para obter a instrução ou dados.
        if(this.unit_control.instr.equals("lw") && this.unit_control.state_UC.equals("S3")){ //verifica se é a instrução lw e se esta no estado S3 - verificação necessaria para evitar uns problemas
            this.A += endereco_inicial_DYNAMIC_DATA; //(SignImm + reg) + endereço de inicio do segmento DYNAMIC DATA 
            if(this.A > endereco_final_DYNAMIC_DATA){
                throw new IllegalArgumentException("[Memory.java]: getRD_Word() - ERRO: Endereço de memória excedeu o endereço limite do segmento DYNAMIC DATA - "+ this.A);
            }
        }else if(this.unit_control.instr.equals("lb") && this.unit_control.state_UC.equals("S3")){
            this.A += endereco_inicial_DYNAMIC_DATA; //(SignImm + reg) + endereço de inicio do segmento DYNAMIC DATA 
            if(this.A > endereco_final_DYNAMIC_DATA){
                throw new IllegalArgumentException("[Memory.java]: getRD_Word() - ERRO: Endereço de memória excedeu o endereço limite do segmento DYNAMIC DATA - "+ this.A);
            }
            
            return Memory.this.enderecamento[Memory.this.A] & 0xFF; //lê o byte
            // operação logica "& 0xFF" adicionada para evitar problemas ao ler valores com bit MSB=1, o java interpretava como negativo, e um valor 'ff' (255) por exemplo, se tornava 'ffffffff' (-1).
        }
        
        //lê a palavra
        byte[] b = new byte[4];
        b[0] = Memory.this.enderecamento[Memory.this.A];
        b[1] = Memory.this.enderecamento[Memory.this.A+1];
        b[2] = Memory.this.enderecamento[Memory.this.A+2];
        b[3] = Memory.this.enderecamento[Memory.this.A+3];
        
        return convertByteToDecimal(b); //retorna uma palavra no formato int
    }*/

    public int getRD_Word() {
        // Verifica se a instrução é "lw" ou "lb" e se está no estado S3
        if (this.unit_control.state_UC.equals("S3") && 
            (this.unit_control.instr.equals("lw") || this.unit_control.instr.equals("lb"))) {
    
            //this.A += endereco_inicial_DYNAMIC_DATA; // Ajusta o endereço com base no início do segmento DYNAMIC DATA (offset + reg_base + endereco_inicial_DYNAMIC_DATA) - descontinuado na versão 1.04
    
            // Verifica se o endereço está dentro dos limites do segmento DYNAMIC DATA
            if (this.A > endereco_final_DYNAMIC_DATA) {
                //throw new IllegalArgumentException("[Memory.java]: getRD_Word() - ERRO: Endereço de memória excedeu o endereço limite do segmento DYNAMIC DATA - " + this.A);
                Main.showAlert(AlertType.WARNING, "Warning Dialog", "Memory address exceeded", "Memory address exceeded the address limit for the dynamic data segment. Reserved segments have not been implemented graphically and the values ​​will not be visible.");
                System.out.println("[Memory.java]: getRD_Word() - WARNING: Endereço de memória excedeu o endereço limite do segmento DYNAMIC DATA - " + this.A);
            }else if(this.A < endereco_inicial_TEXT){
                Main.showAlert(AlertType.WARNING, "Warning Dialog", "Memory address exceeded", "Memory address is less than the starting address of the text segment. Reserved segments have not been implemented graphically and the values ​​will not be visible.");
                System.out.println("[Memory.java]: getRD_Word() - WARNING: Endereço de memória é inferior ao endereço inicial do segmento TEXT - " + this.A);
            } 
            // Normalmente as instruções LW, LB, SW, SB podem acessar todos os segmentos de memoria, inclusive os reservados (RESERVED segment).
            // Como os segmentos RESERVED não foram implementados graficamente nesse trabalho, apenas internamente, seria inadequado permitir escrita
            // em um endereço que não seria mostrado na interface gráfica para consulta. Portanto, as areas permitidas para o software na versão 1.04
            // será as areas entre o endereço do byte inicial do Segmento de TEXTO e o endereço do byte final do segmento DYNAMIC DATA. 
    
            // Se a instrução for "lb", retorna o byte lido
            if (this.unit_control.instr.equals("lb")) {
                return Memory.this.enderecamento[Memory.this.A] & 0xFF;
            }
        }
    
        // Para instruções "lw" e outras, lê a palavra (4 bytes)
        byte[] b = new byte[4];
        b[0] = Memory.this.enderecamento[Memory.this.A];
        b[1] = Memory.this.enderecamento[Memory.this.A + 1];
        b[2] = Memory.this.enderecamento[Memory.this.A + 2];
        b[3] = Memory.this.enderecamento[Memory.this.A + 3];
    
        return convertByteToDecimal(b); // Retorna a palavra no formato int
    }
    
    
    
    public void WriteData(){ //escreve na memoria de dados
        byte[] b = new byte[4];
        b = convertDecimalToByte(Memory.this.WD); //envia o dado WD para ser convertido para byte.

        if(this.unit_control.instr.equals("sb")){ // sb
            Memory.this.enderecamento[Memory.this.A] = b[3]; //salva o byte - o byte sempre vai estar mais à direita na palavra, que no caso é o b[3]
            //int pos_byte = Memory.this.A % 4; // obtem a posição do byte na palavra

        } else{ // sw
            //salva os 4 bytes da palavra
            Memory.this.enderecamento[Memory.this.A] = b[0];
            Memory.this.enderecamento[Memory.this.A+1] = b[1];
            Memory.this.enderecamento[Memory.this.A+2] = b[2];
            Memory.this.enderecamento[Memory.this.A+3] = b[3];
        }
        
        //atualiza os segmentos.
        insertAdressOnBar("Dynamic"); 
        insertAdressOnBar("Global"); 
        //insertAdressOnBar("Text"); //não atualiza o segmento text porque suponho que o usuário não vá tentar salvar nesse segmento.
    }
    
    public void setA(int A){ this.A = A;}
    public void setWD(int WD){ this.WD = WD;}
    public void setWE(short WE){ this.WE = WE;}

    public int getA() { return this.A;}
    public int getRD(){ return this.RD;}
    public int getWD(){ return this.WD;}
    
    @Override
    public void clock() {
        this.WE = this.unit_control.getMemWrite(); // WE = MemWrite
        if(this.WE == 1){ // *** [AVISO]: Trecho especifico da Memoria de dados - segmento Dynamic Data
            // this.A += endereco_inicial_DYNAMIC_DATA; 
            if(this.A <= endereco_final_DYNAMIC_DATA && this.A >= endereco_inicial_TEXT){
                WriteData(); //escreve no segmento de memoria escolhido entre DYNAMIC, GLOBAL e TEXT. Segmento RESERVED não é permitido escrita para esta versão do simulador. 
            }else{
                Main.showAlert(AlertType.ERROR, "Error Dialog", "Invalid access segment", "Memory address ("+this.A+") exceeded the limit address of the DYNAMIC DATA segment ("+endereco_final_DYNAMIC_DATA+"), or is lower than the initial address of the TEXT segment ("+endereco_inicial_TEXT+").");
                throw new IllegalArgumentException("[Memory.java]: clock() - ERROR: Memory address ("+this.A+") exceeded the limit address of the DYNAMIC DATA segment ("+endereco_final_DYNAMIC_DATA+"), or is lower than the initial address of the TEXT segment ("+endereco_inicial_TEXT+").");
            }
        } else{
            this.RD = getRD_Word();
        }
    }

    public void showAddress(String modulo_mem, int qtd_enderecos){  //metodo para debug - desconsiderar.
        int endereco_init;
        if(modulo_mem.equals("Text")){ // Text
            endereco_init = endereco_inicial_TEXT; 
        } else if(modulo_mem.equals("Global")){ // Global Data
            endereco_init = endereco_inicial_GLOBAL_DATA; 
        } else if(modulo_mem.equals("Dynamic")){ // Dynamic Data    
            endereco_init = endereco_inicial_DYNAMIC_DATA; 
        } else if(modulo_mem.equals("Reserved_1")){ // Segmento Reserved abaixo do Segmento Text   
            endereco_init = endereco_inicial_RESERVED_1; 
        } else if(modulo_mem.equals("Reserved_2")){ // Segmento Reserved acima do Segmento Dynamic Data   
            endereco_init = endereco_inicial_RESERVED_2; 
        } else{
            endereco_init = endereco_inicial_DYNAMIC_DATA; // por padrão, o endereço de Dynamic Data
        }

        System.out.println("\nMemoria de "+modulo_mem);
        System.out.println("___________________________");
        for(int i=endereco_init; i < endereco_init+qtd_enderecos; i+=4){ //os endereços estão dispostos na memoria no formato hexadecimal
            System.out.println(
                Integer.toHexString(this.enderecamento[i+3] & 0xFF)+" | "+
                Integer.toHexString(this.enderecamento[i+2] & 0xFF)+" | "+
                Integer.toHexString(this.enderecamento[i+1] & 0xFF)+" | "+
                Integer.toHexString(this.enderecamento[i] & 0xFF)+" :"+
                Integer.toHexString(i)); //imprime no formato little-endian
        }
    }






    public void insertAdressOnBar(String modulo_mem){ 
        int endereco_init=0;
        String palavra;
        
        if(modulo_mem.equals("Text")){ // Text
            endereco_init = endereco_inicial_TEXT; 
            barraLateral.resetList("Text"); // reseta as instruções presentes no segmento Text.
            
        } else if(modulo_mem.equals("Global")){ // Global Data
            endereco_init = endereco_inicial_GLOBAL_DATA; 
            barraLateral.resetList("Global");

        } else if(modulo_mem.equals("Dynamic")){ // Dynamic Data 
            endereco_init = endereco_inicial_DYNAMIC_DATA;
            barraLateral.resetList("Dynamic"); 

        } else if(modulo_mem.equals("Reserved_1")){ // Segmento Reserved abaixo do Segmento Text   
            endereco_init = endereco_inicial_RESERVED_1; 
        } else if(modulo_mem.equals("Reserved_2")){ // Segmento Reserved acima do Segmento Dynamic Data   
            endereco_init = endereco_inicial_RESERVED_2; 
        } else{
            endereco_init = endereco_inicial_DYNAMIC_DATA; // por padrão, o endereço de Dynamic Data
        }

        
        for(int i=endereco_init; i < endereco_init + Main.end_limit; i+=4){ //os endereços estão dispostos na memoria no formato hexadecimal

            palavra = bits8(i); //contém a palavra e o endereço do bit menos significativo (LSB)

            if(endereco_init == endereco_inicial_TEXT && i <= endereco_final_TEXT){ //condição para evitar que exceda o endereço limite do segmento inserida em cada bloco if else
                barraLateral.insertOnTextMemory(palavra); // 00 00 00 00   0x00000000

            }else if(endereco_init == endereco_inicial_DYNAMIC_DATA && i <= endereco_final_DYNAMIC_DATA){ 
                barraLateral.insertOnDynamicMemory(palavra);

            }else if(endereco_init == endereco_inicial_GLOBAL_DATA && i <= endereco_final_GLOBAL_DATA){
                barraLateral.insertOnGlobalMemory(palavra);

            }else{ //implementar futuramente para o segmento Reserved.
                //Main.showAlert(AlertType.WARNING, "Warning Dialog", "Memory address invalid", "Reserved segments have not been implemented graphically and the values ​​will not be visible.");
                //throw new IllegalArgumentException("[Memory.java]: insertAdressOnBar() - ERRO: Endereço de segmento de memória inválido ou endereço limite do segmento excedido - "+ endereco_init);
                return;
            }
        }
        
    }

    public String bits8(int endereco){
        String[] Byte = new String[4];
        Byte[0] = Integer.toHexString(this.enderecamento[endereco] & 0xFF);    //0x7FFB4
        Byte[1] = Integer.toHexString(this.enderecamento[endereco+1] & 0xFF);  //0x7FFB5
        Byte[2] = Integer.toHexString(this.enderecamento[endereco+2] & 0xFF);  //0x7FFB6
        Byte[3] = Integer.toHexString(this.enderecamento[endereco+3] & 0xFF);  //0x7FFB7
        
        //trecho necessário para que evite que apenas 1 digito em cada coluna seja enviado para interface grafica.
        if (Byte[0].length() == 1){ Byte[0] = "0"+Byte[0]; }
        if (Byte[1].length() == 1){ Byte[1] = "0"+Byte[1]; }
        if (Byte[2].length() == 1){ Byte[2] = "0"+Byte[2]; }
        if (Byte[3].length() == 1){ Byte[3] = "0"+Byte[3]; }

        String palavra = Byte[0]+"  "+Byte[1]+"  "+Byte[2]+"  "+Byte[3]; //  big-endian
        //                  00           00           00           00
        //String palavra = Byte[3]+"  "+Byte[2]+"  "+Byte[1]+"  "+Byte[0]; //  little-endian.
        String endHex = Integer.toHexString(endereco).toUpperCase();

        while(endHex.length() != 8){
            endHex = "0" + endHex;
        }
        endHex = "0x" + endHex;
        return endHex+"   "+palavra.toUpperCase();
    }

    public void setBarraLateral(BarraLateral bar){ this.barraLateral = bar; }
}   
