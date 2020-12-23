package org.apache.bookkeeper.proto.checksum.entity;

import org.apache.bookkeeper.proto.DataFormats.LedgerMetadataFormat.DigestType;
import org.apache.bookkeeper.util.ByteBufList;

import io.netty.buffer.ByteBuf;

public class DigestManagerEntity {
	
	private int length;
	private int lacID, entryId, entryIdToTest; 
	private DigestType digestType;
	private DigestType digestTypeToTest;
	private int ledgerID, ledgerIdToTest;
	private boolean badByteBufList;
	private ByteBuf testBuf;
	private ByteBufList testBufList;
	private boolean isNull;
	
	private byte[] pass;
	private boolean useV2Protocol;
	
	public DigestManagerEntity(byte[] bs) {
		this.pass = bs;
	}
	
	public DigestManagerEntity(int lacID, DigestType digestType, int ledgerID, boolean useV2Protocol, boolean isNull) {
		this.lacID = lacID;
		this.digestType = digestType;
		this.ledgerID = ledgerID;
		this.useV2Protocol = useV2Protocol;
		this.isNull = isNull;
	}
	
	
	public DigestManagerEntity(int ledgerIdToTest, int entryIdToTest, DigestType digestTypeToTest, 
			int ledgerID, int entryId, DigestType digestType, int length, boolean badByteBufList, 
			boolean isNull) {
		this.entryId = entryId;
		this.entryIdToTest = entryIdToTest;
		this.ledgerIdToTest = ledgerIdToTest;
		this.digestType = digestType;
		this.digestTypeToTest = digestTypeToTest;
		this.ledgerID = ledgerID;
		this.badByteBufList = badByteBufList;
		this.length = length;
		this.isNull = isNull;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLacID() {
		return lacID;
	}

	public void setLacID(int lacID) {
		this.lacID = lacID;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public int getEntryIdToTest() {
		return entryIdToTest;
	}

	public void setEntryIdToTest(int entryIdToTest) {
		this.entryIdToTest = entryIdToTest;
	}

	public DigestType getDigestType() {
		return digestType;
	}

	public void setDigestType(DigestType digestType) {
		this.digestType = digestType;
	}

	public DigestType getDigestTypeToTest() {
		return digestTypeToTest;
	}

	public void setDigestTypeToTest(DigestType digestTypeToTest) {
		this.digestTypeToTest = digestTypeToTest;
	}

	public int getLedgerID() {
		return ledgerID;
	}

	public void setLedgerID(int ledgerID) {
		this.ledgerID = ledgerID;
	}

	public int getLedgerIdToTest() {
		return ledgerIdToTest;
	}

	public void setLedgerIdToTest(int ledgerIdToTest) {
		this.ledgerIdToTest = ledgerIdToTest;
	}

	public boolean isBadByteBufList() {
		return badByteBufList;
	}

	public void setBadByteBufList(boolean badByteBufList) {
		this.badByteBufList = badByteBufList;
	}

	public ByteBuf getTestBuf() {
		return testBuf;
	}

	public void setTestBuf(ByteBuf testBuf) {
		this.testBuf = testBuf;
	}

	public ByteBufList getTestBufList() {
		return testBufList;
	}

	public void setTestBufList(ByteBufList testBufList) {
		this.testBufList = testBufList;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public byte[] getPass() {
		return pass;
	}

	public void setPass(byte[] pass) {
		this.pass = pass;
	}

	public boolean isUseV2Protocol() {
		return useV2Protocol;
	}

	public void setUseV2Protocol(boolean useV2Protocol) {
		this.useV2Protocol = useV2Protocol;
	}
	
	
}
