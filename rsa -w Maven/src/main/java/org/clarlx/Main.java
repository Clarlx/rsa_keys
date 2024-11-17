package org.clarlx;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.lang.String;

public class Main {

    //gets the string from the user and sees if it's actually empty
    public static String input(){
        Scanner scan = new Scanner(System.in);
        String _text;

        System.out.println("Please insert your phrase");
        _text = scan.nextLine();
        if(_text.isEmpty()){
            while(_text.isEmpty()){
                System.out.println("Please insert your phrase again, invalid value given before");
                _text = scan.nextLine();
            }
        }
        return _text;
    }

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        final int BIT_LENGTH = 1024;

        //initializing the variables for the values
        BigInteger value1, value2;
        value1 = value2 = BigInteger.ZERO;

        //generating two prime numbers
        System.out.println("Generating two random prime numbers...");
        value1 = BigInteger.probablePrime(BIT_LENGTH, random);
        value2 = BigInteger.probablePrime(BIT_LENGTH, random);

        System.out.println("Prime 1: " + value1);
        System.out.println("Prime 2: " + value2);

        //obtaining the module of n
        BigInteger n = value1.multiply(value2);

        //Euler function
        BigInteger phi = (value1.subtract(BigInteger.ONE)).multiply(value2.subtract(BigInteger.ONE));

        //coprime
        BigInteger e;
        do {
            e = new BigInteger(512, random);
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phi) >= 0);

        /*
        BigInteger primeArr[] = {BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7),
                BigInteger.valueOf(11), BigInteger.valueOf(13), BigInteger.valueOf(17),
                BigInteger.valueOf(19), BigInteger.valueOf(23), BigInteger.valueOf(29)};

        BigInteger e = primeArr[(int)(Math.random() * primeArr.length)];
         */

        //calculate private exponent d (d ≡ e^(-1) mod φ(n))
        BigInteger d = e.modInverse(phi);
        System.out.println("Private exponent (d): " + d);

        //printing the keys
        System.out.println("Public Key: (e: " + e + ", n: " + n + ")");
        System.out.println("Private Key: (d: " + d + ", n: " + n + ")");

        //get the message to encrypt
        String message = input();

        //convert message to BigInteger
        BigInteger plaintext = new BigInteger(1, message.getBytes());
        if (plaintext.compareTo(n) >= 0) {
            System.out.println("Error: Message is too large for encryption with the given keys.");
            return;
        }

        //encrypt the message  c = m^e mod n
        plaintext = new BigInteger(message.getBytes());
        BigInteger ciphertext = plaintext.modPow(e, n);
        System.out.println("Encrypted Message: " + ciphertext);

        //decrypt the message  m = c^d mod n
        BigInteger decrypted = ciphertext.modPow(d, n);
        String decryptedMessage = new String(decrypted.toByteArray());
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
}