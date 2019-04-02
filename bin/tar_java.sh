#!/bin/bash
rm -f *.tar.gz
tar czvf $(java -version 2>&1 | grep Environment | cut -d "(" -f2 | cut -d ")" -f1 | awk '{print $2'}).tar.gz /usr/lib/jvm/
