package picky.serializer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import picky.schema.Schema;

public interface ValueSerializer {
	
	public byte[] serialize(Schema schema, Map<String, Object> dataEntityMap) throws IOException;
	
	public void serialize(Schema schema, Map<String, Object> dataEntityMap, File file) throws IOException;

	public Map<String, Object> deserialize(Schema schema, byte[] bytes) throws IOException;

	public Map<String, Object> deserialize(Schema schema, File file) throws IOException;
	
}
