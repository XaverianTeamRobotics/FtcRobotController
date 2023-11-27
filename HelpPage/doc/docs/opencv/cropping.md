---
sidebar_position: 3
title: Cropping Images
sidebar_label: Cropping Images
---

# Cropping Images

Cropping an image is a very simple operation. First, we need to establish a rectangle defining the region which we want
to take a section of (referred to as a _**Region of Interest**_, or **_roi_**).

## Defining a Rectangle

To define a rectangle, we need to create a `Rect` object. There are multiple constructors for the `Rect` class, one uses two `Point` objects
and the other uses four `int`s. Both constructors create the same rectangle, but they are used differently. When using 
the four `int` method, the first two `int`s are the x and y coordinates of the top-left corner of the rectangle, and the
last two `int`s are the width and height of the rectangle. When using the two `Point` method, the first `Point` is the top-left
corner of the rectangle, and the second `Point` is the bottom-right corner of the rectangle.

### Using `Point`s

To use the two `Point` method, we need to know the top-left and bottom-right corners of the rectangle. We can create two `Point` objects
to represent these corners, then pass them into the `Rect` constructor.

```java
Point topLeft = new Point(0, 0);
Point bottomRight = new Point(100, 100);
Rect roi = new Rect(topLeft, bottomRight);
```

This will create a rectangle with the top-left corner at (0, 0) and the bottom-right corner at (100, 100).

### Using `int`s
Using the four `int` method is different. Instead of using the top-left and bottom-right corners, we use the top-left corner and the width and height of the rectangle.

```java
Rect roi = new Rect(0, 0, 100, 100);
```

This will create the same rectangle as the previous example.

## Extracting the Region of Interest

Once we have our rectangle, we can use it to extract the region of interest from the image. To do this, we will create a new `Mat` object
and use the `submat` method to extract the region of interest.

```java
Mat cropped = input.submat(roi);
```

This cropped image will be a reference to the original image, so any changes made to the cropped image will also be made to the original image.
Now this is an interesting property, but it can also be a problem. If we don't want this behavior, we can use the `clone` method to create a copy of the image.

```java
Mat cropped = input.submat(roi).clone();
```

The `clone` method creates a copy of the image, so any changes made to the cropped image will not be made to the original image.