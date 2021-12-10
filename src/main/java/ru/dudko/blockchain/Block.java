package ru.dudko.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);

    private String hash;
    private final String prevHash;
    private String data;
    private final long timeCreate;
    private int nonce;

    public Block(String data, String prevHash, long timeCreate) {
        this.data = data;
        this.prevHash = prevHash;
        this.timeCreate = timeCreate;
        this.hash = calculateBlockHash();
    }

    public String calculateBlockHash() {
        String dataToHash = prevHash + timeCreate + nonce + data;
        MessageDigest digest;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(UTF_8));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
        }
        StringBuilder builder = new StringBuilder();
        assert bytes != null;
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    //TODO: Create data verification

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setData(String data) {
        this.data = data;
    }

}
