package certificadosAdmisionBackend.servicio;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;

import java.util.List;

public interface EstudianteService {
    EstudiantePageResponse listarTodosPaginado(int page, int size);
    List<EstudianteDto> obtenerNotasPorCodigo(String codigo);

}