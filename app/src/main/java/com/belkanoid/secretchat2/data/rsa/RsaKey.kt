package com.belkanoid.secretchat2.data.rsa

import android.util.Log
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.PRIVATE_KEY
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.PUBLIC_KEY
import java.io.IOException
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.inject.Inject

class RsaKey @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private var privateKey: PrivateKey
    private var publicKey: PublicKey


    // convert String publickey to Key object
    @Throws(GeneralSecurityException::class, IOException::class)
    fun loadPublicKey(stored: String): Key {
        val data: ByteArray = Base64.getDecoder().decode(stored.toByteArray())
        val spec = X509EncodedKeySpec(data)
        val fact = KeyFactory.getInstance("RSA")
        return fact.generatePublic(spec)
    }

    // Encrypt using publickey
    @Throws(Exception::class)
    fun encryptMessage(plainText: String, publickey: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(publickey))
        return Base64.getEncoder().encodeToString(
            cipher.doFinal(plainText.toByteArray())
        )
    }

    // Decrypt using privatekey
    @Throws(Exception::class)
    fun decryptMessage(encryptedText: String, privatekey: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(privatekey))
        return String(
            cipher.doFinal(Base64.getDecoder().decode(encryptedText))
        )
    }

    // Convert String private key to privateKey object
    @Throws(GeneralSecurityException::class)
    fun loadPrivateKey(key64: String): PrivateKey {
        val clear: ByteArray = Base64.getDecoder().decode(key64.toByteArray())
        val keySpec = PKCS8EncodedKeySpec(clear)
        val fact = KeyFactory.getInstance("RSA")
        val priv = fact.generatePrivate(keySpec)
        Arrays.fill(clear, 0.toByte())
        return priv
    }


    init {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(1024)
        val pair = keyGen.generateKeyPair()
        privateKey = pair.private
        publicKey = pair.public

        sharedPreferences.putString(
            PRIVATE_KEY,
            Base64.getEncoder().encodeToString(privateKey.encoded)
        )
        log("Private Key Generated")
        sharedPreferences.putString(
            PUBLIC_KEY,
            Base64.getEncoder().encodeToString(publicKey.encoded)
        )
        log("Public Key Generated")
    }


    companion object {
        fun log(text: String) {
            Log.i(RsaKey::class.java.name, text)
        }
    }
}