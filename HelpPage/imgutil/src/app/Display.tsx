import React, { FC, ReactElement, useContext, useState } from "react";
import { Octokit } from "octokit";
import { Login } from "./Login.tsx";
import { Dashboard } from "./Dashboard.tsx";
import { createStatefulContext } from "../lib/createStatefulContext.ts";

interface Props {

}

export const AuthContext = createStatefulContext<[ boolean, Octokit | null ]>([ false, null ]);

export const Display: FC<Props> = (): ReactElement => {

  const [ [ auth ] ] = useContext(AuthContext);

  return (
    <React.Fragment>
      <AuthContext.Provider value={useState<[ boolean, Octokit | null ]>([ false, null ])}>
        { auth ? <Dashboard/> : <Login/> }
      </AuthContext.Provider>
    </React.Fragment>
  );
};
