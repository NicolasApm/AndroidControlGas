package devices;

public interface BTCallback {

    void onNext(String data, boolean flag, String Trama);
    void onError(Exception e);
}
