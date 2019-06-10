package rs.raf.infview.util.io.hasher;

public class NoHasher implements Hasher {

    @Override
    public String hash(String content) {
        return content;
    }

    @Override
    public String dehash(String content) {
        return content;
    }
}