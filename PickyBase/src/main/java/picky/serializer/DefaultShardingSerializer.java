package picky.serializer;

import java.io.IOException;

import picky.node.Sharding;
import picky.schema.SerializeHelper;

public final class DefaultShardingSerializer implements ShardingSerializer {

	@Override
	public CharSequence serialize(Sharding<?>... shardings) throws IOException {
		StringBuilder buffer = new StringBuilder();
		for(Sharding<?> sharding : shardings) {
			buffer.append("{\n");
			buffer.append("HostName:\"");
			buffer.append(sharding.getHostName());
			buffer.append("\",");
			buffer.append("SchemaName:\"");
			buffer.append(sharding.getSchemaName());
			buffer.append("\",");
			buffer.append("Role:\"");
			buffer.append(sharding.getRole().toString());
			buffer.append("\",");
			buffer.append("Key:\"");
			buffer.append(SerializeHelper.serializeKey(sharding.getKeyBlock().getKey(), ""));
			buffer.append("\"},");
		}
		SerializeHelper.removeEndComma(buffer);
		return buffer;
	}

	
	@Override
	public Sharding<?>[] deserialize(CharSequence shardingText) throws IOException {
		
		
		
		
		
		return null;
	}


}
