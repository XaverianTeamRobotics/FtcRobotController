import React, { Fragment } from "react";
import BrowserOnly from "@docusaurus/BrowserOnly";
import { ClientRoot } from "@site/src/components/ClientRoot";

export default function Root({ children }) {
  // we wrap everything in browseronly here, which disables ssg
  // i dont LIKE disabling ssg, but its fine because:
  // - we're all using js here
  // - this site isnt indexable anyway
  // why are we doing this? because this site has a very close relationship with the window object, which docusaurus' prerenderer doesn't have. so prerendering always fails :/
  return (
    <>
      <BrowserOnly>
        {() => {
          return (
            <Fragment>
              <ClientRoot>
                { children }
              </ClientRoot>
            </Fragment>
          );
        }}
      </BrowserOnly>
    </>
  );
}
