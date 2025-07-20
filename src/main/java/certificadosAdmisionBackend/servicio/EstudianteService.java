package certificadosAdmisionBackend.servicio;


import certificadosAdmisionBackend.dto.EstudiantePageResponse;

import java.util.List;

public interface EstudianteService {
    EstudiantePageResponse obtenerTodosLosEstudiantes(int pagina, int tamano);
}
