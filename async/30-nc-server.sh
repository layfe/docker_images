#!/bin/sh
while true ; do  echo "HTTP/1.1 200 OK\r\n\r\n" | nc -l -q 0 -p 6000  ; done &
