package picky.test;

import org.junit.Test;

import picky.serializer.DefaultSchemaSerializer;
import picky.serializer.SchemaSerializer;

public class DefaultSchemaSerializerTest {

	@Test
	public void test() {
		// exe script node_src_1 node_dst_1 TokenID ClientTimeStamp(lenth/8) JavaByteCode/json(length/4)

		String schemaText = ""
				+"product {"
				+"  id: AutoIncrement(long),"
				+"  name: string,"
				+"  amount: double,"
				+"  status: atomic(byte),"
				+"  description: string,"
				+"  tags: [{tag: string}],"
				+"  sku: {"
				+"	id: AutoIncrement(long),"
				+"	name: string,"
				+"	quantity: lock(int),"
				+"	price: float,"
				+"	active: atomic(boolean),"
				+"  },"
				+"  PrimaryKey('id,sku.price',1000,dGhpcyBpcyBhIGV4YW1wbGU=),"
				+"}"
				+"sproduct {"
				+"  id: AutoIncrement(long),"
				+"  amount: double,"
				+"  status: atomic(byte),"
				+"  name: string,"
				+"  description: string,"
				+"  tags: [{tag: string}],"
				+"  sku: [{"
				+"	id: AutoIncrement(long),"
				+"	name: string,"
				+"	quantity: lock(int),"
				+"	price: float,"
				+"	active: atomic(boolean),"
				+"  }],"
				+"  PrimaryKey('id',1000,100000),"
				+"}"
				
		;
		
		try {
			SchemaSerializer schemaSerializer = new DefaultSchemaSerializer();
			System.out.println(schemaSerializer.serialize(schemaSerializer.deserialize(schemaText)));
		} catch (Exception e) {
			e.printStackTrace();
		}
  	
	}
}
