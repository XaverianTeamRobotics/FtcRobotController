import { ArrayHelpers, ErrorMessage, Field, FieldArray, Form, Formik } from "formik";
import React, { FC, Fragment, ReactElement } from "react";
import * as Yup from "yup";
import { Symbol } from "../../lib/Symbol.tsx";
import { FormikHelpers } from "formik/dist/types";


interface Props {

}

Yup.addMethod(Yup.array, "unique", function(message, mapper = (a: unknown) => a) {
  return this.test("unique", message, function(list) {
    return list!.length  === new Set(list!.map(mapper)).size;
  });
});

export const ImageUploader: FC<Props> = (): ReactElement => {
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
            .required("Required"),
        description:
          Yup
            .string()
            .required("Required"),
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
        await uploadFile(values, helpers);
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
                    }} className={"w-full bg-slate-800 border-slate-800 focus:border-gray-50 rounded-xl border-[1px] text-lg text-gray-50 px-1 py-1 file:bg-slate-800 file:border-gray-50 file:border-[1px] file:border-solid file:text-gray-50 file:px-3 file:py-2 file:rounded-lg file:mr-3"}/>
                  </div>
                </label>
              </div>
              <div className={"h-4"}/>
              <div>
                <label className={"text-red-500 h-[1lh] text-lg"}>
                  <div className={"inline-flex flex-row w-full px-3"}>
                    <div className={"mr-2"}>*</div>
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

const uploadFile = async (values: {file: File, description: string, tags: string[]}, helpers: FormikHelpers<{file: File, description: string, tags: string[]}>) => {
  console.log(values);
  console.log(helpers);
};
