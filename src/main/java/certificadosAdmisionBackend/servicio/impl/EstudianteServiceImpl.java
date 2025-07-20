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
        List<Object[]> resultados = estudianteRepository.buscarEstudiantesPaginados(pagina, tamano);
        long totalElementos = estudianteRepository.contarTotalEstudiantes();
        int totalPaginas = (int) Math.ceil((double) totalElementos / tamano);

        List<EstudianteDto> estudiantes = resultados.stream()
                .map(obj -> new EstudianteDto(
                        (String) obj[0],                    // estudiante
                        (String) obj[1],                    // codigo
                        (String) obj[2],                    // email
                        (String) obj[3],                    // programaTecnico
                        ((Number) obj[4]).intValue(),       // semestre
                        (String) obj[5]                     // horario
                ))
                .toList();

        return new EstudiantePageResponse(estudiantes, pagina, totalPaginas, totalElementos);
    }
}