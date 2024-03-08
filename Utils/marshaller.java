package Utils;

public class marshaller {

    public static byte[] marshInt(int intData) {
    	// Marshal Int from intData and return byteArray

        byte[] arrayData = new byte[4];

        
        //copy the 4 bytes of int into the array arrayData, one byte at a time
        // arrayData[3] starts first, meaning it is the MSB first (big endian ordering)
        for (int i = 3; i >= 0; i--) {
        	arrayData[i] = (byte) (intData & 0xff);
            intData >>>= 8;
        }

        return arrayData;
    }
    
    public static int unmarshInt(byte[] aryData, int intOffset) {
    	//Unmarshal Int from byteArray and offset, return int
    	System.out.println("size of array data: "+aryData.length );
    	
    	// Getting the LSB first and adding the values one byte at a time
        int value_Int =0;
        if (intOffset==0) {
            value_Int += (int) aryData[intOffset + 3] & 0xFF;
            value_Int += ((int) aryData[intOffset + 2] & 0xFF) << 8;
            value_Int += ((int) aryData[intOffset + 1] & 0xFF) << 16;
            value_Int += ((int) aryData[intOffset] & 0xFF) << 24;
        }
        else if (intOffset==1) {
        	 value_Int += ((int) aryData[intOffset + 2] & 0xFF);
             value_Int += ((int) aryData[intOffset + 1] & 0xFF) << 8;
             value_Int += ((int) aryData[intOffset] & 0xFF) << 16;

        }
        else if (intOffset==2) {
        	 value_Int += ((int) aryData[intOffset + 1] & 0xFF);
             value_Int += ((int) aryData[intOffset] & 0xFF) << 8;

        }
        else if (intOffset==3) {
            value_Int += ((int) aryData[intOffset] & 0xFF);

        }
        
        else {
        	value_Int=-1;
        }
//        value_Int += (int) aryData[intOffset + 3] & 0xFF;
//        value_Int += ((int) aryData[intOffset + 2] & 0xFF) << 8;
//        value_Int += ((int) aryData[intOffset + 1] & 0xFF) << 16;
//        value_Int += ((int) aryData[intOffset] & 0xFF) << 24;

        return value_Int;
    }
    
    
    public static byte[] marshString(String strData) {
    	// Marshal the String data into byte array containing length of string and byteArray
    	
        int intLength = strData.length();

        System.out.println("\nLength of String is: " + intLength);
        // arrayData should contain the length of string first and then stringdata
        // allocated 4 bytes for length of string since it is int
        
        byte[] arrayData = new byte[intLength + 4];

        // Copy length of string first (marshalled) into arrayData
//        source_arr : array to be copied from -> marshalled length of string
//        sourcePos : starting position in source array from where to copy -> 0
//        dest_arr : array to be copied in -> arrayData
//        destPos : starting position in destination array, where to copy in -> 0
//        len : total no. of components to be copied -> 4 bytes since int
        
        System.arraycopy(marshInt(intLength), 0, arrayData, 0, 4);
        
        // Copy string data (byte array)  into arrayData
//       method getBytes() encodes a String into a byte array using the platform’s default charset if no argument is passed
//      source_arr : array to be copied from ->  string data (byte array)
//      sourcePos : starting position in source array from where to copy -> 0
//      dest_arr : array to be copied in -> arrayData
//      destPos : starting position in destination array, where to copy in -> 4 (since int of string inside alrdy)
//      len : total no. of components to be copied -> length of string data

        System.arraycopy(strData.getBytes(), 0, arrayData, 4, strData.length());

        return arrayData;
    }
    
    public static StringBuilder unmarshString(byte[] aryData, int intOffset) {

    	// Unmarshal the byte array data and offset into the String

    	// get the length of the string using unmarshInt
        int intLength = unmarshInt(aryData, intOffset);
        
        StringBuilder newString = new StringBuilder();

        int intCount = 0;

        // int offset +4 since int is 4 bytes
        intOffset += 4;

        // append the string starting from the first char
        if (intOffset==4) {
        	while (intCount < intLength) {
            	
            	newString.append((char) aryData[intOffset + intCount]);
                intCount++;

        	}
        }
        else if (intOffset>4 & intOffset< intLength) {
        	 int i = intOffset-4;
        	 while (intOffset+intCount < intLength+intOffset-i) {
             	
             	newString.append((char) aryData[intOffset + intCount]);
                intCount++;

         }
        }
        else {
        	newString.append("Fail");
        }

        return newString;
    }
    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int testint = 12;
		
		byte[] testintMarsh = marshInt(testint);
		
		int testintUnmarsh = unmarshInt(testintMarsh,0);
		int testintUnmarsh1 = unmarshInt(testintMarsh,1);
		int testintUnmarsh2 = unmarshInt(testintMarsh,2);
		int testintUnmarsh3 = unmarshInt(testintMarsh,3);

		
		System.out.println("Original test int: "+ testint );
		System.out.println("Marshalled test int byteArray: "+ testintMarsh );
		System.out.println("Marshalled test int byteArray size: "+ testintMarsh.length );

		System.out.println("Unmarshalled test int with 0 offset: "+ testintUnmarsh );
		System.out.println("Unmarshalled test int with 1 offset: "+ testintUnmarsh1 );
		System.out.println("Unmarshalled test int with 2 offset: "+ testintUnmarsh2 );
		System.out.println("Unmarshalled test int with 3 offset: "+ testintUnmarsh3 );

		String teststring = "Hello World!";
		
		byte[] teststringMarsh = marshString(teststring);
		
		StringBuilder teststringUnmarsh = unmarshString(teststringMarsh,0);
		
		System.out.println("Original teststring: "+ teststring );
		System.out.println("Marshalled teststring: "+ teststringMarsh );
		System.out.println("Marshalled teststring size: "+ teststringMarsh.length );

		System.out.println("Unmarshalled teststring with 0 offset: "+ teststringUnmarsh );
		
		StringBuilder teststringUnmarsh1 = unmarshString(teststringMarsh,1);
		System.out.println("Unmarshalled teststring with 1 offset: "+ teststringUnmarsh1 );

		StringBuilder teststringUnmarsh2 = unmarshString(teststringMarsh,2);
		System.out.println("Unmarshalled teststring with 2 offset: "+ teststringUnmarsh2 );

		StringBuilder teststringUnmarsh3 = unmarshString(teststringMarsh,3);
		System.out.println("Unmarshalled teststring with 3 offset: "+ teststringUnmarsh3 );

		

	}

}
