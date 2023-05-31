import React, { Fragment } from "react";
import NavbarLayout from "@theme/Navbar/Layout";
import NavbarContent from "@theme/Navbar/Content";

// WARNING: this is an unsafe swizzled component (including all subdirs of Navbar). might break on the next major version

export default function Navbar({ disabled, screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now }) {
  if(window.location.pathname.includes("/.")) {
    return <Fragment/>;
  }

  return (
    <Fragment>
      <NavbarLayout
        disabled={disabled}
        screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now={screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now}
      >
        <NavbarContent disabled={disabled} />
      </NavbarLayout>
    </Fragment>
  );
}
