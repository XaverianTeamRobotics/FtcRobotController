import React, { useEffect, useState } from "react";
import { NavbarBreadcrumbHeightContext } from "@site/src/utils/NavbarBreadcrumbHeightContext";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";

export default function Root({ children }) {

  const breadcrumbHeightState = useState(0);

  const documentHeightState = useState(0);

  // height mgmt using javascript. normally i would be against this for perf reasons but this will have an incredibly minimal effect on perf
  const observer = new ResizeObserver(entries => {
    let height = 0;
    for(const entry of entries) {
      if(height < entry.target.clientHeight) {
        height = entry.target.clientHeight;
      }
    }
    breadcrumbHeightState[1](height);
  });

  useEffect(() => {
    const elems = document.querySelectorAll("#doc-breadcrumbs");
    for(const elem of elems) {
      observer.observe(elem);
    }
    return () => {
      observer.disconnect();
    };
  }, []);

  return (
    <>
      <DocumentHeightContext.Provider value={documentHeightState}>
        <NavbarBreadcrumbHeightContext.Provider value={breadcrumbHeightState}>
          {children}
        </NavbarBreadcrumbHeightContext.Provider>
      </DocumentHeightContext.Provider>
    </>
  );
}
