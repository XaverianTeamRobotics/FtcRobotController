import React, { FC, ReactElement, useContext } from "react";
import { StateContext } from "./Dashboard.tsx";

interface Props {

}

export const Status: FC<Props> = (): ReactElement => {

  const [ status ] = useContext(StateContext);

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
            status === "good" ? "All systems operating properly." : status === "pending" ? "Processing..." : "An error occurred. Check browser console for more details."
          }
        </div>
      </div>
    </React.Fragment>
  );
};
