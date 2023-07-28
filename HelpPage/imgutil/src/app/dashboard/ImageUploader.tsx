import { ArrayHelpers, ErrorMessage, Field, FieldArray, Form, Formik } from "formik";
import React, { FC, Fragment, ReactElement } from "react";
import * as Yup from "yup";


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
        tags: [ "" ]
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
      onSubmit={async (values) => {
        console.log(values);
      }}>
        {({ handleBlur, setFieldValue, values }) => {
          return (
            <Form className={"w-[32rem] p-8"}>
              <div>
                <input name="file" type="file" onBlur={handleBlur} onChange={(event) => {
                  setFieldValue("file", event.currentTarget.files![0]).then(r => {
                    if(typeof r === "object" && Object.keys(r).length >= 1) {
                      console.error("File upload error! ", r);
                    }else{
                      console.log(r);
                    }
                  });
                }}/>
                <ErrorMessage name={"file"}/>
              </div>
              <div>
                <label className={"text-red-500 h-[1lh]"}>
                  <div className={"inline-flex flex-row w-full px-3"}>
                    <div className={"mr-2"}>*</div>
                    <div className={"text-gray-50"}>Description</div>
                    <div className={"grow"}/>
                    <div>
                      <ErrorMessage name={"description"}/>
                    </div>
                  </div>
                  <div className={"w-full"}>
                    <Field name={"description"} className={"block w-full rounded-xl focus:ring-0 bg-slate-800 border-0 text-white h-[4lh] resize-none"} as={"textarea"}/>
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
                                <Field name={`tags.${ index }`}/>
                                <ErrorMessage name={`tags.${ index }`}/>
                                <button onClick={() => props.remove(index)} type={"button"}>Remove this tag</button>
                              </Fragment>
                            );
                          })}
                          <button onClick={() => props.push("")} type={"button"}>Add new tag</button>
                        </Fragment>
                      );
                    }
                  }
                </FieldArray>
              </div>
              <button type={"submit"}>Upload</button>
            </Form>
          );
        }}
      </Formik>
    </React.Fragment>
  );
};
