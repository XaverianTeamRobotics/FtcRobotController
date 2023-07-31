import React, { FC, Fragment, ReactElement, useContext } from "react";
import { AuthContext } from "../Display.tsx";
import { Async } from "react-async";
import { string } from "yup";

interface Props {

}

type ImageMeta = {
  time: number,
  file: string,
  ar: "horizontal" | "vertical" | "square",
  tags: [string, ...string[]] | null,
  description: string | null
};

type TreeNode = {
  name: string,
  type: "file" | "directory",
  children?: TreeNode[]
}

export const ImageViewer: FC<Props> = (): ReactElement => {

  const [ [ , octokit ] ] = useContext(AuthContext);

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

    console.log(meta);

  };

  return (
    <Fragment>
      <Async promiseFn={getImages}>

      </Async>
    </Fragment>
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
