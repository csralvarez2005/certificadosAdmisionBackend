package certificadosAdmisionBackend.util;

import certificadosAdmisionBackend.dto.EstudianteDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;

@Component
public class GeneradorCertificadoBuenaConductaPdf {

    public static byte[] generarPdfCertificadoBuenaConducta(EstudianteDto est) {
        try {
            // Configuración de la página
            Rectangle pageSize = new Rectangle(612, 792); // Tamaño carta
            Document document = new Document(pageSize, 50, 50, 160, 100);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Agregar imagen de fondo
            String rutaImagenFondo = "src/main/resources/membrete.jpg";  // Imagen de fondo
            try {
                System.out.println("Cargando imagen de fondo desde: " + rutaImagenFondo);
                Image background = Image.getInstance(new File(rutaImagenFondo).getAbsolutePath());
                background.setAbsolutePosition(0, 0);
                background.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
                writer.getDirectContentUnder().addImage(background);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
                throw new RuntimeException("Error al cargar la imagen de fondo", e);
            }

            // Definir fuentes
            Font tituloFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
            Font subtituloFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
            Font cuerpoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
            Font negritaFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            // Título
            Paragraph titulo = new Paragraph("EL SUSCRITO COORDINADOR DE LA FUNDACIÓN INTERNACIONAL DE EDUCACIÓN ELYON YIREH", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Subtítulo
            Paragraph subtitulo = new Paragraph("Con Licencia de funcionamiento otorgada por la Secretaría de Educación Distrital de Cartagena\n" +
                    "con resolución N°. 5749 del 20 de septiembre de 2022\n\n", subtituloFont);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20f);
            document.add(subtitulo);

            // Encabezado
            Paragraph encabezado = new Paragraph("CERTIFICA QUE\n", negritaFont);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setSpacingAfter(5f);
            document.add(encabezado);

// Cuerpo del Certificado
            Paragraph cuerpo = new Paragraph();
            cuerpo.setFont(cuerpoFont);
            cuerpo.setAlignment(Element.ALIGN_JUSTIFIED);
            cuerpo.setSpacingBefore(10f);

// Nombre y documento del estudiante
            Chunk nombreEstudiante = new Chunk(est.getEstudiante().toUpperCase(), negritaFont);
            String documento = est.getTipoDocumento().toLowerCase() + " No. ";
            Chunk documentoChunk = new Chunk(documento, negritaFont);
            Chunk numeroDocumento = new Chunk(est.getCodigo(), negritaFont);

// Construcción del párrafo
            cuerpo.add("Que: el estudiante ");
            cuerpo.add(nombreEstudiante);
            cuerpo.add(", identificada(o) con ");
            cuerpo.add(documentoChunk);
            cuerpo.add(numeroDocumento);
            cuerpo.add(", durante su permanencia en esta institución, en el programa Técnico Laboral en ");
            cuerpo.add(new Chunk(est.getProgramaTecnico(), negritaFont));
            cuerpo.add(", se distinguió por mantener una conducta ejemplar y un comportamiento adecuado en el ámbito académico y disciplinario.\n");

            cuerpo.add("Su formación se desarrolló en la modalidad presencial diurna, comprendida desde el segundo periodo académico del año 2023 hasta el primer periodo académico del año 2025.\n\n");

// Lugar de expedición
            cuerpo.add("Este certificado se expide en " + est.getLugarExpedicionDocumento() + ".\n\n");

// Fecha de emisión
            cuerpo.add("Fecha de emisión: "
                    + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                    + ".\n\n");

// Firma
            // Firma
            cuerpo.add("Atentamente,\n\n\n\n");
            cuerpo.add("_________________________\n");
            cuerpo.add("LIBIA MARCY LAVERDE ROJAS\n");
            cuerpo.add("COORDINACIÓN ACADÉMICA\n");

// Correo en color azul
            Font correoFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLUE);
            Chunk correo = new Chunk("E-MAIL: academica@elyonyireh.edu.co", correoFont);
            cuerpo.add(correo);
            cuerpo.add("\n");

            document.add(cuerpo);

// Cerrar documento
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();  // Detalles adicionales del error
            throw new RuntimeException("Error generando el certificado de buena conducta", e);
        }
    }
}