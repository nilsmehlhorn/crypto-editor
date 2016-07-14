# crypto-editor
Simple text-editor to illustrate encryption techniques

This project is based on a learning incentive for implementing a system for secure operations.

## Installation

### JCE Unrestricted Policies
To use all provided encryption functionalities (e.g. keylength above 128Bit) make sure to install the [unrestricted policies](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html) for the Java Cryptography Extension

### Download compiled release
Download a version from the [release list](https://github.com/nilsmehlhorn/crypto-editor#releases) and run the jar via ```java -jar <filename>```

### Build it yourself
1. Clone the repository via ```git clone https://github.com/nilsmehlhorn/crypto-editor.git```
2. Build the jar using Maven via ```mvn package```


## Cryptography

### Symmetric Encryption

Currently supported ciphers are
* AES
* DES
* ARC4

Currently supported block cipher modes are
* ECB
* CBC
* CTR
* OFB
* CFB
* GCM

Currently supported padding methods are
* No Padding (will not work in every case e.g. if content does not represent a multiple of the used block size)
* PKCS5
* PKCS7

### Password Based Encryption

Currently supported schemes are
* 128Bit AES in CBC mode with SHA1
* 256Bit AES in CBC mode with SHA1
* DES with MD5
* 40Bit ARC4 with SHA1

### Asymmetric Encryption

At the moment only RSA encryption is supported

---

You also have the possiblity to save files without encryption.
## Releases

| Latest Snapshot | Release 0.2 | 
| ---             | ---         |
|[Documentation](http://nilsmehlhorn.github.io/crypto-editor/snapshot/doc/doxygen)|[Documentation](http://nilsmehlhorn.github.io/crypto-editor/doc/release/0.2)|
|[Coverage](http://nilsmehlhorn.github.io/crypto-editor/snapshot/coverage/)| - |
|[Download](http://nilsmehlhorn.github.io/crypto-editor/snapshot/build/crypto-editor-0.2.2-bundle.zip)| - |
