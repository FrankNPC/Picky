package picky.serializer;

import java.io.File;
import java.io.IOException;

import picky.key.KeyBlock;
import picky.schema.Schema;

public class DefaultPrimaryKeyBlockSerializer implements KeyBlockSerializer{

	@Override
	public byte[] serialize(Schema schema, KeyBlock<?> keyBlock) throws IOException {
		// ?ODO Auto-generated method stub
		return null;
	}

	@Override
	public void serialize(Schema schema, KeyBlock<?> keyBlock, File parentFolder) throws IOException {
		// ?ODO Auto-generated method stub
	}

	@Override
	public KeyBlock<?> deserialize(Schema schema, byte[] bytes) throws IOException {
		// ?ODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyBlock<?> deserialize(Schema schema, File parentFolder) throws IOException {
		// ?ODO Auto-generated method stub
		return null;
	}
	

}
