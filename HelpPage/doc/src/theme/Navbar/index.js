import React from "react";
import NavbarLayout from "@theme/Navbar/Layout";
import NavbarContent from "@theme/Navbar/Content";

// WARNING: this is an unsafe swizzled component (including all subdirs of Navbar). might break on the next major version

export default function Navbar() {
  return (
    <NavbarLayout>
      <NavbarContent />
    </NavbarLayout>
  );
}
