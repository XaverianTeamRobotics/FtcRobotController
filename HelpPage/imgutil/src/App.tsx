import React, { FC, ReactElement, useState } from "react";
import { Display } from "./app/Display.tsx";
import { Octokit } from "octokit";
import { AuthContext } from "./app/Display.tsx";

interface Props {

}

export const App: FC<Props> = (): ReactElement => {
  return (
    <React.Fragment>
      <AuthContext.Provider value={useState<[ boolean, Octokit | null ]>([ false, null ])}>
        <Display/>
      </AuthContext.Provider>
    </React.Fragment>
  );
};
