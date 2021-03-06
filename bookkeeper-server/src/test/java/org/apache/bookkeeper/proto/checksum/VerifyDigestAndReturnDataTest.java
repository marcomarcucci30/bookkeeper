package org.apache.bookkeeper.proto.checksum;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.apache.bookkeeper.client.BKException.BKDigestMatchException;
import org.apache.bookkeeper.proto.DataFormats.LedgerMetadataFormat.DigestType;
import org.apache.bookkeeper.proto.checksum.entity.DigestManagerEntity;
import org.apache.bookkeeper.util.ByteBufList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;

@RunWith(Parameterized.class)
public class VerifyDigestAndReturnDataTest {

	private Object expectedResult;
	
	private DigestManagerEntity entity;

	private DigestManager digestManager;
	private DigestManager digestForSending;

	private boolean checked = false;
	
	public void generatesRandomByteBuf(int byteLenght) {
    	if (!checked) {
    		Random rnd = new Random();
        	byte[] data = new byte[byteLenght];
    		rnd.nextBytes(data);
    		
    		ByteBuf bb = Unpooled.buffer(DigestManager.METADATA_LENGTH);
    		bb.writeBytes(data);
    		this.entity.setTestBuf(bb);
    		this.checked = true;
		}
	}
	
	@Parameterized.Parameters
	public static Collection<?> BufferedChannelParameters(){
		return Arrays.asList(new Object[][] {
			
			// Suite minimale
			{new DigestManagerEntity(0, -1, DigestType.HMAC, 0, -1, DigestType.HMAC, 5, false, true), NullPointerException.class},
			{new DigestManagerEntity(0, 1, DigestType.HMAC, 0, 1, DigestType.HMAC, 5, false, false), 0},
			{new DigestManagerEntity(0, 0, DigestType.HMAC, 0, 0, DigestType.HMAC, 5, false, false), 0},
			/*l'unico modo per rendere Invalid l'oggeto ByteBufe è modificare il parametro "lenght". L'oggetto BytebUf, però, se riceve un lenght positivo
			 * viene creato correttamente altrimenti genera un'eccezione al momento dell'istanziazione. Quindi, il parametro ByteBuf deve essere passato
			 * già in maniera corretta alla funzione da testare. Questo ci permette di non considerare il caso di test in cui il parametro ByteBuf è
			 * NON valido. */
			
			//mutation
			{new DigestManagerEntity(1, 1, DigestType.CRC32C, 1, 1, DigestType.CRC32, 0, false, false), BKDigestMatchException.class}
			
			});
	}
	
	public VerifyDigestAndReturnDataTest(DigestManagerEntity entity, Object expectedResult){
		this.entity = entity;
		if (expectedResult.equals(0)) {
			generatesRandomByteBuf(entity.getLength());
			this.expectedResult = entity.getTestBuf();
		}else {
			this.expectedResult = expectedResult;
		}
	}


	@Before
	public void setUp() throws GeneralSecurityException {
		this.digestManager = DigestManager.instantiate(entity.getLedgerIdToTest(), 
				"pass".getBytes(), entity.getDigestTypeToTest(), UnpooledByteBufAllocator.DEFAULT, false);

		this.digestForSending = DigestManager.instantiate(entity.getLedgerID(), 
				"pass".getBytes(), entity.getDigestType(), UnpooledByteBufAllocator.DEFAULT, false);

		generatesRandomByteBuf(entity.getLength());
		
		if (entity.isNull()) {
			entity.setTestBufList(null);
			return;
		}
		
		ByteBufList byteBufList = digestForSending.computeDigestAndPackageForSending(entity.getEntryId(), 0, 
				this.entity.getTestBuf().readableBytes(), this.entity.getTestBuf());
		this.entity.setTestBufList(byteBufList);
	}
	
	@Test
	public void verifyDigestAndReturnDataTest(){
		try {
			ByteBuf result = digestManager.verifyDigestAndReturnData(entity.getEntryIdToTest(), 
					ByteBufList.coalesce(entity.getTestBufList()));
			Assert.assertEquals(expectedResult, result);
		} catch (Exception e) {
			Assert.assertEquals(expectedResult, e.getClass());
		}
	}
}
