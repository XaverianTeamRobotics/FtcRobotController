---
sidebar_position: 5
title: Bitwise Operations
sidebar_label: Bitwise Operations
---

# Bitwise Operations

Bitwise operations are operations that are performed on the binary representation of a number. They are very useful for
combining images, using masks, and more.

There are two most common bitwise operations: `and` and `not`. 

There is also `or` and `xor`, but I have yet to find a use for these, so I will not be covering them. You can google them if you want to learn more.

## And

The `and` operation is used to combine two images. This allows us to use masks to only show certain parts of an image.
A common use is to have a mask (say one provided by the `inRange` method) and use it to only show the parts of the image
where the mask is white.

```java
Core.bitwise_and(Mat src1, Mat src2, Mat dst)
```

## Not

The `not` operation is used to invert an image.

```java
Core.bitwise_not(Mat src, Mat dst)
```