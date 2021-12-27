package picky.serializer;

import java.io.IOException;

import picky.node.Sharding;

public interface ShardingSerializer {
	
	public CharSequence serialize(Sharding... shardings) throws IOException;
	
	public Sharding[] deserialize(CharSequence shardingText) throws IOException;

}
