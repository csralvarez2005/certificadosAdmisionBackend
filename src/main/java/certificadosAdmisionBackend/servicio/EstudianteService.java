package certificadosAdmisionBackend.servicio;

import certificadosAdmisionBackend.dto.EstudianteDto;

import java.util.List;

public interface EstudianteService {
    List<EstudianteDto> obtenerTodosLosEstudiantes();
}
