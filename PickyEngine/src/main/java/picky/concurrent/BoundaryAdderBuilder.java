package picky.concurrent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import picky.common.CompareUtils;
import picky.schema.FieldType;

public final class BoundaryAdderBuilder{

	private BoundaryAdderBuilder() {}

	public static BoundaryAdder<?> buildDataBlock(FieldType fieldType, Object initialObj, Object minObj, Object maxObj, Object incrementObj) {
		switch (fieldType) {
		case Boolean:return null;
		case Byte:
			
			return new BoundaryAdder<Byte>() {
				private volatile AtomicInteger value = new AtomicInteger();
				private final byte MIN_VALUE = (byte) minObj, MAX_VALUE = (byte) maxObj, increment = (byte) incrementObj;
				{
					set((byte) initialObj);
				}

				private int get(int newValue) {
					if (newValue<MIN_VALUE) { return MIN_VALUE; } else if (newValue>MAX_VALUE) { return MAX_VALUE; }
					return newValue;
				}
				private int oprand(int newValue, int delta) {
					newValue+=delta;
					if (newValue<=MIN_VALUE||MAX_VALUE<=newValue) {newValue-=delta;}
					return newValue;
				}
				
				@Override
				public Byte get() {
					return (byte) value.get();
				}

				@Override
				public void set(Byte newValue) {
					value.set(get(newValue));
				}

				@Override
				public void set(AdderUnaryOperator<Byte> adderUnaryOperator) {
					value.updateAndGet((v)->get(adderUnaryOperator.apply((byte) v)));
				}

				@Override
				public Byte getAndSet(Byte newValue) {
					return (byte) value.getAndSet(get(newValue));
				}

				@Override
				public boolean compareAndSet(Byte expect, Byte update) {
					return value.compareAndSet(expect, get(update));
				}

				@Override
				public boolean increment(Byte delta) {
					return get() != value.updateAndGet((v)->oprand(v, (int)delta));
				}
				
				@Override
				public boolean decrement(Byte delta) {
					return get() != value.updateAndGet((v)->oprand(v, -(int)delta));
				}
				
				@Override
				public Byte getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public Byte incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public Byte getAndDecrement() {
					return (byte) value.getAndUpdate((v)->oprand(v, -increment));
				}

				@Override
				public Byte decrementAndGet() {
					return (byte) value.updateAndGet((v)->oprand(v, -increment));
				}

				@Override
				public Byte getAndAdd(Byte delta) {
					return (byte) value.getAndUpdate((v)->oprand(v, delta));
				}

				@Override
				public Byte addAndGet(Byte delta) {
					return (byte) value.updateAndGet((v)->oprand(v, delta));
				}
			};
		case Int:
			return new BoundaryAdder<Integer>() {
				private volatile AtomicInteger value = new AtomicInteger();
				private final int MIN_VALUE = (int) minObj, MAX_VALUE = (int) maxObj, increment = (int) incrementObj;
				{
					set((int) initialObj);
				}

				private int get(long newValue) {
					if (newValue<MIN_VALUE) { return MIN_VALUE; } else if (newValue>MAX_VALUE) { return MAX_VALUE; }
					return (int) newValue;
				}
				private int oprand(long newValue, long delta) {
					newValue+=delta;
					if (newValue<=MIN_VALUE||MAX_VALUE<=newValue) {newValue-=delta;}
					return (int) newValue;
				}
				
				@Override
				public Integer get() {
					return value.get();
				}

				@Override
				public void set(Integer newValue) {
					value.set(get(newValue));
				}

				@Override
				public void set(AdderUnaryOperator<Integer> adderUnaryOperator) {
					value.updateAndGet((v)->get(adderUnaryOperator.apply(v)));
				}

				@Override
				public Integer getAndSet(Integer newValue) {
					return value.getAndSet(get(newValue));
				}

				@Override
				public boolean compareAndSet(Integer expect, Integer update) {
					return value.compareAndSet(expect, get(update));
				}

				@Override
				public boolean increment(Integer delta) {
					return get() != value.updateAndGet((v)->oprand(v, (long)delta));
				}
				
				@Override
				public boolean decrement(Integer delta) {
					return get() != value.updateAndGet((v)->oprand(v, -(long)delta));
				}

				@Override
				public Integer getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public Integer incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public Integer getAndDecrement() {
					return value.getAndUpdate((v)->{return oprand(v, -increment);});
				}

				@Override
				public Integer decrementAndGet() {
					return value.updateAndGet((v)->{return oprand(v, -increment);});
				}

				@Override
				public Integer getAndAdd(Integer delta) {
					return value.getAndUpdate((v)->{return oprand(v, delta);});
				}

				@Override
				public Integer addAndGet(Integer delta) {
					return value.updateAndGet((v)->{return oprand(v, delta);});
				}
			};
		case Long:
			return new BoundaryAdder<Long>() {
				private volatile AtomicLong value = new AtomicLong();
				private final long MIN_VALUE = (long) minObj, MAX_VALUE = (long) maxObj, increment = (long) incrementObj;
				{
					set((long) initialObj);
				}

				private long get(long newValue) {
					if (newValue<MIN_VALUE) { return MIN_VALUE; } else if (newValue>MAX_VALUE) { return MAX_VALUE; }
					return newValue;
				}
				private long oprand(long newValue, long delta) {
					newValue+=delta;
					if (newValue<=MIN_VALUE||MAX_VALUE<=newValue) {newValue-=delta;}
					return newValue;
				}
				
				@Override
				public Long get() {
					return value.get();
				}

				@Override
				public void set(Long newValue) {
					value.set(get(newValue));
				}

				@Override
				public void set(AdderUnaryOperator<Long> adderUnaryOperator) {
					value.updateAndGet((v)->get(adderUnaryOperator.apply(v)));
				}
				
				@Override
				public Long getAndSet(Long newValue) {
					return value.getAndSet(get(newValue));
				}

				@Override
				public boolean compareAndSet(Long expect, Long update) {
					return value.compareAndSet(expect, get(update));
				}

				@Override
				public boolean increment(Long delta) {
					return get() != addAndGet(delta);
				}
				
				@Override
				public boolean decrement(Long delta) {
					return get() != addAndGet(-delta);
				}

				@Override
				public Long getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public Long incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public Long getAndDecrement() {
					return value.getAndUpdate((v)->{return oprand(v, -increment);});
				}

				@Override
				public Long decrementAndGet() {
					return value.updateAndGet((v)->{return oprand(v, -increment);});
				}

				@Override
				public Long getAndAdd(Long delta) {
					return value.getAndUpdate((v)->{return oprand(v, delta);});
				}

				@Override
				public Long addAndGet(Long delta) {
					return value.updateAndGet((v)->{return oprand(v, delta);});
				}
			};
		case Float:
			return new BoundaryAdder<Float>() {
				private volatile AtomicInteger value = new AtomicInteger();
				private final float MIN_VALUE = (float) minObj, MAX_VALUE = (float) maxObj, increment = (float) incrementObj;
				{
					set((float) initialObj);
				}

				private float get(double newValue) {
					if (newValue<MIN_VALUE) { return MIN_VALUE; } else if (newValue>MAX_VALUE) { return MAX_VALUE; }
					return (float) newValue;
				}
				private float oprand(double newValue, double delta) {
					newValue+=delta;
					if (newValue<=MIN_VALUE||MAX_VALUE<=newValue) {newValue-=delta;}
					return (float) newValue;
				}
				
				@Override
				public Float get() {
					return Float.intBitsToFloat(value.get());
				}

				@Override
				public void set(Float newValue) {
					value.set(Float.floatToIntBits(get(newValue)));
				}

				@Override
				public void set(AdderUnaryOperator<Float> adderUnaryOperator) {
					value.updateAndGet((v)->Float.floatToIntBits(get(adderUnaryOperator.apply(Float.intBitsToFloat(v)))));
				}
				
				@Override
				public Float getAndSet(Float newValue) {
					return Float.intBitsToFloat(value.getAndSet(Float.floatToIntBits(get(newValue))));
				}

				@Override
				public boolean compareAndSet(Float expect, Float update) {
					return value.compareAndSet(Float.floatToIntBits(expect), Float.floatToIntBits(get(update)));
				}

				@Override
				public boolean increment(Float delta) {
					return get() != addAndGet(delta);
				}
				
				@Override
				public boolean decrement(Float delta) {
					return get() != addAndGet(-delta);
				}

				@Override
				public Float getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public Float incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public Float getAndDecrement() {
					return Float.intBitsToFloat(value.getAndUpdate((v)->{return Float.floatToIntBits(oprand(Float.intBitsToFloat(v), -(double)increment));}));
				}

				@Override
				public Float decrementAndGet() {
					return Float.intBitsToFloat(value.updateAndGet((v)->{return Float.floatToIntBits(oprand(Float.intBitsToFloat(v), -(double)increment));}));
				}

				@Override
				public Float getAndAdd(Float delta) {
					return Float.intBitsToFloat(value.getAndUpdate((v)->{return Float.floatToIntBits(oprand(Float.intBitsToFloat(v), delta));}));
				}

				@Override
				public Float addAndGet(Float delta) {
					return Float.intBitsToFloat(value.updateAndGet((v)->{return Float.floatToIntBits(oprand(Float.intBitsToFloat(v), delta));}));
				}
			};
		case Double:
			return new BoundaryAdder<Double>() {
				private volatile AtomicLong value = new AtomicLong();
				private final double MIN_VALUE = (double) minObj, MAX_VALUE = (double) maxObj, increment = (double) incrementObj;
				{
					set((double) initialObj);
				}

				private double get(double newValue) {
					if (newValue<MIN_VALUE) { return MIN_VALUE; } else if (newValue>MAX_VALUE) { return MAX_VALUE; }
					return newValue;
				}
				private double oprand(double newValue, double delta) {
					newValue+=delta;
					if (newValue<=MIN_VALUE||MAX_VALUE<=newValue) {newValue-=delta;}
					return newValue;
				}
				
				@Override
				public Double get() {
					return Double.longBitsToDouble(value.get());
				}

				@Override
				public void set(Double newValue) {
					value.set(Double.doubleToLongBits(get(newValue)));
				}

				@Override
				public void set(AdderUnaryOperator<Double> adderUnaryOperator) {
					value.updateAndGet((v)->Double.doubleToLongBits(get(adderUnaryOperator.apply(Double.longBitsToDouble(v)))));
				}
				
				@Override
				public Double getAndSet(Double newValue) {
					return Double.longBitsToDouble(value.getAndSet(Double.doubleToLongBits(get(newValue))));
				}

				@Override
				public boolean compareAndSet(Double expect, Double update) {
					return value.compareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(get(update)));
				}

				@Override
				public boolean increment(Double delta) {
					return get() != addAndGet(delta);
				}
				
				@Override
				public boolean decrement(Double delta) {
					return get() != addAndGet(-delta);
				}

				@Override
				public Double getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public Double incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public Double getAndDecrement() {
					return Double.longBitsToDouble(value.getAndUpdate((v)->{return Double.doubleToLongBits(oprand(Double.longBitsToDouble(v), -increment));}));
				}

				@Override
				public Double decrementAndGet() {
					return Double.longBitsToDouble(value.updateAndGet((v)->{return Double.doubleToLongBits(oprand(Double.longBitsToDouble(v), -increment));}));
				}

				@Override
				public Double getAndAdd(Double delta) {
					return Double.longBitsToDouble(value.getAndUpdate((v)->{return Double.doubleToLongBits(oprand(Double.longBitsToDouble(v), delta));}));
				}

				@Override
				public Double addAndGet(Double delta) {
					return Double.longBitsToDouble(value.updateAndGet((v)->{return Double.doubleToLongBits(oprand(Double.longBitsToDouble(v), delta));}));
				}
			};
		case String:
			return new BoundaryAdder<String>() {
				private AtomicReference<String> value = new AtomicReference<>();
				private String MIN_VALUE = (String) minObj, MAX_VALUE = (String) maxObj, increment = (String) incrementObj;
				{
					set((String) initialObj);
				}
				
				private String get(String v) {
					int cmp = CompareUtils.compareStringRange(v, MIN_VALUE, MAX_VALUE);
					if (cmp<0) {
						v = MIN_VALUE;
					}else if (cmp>0) {
						v = MAX_VALUE;
					}
					return v;
				}

				private String increment(String str1, String str2) {
					char[] chars1 = str1.toCharArray();
					char[] chars2 = str2.toCharArray();
					
					if (chars1.length<chars2.length) {
						char[] tmp = chars1;
						chars1 = chars2;
						chars2 = tmp;
					}
					
					int n = 0;
					for(int i=chars1.length-1, j=chars2.length-1; i>=0&&j>=0; i--,j--) {
						n += ((int)chars1[i]&0xFF)+((int)chars2[j]&0xFF);
						if (n>=Character.MAX_VALUE) {
							chars1[i] = (char) (n&=0xFF); n =  1;
						}else if (n<=Character.MIN_VALUE) {
							chars1[i] = (char) (n&=0xFF); n = -1;
						}else {
							chars1[i] = (char) n; n = 0;
						}
					}
					
					if (n>0) {
						char[] chars = new char[chars1.length+1];
						System.arraycopy(chars1, 0, chars, 1, chars.length);
						chars[0] = 1;
						chars1 = chars;
					}else if (n<0) {
						chars1 = Arrays.copyOfRange(chars1, 1, chars1.length);
					}
					return new String(chars1);
				}
				
				private String decrement(String str1, String str2) {
					char[] chars1 = str1.toCharArray();
					char[] chars2 = str2.toCharArray();
					
					if (chars1.length<chars2.length) {
						char[] tmp = chars1;
						chars1 = chars2;
						chars2 = tmp;
					}
					
					int n = 0;
					for(int i=chars1.length-1, j=chars2.length-1; i>=0&&j>=0; i--,j--) {
						n += ((int)chars1[i]&0xFF)-((int)chars2[j]&0xFF);
						if (n>=Character.MAX_VALUE) {
							chars1[i] = (char) (n&=0xFF); n =  1;
						}else if (n<=Character.MIN_VALUE) {
							chars1[i] = (char) (n&=0xFF); n = -1;
						}else {
							chars1[i] = (char) n; n = 0;
						}
					}
					
					if (n>0) {
						char[] chars = new char[chars1.length+1];
						System.arraycopy(chars1, 0, chars, 1, chars.length);
						chars[0] = 1;
						chars1 = chars;
					}else if (n<0) {
						chars1 = Arrays.copyOfRange(chars1, 1, chars1.length);
					}
					return new String(chars1);
				}
				
				@Override
				public String get() {
					return (String) value.get();
				}

				@Override
				public void set(String newValue) {
					value.set(get(newValue));
				}

				@Override
				public void set(AdderUnaryOperator<String> adderUnaryOperator) {
					value.updateAndGet((v)->get(adderUnaryOperator.apply(v)));
				}
				
				@Override
				public String getAndSet(String newValue) {
					String prev = get();
					set(newValue);
					return prev;
				}

				@Override
				public boolean compareAndSet(String expect, String update) {
					String prev = get();
					String curr = value.updateAndGet(v->{
									if (v.equals(expect)) {
										return get(update);
									}
									return v;
								});
					return !prev.equals(curr);
				}

				private String incrementTo(String v, String delta) {
					return get(increment(v, delta));
				}
				private String decrementTo(String v, String delta) {
					return get(decrement(v, delta));
				}

				@Override
				public boolean increment(String delta) {
					String prev = get(); prev = new String(prev);
					return !prev.equals(addAndGet(delta));
				}

				@Override
				public boolean decrement(String delta) {
					String prev = get(); prev = new String(prev);
					return !prev.equals(value.updateAndGet((v)->{
						return decrementTo(v, delta);
					}));
				}

				@Override
				public String getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public String incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public String getAndDecrement() {
					return value.getAndUpdate((v)->{
						return decrementTo(v, increment);
					});
				}

				@Override
				public String decrementAndGet() {
					return value.updateAndGet((v)->{
						return decrementTo(v, increment);
					});
				}

				@Override
				public String getAndAdd(String delta) {
					return value.getAndUpdate((v)->{
						return incrementTo(v, delta);
					});
				}

				@Override
				public String addAndGet(String delta) {
					return value.updateAndGet((v)->{
						return incrementTo(v, delta);
					});
				}
				
			};
		case Bytes:
			return new BoundaryAdder<byte[]>() {
				private AtomicReference<byte[]> value = new AtomicReference<>();
				private byte[] MIN_VALUE = (byte[]) minObj, MAX_VALUE = (byte[]) maxObj, increment = (byte[]) incrementObj;
				{
					set((byte[]) initialObj);
				}
				
				private byte[] get(byte[] v) {
					int cmp = CompareUtils.compareBytesRange(v, MIN_VALUE, MAX_VALUE);
					if (cmp<0) {
						v = MIN_VALUE;
					}else if (cmp>0) {
						v = MAX_VALUE;
					}
					return v;
				}

				private byte[] increment(byte[] bytes1, byte[] bytes2) {
					if (bytes1.length<bytes2.length) {
						byte[] tmp = bytes1;
						bytes1 = bytes2;
						bytes2 = tmp;
					}
					
					int n = 0;
					for(int i=bytes1.length-1, j=bytes2.length-1; i>=0&&j>=0; i--,j--) {
						n += ((int)bytes1[i]&0xFF)+((int)bytes2[j]&0xFF);
						if (n>=Byte.MAX_VALUE) {
							bytes1[i] = (byte) (n&=0xFF); n =  1;
						}else if (n<=Byte.MIN_VALUE) {
							bytes1[i] = (byte) (n&=0xFF); n = -1;
						}else {
							bytes1[i] = (byte) n; n = 0;
						}
					}
					
					if (n>0) {
						byte[] bytes = new byte[bytes1.length+1];
						System.arraycopy(bytes1, 0, bytes, 1, bytes.length);
						bytes[0] = 1;
						bytes1 = bytes;
					}else if (n<0) {
						bytes1 = Arrays.copyOfRange(bytes1, 1, bytes1.length);
					}
					return bytes1;
				}
				
				private byte[] decrement(byte[] bytes1, byte[] bytes2) {
					if (bytes1.length<bytes2.length) {
						byte[] tmp = bytes1;
						bytes1 = bytes2;
						bytes2 = tmp;
					}
					
					int n = 0;
					for(int i=bytes1.length-1, j=bytes2.length-1; i>=0&&j>=0; i--,j--) {
						n += ((int)bytes1[i]&0xFF)-((int)bytes2[j]&0xFF);
						if (n>=Byte.MAX_VALUE) {
							bytes1[i] = (byte) (n&=0xFF); n =  1;
						}else if (n<=Byte.MIN_VALUE) {
							bytes1[i] = (byte) (n&=0xFF); n = -1;
						}else {
							bytes1[i] = (byte) n; n = 0;
						}
					}
					
					if (n>0) {
						byte[] bytes = new byte[bytes1.length+1];
						System.arraycopy(bytes1, 0, bytes, 1, bytes.length);
						bytes[0] = 1;
						bytes1 = bytes;
					}else if (n<0) {
						bytes1 = Arrays.copyOfRange(bytes1, 1, bytes1.length);
					}
					return bytes1;
				}

				@Override
				public byte[] get() {
					return (byte[]) value.get();
				}
				
				@Override
				public void set(byte[] newValue) {
					value.set(get(newValue));
				}

				@Override
				public void set(AdderUnaryOperator<byte[]> adderUnaryOperator) {
					value.updateAndGet((v)->get(adderUnaryOperator.apply(v)));
				}
				
				@Override
				public byte[] getAndSet(byte[] newValue) {
					byte[] prev = get();
					set(newValue);
					return prev;
				}

				@Override
				public boolean compareAndSet(byte[] expect, byte[] update) {
					byte[] prev = get();
					byte[] curr = value.updateAndGet(v->{
									if (0==Arrays.compare(v, expect)) {
										return get(update);
									}
									return v;
								});
					return 0!=Arrays.compare(prev, curr);
				}

				private byte[] incrementTo(byte[] v, byte[] delta) {
					return get(increment(v, delta));
				}
				
				private byte[] decrementTo(byte[] v, byte[] delta) {
					return get(decrement(v, delta));
				}

				@Override
				public boolean increment(byte[] delta) {
					byte[] prev = get(); prev = Arrays.copyOf(prev, prev.length);
					return !Arrays.equals(prev, addAndGet(delta));
				}

				@Override
				public boolean decrement(byte[] delta) {
					byte[] prev = get(); prev = Arrays.copyOf(prev, prev.length);
					return !Arrays.equals(prev, value.updateAndGet((v)->{
						return decrementTo(v, delta);
					}));
				}

				@Override
				public byte[] getAndIncrement() {
					return getAndAdd(increment);
				}

				@Override
				public byte[] incrementAndGet() {
					return addAndGet(increment);
				}

				@Override
				public byte[] getAndDecrement() {
					byte[] prev = get(); prev = Arrays.copyOf(prev, prev.length);
					value.getAndUpdate((v)->{
						return decrementTo(v, increment);
					});
					return prev;
				}

				@Override
				public byte[] decrementAndGet() {
					return value.updateAndGet((v)->{
						return decrementTo(v, increment);
					});
				}

				@Override
				public byte[] getAndAdd(byte[] delta) {
					byte[] prev = get(); prev = Arrays.copyOf(prev, prev.length);
					value.getAndUpdate((v)->{
						return incrementTo(v, delta);
					});
					return prev;
				}

				@Override
				public byte[] addAndGet(byte[] delta) {
					return value.updateAndGet((v)->{
						return incrementTo(v, delta);
					});
				}
			};
		}
		return null;
	}
}
