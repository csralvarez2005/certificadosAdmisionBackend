package certificadosAdmisionBackend.controller;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.servicio.EstudianteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/nivel-formacion/2")
    public List<EstudianteDto> obtenerEstudiantesNivelFormacionDos() {
        return estudianteService.obtenerEstudiantesNivelFormacionDos();
    }
}