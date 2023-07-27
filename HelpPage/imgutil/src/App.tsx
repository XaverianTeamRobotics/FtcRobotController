import React, { FC, ReactElement } from "react";

interface Props {

}

export const App: FC<Props> = (): ReactElement => {
  return (
    <React.Fragment>
      <h1 className="text-3xl font-bold underline">
        Hello world!
      </h1>
    </React.Fragment>
  );
};
