import React, { Dispatch, FC, Fragment, ReactElement, SetStateAction, useContext } from "react";
import { Async, AsyncProps } from "react-async";
import { Oval } from "../../lib/Oval.tsx";
import { AuthContext } from "../Display.tsx";
import { StateContext, SyncContext } from "./Dashboard.tsx";

interface Props {

}

type ImageMeta = {
  time: number,
  file: string,
  ar: "horizontal" | "vertical" | "square",
  tags: [string, ...string[]] | null,
  description: string | null
};

type Image = {
  time: number,
  file: `https://raw.githubusercontent.com/XaverianTeamRobotics/imgs/main/data/${ number }/${ string }`,
  ar: "horizontal" | "vertical" | "square",
  tags: [string, ...string[]] | [ "No tags" ],
  description: `https://raw.githubusercontent.com/XaverianTeamRobotics/imgs/main/data/${ number }/description.dbe` | "https://raw.githubusercontent.com/XaverianTeamRobotics/imgs/main/nd.txt"
};

type TreeNode = {
  name: string,
  type: "file" | "directory",
  children?: TreeNode[]
}

export const ImageViewer: FC<Props> = (): ReactElement => {

  console.log("Starting image fetch process...");

  const [ [ , octokit ] ] = useContext(AuthContext);
  const [ , setStatus ] = useContext(StateContext);
  const [ sync, setSync ] = useContext(SyncContext);

  const getImages = async (props: object, controller: AbortController) => {

    if(octokit === null) {
      throw "Octokit null";
    }

    const owner = "XaverianTeamRobotics";
    const repo = "imgs";

    const repository = await octokit.rest.repos.get({
      owner,
      repo
    });

    const branch = repository.data.default_branch;

    const ref = await octokit.rest.git.getRef({
      owner,
      repo,
      ref: `heads/${ branch }`,
    });

    const data = await octokit.rest.git.getTree({
      owner,
      repo,
      tree_sha: ref.data.object.sha
    });

    const folder = data.data.tree.find(value => value.path === "data");

    if(folder === undefined) {
      throw "XaverianTeamRobotics/imgs/data doesn't exist! This is bad!!! Error: folder undefined";
    }

    if(folder.sha === undefined) {
      throw "XaverianTeamRobotics/imgs/data doesn't exist! This is bad!!! Error: sha undefined";
    }

    const imgs = await octokit.rest.git.getTree({
      owner,
      repo,
      tree_sha: folder.sha,
      recursive: "true"
    });

    if(imgs === undefined) {
      throw "Images undefined";
    }

    const paths: string[] = [];

    for(const path of imgs.data.tree) {
      if(path.path !== undefined) {
        paths.push(path.path);
      }
    }

    const tree = convertToTree(paths);

    const meta = convertToMetadata(tree);

    console.log("Images from GH:");
    console.log(meta);

    const images: Image[] = [];

    for(const m of meta) {

      const tags = m.tags ?? [ "No tags" ];
      const filename = m.file as Image["file"];
      const description = (m.description ?? "https://raw.githubusercontent.com/XaverianTeamRobotics/imgs/main/nd.txt") as Image["description"];

      images.push({
        time: m.time,
        file: filename,
        tags,
        description,
        ar: m.ar
      });
    }

    return images;

  };

  return (
    <Fragment>
      <Async promiseFn={getImages}>
        <Async.Pending>
          <div className={"flex justify-center items-center h-screen"}>
            <div>
              <Oval/>
            </div>
          </div>
        </Async.Pending>
        <Async.Rejected>
          { error => {
            return (
              <Fragment>
                <div className={"flex justify-center flex-col items-center h-screen text-red-500"}>
                  <div>
                    <span>An error occurred while loading the images already in the database.</span>
                  </div>
                  <div>
                    <div className={"flex justify-center flex-row"}>
                      <div>
                        <pre>Error: </pre>
                      </div>
                      <div>
                        <div>
                          <span>{ error.name }</span>
                        </div>
                        <div>
                          <span>{ error.message }</span>
                        </div>
                        <div>
                          <span>{ error.stack }</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </Fragment>
            );
          }}
        </Async.Rejected>
        <Async.Fulfilled>
          { (data: Image[]) => {
            return (
              <Fragment>
                { data.map((value, index) => {
                  return (
                    <Fragment key={index}>
                      <div className={"border-2 flex flex-col"}>
                        <img src={value.file} alt={"An image from our team"} className={"w-12"}/>
                        <p>Time: { new Date(value.time).toString() }</p>
                        <p>A/R: { value.ar }</p>
                        <p>Desc: <Description src={value.description}/></p>
                        <ul>
                          { value.tags.map((value, index) => {
                            return (
                              <Fragment key={index}>
                                <li>
                                  { value }
                                </li>
                              </Fragment>
                            );
                          })}
                        </ul>
                      </div>
                    </Fragment>
                  );
                }) }
                <TrillEmAll1989 sync={sync} setSync={setSync} setStatus={setStatus}/>
              </Fragment>
            );
          }}
        </Async.Fulfilled>
      </Async>
    </Fragment>
  );
};

const TrillEmAll1989: FC<{ sync: boolean, setSync: Dispatch<SetStateAction<boolean>>, setStatus: Dispatch<SetStateAction<"good" | "pending" | "bad">>}> = ({ sync, setSync, setStatus }): ReactElement => {

  if(sync) {
    setSync(false);
    setTimeout(() => {
      setStatus("good");
    }, 500);
  }

  return (
    <Fragment/>
  );
};

const convertToTree = (filenames: string[]): TreeNode => {
  const root: TreeNode = { name: "", type: "directory", children: [] };

  for(const filename of filenames) {
    const parts = filename.split("/");

    let currentNode = root;
    for(const part of parts) {
      if(part === "") continue;

      let childNode = currentNode.children?.find((child) => child.name === part);

      if(!childNode) {
        childNode = { name: part, type: "directory", children: [] };
        currentNode.children?.push(childNode);
      }

      currentNode = childNode;
    }
    currentNode.type = "file";
  }

  return root;
};

const convertToMetadata = (root: TreeNode): ImageMeta[] => {

  const arr: ImageMeta[] = [];

  for(const dir of root.children ?? []) {

    const obj: Partial<ImageMeta> = {};
    obj.description = null;
    obj.tags = [] as unknown as [string, ...string[]];
    obj.time = +dir.name;

    for(const file of dir.children ?? []) {

      if(file.name === "description.dbe") {
        obj.description = `https://raw.githubusercontent.com/XaverianTeamRobotics/imgs/main/data/${ obj.time }/description.dbe`;
        continue;
      }

      if(file.name.endsWith(".file.dbe")) {
        obj.file = `https://raw.githubusercontent.com/XaverianTeamRobotics/imgs/main/data/${ obj.time }/${ file.name.substring(0, file.name.lastIndexOf(".file.dbe")) }`;
        continue;
      }

      if(obj.tags !== null && file.name.endsWith(".tag.dbe")) {
        obj.tags.push(file.name.substring(0, file.name.lastIndexOf(".tag.dbe")));
        continue;
      }

      if(file.name.endsWith(".ar.dbe")) {
        if(file.name.startsWith("horizontal") || file.name.startsWith("vertical") || file.name.startsWith("square")) {
          obj.ar = file.name.substring(0, file.name.lastIndexOf(".ar.dbe")) as "horizontal" | "vertical" | "square";
        }
      }

    }

    if(obj.tags !== null && obj.tags.length < 1) {
      obj.tags = null;
    }

    if(obj.tags === undefined) obj.tags = null;
    if(obj.description === undefined) obj.description = null;

    if(obj.file === undefined || obj.file == "" || obj.ar !== "horizontal" && obj.ar !== "vertical" && obj.ar !== "square") {
      console.error("Error: DB entry failed sanitization! \n\n Entry is:\n", JSON.stringify(obj, null, 2));
      continue;
    }

    arr.push(obj as ImageMeta);

  }

  return arr;

};

const Description: FC<{ src: string }> = ({ src }): ReactElement => {

  const getDescription = async ({ src }: AsyncProps<string>) => {
    src = src as string;
    console.log("Pinging GH");
    const res = await fetch(src);
    console.log("Desc response:");
    console.log(res);
    return res.text();
  };

  return (
    <Fragment>
      <Async promiseFn={getDescription} src={src}>
        <Async.Fulfilled>
          { (data: string) => {
            return (
              <Fragment>
                <span>{ data }</span>
              </Fragment>
            );
          }}
        </Async.Fulfilled>
        <Async.Pending>
          <span>Loading...</span>
        </Async.Pending>
        <Async.Rejected>
          <span>Description failed to load. Check console for details.</span>
        </Async.Rejected>
      </Async>
    </Fragment>
  );
};
