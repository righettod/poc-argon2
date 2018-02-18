#!/bin/sh
RELEASE_NAME=20171227
wget https://github.com/P-H-C/phc-winner-argon2/archive/$RELEASE_NAME.zip
unzip $RELEASE_NAME.zip
cd phc-winner-argon2-$RELEASE_NAME
make
make test
mkdir ../src/main/resources/linux-x86-64
cp libargon2.so.1 ../src/main/resources/linux-x86-64/libargon2.so
cd ..
rm -rf phc-winner-argon2-$RELEASE_NAME
rm $RELEASE_NAME.zip
exit 0
