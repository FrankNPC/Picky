package picky.key.block;

import java.io.IOException;

import picky.common.LocalStorage;
import picky.schema.Field;
import picky.schema.FieldType;
import picky.schema.Key;

/**
 * @author FrankNPC
 *
 */
public final class DataBlockBuilder {
	
//	public static String LineSeperator = "\n";
	
	private DataBlockBuilder() {}
	
	public static DataBlock<?> buildDataBlock(String absolutePath, Key<?> key) {
		FieldType fieldType = key.getFields().length==1?key.getFields()[0].getFieldType():FieldType.String;
		for(Field f : key.getFields()) {
			if (fieldType==FieldType.String&&f.getFieldType()!=FieldType.String) {
				fieldType = FieldType.Bytes;
			}
		}

		final byte[] originals;
		try {
			originals = LocalStorage.getInstance().read(absolutePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		switch (fieldType) {
		case Int: return new DataBlockInt(absolutePath, originals);
		case Long: return new DataBlockLong(absolutePath, originals);
		case String: return new DataBlockString(absolutePath, originals);
		case Bytes: return new DataBlockBytes(absolutePath, originals);
		case Boolean:
		case Byte:
		case Float:
		case Double:
			default:break;
		}
		return null;
	}

}

