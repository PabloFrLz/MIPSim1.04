package MIPS;
import java.util.ArrayList;

import GUI.BarraLateral;

public class RegisterFile implements ClockListener{
    ArrayList<Integer> registradores;
    int A1;
    int A2;
    int A3;
    int RD1;
    int RD2;
    int WE3;
    int WD3;
    UC unit_control;
    BarraLateral barraLateral;
    public static int[] bar_info;

    public void setBarraLateral(BarraLateral barraLateral) { this.barraLateral = barraLateral;} //para atualizar os registradores na barra lateral direita.

    public RegisterFile(ArrayList<Integer> registradores, UC unit_control){
        this.registradores = registradores;
        this.unit_control = unit_control;
        bar_info = new int[2];
    }

    public void setRegistradores(int reg, int valor) {
        if(reg <= 32 && reg >= 0){ // 32 registradores
            this.registradores.set(reg, valor);
            bar_info[0] = reg; //para atualizar os valores no registrador atualizado na barra lateral direita.
            bar_info[1] = valor;
        } else{
            System.out.println("[RegisterFile.java]: setRegistradores() - ERRO: Enderecamento de registrador excedido.");
        }
    }
    public void setA1(int a1) { this.A1 = a1;}
    public void setA2(int a2) { this.A2 = a2;}
    public void setA3(int a3) { this.A3 = a3;}
    public void setWE3(int WE3) { this.WE3 = WE3;}
    public void setWD3(int WD3) { this.WD3 = WD3;}

    public int getA1() { return this.A1;}
    public int getA2() { return this.A2;}
    public int getA3() { return this.A3;}
    public int getRD1(){ return this.RD1;}
    public int getRD2(){ return this.RD2;}
    public int getWD3() { return this.WD3;}
    public int getWE3() { return this.WE3;}


    
    public String registrador_num(String asm_reg){
        if(asm_reg.equals("$0")){ return "00000";
        } else if(asm_reg.equals("$at")){ return "00001";
        } else if(asm_reg.equals("$v0")){ return "00010";
        } else if(asm_reg.equals("$v1")){ return "00011";
        } else if(asm_reg.equals("$a0")){ return "00100";
        } else if(asm_reg.equals("$a1")){ return "00101";
        } else if(asm_reg.equals("$a2")){ return "00110";
        } else if(asm_reg.equals("$a3")){ return "00111";
        } else if(asm_reg.equals("$t0")){ return "01000";
        } else if(asm_reg.equals("$t1")){ return "01001";
        } else if(asm_reg.equals("$t2")){ return "01010";
        } else if(asm_reg.equals("$t3")){ return "01011";
        } else if(asm_reg.equals("$t4")){ return "01100";
        } else if(asm_reg.equals("$t5")){ return "01101";
        } else if(asm_reg.equals("$t6")){ return "01110";
        } else if(asm_reg.equals("$t7")){ return "01111";
        } else if(asm_reg.equals("$s0")){ return "10000";
        } else if(asm_reg.equals("$s1")){ return "10001";
        } else if(asm_reg.equals("$s2")){ return "10010";
        } else if(asm_reg.equals("$s3")){ return "10011";
        } else if(asm_reg.equals("$s4")){ return "10100";
        } else if(asm_reg.equals("$s5")){ return "10101";
        } else if(asm_reg.equals("$s6")){ return "10110";
        } else if(asm_reg.equals("$s7")){ return "10111";
        } else if(asm_reg.equals("$t8")){ return "11000";
        } else if(asm_reg.equals("$t9")){ return "11001";
        } else if(asm_reg.equals("$k0")){ return "11010";
        } else if(asm_reg.equals("$k1")){ return "11011";
        } else if(asm_reg.equals("$gp")){ return "11100";
        } else if(asm_reg.equals("$sp")){ return "11101";
        } else if(asm_reg.equals("$fp")){ return "11110";
        } else if(asm_reg.equals("$ra")){ return "11111";
        } else{  throw new IllegalArgumentException("[RegisterFile.java]: registrador_num() - ERRO: Registrador inválido: " + asm_reg); }
    }


    public String getRegistrador(String inst, String name_reg){ //Funciona para intruções Tipo-R e Tipo-I - lembrando que instruções Tipo-I não tem o campo "rd"
        if (name_reg.equals("rs")){
            return inst.substring(6, 11);
        } else if (name_reg.equals("rt")){
            return inst.substring(11, 16);
        } else if (name_reg.equals("rd")){
            return inst.substring(16, 21);
        } else {
            throw new IllegalArgumentException("[RegisterFile.java]: getRegistradorTIPO_R() - ERRO: Registrador inválido - " + name_reg);
        }
    }

    
    
    public void showAllRegisters(){ // para debug
        System.out.println("\n| *** [Registradores] *** |");
        System.out.println("\t $0: "+this.registradores.get(0));
        System.out.println("\t $at: "+this.registradores.get(1));
        System.out.println("\t $v0: "+this.registradores.get(2));
        System.out.println("\t $v1: "+this.registradores.get(3));
        System.out.println("\t $a0: "+this.registradores.get(4));
        System.out.println("\t $a1: "+this.registradores.get(5));
        System.out.println("\t $a2: "+this.registradores.get(6));
        System.out.println("\t $a3: "+this.registradores.get(7));
        System.out.println("\t $t0: "+this.registradores.get(8));
        System.out.println("\t $t1: "+this.registradores.get(9));
        System.out.println("\t $t2: "+this.registradores.get(10));
        System.out.println("\t $t3: "+this.registradores.get(11));
        System.out.println("\t $t4: "+this.registradores.get(12));
        System.out.println("\t $t5: "+this.registradores.get(13));
        System.out.println("\t $t6: "+this.registradores.get(14));
        System.out.println("\t $t7: "+this.registradores.get(15));
        System.out.println("\t $s0: "+this.registradores.get(16));
        System.out.println("\t $s1: "+this.registradores.get(17));
        System.out.println("\t $s2: "+this.registradores.get(18));
        System.out.println("\t $s3: "+this.registradores.get(19));
        System.out.println("\t $s4: "+this.registradores.get(20));
        System.out.println("\t $s5: "+this.registradores.get(21));
        System.out.println("\t $s6: "+this.registradores.get(22));
        System.out.println("\t $s7: "+this.registradores.get(23));
        System.out.println("\t $t8: "+this.registradores.get(24));
        System.out.println("\t $t9: "+this.registradores.get(25));
        System.out.println("\t $k0: "+this.registradores.get(26));
        System.out.println("\t $k1: "+this.registradores.get(27));
        System.out.println("\t $gp: "+this.registradores.get(28));
        System.out.println("\t $sp: "+this.registradores.get(29));
        System.out.println("\t $fp: "+this.registradores.get(30));
        System.out.println("\t $ra: "+this.registradores.get(31));
        
    }
    
    @Override
    public void clock() { //ao dar o clock, os valores nos registradores especificados pelos endereços A1 e A2, são postos nas saídas RD1 e RD2. Ou ocorre escrita dos dados
        this.RD1 = this.registradores.get(A1);
        this.RD2 = this.registradores.get(A2);
        this.WE3 = this.unit_control.getRegWrite();
        if(this.WE3 == 1){ //escrita
            setRegistradores(this.A3, this.WD3);
        }
    }

    public void resetRegisters() {
        for (int i = 0; i < 32; i++) { 
            this.registradores.set(i, 0);
        } 
        
        barraLateral.resetRegisters(); //reseta o valor dos registradores na interface grafica
    }
    
    
}
