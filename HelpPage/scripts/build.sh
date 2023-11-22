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
# Copy docusaurus build to final output
cp -R ./doc/build ./build
# Copy imgutil build to final output
cp -R ./imgutil/dist ./build/imgutil
# Copy static files to final output
cp -R ./apk ./build/apk
cp -R ./legacy-apk ./build/legacy-apk

echo Build complete.
