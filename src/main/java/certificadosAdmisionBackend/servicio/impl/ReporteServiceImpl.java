package certificadosAdmisionBackend.servicio.impl;


import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.model.postgres.ReportePdf;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.repository.postgres.ReportePdfRepository;
import certificadosAdmisionBackend.servicio.ReporteService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    // ✅ Obtener PDF por ID
    @Override
    @Transactional("transactionManager")
    public Optional<byte[]> obtenerReportePdfPorId(Long id) {
        return reportePdfRepository.findById(id).map(ReportePdf::getArchivoPdf);
    }

    // ✅ Generar constancia individual con fondo
    @Override
    @Transactional
    public Long generarConstanciaEstudioPorId(Integer estudianteId) {
        EstudianteDto est = estudianteRepository.buscarPorId(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        byte[] pdfBytes = generarPdfConstanciaEstudio(est);

        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte("constancia_estudio_" + est.getCodigo());
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdfBytes);

        reportePdfRepository.save(reporte);
        return reporte.getId();
    }

    // ✅ Generar PDF por concepto con fondo
    @Transactional("transactionManager")
    public Long generarYGuardarReportePdf(int conceptoId, List<Integer> estudianteIds) {
        List<EstudianteDto> estudiantes = estudianteIds.stream()
                .map(estudianteRepository::buscarPorId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        String contenido = generarContenidoPorConcepto(estudiantes, conceptoId);
        byte[] pdfBytes = generarPdfConFondo(contenido);

        ReportePdf reporte = new ReportePdf();
        reporte.setNombreReporte(obtenerNombrePorConcepto(conceptoId));
        reporte.setFecha(LocalDateTime.now());
        reporte.setArchivoPdf(pdfBytes);

        reportePdfRepository.save(reporte);
        return reporte.getId();
    }

    // 🔧 Método que genera el PDF con fondo a partir de texto plano
    private byte[] generarPdfConFondo(String contenidoTexto) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // 🖼 Fondo personalizado
            String rutaImagen = "src/main/resources/fondo.jpg";
            Image background = Image.getInstance(new File(rutaImagen).getAbsolutePath());
            background.setAbsolutePosition(0, 0);
            background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.addImage(background);

            // ✍ Contenido
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Paragraph contenido = new Paragraph(contenidoTexto, font);
            document.add(contenido);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF con fondo", e);
        }
    }

    // 🔧 Método para generar constancia de estudio individual con fondo
    private byte[] generarPdfConstanciaEstudio(EstudianteDto est) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // 🖼 Fondo
            String rutaImagen = "src/main/resources/membrete.jpg";
            Image background = Image.getInstance(new File(rutaImagen).getAbsolutePath());
            background.setAbsolutePosition(0, 0);
            background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.addImage(background);

            // ✍ Contenido
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            Paragraph titulo = new Paragraph("CONSTANCIA DE ESTUDIO\n\n", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph contenido = new Paragraph();
            contenido.setFont(font);
            contenido.add("Se hace constar que el estudiante:\n");
            contenido.add("Nombre: " + est.getEstudiante() + "\n");
            contenido.add("Código: " + est.getCodigo() + "\n");
            contenido.add("Ha cursado estudios en nuestra institución.\n");

            document.add(titulo);
            document.add(contenido);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando constancia individual con fondo", e);
        }
    }

    // 🔧 Contenido por concepto
    private String generarContenidoPorConcepto(List<EstudianteDto> estudiantes, int conceptoId) {
        StringBuilder sb = new StringBuilder();
        switch (conceptoId) {
            case 22:
                sb.append("CONSTANCIA DE ESTUDIO\n\n");
                for (EstudianteDto e : estudiantes) {
                    sb.append("Nombre: ").append(e.getEstudiante())
                            .append(" - Código: ").append(e.getCodigo()).append("\n");
                }
                break;
            case 21:
                sb.append("CERTIFICADO DE NOTAS\n\n");
                break;
            case 20:
                sb.append("CERTIFICADO DE BUENA CONDUCTA\n\n");
                break;
            default:
                sb.append("REPORTE DESCONOCIDO\n");
        }
        return sb.toString();
    }

    // 🔧 Nombre del archivo por concepto
    private String obtenerNombrePorConcepto(int conceptoId) {
        return switch (conceptoId) {
            case 22 -> "constancia_estudio";
            case 21 -> "certificado_notas";
            case 20 -> "certificado_conducta";
            default -> "reporte_desconocido";
        };
    }
}