package picky.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import picky.key.KeyBlockManager;
import picky.node.Role;
import picky.node.Sharding;
import picky.schema.SchemaManager;
import picky.schema.SerializeHelper;

public final class DefaultShardingSerializer implements ShardingSerializer {

	@Override
	public CharSequence serialize(Sharding... shardings) throws IOException {
		StringBuilder buffer = new StringBuilder();
		for(Sharding sharding : shardings) {
			buffer.append("{\n");
			buffer.append("NodeName:\"");
			buffer.append(sharding.getNodeName());
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
	public Sharding[] deserialize(CharSequence shardingText) throws IOException {
		List<Sharding> shardingList = new ArrayList<>();
		for(int i=0,j=0; i<shardingText.length(); i++) {
			switch(shardingText.charAt(i)) {
			case '{':
				Sharding sharding = new Sharding();
				shardingList.add(sharding);
				if ("NodeName:\"".equals(shardingText.subSequence(i, i+10))) {
					j=i+10;
					while (shardingText.charAt(i)!='"') {i++;continue;}
					sharding.setNodeName(shardingText.subSequence(j, i).toString().trim());
					i+=2;
				}
				if ("SchemaName:\"".equals(shardingText.subSequence(i, i+12))) {
					j=i+12;
					while (shardingText.charAt(i)!='"') {i++;continue;}
					sharding.setSchemaName(shardingText.subSequence(j, i).toString().trim());
					i+=2;
				}
				if ("Role:\"".equals(shardingText.subSequence(i, i+6))) {
					j=i+6;
					while (shardingText.charAt(i)!='"') {i++;continue;}
					sharding.setRole(Role.forName(shardingText.subSequence(j, i).toString().trim()));
					i+=2;
				}
				if ("Key:\"".equals(shardingText.subSequence(i, i+5))) {
					j=i+5;
					while (shardingText.charAt(i)!='"') {i++;continue;}
					sharding.setKeyBlock(KeyBlockManager.getInstance(
							SchemaManager.getInstance().getSchemas(sharding.getSchemaName()))
								.getKeyBlock(shardingText.subSequence(j, i).toString().trim()));
					i+=2;
				}
				i+=1;
				
			}
		}
		
		return shardingList.toArray(new Sharding[shardingList.size()]);
	}

}
