#!/bin/sh

for n in hogajama mongodb amq; do
  ( cd $n && ./install.sh )
done
