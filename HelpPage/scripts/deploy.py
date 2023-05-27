import os
import shutil


def run():

    print("Running build...")
    print("Building Docusaurus...")

    # Build docusaurus site
    os.system("cd ../doc && npm run build")

    print("Docusaurus build complete.")

    # Prep build output
    maybe_rmdir("../opt")

    print("Bundling...")

    # Copy docusaurus build to final output
    shutil.copytree("../doc/build", "../opt")

    # Copy static files to final output
    maybe_rmdir("../opt/apk")
    shutil.copytree("../apk", "../opt/apk")
    maybe_rmdir("../opt/book")
    shutil.copytree("../book", "../opt/book")
    maybe_rmdir("../opt/legacy-apk")
    shutil.copytree("../legacy-apk", "../opt/legacy-apk")
    maybe_rmdir("../opt/kdoc")
    shutil.copytree("../kdoc", "../opt/kdoc")

    print("Cleaning up artifacts...")

    # Delete extra build artifacts
    maybe_rmdir("../doc/build")

    print("Build complete.")

    exit(0)


def maybe_rmdir(pth):
    if os.path.exists(pth):
        shutil.rmtree(pth)


run()
