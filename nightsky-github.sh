#!/bin/bash

set -x

cp -rfp src/nightskyataglance/ ../github/NightSkyAtAGlance/src/
cp -rfp bin/nightskyataglance/ ../github/NightSkyAtAGlance/bin
cp -rfp src/lib/ ../github/NightSkyAtAGlance/src/
cp -rfp bin/lib/ ../github/NightSkyAtAGlance/bin/
cp -rfp data/nightsky/catalogs/ data/nightsky/version.txt data/nightsky/hash.txt ../github/NightSkyAtAGlance/data/nightsky/
cp nightsky-manifest.txt nightsky-mkjar.sh nightsky-github.sh nightsky.sh ../github/NightSkyAtAGlance/

set +x
