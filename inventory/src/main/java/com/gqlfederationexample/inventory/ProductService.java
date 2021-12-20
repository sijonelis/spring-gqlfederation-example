package com.gqlfederationexample.inventory;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ProductService {
    public Product lookupProduct(String upc) {
        try {
            // Why not?
            int quantity = Math.floorMod(
                    new BigInteger(1,
                            MessageDigest.getInstance("SHA1").digest(upc.getBytes())
                    ).intValue(),
                    10_000);

            return new Product(upc, quantity);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
