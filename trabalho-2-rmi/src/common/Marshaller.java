package common;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Marshaller {

    private Marshaller() {}

    

    public static byte[] marshal(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (XMLEncoder encoder = new XMLEncoder(bos)) {
                encoder.setExceptionListener(e -> {
                    throw new RuntimeException(e);
                });
                encoder.writeObject(obj);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao empacotar (marshal): " + e.getMessage(), e);
        }
    }

    

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) return null;
        try (XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(bytes))) {
            Object obj = decoder.readObject();
            if (obj == null) return null;
            return clazz.cast(obj);
        } catch (Exception e) {
            String sample = new String(bytes, 0, Math.min(bytes.length, 120), StandardCharsets.UTF_8);
            throw new RuntimeException("Erro ao desempacotar (unmarshal): "
                    + e.getMessage() + " | amostra=" + sample, e);
        }
    }

    
    public static Object unmarshal(byte[] bytes) {
        return unmarshal(bytes, Object.class);
    }
}
