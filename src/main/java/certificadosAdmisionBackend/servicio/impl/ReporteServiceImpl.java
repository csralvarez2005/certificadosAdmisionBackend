package certificadosAdmisionBackend.servicio.impl;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.model.postgres.ReportePdf;
import certificadosAdmisionBackend.repository.postgres.ReportePdfRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteNotasRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.servicio.ReporteService;
import certificadosAdmisionBackend.util.GeneradorConstanciaNotasPdf;
import certificadosAdmisionBackend.util.GeneradorConstanciaPdf;
import certificadosAdmisionBackend.util.GeneradorCertificadoBuenaConductaPdf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteNotasRepository estudianteNotasRepository;
    private final ReportePdfRepository reportePdfRepository;
    private final GeneradorCertificadoBuenaConductaPdf generadorCertificadoBuenaConductaPdf;

    public ReporteServiceImpl(EstudianteRepository estudianteRepository,
                              EstudianteNotasRepository estudianteNotasRepository,
                              ReportePdfRepository reportePdfRepository,
                              GeneradorCertificadoBuenaConductaPdf generadorCertificadoBuenaConductaPdf) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteNotasRepository = estudianteNotasRepository;
        this.reportePdfRepository = reportePdfRepository;
        this.generadorCertificadoBuenaConductaPdf = generadorCertificadoBuenaConductaPdf;
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
    public Long generarConstanciaNotasPorCodigoYNivel(String codigoEstudiante, Integer nivel, String cuerpo, String infoPrograma) {
        // Obtener todas las notas del estudiante
        List<EstudianteDto> todasLasNotas = estudianteNotasRepository.buscarNotasPorEstudiante(codigoEstudiante);

        if (todasLasNotas.isEmpty()) {
            throw new RuntimeException("El estudiante con código " + codigoEstudiante + " no tiene ninguna nota registrada.");
        }

        boolean hayNotasNivel = todasLasNotas.stream()
                .anyMatch(n -> Objects.equals(n.getNivel(), nivel));

        if (!hayNotasNivel) {
            throw new IllegalArgumentException("No hay notas para el nivel " + nivel + " del estudiante con código " + codigoEstudiante);
        }

        // Generar PDF con cuerpo e infoPrograma
        byte[] pdf = GeneradorConstanciaNotasPdf.generarPdfConstanciaNotas(todasLasNotas, nivel, cuerpo, infoPrograma);

        // Guardar en base de datos
        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte("constancia_notas_" + codigoEstudiante + "_nivel" + nivel);
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdf);

        reportePdfRepository.save(reporte);
        return reporte.getId();
    }

    @Override
    @Transactional("transactionManager")
    public Long generarCertificadoBuenaConductaPorId(Integer estudianteId) {
        // Obtener el estudiante por su ID
        EstudianteDto estudiante = estudianteRepository.buscarPorId(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Generar el certificado de buena conducta en PDF
        byte[] pdfBytes = generadorCertificadoBuenaConductaPdf.generarPdfCertificadoBuenaConducta(estudiante);

        // Crear un objeto ReportePdf y almacenarlo en la base de datos
        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte("certificado_buena_conducta_" + estudiante.getCodigo());
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdfBytes);

        reportePdfRepository.save(reporte);
        return reporte.getId();  // Retorna el ID del reporte generado
    }
}

