# Introduction

This project is a prototype in order to materialize prevention concepts described in the following OWASP cheatsheet, in *Leverage an adaptive one-way function* section:

https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet#Leverage_an_adaptive_one-way_function

The objective is to propose a example of secure usage/integration of the Argon2 algorithm in Java application.

# Technical choice

[libsodium](https://download.libsodium.org/doc/) propose a implementation but we have decided to use a dedicated project because libsodium provide several crypto feature and thus work from maintainer will not focus on Argon2 implementation (however project is active and propose also bindings for many technologies).

The Argon2 implementation provided by [phc-winner-argon2](https://github.com/P-H-C/phc-winner-argon2) project has been used because:
* It's the reference implementation of this algorithm.
* It's dedicated to this new algorithm so all work by the maintainer are focused on the implementation.
* Project is active, [last release](https://github.com/P-H-C/phc-winner-argon2/blob/master/CHANGELOG.md) date from december 2017.
* There bindings for many technologies.

Java bindings by [phxql](https://github.com/phxql/argon2-jvm) has been used because it's the only currently proposed for Java in the [bindings list](https://github.com/P-H-C/phc-winner-argon2#bindings).

Due to the kind of data processed (password), the implementation without the embedded pre-compiled native libraries has been used in order to don't embed native untrusted compiled code (even if project owner of argon2-jvm is a nice guy  :+1: , is just a secure approach) that will be difficult to validate (for java part, sources are provided in Maven repositories along compiled one and jar files can be decompiled if needed):

https://github.com/phxql/argon2-jvm/blob/master/docs/compile-argon2.md

Always name the compiled library with this format: 
* For Windows: **argon2**.dll
* For Linux: **libargon2**.so
* For OSX: **libargon2**.dylib

# Integration in company projects

Integration in company projects can use the following approach:
1. Create a internal shared java utility library that embeed your compiled version of the Argon2 library.
2. Use this shared java library in the differents project in order to:
    * Prevent to embed a version of the Argon2 library in all your project.
    * Centralize and unify the version of the Argon2 library used (important for upgrade process).

# Proposed configuration options

Proposed configuration options for Argon2 are based on the following source of recommendation:
* https://github.com/P-H-C/phc-winner-argon2/issues/59.
* Section 9 of the Argon2 [specifications document](https://github.com/P-H-C/phc-winner-argon2/blob/master/argon2-specs.pdf).

Configuration is the following:

```
# Configuration to define Argon2 options
# See https://github.com/P-H-C/phc-winner-argon2#command-line-utility
# See https://github.com/phxql/argon2-jvm/blob/master/src/main/java/de/mkammerer/argon2/Argon2.java
# See https://github.com/P-H-C/phc-winner-argon2/issues/59
#
# Number of iterations, here adapted to take at least 2 seconds
# Tested on the following environments:
#   ENV NUMBER 1: LAPTOP - 15 Iterations is enough to reach 2 seconds processing time
#       CPU: Intel Core i7-2670QM 2.20 GHz with 8 logical processors and 4 cores
#       RAM: 24GB but no customization on JVM (Java8 32 bits)
#       OS: Windows 10 Pro 64 bits
#   ENV NUMBER 2: TRAVIS CI LINUX VM - 15 Iterations is NOT enough to reach 2 seconds processing time (processing time take 1 second)
#       See details on https://docs.travis-ci.com/user/reference/overview/#Virtualisation-Environment-vs-Operating-System
#       "Ubuntu Precise" and "Ubuntu Trusty" using infrastructure "Virtual machine on GCE" were used (GCE = Google Compute Engine)
ITERATIONS=30
# The memory usage of 2^N KiB, here set to recommended value from Issue n°9 of PHC project (128 MB)
MEMORY=128000
# Parallelism to N threads here set to recommended value from Issue n°9 of PHC project
PARALLELISM=4
```

# Run unit tests

Run the following command line `gradlew test` from the project root folder.

# TODO

- [x] Add TravisCI build with Argon2 lib auto compilation for Linux
- [ ] Update OWASP article with POC information
