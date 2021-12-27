package picky.serializer;

import java.util.List;

import picky.schema.Schema;

public interface SchemaSerializer {

	public CharSequence serialize(List<Schema> schemas) throws Exception;
	
	public List<Schema> deserialize(CharSequence schemaText) throws Exception;
	
}
