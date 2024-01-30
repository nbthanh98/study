package com.thanhnb.jwtauth;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        convertDocxToPdf("templates/t1.docx");
    }

    public static void convertDocxToPdf(String filename) throws IOException, InvalidFormatException {
        InputStream doc = getFileFromResourceAsStream(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        XWPFDocument document = new XWPFDocument(doc);
        PdfOptions options = PdfOptions.create();
        PdfConverter.getInstance().convert(document, baos, options);

        writeFile("test.pdf", baos);
    }

    private static InputStream getFileFromResourceAsStream(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        return new FileInputStream(resource.getFile());
    }

    private static void writeFile(String fileName, ByteArrayOutputStream byteArrayOutputStream) {
        try(OutputStream outputStream = new FileOutputStream(fileName)) {
            byteArrayOutputStream.writeTo(outputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
