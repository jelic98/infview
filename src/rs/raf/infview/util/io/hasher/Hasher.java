package rs.raf.infview.util.io.hasher;

public interface Hasher {

    String hash(String content);
    String dehash(String content);
}