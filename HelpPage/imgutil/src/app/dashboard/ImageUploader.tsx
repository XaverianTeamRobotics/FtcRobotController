import { ArrayHelpers, ErrorMessage, Field, FieldArray, Form, Formik } from "formik";
import { FormikHelpers } from "formik/dist/types";
import { Octokit } from "octokit";
import React, { FC, Fragment, ReactElement, useContext } from "react";
import * as Yup from "yup";
import { Symbol } from "../../lib/Symbol.tsx";
import { AuthContext } from "../Display.tsx";
import { ImageContext, StateContext } from "./Dashboard.tsx";
import { bool } from "yup";


interface Props {

}

Yup.addMethod(Yup.array, "unique", function(message, mapper = (a: unknown) => a) {
  return this.test("unique", message, function(list) {
    return list!.length  === new Set(list!.map(mapper)).size;
  });
});

export const ImageUploader: FC<Props> = (): ReactElement => {

  const [ [ , octokit ] ] = useContext(AuthContext);
  const [ , setImageContext ] = useContext(ImageContext);
  const [ , setStatus ] = useContext(StateContext);

  return (
    <React.Fragment>
      <Formik initialValues={{
        file: (null as unknown as File),
        description: "",
        tags: ([] as string[])
      }}
      validationSchema={Yup.object({
        file:
          Yup
            .mixed()
            .test("is-file", "Image must be a file", value => value instanceof File)
            .test("is-allowed-file", "Image must be a JPEG, PNG, or WebP file", value => {
              const file = value as File;
              return file.type === "image/png" || file.type === "image/jpeg" || file.type === "image/webp";
            })
            .test("is-small-enough", "Image size must not exceed 25MB", value => {
              const file = value as File;
              return file.size <= 26214400;
            })
            .test("name-correct", "Names can only contain letters, numbers, . and _", value => {
              const file = value as File;
              return !/[^a-zA-Z0-9_.]/.test(file.name);
            })
            .required("Required"),
        description:
          Yup
            .string()
            .notRequired(),
        tags:
          Yup
            .array()
            .of(
              Yup
                .string()
                .test("no-disallowed-chars", "Tags can only contain letters, numbers, and _", value => {
                  return !/[^a-zA-Z0-9_]/.test(value ?? "");
                })
                .required("Required")
            )
            .notRequired()
      })}
      onSubmit={async (values, helpers) => {

        setStatus("pending");

        const res = await uploadFile(values, helpers, octokit);

        if(res) {
          console.log("Image uploaded successfully.");
        }else{
          console.error("Image failed to upload. Read the errors for more details. You probably want to either retry uploading now, retry uploading later, or manually upload to the database. Contact tom if this is your first time manually updating the database, or if you get stuck.");
        }

        setImageContext(([ , i ]) => {
          return [ res, i + 1 ];
        });

        setStatus(res ? "good" : "bad");

      }}>
        {({ handleBlur, setFieldValue, values }) => {
          return (
            <Form className={"w-[36rem] p-8"}>
              <div>
                <label className={"text-red-500 h-[1lh] text-lg"}>
                  <div className={"inline-flex flex-row w-full px-3"}>
                    <div className={"mr-2"}>*</div>
                    <div className={"text-gray-50"}>Image</div>
                    <div className={"grow"}/>
                    <div>
                      <ErrorMessage name={"file"}/>
                    </div>
                  </div>
                  <div className={"w-full"}>
                    <input name="file" type="file" onBlur={handleBlur} onChange={(event) => {
                      setFieldValue("file", event.currentTarget.files![0]).then(r => {
                        if(typeof r === "object" && Object.keys(r).length >= 1) {
                          console.error("File upload error! ", r);
                        }else{
                          console.log(r);
                        }
                      });
                    }} className={"w-full bg-slate-800 border-slate-800 focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 px-1 py-1 file:bg-slate-800 file:border-gray-50 file:border-[1px] file:border-solid file:text-gray-50 file:px-3 file:py-2 file:rounded-lg file:mr-3 cursor-pointer file:cursor-pointer"}/>
                  </div>
                </label>
              </div>
              <div className={"h-4"}/>
              <div>
                <label className={"text-red-500 h-[1lh] text-lg"}>
                  <div className={"inline-flex flex-row w-full px-3"}>
                    <div className={"text-gray-50"}>Description</div>
                    <div className={"grow"}/>
                    <div>
                      <ErrorMessage name={"description"}/>
                    </div>
                  </div>
                  <div className={"w-full"}>
                    <Field name={"description"} className={"block w-full rounded-xl focus:ring-0 bg-slate-800 border-slate-800 focus:border-gray-50 text-gray-50 h-[calc(4lh+1rem+2px)] resize-none text-lg"} as={"textarea"}/>
                  </div>
                </label>
              </div>
              <div>
                <FieldArray name={"tags"}>
                  {
                    (props: ArrayHelpers ) => {
                      return (
                        <Fragment>
                          { values.tags.map((_, index) => {
                            return (
                              <Fragment key={index}>
                                <div className={"h-4"}/>
                                <label className={"text-red-500 h-[1lh] text-lg"}>
                                  <div className={"inline-flex flex-row w-full px-3"}>
                                    <div className={"mr-2"}>*</div>
                                    <div className={"text-gray-50"}>Tag { index + 1 }</div>
                                    <div className={"grow"}/>
                                    <div>
                                      <ErrorMessage name={`tags.${ index }`}/>
                                    </div>
                                  </div>
                                  <div className={"flex w-full flex-row bg-slate-800 border-slate-800 focus-within:border-gray-50 resize-none rounded-xl border-[1px]"}>
                                    <Field name={`tags.${ index }`} className={"block grow rounded-xl focus:ring-0 bg-slate-800 border-slate-800 focus:border-slate-800 text-gray-50 h-[calc(1lh+1rem+2px)] resize-none text-lg"}/>
                                    <div className={"aspect-square w-[calc(1lh+1rem+2px)] p-1"}>
                                      <button type={"button"} onClick={() => props.remove(index)} className={"bg-red-500 text-gray-50 w-full h-full rounded-lg flex justify-center items-center"}>
                                        <Symbol glyph={"close"} design={"sharp"} stroke={20} className={"align-text-bottom text-lg"}/>
                                      </button>
                                    </div>
                                  </div>
                                </label>
                              </Fragment>
                            );
                          })}
                          <div className={"h-4"}/>
                          <div className={`flex flex-row gap-4 ${ values.tags.length <= 0 ? "hidden" : "" }`}>
                            <button type={"button"} onClick={() => props.push("")} className={"bg-[#23BD5C] border-[#23BD5C] focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 w-[48px] h-[48px]"}>
                              <Symbol glyph={"add"} design={"sharp"} className={"p-2 align-bottom"}/>
                            </button>
                            <button type={"button"} onClick={() => {
                              for(let i = values.tags.length; i >= 0; i--) {
                                props.remove(i);
                              }
                            }} className={"bg-red-500 border-red-500 focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 h-[48px]"}>
                              <div className={"flex flex-row items-center"}>
                                <Symbol glyph={"delete"} design={"sharp"} className={"p-2 align-bottom"}/>
                                <div className={"inline-flex h-[46px] flex-col justify-center items-center pr-3"}>
                                  <div>
                                    Delete all tags
                                  </div>
                                </div>
                              </div>
                            </button>
                          </div>
                          <div className={`${ values.tags.length > 0 ? "hidden" : "" }`}>
                            <button type={"button"} onClick={() => props.push("")} className={"bg-slate-800 border-slate-800 focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 h-[48px]"}>
                              <div className={"flex flex-row items-center"}>
                                <Symbol glyph={"add"} design={"sharp"} className={"p-2 align-bottom"}/>
                                <div className={"inline-flex h-[46px] flex-col justify-center items-center pr-3"}>
                                  <div>
                                    Add a tag
                                  </div>
                                </div>
                              </div>
                            </button>
                          </div>
                        </Fragment>
                      );
                    }
                  }
                </FieldArray>
              </div>
              <div className={"h-4"}/>
              <div>
                <button type={"submit"} className={"w-full bg-blue-500 border-blue-500 focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 px-3 py-2 h-[calc(1lh+1rem+2px)]"}>Upload</button>
              </div>
            </Form>
          );
        }}
      </Formik>
    </React.Fragment>
  );
};

const uploadFile = async (values: {file: File, description: string, tags: string[]}, helpers: FormikHelpers<{file: File, description: string, tags: string[]}>, octokit: Octokit | null): Promise<boolean> => {

  console.log("Beginning upload process...");
  console.log(values);
  console.log(helpers);

  if(octokit === null) {
    console.log("Octokit is null. Upload failed. Maybe you didn't log in properly?");
    return false;
  }

  const owner = "XaverianTeamRobotics";
  const repo = "imgs";
  const [ name, email ] = [ "Lasagna Man", "lasagnaman@xbhs.net" ];
  const time = Date.now();
  let step = 1;
  let cancel = false;

  const bmp = await createImageBitmap(values.file);
  const { width, height } = bmp;
  let ar: "horizontal" | "vertical" | "square";
  if(width > height) {
    ar = "horizontal";
  }else if(width < height) {
    ar = "vertical";
  }else{
    ar = "square";
  }
  bmp.close();

  console.log(`The time of upload is ${ time }, storing data in /data/${ time }`);

  console.log("Uploading name...");

  await octokit.rest.repos.createOrUpdateFileContents(
    {
      owner,
      repo,
      path: `data/${ time }/${ values.file.name }.file.dbe`,
      content: btoa("file-name"),
      committer: {
        name,
        email,
      },
      author: {
        name,
        email
      },
      message: `Uploading an image. Step [${ step }/${ values.tags.length + 4 }]`
    }
  ).catch(reason => {
    console.error(reason);
    cancel = true;
  });

  if(cancel) {
    await rmdb(octokit, time);
    return false;
  }

  console.log("Uploaded name!");
  step++;

  console.log("Uploading image file...");

  const imageContent = (await blobToBase64(values.file) as string).split("base64,")[1];
  console.log("Base64 Image Content:");
  console.log(imageContent);

  await octokit.rest.repos.createOrUpdateFileContents(
    {
      owner,
      repo,
      path: `data/${ time }/${ values.file.name }`,
      content: imageContent,
      committer: {
        name,
        email,
      },
      author: {
        name,
        email
      },
      message: `Uploading an image. Step [${ step }/${ values.tags.length + 4 }]`
    }
  ).catch(reason => {
    console.error(reason);
    cancel = true;
  });

  if(cancel) {
    await rmdb(octokit, time);
    return false;
  }

  console.log("Uploaded image file!");
  step++;

  console.log("Uploading A/R...");

  await octokit.rest.repos.createOrUpdateFileContents(
    {
      owner,
      repo,
      path: `data/${ time }/${ ar }.ar.dbe`,
      content: btoa("a/r"),
      committer: {
        name,
        email,
      },
      author: {
        name,
        email
      },
      message: `Uploading an image. Step [${ step }/${ values.tags.length + 4 }]`
    }
  ).catch(reason => {
    console.error(reason);
    cancel = true;
  });

  if(cancel) {
    await rmdb(octokit, time);
    return false;
  }

  console.log("Uploaded A/R!");
  step++;

  if(values.description.length > 0) {
    console.log("Uploading description...");

    await octokit.rest.repos.createOrUpdateFileContents(
      {
        owner,
        repo,
        path: `data/${ time }/description.dbe`,
        content: btoa(values.description),
        committer: {
          name,
          email,
        },
        author: {
          name,
          email
        },
        message: `Uploading an image. Step [${ step }/${ values.tags.length + 4 }]`
      }
    ).catch(reason => {
      console.error(reason);
      cancel = true;
    });

    if(cancel) {
      await rmdb(octokit, time);
      return false;
    }

    console.log("Uploaded description!");
    step++;
  }

  if(values.tags.length > 0) {

    const oldTags: string[] = [];

    for(const tag of values.tags) {
      console.log(`Uploading a tag (${ tag })...`);

      if(oldTags.includes(tag)) {
        console.log(`Tag (${ tag }) already uploaded, skipping...`);
        continue;
      }

      await octokit.rest.repos.createOrUpdateFileContents(
        {
          owner,
          repo,
          path: `data/${ time }/${ tag }.tag.dbe`,
          content: btoa("tagfile"),
          committer: {
            name,
            email,
          },
          author: {
            name,
            email
          },
          message: `Uploading an image. Step [${ step }/${ values.tags.length + 4 }]`
        }
      ).catch(reason => {
        console.error(reason);
        cancel = true;
      });

      if(cancel) {
        await rmdb(octokit, time);
        return false;
      }

      console.log(`Uploaded a tag (${ tag })!`);
      step++;
      oldTags.push(tag);
    }
  }

  console.log("Upload completed successfully!");
  return true;

};

export const rmdb = async (octokit: Octokit, time: number, error: boolean = true, reducer:  React.Dispatch<React.SetStateAction<"good" | "pending" | "bad">> | null = null) => {

  if(error) {
    console.error("Something went wrong while uploading files. Reverting changes...");
  }else{
    reducer!("pending");
  }

  const commitMsg = error ? "Error occured, reverting changes. Step [?/?]" : "Deleting an image. Step [?/?]";

  const attempt = async () => {

    const owner = "XaverianTeamRobotics";
    const repo = "imgs";
    const [ name, email ] = [ "Lasagna Man", "lasagnaman@xbhs.net" ];

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
      tree_sha: ref.data.object.sha,
      recursive: "true"
    });

    console.log(data);

    const folder = data.data.tree.find(value => value.path === `data/${ time }`);

    if(folder === undefined) {
      throw `XaverianTeamRobotics/imgs/data/${ time } doesn't exist! This is bad!!! Error: folder undefined`;
    }

    if(folder.sha === undefined) {
      throw `XaverianTeamRobotics/imgs/data/${ time } doesn't exist! This is bad!!! Error: sha undefined`;
    }

    const folders = await octokit.rest.git.getTree({
      owner,
      repo,
      tree_sha: folder.sha,
      recursive: "true"
    });

    if(folders === undefined) {
      throw "Images undefined";
    }

    const paths: [ string, string ][] = [];

    for(const path of folders.data.tree) {
      if(path.path !== undefined && path.sha !== undefined) {
        paths.push([ `data/${ time }/${ path.path }`, path.sha ]);
      }
    }

    for(const [ path, sha ] of paths) {

      const message = commitMsg;
      await octokit.rest.repos.deleteFile({
        owner,
        repo,
        path,
        message,
        sha,
        author: {
          name,
          email
        },
        committer: {
          name,
          email
        }
      }).catch(reason => {
        console.error(reason);
        console.error(`Deleting ${ path } with SHA ${ sha } failed. Cancelling automatic cleanup...`);
        throw new Error(`Deleting ${ path } with SHA ${ sha } failed with the error ${ reason }`);
      });

    }

  };

  if(error) {
    attempt().then(() => {
      console.log("Reverted changes. Please try uploading again or manually upload the file. Contact tom if this is your first time manually uploading a file, or if you forgot.");
    }).catch(reason => {
      console.error(`Something went wrong while reverting changes. Please delete the '/data/${ time }' folder manually if it still exists.\n\n(clone repo, delete folder, commit change, push commit)`);
      console.error(reason);
    });
  }else{
    attempt().then(() => {
      console.log("Deleted image from database.");
    }).catch(reason => {
      if(!(reason.message as string).includes("failed with the error HttpError: Not Found")) {
        console.error(`Something went wrong while deleting the image. Please delete the '/data/${ time }' folder manually if it still exists.\n\n(clone repo, delete folder, commit change, push commit)`);
        console.error(reason);
        reducer!("bad");
      }else{
        console.warn("We 404ed while deleting, this is probably fine, but you might want to know about it just in case.");
        console.warn(reason);
        reducer!("good");
      }
    });
  }

  return;
};

const blobToBase64 = (blob: Blob) => {
  return new Promise((resolve, _) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result);
    reader.readAsDataURL(blob);
  });
};
