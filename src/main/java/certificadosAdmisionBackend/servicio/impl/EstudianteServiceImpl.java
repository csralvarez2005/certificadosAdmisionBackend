package certificadosAdmisionBackend.servicio.impl;


import certificadosAdmisionBackend.dto.EstudianteDto;
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
    public List<EstudianteDto> obtenerTodosLosEstudiantes() {
        List<Object[]> resultados = estudianteRepository.buscarTodosLosEstudiantes();
        return resultados.stream()
                .map(obj -> new EstudianteDto(
                        (String) obj[0], // estudiante
                        (String) obj[1], // codigo
                        (String) obj[2], // email
                        (String) obj[3], // programaTecnico
                        (String) obj[4]  // horario
                ))
                .toList();
    }
}