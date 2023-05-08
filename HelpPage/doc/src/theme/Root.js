import React, { useEffect, useState } from "react";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";

// Default implementation, that you can customize
export default function Root({ children }) {

  const heightState = useState(0);

  const observer = new ResizeObserver(entries => {
    let height = 0;
    for(const entry of entries) {
      if(height < entry.target.clientHeight) {
        height = entry.target.clientHeight;
      }
    }
    heightState[1](height);
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
      <DocumentHeightContext.Provider value={heightState}>
        {children}
      </DocumentHeightContext.Provider>
    </>
  );
}
