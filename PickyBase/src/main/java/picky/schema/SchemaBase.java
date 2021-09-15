package picky.schema;

/**
 * 
 * @author FrankNPC
 *
 */
public class SchemaBase{

	private String[] fieldsOrder;
	
	private String name;

	private Field[] fields;
	
	private SchemaBase[] schemaBases;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public String[] getFieldsOrder() {
		return fieldsOrder;
	}

	public void setFieldsOrder(String[] fieldsOrder) {
		this.fieldsOrder = fieldsOrder;
	}

	public SchemaBase[] getSchemaBases() {
		return schemaBases;
	}

	public void setSchemaBases(SchemaBase[] schemaBases) {
		this.schemaBases = schemaBases;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		if (name!=null) {hash^=name.hashCode();}
		for(String s : fieldsOrder) {hash^=s.hashCode();}
		for(Field f : fields) {hash^=f.hashCode();}
		for(SchemaBase s : schemaBases) {hash^=s.hashCode();}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {return false;}
		if (this == obj) {return true;}
		if (!(obj instanceof SchemaBase)||hashCode()!=obj.hashCode()) {return false;}
		SchemaBase schemaBase = (SchemaBase) obj;

		if (name==null^schemaBase.name==null) {return false;}
		if (fieldsOrder==null^schemaBase.fieldsOrder==null) {return false;}
		if (fields==null^schemaBase.fields==null) {return false;}
		if (schemaBases==null^schemaBase.schemaBases==null) {return false;}

		if (name!=null&&schemaBase.name!=null&&!name.equals(schemaBase.name)) {return false;}
		
		if (fieldsOrder!=null&&schemaBase.fieldsOrder!=null) {
			if (fieldsOrder.length!=schemaBase.fieldsOrder.length) {return false;}
			for(int i=0; i<fieldsOrder.length; i++) {
				if (!fieldsOrder[i].equals(schemaBase.fieldsOrder[i])) {return false;}
			}
		}
		if (fields!=null&&schemaBase.fields!=null) {
			if (fields.length!=schemaBase.fields.length) {return false;}
			for(int i=0; i<fields.length; i++) {
				if (!fields[i].equals(schemaBase.fields[i])) {return false;}
			}
		}
		if (schemaBases!=null&&schemaBase.schemaBases!=null) {
			if (schemaBases.length!=schemaBase.schemaBases.length) {return false;}
			for(int i=0; i<schemaBases.length; i++) {
				if (!schemaBases[i].equals(schemaBase.schemaBases[i])) {return false;}
			}
		}
		return true;
	}
}
