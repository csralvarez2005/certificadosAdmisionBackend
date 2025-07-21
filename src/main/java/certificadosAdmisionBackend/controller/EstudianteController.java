package certificadosAdmisionBackend.controller;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.servicio.EstudianteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/todos")
    public EstudiantePageResponse obtenerTodosLosEstudiantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano
    ) {
        return estudianteService.obtenerTodosLosEstudiantes(pagina, tamano);
    }
}