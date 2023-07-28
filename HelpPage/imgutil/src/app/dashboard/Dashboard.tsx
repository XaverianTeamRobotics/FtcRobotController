import React, { FC, ReactElement } from "react";
import { ImageUploader } from "./ImageUploader.tsx";

interface Props {

}

export const Dashboard: FC<Props> = (): ReactElement => {
  return (
    <React.Fragment>
      <div className={"w-screen h-screen bg-slate-700 dashboard"}>
        <div className={"flex flex-row"}>
          <div className={"shrink-0"}>
            <ImageUploader/>
          </div>
          <div className={"grow"}>

          </div>
        </div>
      </div>
    </React.Fragment>
  );
};
