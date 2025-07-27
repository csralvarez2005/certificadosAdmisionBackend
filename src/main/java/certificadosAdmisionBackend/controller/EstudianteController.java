package certificadosAdmisionBackend.controller;

import certificadosAdmisionBackend.dto.CertificadoRequest;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.servicio.EstudianteService;
import certificadosAdmisionBackend.servicio.ReporteService;
import certificadosAdmisionBackend.util.GeneradorConstanciaPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;
    private final ReporteService reporteService;
    private final GeneradorConstanciaPdf generadorConstanciaPdf;

    @Autowired
    public EstudianteController(
            EstudianteService estudianteService,
            ReporteService reporteService,
            GeneradorConstanciaPdf generadorConstanciaPdf
    ) {
        this.estudianteService = estudianteService;
        this.reporteService = reporteService;
        this.generadorConstanciaPdf = generadorConstanciaPdf;
    }

    @GetMapping("/reporte/{id}")
    public ResponseEntity<byte[]> descargarReportePdf(@PathVariable Long id) {
        Optional<byte[]> contenido = reporteService.obtenerReportePdfPorId(id);

        return contenido.map(pdfBytes -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_estudiantes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes)
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/reporte/constancia-estudio/{id}")
    public ResponseEntity<Map<String, Object>> generarConstanciaEstudio(@PathVariable Integer id) {
        Long idGenerado = reporteService.generarConstanciaEstudioPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Constancia de estudio generada");
        response.put("id", idGenerado);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reporte/constancia-estudio/personalizada")
    public ResponseEntity<byte[]> generarConstanciaPersonalizada(
            @RequestParam Integer id,
            @RequestBody CertificadoRequest req
    ) {
        byte[] pdf = generadorConstanciaPdf.generarDesdeTexto(req.getCuerpo());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=constancia_estudio.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/listar")
    public ResponseEntity<EstudiantePageResponse> listarEstudiantes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        EstudiantePageResponse respuesta = estudianteService.listarTodosPaginado(page, size);
        return ResponseEntity.ok(respuesta);
    }
}