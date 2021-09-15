package picky.serializer;

import java.io.File;
import java.io.IOException;

import picky.key.KeyBlock;
import picky.schema.Schema;

public interface KeyBlockSerializer {
	
	public byte[] serialize(Schema schema, KeyBlock<?> keyBlock) throws IOException;
	
	public void serialize(Schema schema, KeyBlock<?> keyBlock, File parentFolder) throws IOException;

	public KeyBlock<?> deserialize(Schema schema, byte[] bytes) throws IOException;

	public KeyBlock<?> deserialize(Schema schema, File parentFolder) throws IOException;
}
