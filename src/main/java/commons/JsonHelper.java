package commons;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

	private static ObjectMapper objectMapper;

	private static ObjectMapper getObjectMapper() {

		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}

	public static Object toObject(String json, Class<?> clazz) {

		Object object = null;
		try {
			object = json == null || json.isEmpty() ? null : getObjectMapper().readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static void tojson(File file, Object object) {

		try {
			if (file != null && file.exists() && object != null) {
				getObjectMapper().writeValue(file, object);
			}
		} catch (IOException e) {
		}
	}

	public static String pretty(final String value) throws IOException {

		String pretty = "";

		Object json = getObjectMapper().readValue(value == null ? "" : value, Object.class);
		pretty = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(json);

		return pretty;
	}

}
