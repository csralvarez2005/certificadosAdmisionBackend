package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.model.postgres.ReportePdf;
import certificadosAdmisionBackend.repository.postgres.ReportePdfRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteNotasRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.servicio.ReporteService;
import certificadosAdmisionBackend.util.GeneradorConstanciaNotasPdf;
import certificadosAdmisionBackend.util.GeneradorConstanciaPdf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteNotasRepository estudianteNotasRepository; // ✅ Declaración agregada
    private final ReportePdfRepository reportePdfRepository;

    public ReporteServiceImpl(EstudianteRepository estudianteRepository,
                              EstudianteNotasRepository estudianteNotasRepository, // ✅ Constructor actualizado
                              ReportePdfRepository reportePdfRepository) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteNotasRepository = estudianteNotasRepository;
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
    @Transactional("transactionManager")
    public Long generarConstanciaNotasPorCodigoYNivel(String codigoEstudiante, Integer nivel) {
        // ✅ Cambiado a estudianteNotasRepository
        List<EstudianteDto> todasLasNotas = estudianteNotasRepository.buscarNotasPorEstudiante(codigoEstudiante);

        if (todasLasNotas.isEmpty()) {
            throw new RuntimeException("El estudiante con código " + codigoEstudiante + " no tiene ninguna nota registrada.");
        }

        boolean hayNotasNivel = todasLasNotas.stream()
                .anyMatch(n -> Objects.equals(n.getNivel(), nivel));

        if (!hayNotasNivel) {
            throw new IllegalArgumentException("No hay notas para el nivel " + nivel + " del estudiante con código " + codigoEstudiante);
        }

        byte[] pdf = GeneradorConstanciaNotasPdf.generarPdfConstanciaNotas(todasLasNotas, nivel);

        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte("constancia_notas_" + codigoEstudiante + "_nivel" + nivel);
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdf);

        reportePdfRepository.save(reporte);
        return reporte.getId();
    }
}

