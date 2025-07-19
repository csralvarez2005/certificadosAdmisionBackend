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
    public List<EstudianteDto> obtenerEstudiantesNivelFormacionDos() {
        return estudianteRepository.buscarEstudiantesConNivelFormacionDos();
    }
}