package org.apache.bookkeeper.proto.checksum;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.apache.bookkeeper.proto.checksum.entity.DigestManagerEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GenerateMasterKeyTest {
	
	private byte[] pass;
	private Object expectedResult;
	private static int byteLenght = 50;
	static byte[] randomByte = null;
	
	public static void generatesBytes() {
		randomByte = new byte[byteLenght];
		Random rnd = new Random();
		rnd.nextBytes(randomByte);
	}

	@Parameterized.Parameters
	public static Collection<?> BufferedChannelParameters() throws Exception {
		generatesBytes();
		return Arrays.asList(new Object[][] {
			
			// Suite minimale
			{new DigestManagerEntity("".getBytes()), MacDigestManager.EMPTY_LEDGER_KEY},
			{new DigestManagerEntity(randomByte), MacDigestManager.genDigest("ledger", randomByte)}
			

		});
	}
	
	public GenerateMasterKeyTest(DigestManagerEntity entity, Object expectedResult){
		this.pass = entity.getPass();
		this.expectedResult = expectedResult;
	}

	@Test
	public void testGenerateMasterKey() {		
		try {
			byte[] result = DigestManager.generateMasterKey(pass);
			Assert.assertArrayEquals((byte[]) expectedResult, result);
		} catch (Exception e) {
			//nothing
		}
	}
}  


