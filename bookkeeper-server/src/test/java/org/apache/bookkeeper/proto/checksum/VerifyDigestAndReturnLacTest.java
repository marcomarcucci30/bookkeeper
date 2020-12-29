package org.apache.bookkeeper.proto.checksum;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.bookkeeper.client.BKException.BKDigestMatchException;
import org.apache.bookkeeper.proto.DataFormats.LedgerMetadataFormat.DigestType;
import org.apache.bookkeeper.proto.checksum.entity.DigestManagerEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.netty.buffer.UnpooledByteBufAllocator;

@RunWith(Parameterized.class)
public class VerifyDigestAndReturnLacTest {

	private Object expectedResult;
	private DigestManager digest;
	private DigestManager digestForSending;

	private DigestManagerEntity entity;

	@Parameterized.Parameters
	public static Collection<?> BufferedChannelParameters() {
		return Arrays.asList(new Object[][] {
			
			// Suite minimale
			{new DigestManagerEntity(-1, DigestType.CRC32, 1, true, true), NullPointerException.class},
			{new DigestManagerEntity(-1, DigestType.CRC32, 1, true, false), (long)-1},
			
			// Coverage and mutation
			/*{new DigestManagerEntity(0, DigestType.HMAC,1, false, false), BKDigestMatchException.class},
			{new DigestManagerEntity(1, DigestType.CRC32C, 1, true, false), BKDigestMatchException.class},
			{new DigestManagerEntity(-1, DigestType.CRC32, 0, true, false), BKDigestMatchException.class},*/
		});
	}

	public VerifyDigestAndReturnLacTest(DigestManagerEntity entity, Object expectedResult){
		this.entity=entity;
		this.expectedResult = expectedResult;
	}

	@Before
	public void setUp() throws GeneralSecurityException{
		this.digest = DigestManager.instantiate(1, "pass".getBytes(), 
				DigestType.CRC32, UnpooledByteBufAllocator.DEFAULT, false);
		
		if (!entity.isNull()) {
		
			this.digestForSending = DigestManager.instantiate(entity.getLedgerID(), "pass".getBytes(), 
					entity.getDigestType(), UnpooledByteBufAllocator.DEFAULT, entity.isUseV2Protocol());
			
			entity.setTestBufList(digestForSending.computeDigestAndPackageForSendingLac(entity.getLacID()));
		}
	}

	@Test
	public void verifyDigestAndReturnLacTest(){
		
		long result;
		try {
			if (entity.isNull()) {
				result = digest.verifyDigestAndReturnLac(null);
			}else {
				result = digest.verifyDigestAndReturnLac(entity.getTestBufList().getBuffer(0));
			}
			Assert.assertEquals(expectedResult, result);
		} catch (Exception e) {
			Assert.assertEquals(expectedResult, e.getClass());
		}
	}
	
}  


