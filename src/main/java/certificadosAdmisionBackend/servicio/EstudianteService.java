package certificadosAdmisionBackend.servicio;


import certificadosAdmisionBackend.dto.EstudiantePageResponse;



public interface EstudianteService {
    EstudiantePageResponse obtenerTodosLosEstudiantes(int pagina, int tamano);
}
