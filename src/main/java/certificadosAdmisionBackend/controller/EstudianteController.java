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

    // ✅ 1. Descargar PDF desde base de datos por ID (GET)
    @GetMapping("/reporte/{id}")
    public ResponseEntity<byte[]> descargarReportePdf(@PathVariable Long id) {
        Optional<byte[]> contenido = reporteService.obtenerReportePdfPorId(id);

        return contenido.map(pdfBytes -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_estudiantes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes)
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 2. Generar constancia de estudio y guardar en base de datos (POST)
    @PostMapping("/reporte/constancia-estudio/{id}")
    public ResponseEntity<Map<String, Object>> generarConstanciaEstudio(@PathVariable Integer id) {
        Long idGenerado = reporteService.generarConstanciaEstudioPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Constancia de estudio generada");
        response.put("id", idGenerado);
        return ResponseEntity.ok(response);
    }

    // ✅ 3. Generar constancia de notas y guardar en base de datos (POST)
    @PostMapping("/reporte/constancia-notas/{id}")
    public ResponseEntity<Map<String, Object>> generarConstanciaNotas(
            @PathVariable Integer id,
            @RequestParam Integer nivel
    ) {
        Long idGenerado = reporteService.generarConstanciaNotasPorIdYNivel(id, nivel);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Constancia de notas generada");
        response.put("id", idGenerado);
        return ResponseEntity.ok(response);
    }

    // ✅ 4. Generar constancia personalizada (sin guardar en BD) (POST)
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

    // ✅ 5. Listar estudiantes paginados (GET)
    @GetMapping("/listar")
    public ResponseEntity<EstudiantePageResponse> listarEstudiantes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        EstudiantePageResponse respuesta = estudianteService.listarTodosPaginado(page, size);
        return ResponseEntity.ok(respuesta);
    }
}