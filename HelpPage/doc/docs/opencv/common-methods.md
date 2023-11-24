---
sidebar_position: 4
title: Common Methods
sidebar_label: Common Methods
---

# Common Methods

While we can't go over every single method in OpenCV, we will briefly go over some of the more common methods that will
depend on what you are trying to accomplish.

We are always adding more methods to this page, but if you don't see a method you need, you can always google it.

## Mean value
```java
Core.mean(Mat mat)
```

Find the mean value of an image.

**Parameters:**
- `mat`: The image to find the mean value of.

**Returns:** A `Scalar` object containing the mean value of the image, seperated by channel.

**Example:**
```java
Scalar mean = Core.mean(input);

mean.val[0]; // Mean value of the first channel
mean.val[1]; // Mean value of the second channel
mean.val[2]; // Mean value of the third channel
```

## In Range
```java
Core.inRange(Mat src, Scalar lowerBound, Scalar upperBound, Mat dst)
```

Get a binary image of the pixels in a certain range. If the pixel is in the range, it will be white, otherwise it will be black.

**Parameters:**
- `src`: The image to find the mean value of.
- `lowerBound`: The lower bound of the range.
- `upperBound`: The upper bound of the range.
- `dst`: The output image.

**Returns:** None

**Example:**
```java
Scalar lowerBound = new Scalar(0, 0, 0);
Scalar upperBound = new Scalar(255, 255, 255);
Mat dst = new Mat();

Core.inRange(input, lowerBound, upperBound, dst);
```

## Split
```java
Core.split(Mat src, List<Mat> dst)
```

Split an image into its channels.

**Parameters:**
- `src`: The image to split.
- `dst`: The list of images to store the channels in.

**Returns:** None

**Example:**
```java
ArrayList<Mat> channels = new ArrayList<>();
Core.split(input, channels);
```