import React, { CSSProperties, FC, HTMLProps, ReactElement } from "react";
import { Glyph } from "./Glyph";

interface Props extends HTMLProps<HTMLSpanElement> {
  glyph: Glyph | string;
  design: "normal" | "rounded" | "sharp";
  variant?: "outlined" | "filled";
  weight?: 100 | 200 | 300 | 400 | 500 | 600 | 700;
  grade?: -25 | 0 | 200;
  stroke?: 20 | 24 | 40 | 48;
  children?: never;
}

/**
 * A Material Symbols glyph.
 * @constructor
 */
export const Symbol: FC<Props> = (props): ReactElement => {
  let variant = props.variant;
  variant ??= "outlined";
  let design: string = props.design;
  design = design === "normal" ? "outlined" : design;
  // glyph styling options
  const reqStyle: CSSProperties = {
    fontVariationSettings: `'FILL' ${ variant === "filled" ? 1 : 0 }, 'wght' ${ props.weight ?? 400 }, 'GRAD' ${ props.grade ?? 0 }, 'opsz' ${ props.stroke ?? 48 }`
  };
  const mutProps = { ...props };
  delete mutProps.style;
  delete mutProps.className;
  return (
    <React.Fragment>
      <span className={`${ props.className } material-symbols-${ design }`} style={{ ...props.style, ...reqStyle }} { ...mutProps }>
        { props.glyph }
      </span>
    </React.Fragment>
  );
};
