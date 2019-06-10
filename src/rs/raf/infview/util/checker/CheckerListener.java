package rs.raf.infview.util.checker;

interface CheckerListener {

    void onError(String message);
    void onSuccess();
}