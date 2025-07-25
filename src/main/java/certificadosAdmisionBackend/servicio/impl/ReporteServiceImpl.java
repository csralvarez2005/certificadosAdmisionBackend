package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.model.postgres.ReportePdf;
import certificadosAdmisionBackend.repository.postgres.ReportePdfRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.servicio.ReporteService;
import certificadosAdmisionBackend.util.GeneradorConstanciaPdf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
}
