import { Octokit } from "octokit";
import React, { FC, ReactElement, useContext } from "react";
import { createStatefulContext } from "../lib/createStatefulContext.ts";
import { Dashboard } from "./dashboard/Dashboard.tsx";

interface Props {

}

export const AuthContext = createStatefulContext<[ boolean, Octokit | null ]>([ false, null ]);

export const Display: FC<Props> = (): ReactElement => {

  const [ [ auth ] ] = useContext(AuthContext);

  return (
    <React.Fragment>
      <Dashboard/>
      {/*<AuthContext.Provider value={useState<[ boolean, Octokit | null ]>([ false, null ])}>*/}
      {/*  { auth ? <Dashboard/> : <Login/> }*/}
      {/*</AuthContext.Provider>*/}
    </React.Fragment>
  );
};
