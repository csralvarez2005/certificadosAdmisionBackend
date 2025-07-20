package certificadosAdmisionBackend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudiantePageResponse {
    private List<EstudianteDto> estudiantes;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
}
