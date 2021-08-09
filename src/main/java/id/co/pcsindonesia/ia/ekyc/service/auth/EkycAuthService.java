package id.co.pcsindonesia.ia.ekyc.service.auth;

import java.io.IOException;

public interface EkycAuthService<T> {
    public T auth() throws IOException;
}
