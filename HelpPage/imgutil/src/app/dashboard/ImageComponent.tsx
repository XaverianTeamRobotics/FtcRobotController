import React, { FC, Fragment, ReactElement, useContext, useState } from "react";
import { Description, Image } from "./ImageViewer.tsx";
import { AuthContext } from "../Display.tsx";
import { ImageContext, StateContext } from "./Dashboard.tsx";
import * as path from "path";
import { rmdb } from "./ImageUploader.tsx";
import { Symbol } from "../../lib/Symbol.tsx";

interface Props {
  value: Image;
}

export const ImageComponent: FC<Props> = ({ value }): ReactElement => {

  const [ exists, shouldExist ] = useState(true);

  const [ [ , octokit ] ] = useContext(AuthContext);
  const [ , reload ] = useContext(ImageContext);
  const [ , status ] = useContext(StateContext);

  const deleteMe = async () => {

    if(octokit === null) {
      throw "Octokit null!";
    }

    await rmdb(octokit, value.time, false, status);

    reload(prevState => [ prevState[0], prevState[1] + 1 ]);

  };

  if(!exists) {
    console.log(`Deleting ${ value.file } from repo.`);
    deleteMe();
  }

  return (
    <React.Fragment>
      <div className={"bg-slate-800 text-gray-50 rounded-xl text-lg m-4 mb-0"}>
        <div className={"flex flex-row p-4 gap-4"}>
          <div>
            <img src={value.file} alt={"An image from our team"} className={`${ value.ar === "vertical" ? "h-64" : "w-64" } rounded-lg`}/>
          </div>
          <div>
            <p>Uploaded On:</p>
            <p className={"text-gray-400 mb-4 ml-2 -mt-2"}>
              <Symbol glyph={"minimize"} design={"normal"} className={"text-red-500"}/>
              { new Date(value.time).toLocaleString("en-US", {
                dateStyle: "full",
                timeStyle: "short"
              }) }
            </p>
            <p>Aspect Ratio:</p>
            <p className={"text-gray-400 mb-4 ml-2 -mt-2"}>
              <Symbol glyph={"minimize"} design={"normal"} className={"text-red-500"}/>
              { `${ value.ar.substring(0, 1).toUpperCase() }${ value.ar.substring(1) }` }
            </p>
            <p>Description:</p>
            <p className={"text-gray-400 mb-4 ml-2 -mt-2"}>
              <Symbol glyph={"minimize"} design={"normal"} className={"text-red-500"}/>
              { <Description src={value.description}/> }
            </p>
            <ul className={"flex flex-row gap-2"}>
              { value.tags.map((value, index) => {
                return (
                  <Fragment key={index}>
                    <li className={"bg-blue-500 px-2 text-gray-50 rounded-full"}>
                      { value }
                    </li>
                  </Fragment>
                );
              })}
            </ul>
          </div>
          <div className={"grow"}/>
          <button className={"bg-red-500 border-red-500 focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 w-[48px] h-[48px]"} onClick={() => {
            shouldExist(false);
          }}>
            <Symbol glyph={"delete"} design={"sharp"} className={"p-2 align-bottom"}/>
          </button>
        </div>
      </div>
    </React.Fragment>
  );
};
