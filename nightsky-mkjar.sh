#!/bin/bash

for i in $* ; do
    if [[ $1 == [Cc][Oo][Mm][Pp][Ii][Ll][Ee] ]] ; then
	export COMPILE=true
    elif [[ $1 == [Cc][Oo][Pp][Yy] ]] ; then
	export COPY=true
    fi
done

set -x

hostname=`hostname`

java_list=( `java --version` )
java_version=${java_list[1]}

target=1.8
root=`pwd`
src=$root/src
bin=$root/bin-$java_version
bin=$root/bin-$target
bin=$root/bin
# mkdir -p $bin
# set +x
# echo javac -d $bin '-source 1.8 -target 1.8 src/lib/*/*.java src/nightskyataglance/*.java src/nightskyataglance/*/*.java '
# time javac -d $bin  -source 1.8 -target 1.8 src/lib/*/*.java src/nightskyataglance/*.java src/nightskyataglance/*/*.java  
# set -x

if [[ $COMPILE = true ]] ; then
    echo javac -d $bin 'src/lib/*/*.java src/nightskyataglance/*.java src/nightskyataglance/*/*.java'
    time javac -d $bin  src/lib/*/*.java src/nightskyataglance/*.java src/nightskyataglance/*/*.java  
fi

# nightsky_version=`java -cp $bin nightskyataglance.NightSkyAtAGlance --version | sed -e 's/[-].*$//'`
date +"%Y.%m.%d" > data/nightsky/version.txt
nightsky_version=`cat data/nightsky/version.txt`
jar_version=$nightsky_version


nightsky_jar=nightsky-${jar_version}.jar
cat << EOF > nightsky.sh
#!/bin/bash 

java -jar ${nightsky_jar}

EOF
pushd $bin
# time jar cfm ../$nightsky_jar ../nightsky-manifest.txt nightskyataglance lib ../nightsky.sh ../data/nightsky/version.txt ../data/nightsky/catalogs
time jar cfm ../$nightsky_jar ../nightsky-manifest.txt nightskyataglance lib ../src/nightskyataglance ../src/lib ../nightsky.sh ../data/nightsky/version.txt ../data/nightsky/catalogs
popd

if [[ $COPY = true ]] ; then
    cp -f $nightsky_jar ~/Desktop
fi

mv $nightsky_jar jar

set +x
