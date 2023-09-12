import { ErrorMessage, Field, Form, Formik } from "formik";
import React, { FC, ReactElement, useContext } from "react";
import * as Yup from "yup";
import { Symbol } from "../lib/Symbol.tsx";
import { AuthContext } from "./Display.tsx";
import { Octokit } from "octokit";

interface Props {

}

export const Login: FC<Props> = (): ReactElement => {

  const [ , setLogin ] = useContext(AuthContext);

  return (
    <React.Fragment>
      <div className={"w-screen h-screen flex justify-center items-center bg-blue-500 p-4"}>
        <div className={"container"}>
          <div className={"max-w-[30rem] mx-auto"}>
            <Formik
              initialValues={{
                token: ""
              }}
              validationSchema={Yup.object({
                token: Yup.string().required("Required")
              })}
              onSubmit={async (values, helpers) => {
                try {
                  const octokit = new Octokit({
                    auth: values.token,
                    userAgent: "xbhs-image-uploader/v1",
                  });
                  setLogin([ true, octokit ]);
                } catch(e) {
                  console.error(e);
                  helpers.setErrors({
                    token: "An error occured. Check console for details."
                  });
                }
              }}>
              <Form className={"relative"}>
                <div className={"absolute top-[-1lh] w-full"}>
                  <div className={"px-3 text-white opacity-60 w-full"}>
                    <ErrorMessage name={"token"}/>
                  </div>
                </div>
                <div className={"flex flex-row w-full bg-blue-600 border-blue-600 rounded-xl focus-within:border-[#b1cdfb] border-[1px]"}>
                  <Field name={"token"} placeholder={"Token"} className={"block flex-grow bg-blue-600 border-blue-600 focus:border-blue-600 rounded-xl text-white opacity-60 placeholder-white focus:ring-0 border-none"}/>
                  <div className={"aspect-square w-[40px] p-1"}>
                    <button type={"submit"} className={"border-[#b1cdfb] border-[1px] text-[#b1cdfb] w-full h-full rounded-lg flex justify-center items-center"}>
                      <Symbol glyph={"arrow_forward"} design={"sharp"} className={"align-text-bottom text-lg"}/>
                    </button>
                  </div>
                </div>
              </Form>
            </Formik>
          </div>
          <div className={"h-4"}/>
          <p className={"text-white text-center"}>
            <span className={"opacity-60"}>Login with your token or </span>
            <a href={"https://github.com/settings/tokens/new?scopes=public_repo"} className={"opacity-80 underline"} target={"_blank"} rel={"noopener noreferrer"}>click here</a>
            <span className={"opacity-60"}> to generate one.</span>
          </p>
        </div>
      </div>
    </React.Fragment>
  );
};
