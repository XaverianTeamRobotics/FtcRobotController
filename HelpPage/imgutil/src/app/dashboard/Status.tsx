import React, { FC, ReactElement, useContext } from "react";
import { StateContext, SyncContext } from "./Dashboard.tsx";
import { Symbol } from "../../lib/Symbol.tsx";

interface Props {

}

export const Status: FC<Props> = (): ReactElement => {

  const [ status, setStatus ] = useContext(StateContext);
  const [ , setSync ] = useContext(SyncContext);

  console.log(status);

  return (
    <React.Fragment>
      <div className={"flex flex-row items-center w-full text-gray-50 text-sm"}>
        <div className={"p-6"}>
          {
            status === "good" ? <i className={"bg-[#23BD5C] h-4 w-4 block rounded-full"}/>
              : status === "pending" ? <i className={"bg-amber-300 h-4 w-4 block rounded-full"}/>
                : <i className={"bg-red-500 h-4 w-4 block rounded-full"}/>
          }
        </div>
        <div>
          {
            status === "good" ? "All systems operating properly" : status === "pending" ? "Processing request..." : "An error occurred! Check browser console for more details"
          }
        </div>
        <div className={"grow"}/>
        <div className={"text-gray-50"}>
          Sync image library with database
        </div>
        <div>
          <button className={"mx-6 px-2 py-2 bg-blue-500 text-lg rounded-full border-blue-500 focus:border-gray-50 border-[1px] text-gray-50"} onClick={() => {
            setStatus("pending");
            setSync(true);
          }}>
            <Symbol glyph={"sync"} design={"normal"} className={"block text-gray-50"}/>
          </button>
        </div>
      </div>
    </React.Fragment>
  );
};
