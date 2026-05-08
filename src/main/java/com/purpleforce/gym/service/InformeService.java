package com.purpleforce.gym.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.purpleforce.gym.model.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformeService {

    private static final Color MORADO = new Color(142, 68, 173);
    private static final Color MORADO_CLARO = new Color(187, 143, 206);
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Genera un PDF con el historial de reservas de un socio
     */
    public byte[] generarHistorialPDF(Usuario usuario, List<Reserva> reservas) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
        PdfWriter.getInstance(doc, baos);
        doc.open();

        // Cabecera
        Font fontTitulo = new Font(Font.HELVETICA, 22, Font.BOLD, MORADO);
        Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
        Font fontNormal = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
        Font fontBold = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);

        Paragraph titulo = new Paragraph("PurpleForce Gym", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);

        Paragraph subtitulo = new Paragraph("Historial de Reservas - " + usuario.getNombreCompleto(), fontSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingBefore(4);
        subtitulo.setSpacingAfter(20);
        doc.add(subtitulo);

        // Tabla de reservas
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3f, 2f, 2f, 2f});

        String[] cabeceras = {"Clase", "Fecha", "Hora", "Estado"};
        for (String cab : cabeceras) {
            PdfPCell cell = new PdfPCell(new Phrase(cab, fontBold));
            cell.setBackgroundColor(MORADO);
            cell.setPadding(8);
            cell.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(cell);
        }

        boolean fila = false;
        for (Reserva r : reservas) {
            Color bg = fila ? new Color(245, 235, 250) : Color.WHITE;
            fila = !fila;

            addCelda(tabla, r.getClase().getTipoClase().getNombre(), fontNormal, bg);
            addCelda(tabla, r.getClase().getFecha().format(FMT_FECHA), fontNormal, bg);
            addCelda(tabla, r.getClase().getHoraInicio().format(FMT_HORA), fontNormal, bg);

            Color colorEstado = r.getEstado().equals("CONFIRMADA") ? new Color(39, 174, 96) : Color.RED;
            Font fontEstado = new Font(Font.HELVETICA, 10, Font.BOLD, colorEstado);
            PdfPCell cellEstado = new PdfPCell(new Phrase(r.getEstado(), fontEstado));
            cellEstado.setBackgroundColor(bg);
            cellEstado.setPadding(6);
            cellEstado.setBorderColor(new Color(220, 220, 220));
            tabla.addCell(cellEstado);
        }

        doc.add(tabla);

        Paragraph pie = new Paragraph("Total de reservas: " + reservas.size(), fontNormal);
        pie.setSpacingBefore(15);
        doc.add(pie);

        doc.close();
        return baos.toByteArray();
    }

    /**
     * Genera CSV con el ranking de clases más populares
     */
    public byte[] generarRankingCSV(List<Object[]> ranking) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");

        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Posición", "Tipo de Clase", "Total Reservas"));

        int pos = 1;
        for (Object[] fila : ranking) {
            TipoClase tc = (TipoClase) fila[0];
            Long total = (Long) fila[1];
            printer.printRecord(pos++, tc.getNombre(), total);
        }

        printer.flush();
        writer.close();
        return baos.toByteArray();
    }

    /**
     * Genera un PDF con el horario semanal de un entrenador
     */
    public byte[] generarHorarioEntrenadorPDF(Entrenador entrenador, List<Clase> clases) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4.rotate(), 40, 40, 60, 40);
        PdfWriter.getInstance(doc, baos);
        doc.open();

        Font fontTitulo = new Font(Font.HELVETICA, 20, Font.BOLD, MORADO);
        Font fontSub = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);
        Font fontBold = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font fontNormal = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);

        Paragraph titulo = new Paragraph("PurpleForce Gym - Horario de Entrenador", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);

        Paragraph sub = new Paragraph("Entrenador: " + entrenador.getNombreCompleto()
                + " | Especialidad: " + entrenador.getEspecialidad(), fontSub);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingBefore(4);
        sub.setSpacingAfter(20);
        doc.add(sub);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 2f, 2f, 2f, 2f});

        for (String cab : new String[]{"Tipo de Clase", "Fecha", "Hora Inicio", "Hora Fin", "Sala"}) {
            PdfPCell c = new PdfPCell(new Phrase(cab, fontBold));
            c.setBackgroundColor(MORADO);
            c.setPadding(8);
            c.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(c);
        }

        boolean fila = false;
        for (Clase c : clases) {
            Color bg = fila ? new Color(245, 235, 250) : Color.WHITE;
            fila = !fila;
            addCelda(tabla, c.getTipoClase().getNombre(), fontNormal, bg);
            addCelda(tabla, c.getFecha().format(FMT_FECHA), fontNormal, bg);
            addCelda(tabla, c.getHoraInicio().format(FMT_HORA), fontNormal, bg);
            addCelda(tabla, c.getHoraFin().format(FMT_HORA), fontNormal, bg);
            addCelda(tabla, c.getSala() != null ? c.getSala() : "-", fontNormal, bg);
        }

        doc.add(tabla);
        doc.close();
        return baos.toByteArray();
    }

    private void addCelda(PdfPTable tabla, String texto, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        cell.setBorderColor(new Color(220, 220, 220));
        tabla.addCell(cell);
    }
}
