package org.apache.bookkeeper.bookie.storage.ldb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.entity.WriteCacheEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class PutTest {

    private WriteCache writeCache;
    private ByteBufAllocator byteBufAllocator;
    private ByteBuf entry;
    private WriteCacheEntity writeEntity;
    private boolean expectedResults;
	private int expectedCount;


    @Parameterized.Parameters
    public static Collection<?> getParameters(){
    final int entryNumber = 10;
    final int kilo = 1024;
    final int mega = 1 * 1024 * 1024;
    final int giga = 1 * 1024 * 1024 * 1024;
    /*MUTATION COVERAGE:
	 * 
	 * -LINE 168: Mutation equivalent (strong mutation)
	 * */
    
    /*
     * ledgerId e entryId >= 0 
     * cambiare 3 par con null */
        return Arrays.asList(new Object[][] {
        	//suite minimale
        	
    		{new WriteCacheEntity(1, 1, true, kilo * entryNumber, kilo, kilo, false), true, 2},
    		{new WriteCacheEntity(0, -1, false, kilo * entryNumber, kilo, giga, false), false, 0},
            {new WriteCacheEntity(-1, 0, true, kilo * entryNumber, kilo, giga, false), false, 0},
            
            //coverage 
            /*{new WriteCacheEntity(1, 2, true, kilo / entryNumber, kilo, giga, false), false, 0},
            {new WriteCacheEntity(1, 2, true, 2 * mega, mega, mega/2, false), false, 0},
            {new WriteCacheEntity(1, 1, true, kilo * 2, kilo, giga, true), true, 2},*/
            

            //ridondante
            //{new WriteTestEntity(1, -1, true, kilo * entryNumber, kilo, giga, false), false},
                
        });
    }

    public PutTest(WriteCacheEntity writeEntity, boolean expectedResults, int expectedCount){
        this.writeEntity = writeEntity;
        this.expectedResults = expectedResults;
        this.expectedCount = expectedCount;
    }

    @Before
    public void setUp() throws Exception {
        byteBufAllocator = UnpooledByteBufAllocator.DEFAULT;
        writeCache = new WriteCache(byteBufAllocator, writeEntity.getCacheSize(), writeEntity.getSegmentSize());
        if(writeEntity.isValidEntry()) {
        	
            entry = byteBufAllocator.buffer(writeEntity.getEntrySize());
            ByteBufUtil.writeAscii(entry, "test");
            entry.writerIndex(entry.capacity());
            
            if (writeEntity.getSegmentSize() == 1024) {
            	writeCache.put(3, 3, entry);
            }
        }
        else {
            entry = null;
        }
        
        
    }



    @Test
    public void putTest(){

        boolean result;

        try{

            result = writeCache.put(writeEntity.getLedgerId(), writeEntity.getEntryId(), entry);
            
            if(writeEntity.isDoublePut()) {
            	result = writeCache.put(writeEntity.getLedgerId(), writeEntity.getEntryId() - 1, entry);
            }
        }
        catch (Exception e){
            result = false;
        }

        Assert.assertEquals(this.expectedResults, result);
                
        //for mutation line 178
        Assert.assertEquals(this.expectedCount, this.writeCache.count());

    }
}