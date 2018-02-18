# Introduction

This project is a prototype in order to materialize concepts described in the following OWASP cheatsheet, in *Leverage an adaptive one-way function* section:

https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet#Leverage_an_adaptive_one-way_function

The objective is to propose a example of secure usage/integration of the **Argon2** algorithm in Java application to protect password when stored by using:
* The [PHC reference implementation](https://github.com/P-H-C/phc-winner-argon2) of Argon2.
* The Java binding [Argon-JVM](https://github.com/phxql/argon2-jvm/).

# Configuration options

Argon2 options used are defined [here](src/main/resources/config.properties).

# Argon2 library compilation

The TravisCI integration file **.travis.yml** show an example of integration of the building of the Argon2 library in the process of build of a project.

# Run unit tests

Run the following command line `gradlew test` from the project root folder to run the tests suites validating that the code provided is functional.
