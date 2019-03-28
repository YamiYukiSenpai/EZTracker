#!/bin/bash
touch hello.txt

echo $1 $2 | tee hello.txt
