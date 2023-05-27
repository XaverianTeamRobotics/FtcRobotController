import React, { Fragment } from "react";
import NavbarLayout from "@theme/Navbar/Layout";
import NavbarContent from "@theme/Navbar/Content";

// WARNING: this is an unsafe swizzled component (including all subdirs of Navbar). might break on the next major version

export default function Navbar({ disabled }) {
  if(window.location.pathname.includes("/.")) {
    return <Fragment/>;
  }

  // Disable breadcrumbs in navbar if the current page doesn't support them
  const breadcrumbsDisabled = false;


  // DONT REMOVE! might need later idk
  // if we use the src/pages, we'll need this. however it is currently unstable.
  //
  // please do not use the src/pages directory

  // if(routes.find(value => value.path.replace("/", "") === window.location.pathname.replace("/", "") && !("routes" in value)) !== undefined) {
  //   breadcrumbsDisabled = true;
  // }

  return (
    <Fragment>
      <NavbarLayout disabled={disabled} >
        <NavbarContent disabled={breadcrumbsDisabled} />
      </NavbarLayout>
    </Fragment>
  );
}
