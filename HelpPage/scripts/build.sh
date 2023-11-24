#!/bin/sh

set -eu

echo Running build...
rm -rf ../build

echo Building Docusaurus...
cd ../doc
npm install
npm run build
echo Docusaurus build complete.

echo Building imgutil...
cd ../imgutil
npm install
npm run build
echo Imgutil build complete.

echo "Bundling..."
cd ..
mkdir output
# Move docusaurus build to final output
mv ./doc/build ./build
# Move imgutil build to final output
mv ./imgutil/dist ./build/imgutil
# Move static files to final output
mv ./apk ./build/apk
mv ./legacy-apk ./build/legacy-apk

echo Build complete.
