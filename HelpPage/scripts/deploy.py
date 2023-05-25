import os
import shutil

# Build docusaurus site
os.system("cd ../doc && npm run build")

# Prep build output
if os.path.exists("../opt"):
    os.rmdir("../opt")
os.mkdir("../opt")

# Copy docusaurus build to final output
shutil.copytree("../doc/build", "../opt")

# Copy static files to final output
