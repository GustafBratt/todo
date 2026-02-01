cat Readme.md > all.txt
find *|grep "\.java$" |xargs cat >> all.txt
