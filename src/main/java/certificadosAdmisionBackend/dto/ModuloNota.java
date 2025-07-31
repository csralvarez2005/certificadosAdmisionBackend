package certificadosAdmisionBackend.dto;

public class ModuloNota {

    private String modulo;
    private double nota;

    public ModuloNota() {
    }

    public ModuloNota(String modulo, double nota) {
        this.modulo = modulo;
        this.nota = nota;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
