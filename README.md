# crypto-editor
Simple text-editor to illustrate encryption techniques

This project is based on a learning incentive for implementing a system for secure operations.

## Installation

### Build it yourself
1. Clone the repository via ```git clone https://github.com/nilomatix/crypto-editor.git```
2. Build the jar using Maven via ```mvn jfx:jar``` or run it directly via ```mvn jfx:run```

### Download compiled release
(coming soon)

## Cryptography

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

You also have the possiblity to save files without encryption.
## Documentation
[Latest Snapshot](http://nilomatix.github.io/crypto-editor/doc/snapshot)
