---
sidebar_position: 1
title: Creating Pipelines
sidebar_label: Creating Pipelines
pagination_prev: null
---

# Creating Pipelines

## Pipeline Template

All pipelines must extend the `VisionPipeline` class. They should be stored in the `internals.image.<game-name>` folder. 

Here is a quick, basic template for creating a pipeline.
```java
public class BlankVisionPipeline extends VisionPipeline {
    @Override
    public void init(Mat mat) {
        
    }

    @Override
    public Mat processFrame(Mat input) {
        return input;
    }
}
```
:::danger
You **_MUST_** return `input` in the `processFrame` method.
:::

The `init` method is optional, but `processFrame` is not.

## Using Pipelines

There is a universal Feature that works for any pipeline extending the `VisionPipeline` class, called `VisionProcessingFeature`.
It has one constructor parameter, which is your pipeline.

```java
VisionProcessingFeature detector = new VisionProcessingFeature(new <your_pipeline>());
registerFeature(detector);
```

:::tip
You should store the feature in a variable, so you can get info from it later.
:::

## Viewing the output

Once you have an OpMode set up with just your basic pipeline, you might want to see it in action (granted it won't actually do anything).
Once you start the OpMode, you have to connect your computer to the Wi-Fi network navigate to the 
[Robot Dashboard](http://192.168.43.1:8080/dash). Once the OpMode is started, the camera view should appear.