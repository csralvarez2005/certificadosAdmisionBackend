package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.model.postgres.ReportePdf;
import certificadosAdmisionBackend.repository.postgres.ReportePdfRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.servicio.ReporteService;
import certificadosAdmisionBackend.util.GeneradorConstanciaNotasPdf;
import certificadosAdmisionBackend.util.GeneradorConstanciaPdf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final EstudianteRepository estudianteRepository;
    private final ReportePdfRepository reportePdfRepository;

    public ReporteServiceImpl(EstudianteRepository estudianteRepository,
                              ReportePdfRepository reportePdfRepository) {
        this.estudianteRepository = estudianteRepository;
        this.reportePdfRepository = reportePdfRepository;
    }

    @Override
    @Transactional("transactionManager")
    public Optional<byte[]> obtenerReportePdfPorId(Long id) {
        return reportePdfRepository.findById(id).map(ReportePdf::getArchivoPdf);
    }

    @Override
    @Transactional
    public Long generarConstanciaEstudioPorId(Integer estudianteId) {
        EstudianteDto est = estudianteRepository.buscarPorId(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        byte[] pdfBytes = GeneradorConstanciaPdf.generarPdfConstanciaEstudio(est);

        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte("constancia_estudio_" + est.getCodigo());
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdfBytes);

        reportePdfRepository.save(reporte);
        return reporte.getId();
    }

    @Override
    public Long generarConstanciaNotasPorIdYNivel(Integer idEstudiante, Integer nivel) {
        List<EstudianteDto> todasLasNotas = estudianteRepository.buscarNotasPorEstudiante(idEstudiante);

        // Filtrar notas por nivel deseado
        List<EstudianteDto> notasFiltradas = todasLasNotas.stream()
                .filter(n -> n.getNivel() != null && n.getNivel().equals(nivel))
                .distinct() // opcional, pero útil para eliminar duplicados
                .collect(Collectors.toList());

        byte[] pdf = GeneradorConstanciaNotasPdf.generarPdfConstanciaNotas(notasFiltradas, nivel);

        // Guardar el PDF como ReportePdf (como ya lo haces)
        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte("Constancia de notas - Nivel " + nivel);
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdf);  // pdf generado como byte[]
        reporte = reportePdfRepository.save(reporte);

        return reporte.getId();
    }


}
