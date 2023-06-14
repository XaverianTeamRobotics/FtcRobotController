import React, { FC, Fragment, ReactElement } from "react";
import { default as _ReactPlayer } from "react-player/lazy";
import { ReactPlayerProps } from "react-player/types/lib";

const ReactPlayer = _ReactPlayer as unknown as React.FC<ReactPlayerProps>;

type Unit =  "cm" | "mm" | "in" | "px" | "pt" | "pc" | "em" | "ex" | "ch" | "rem" | "vw" | "vh" | "vmin" | "vmax" | "%";

interface Props {
  loop?: boolean;
  src: string;
  height?: `${ number }${ Unit }`,
  width?: `${ number }${ Unit }`,
}

export const Video: FC<Props> = ({ loop, src, height, width }): ReactElement => {
  loop ??= false;
  height ??= "360px";
  width ??= "640px";
  return (
    <React.Fragment>
      {loop ?
        <Fragment>
          <ReactPlayer playing loop url={src} height={height} width={width} style={{ maxHeight: "100%", maxWidth: "100%" }} fallback={
            <Fragment>
              <p>Your browser does not include a video player.</p>
              <p>Please <a href={src}>download</a> and watch this video locally.</p>
            </Fragment>
          }
          />
        </Fragment>
        :
        <Fragment>
          <ReactPlayer controls url={src} height={height} width={width} style={{ maxHeight: "100%", maxWidth: "100%" }} fallback={
            <Fragment>
              <p>Your browser does not include a video player.</p>
              <p>Please <a href={src}>download</a> and watch this video locally.</p>
            </Fragment>
          }
          />
        </Fragment>
      }
    </React.Fragment>
  );
};
