package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.repository.EstudianteRepository;
import certificadosAdmisionBackend.servicio.EstudianteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public EstudiantePageResponse obtenerTodosLosEstudiantes(int pagina, int tamano) {
        // Ya devuelve una lista de EstudianteDto
        List<EstudianteDto> estudiantes = estudianteRepository.buscarEstudiantesPaginados(pagina, tamano);
        long totalElementos = estudianteRepository.contarTotalEstudiantes();
        int totalPaginas = (int) Math.ceil((double) totalElementos / tamano);

        return new EstudiantePageResponse(estudiantes, pagina, totalPaginas, totalElementos);
    }
}