import React, { Fragment } from "react";
import NavbarLayout from "@theme/Navbar/Layout";
import NavbarContent from "@theme/Navbar/Content";
import routes from "@generated/routes";

// WARNING: this is an unsafe swizzled component (including all subdirs of Navbar). might break on the next major version

export default function Navbar({ disabled }) {
  if(window.location.pathname.includes("/.")) {
    return <Fragment/>;
  }
  // Disable breadcrumbs in navbar if the current page doesn't support them
  let breadcrumbsDisabled = false;
  if(routes.find(value => value.path.replace("/", "") === window.location.pathname.replace("/", "")) !== undefined) {
    breadcrumbsDisabled = true;
  }
  return (
    <Fragment>
      <NavbarLayout disabled={disabled} >
        <NavbarContent disabled={breadcrumbsDisabled} />
      </NavbarLayout>
    </Fragment>
  );
}
