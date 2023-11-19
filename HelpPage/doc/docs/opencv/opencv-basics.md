---
sidebar_position: 0
title: OpenCV Basics
sidebar_label: OpenCV Basics
pagination_prev: null
---

# OpenCV Basics

## What is OpenCV?

OpenCV is a library for computer vision. It includes a lot of useful functions for image processing, 
such as color detection, object detection, and more. It works differently than object detectors like TensorFlow. Instead of using a neural network to detect objects,
OpenCV allows you to program your own set of algorithms to detect objects or patterns in images. It is the most difficult to program, as you need to program the logic yourself, but it is also the most flexible and
doesn't require any pre-trained models.

Normally, OpenCV is a pain to work with cameras in the FTC framework, but we have a library called EasyOpenCV which simplifies
the process a lot.

## How does OpenCV work?

OpenCV works by using components called "pipelines". Each pipeline gets an image, and must return that same image. We can
modify the original image and create additional images along the way, but in the end we are responsible for "returning it
as we found it". Images are stored in the Mat type (short for "matrix"). Whenever we create an additional image, if we 
don't clean it up, it stays in memory. This is because OpenCV is written in C++, so Java's garbage collector cannot clean up 
images. Computers only have so much memory, so forgetting to destroy an image can fill up memory really quickly. 
With our images, we can convert color schemes, extract color channels, run blob detection, edge detection, color analysis, etc.