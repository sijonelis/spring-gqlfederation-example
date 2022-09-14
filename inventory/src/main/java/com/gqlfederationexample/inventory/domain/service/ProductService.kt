package com.gqlfederationexample.inventory.domain.service

import com.gqlfederationexample.inventory.domain.model.Product
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Service
class ProductService {
    fun lookupProduct(upc: String): Product {
        return try {
            // Why not?
            val quantity = Math.floorMod(
                BigInteger(
                    1,
                    MessageDigest.getInstance("SHA1").digest(upc.toByteArray())
                ).toInt(),
                10000
            )
            Product(upc, quantity)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}