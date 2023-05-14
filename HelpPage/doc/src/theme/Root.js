import React, { useEffect, useRef, useState } from "react";
import { NavbarBreadcrumbHeightContext } from "@site/src/utils/NavbarBreadcrumbHeightContext";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";
import { sidebarEventManager } from "@site/src/utils/sidebarEventManager";

let log = true; // change to disable the "this is not an error" log

export default function Root({ children }) {

  const breadcrumbHeightState = useState(0);

  const docHeightState = useState(0);

  // height mgmt using javascript. normally i would be against this for perf reasons but this will have an incredibly minimal effect on perf
  const observer = new ResizeObserver(entries => {
    window.requestAnimationFrame(() => {
      let height = 0;
      for(const entry of entries) {
        if(height < entry.target.clientHeight) {
          height = entry.target.clientHeight;
        }
      }
      breadcrumbHeightState[1](height);
    });
  });

  // i am VERY against using resize observer here, but webkit leaves me no other choice.
  // (i need to apply styles based on whether a scrollbar is visible on an element with an explicit height & width, and webkit has no scrollbar visibility detection in css nor does javascript detection work because there are no scrollbar visibility detection apis and therefore javascript detection can only use height & width to determine scrollbar visibility implicitly, but that is impossible due to the explicit height & width)
  const sidebarObserver = new ResizeObserver(entries => {
    window.requestAnimationFrame(() => {
      const height = entries[0].target.clientHeight;
      const overflowContainer = document.querySelector("nav[aria-label='Docs sidebar'].menu.thin-scrollbar");
      if(overflowContainer === null || overflowContainer === undefined)return;
      const overflow = height > document.querySelector("nav[aria-label='Docs sidebar'].menu.thin-scrollbar").clientHeight;
      document.dispatchEvent(new CustomEvent("sidebaroverflowed", {
        detail:
          {
            overflow
          }
      }));
    });
  });

  useEffect(() => {
    const elems = document.querySelectorAll("#doc-breadcrumbs");
    for(const elem of elems) {
      observer.observe(elem);
    }

    sidebarObserver.observe(document.getElementsByClassName("theme-doc-sidebar-menu menu__list")[0]);

    const sidebarEventManagerCleanup = sidebarEventManager();

    if(log) {
      console.log("%cNOTICE", "color: red; font-size: 64px;");
      console.log("%cThe error:", "font-size: 22px;");
      console.log("%cCannot update a component (`Root`) while rendering a different component (`NavbarHR`). To locate the bad setState() call inside `NavbarHR`, follow the stack trace as described in https://reactjs.org/link/setstate-in-render", "color: gray; font-size: 14px;");
      console.log("%cis not an issue, and can be ignored.", "font-size: 22px;");
      log = false;
    }

    return () => {
      observer.disconnect();
      sidebarObserver.disconnect();
      document.removeEventListener("sidebaroverflowed", sidebarEventManagerCleanup);
    };

  }, );

  return (
    <>
      <DocumentHeightContext.Provider value={docHeightState}>
        <NavbarBreadcrumbHeightContext.Provider value={breadcrumbHeightState}>
          {children}
        </NavbarBreadcrumbHeightContext.Provider>
      </DocumentHeightContext.Provider>
    </>
  );
}
