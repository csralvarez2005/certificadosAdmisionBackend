package certificadosAdmisionBackend.controller;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.servicio.EstudianteService;
import certificadosAdmisionBackend.servicio.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;
    private final ReporteService reporteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService, ReporteService reporteService) {
        this.estudianteService = estudianteService;
        this.reporteService = reporteService;
    }
    // NUEVO: Descargar el PDF por ID
    @GetMapping("/reporte/{id}")
    public ResponseEntity<byte[]> descargarReportePdf(@PathVariable Long id) {
        Optional<byte[]> contenido = reporteService.obtenerReportePdfPorId(id);

        return contenido.map(pdfBytes -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_estudiantes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes)
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }
    // NUEVO: Generar constancia de estudio por ID
    @PostMapping("/reporte/constancia-estudio/{id}")
    public ResponseEntity<String> generarConstanciaEstudio(@PathVariable Integer id) {
        Long idGenerado = reporteService.generarConstanciaEstudioPorId(id);
        return ResponseEntity.ok("Constancia de estudio generada con ID: " + idGenerado);
    }

}