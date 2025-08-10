package certificadosAdmisionBackend.util;


import certificadosAdmisionBackend.dto.EstudianteDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneradorConstanciaNotasPdf {

    public static byte[] generarPdfConstanciaNotas(List<EstudianteDto> todasLasNotas, int nivelDeseado, String cuerpoPersonalizado,String infoPrograma) {
        try {
            Rectangle pageSize = new Rectangle(612, 792);
            Document document = new Document(pageSize, 50, 50, 90, 100);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            agregarFondo(writer, pageSize);
            Font[] fuentes = inicializarFuentes();

            List<EstudianteDto> notasFiltradas = filtrarNotasPorNivel(todasLasNotas, nivelDeseado);
            if (notasFiltradas.isEmpty()) {
                document.add(new Paragraph("No hay notas para el nivel " + nivelDeseado, fuentes[0]));
                document.close();
                return baos.toByteArray();
            }

            EstudianteDto est = notasFiltradas.get(0);

            document.add(crearTitulo(fuentes[2]));
            document.add(crearSubtitulo(fuentes[0]));
            document.add(crearEncabezadoCertificaQue(fuentes[1]));
            document.add(crearCuerpo(est, fuentes[0], fuentes[1], cuerpoPersonalizado));
            document.add(crearTablaNotas(notasFiltradas, nivelDeseado, fuentes[3], fuentes[4]));
            document.add(crearInformacionAdicional(fuentes[0], infoPrograma));
            document.add(crearPieDePagina(fuentes[0]));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando constancia de notas", e);
        }
    }

    private static void agregarFondo(PdfWriter writer, Rectangle pageSize) throws Exception {
        String rutaImagen = "src/main/resources/membrete.jpg";
        Image background = Image.getInstance(new File(rutaImagen).getAbsolutePath());
        background.setAbsolutePosition(0, 0);
        background.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
        writer.getDirectContentUnder().addImage(background);
    }

    private static Font[] inicializarFuentes() {
        Font cuerpoFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font negritaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font encabezadoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Font filaFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        return new Font[]{cuerpoFont, negritaFont, encabezadoFont, headerFont, filaFont};
    }

    private static List<EstudianteDto> filtrarNotasPorNivel(List<EstudianteDto> todasLasNotas, int nivelDeseado) {
        return todasLasNotas.stream()
                .filter(n -> Objects.equals(n.getNivel(), nivelDeseado))
                .collect(Collectors.toList());
    }

    private static Paragraph crearTitulo(Font encabezadoFont) {
        Paragraph titulo = new Paragraph("EL SUSCRITO COORDINADOR DE LA FUNDACIÓN INTERNACIONAL DE EDUCACIÓN ELYON YIREH\n", encabezadoFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        return titulo;
    }

    private static Paragraph crearSubtitulo(Font cuerpoFont) {
        Paragraph subtitulo = new Paragraph(
                "Con Licencia de funcionamiento otorgada por la Secretaría de Educación Distrital de Cartagena con resolución N°. 5749 del 20 de septiembre de 2022\n\n",
                cuerpoFont
        );
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        return subtitulo;
    }

    private static Paragraph crearEncabezadoCertificaQue(Font negritaFont) {
        Paragraph cert = new Paragraph("CERTIFICA QUE\n", negritaFont);
        cert.setAlignment(Element.ALIGN_CENTER);
        cert.setSpacingAfter(5f);
        return cert;
    }

    private static Paragraph crearCuerpo(
            EstudianteDto est,
            Font cuerpoFont,
            Font negritaFont,
            String cuerpoPersonalizado
    ) {
        Paragraph cuerpo = new Paragraph();
        cuerpo.setFont(cuerpoFont);
        cuerpo.setAlignment(Element.ALIGN_JUSTIFIED);
        cuerpo.setSpacingBefore(10f);

        if (cuerpoPersonalizado != null && !cuerpoPersonalizado.isBlank()) {
            // Usar el texto personalizado que el usuario escribió en el modal
            cuerpo.add(cuerpoPersonalizado);
            cuerpo.add("\n\n");
        } else {
            // Texto por defecto si el usuario no escribió nada
            Chunk nombreEstudiante = new Chunk(est.getEstudiante().toUpperCase(), negritaFont);
            String documento = est.getTipoDocumento().toUpperCase() + " " + est.getCodigo().toUpperCase();
            Chunk documentoChunk = new Chunk(documento, negritaFont);

            cuerpo.add("Que ");
            cuerpo.add(nombreEstudiante);
            cuerpo.add(", identificado con ");
            cuerpo.add(documentoChunk);
            cuerpo.add(" de " + est.getLugarExpedicionDocumento() + ", cursó y aprobó satisfactoriamente el ciclo (semestre) " + est.getSemestre());
            cuerpo.add(" en esta institución en el programa Técnico Laboral por Competencias en ");
            cuerpo.add(new Chunk(est.getProgramaTecnico().toUpperCase(), negritaFont));
            cuerpo.add(", en la modalidad " + est.getHorario().toLowerCase() + " jornada " + est.getHorario().toLowerCase());
            cuerpo.add(" periodo B 2025. Donde obtuvo las siguientes notas:\n\n");
        }

        return cuerpo;
    }

    private static PdfPTable crearTablaNotas(List<EstudianteDto> notas, int nivel,
                                             Font headerFont, Font filaFont) throws DocumentException {

        // Reducimos el ancho de la tabla
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidths(new float[]{1.2f, 6f, 1.8f});
        tabla.setWidthPercentage(85);  // Antes estaba al 100%
        tabla.setSpacingBefore(8f);    // Espacio superior reducido

        // Encabezados
        Stream.of("PERIODO", "MÓDULOS", "NOTAS").forEach(titulo -> {
            PdfPCell celda = new PdfPCell(new Phrase(titulo, headerFont));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setPadding(4f);  // Padding más pequeño
            celda.setBorderWidth(0.8f);  // Bordes más finos
            tabla.addCell(celda);
        });

        // Eliminar duplicados
        Set<String> modulosUnicos = new LinkedHashSet<>();
        List<EstudianteDto> modulosFiltrados = notas.stream()
                .filter(n -> n.getAsignatura() != null && n.getNotaDefinitiva() != null)
                .filter(n -> modulosUnicos.add(n.getAsignatura().trim() + "|" + n.getNotaDefinitiva()))
                .toList();

        double sumaNotas = 0.0;

        // Obtener el nivel y convertirlo a romano
        String periodoTexto = modulosFiltrados.isEmpty() || modulosFiltrados.get(0).getNivel() == null
                ? ""
                : convertirNivelARomano(modulosFiltrados.get(0).getNivel());

        // Celda de PERIODO combinada verticalmente
        if (!modulosFiltrados.isEmpty()) {
            PdfPCell celdaPeriodo = new PdfPCell(new Phrase(periodoTexto, filaFont));
            celdaPeriodo.setRowspan(modulosFiltrados.size());
            celdaPeriodo.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaPeriodo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaPeriodo.setPadding(3f);
            celdaPeriodo.setBorderWidth(0.8f);
            tabla.addCell(celdaPeriodo);
        }

        // Celdas de módulos y notas
        for (EstudianteDto dto : modulosFiltrados) {
            // Módulo
            PdfPCell celdaModulo = new PdfPCell(new Phrase(dto.getAsignatura(), filaFont));
            celdaModulo.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaModulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaModulo.setPadding(3f);
            celdaModulo.setBorderWidth(0.8f);
            tabla.addCell(celdaModulo);

            // Nota
            double nota = dto.getNotaDefinitiva();
            PdfPCell celdaNota = new PdfPCell(new Phrase(String.format(Locale.US, "%.2f", nota), filaFont));
            celdaNota.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaNota.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaNota.setPadding(3f);
            celdaNota.setBorderWidth(0.8f);
            tabla.addCell(celdaNota);

            sumaNotas += nota;
        }

        // PROMEDIO final
        double promedio = modulosFiltrados.isEmpty() ? 0.0 : sumaNotas / modulosFiltrados.size();
        BigDecimal promedioRedondeado = BigDecimal.valueOf(promedio).setScale(2, RoundingMode.HALF_UP);
        String promedioTexto = promedioRedondeado.toPlainString();

        // Celda vacía para alinear con "PERIODO"
        PdfPCell celdaVacia = new PdfPCell(new Phrase(""));
        celdaVacia.setBorderWidth(0.8f);
        celdaVacia.setPadding(3f);
        tabla.addCell(celdaVacia);

        // Celda PROMEDIO
        PdfPCell celdaPromedioTexto = new PdfPCell(new Phrase("PROMEDIO", headerFont));
        celdaPromedioTexto.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaPromedioTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaPromedioTexto.setPadding(3f);
        celdaPromedioTexto.setBorderWidth(0.8f);
        tabla.addCell(celdaPromedioTexto);

        PdfPCell celdaPromedioValor = new PdfPCell(new Phrase(promedioTexto, headerFont));
        celdaPromedioValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaPromedioValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaPromedioValor.setPadding(3f);
        celdaPromedioValor.setBorderWidth(0.8f);
        tabla.addCell(celdaPromedioValor);

        return tabla;
    }

    // Método auxiliar para convertir entero a número romano
    public static String convertirNivelARomano(Integer nivel) {
        return switch (nivel) {
            case 1 -> "PERIODO-I";
            case 2 -> "PERIODO-II";
            case 3 -> "PERIODO-III";
            case 4 -> "PERIODO-IV";
            default -> nivel != null ? nivel.toString() : "";
        };
    }

    private static Paragraph crearInformacionAdicional(Font cuerpoFont, String infoPrograma) {
        Paragraph info = new Paragraph();
        info.setFont(cuerpoFont);
        info.setSpacingBefore(10f);

        if (infoPrograma != null && !infoPrograma.isBlank()) {
            info.add(infoPrograma); // Usa el texto que viene del frontend
        } else {
            info.add("\nDuración de programa: 2 años\n");
            info.add("Intensidad horaria del programa: 1.358 horas\n");
            info.add("Intensidad horaria del programa: 24 horas semanales\n");
        }

        return info;
    }

    private static Paragraph crearPieDePagina(Font cuerpoFont) {
        Paragraph pie = new Paragraph();
        pie.setFont(cuerpoFont);
        pie.setSpacingBefore(15f);
        pie.setAlignment(Element.ALIGN_JUSTIFIED);

        LocalDate fecha = LocalDate.now();
        int dia = fecha.getDayOfMonth();
        String mes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int anio = fecha.getYear();

        pie.add(String.format(
                "\nPara constancia se firma y sella en Cartagena de Indias a los %s (%d) días del mes de %s de %d",
                convertirNumeroATexto(dia), dia, mes, anio
        ));

        pie.add("\n\nAtentamente,\n\n");
        pie.add("_______________________________\n");
        pie.add("LIBIA MARCY LAVERDE ROJAS\n");
        pie.add("COORDINACION ACADEMICA\n");
        pie.add("E-MAIL academica@elyonyireh.edu.co\n");
        return pie;

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