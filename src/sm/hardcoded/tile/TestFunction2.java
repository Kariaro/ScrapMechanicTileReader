 package sm.hardcoded.tile;

class TestFunction2 {
	private static final int[] INT_00e6cab8 = { 0x0, 0x1, 0x2, 0x1, 0x0, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7, 0x1000b, 0x10 };
	private static final int[] INT_00e6cbe0 = { 0x0, 0x0, 0x0, 0xffffffff, 0xfffffffc, 0x1, 0x2, 0x3, 0x6, 0xd, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7 };
	
	public int decompress(byte[] bytes, byte[] input, int uncompressed_size) {
		Pointer __input = new Pointer(1000000).WriteBytes(bytes);
		Pointer __param_2 = new Pointer(1000000);
		int __result = DecompressJava5(__input, __param_2, uncompressed_size);
		
		__param_2.Bytes(input, 0, uncompressed_size, true);
		// System.out.println("Jva: '" + __result + "'");
		
		// System.out.println("Asm: '" + result + "'");
		return __result;
	}
	
	private static final void memcpy(Pointer dst, int dstPos, Pointer src, int srcPos, int len) {
		byte[] d = dst.data();
		byte[] s = src.data();
		for(int i = 0; i < len; i++) {
			d[i + dstPos] = s[i + srcPos];
		}
	}
	
	private static final void memmove(Pointer dst, int dstPos, Pointer src, int srcPos, int len) {
		byte[] d = dst.data();
		byte[] s = src.data();
		for(int i = len - 1; i >= 0; i--) {
			d[i + dstPos] = s[i + srcPos];
		}
	}
	
	// TODO: Use only byte arrays
	private int DecompressJava5(Pointer input, Pointer param_2, int size) {
		int EAX = 0;
		int index = 0;
		int ESI = 0;
		int EDI = 0;
		
		int EBP_0x1c = 0;
		
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		do {
			int readByte = input.UnsignedByte(index++);
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			if((highByte < 0x9) && (ESI <= size - 26)) {
				memcpy(param_2, ESI, input, index, 8);
				
				ESI += highByte;
				index += highByte;
				
				highByte = input.UnsignedShort(index);
				
				if((lowByte == 0xf) || (highByte < 0x8)) {
					EAX = index;
					index += 0x2;
					
					EDI = ESI - highByte;
				} else {
					memcpy(param_2, ESI, param_2, ESI - highByte, 0x12);
					
					ESI += lowByte + 0x4;
					index += 0x2;
					continue;
				}
			} else {
				if(highByte == 0xf) {
					int value = 0;
					int read = 0;
					
					do {
						read = input.UnsignedByte(index++);
						value += read;
					} while(read == 0xff);
					
					highByte = value + 0xf;
				}
				
				int someOffset = highByte + ESI;
				if(someOffset > size - 8) {
					if(someOffset != size) break;
					memmove(param_2, ESI, input, index, highByte);
					return highByte + index;
				}
				
				memcpy(param_2, ESI, input, index, 8 * ((highByte + 7) / 8));
				
				index += highByte;
				highByte = input.UnsignedShort(index);
				
				ESI = someOffset;
				EDI = someOffset - highByte;
				
				// NOTE - EAX and EBX can probably be combined
				EAX = index;
			}
			
			// EDI = x - highByte
			index = EAX + 0x2;
			
			if(lowByte == 0xf) {
				int value = 0;
				int read = 0;
				
				do {
					read = input.UnsignedByte(index++);
					value += read;
				} while(read == 0xff);
				
				lowByte = value + 0xf;
			}
			
			if(highByte < 8) {
				param_2.WriteInt(0, ESI);
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[highByte];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[highByte];
			} else {
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			EBP_0x1c = lowByte + ESI + 0x4;
			ESI += 0x8;
			
			if(EBP_0x1c > size - 12) {
				if(EBP_0x1c > size - 5) break;
				
				if(ESI < size - 0x7) {
					memcpy(param_2, ESI, param_2, EDI, 8 * ((size - ESI) / 8));
					
					EAX = size - 7 - ESI;
					ESI = size - 7;
					EDI += EAX;
				}
				
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				}
				
				ESI = EBP_0x1c;
				continue;
			}
			
			memcpy(param_2, ESI, param_2, EDI, 8);
			
			if(lowByte > 12) {
				memcpy(param_2, ESI + 8, param_2, EDI + 8, 8 * ((lowByte + 7) / 8));
			}
			
			ESI = EBP_0x1c;
		} while(true);
		
		return - (index + 1);
	}
	
	private int DecompressJava4(Pointer input, Pointer param_2, int size) {
		int EAX = 0;
		int index = 0;
		int ESI = 0;
		int EDI = 0;
		
		int EBP_0x1c = 0;
		
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		do {
			int readByte = input.UnsignedByte(index++);
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			if((highByte < 0x9) && (ESI <= size - 26)) {
				memcpy(param_2, ESI, input, index, 8);
				
				ESI += highByte;
				index += highByte;
				
				highByte = input.UnsignedShort(index);
				
				if((lowByte == 0xf) || (highByte < 0x8)) {
					EAX = index;
					index += 0x2;
					
					EDI = ESI - highByte;
				} else {
					memcpy(param_2, ESI, param_2, ESI - highByte, 0x12);
					
					ESI += lowByte + 0x4;
					index += 0x2;
					continue;
				}
			} else {
				if(highByte == 0xf) {
					int value = 0;
					int read = 0;
					
					do {
						read = input.UnsignedByte(index++);
						value += read;
					} while(read == 0xff);
					
					highByte = value + 0xf;
				}
				
				int someOffset = highByte + ESI;
				if(someOffset > size - 8) {
					if(someOffset != size) break;
					memmove(param_2, ESI, input, index, highByte);
					return highByte + index;
				}
				
				memcpy(param_2, ESI, input, index, 8 * ((highByte + 7) / 8));
				
				index += highByte;
				highByte = input.UnsignedShort(index);
				
				ESI = someOffset;
				EDI = someOffset - highByte;
				
				// NOTE - EAX and EBX can probably be combined
				EAX = index;
			}
			
			// EDI = x - highByte
			index = EAX + 0x2;
			
			if(lowByte == 0xf) {
				int value = 0;
				int read = 0;
				
				do {
					read = input.UnsignedByte(index++);
					value += read;
				} while(read == 0xff);
				
				lowByte = value + 0xf;
			}
			
			if(highByte < 0x8) {
				param_2.WriteInt(0, ESI);
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[highByte];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[highByte];
			} else {
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			EBP_0x1c = lowByte + ESI + 0x4;
			ESI += 0x8;
			
			if(EBP_0x1c > size - 12) {
				if(EBP_0x1c > size - 5) break;
				
				if(ESI < size - 0x7) {
					memcpy(param_2, ESI, input, EDI, 8 * ((size - ESI) / 8));
					
					EAX = size - 7 - ESI;
					ESI = size - 7;
					EDI += EAX;
				}
				
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				}
				
				ESI = EBP_0x1c;
				continue;
			}
			
			memcpy(param_2, ESI, param_2, EDI, 8);
			
			if(lowByte > 12) {
				memcpy(param_2, ESI + 8, param_2, EDI + 8, 8 * ((lowByte + 7) / 8));
			}
			
			ESI = EBP_0x1c;
		} while(true);
		
		return - (index + 1);
	}
	
	private int DecompressJava3(Pointer input, Pointer param_2, int size) {
		int EAX = 0; // inputIndex
		int index = 0; // inputIndex
		int ESI = 0; // param_2Index
		int EDI = 0; // param_2Index
		
		int EBP_0x1c = 0; // ESI
		
		// int __fastcall CalculateCompressedSize_007039c0(byte* input, int* param_2, int size)
		//
		// __fastcall
		// EAX:4			int		<RETURN>
		// ECX:4			byte*	input
		// EDX:4			int*	param_2
		// Stack[0x4]:4		int		size
		
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		do {
			int readByte = input.UnsignedByte(index++);
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			if((highByte < 0x9) && (ESI <= size - 26)) {
				memcpy(param_2, ESI, input, index, 8);
				
				ESI += highByte;
				index += highByte;
				
				highByte = input.UnsignedShort(index);
				// lowByte = readByte & 0xf;
				
				if((lowByte == 0xf) || (highByte < 0x8)) {
					EAX = index;
					index += 0x2;
					
					EDI = ESI - highByte;
					
				} else {
					memcpy(param_2, ESI, param_2, ESI - highByte, 0x12);
					
					ESI += lowByte + 0x4;
					index += 0x2;
					continue;
				}
			} else {
				if(highByte == 0xf) {
					int offset = index;
					int value = 0;
					int read = 0;
					
					do {
						read = input.UnsignedByte(offset++);
						value += read;
					} while(read == 0xff);
					
					highByte = value + 0xf;
					index = offset;
				}
				
				int someOffset = highByte + ESI;
				if(someOffset > size - 8) { // LAB_00703bfb:
					if(someOffset != size) break;
					
					memmove(param_2, ESI, input, index, highByte);
					
					return highByte + index;
				}
				
				int offset1 = ESI;
				//int offset2 = index - ESI;
				
				// TODO - Replace this with memcpy
				/*
				System.out.printf("Start: %d < %d\n", offset1, someOffset);
				System.out.printf("T0: %d\n", someOffset - offset1);
				System.out.printf("T1: %d\n", (someOffset - offset1 + 7) / 8);
				int testCounter = 0;
				do { // LAB_00703ab0:
					memcpy(param_2, offset1, input, offset2 + offset1, 8);
					System.out.printf("o1: %d  o2: %d\n", offset1, offset2);
					offset1 += 0x8;
					testCounter++;
				} while(offset1 < someOffset);
				System.out.printf("Counter: %d\n\n", testCounter);
				*/

				memcpy(param_2, offset1, input, index, 8 * ((someOffset - offset1 + 7) / 8));
				index += highByte;
				
				highByte = input.UnsignedShort(index);
				
				ESI = someOffset;
				EDI = someOffset - highByte;
				
				// NOTE - EAX and EBX can probably be combined
				EAX = index;
			}
			
			// EAX is not changed after this point
			// LAB_00703ade:
			int nextOffsetMaybe = EAX + 0x2;
			index = nextOffsetMaybe;
			
			if(lowByte == 0xf) {
				int offset = nextOffsetMaybe;
				int value = 0;
				int read = 0;
				
				do { // LAB_00703af0:
					read = input.UnsignedByte(offset++);
					value += read;
				} while(read == 0xff);
				
				nextOffsetMaybe = offset;
				index = offset;
				
				lowByte = value + 0xf;
			}
			
			// EBX = nextOffsetMaybe;
			
			// LAB_00703b03:
			if(highByte < 0x8) {
				param_2.WriteInt(0, ESI);
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[highByte];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[highByte];
			} else { // LAB_00703b4d:
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			// LAB_00703b5a:
			EBP_0x1c = lowByte + ESI + 0x4;
			
			if(EBP_0x1c > size - 12) {
				if(EBP_0x1c > size - 5) break;
				ESI += 0x8;
				
				if(ESI < size - 0x7) { // JC branch
					int offset1 = ESI;
					//int offset2 = EDI - ESI;
					/*
					// TODO - Replace this with memcpy
					do { // LAB_00703b90:
						memcpy(param_2, offset1, input, offset2 + offset1, 8);
						offset1 += 0x8;
					} while(offset1 < size - 7);
					*/
					
					memcpy(param_2, offset1, input, EDI, 8 * ((size - offset1) / 8));
					
					EAX = size - 7 - ESI;
					ESI = size - 7;
					EDI += EAX;
					
					// EBX = nextOffsetMaybe;
				}
				
				// LAB_00703bb1:
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				} else { // LAB_00703bf4:
					
				}
				
				ESI = EBP_0x1c;
				continue;
			}
			
			memcpy(param_2, ESI + 8, param_2, EDI, 8);
			
			if(lowByte > 0x10) {
				int offset1 = ESI + 16;
				int offset2 = EDI - offset1;
				
				// TODO - Replace this with memcpy
				do { // LAB_00703be0:
					// memcpy(param_2, offset1, param_2, offset2 + offset1 + 0x8, 8);
					memcpy(param_2, offset1, param_2, offset2 + offset1 + 0x8, 8);
					offset1 += 0x8;
				} while(offset1 < EBP_0x1c);
			}
			
			ESI = EBP_0x1c;
		} while(true);
		
		// LAB_00703c19:
		return - (index + 1);
	}

	
	private int DecompressJava2(Pointer input, Pointer param_2, int size) {
		int EAX = 0; // inputIndex
		int EBX = 0; // inputIndex
		int ESI = 0; // param_2Index
		int EDI = 0; // param_2Index
		
		int EBP_0x1c = 0; // ESI
		
		// int __fastcall CalculateCompressedSize_007039c0(byte* input, int* param_2, int size)
		//
		// __fastcall
		// EAX:4			int		<RETURN>
		// ECX:4			byte*	input
		// EDX:4			int*	param_2
		// Stack[0x4]:4		int		size

		// NOTE - Jump table
		// dst = src    ZF = 1    CF = 0
		// dst < src    ZF = 0    CF = 1
		// dst > src    ZF = 0    CF = 0
		
		
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		int idx = 0;
		do { // LAB_00703a07:
			EDI = size - 0x1a;
			
			int readByte = input.UnsignedByte(EBX++);
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			boolean jump_00703a9b = true;
			
			if(highByte > 0x8) {	// LAB_00703a81: (Siplified)
				// This branch will enter 'jump_00703a9b'
				
				if(highByte == 0xf) {
					int offset = EBX;
					int value = 0;
					int read = 0;
					
					do { // LAB_00703a88:
						read = input.UnsignedByte(offset++);
						value += read;
					} while(read == 0xff);
					
					highByte = value + 0xf;
					EBX = offset;
				}
			} else { // EBP_0x4 < 0x9
				if(ESI <= size - 26) { // TODO - Put this first then inside the else might be able to put jump_00703a9b 
					// This is the only branch that will not enter 'jump_00703a9b'
					
					memcpy(param_2, ESI, input, EBX, 8);
					
					ESI += highByte;
					EBX += highByte;
					
					highByte = input.UnsignedShort(EBX);
					// lowByte = readByte & 0xf;
					
					if((lowByte == 0xf) || (highByte < 0x8)) { // LAB_00703a7c
						EAX = EBX;
						EBX += 0x2;
						
						jump_00703a9b = false;
					} else {
						memcpy(param_2, ESI, param_2, ESI - highByte, 0x12);
						
						ESI += lowByte + 0x4;
						EBX += 0x2;
						continue;
					}
				} else {
					// This branch will enter 'jump_00703a9b'
				}
			}
			
			if(jump_00703a9b) { // LAB_00703a9b:
				int someOffset = highByte + ESI;
				
				if(someOffset > size - 8) { // LAB_00703bfb:
					if(someOffset != size) break;
					
					memmove(param_2, ESI, input, EBX, highByte);
					
					return highByte + EBX;
				}
				
				int offset1 = ESI;
				int offset2 = EBX - ESI;
				
				// TODO - Replace this with memcpy
				do { // LAB_00703ab0:
					memcpy(param_2, offset1, input, offset2 + offset1, 8);
					offset1 += 0x8;
				} while(offset1 < someOffset);
				
				EBX += highByte;
				
				highByte = input.UnsignedShort(EBX);
				
				ESI = someOffset;
				EDI = someOffset - highByte;
				
				// NOTE - EAX and EBX can probably be combined
				EAX = EBX;
				
				// EDI = someOffset - EBP_0x4;
			} else {
				EDI = ESI - highByte;
			}
			
			// lowByte = readByte & 0xf;
			// EAX is not changed after this point
			// LAB_00703ade:
			int nextOffsetMaybe = EAX + 0x2;
			EBX = nextOffsetMaybe;
			
			if(lowByte == 0xf) {
				int offset = nextOffsetMaybe;
				int value = 0;
				int read = 0;
				
				do { // LAB_00703af0:
					read = input.UnsignedByte(offset++);
					value += read;
				} while(read == 0xff);
				
				nextOffsetMaybe = offset;
				EBX = offset;
				
				lowByte = value + 0xf;
			}

			// EBX = nextOffsetMaybe;
			
			// LAB_00703b03:
			if(highByte < 0x8) {
				param_2.WriteInt(0, ESI);
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[highByte];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[highByte];
			} else { // LAB_00703b4d:
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			// LAB_00703b5a:
			EBP_0x1c = lowByte + ESI + 0x4;
			lowByte += 0x4;
			ESI += 0x8;
			
			if(EBP_0x1c > size - 12) {
				if(EBP_0x1c > size - 5) break;
				//ESI += 0x8;
				
				if(ESI < size - 7) { // JC branch
					int offset1 = ESI;
					int offset2 = EDI - ESI;
					
					// TODO - Replace this with memcpy
					do { // LAB_00703b90:
						memcpy(param_2, offset1, input, offset2 + offset1, 8);
						offset1 += 0x8;
					} while(offset1 < size - 7);
					
					EAX = size - 7 - ESI;
					ESI = size - 7;
					EDI += EAX;
					
					// EBX = nextOffsetMaybe;
				}
				
				// LAB_00703bb1:
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				} else { // LAB_00703bf4:
					
				}

				ESI = EBP_0x1c;
				continue;
			}

			if(idx++ < 100 && idx > 90) System.out.println(lowByte);
			memcpy(param_2, ESI, param_2, EDI, 8);
			
			if(lowByte > 0x10) {
				int offset1 = ESI + 0x8;
				int offset2 = EDI - offset1;
				// EDI -= ESI;
				
				// TODO - Replace this with memcpy
				do { // LAB_00703be0:
					// memcpy(param_2, offset1, param_2, offset2 + offset1 + 0x8, 8);
					memcpy(param_2, offset1, param_2, offset2 + offset1 + 0x8, 8);
					offset1 += 0x8;
				} while(offset1 < EBP_0x1c);
			}
			
			ESI = EBP_0x1c;
		} while(true);
		
		// LAB_00703c19:
		return - (EBX + 1);
	}

	private int DecompressJava(Pointer input, Pointer param_2, int size) {
		int EAX = 0;
		int ECX = 0;
		int EDX = 0;
		int EBX = 0;
		int ESI = 0;
		int EDI = 0;
		
		int EBP_0x4 = 0;
		int EBP_0x8 = 0;
		int EBP_0x1c = 0;
		
		// int __fastcall CalculateCompressedSize_007039c0(byte* input, int* param_2, int size)
		//
		// __fastcall
		// EAX:4			int		<RETURN>
		// ECX:4			byte*	input
		// EDX:4			int*	param_2
		// Stack[0x4]:4		int		size

		// NOTE - Jump table
		// dst = src    ZF = 1    CF = 0
		// dst < src    ZF = 0    CF = 1
		// dst > src    ZF = 0    CF = 0
		
		
		// JNZ - Jump short if not zero (ZF=0)
		// Log("TEST EAX,EAX");
		// Log("JNZ LAB_00703a07");
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		EAX = size;
		
		int idx = 0;
		for(;;) { // LAB_00703a07:
			// NOTE - EDI is always 'SIZE - 0x1a' here
			EDI = size - 0x1a;
			
			int readByte = input.UnsignedByte(EBX++);
			ECX = readByte;
			
			EBP_0x8 = readByte;
			EBP_0x4 = readByte >> 0x4;
			EDX = EBP_0x4;
			
			boolean jump_00703a9b = true;
			
			// JA - Jump short if above (CF=0 and ZF=0)
			// Log("CMP EDX,0x8");
			// Log("JA LAB_00703a81");
			if(EBP_0x4 > 0x8) {	// LAB_00703a81: (Siplified)
				// JNZ - Jump short if not zero (ZF=0)
				// Log("CMP EDX,0xf");
				// Log("JNZ LAB_00703a9b");
				if(EBP_0x4 == 0xf) {
					int value = 0;
					int read = 0;
					
					do { // LAB_00703a88:
						read = input.UnsignedByte(EBX++);
						value += read;
					} while(read == 0xff);
					
					EBP_0x4 = value + 0xf;
					EDX = EBP_0x4;
					ECX = value;
				}
				
				// JA - Jump short if above (CF=0 and ZF=0)
				// Log("CMP ESI,EDI");
				// Log("JA LAB_00703a9b");
			} else if(!(ESI > size - 0x1a)) {
				memcpy(param_2, ESI, input, EBX, 8);
				
				ESI += EBP_0x4;
				EBX += EBP_0x4;
				
				EBP_0x4 = input.UnsignedShort(EBX);
				EBP_0x8 = readByte & 0xf;
				
				// JZ - Jump short if zero (ZF = 1)
				// Log("CMP ECX,0xf");					// 00703a4d
				// Log("JZ LAB_00703a7c");				// 00703a50
				
				// JC - Jump short if carry (CF = 1)
				// Log("CMP EAX,0x8");					// 00703a52
				// Log("JC LAB_00703a7c");				// 00703a55
				if((EBP_0x8 == 0xf) || (EBP_0x4 < 0x8)) { // LAB_00703a7c
					EAX = EBX;
					
					EDI = ESI - EBP_0x4;
					EBX += 0x2;
					ECX &= 0xf;
					
					jump_00703a9b = false;
				} else {
					memcpy(param_2, ESI, param_2, ESI - EBP_0x4, 0x12);
					
					ESI += EBP_0x8 + 0x4;
					EBX += 0x2;
					
					// EDI = SIZE - 0x1a;
					continue;
				}
			}

			if(jump_00703a9b) { // LAB_00703a9b:
				// JA - Jump short if above (CF=0 and ZF=0)
				// Log("CMP EDI,EAX");					// 00703aa4
				// Log("JA LAB_00703bfb");				// 00703aa6
				if(EDX + ESI > size - 0x8) { // LAB_00703bfb:
					// JNZ - Jump short if not zero (ZF=0)
					// Log("CMP EDI,ECX");					// 00703bfb
					// Log("JNZ LAB_00703c19");				// 00703bfd
					if(EDX + ESI != size) break; // Jump to end of function
					
					//param_2.WriteBytes(input.Bytes(EDX, EBX), EDX, ESI);
					memmove(param_2, ESI, input, EBX, EDX);
					
					// return EBP_0x4 - EBP_0x14 + EBX;
					return EBP_0x4 + EBX;
				}
				
				// EAX = EBP_0xc - 0x8;
				EDI = EDX + ESI;
				ECX = EBX - ESI;
				
				// TODO - Replace this with memcpy
				do { // LAB_00703ab0:
					memcpy(param_2, ESI, input, ECX + ESI, 8);
					ESI += 0x8;
					
					// JC - Jump short if carry (CF = 1)
					// Log("CMP ESI,EDI");						// 00703abf
					// Log("JC LAB_00703ab0");					// 00703ac1
				} while(ESI < EDI);
				
				EBX += EDX;
				
				EBP_0x4 = input.UnsignedShort(EBX);
				
				ESI = EDI;
				EDI -= EBP_0x4;
				
				ECX = EBP_0x8 & 0xf;
				EAX = EBX;
			}
			
			// LAB_00703ade:
			EBP_0x8 = EAX + 0x2;
			EBX = EBP_0x8;
			
			// JNZ - Jump short if not zero (ZF=0)
			// Log("CMP ECX,0xf");						// 00703ae4
			// Log("JNZ LAB_00703b03");					// 00703ae7
			if(ECX == 0xf) {
				int value = 0;
				int read = 0;
				
				do { // LAB_00703af0:
					read = input.UnsignedByte(EBX++);
					value += read;
				} while(read == 0xff);
				
				EBP_0x8 = EBX;
				ECX = value + 0xf;
			}
			
			// LAB_00703b03:
			// JNC - Jump short if not carry (CF=0)
			// Log("CMP dword ptr [EBP + -0x4],0x8");	// 00703b06
			// Log("JNC LAB_00703b4d");					// 00703b10
			if(EBP_0x4 < 0x8) {
				param_2.WriteInt(0, ESI);
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[EBP_0x4];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[EBP_0x4];
			} else { // LAB_00703b4d:
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			// LAB_00703b5a:
			// EAX = EBP_0xc - 0xc;
			EBP_0x1c = ECX + ESI + 0x4;
			ECX += 0x4;
			ESI += 0x8;
			
			// JBE - Jump short if below or equal (CF=1 or ZF=1)
			// Log("CMP EDX,EAX");						// 00703b63
			// Log("JBE LAB_00703bc8");					// 00703b65
			if(!(EBP_0x1c <= size - 0xc)) {
				// JA - Jump short if above (CF=0 and ZF=0)
				// Log("CMP EDX,EAX");							// 00703b73
				// Log("JA LAB_00703c19");						// 00703b75
				if(EBP_0x1c > size - 0x5) break;
				
				// ECX = size - 0x7;
				EAX = size - 0x5;
				
				// JNC - Jump short if not carry (CF=0)
				// Log("CMP ESI,ECX");						// 00703b7b
				// Log("JNC LAB_00703bb1");					// 00703b7d
				if(ESI < size - 0x7) { // JC branch
					int start = ESI;
					EDX = EDI - ESI;
					
					// TODO - Replace this with memcpy
					do { // LAB_00703b90:
						memcpy(param_2, start, input, EDX + start, 8);
						start += 0x8;
						
						// JC - Jump short if carry (CF = 1)
						// Log("CMP ECX,EBX");							// 00703b9f
						// Log("JC LAB_00703b90");						// 00703ba1
					} while(start < size - 0x7);
					
					EAX = size - 0x7 - ESI;
					EDI += EAX;
					
					ESI = size - 0x7;
					EBX = EBP_0x8;
				}
				
				// LAB_00703bb1:
				// JNC - Jump short if not carry (CF=0)
				// Log("CMP ESI,EDX");						// 00703bb1
				// Log("JNC LAB_00703bf4");					// 00703bb3
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				} else { // LAB_00703bf4:
					
				}
				
				ESI = EBP_0x1c;
				continue;
			}


			if(idx++ < 100 && idx > 90) System.out.println(ECX);
			// LAB_00703bc8:
			memcpy(param_2, ESI, param_2, EDI, 8);
			
			// JBE - Jump short if below or equal (CF=1 or ZF=1)
			// Log("CMP ECX,0x10");					// 00703bd2
			// Log("JBE LAB_00703bf4");				// 00703bd5
			if(!(ECX <= 0x10)) { // JA branch
				ESI += 0x8;
				EDI -= ESI;

				// TODO - Replace this with memcpy
				do { // LAB_00703be0:
					memcpy(param_2, ESI, param_2, EDI + ESI + 0x8, 8);
					ESI += 0x8;
					
					// JC - Jump short if carry (CF = 1)
					// Log("CMP ESI,EDX");							// 00703bf0
					// Log("JC LAB_00703be0");						// 00703bf2
				} while(ESI < EBP_0x1c);
			}
			
			ESI = EBP_0x1c;
			continue;
		}
		
		// LAB_00703c19:
		return - (EBX + 1);
	}
}
