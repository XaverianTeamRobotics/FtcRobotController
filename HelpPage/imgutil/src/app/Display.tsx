import { Octokit } from "octokit";
import React, { FC, ReactElement, useContext } from "react";
import { createStatefulContext } from "../lib/createStatefulContext.ts";
import { Dashboard } from "./dashboard/Dashboard.tsx";
import { Login } from "./Login.tsx";

interface Props {

}

export const AuthContext = createStatefulContext<[ boolean, Octokit | null ]>([ false, null ]);

export const Display: FC<Props> = (): ReactElement => {

  const [ [ auth ] ] = useContext(AuthContext);

  return (
    <React.Fragment>
      { auth ? <Dashboard/> : <Login/> }
    </React.Fragment>
  );
};
