cat Readme.md > all.txt
find *|grep "\.java$" |xargs cat >> all.txt
echo "Skrev till all.txt"
