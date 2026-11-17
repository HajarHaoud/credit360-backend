package ma.credit360.service.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimplePdfBuilder {

    private final List<String> objects = new ArrayList<>();
    private final StringBuilder contentStream = new StringBuilder();

    public SimplePdfBuilder() {
        // Init state
    }

    public void addText(String text, float x, float y, int fontSize, boolean bold) {
        String clean = sanitize(text);
        String fontRef = bold ? "/F2" : "/F1";
        contentStream.append("BT\n")
                .append(fontRef).append(" ").append(fontSize).append(" Tf\n")
                .append(x).append(" ").append(y).append(" Td\n")
                .append("(").append(clean).append(") Tj\n")
                .append("ET\n");
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        contentStream.append(x1).append(" ").append(y1).append(" m\n")
                .append(x2).append(" ").append(y2).append(" l\n")
                .append("S\n");
    }

    public void drawRect(float x, float y, float width, float height) {
        contentStream.append(x).append(" ").append(y).append(" ")
                .append(width).append(" ").append(height).append(" re\n")
                .append("S\n");
    }

    public void fillRect(float x, float y, float width, float height, float r, float g, float b) {
        contentStream.append(r).append(" ").append(g).append(" ").append(b).append(" rg\n")
                .append(x).append(" ").append(y).append(" ")
                .append(width).append(" ").append(height).append(" re\n")
                .append("f\n")
                .append("0 0 0 rg\n"); // reset black
    }

    public byte[] build() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("%PDF-1.4\n".getBytes(StandardCharsets.US_ASCII));

        List<Integer> offsets = new ArrayList<>();
        offsets.add(0); // 0th entry

        // 1: Catalog
        int catalogId = 1;
        // 2: Pages
        int pagesId = 2;
        // 3: Page
        int pageId = 3;
        // 4: Font Helvetica
        int fontHelvId = 4;
        // 5: Font Helvetica-Bold
        int fontHelvBoldId = 5;
        // 6: Content Stream
        int streamId = 6;

        byte[] streamBytes = contentStream.toString().getBytes(StandardCharsets.US_ASCII);

        // Object 1: Catalog
        offsets.add(out.size());
        out.write((catalogId + " 0 obj\n<< /Type /Catalog /Pages " + pagesId + " 0 R >>\nendobj\n").getBytes(StandardCharsets.US_ASCII));

        // Object 2: Pages
        offsets.add(out.size());
        out.write((pagesId + " 0 obj\n<< /Type /Pages /Kids [" + pageId + " 0 R] /Count 1 >>\nendobj\n").getBytes(StandardCharsets.US_ASCII));

        // Object 3: Page
        offsets.add(out.size());
        out.write((pageId + " 0 obj\n<< /Type /Page /Parent " + pagesId + " 0 R\n" +
                "/MediaBox [0 0 595.28 841.89]\n" + // A4 format in points
                "/Resources << /Font << /F1 " + fontHelvId + " 0 R /F2 " + fontHelvBoldId + " 0 R >> >>\n" +
                "/Contents " + streamId + " 0 R >>\nendobj\n").getBytes(StandardCharsets.US_ASCII));

        // Object 4: Font F1 (Helvetica)
        offsets.add(out.size());
        out.write((fontHelvId + " 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\nendobj\n").getBytes(StandardCharsets.US_ASCII));

        // Object 5: Font F2 (Helvetica-Bold)
        offsets.add(out.size());
        out.write((fontHelvBoldId + " 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>\nendobj\n").getBytes(StandardCharsets.US_ASCII));

        // Object 6: Stream
        offsets.add(out.size());
        out.write((streamId + " 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n").getBytes(StandardCharsets.US_ASCII));
        out.write(streamBytes);
        out.write("\nendstream\nendobj\n".getBytes(StandardCharsets.US_ASCII));

        // XRef
        int xrefOffset = out.size();
        out.write(("xref\n0 " + (streamId + 1) + "\n").getBytes(StandardCharsets.US_ASCII));
        out.write("0000000000 65535 f \n".getBytes(StandardCharsets.US_ASCII));
        for (int i = 1; i <= streamId; i++) {
            String off = String.format("%010d 00000 n \n", offsets.get(i));
            out.write(off.getBytes(StandardCharsets.US_ASCII));
        }

        // Trailer
        out.write(("trailer\n<< /Size " + (streamId + 1) + " /Root " + catalogId + " 0 R >>\n").getBytes(StandardCharsets.US_ASCII));
        out.write("startxref\n".getBytes(StandardCharsets.US_ASCII));
        out.write((xrefOffset + "\n%%EOF\n").getBytes(StandardCharsets.US_ASCII));

        return out.toByteArray();
    }

    private String sanitize(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("à", "a")
                .replace("ç", "c")
                .replace("ù", "u")
                .replace("°", " deg");
    }
}
