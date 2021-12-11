#!/bin/bash

options="-classpath /home/alan/Documents/contract-net/contract-net-jade/bin:/home/alan/Documents/contract-net/contract-net-jade/lib/jade.jar -XX:+ShowCodeDetailsInExceptionMessages"


#Experiment 1a
path="results/1a"
ser=1
par=30
for ini in 100 110 120
do
  for i in {1..5}
  do
    java  $options StartJade $ini $par $ser | tee $path/"1a-"$ini"i-"$par"p-"$ser"s".txt
    printf "\n\n\n"
  done
done

exit 1

#Experiment 1b
path="results/1b"
ser=1
ini=100
for par in 40 50
do
  for i in {1..5}
  do
    java  $options StartJade $ini $par $ser | tee $path/"1b-"$ini"i-"$par"p-"$ser"s".txt
    printf "\n\n\n"
  done
done

#Experiment 2a
path="results/2a"
ser=1
par=30
for ini in 5 10 20
do
  for i in {1..5}
  do
    java  $options StartJade $ini $par $ser | tee $path/"2a-"$ini"i-"$par"p-"$ser"s".txt
    printf "\n\n\n"
  done
done

#Experiment 2b
path="results/2b"
ser=1
ini=5
for par in 30 35 45
do
  for i in {1..5}
  do
    java  $options StartJade $ini $par $ser | tee $path/"2b-"$ini"i-"$par"p-"$ser"s".txt
    printf "\n\n\n"
  done
done

#Experiment 3a
path="results/3a"
ini=100
par=30
for ser in 5 10
do
  for i in {1..5}
  do
    java  $options StartJade $ini $par $ser | tee $path/"3a-"$ini"i-"$par"p-"$ser"s".txt
    printf "\n\n\n"
  done
done

#Experiment 3a
path="results/3b"
ini=5
par=30
for ser in 5 10
do
  for i in {1..5}
  do
    java  $options StartJade $ini $par $ser | tee $path/"3b-"$ini"i-"$par"p-"$ser"s".txt
    printf "\n\n\n"
  done
done
