package certificadosAdmisionBackend.controller;

import certificadosAdmisionBackend.dto.CertificadoRequest;
import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.servicio.EstudianteService;
import certificadosAdmisionBackend.servicio.ReporteService;
import certificadosAdmisionBackend.util.GeneradorConstanciaNotasPdf;
import certificadosAdmisionBackend.util.GeneradorConstanciaPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import static certificadosAdmisionBackend.util.GeneradorConstanciaNotasPdf.convertirNivelARomano;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    @PostMapping("/reporte/constancia-notas/{codigo}")
    public ResponseEntity<Map<String, Object>> generarConstanciaNotas(
            @PathVariable String codigo,
            @RequestParam Integer nivel,
            @RequestParam(required = false) String cuerpo,
            @RequestParam(required = false) String infoPrograma
    ) {
        Long idGenerado = reporteService.generarConstanciaNotasPorCodigoYNivel(
                codigo,
                nivel,
                cuerpo != null ? cuerpo : "",
                infoPrograma != null ? infoPrograma : ""
        );

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

    // ✅ 6. Generar constancia de notas personalizada (POST)
    @PostMapping("/reporte/constancia-notas/personalizada")
    public ResponseEntity<byte[]> generarConstanciaNotasPersonalizada(
            @RequestParam Integer id,
            @RequestParam Integer nivel,
            @RequestBody CertificadoRequest req
    ) {
        byte[] pdf = generadorConstanciaPdf.generarDesdeTexto(req.getCuerpo());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado_notas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ✅ 7. Nuevo: Generar Certificado de Buena Conducta (POST)
    @PostMapping("/reporte/certificado-buena-conducta/{id}")
    public ResponseEntity<Map<String, Object>> generarCertificadoBuenaConducta(@PathVariable Integer id) {
        Long idGenerado = reporteService.generarCertificadoBuenaConductaPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Certificado de buena conducta generado");
        response.put("id", idGenerado);
        return ResponseEntity.ok(response);
    }

    public static List<Map<String, Object>> generarTablaNotasJson(List<EstudianteDto> notas, int nivelDeseado) {
        List<Map<String, Object>> tabla = new ArrayList<>();
        Set<String> modulosUnicos = new LinkedHashSet<>();

        List<EstudianteDto> modulosFiltrados = notas.stream()
                .filter(n -> Objects.equals(n.getNivel(), nivelDeseado))
                .filter(n -> n.getAsignatura() != null && n.getNotaDefinitiva() != null)
                .filter(n -> modulosUnicos.add(n.getAsignatura().trim() + "|" + n.getNotaDefinitiva()))
                .toList();

        String periodo = modulosFiltrados.isEmpty() || modulosFiltrados.get(0).getNivel() == null
                ? ""
                : convertirNivelARomano(modulosFiltrados.get(0).getNivel());

        for (EstudianteDto dto : modulosFiltrados) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("periodo", periodo);
            fila.put("modulo", dto.getAsignatura());
            fila.put("nota", dto.getNotaDefinitiva());
            tabla.add(fila);
        }

        double promedio = modulosFiltrados.stream()
                .mapToDouble(EstudianteDto::getNotaDefinitiva)
                .average()
                .orElse(0.0);

        Map<String, Object> promedioFila = new HashMap<>();
        promedioFila.put("modulo", "PROMEDIO");
        promedioFila.put("nota", BigDecimal.valueOf(promedio).setScale(2, RoundingMode.HALF_UP).toPlainString());
        promedioFila.put("periodo", "");
        tabla.add(promedioFila);

        return tabla;
    }

    @GetMapping("/notas/tabla/{codigo}")
    public ResponseEntity<List<Map<String, Object>>> generarTablaNotas(
            @PathVariable String codigo,
            @RequestParam int nivel
    ) {
        List<EstudianteDto> notas = estudianteService.obtenerNotasPorCodigo(codigo);
        List<Map<String, Object>> tabla = generarTablaNotasJson(notas, nivel);
        return ResponseEntity.ok(tabla);
    }

    @GetMapping("/notas/{codigo}")
    public ResponseEntity<List<EstudianteDto>> obtenerNotasPorCodigo(@PathVariable String codigo) {
        List<EstudianteDto> notas = estudianteService.obtenerNotasPorCodigo(codigo);
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/constancia-notas")
    public ResponseEntity<byte[]> generarConstanciaNotas(
            @RequestParam Long id,
            @RequestParam int nivel,
            @RequestParam String cuerpo,
            @RequestParam String infoPrograma
    ) {
        List<EstudianteDto> notas = estudianteService.obtenerNotasPorIdYNivel(id, nivel);

        byte[] pdfBytes = GeneradorConstanciaNotasPdf.generarPdfConstanciaNotas(
                notas,
                nivel,
                cuerpo,
                infoPrograma
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado_notas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}