package certificadosAdmisionBackend.util;

import certificadosAdmisionBackend.dto.EstudianteDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
@Component
public class GeneradorConstanciaPdf {

    public static byte[] generarPdfConstanciaEstudio(EstudianteDto est) {
        try {
            Rectangle pageSize = new Rectangle(612, 792); // Tamaño carta
            Document document = new Document(pageSize, 50, 50, 160, 100);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Agrega fondo
            String rutaImagen = "src/main/resources/membrete.jpg";
            Image background = Image.getInstance(new File(rutaImagen).getAbsolutePath());
            background.setAbsolutePosition(0, 0);
            background.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
            writer.getDirectContentUnder().addImage(background);

            // Definir fuentes
            Font tituloFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
            Font subtituloFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
            Font cuerpoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
            Font encabezadoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
            Font negritaFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font correoFontAzul = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE);

            // Título
            Paragraph titulo = new Paragraph(
                    "EL SUSCRITO COORDINADOR DE LA FUNDACION INTERNACIONAL DE EDUCACION ELYON YIREH",
                    tituloFont
            );
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Subtítulo
            Paragraph subtitulo = new Paragraph(
                    "Con Licencia de funcionamiento otorgada por la secretaria de educación Distrital de Cartagena\n" +
                            "con resolución N°. 5749 del 20 de septiembre de 2022\n\n",
                    subtituloFont
            );
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20f);
            document.add(subtitulo);

            // Encabezado
            Paragraph encabezado = new Paragraph("HACE CONSTAR\n", encabezadoFont);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);

            // Cuerpo
            Paragraph cuerpo = new Paragraph();
            cuerpo.setFont(cuerpoFont);
            cuerpo.setAlignment(Element.ALIGN_JUSTIFIED);
            cuerpo.setSpacingBefore(10f);

            Chunk nombreEstudiante = new Chunk(est.getEstudiante().toUpperCase(), negritaFont);
            String documento = est.getTipoDocumento().toUpperCase() + " " + est.getCodigo().toUpperCase();
            Chunk documentoChunk = new Chunk(documento, negritaFont);

            cuerpo.add("Que: ");
            cuerpo.add(nombreEstudiante);
            cuerpo.add(", identificado(a) con ");
            cuerpo.add(documentoChunk);
            cuerpo.add(", se encuentra matriculado(a) en esta Fundación para el ");
            cuerpo.add("segundo semestre del programa Técnico laboral en " + est.getProgramaTecnico() + ". ");
            cuerpo.add("Con Jornada " + est.getHorario().toLowerCase() + ". ");
            cuerpo.add("Periodo B 2025.\n\n");

            cuerpo.add("Inicio de semestre: 01 de julio 2025               Finalización: 4 de diciembre 2025\n\n");
            cuerpo.add("Duración de programa: 2 años\n");
            cuerpo.add("Intensidad horaria del programa: 1.248 horas\n");
            cuerpo.add("Intensidad horaria semanal: 24 horas\n");

            document.add(cuerpo);

            // Pie de página
            Paragraph pie = new Paragraph();
            pie.setFont(cuerpoFont);
            pie.setSpacingBefore(30f);
            pie.setAlignment(Element.ALIGN_LEFT);

            // Generar fecha dinámica
            LocalDate fechaActual = LocalDate.now();
            int dia = fechaActual.getDayOfMonth();
            String diaTexto = convertirNumeroATexto(dia);
            String mesTexto = fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            int anio = fechaActual.getYear();

            String fraseFecha = String.format(
                    "Para constancia se firma y sella en Cartagena de Indias a los %s (%d) días del mes de %s de %d.\n\n",
                    diaTexto,
                    dia,
                    mesTexto,
                    anio
            );

            pie.add(fraseFecha);
            pie.add("Atentamente,\n\n\n\n");
            pie.add("_________________________\n");
            pie.add("LIBIA MARCY LAVERDE ROJAS\n");
            pie.add("COORDINACION ACADEMICA\n");

            Chunk correo = new Chunk("E-MAIL academica@elyonyireh.edu.co", correoFontAzul);
            pie.add(correo);

            document.add(pie);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando constancia de estudio", e);
        }
    }
    public static byte[] generarDesdeTexto(String cuerpoTexto) {
        try {
            Rectangle pageSize = new Rectangle(612, 792); // Tamaño carta
            Document document = new Document(pageSize, 50, 50, 160, 100);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Fondo
            String rutaImagen = "src/main/resources/membrete.jpg";
            Image background = Image.getInstance(new File(rutaImagen).getAbsolutePath());
            background.setAbsolutePosition(0, 0);
            background.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
            writer.getDirectContentUnder().addImage(background);

            // Fuentes
            Font tituloFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
            Font subtituloFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
            Font cuerpoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
            Font encabezadoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
            Font correoFontAzul = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE);

            // Título
            Paragraph titulo = new Paragraph(
                    "EL SUSCRITO COORDINADOR DE LA FUNDACION INTERNACIONAL DE EDUCACION ELYON YIREH",
                    tituloFont
            );
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Subtítulo
            Paragraph subtitulo = new Paragraph(
                    "Con Licencia de funcionamiento otorgada por la secretaria de educación Distrital de Cartagena\n" +
                            "con resolución N°. 5749 del 20 de septiembre de 2022\n\n",
                    subtituloFont
            );
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20f);
            document.add(subtitulo);

            // Encabezado
            Paragraph encabezado = new Paragraph("HACE CONSTAR\n", encabezadoFont);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);

            // Cuerpo personalizado
            Paragraph cuerpo = new Paragraph(cuerpoTexto, cuerpoFont);
            cuerpo.setAlignment(Element.ALIGN_JUSTIFIED);
            cuerpo.setSpacingBefore(10f);
            document.add(cuerpo);

            // Pie de página
            Paragraph pie = new Paragraph();
            pie.setFont(cuerpoFont);
            pie.setSpacingBefore(30f);
            pie.setAlignment(Element.ALIGN_LEFT);

            LocalDate fechaActual = LocalDate.now();
            int dia = fechaActual.getDayOfMonth();
            String diaTexto = convertirNumeroATexto(dia);
            String mesTexto = fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            int anio = fechaActual.getYear();

            String fraseFecha = String.format(
                    "Para constancia se firma y sella en Cartagena de Indias a los %s (%d) días del mes de %s de %d.\n\n",
                    diaTexto,
                    dia,
                    mesTexto,
                    anio
            );

            pie.add(fraseFecha);
            pie.add("Atentamente,\n\n\n\n");
            pie.add("_________________________\n");
            pie.add("LIBIA MARCY LAVERDE ROJAS\n");
            pie.add("COORDINACION ACADEMICA\n");

            Chunk correo = new Chunk("E-MAIL academica@elyonyireh.edu.co", correoFontAzul);
            pie.add(correo);

            document.add(pie);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando constancia personalizada", e);
        }
    }

    private static String convertirNumeroATexto(int numero) {
        String[] texto = {
                "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez",
                "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho",
                "diecinueve", "veinte", "veintiuno", "veintidós", "veintitrés", "veinticuatro",
                "veinticinco", "veintiséis", "veintisiete", "veintiocho", "veintinueve", "treinta", "treinta y uno"
        };
        return (numero >= 1 && numero <= 31) ? texto[numero] : String.valueOf(numero);
    }
}

