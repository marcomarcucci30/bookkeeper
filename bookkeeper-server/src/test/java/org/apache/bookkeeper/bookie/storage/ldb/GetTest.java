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
public class GetTest { 

    private WriteCache writeCache;
    private ByteBufAllocator byteBufAllocator;
    private final int entryNumber = 10;
    private final int entrySize = 1024;
    private ByteBuf entry;
    private WriteCacheEntity writeEntity;
    private ByteBuf expectedResults;


    //Parametri in input
    @Parameterized.Parameters
    public static Collection<?> getParameters(){
        return Arrays.asList(new Object[][] {
        	//Ledger id >= 0
        	
        	//suite minimale
            {new WriteCacheEntity(0, 0), null}, //sovrascritto dal setup: ByteBuff immesso in cache
            {new WriteCacheEntity(-1, -1), null},
            {new WriteCacheEntity(1, 1), null},
            
            //branch e line coverage
            {new WriteCacheEntity(0, -1), null},
        });
    }
    
    @Before
    public void setUp() throws Exception {
        byteBufAllocator = UnpooledByteBufAllocator.DEFAULT;
        /*cache formata da segmenti
         * segmenti composti da più entry
         * 
         * piu entry = ledger
         * piu ledger = bokkie
         * */
        
        /*se mettiamo 1024 come segment max, allora l'id del segmento che andiamo a considerare
         * quando ricerchiamo una entry aumenta.*/
        writeCache = new WriteCache(byteBufAllocator, entrySize * entryNumber, 1024);

        entry = byteBufAllocator.buffer(entrySize);
        ByteBufUtil.writeAscii(entry, "test");
        entry.writerIndex(entry.capacity());
        
        if(writeEntity.getLedgerId()>=0 && writeEntity.getEntryId()>=0) {
        	writeCache.put(2, 2, entry);

            writeCache.put(writeEntity.getLedgerId(), writeEntity.getEntryId(), entry);
        	this.expectedResults = entry;
        }

    }

    public GetTest(WriteCacheEntity writeEntity, ByteBuf expectedResults){
        this.writeEntity = writeEntity;
        this.expectedResults = expectedResults;
    }
    
    @Test
    public void getTest(){

        ByteBuf result = null;

        try{
            result = writeCache.get(writeEntity.getLedgerId(), writeEntity.getEntryId());
        }
        catch(Exception e){
            result = null;
        }
        
        Assert.assertEquals(expectedResults, result);

    }


}