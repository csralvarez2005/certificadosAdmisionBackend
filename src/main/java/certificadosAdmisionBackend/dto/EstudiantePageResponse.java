package certificadosAdmisionBackend.dto;

import java.util.List;

public class EstudiantePageResponse {
    private List<EstudianteDto> estudiantes;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;

    public EstudiantePageResponse() {
    }

    public EstudiantePageResponse(List<EstudianteDto> estudiantes, int paginaActual, int totalPaginas, long totalElementos) {
        this.estudiantes = estudiantes;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.totalElementos = totalElementos;
    }

    public List<EstudianteDto> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<EstudianteDto> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(long totalElementos) {
        this.totalElementos = totalElementos;
    }
}
