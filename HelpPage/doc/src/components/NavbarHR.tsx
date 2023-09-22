import React, { FC, Fragment, ReactElement, useContext } from "react";
import { NavbarBreadcrumbHeightContext } from "@site/src/utils/NavbarBreadcrumbHeightContext";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";

interface Props {}

export const NavbarHR: FC<Props> = (): ReactElement => {

  const [ height ] = useContext(NavbarBreadcrumbHeightContext);

  const [ , setDocHeight ] = useContext(DocumentHeightContext);

  const navbarHeight = () => {
    let h = 0;
    const elems = document.querySelectorAll("#nav-with-breadcrumbs");
    elems.forEach(elem => {
      if(h < elem.clientHeight) {
        h = elem.clientHeight;
      }
    });
    return h;
  };

  const findRealHeight = () => {
    const nH = navbarHeight();
    let fH;
    if(height > nH) {
      fH = height + 16;
    }else{
      fH = nH;
    }
    setDocHeight(fH);
    return fH;
  };

  return (
    <Fragment>
      <div style={{ height: findRealHeight() }} className={"navbar-hr"}>
        <div className={"navbar-hr-container"}>
          <div className={"navbar-hr-flex-grow"}>
          </div>
          <hr className={"navbar-hr-hr"}/>
        </div>
      </div>
    </Fragment>
  );
};
