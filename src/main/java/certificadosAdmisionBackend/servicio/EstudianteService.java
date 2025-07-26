package certificadosAdmisionBackend.servicio;


import certificadosAdmisionBackend.dto.EstudiantePageResponse;

public interface EstudianteService {
    EstudiantePageResponse listarTodosPaginado(int page, int size);
}
