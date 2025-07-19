package certificadosAdmisionBackend.servicio;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.entity.Estudiante;

import java.util.List;

public interface EstudianteService {
    List<EstudianteDto> obtenerEstudiantesNivelFormacionDos();
}
