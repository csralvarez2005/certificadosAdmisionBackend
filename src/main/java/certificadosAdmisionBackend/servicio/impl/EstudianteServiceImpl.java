package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.servicio.EstudianteService;
import org.springframework.stereotype.Service;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;


    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }


}