package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
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
    public EstudiantePageResponse listarTodosPaginado(int page, int size) {
        List<EstudianteDto> estudiantes = estudianteRepository.buscarTodosPaginado(page, size);

        // Delegamos completamente al repository el conteo
        long totalRegistros = estudianteRepository.contarTotalEstudiantes();
        int totalPages = (int) Math.ceil((double) totalRegistros / size);

        return new EstudiantePageResponse(
                estudiantes,
                page,
                totalPages,
                totalRegistros
        );
    }
}