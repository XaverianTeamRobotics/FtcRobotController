import React, { FC, ReactElement, useState } from "react";
import { createStatefulContext } from "../../lib/createStatefulContext.ts";
import { ImageUploader } from "./ImageUploader.tsx";
import { ImageViewer } from "./ImageViewer.tsx";
import { Status } from "./Status.tsx";

interface Props {

}

export const ImageContext = createStatefulContext<[ boolean, number ]>([ false, 0 ]);

export const StateContext = createStatefulContext<"good" | "pending" | "bad">("good");

export const SyncContext = createStatefulContext(false);

export const Dashboard: FC<Props> = (): ReactElement => {
  return (
    <React.Fragment>
      <SyncContext.Provider value={useState(false)}>
        <ImageContext.Provider value={useState<[ boolean, number ]>([ false, 0 ])}>
          <StateContext.Provider value={useState<"good" | "pending" | "bad">("good")}>
            <div className={"w-screen h-screen bg-slate-700 dashboard"}>
              <div className={"flex flex-col h-full"}>
                <div className={"flex flex-row grow overflow-auto"}>
                  <div className={"shrink-0"}>
                    <ImageUploader/>
                  </div>
                  <div className={"grow"}>
                    <ImageViewer/>
                  </div>
                </div>
                <div className={"shrink-0 bg-slate-800"}>
                  <Status/>
                </div>
              </div>
            </div>
          </StateContext.Provider>
        </ImageContext.Provider>
      </SyncContext.Provider>
    </React.Fragment>
  );
};
