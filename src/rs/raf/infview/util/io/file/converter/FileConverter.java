package rs.raf.infview.util.io.file.converter;

interface FileConverter<F1, F2> {

    String EXTENSION = ".converted";

    F2 convert(F1 file);
}