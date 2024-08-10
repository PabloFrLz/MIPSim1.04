package MIPS;
public class NARegistrador implements ClockListener{
    private int portaA;
    private int portaB;//porta usada apenas em registros não arquiteturais com duas entradas e duas saídas
    private int saidaA;
    private int saidaB;

    public NARegistrador(){
        this.portaA = 0;
        this.portaB = 0;
    }

    public void setPortaA(int portaA) { this.portaA = portaA;}
    public void setPortaB(int portaB) { this.portaB = portaB;} 


    public int getPortaA() { return this.portaA;}
    public int getPortaB() { return this.portaB;}
    public int getSaidaA() { return this.saidaA;}
    public int getSaidaB() { return this.saidaB;}

    @Override
    public void clock() {
        this.saidaA = this.portaA;
        this.saidaB = this.portaB;
    }


}
