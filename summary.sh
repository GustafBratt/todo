cat Readme.md > all.txt

find . -name "build.gradle" -print0 | xargs -0 tail -n +1 >> all.txt
find . -name "*.java" -print0 | xargs -0 tail -n +1 >> all.txt
